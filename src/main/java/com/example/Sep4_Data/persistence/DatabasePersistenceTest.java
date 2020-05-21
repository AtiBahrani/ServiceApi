package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.Sensor;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;


public class DatabasePersistenceTest {
    private DatabasePersistence db;

    @Before
    public void setUp() throws ClassNotFoundException {
        this.db = new DatabasePersistence();
    }

    @Test
    public void addDataTest() throws SQLException {
        Sensor sensor = new Sensor("co2", "ppm",49);
        db.addSensorData(sensor);

    }

    @Test
    public void getData() throws SQLException {
        System.out.println(db.getData());
    }
}