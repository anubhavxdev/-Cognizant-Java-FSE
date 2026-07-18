package com.cognizant.springlearn.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.cognizant.springlearn.model.Employee;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);
    
    private static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();

    public EmployeeDao() {
        LOGGER.info("START EmployeeDao Constructor - Loading employee.xml config");
        ApplicationContext context = new ClassPathXmlApplicationContext("employee.xml");
        @SuppressWarnings("unchecked")
        ArrayList<Employee> employees = context.getBean("employeeList", ArrayList.class);
        EMPLOYEE_LIST.clear();
        EMPLOYEE_LIST.addAll(employees);
        LOGGER.debug("EMPLOYEE_LIST loaded: {}", EMPLOYEE_LIST);
        LOGGER.info("END EmployeeDao Constructor");
    }

    public List<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees");
        LOGGER.info("END getAllEmployees");
        return EMPLOYEE_LIST;
    }
}
