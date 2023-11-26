package com.address;

import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RequestCounterListener implements ServletContextListener {
	private int totalRequests = 0;
	private Set<String> uniqueUsers = new HashSet<>();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute("totalRequests", totalRequests);
		servletContext.setAttribute("uniqueUsers", uniqueUsers);

		System.out.println("Application Initialized. Request Counters set to 0.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Application Destroyed.");
	}

	public void incrementTotalRequests() {
		totalRequests++;
	}

	public void addUniqueUser(String username) {
		uniqueUsers.add(username);
	}

	public int getTotalRequests() {
		return totalRequests;
	}

	public Set<String> getUniqueUsers() {
		return uniqueUsers;
	}
}
