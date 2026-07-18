package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.model.Employee;
import com.cognizant.springlearn.service.EmployeeService;

import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

@RestController
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        LOGGER.info("START getAllEmployees Endpoint");
        List<Employee> list = employeeService.getAllEmployees();
        LOGGER.debug("Employees fetched: {}", list);
        LOGGER.info("END getAllEmployees Endpoint");
        return list;
    }

    @PutMapping("/employees")
    public void updateEmployee(@RequestBody @Valid Employee employee) throws com.cognizant.springlearn.service.exception.EmployeeNotFoundException {
        LOGGER.info("START updateEmployee Endpoint");
        employeeService.updateEmployee(employee);
        LOGGER.info("END updateEmployee Endpoint");
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable("id") int id) throws com.cognizant.springlearn.service.exception.EmployeeNotFoundException {
        LOGGER.info("START deleteEmployee Endpoint for id: {}", id);
        employeeService.deleteEmployee(id);
        LOGGER.info("END deleteEmployee Endpoint");
    }
}
