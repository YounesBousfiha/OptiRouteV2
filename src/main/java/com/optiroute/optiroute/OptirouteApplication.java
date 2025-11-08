package com.optiroute.optiroute;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableAsync
public class OptirouteApplication implements CommandLineRunner {


    private final ChatClient chatClient;


    @Autowired
    public OptirouteApplication(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(OptirouteApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        String promptText = "Hello, what day is today ?";

         String requestSpec = chatClient.prompt(promptText).call().content();

         log.warn(requestSpec);
    }
}
