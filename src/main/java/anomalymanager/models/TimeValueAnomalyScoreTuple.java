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
package anomalymanager.models;

import java.time.LocalDateTime;

/**
 * Data model class representing a tuple of a timestamp formatted as a {@link String}
 * and either  a {@link Float} value or a {@link String} value.
 * The value fields are supposed to be used mutually exclusive, i.e. one of them is always null
 * and cointains an anomaly score
 */
@SuppressWarnings("WeakerAccess")
public class TimeValueAnomalyScoreTuple {
    /**
     * The point of time at which the value was valid.
     */
    public LocalDateTime dateTime;

    /**
     * The value if it is representable as a {@link Float}.
     */
    public Float value;


    /**
     * The anomalyscore as a {@link Float}.
     */
    public Float anomalyScore;


    /**
     * The constructor for a {@link TimeValueAnomalyScoreTuple} containing a {@link String} value.
     * @param dateTime The point of time at which the value was valid.
     * @param value The value at the given point of time.
     * @param anomalyScore the anomaly score of the value.
     */
    public TimeValueAnomalyScoreTuple(LocalDateTime dateTime, Float value, Float anomalyScore){
        this.dateTime = dateTime;
        this.value = value;
        this.anomalyScore = anomalyScore;
    }

}
