package com.bht;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.bht.async.MsThreadPoolExecutor;

@SpringBootApplication
public class MicrositeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrositeApplication.class, args);
	}
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	@Bean
	public MsThreadPoolExecutor custompoolExecutor() {
		MsThreadPoolExecutor MsThreadPoolExecutor = new MsThreadPoolExecutor(10, 25, 10, TimeUnit.SECONDS,
				"Customthread", new LinkedBlockingDeque<>(10));
		return MsThreadPoolExecutor;

	}
}
