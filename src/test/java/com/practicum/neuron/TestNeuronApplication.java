package com.practicum.neuron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestNeuronApplication {
	public static void main(String[] args) {
		SpringApplication.from(NeuronApplication::main).with(TestNeuronApplication.class).run(args);
	}
}
