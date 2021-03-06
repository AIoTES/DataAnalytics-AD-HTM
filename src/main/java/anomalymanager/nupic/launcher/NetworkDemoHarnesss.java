package anomalymanager.nupic.launcher;
import java.util.HashMap;
import java.util.Map;

import anomalymanager.nupic.Parameters;
import anomalymanager.nupic.algorithms.Classifier;
import anomalymanager.nupic.algorithms.SDRClassifier;
import anomalymanager.nupic.encoders.Encoder;
import anomalymanager.nupic.util.Tuple;

/**
 * Encapsulates setup methods which are shared among various tests
 * in the {@link anomalymanager.nupic.network} package.
 * 
 * @author cogmission
 */
public class NetworkDemoHarnesss {
    /**
     * Sets up an Encoder Mapping of configurable values.
     *  
     * @param map               if called more than once to set up encoders for more
     *                          than one field, this should be the map itself returned
     *                          from the first call to {@code #setupMap(Map, int, int, double, 
     *                          double, double, double, Boolean, Boolean, Boolean, String, String, String)}
     * @param n                 the total number of bits in the output encoding
     * @param w                 the number of bits to use in the representation
     * @param min               the minimum value (if known i.e. for the ScalarEncoder)
     * @param max               the maximum value (if known i.e. for the ScalarEncoder)
     * @param radius            see {@link Encoder}
     * @param resolution        see {@link Encoder}
     * @param periodic          such as hours of the day or days of the week, which repeat in cycles
     * @param clip              whether the outliers should be clipped to the min and max values
     * @param forced            use the implied or explicitly stated ratio of w to n bits rather than the "suggested" number
     * @param fieldName         the name of the encoded field
     * @param fieldType         the data type of the field
     * @param encoderType       the Camel case class name minus the .class suffix
     * @return
     */
    public static Map<String, Map<String, Object>> setupMap(
            Map<String, Map<String, Object>> map,
            int n, int w, double min, double max, double radius, double resolution, Boolean periodic,
            Boolean clip, Boolean forced, String fieldName, String fieldType, String encoderType) {

        if(map == null) {
            map = new HashMap<String, Map<String, Object>>();
        }
        Map<String, Object> inner = null;
        if((inner = map.get(fieldName)) == null) {
            map.put(fieldName, inner = new HashMap<String, Object>());
        }

        inner.put("n", n);
        inner.put("w", w);
        inner.put("minVal", min);
        inner.put("maxVal", max);
        inner.put("radius", radius);
        inner.put("resolution", resolution);

        if(periodic != null) inner.put("periodic", periodic);
        if(clip != null) inner.put("clipInput", clip);
        if(forced != null) inner.put("forced", forced);
        if(fieldName != null) inner.put("fieldName", fieldName);
        if(fieldType != null) inner.put("fieldType", fieldType);
        if(encoderType != null) inner.put("encoderType", encoderType);

        return map;
    }

    /**
     * Returns the Hot Gym encoder setup.
     * @return
     */
    public static Map<String, Map<String, Object>> getHotGymFieldEncodingMap() {
        Map<String, Map<String, Object>> fieldEncodings = setupMap(
                null,
                0, // n
                0, // w
                0, 0, 0, 0, null, null, null,
                "timestamp", "datetime", "DateEncoder");
        fieldEncodings = setupMap(
                fieldEncodings, 
                130, 
                42, 
                0, 0, 0, 0.1, null, null, null, 
                "value", "float", "RandomDistributedScalarEncoder");
        
        fieldEncodings.get("timestamp").put(Parameters.KEY.DATEFIELD_DOFW.getFieldName(), new Tuple(1, 1.0)); // Day of week
        fieldEncodings.get("timestamp").put(Parameters.KEY.DATEFIELD_TOFD.getFieldName(), new Tuple(5, 4.0)); // Time of day
        fieldEncodings.get("timestamp").put(Parameters.KEY.DATEFIELD_PATTERN.getFieldName(), "MM/dd/YY HH:mm");
//                fieldEncodings.get("timestamp").put(KEY.DATEFIELD_PATTERN.getFieldName(), "YYYY/MM/dd");
        
        return fieldEncodings;
    }
    
    /**
     * Returns the Hot Gym encoder setup.
     * @return
     */
    public static Map<String, Map<String, Object>> getNetworkDemoFieldEncodingMap() {
        Map<String, Map<String, Object>> fieldEncodings = setupMap(
                null,
                0, // n
                0, // w
                0, 0, 0, 0, null, null, null,
                "timestamp", "datetime", "DateEncoder");
        fieldEncodings = setupMap(
                fieldEncodings, 
                500, 
                21, 
                0, 100, 0, 0.1, null, Boolean.TRUE, null, 
                "value", "float", "ScalarEncoder");
        
        fieldEncodings.get("timestamp").put(Parameters.KEY.DATEFIELD_TOFD.getFieldName(), new Tuple(21,9.5)); // Time of day
        fieldEncodings.get("timestamp").put(Parameters.KEY.DATEFIELD_PATTERN.getFieldName(), "MM/dd/YY HH:mm");
//                fieldEncodings.get("timestamp").put(KEY.DATEFIELD_PATTERN.getFieldName(), "YYYY/MM/dd");
        
        return fieldEncodings;
    }
    
    /**
     * Returns Encoder parameters and meta information for the "Hot Gym" encoder
     * @return
     */
    public static Parameters getNetworkDemoTestEncoderParams() {
        Map<String, Map<String, Object>> fieldEncodings = getNetworkDemoFieldEncodingMap();

        Parameters p = Parameters.getEncoderDefaultParameters();
        p.set(Parameters.KEY.GLOBAL_INHIBITION, true);
        p.set(Parameters.KEY.COLUMN_DIMENSIONS, new int[] { 2048 });
//        p.set(KEY.CELLS_PER_COLUMN, 32);
        p.set(Parameters.KEY.NUM_ACTIVE_COLUMNS_PER_INH_AREA, 40.0);
        p.set(Parameters.KEY.POTENTIAL_PCT, 0.8);
        p.set(Parameters.KEY.SYN_PERM_CONNECTED,0.2);
        p.set(Parameters.KEY.SYN_PERM_ACTIVE_INC, 0.003);
        p.set(Parameters.KEY.SYN_PERM_INACTIVE_DEC, 0.0005);
        p.set(Parameters.KEY.MAX_BOOST, 0.0);
        p.set(Parameters.KEY.INFERRED_FIELDS, getInferredFieldsMap("consumption", SDRClassifier.class));
        
        p.set(Parameters.KEY.MAX_NEW_SYNAPSE_COUNT, 31);
        p.set(Parameters.KEY.INITIAL_PERMANENCE, 0.24);
        p.set(Parameters.KEY.PERMANENCE_INCREMENT, 0.04);
        p.set(Parameters.KEY.PERMANENCE_DECREMENT, 0.008);
        p.set(Parameters.KEY.MIN_THRESHOLD, 13);
        p.set(Parameters.KEY.ACTIVATION_THRESHOLD, 20);
        
        p.set(Parameters.KEY.CLIP_INPUT, true);
        p.set(Parameters.KEY.FIELD_ENCODING_MAP, fieldEncodings);

        return p;
    }
    
    /**
     * @return a Map that can be used as the value for a Parameter
     * object's KEY.INFERRED_FIELDS key, to classify the specified
     * field with the specified Classifier type.
     */
    public static Map<String, Class<? extends Classifier>> getInferredFieldsMap(
            String field, Class<? extends Classifier> classifier) {
        Map<String, Class<? extends Classifier>> inferredFieldsMap = new HashMap<>();
        inferredFieldsMap.put(field, classifier);
        return inferredFieldsMap;
    }

    /**
     * Returns Encoder parameters and meta information for the "Hot Gym" encoder
     * @return
     */
    public static Parameters getHotGymTestEncoderParams() {
        Map<String, Map<String, Object>> fieldEncodings = getHotGymFieldEncodingMap();

        Parameters p = Parameters.getEncoderDefaultParameters();
        p.set(Parameters.KEY.FIELD_ENCODING_MAP, fieldEncodings);

        return p;
    }
    
    /**
     * Parameters and meta information for the "dayOfWeek" encoder
     * @return
     */
    public static Map<String, Map<String, Object>> getDayDemoFieldEncodingMap() {
        Map<String, Map<String, Object>> fieldEncodings = setupMap(
                null,
                8, // n
                3, // w
                0.0, 8.0, 0, 1, Boolean.TRUE, null, Boolean.TRUE,
                "dayOfWeek", "number", "ScalarEncoder");
        return fieldEncodings;
    }

    /**
     * Returns Encoder parameters for the "dayOfWeek" test encoder.
     * @return
     */
    public static Parameters getDayDemoTestEncoderParams() {
        Map<String, Map<String, Object>> fieldEncodings = getDayDemoFieldEncodingMap();

        Parameters p = Parameters.getEncoderDefaultParameters();
        p.set(Parameters.KEY.FIELD_ENCODING_MAP, fieldEncodings);

        return p;
    }
    
    /**
     * Returns the default parameters used for the "dayOfWeek" encoder and algorithms.
     * @return
     */
    public static Parameters getParameters() {
        Parameters parameters = Parameters.getAllDefaultParameters();
//        parameters.set(KEY.INPUT_DIMENSIONS, new int[] { 2048 });
        parameters.set(Parameters.KEY.COLUMN_DIMENSIONS, new int[] { 2048 });
        parameters.set(Parameters.KEY.CELLS_PER_COLUMN, 6);
        
        //SpatialPooler specific
//        parameters.set(KEY.POTENTIAL_RADIUS, 12);//3
        parameters.set(Parameters.KEY.POTENTIAL_PCT, 0.8);//0.5
        parameters.set(Parameters.KEY.GLOBAL_INHIBITION, true);
//        parameters.set(KEY.LOCAL_AREA_DENSITY, -1.0);
        parameters.set(Parameters.KEY.NUM_ACTIVE_COLUMNS_PER_INH_AREA, 40.0);
//        parameters.set(KEY.STIMULUS_THRESHOLD, 1.0);
        parameters.set(Parameters.KEY.SYN_PERM_INACTIVE_DEC, 0.0005);
        parameters.set(Parameters.KEY.SYN_PERM_ACTIVE_INC, 0.003);
//        parameters.set(KEY.SYN_PERM_TRIM_THRESHOLD, 0.05);
//        parameters.set(KEY.SYN_PERM_CONNECTED, 0.2);
//        parameters.set(KEY.MIN_PCT_OVERLAP_DUTY_CYCLES, 0.1);
//        parameters.set(KEY.MIN_PCT_ACTIVE_DUTY_CYCLES, 0.1);
//        parameters.set(KEY.DUTY_CYCLE_PERIOD, 10);
        parameters.set(Parameters.KEY.MAX_BOOST, 0.0);
        parameters.set(Parameters.KEY.SEED, 1956);
        
        //Temporal Memory specific
        parameters.set(Parameters.KEY.INITIAL_PERMANENCE, 0.24);
//        parameters.set(KEY.CONNECTED_PERMANENCE, 0.8);
        parameters.set(Parameters.KEY.MIN_THRESHOLD, 13);
        parameters.set(Parameters.KEY.MAX_NEW_SYNAPSE_COUNT, 31);
        parameters.set(Parameters.KEY.PERMANENCE_INCREMENT, 0.04);
        parameters.set(Parameters.KEY.PERMANENCE_DECREMENT, 0.008);
        parameters.set(Parameters.KEY.ACTIVATION_THRESHOLD, 20);
        
        return parameters;
    }
    
    
}