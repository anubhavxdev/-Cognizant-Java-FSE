package com.cognizant.springlearn.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.cognizant.springlearn.model.Department;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDao.class);
    
    private static final List<Department> DEPARTMENT_LIST = new ArrayList<>();

    public DepartmentDao() {
        LOGGER.info("START DepartmentDao Constructor - Loading employee.xml config");
        ApplicationContext context = new ClassPathXmlApplicationContext("employee.xml");
        @SuppressWarnings("unchecked")
        ArrayList<Department> departments = context.getBean("departmentList", ArrayList.class);
        DEPARTMENT_LIST.clear();
        DEPARTMENT_LIST.addAll(departments);
        LOGGER.debug("DEPARTMENT_LIST loaded: {}", DEPARTMENT_LIST);
        LOGGER.info("END DepartmentDao Constructor");
    }

    public List<Department> getAllDepartments() {
        LOGGER.info("START getAllDepartments");
        LOGGER.info("END getAllDepartments");
        return DEPARTMENT_LIST;
    }
}
