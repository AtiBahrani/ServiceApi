package com.example.Sep4_Data.Gateway;
import com.example.Sep4_Data.persistence.DatabaseAdaptor;
import com.example.Sep4_Data.model.Sensor;
import com.example.Sep4_Data.persistence.DatabasePersistence;
import java.sql.SQLException;

public class ConvertingService {
    private String valueTrim;
    private DatabaseAdaptor dba;

    public ConvertingService(){
        this.dba=new DatabasePersistence();
    }
    public void convertData(Message msg) throws SQLException {
        valueTrim = String.valueOf(msg.getData().charAt(0)) + String.valueOf(msg.getData().charAt(1));
        Sensor sensor1 = new Sensor("Temperature Sensor 1","C",Double.parseDouble(valueTrim),msg.getTs());
        dba.addSensorData(sensor1);

        valueTrim = String.valueOf(msg.getData().charAt(2)) + String.valueOf(msg.getData().charAt(3));
        Sensor sensor2 = new Sensor("CO2 Sensor 1","ppm",Double.parseDouble(valueTrim),msg.getTs());
        dba.addSensorData(sensor2);

        valueTrim = String.valueOf(msg.getData().charAt(2)) + String.valueOf(msg.getData().charAt(3));
        Sensor sensor3 = new Sensor("Humidity Sensor 1","%",Double.parseDouble(valueTrim),msg.getTs());
        dba.addSensorData(sensor3);
    }
}