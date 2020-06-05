package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.DefaultValue;
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
//        Sensor sensor = new Sensor("co2", "ppm", 1480, time);
        // Sensor sensor1 = new Sensor("temperature", "celsius", 26, time);
        Sensor sensor2 = new Sensor("humidity", "percent", 48, time);

//        db.addSensorData(sensor);
        //  db.addSensorData(sensor1);
        db.addSensorData(sensor2);

    }

    @Test
    public void getData() throws SQLException {
        System.out.println(db.getData());
    }


    @Test
    public void getDefaultEm() throws SQLException {
        System.out.println(db.getDefaultValueEm());
    }
//    @Test
//    public void addDefaultValue() throws SQLException {
//        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        DefaultValue defaultValue = new DefaultValue(ts, "enable", "Penne", 1200, 87, 75);
//        db.addDefaultValue(defaultValue);
//
//
//    }

}
