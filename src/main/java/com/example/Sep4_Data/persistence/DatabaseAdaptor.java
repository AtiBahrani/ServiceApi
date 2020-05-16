package com.example.Sep4_Data.persistence;


import com.example.Sep4_Data.model.Sensor;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseAdaptor {

    void addSensorData(Sensor data) throws SQLException;
    List<Sensor> getData() throws SQLException;
    /* void setDefaultValue(DefaultValue defaultValue);
    void addRoom(Room room);
    void addProfile(Profile profile);*/

}
