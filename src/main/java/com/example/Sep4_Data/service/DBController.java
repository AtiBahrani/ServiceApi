package com.example.Sep4_Data.service;

import com.example.Sep4_Data.model.EmDefaultValue;
import com.example.Sep4_Data.model.Report;
import com.example.Sep4_Data.model.Sensor;
import com.example.Sep4_Data.model.SensorWithSDate;
import com.example.Sep4_Data.persistence.DatabaseAdaptor;
import com.example.Sep4_Data.persistence.DatabasePersistence;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@RestController
public class DBController {
    DatabaseAdaptor db = new DatabasePersistence();

    @GetMapping("/parameters")
    public List<SensorWithSDate> getSensorInfo() throws SQLException {
        System.out.println("Data sent to client.");
        return db.getData();
    }

    @GetMapping("/parametersFiltered")
    public List<SensorWithSDate> getFilteredDateSensorInfo(String timestampFrom, String timestampTo) throws SQLException, ParseException {
        System.out.println("Data sent to client.");
        return db.getDataFromTo(timestampFrom, timestampTo);
    }

    @GetMapping("/defaultValue")
    public List<EmDefaultValue> getDfValue() throws SQLException {
        System.out.println("Data sent to client.");
        return db.getDefaultValueEm();
    }

    @GetMapping("/getReport")
    public List<Report> getReport(String timestamp) throws SQLException {
        System.out.println("Data sent to client.");
        return db.getReport(timestamp);
    }

    @PostMapping("/sensor")
    public void sendData(@RequestBody String data) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Sensor sensor = null;
        try {
            sensor = mapper.readValue(data, Sensor.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        db.addSensorData(sensor);
        System.out.println("Sensor info added to database for " + sensor.getSensorName());
    }

    @PostMapping("/report")
    public void addReport(@RequestBody Report report) throws SQLException {
        db.addReport(report);
    }

}