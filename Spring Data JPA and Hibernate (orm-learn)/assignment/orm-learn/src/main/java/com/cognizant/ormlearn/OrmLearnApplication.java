package com.cognizant.ormlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.model.Department;
import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.model.Skill;
import com.cognizant.ormlearn.model.Stock;
import com.cognizant.ormlearn.service.CountryService;
import com.cognizant.ormlearn.service.DepartmentService;
import com.cognizant.ormlearn.service.EmployeeService;
import com.cognizant.ormlearn.service.SkillService;
import com.cognizant.ormlearn.service.StockService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    private static CountryService countryService;
    private static StockService stockService;
    private static EmployeeService employeeService;
    private static DepartmentService departmentService;
    private static SkillService skillService;

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "h2");
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        countryService = context.getBean(CountryService.class);
        stockService = context.getBean(StockService.class);
        employeeService = context.getBean(EmployeeService.class);
        departmentService = context.getBean(DepartmentService.class);
        skillService = context.getBean(SkillService.class);

        // Run Hands-on 1 Tests
        testCountryContainingText();
        testCountryContainingTextSorted();
        testCountryStartingWith();

        // Run Hands-on 2 Tests
        try {
            testGetFacebookStocksSept2019();
        } catch (Exception e) {
            LOGGER.error("Error testing FB stocks", e);
        }
        testGetGoogleStocksGreaterThan();
        testGetTop3StocksByVolume();
        testGetLowestNetflixStocks();

        // Run Hands-on 4 Tests
        testGetEmployee();
        try {
            testAddEmployee();
        } catch (Exception e) {
            LOGGER.error("Error adding employee", e);
        }
        testUpdateEmployee();

        // Run Hands-on 5 Tests
        testGetDepartment();

        // Run Hands-on 6 Tests
        testAddSkillToEmployee();
    }

    // Helper methods for displaying Country and Stock data
    private static void displayCountries(List<Country> countries) {
        for (Country country : countries) {
            LOGGER.info(String.format("%-8s %s", country.getCode(), country.getName()));
        }
    }

    private static void displayStocks(List<Stock> stocks) {
        LOGGER.info("+---------+------------+---------+----------+-----------+");
        LOGGER.info("| st_code | st_date    | st_open | st_close | st_volume |");
        LOGGER.info("+---------+------------+---------+----------+-----------+");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Stock stock : stocks) {
            LOGGER.info(String.format("| %-7s | %-10s | %7.2f | %8.2f | %9d |",
                    stock.getCode(),
                    format.format(stock.getDate()),
                    stock.getOpen().doubleValue(),
                    stock.getClose().doubleValue(),
                    stock.getVolume()));
        }
        LOGGER.info("+---------+------------+---------+----------+-----------+");
    }

    // Hands-on 1 methods
    private static void testCountryContainingText() {
        LOGGER.info("Start: Searching countries containing 'ou'");
        List<Country> countries = countryService.findCountriesByName("ou");
        displayCountries(countries);
        LOGGER.info("End: Searching countries containing 'ou'");
    }

    private static void testCountryContainingTextSorted() {
        LOGGER.info("Start: Searching countries containing 'ou' sorted");
        List<Country> countries = countryService.findCountriesByNameSorted("ou");
        displayCountries(countries);
        LOGGER.info("End: Searching countries containing 'ou' sorted");
    }

    private static void testCountryStartingWith() {
        LOGGER.info("Start: Searching countries starting with 'Z'");
        List<Country> countries = countryService.findCountriesStartingWith("Z");
        displayCountries(countries);
        LOGGER.info("End: Searching countries starting with 'Z'");
    }

    // Hands-on 2 methods
    private static void testGetFacebookStocksSept2019() throws Exception {
        LOGGER.info("Start: Facebook stocks in Sept 2019");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse("2019-09-01");
        Date end = format.parse("2019-09-30");
        List<Stock> stocks = stockService.getStocksByCodeAndDateBetween("FB", start, end);
        displayStocks(stocks);
        LOGGER.info("End: Facebook stocks in Sept 2019");
    }

    private static void testGetGoogleStocksGreaterThan() {
        LOGGER.info("Start: Google stocks > 1250");
        List<Stock> stocks = stockService.getGoogleStocksGreaterThan(new BigDecimal("1250"));
        displayStocks(stocks);
        LOGGER.info("End: Google stocks > 1250");
    }

    private static void testGetTop3StocksByVolume() {
        LOGGER.info("Start: Top 3 volume stocks");
        List<Stock> stocks = stockService.getTop3StocksByVolume();
        displayStocks(stocks);
        LOGGER.info("End: Top 3 volume stocks");
    }

    private static void testGetLowestNetflixStocks() {
        LOGGER.info("Start: Lowest Netflix stocks");
        List<Stock> stocks = stockService.getLowestNetflixStocks();
        displayStocks(stocks);
        LOGGER.info("End: Lowest Netflix stocks");
    }

    // Hands-on 4 methods
    private static void testGetEmployee() {
        LOGGER.info("Start: Get Employee 1");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.debug("Department:{}", employee.getDepartment());
        LOGGER.debug("Skills:{}", employee.getSkillList());
        LOGGER.info("End: Get Employee 1");
    }

    private static void testAddEmployee() throws Exception {
        LOGGER.info("Start: Add Employee");
        Employee employee = new Employee();
        employee.setName("David Miller");
        employee.setSalary(55000.0);
        employee.setPermanent(true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        employee.setDateOfBirth(format.parse("1994-08-25"));
        
        Department department = departmentService.get(1);
        employee.setDepartment(department);
        
        employeeService.save(employee);
        LOGGER.debug("Saved Employee:{}", employee);
        LOGGER.info("End: Add Employee");
    }

    private static void testUpdateEmployee() {
        LOGGER.info("Start: Update Employee");
        Employee employee = employeeService.get(1);
        Department department = departmentService.get(2);
        employee.setDepartment(department);
        employeeService.save(employee);
        LOGGER.debug("Updated Employee:{}", employee);
        LOGGER.info("End: Update Employee");
    }

    // Hands-on 5 methods
    private static void testGetDepartment() {
        LOGGER.info("Start: Get Department 1");
        Department department = departmentService.get(1);
        LOGGER.debug("Department:{}", department);
        LOGGER.debug("Employees:{}", department.getEmployeeList());
        LOGGER.info("End: Get Department 1");
    }

    // Hands-on 6 methods
    private static void testAddSkillToEmployee() {
        LOGGER.info("Start: Add Skill to Employee");
        Employee employee = employeeService.get(2);
        Skill skill = skillService.get(2);
        employee.getSkillList().add(skill);
        employeeService.save(employee);
        LOGGER.debug("Saved Employee Skill list:{}", employee.getSkillList());
        LOGGER.info("End: Add Skill to Employee");
    }
}
