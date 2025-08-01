package com.thukera;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MyRootJwt {
	
	private static final Logger logger = LogManager.getLogger(MyRootJwt.class);

	public static void main(String[] args) {
		SpringApplication.run(MyRootJwt.class, args);
		
		logger.info("## ######## ######## ######### ######## ######## ##########");
		logger.info("######## ###  MY ROOT JWT APPLICATION STARTER  ### ########");
		logger.info("## ######## ######## ######### ######## ######## ##########");
	}

}
