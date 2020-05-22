package com.example.Sep4_Data.persistence;
import com.example.Sep4_Data.model.Sensor;
import utility.persistence.MyDatabase;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DatabasePersistence implements DatabaseAdaptor {
    private MyDatabase db;

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "machintosh";

    public DatabasePersistence() {
        try {
            this.db = new MyDatabase(DRIVER, URL, USER, PASSWORD);
            System.out.println("connecting...");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is passing the relevant data for sensor and insert into the
     * SensorType, Unit and Measurement tables and makes a query to get the ids from these tables
     * first inserts sensorType_ID as FK into Sensor table then makes a query to get the sensor_ID
     * and with the other ids inserts them into SensorMeasurement and SensorUnit tables as FK.
     *
     * @param data (sensorName, value, timestamp,unit)
     * @throws SQLException
     */
    @Override
    public synchronized void addSensorData(Sensor data) throws SQLException {
        //insert data in three tables sensorType, measurement and unit
        String sensor = "INSERT INTO SEP4_Source.SensorType(sensorName) VALUES (?);";
        String measurement = "INSERT INTO SEP4_Source.Measurement(value,timestamp) VALUES (?,?);";
        String unit = "INSERT INTO SEP4_Source.Unit(unitName) VALUES (?);";

        db.update(sensor, data.getSensorName());
        db.update(measurement, data.getValue(), new Timestamp(data.getTimestamp()));
        db.update(unit, data.getUnitType());

        //key look up for getting the id in order to put to the in between relations
        String sensorTypeId = "SELECT sensorType_ID FROM SEP4_Source.SensorType WHERE sensorName =?;";

        int sID, mID, uID = 0;

        //query to get the sensorType_ID and inserting it to sensor table

        ArrayList<Object[]> sIds = db.query(sensorTypeId, data.getSensorName());
        sID = Integer.parseInt(sIds.get(0)[0].toString());
        String sensorTable = "INSERT INTO SEP4_Source.Sensor(sensorType_ID )VALUES (?);";
        db.update(sensorTable, sID);

        //get sensorId
        int sensor_ID = 0;
        String sensorId = "SELECT sensor_ID FROM SEP4_Source.Sensor join SEP4_Source.SensorType ON " +
                "SensorType.sensorType_ID =Sensor.sensorType_ID WHERE sensorName =?;";

        ArrayList<Object[]> senID = db.query(sensorId, data.getSensorName());
        sensor_ID = Integer.parseInt(senID.get(0)[0].toString());


        //query to get measurementId to put in SensorMeasurement
        String measurementId = "SELECT measurement_ID FROM SEP4_Source.Measurement WHERE value =? and timestamp=?;";

        ArrayList<Object[]> mIds = db.query(measurementId, data.getValue(), new Timestamp(data.getTimestamp()));
        mID = Integer.parseInt(mIds.get(0)[0].toString());
        String sensorMeasurements = "INSERT INTO SEP4_Source.SensorMeasurement(measurement_ID ,sensor_ID)VALUES (?,?);";

        db.update(sensorMeasurements, mID, sensor_ID);

        // get unit_ID
        String unitId = "SELECT unit_ID FROM SEP4_Source.Unit WHERE unitName =?;";

        ArrayList<Object[]> uIds = db.query(unitId, data.getUnitType());
        uID = Integer.parseInt(uIds.get(uIds.size() - 1)[0].toString());

        String sensorUnits = "INSERT INTO SEP4_Source.SensorUnit(unit_ID,sensor_ID )VALUES (?,?);";


        db.update(sensorUnits, uID, sensor_ID);

    }

    /**
     * The method is loading all the data for the sensor from Sensor, Unit and Measurement tables
     * creates a list with these information
     *
     * @return list
     * @throws SQLException
     */
    @Override
    public List<Sensor> getData() throws SQLException {
        //join between 6 tables to get the data for sensor object
        String dataSql = "SELECT  unitName ,sensorName,Measurement.value , Measurement.timestamp " +
                "FROM SEP4_Source.Unit JOIN SEP4_Source.SensorUnit ON SEP4_Source.Unit.unit_ID = SEP4_Source.SensorUnit.unit_ID " +
                "JOIN SEP4_Source.Sensor ON SEP4_Source.SensorUnit.sensor_ID = Sensor.sensor_ID " +
                "JOIN SEP4_Source.SensorType ON SEP4_Source.Sensor.sensorType_ID = SensorType.sensorType_ID " +
                "INNER JOIN SEP4_Source.SensorMeasurement s ON Sensor.sensor_ID = s.sensor_ID " +
                "INNER JOIN SEP4_Source.Measurement ON s.measurement_ID = Measurement.measurement_ID;";

        List<Sensor> list = new ArrayList<>();
        //select query
        ArrayList<Object[]> dataList = db.query(dataSql);

        for (int i = 0; i < dataList.size(); i++) {
            Object[] array = dataList.get(i);
            //instantiate measurement by casting the object to double type and timestamp
            String str = String.valueOf(array[2]);
            double value = Double.valueOf(str).doubleValue();
            String ts = String.valueOf(array[3]);
            Timestamp timestamp = Timestamp.valueOf(ts);
            long datetime = timestamp.getTime();
           //create the sensor object
            Sensor sensorInfo = new Sensor(String.valueOf(array[1]), String.valueOf(array[0]),value,datetime);
            list.add(sensorInfo);
        }
        return list;

    }

   /* @Override
    public void setDefaultValue(DefaultValue defaultValue) {

    }

    @Override
    public void addRoom(Room room) {

    }

    @Override
    public void addProfile(Profile profile) {

    }*/
}
