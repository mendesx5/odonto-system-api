package com.odonto.odonto_system;

import org.springframework.boot.SpringApplication;

public class TestOdontoSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(OdontoSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
