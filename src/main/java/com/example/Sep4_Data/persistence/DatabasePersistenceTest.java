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
    public void addDataTest() throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        long time = (long) ts.getTime();
        // Sensor sensor = new Sensor("co2", "ppm", 3305, time);
        // Sensor sensor1 = new Sensor("temperature", "celsius", 45.5, time);
        Sensor sensor2 = new Sensor("humidity", "percent", 68, time);

        db.addSensorData(sensor2);
        System.out.println("1 ");
        // db.addSensorData(sensor1);
        //  System.out.println("2");

        // db.addSensorData(sensor2);
        //System.out.println("3");

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
