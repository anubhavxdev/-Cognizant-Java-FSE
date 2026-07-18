package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);
        LOGGER.info("Inside main method of SpringLearnApplication");

        displayDate();
        displayCountry();
        displayCountries();
    }

    public static void displayDate() {
        LOGGER.info("START displayDate");
        ApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml");
        SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);
        try {
            Date date = format.parse("31/12/2018");
            LOGGER.debug("Parsed Date: {}", date);
        } catch (Exception e) {
            LOGGER.error("Error parsing date", e);
        }
        LOGGER.info("END displayDate");
    }

    public static void displayCountry() {
        LOGGER.info("START displayCountry");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        
        LOGGER.info("Fetching country bean first time (Prototype Scope demonstration)...");
        Country country = context.getBean("country", Country.class);
        LOGGER.debug("Country : {}", country.toString());

        LOGGER.info("Fetching country bean second time (Prototype Scope demonstration)...");
        Country anotherCountry = context.getBean("country", Country.class);
        LOGGER.debug("Another Country : {}", anotherCountry.toString());
        
        LOGGER.info("Are both references identical? {}", (country == anotherCountry));
        LOGGER.info("END displayCountry");
    }

    public static void displayCountries() {
        LOGGER.info("START displayCountries");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        
        @SuppressWarnings("unchecked")
        ArrayList<Country> countryList = context.getBean("countryList", ArrayList.class);
        LOGGER.debug("Country List: {}", countryList);
        LOGGER.info("END displayCountries");
    }
}
