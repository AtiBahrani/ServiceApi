package com.example.Sep4_Data.service;

import com.example.Sep4_Data.model.Sensor;
import com.example.Sep4_Data.persistence.DatabaseAdaptor;
import com.example.Sep4_Data.persistence.DatabasePersistence;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class DBController {
    DatabaseAdaptor db=new DatabasePersistence();
    @GetMapping("/sensor")
    public List<Sensor> index() throws SQLException {
        return db.getData();
    }

}
