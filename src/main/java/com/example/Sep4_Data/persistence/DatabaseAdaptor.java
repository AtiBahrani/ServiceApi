package com.example.Sep4_Data.persistence;

import com.example.Sep4_Data.model.EmDefaultValue;
import com.example.Sep4_Data.model.Sensor;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseAdaptor {

    void addSensorData(Sensor data) throws SQLException;



    List<Sensor> getData() throws SQLException;

    List<EmDefaultValue> getDefaultValueEm()throws SQLException; //replace the name and type of the method with the code you have
    /*void addRoom(Room room);
    void addProfile(Profile profile);*/
}