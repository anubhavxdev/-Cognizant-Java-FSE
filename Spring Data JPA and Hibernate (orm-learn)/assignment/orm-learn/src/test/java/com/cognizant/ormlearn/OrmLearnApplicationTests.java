package com.cognizant.ormlearn;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.model.Department;
import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.model.Skill;
import com.cognizant.ormlearn.model.Stock;
import com.cognizant.ormlearn.repository.CountryRepository;
import com.cognizant.ormlearn.service.CountryService;
import com.cognizant.ormlearn.service.DepartmentService;
import com.cognizant.ormlearn.service.EmployeeService;
import com.cognizant.ormlearn.service.SkillService;
import com.cognizant.ormlearn.service.StockService;
import com.cognizant.ormlearn.service.exception.CountryNotFoundException;

@SpringBootTest
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrmLearnApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplicationTests.class);

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SkillService skillService;

    // ==========================================
    // Country Tests (Hands-on 1)
    // ==========================================

    @Test
    @Order(1)
    void testSearchCountryContainingText() {
        LOGGER.info("Start testSearchCountryContainingText");
        List<Country> countries = countryRepository.findByNameContaining("ou");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        // Verify that "ou" is contained in country names
        for (Country c : countries) {
            assertTrue(c.getName().toLowerCase().contains("ou"));
        }
        LOGGER.debug("Countries containing 'ou': {}", countries);
        LOGGER.info("End testSearchCountryContainingText");
    }

    @Test
    @Order(2)
    void testSearchCountryContainingTextSorted() {
        LOGGER.info("Start testSearchCountryContainingTextSorted");
        List<Country> countries = countryRepository.findByNameContainingOrderByNameAsc("ou");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        // Check order
        for (int i = 0; i < countries.size() - 1; i++) {
            assertTrue(countries.get(i).getName().compareTo(countries.get(i + 1).getName()) <= 0);
        }
        LOGGER.debug("Countries containing 'ou' sorted: {}", countries);
        LOGGER.info("End testSearchCountryContainingTextSorted");
    }

    @Test
    @Order(3)
    void testSearchCountryStartingWith() {
        LOGGER.info("Start testSearchCountryStartingWith");
        List<Country> countries = countryRepository.findByNameStartingWith("Z");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        for (Country c : countries) {
            assertTrue(c.getName().startsWith("Z"));
        }
        LOGGER.debug("Countries starting with 'Z': {}", countries);
        LOGGER.info("End testSearchCountryStartingWith");
    }

    // ==========================================
    // Stock Tests (Hands-on 2)
    // ==========================================

    @Test
    @Order(4)
    void testGetFacebookStocksSept2019() throws ParseException {
        LOGGER.info("Start testGetFacebookStocksSept2019");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse("2019-09-01");
        Date end = format.parse("2019-09-30");

        List<Stock> stocks = stockService.getStocksByCodeAndDateBetween("FB", start, end);
        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
        LOGGER.debug("Facebook stocks in Sept 2019: {}", stocks);
        LOGGER.info("End testGetFacebookStocksSept2019");
    }

    @Test
    @Order(5)
    void testGetGoogleStocksGreaterThan() {
        LOGGER.info("Start testGetGoogleStocksGreaterThan");
        List<Stock> stocks = stockService.getGoogleStocksGreaterThan(new BigDecimal("1250"));
        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
        for (Stock s : stocks) {
            assertEquals("GOOGL", s.getCode());
            assertTrue(s.getClose().compareTo(new BigDecimal("1250")) > 0);
        }
        LOGGER.debug("Google stocks > 1250: {}", stocks);
        LOGGER.info("End testGetGoogleStocksGreaterThan");
    }

    @Test
    @Order(6)
    void testGetTop3StocksByVolume() {
        LOGGER.info("Start testGetTop3StocksByVolume");
        List<Stock> stocks = stockService.getTop3StocksByVolume();
        assertNotNull(stocks);
        assertEquals(3, stocks.size());
        LOGGER.debug("Top 3 volume stocks: {}", stocks);
        LOGGER.info("End testGetTop3StocksByVolume");
    }

    @Test
    @Order(7)
    void testGetLowestNetflixStocks() {
        LOGGER.info("Start testGetLowestNetflixStocks");
        List<Stock> stocks = stockService.getLowestNetflixStocks();
        assertNotNull(stocks);
        assertEquals(3, stocks.size());
        for (Stock s : stocks) {
            assertEquals("NFLX", s.getCode());
        }
        LOGGER.debug("Netflix 3 lowest stocks: {}", stocks);
        LOGGER.info("End testGetLowestNetflixStocks");
    }

    // ==========================================
    // Payroll - ManyToOne Relationship (Hands-on 4)
    // ==========================================

    @Test
    @Order(8)
    void testGetEmployee() {
        LOGGER.info("Start testGetEmployee");
        Employee employee = employeeService.get(1);
        assertNotNull(employee);
        assertEquals("John Doe", employee.getName());
        assertNotNull(employee.getDepartment());
        LOGGER.debug("Employee: {}", employee);
        LOGGER.debug("Department: {}", employee.getDepartment());
        LOGGER.info("End testGetEmployee");
    }

    @Test
    @Order(9)
    @Transactional
    @Rollback(true)
    void testAddEmployee() {
        LOGGER.info("Start testAddEmployee");
        Employee employee = new Employee();
        employee.setName("New Hired");
        employee.setSalary(75000.00);
        employee.setPermanent(true);
        employee.setDateOfBirth(new Date());

        Department department = departmentService.get(1);
        assertNotNull(department);
        employee.setDepartment(department);

        employeeService.save(employee);
        assertTrue(employee.getId() > 0);
        LOGGER.debug("Saved Employee: {}", employee);
        LOGGER.info("End testAddEmployee");
    }

    @Test
    @Order(10)
    @Transactional
    @Rollback(true)
    void testUpdateEmployee() {
        LOGGER.info("Start testUpdateEmployee");
        Employee employee = employeeService.get(1);
        assertNotNull(employee);

        Department otherDept = departmentService.get(2);
        assertNotNull(otherDept);

        employee.setDepartment(otherDept);
        employeeService.save(employee);

        Employee updatedEmployee = employeeService.get(1);
        assertEquals(2, updatedEmployee.getDepartment().getId());
        LOGGER.debug("Updated Employee: {}", updatedEmployee);
        LOGGER.info("End testUpdateEmployee");
    }

    // ==========================================
    // Payroll - OneToMany Relationship (Hands-on 5)
    // ==========================================

    @Test
    @Order(11)
    void testGetDepartment() {
        LOGGER.info("Start testGetDepartment");
        Department department = departmentService.get(1);
        assertNotNull(department);
        Set<Employee> employees = department.getEmployeeList();
        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        LOGGER.debug("Department: {}", department);
        LOGGER.debug("Employees: {}", employees);
        LOGGER.info("End testGetDepartment");
    }

    // ==========================================
    // Payroll - ManyToMany Relationship (Hands-on 6)
    // ==========================================

    @Test
    @Order(12)
    void testGetEmployeeSkills() {
        LOGGER.info("Start testGetEmployeeSkills");
        Employee employee = employeeService.get(1);
        assertNotNull(employee);
        Set<Skill> skills = employee.getSkillList();
        assertNotNull(skills);
        assertFalse(skills.isEmpty());
        LOGGER.debug("Skills: {}", skills);
        LOGGER.info("End testGetEmployeeSkills");
    }

    @Test
    @Order(13)
    @Transactional
    @Rollback(true)
    void testAddSkillToEmployee() {
        LOGGER.info("Start testAddSkillToEmployee");
        // Get Employee 2 (Jane Smith) and Skill 2 (Spring Boot)
        Employee employee = employeeService.get(2);
        assertNotNull(employee);

        Skill skill = skillService.get(2);
        assertNotNull(skill);

        employee.getSkillList().add(skill);
        employeeService.save(employee);

        Employee updated = employeeService.get(2);
        assertTrue(updated.getSkillList().contains(skill));
        LOGGER.debug("Updated Employee Skills: {}", updated.getSkillList());
        LOGGER.info("End testAddSkillToEmployee");
    }
}
