package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.Country;
import com.cognizant.springlearn.service.CountryService;
import com.cognizant.springlearn.service.exception.CountryNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    @RequestMapping("/country")
    public Country getCountryIndia() {
        LOGGER.info("START getCountryIndia");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("in", Country.class);
        LOGGER.debug("Country India: {}", country);
        LOGGER.info("END getCountryIndia");
        return country;
    }

    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        LOGGER.info("START getAllCountries");
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        @SuppressWarnings("unchecked")
        List<Country> countries = context.getBean("countryList", ArrayList.class);
        LOGGER.debug("All Countries: {}", countries);
        LOGGER.info("END getAllCountries");
        return countries;
    }

    @GetMapping({"/countries/{code}", "/country/{code}"})
    public Country getCountry(@PathVariable("code") String code) throws CountryNotFoundException {
        LOGGER.info("START getCountry for code: {}", code);
        Country country = countryService.getCountry(code);
        LOGGER.debug("Country found: {}", country);
        LOGGER.info("END getCountry");
        return country;
    }
}
