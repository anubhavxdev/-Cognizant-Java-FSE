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

    public void updateEmployee(Employee employee) throws com.cognizant.springlearn.service.exception.EmployeeNotFoundException {
        LOGGER.info("START updateEmployee with id: {}", employee.getId());
        for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
            Employee emp = EMPLOYEE_LIST.get(i);
            if (emp.getId().equals(employee.getId())) {
                EMPLOYEE_LIST.set(i, employee);
                LOGGER.debug("Updated Employee: {}", employee);
                LOGGER.info("END updateEmployee");
                return;
            }
        }
        LOGGER.error("Employee not found for id: {}", employee.getId());
        throw new com.cognizant.springlearn.service.exception.EmployeeNotFoundException("Employee not found");
    }

    public void deleteEmployee(int id) throws com.cognizant.springlearn.service.exception.EmployeeNotFoundException {
        LOGGER.info("START deleteEmployee with id: {}", id);
        for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
            Employee emp = EMPLOYEE_LIST.get(i);
            if (emp.getId() == id) {
                EMPLOYEE_LIST.remove(i);
                LOGGER.info("Deleted Employee with id: {}", id);
                LOGGER.info("END deleteEmployee");
                return;
            }
        }
        LOGGER.error("Employee not found for id: {}", id);
        throw new com.cognizant.springlearn.service.exception.EmployeeNotFoundException("Employee not found");
    }
}
