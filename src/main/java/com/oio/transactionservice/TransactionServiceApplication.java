package com.oio.transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
public class TransactionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}
