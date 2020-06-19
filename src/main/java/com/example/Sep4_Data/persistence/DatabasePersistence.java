package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.*;
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
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1193";

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
        sensor_ID = Integer.parseInt(senID.get(senID.size() - 1)[0].toString());
        //query to get measurementId to put in SensorMeasurement
        ArrayList<Object[]> mIds = db.query(DatabaseQueries.GET_MEASUREMENT_ID,
                data.getValue(), new Timestamp(data.getTimestamp()));
        mID = Integer.parseInt(mIds.get(mIds.size() - 1)[0].toString());
        // get unit_ID
        ArrayList<Object[]> uIds = db.query(DatabaseQueries.GET_UNIT_ID, data.getUnitType());
        uID = Integer.parseInt(uIds.get(uIds.size() - 1)[0].toString());

        db.update(DatabaseQueries.INSERT_INTO_ROOMHASMEASUREMENT, 1, mID);
        db.update(DatabaseQueries.INSERT_INTO_SENSORMEASUREMENT, mID, sensor_ID);
        db.update(DatabaseQueries.INSERT_INTO_SENSORUNIT, sensor_ID, uID);
        //update the data warehouse after each update
        updateDW(data);
    }

    /**
     * This method gets three values for each environmental parameters as the average value which comes
     * from the android application with a timestamp. these values with the timestamp will be stored
     * in the source database as report which later can be retrieved and  sent to the front end.
     *
     * @param report (average values with timestamp)
     * @throws SQLException
     */
    @Override
    public synchronized void addReport(Report report) throws SQLException {
        int reportId = 0;
        Timestamp timestamp = Timestamp.valueOf(report.getTimestamp());

        int rID = 1; //representing room_ID assuming there is only one room having id=1
        db.update(DatabaseQueries.INSERT_INTO_REPORT, report.getCo2_value(), report.getHumidity_value(), report.getTemperature_value(), timestamp);
        ArrayList<Object[]> reportIds = db.query(DatabaseQueries.GET_REPORT_ID, timestamp);
        reportId = Integer.parseInt(reportIds.get(reportIds.size() - 1)[0].toString());
        db.update(DatabaseQueries.INSERT_INTO_ROOMREPORT, rID, reportId);
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
    public List<Parameter> getData() throws SQLException {
        List<Parameter> list = new ArrayList<>();
        //select query
        ArrayList<Object[]> dataList = db.query(DatabaseQueries.GET_SENSOR_FROM_DW);

        for (int i = 0; i < dataList.size(); i++) {
            Object[] array = dataList.get(i);
            //instantiate measurement by casting the object to double type and timestamp
            String str = String.valueOf(array[2]);
            double value = Double.parseDouble(str);
            String ts = (array[3]) + " " + (array[4]);
            Timestamp timestamp = Timestamp.valueOf(ts);
            String hour = "";
            if (timestamp.getHours() < 10)
                hour = "0" + timestamp.getHours();
            else
                hour = "" + timestamp.getHours();
            String minute = "";
            if (timestamp.getMinutes() < 10)
                minute = "0" + timestamp.getMinutes();
            else
                minute = "" + timestamp.getMinutes();
            String datetime = "0" + timestamp.toLocalDateTime().getDayOfMonth() + "-0" + (timestamp.getMonth() + 1) + "-" +
                    timestamp.toLocalDateTime().getYear() + " " + hour + ":" + minute;
            //create the sensor object
            Parameter sensorInfo = new Parameter(String.valueOf(array[0]), String.valueOf(array[1]), value, datetime);
            list.add(sensorInfo);
        }
        return list;
    }

    @Override
    public List<Parameter> getLastParam() throws SQLException {
        List<Parameter> all = new ArrayList<>(getData());
        List<Parameter> last = new ArrayList<>();
        for (int i = all.size() - 3; i < all.size(); i++) {
            last.add(all.get(i));
        }
        return last;
    }

    @Override
    public List<Parameter> getDataFromTo(String from, String to) throws SQLException, ParseException {
        List<Parameter> all = new ArrayList<>(getData());
        List<Parameter> filtered = new ArrayList<>();
        Date datefrom = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(from);
        Date dateto = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(to);

        for (int i = 0; i < all.size(); i++) {
            Date date = new SimpleDateFormat("dd-MM-yyyy hh:mm").parse(all.get(i).getTimestamp());
            if (date.before(dateto) || date.after(datefrom)) {
                filtered.add(all.get(i));
            }
        }
        return filtered;
    }

    /**
     * This method is getting the average values for the three environmental parameters
     * on the requested date from the source database due to lack of time reports are not added to
     * the data warehouse and currently it is being stored in and retrieved from the source database.
     *
     * @param timestamp
     * @return list of reports made with average values for a specific timestamp
     * @throws SQLException
     */
    @Override
    public List<Report> getReport(String timestamp) throws SQLException {
        Timestamp t = Timestamp.valueOf(timestamp);

        List<Report> list = new ArrayList<>();
        //select query
        ArrayList<Object[]> reports = db.query(DatabaseQueries.GET_REPORT_FOR_DATE, t);

        for (int i = 0; i < reports.size(); i++) {
            Object[] array = reports.get(i);
            String co2 = String.valueOf(array[0]);
            double avgCo2 = Double.parseDouble(co2);
            String humidity = String.valueOf(array[1]);
            double avgHumidity = Double.parseDouble(humidity);
            String temperature = String.valueOf(array[2]);
            double avgTemp = Double.parseDouble(temperature);
            String time = (array[3] + "");

            Report report = new Report(avgCo2, avgHumidity, avgTemp, time);
            list.add(report);
        }
        return list;
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
        db.update(DatabaseQueries.DELETE_FROM_ROOM_DIM_STAGE);
    }

    public void updateDW() throws SQLException {
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
        db.update(DatabaseQueries.LAST_UPDATE, new Timestamp(System.currentTimeMillis()));
        db.update(DatabaseQueries.DELETE_FROM_TEMP_FACT);
        db.update(DatabaseQueries.DELETE_FROM_SENSOR_DIM_STAGE);
        db.update(DatabaseQueries.DELETE_FROM_ROOM_DIM_STAGE);
    }
}