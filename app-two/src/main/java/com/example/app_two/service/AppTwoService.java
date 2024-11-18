package com.example.app_two.service;

import com.example.app_two.service.exception.ServiceTwoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppTwoService {

	public void method1() {
		log.info("Service two methow one called");
	}

	public void method2() {
		log.info("Service two method two called");
		throw new ServiceTwoException();
	}

	public void method4() {
		log.info("Service two method four called");
	}

	public void method5() {
		log.info("Service two method five called");
	}

	public void method6() {
		log.info("Service two method six called");
	}

}
