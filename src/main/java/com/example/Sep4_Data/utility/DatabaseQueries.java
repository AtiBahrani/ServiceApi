package com.example.Sep4_Data.utility;



public class DatabaseQueries {
    /*INSERT INTO SOURCE DATABASE*/
    public static final String INSERT_INTO_SENSORTYPE = "INSERT INTO SensorType(sensorName) VALUES (?);";
    public static final String INSERT_INTO_MEASUREMENT = "INSERT INTO Measurement(value,timestamp) VALUES (?,?);";
    public static final String INSERT_INTO_UNIT = "INSERT INTO Unit(unitName) VALUES (?);";
    public static final String INSERT_INTO_SENSOR = "INSERT INTO Sensor(sensorType_ID )VALUES (?);";
    public static final String INSERT_INTO_SENSORMEASUREMENT = "INSERT INTO SensorMeasurement(measurement_ID ,sensor_ID)VALUES (?,?);";
    public static final String INSERT_INTO_SENSORUNIT = "INSERT INTO SensorUnit(unit_ID,sensor_ID )VALUES (?,?);";
    public static final String INSERT_INTO_ROOM = "INSERT INTO Room (roomType) VALUES (?);";
    public static final String INSERT_INTO_ROOMHASMEASUREMENT = "INSERT INTO RoomHasMeasurement (room_ID,measurement_ID) VALUES(?,?);";
    public static final String INSERT_INTO_ACTUATOR = "INSERT INTO Actuator (actuatorName) VALUES (?);";
    public static final String INSERT_INTO_ACTOATORSTATE = "INSERT INTO ActuatorState(actuator_ID,state) VALUES (?,?);";
    public static final String INSERT_INTO_ROOMACTUATORSTATE = "INSERT INTO RoomActuatorState(room_ID,state_ID) VALUES (?,?);";
    public static final String INSERT_INTO_PROFILE = "INSERT INTO Profile(name,co2_value,humidity_value,temperature_value) VALUES (?,?,?);";
    public static final String INSERT_INTO_STATE = "INSERT INTO ProfileState (profile_ID,state,timestamp) VALUES (?,?,?);";
    public static final String INSERT_INTO_PROFILEDEFAULTVALUESTATE = "INSERT INTO ProfileDefaultValueState(room_ID,state_ID) VALUES (?,?);";
    //KEY LOOP-UP TO GET IDs in source database
    public static final String GET_SENSORTYPE_ID = "SELECT sensorType_ID FROM SensorType WHERE sensorName =?;";
    public static final String GET_SENSOR_ID = "SELECT sensor_ID FROM Sensor join SensorType ON " +
            "SensorType.sensorType_ID = Sensor.sensorType_ID WHERE sensorName =?;";
    public static final String GET_MEASUREMENT_ID = "SELECT measurement_ID FROM Measurement WHERE value =? and timestamp=?;";
    public static final String GET_UNIT_ID = "SELECT unit_ID FROM Unit WHERE unitName =?;";
    public static final String GET_ACTUATOR_ID = "SELECT actuator_ID FROM Actuator WHERE actuatorName=?;";
    public static final String GET_ACTUATORSTATE_ID = "SELECT state_ID FROM ActuatorSate where actuator_ID =?;";
    public static final String GET_PROFILE_ID = "SELECT profile_ID FROM Profile WHERE name=?;";
    public static final String GET_STATE_ID_FROM_STATE = "SELECT state_ID FROM State WHERE profile_ID=? AND state='enable' AND timestamp=?;";

    /*dw*/
    public static final String GET_SENSOR_FROM_DW = "SELECT  sensorName,unitName,measurement_fact_dw.value,date_dim_dw.calendardate ,time_dim_dw.time_format " +
            "FROM sensor_dim_dw JOIN measurement_fact_dw " +
            "ON sensor_dim_dw.S_ID = measurement_fact_dw.S_ID " +
            "JOIN date_dim_dw ON  date_dim_dw.D_ID = measurement_fact_dw.D_ID " +
            "join time_dim_dw on time_dim_dw.t_id=measurement_fact_dw.t_id;";

    //incremental load to DW
    public static final String INSERT_INTO_MEASURE_FACT_STAGE = "INSERT INTO temp_measure_fact( room_id, sensor_id, \"_value\",_timestamp)" +
            "SELECT room.room_ID, sensor.sensor_ID, measurement.value, measurement.timestamp" +
            "FROM  room join roomhasmeasurement " +
            "on room.room_id = roomhasmeasurement.room_id join measurement " +
            "on roomhasmeasurement.measurement_id = measurement.measurement_id " +
            "join sensormeasurement on measurement.measurement_id = sensormeasurement.measurement_id" +
            "join sensor on sensormeasurement.sensor_id = sensor.sensor_id " +
            "where measurement.timestamp >(SELECT lastUpdate FROM last_updated_dw);";

    // stage dimension
    public static final String INSERT_INTO_SENSOR_DIM_STAGE = "INSERT INTO sensor_dim_stage(sensor_ID,sensorName,unitName)" +
            "SELECT sensor.sensor_ID,sensortype.sensorName,unit.unitName " +
            "FROM unit join sensorunit on unit.unit_id = sensorunit.unit_id " +
            "join sensor on sensorunit.sensor_id = sensor.sensor_id join sensortype " +
            "on sensor.sensortype_id = sensortype.sensortype_id;";
    public static final String INSERT_INTO_ROOM_DIM_STAGE = "INSERT INTO room_dim_stage(room_ID,roomType)" +
            "SELECT room_ID,roomType FROM room;";

    //POPULATE THE DATA WAREHOUSE WITH CHANGES ///check the queries to get date from lastupdated table
    public static final String INSERT_INTO_SENSOR_DIM_DW = "INSERT INTO sensor_dim_dw(sensor_id, sensorname,validFrom, validTo)" +
            "  SELECT sensor_dim_stage.sensor_id,sensor_dim_stage.sensorname,(select last_updated.lastUpdate from Last_updated),'9999-12-31'" +
            "    FROM sensor_dim_stage;";
    public static final String INSERT_INTO_ROOM_DIM_DW = "INSERT INTO room_dim_dw (room_id, roomtype,validFrom,validTo)" +
            "  SELECT room_dim_stage.room_id, room_dim_stage.roomtype,(select last_updated.lastUpdate from Last_updated),'9999-12-31'" +
            "    FROM room_dim_stage;";
    //KEY LOOK-UP
    public static final String R_ID_LOOKUP = "UPDATE temp_measure_fact" +
            " SET R_ID=( SELECT R_ID FROM room_dim_dw WHERE room_ID = temp_measure_fact.room_id );";

    public static final String S_ID_LOOKUP = "UPDATE temp_measure_fact" +
            " SET S_ID =( SELECT S_ID FROM sensor_dim_dw" +
            " WHERE sensor_dim_dw.sensor_id=temp_measure_fact.sensor_id );";

    public static final String D_ID_LOOKUP = " UPDATE  temp_measure_fact" +
            " SET D_ID =( SELECT D_ID  FROM date_dim_dw d " +
            "WHERE d.calendardate = temp_measure_fact._timestamp::date);";

    public static final String T_ID_LOOKUP = "UPDATE temp_measure_fact" +
            " SET T_ID = (SELECT T_ID FROM time_dim_dw t" +
            " WHERE temp_measure_fact._timestamp:: time in(select t.time_format::time));";

    // populate warehouse fact table
    public static final String INSERT_INTO_MEASUREMENT_FACT_DW = "INSERT INTO measurement_fact_dw (r_id, s_id, t_id, d_id, \"value\",\"timpstamp\")" +
            "  SELECT temp_measure_fact.r_id" +
            "  ,temp_measure_fact.s_id" +
            "  ,temp_measure_fact.t_id" +
            "  ,temp_measure_fact.d_id" +
            "  ,temp_measure_fact._value" +
            "  ,temp_measure_fact._timestamp" +
            "    FROM temp_measure_fact;";
    //update the last_updated_dw table after each incremental load
    public static final String LAST_UPDATE = "UPDATE  last_updated_dw SET lastUpdate=?;";
    //delete data from staging area after each load
    public static final String DELETE_FROM_TEMP_FACT = "DELETE FROM temp_measure_fact CASCADE;";
    public static final String DELETE_FROM_SENSOR_DIM_STAGE="DELETE FROM sensor_dim_stage CASCADE;";
    public static final String DELETE_FROM_ROOM_DIM_STAGE= "DELETE FROM room_dim_stage CASCADE;";

    /* get the last value as default value for the parameters */
    public static final String GET_DEFAULT_VALUE_FOR_IOT="SELECT sensor_dim_dw.sensorname,measurement_fact_dw.\"_value\" from " +
            "sensor_dim_dw join measurement_fact_dw on sensor_dim_dw.s_id= measurement_fact_dw.s_id order by " +
            "measurement_fact_dw._timestamp desc limit 3;";


}
