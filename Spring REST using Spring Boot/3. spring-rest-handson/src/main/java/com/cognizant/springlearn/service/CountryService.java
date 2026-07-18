package com.cognizant.springlearn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cognizant.springlearn.Country;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;

import java.util.ArrayList;

@Service
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    public Country getCountry(String code) throws CountryNotFoundException {
        LOGGER.info("START getCountry with code: {}", code);
        
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        @SuppressWarnings("unchecked")
        ArrayList<Country> countryList = context.getBean("countryList", ArrayList.class);

        for (Country country : countryList) {
            if (country.getCode().equalsIgnoreCase(code)) {
                LOGGER.debug("Found Country: {}", country);
                LOGGER.info("END getCountry");
                return country;
            }
        }

        LOGGER.error("Country not found for code: {}", code);
        LOGGER.info("END getCountry with exception");
        throw new CountryNotFoundException("Country not found");
    }
}
