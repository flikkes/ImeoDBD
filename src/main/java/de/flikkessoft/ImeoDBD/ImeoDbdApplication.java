package de.flikkessoft.ImeoDBD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ImeoDbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImeoDbdApplication.class, args);
    }

}
