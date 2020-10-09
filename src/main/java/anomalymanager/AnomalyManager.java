/*
 * Copyright 2017-2020 Fraunhofer Institute for Computer Graphics Research IGD
 *
 * Licensed under the GNU AFFERO GENERAL PUBLIC LICENSE, Version 3, 19 November 2007
 * You may not use this work except in compliance with the Version 3 Licence.
 * You may obtain a copy of the Licence at:
 * https://www.gnu.org/licenses/agpl-3.0.html
 *
 * See the Licence for the specific permissions and limitations under the Licence.
 */
package anomalymanager;

import anomalymanager.models.AnomalySensorDataModel;
import anomalymanager.models.TimeValueAnomalyScoreTuple;
import anomalymanager.nupic.launcher.AnomalyDetector;
import anomalymanager.nupic.network.Inference;
import anomalymanager.nupic.network.Network;
import anomalymanager.nupic.network.Persistence;
import anomalymanager.nupic.network.PersistenceAPI;
import com.fasterxml.jackson.annotation.JsonProperty;
import exceptions.*;
import rx.Observer;
//import visualmanager.models.SensorDataModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

class DeviceData {
    @JsonProperty("data")
    List<ValueTimeTuple> deviceData;
}
class ValueTimeTuple {
    @JsonProperty("time")
    String time;
    @JsonProperty("value")
    float value;
}

/**
 * This class receives REST-Requests from frontend for the retrieval of sensor data with anomaly score.
 */
@Path("/anomalyManager")
public class AnomalyManager {

    float currentValue = 0f;
    String currentTime = "";

    boolean waitforprocess = false;
    static HashMap<String, PersistenceAPI> map = new HashMap<>(5);
    AnomalyDetector anomalyDetector;
    Network network;
    boolean train;
    private PersistenceAPI api = Persistence.get();;

    /**
     * This is the default constructor for the {@link AnomalyManager} class.
     */

    public AnomalyManager(){
    }
//    api = Persistence.get();
//    visualManager = new VisualManager();


    //Test
//    /**
//     * Starts a HTM Network
//     *
//     * @param deviceId The internal Id of the target device.
//
//     * @throws PlatformDataProcessingException If there was an error retrieving the Item data.
//     */
//    @Path("/start")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String start(
//            @QueryParam("deviceId") String deviceId
//    )
//            throws PlatformDataProcessingException, IOException, InterruptedException, MissingDatabaseEntryException, ActionCycleException {
//
//        try{
//           return "Hello";
//
//        }  catch(Exception ex) {
//            throw new IOException("An error occurred while starting Anomalynetwork");
//        }
//    }

    /**
     * Starts a HTM Network
     *
     * @param deviceId The internal Id of the target device.
     *
     * @throws PlatformDataProcessingException If there was an error retrieving the Item data.
     */
    @Path("/startNetwork")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String startNetwork(
            @QueryParam("deviceId") String deviceId,
            DeviceData trainingData
            )
            throws NetworkAlreadyExistsException, PlatformDataProcessingException, IOException, InterruptedException, MissingDatabaseEntryException, ActionCycleException {

        /*while(waitforprocess){
            if(!waitforprocess)
                break;
        }*/
        waitforprocess = true;

        if(isNetworkInDatabase(deviceId)){
            throw new NetworkAlreadyExistsException("A network of this device already exists.");
        }

        try{
            anomalyDetector = new AnomalyDetector();
            network = anomalyDetector.setupNetwork();
            int counter = 0;
            train  = true;
            // Subscribes and receives Network Output
            network.observe().subscribe(new Observer<Inference>() {
                @Override public void onCompleted() { /* Any finishing touches after Network is finished */
                train = false;}
                @Override public void onError(Throwable e) { /* Handle your errors here */ }
                @Override public void onNext(Inference inf) {

                    /* This is the OUTPUT of the network after each input has been "reacted" to. */
                }
            });

            while(train){
                for(ValueTimeTuple tuple: trainingData.deviceData){
                    network.getPublisher().onNext(tuple.time + "," + tuple.value);
                    Thread.sleep(7);
                    counter ++;

                    // if to many training data, just do 4000 data
                    if(counter >= 4000){
                        counter = 0;
                        break;
                    }
                }
                train = false;
            }
            api.store(network);
            map.put(deviceId, api);
            waitforprocess = false;
           // savePersistenceNetwork(api, deviceId);
            return "Network was created.";

        }  catch(Exception ex) {
            throw new IOException("An error occurred while starting Anomalynetwork");
        }
    }

    /**
     * Gets the most recent data available for a sensor with AnomalyScore.
     *
     * @param deviceId The internal Id of the target device.
     * @throws PlatformDataProcessingException If there was an error retrieving the Item data.
     * @throws PlatformNotFoundException If the given deviceId contains an unknown platform Id.
     */
    @Path("/checkAnomaly")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AnomalySensorDataModel checkAnomaly(
            @QueryParam("deviceId") String deviceId,
            DeviceData data)
            throws PlatformDataProcessingException, IOException, InterruptedException {

        /*while(waitforprocess){
            if(!waitforprocess)
                break;
        }*/
        waitforprocess = true;

        AnomalySensorDataModel anomalySensorDataModel = new AnomalySensorDataModel();

        try{
            network = map.get(deviceId).load();
            if(network == null){
                throw new NetworkDoesNotExistException("There is no network for this device. Please create one.");
            }
            network.restart();

            // Subscribes and receives Network Output
            network.observe().subscribe(new Observer<Inference>() {
                @Override public void onCompleted() { /* Any finishing touches after Network is finished */ }
                @Override public void onError(Throwable e) { /* Handle your errors here */}
                @Override public void onNext(Inference inf) {
                   // for(ValueTimeTuple tuple : data.deviceData) {
                        float score =(float) inf.getAnomalyScore();
                       // score = 3f;
                        anomalySensorDataModel.Values.add(new TimeValueAnomalyScoreTuple(LocalDateTime.parse(currentTime), currentValue, score));
                    //}
                    /* This is the OUTPUT of the network after each input has been "reacted" to. */
                }
            });
            for(ValueTimeTuple tuple : data.deviceData){
                currentTime = tuple.time;
                currentValue = tuple.value;
                network.getPublisher().onNext(anomalyDateTimeFormat(currentTime) + "," + currentValue);
                Thread.sleep(700);
            }

            //time to calculate, not to be less
            Thread.sleep(90);
            api.store(network);
            map.put(deviceId, api);
            waitforprocess = false;
            return anomalySensorDataModel;
        } catch(Exception ex) {
            throw new PlatformDataProcessingException("An error occurred while processing Data. Try to create a network for this device first. Exception: " + ex.toString());
        }
        //time to calculate, not to be less
        //Thread.sleep(20);
       // map.put(deviceId, api);
       // waitforprocess = false;

    }






//    /**
//     * Gets the most recent data available for a sensor with AnomalyScore.
//     *
//     * @param deviceId The internal Id of the target device.
//     * @param sensorId The external Id of the target sensor that belongs to the target device.
//     * @return Returns a {@link SensorDataModel} for the target sensor.
//     * @throws PlatformDataProcessingException If there was an error retrieving the Item data.
//     * @throws PlatformNotFoundException If the given deviceId contains an unknown platform Id.
//     */
//    @Path("{userId}/{projectId}/getSensorWithAnomalyScoreDataSocket")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public float getSensorWithAnomalyScoreDataSocket(
//            @QueryParam("deviceId") String deviceId,
//            @QueryParam("sensorId") String sensorId,
//            @QueryParam("result") float score,
//            @QueryParam("dateTime") String dateTime,
//            @PathParam("userId") String userId,
//            @PathParam("projectId") String projectId)
//            throws PlatformDataProcessingException, PlatformNotFoundException, IOException, InterruptedException {
//
//        while(waitforprocess){
//            if(!waitforprocess)
//                break;
//        }
//        waitforprocess = true;
//        AnomalySensorDataModel anomalySensorDataModel = new AnomalySensorDataModel();
//        final float[] anoamlyscore = {-1.0f};
//        try{
//            network = map.get(sensorId).load();
//            network.restart();
//
//            // Subscribes and receives Network Output
//            network.observe().subscribe(new Observer<Inference>() {
//                @Override public void onCompleted() { /* Any finishing touches after Network is finished */ }
//                @Override public void onError(Throwable e) { /* Handle your errors here */ }
//                @Override public void onNext(Inference inf) {
//                    anoamlyscore[0] = (float) inf.getAnomalyScore();
//                    /* This is the OUTPUT of the network after each input has been "reacted" to. */
//                }
//            });
//            dateTime = dateTime;
//
//
//            network.getPublisher().onNext(anomalyDateTimeFormat(dateTime) + "," + score);
//        } catch(Exception ex) {
//            throw new PlatformDataProcessingException("An error occurred while processing HTM " + ex.toString());
//        }
//        Thread.sleep(9);
//        api.store(network);
//        map.put(sensorId, api);
//        waitforprocess = false;
//        return anoamlyscore[0];
//    }

//    /**
//     * Gets the most recent data available for a sensor.
//     *
//     * @param deviceId The internal Id of the target device.
//     * @return Returns a {@link SensorDataModel} for the target sensor.
//     * @throws PlatformDataProcessingException If there was an error retrieving the Item data.
//     * @throws PlatformNotFoundException If the given deviceId contains an unknown platform Id.
//     */
//    @Path("/deleteNetwork")
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    public void deleteNetwork(
//            @QueryParam("deviceId") String deviceId)
//            throws PlatformDataProcessingException, PlatformNotFoundException, IOException, InterruptedException {
//
//
//        try{//todo Janina: delete method!
//           // network = map.get(deviceId).load();
//           //deletePersistenceNetwork(deviceId);
//        } catch(Exception ex) {
//            throw new PlatformDataProcessingException("An error occurred while processing deleteNetwork" + ex.toString());
//        }
//    }


    //TODO better way to format "yyyy-MM-ddTHH:mm:ss" format to "MM/dd/YY HH:mm"

    //"yyyy-MM-ddTHH:mm:ss" format to "MM/dd/YY HH:mm"
    private String anomalyDateTimeFormat(String datetime){

        String year;
        String month;
        String day;
        String hours;
        String minutes;
        String result;

        if(datetime != null) {
            year = datetime.substring(2,4);
            month = datetime.substring(5,7);
            day = datetime.substring(8,10);
            hours = datetime.substring(11, 13);
            minutes = datetime.substring(14,16);
            result = month + "/" + day + "/" + year + " " + hours + ":" + minutes;
            return result;
        }
        return null;
    }

public float getScore(String dateTime, String entityId, String value) throws PlatformDataProcessingException, InterruptedException {

    AnomalySensorDataModel anomalySensorDataModel = new AnomalySensorDataModel();
    final float[] anoamlyscore = {-1.0f};
    try{
        network = map.get(entityId).load();
        network.restart();

        // Subscribes and receives Network Output
        network.observe().subscribe(new Observer<Inference>() {
            @Override public void onCompleted() { /* Any finishing touches after Network is finished */ }
            @Override public void onError(Throwable e) { /* Handle your errors here */ }
            @Override public void onNext(Inference inf) {
                anoamlyscore[0] = (float) inf.getAnomalyScore();
                /* This is the OUTPUT of the network after each input has been "reacted" to. */
            }
        });
        network.getPublisher().onNext(anomalyDateTimeFormat(dateTime) + "," + value);
    } catch(Exception ex) {
        throw new PlatformDataProcessingException("An error occurred while processing HTM " + ex.toString());
    }
    Thread.sleep(20);
    api.store(network);
    map.put(entityId, api);
    return anoamlyscore[0];
}

    public boolean isNetworkInDatabase(String entityId) throws PlatformDataProcessingException, InterruptedException, MissingDatabaseEntryException, ActionCycleException {
        for (String key : map.keySet()) {
            if(entityId.equals(key)){
                return true;
            }
        }
        return false;
    }

}
