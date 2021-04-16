package com.ibm.wh.siam.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication( scanBasePackages = "com.ibm.wh.siam" )
@EnableAutoConfiguration
@EnableScheduling
public class PostgresPocApplication
{
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger( PostgresPocApplication.class );

    public static void main(String[] args) {
        SpringApplication.run(PostgresPocApplication.class, args);
    }
}
