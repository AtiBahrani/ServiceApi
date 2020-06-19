package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.DefaultValue;
import com.example.Sep4_Data.model.Report;
import com.example.Sep4_Data.model.Sensor;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;


public class DatabasePersistenceTest {
    private DatabasePersistence db;

    @Before
    public void setUp() throws ClassNotFoundException {
        this.db = new DatabasePersistence();
    }


    @Test
    public void addDataTest() throws SQLException, InterruptedException {
        long milis = System.currentTimeMillis() - 86400000;
        long name = 0;
        for (int i = 0; i < 288; i++) {
            Timestamp ts = new Timestamp(milis + name);
            Sensor sensor = new Sensor("CO2", "ppm", 1360, ts.getTime());
            db.addSensorData(sensor);
            Thread.sleep(1000);
            Sensor sensor1 = new Sensor("temperature", "celsius", 45.6, ts.getTime());
            db.addSensorData(sensor1);
            Thread.sleep(1000);
            Sensor sensor2 = new Sensor("humidity", "percent", 62.7, ts.getTime());
            db.addSensorData(sensor2);
            Thread.sleep(1000);
            name+=300000;
        }
    }

    @Test
    public void TestInsertDB()throws SQLException{
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Sensor sensor = new Sensor("CO2", "ppm", 1120, ts.getTime());
        Sensor sensor1 = new Sensor("temperature", "celsius", 52, ts.getTime());
        Sensor sensor2 = new Sensor("humidity", "percent", 59.3, ts.getTime());

        db.addSensorData(sensor2);
    }
    @Test
    public void updateDW() throws SQLException {
        db.updateDW();
    }

    @Test
    public void getData() throws SQLException {
        System.out.println(db.getData());
    }


    @Test
    public void getDefaultEm() throws SQLException {
        System.out.println(db.getDefaultValueEm());
    }

    @Test
    public void addReport() throws SQLException {
        Report r = new Report(2250, 62.85, 41.36, "2020-06-05 23:28:26");
        db.addReport(r);
    }

    @Test
    public void getReport() throws SQLException {
        System.out.println(db.getReport("2020-06-05 23:38:26"));
    }


}
