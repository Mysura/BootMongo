package com.example.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.Repository.EmployeeMongoRepository;
import com.example.Repository.EmployeeRepository;
import com.example.model.Employee;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired

	public EmployeeRepository empRepository;
	
	@Autowired
	public EmployeeMongoRepository empMongoRep;

	@Override
	public Collection<Employee> findAll() {
		Collection<Employee> employees = empRepository.findAll();
		return employees;
	}

	@Override
	@Cacheable(value="employee" , key = "#id")
	public Employee findOne(int id) {
		return empRepository.findOne(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@CachePut(value="employee" , key="#result.id")
	public Employee save(Employee emp) {
		Employee createdEmployee =  empRepository.save(emp);
		Employee mongoEmplo =  empMongoRep.save(emp);
		if(createdEmployee.getId() == 6){
			throw new RuntimeException("Exception Occurred Roll backed....");
		}
		return createdEmployee;
	}

	@Override
	@CacheEvict(value="employee" , key = "#id")
	public void delete(int id) {
		empRepository.delete(id);
	}
	
	@Override
	public Employee findByMongoUserName(String userName) {
		return empMongoRep.findByName(userName);
	}
	

}
