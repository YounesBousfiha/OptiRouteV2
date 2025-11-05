package com.optiroute.optiroute;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
public class OptirouteApplication {
    public static void main(String[] args) {
        SpringApplication.run(OptirouteApplication.class, args);
	}
}
