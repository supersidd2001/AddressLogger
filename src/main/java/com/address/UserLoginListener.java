package com.address;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@WebListener
public class UserLoginListener implements ServletContextListener {
	private Map<String, Double> userBalances = new HashMap<>();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute("userBalances", userBalances);

		System.out.println("UserLoginListener Initialized.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("UserLoginListener Destroyed.");
	}

	public void updateUserBalance(String username) {
		if (!userBalances.containsKey(username)) {
			userBalances.put(username, 1000.0); // Initial amount for a new user
		} else {
			double currentBalance = userBalances.get(username);
			double updatedBalance = currentBalance + (0.10 * currentBalance); // Add 10%
			userBalances.put(username, updatedBalance);
		}
	}

	public double getUserBalance(String username) {
		//This is for non mapped user
		return userBalances.getOrDefault(username, 0.0);
	}

	public void setCookieBalance(HttpServletResponse response, String username, double balance) {
		// Update the user's balance before setting the cookie
		updateUserBalance(username);

		// Retrieve the updated balance
		double userBalance = getUserBalance(username);

		// Create a cookie and set it with the user's updated balance
		Cookie balanceCookie = new Cookie("userBalance", String.valueOf(userBalance));
		balanceCookie.setMaxAge(-1); // Cookie will be deleted when the browser is closed
		balanceCookie.setPath("/"); // Set the path to the root context
		response.addCookie(balanceCookie);
	}

}
