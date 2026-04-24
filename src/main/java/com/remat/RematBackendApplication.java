package com.remat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RematBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RematBackendApplication.class, args);
    }

}
