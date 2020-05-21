package com.example.Sep4_Data;

import com.example.Sep4_Data.Gateway.EmbeddedService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApiApplication.class, args);
		EmbeddedService emb = new EmbeddedService("wss://iotnet.teracom.dk/app?token=vnoSwwAAABFpb3RuZXQudGVyYWNvbS5ka0q7HnnqktfJAYtYolL9nnQ=");
		System.out.println("Service started.");

		while(true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
