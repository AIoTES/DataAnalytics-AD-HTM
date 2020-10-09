# HTM anomaly detection

To detect and visualize anomalies in the data of a device, the anomaly detection widgets were developed. Here is a stand-alone version, which was integrated into IDE-ClickDigital. 
In IDE-ClickDigital it functions like a visualization widget, but additionally shows with coloured markers where anomaly is recognized. Behind the widget, a learning algorithm based on Hierarchical Temporary Memory (HTM) is used. This algorithm attempts to simulate a human brain biologically and is not a neural network. It learns time-based patterns in unlabeled data on a continuous basis and is because of this context-related recognition well suited for IoT-based data.

The dockerized version of the stand-alone code used for the HTM andomaly detection widget can be tried out here independent of the IDE-ClickDigital installation.

## Getting started
POST localhost:8080/ClickDigitalBackend_war_exploded/anomalyManager/startNetwork
Parameters: key = deviceId, value = someidentifier (just an example, free to choose whatever value)
Body: is training data in following JSON format:
{
    "data": [
        {
            "time": "2018-05-07T20:34:25",
            "value": 200.0
        },
        {
            "time": "2019-05-29T15:57:34",
            "value": 93.3
        }]
}
Example
Sent POST using Postman (https://www.getpostman.com/), see also screenshots post-startnetwork1 and post-startnetwork2.
localhost:8080/ClickDigitalBackend_war_exploded/anomalyManager/startNetwork?deviceId=netident1
Load as training data the content of "taxirides_trainingdata_okt2weeks.txt" as JSON Body

Result Message:
"Network was created."

## Deployment
To run HTM_AnomalyManager:
Create a new folder.
Download "docker-compose.yml" into that folder.
In command line browse to the new created folder containing the "docker-compose.yml" file.
Login to activage docker repository with command: "docker repository docker-activage.satrd.es/"
Start the HTMAnomalyManager with the command "docker-compose up -d"

#Start HTM network (train data with HTM and create the model)
POST localhost:8080/ClickDigitalBackend_war_exploded/anomalyManager/startNetwork
Parameters: key = deviceId, value = someidentifier (just an example, free to choose whatever value)
Body: is training data in following JSON format:
```
{
    "data": [
        {
            "time": "2018-05-07T20:34:25",
            "value": 200.0
        },
        {
            "time": "2019-05-29T15:57:34",
            "value": 93.3
        }]
}
```
Example
Sent POST using Postman (https://www.getpostman.com/), see also screenshots post-startnetwork1 and post-startnetwork2.
localhost:8080/ClickDigitalBackend_war_exploded/anomalyManager/startNetwork?deviceId=netident1
Load as training data the content of "taxirides_trainingdata_okt2weeks.txt" as JSON Body

Result Message:
"Network was created."


#HTM anomalyscore (receive anomylscore for training data)
POST localhost:8080/ClickDigitalBackend_war_exploded/anomalyManager/checkAnomaly
Parameters: key = deviceId, value = someidentifier (has to be the same of the previously created model)
Body: is test data in same JSON format as before

RESULT should be a JSON like this indicating an anomalyscore:
```
{
    "Values": [
        {
			"dateTime": {
				"monthValue": 5, 
				"dayOfMonth": 7, 
				"dayOfWeek": "MONDAY", 
				"month": "MAY", 
				"year": 2018, 
				"dayOfYear": 127, 
				"nano": 0, 
				"hour": 20, 
				minute": 34, 
				"second": 25, 
				"chronology": { 
					"id": "ISO", 
					"calendarType": "iso8601"
				}
            },
            "value": 200,
            "anomalyScore": 1
        } ]
}
```
Example
Sent POST using Postman (https://www.getpostman.com/), see also screenshots post-checkanomaly1 and post-checkanomaly2.

localhost:8080/ClickDigitalBackend_war_exploded/anomalyManager/checkAnomaly?deviceId=netident1
Load as test data the content of "taxirides_testdata-nov3days.txt" as JSON Body

Result Message snippet: 
```
{
    "Values": [
        {
            "dateTime": {
                "monthValue": 11,
                "nano": 0,
                "hour": 0,
                "minute": 40,
                "second": 0,
                "year": 2014,
                "month": "NOVEMBER",
                "dayOfMonth": 1,
                "dayOfWeek": "SATURDAY",
                "dayOfYear": 305,
                "chronology": {
                    "calendarType": "iso8601",
                    "id": "ISO"
                }
            },
            "value": 24946,
            "anomalyScore": 1
        },
        {
            "dateTime": {
                "monthValue": 11,
                "nano": 0,
                "hour": 1,
                "minute": 22,
                "second": 55,
                "year": 2014,
                "month": "NOVEMBER",
                "dayOfMonth": 1,
                "dayOfWeek": "SATURDAY",
                "dayOfYear": 305,
                "chronology": {
                    "calendarType": "iso8601",
                    "id": "ISO"
                }
            },
            "value": 23490,
            "anomalyScore": 1
        },
...

```

## Further information

The anomaly score has to be interpreted. We got good results with the following interpretation:
```
score <= 0.84: normal value 
score > 0.84: at risk to become an anomaly 
score > 0.89: anomaly
```

## Credits

This software is maintained by: 
* Silvia Rus <silvia.rus@igd.fraunhofer.de> 

## Licence
```
 Copyright 2017-2020 Fraunhofer Institute for Computer Graphics Research IGD
 
 Licensed under the GNU AFFERO GENERAL PUBLIC LICENSE, Version 3, 19 November 2007
 
 You may not use this work except in compliance with the Version 3 Licence. 
 You may obtain a copy of the Licence at: 
 
 https://www.gnu.org/licenses/agpl-3.0.html

 See the Licence for the specific permissions and limitations under the Licence. 