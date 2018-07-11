package com.crypto.raModule.RA.Module.NoSQL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class RaModuleNoSqlApplication {

	public static void main(String[] args) {

		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		SpringApplication.run(RaModuleNoSqlApplication.class, args);
	}
}
