package com.cognizant.springlearn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.springlearn.dao.DepartmentDao;
import com.cognizant.springlearn.model.Department;

import java.util.List;

@Service
public class DepartmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentDao departmentDao;

    @Transactional
    public List<Department> getAllDepartments() {
        LOGGER.info("START getAllDepartments");
        List<Department> list = departmentDao.getAllDepartments();
        LOGGER.info("END getAllDepartments");
        return list;
    }
}
