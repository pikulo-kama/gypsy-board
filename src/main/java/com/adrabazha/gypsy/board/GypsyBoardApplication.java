package com.adrabazha.gypsy.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GypsyBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(GypsyBoardApplication.class, args);
	}

}
