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

        Sensor sensor = new Sensor("humidity", "percent",65,15901370);
        db.addSensorData(sensor);

    }

    @Test
    public void getData() throws SQLException {
        System.out.println(db.getData());
    }

    @Test
    public void addDefaultValue()throws SQLException{
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        DefaultValue defaultValue= new DefaultValue(ts,"enable","Penne",1200,87,75);
        db.addDefaultValue(defaultValue);


    }

}
