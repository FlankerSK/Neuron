package com.practicum.neuron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NeuronApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeuronApplication.class, args);
	}

}
