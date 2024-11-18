package com.example.app_one.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class UserSessionsRepository {

	@Getter
	private Integer activeUsers;

	public void setActiveUsers(Integer activeUsers) {
		this.activeUsers = activeUsers;
	}

}
