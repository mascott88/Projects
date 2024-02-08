package com.app.bank.service;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Person;

public interface SystemAccess {
	
	public void ROCBANKMAIN() throws BusinessException;
	public void CustomerSystemAccess(Person person);
	public void EmployeeSystemAccess(Person person) throws BusinessException;
	
}
