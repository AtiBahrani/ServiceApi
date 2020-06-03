package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.DefaultValue;
import com.example.Sep4_Data.model.EmDefaultValue;
import com.example.Sep4_Data.model.Sensor;
import com.example.Sep4_Data.model.SensorWithSDate;
import com.example.Sep4_Data.utility.DatabaseQueries;

import utility.persistence.MyDatabase;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabasePersistence implements DatabaseAdaptor {
    private MyDatabase db;

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://balarama.db.elephantsql.com:5432/lwavwwgi";
    private static final String USER = "lwavwwgi";
    private static final String PASSWORD = "B1jwM3F8_fo289D9wXPxNHLEgVDYXZxr";

    public DatabasePersistence() {
        try {
            this.db = new MyDatabase(DRIVER, URL, USER, PASSWORD);
            System.out.println("connecting...");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("connection error");
        }
    }


    /**
     * This method is passing the relevant data for sensor and insert into the
     * SensorType, Unit and Measurement tables and makes a query to get the ids from these tables
     * first inserts sensorType_ID as FK into Sensor table then makes a query to get the sensor_ID
     * and with the other ids inserts them into SensorMeasurement and SensorUnit tables as FK.
     * and update the data warehouse
     *
     * @param data (sensorName, value, timestamp,unit)
     * @throws SQLException
     */
    @Override
    public synchronized void addSensorData(Sensor data) throws SQLException {
        int sID, mID, uID = 0;
        //insert data in three tables sensorType, measurement and unit
        db.update(DatabaseQueries.INSERT_INTO_SENSORTYPE, data.getSensorName());
        db.update(DatabaseQueries.INSERT_INTO_MEASUREMENT, data.getValue(), new Timestamp(data.getTimestamp()));
        db.update(DatabaseQueries.INSERT_INTO_UNIT, data.getUnitType());
        ArrayList<Object[]> sIds = db.query(DatabaseQueries.GET_SENSORTYPE_ID, data.getSensorName());
        sID = Integer.parseInt(sIds.get(sIds.size() - 1)[0].toString());
        db.update(DatabaseQueries.INSERT_INTO_SENSOR, sID);
        //get sensorId
        int sensor_ID = 0;
        ArrayList<Object[]> senID = db.query(DatabaseQueries.GET_SENSOR_ID, data.getSensorName());
        sensor_ID = Integer.parseInt(senID.get(0)[0].toString());
        //query to get measurementId to put in SensorMeasurement
        ArrayList<Object[]> mIds = db.query(DatabaseQueries.GET_MEASUREMENT_ID, data.getValue(), new Timestamp(data.getTimestamp()));
        mID = Integer.parseInt(mIds.get(mIds.size() - 1)[0].toString());
        db.update(DatabaseQueries.INSERT_INTO_SENSORMEASUREMENT, mID, sensor_ID);
        // get unit_ID
        ArrayList<Object[]> uIds = db.query(DatabaseQueries.GET_UNIT_ID, data.getUnitType());
        uID = Integer.parseInt(uIds.get(uIds.size() - 1)[0].toString());
        db.update(DatabaseQueries.INSERT_INTO_SENSORUNIT, uID, sensor_ID);
        //update the data warehouse after each update
        updateDW(data);
    }

    @Override
    public synchronized void addDefaultValue(DefaultValue defaultValue) throws SQLException {
        int psID, pID = 0;
        int rID = 1; //representing room_ID assuming there is only one room having id=1
        db.update(DatabaseQueries.INSERT_INTO_PROFILE, defaultValue.getParameterType(), defaultValue.getCo2(), defaultValue.getHumidity(), defaultValue.getTemperature());
        ArrayList<Object[]> pIds = db.query(DatabaseQueries.GET_PROFILE_ID, defaultValue.getParameterType());
        pID = Integer.parseInt(pIds.get(pIds.size() - 1)[0].toString());
        System.out.println(pID);
        db.update(DatabaseQueries.INSERT_INTO_STATE, pID, defaultValue.getState(), defaultValue.getTimestamp());
        ArrayList<Object[]> psIds = db.query(DatabaseQueries.GET_STATE_ID_FROM_STATE, pID, defaultValue.getTimestamp());
        psID = Integer.parseInt(psIds.get(psIds.size() - 1)[0].toString());
        db.update(DatabaseQueries.INSERT_INTO_PROFILEDEFAULTVALUESTATE, rID, psID);


    }


    /**
     * The method is loading all the data for the sensor which are name, unit, value
     * and timestamp from the data warehouse
     * and creates a list with these information
     *
     * @return list
     * @throws SQLException
     */
    @Override
    public List<SensorWithSDate> getData() throws SQLException {

        List<SensorWithSDate> list = new ArrayList<>();
        //select query
        ArrayList<Object[]> dataList = db.query(DatabaseQueries.GET_SENSOR_FROM_DW);

        for (int i = 0; i < dataList.size(); i++) {
            Object[] array = dataList.get(i);
            //instantiate measurement by casting the object to double type and timestamp
            String str = String.valueOf(array[2]);
            double value = Double.parseDouble(str);
            String ts = (array[3]) + " " + (array[4]);
            Timestamp timestamp = Timestamp.valueOf(ts);
            String datetime = timestamp.getDay() + "-" + timestamp.getMonth() + "-" + timestamp.getYear() + " " + timestamp.getHours() + ":" + timestamp.getMinutes();
            //create the sensor object
            SensorWithSDate sensorInfo = new SensorWithSDate(String.valueOf(array[0]), String.valueOf(array[1]), value, datetime);
            list.add(sensorInfo);
        }
        return list;
    }

    @Override
    public List<SensorWithSDate> getDataFromTo(String from, String to) throws SQLException, ParseException {
        List<SensorWithSDate> all = new ArrayList<>(getData());
        List<SensorWithSDate> filtered = new ArrayList<>();
        Date datefrom = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(from);
        Date dateto = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(to);

        for (int i = 0; i < all.size(); i++) {
            Date date = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(all.get(i).getTimestamp());
            if (date.before(dateto) && date.after(datefrom)) {
                filtered.add(all.get(i));
            }
        }
        return filtered;
    }

    /**
     * The method is getting sensorName with its value from the data warehouse showing
     * the last values added in the warehouse representing the default value from the sensors
     *
     * @return list (sensor name with values showing the default
     * value that has received from the device)
     * @throws SQLException
     */

    @Override
    public List<EmDefaultValue> getDefaultValueEm() throws SQLException {
        List<EmDefaultValue> list = new ArrayList<>();
        //select query
        ArrayList<Object[]> dvList = db.query(DatabaseQueries.GET_DEFAULT_VALUE_FOR_IOT);
        for (int i = 0; i < dvList.size(); i++) {
            Object[] array = dvList.get(i);
            String v = String.valueOf(array[1]);
            double value = Double.valueOf(v).doubleValue();
            EmDefaultValue info = new EmDefaultValue(String.valueOf(array[0]), value);
            list.add(info);
        }
        return list;
    }

    /**
     * this is private method for the ETL process and incremental load of the data warehouse
     * the order is to extract data into staging area
     * populating the data warehouse dimensions
     * key look up
     * populating the fact table in the data warehouse
     * and update the last_updated table with the last time load was done
     * and deleting data from the staging area to be ready for the next load.
     *
     * @param sensor
     * @throws SQLException
     */
    private void updateDW(Sensor sensor) throws SQLException {
        db.update(DatabaseQueries.INSERT_INTO_MEASURE_FACT_STAGE);
        db.update(DatabaseQueries.INSERT_INTO_SENSOR_DIM_STAGE);
        db.update(DatabaseQueries.INSERT_INTO_ROOM_DIM_STAGE);
        db.update(DatabaseQueries.INSERT_INTO_SENSOR_DIM_DW);
        db.update(DatabaseQueries.INSERT_INTO_ROOM_DIM_DW);
        db.update(DatabaseQueries.R_ID_LOOKUP);
        db.update(DatabaseQueries.S_ID_LOOKUP);
        db.update(DatabaseQueries.D_ID_LOOKUP);
        db.update(DatabaseQueries.T_ID_LOOKUP);
        db.update(DatabaseQueries.INSERT_INTO_MEASUREMENT_FACT_DW);
        db.update(DatabaseQueries.LAST_UPDATE, (Timestamp) new Timestamp(sensor.getTimestamp()));
        db.update(DatabaseQueries.DELETE_FROM_TEMP_FACT);
        db.update(DatabaseQueries.DELETE_FROM_SENSOR_DIM_STAGE);
        db.update(DatabaseQueries.DELETE_FROM_TEMP_FACT);
    }


}
