package com.address;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/WelcomeServlet")
public class WelcomeServlet extends HttpServlet {

	private static final long serialVersionUID = 8503505837578821895L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		RequestCounterListener requestCounterListener = (RequestCounterListener) getServletContext()
				.getAttribute("RequestCounterListener");

		int totalRequests = requestCounterListener.getTotalRequests();
		Set<String> uniqueUsers = requestCounterListener.getUniqueUsers();
		PrintWriter out = response.getWriter();

		out.println("Total Requests: " + totalRequests);
		out.println("Unique Users: " + uniqueUsers.size());

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");

		if (username != null) {
			out.println("Welcome, " + username + "!");

			// Retrieve user balance from the cookie
			double userBalance = getUserBalanceFromCookie(request, username);
			out.println("Your account balance: $" + userBalance);

			// User Addresses
			displayUserAddresses(response, username);

			// Add Address Form
			out.println("<form action='AddAddressServlet' method='post'>");
			out.println("  <label for='address'>Address:</label>");
			out.println("  <input type='text' id='address' name='address' required>");
			out.println("  <input type='submit' value='Add Address'>");
			out.println("</form>");

		} else {
			response.sendRedirect("login.html");
		}
	}

	private double getUserBalanceFromCookie(HttpServletRequest request, String username) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userBalance".equals(cookie.getName()) && username.equals(cookie.getValue())) {
					try {
						return Integer.parseInt(cookie.getValue());
					} catch (NumberFormatException e) {
						System.err.println("\n\nSession not found\n\n");// Error message on console
					}
				}
			}
		}
		return 0.0;
	}

	private void displayUserAddresses(HttpServletResponse response, String username) throws IOException {
		String filePath = getServletContext().getRealPath("/META-INF/userdata/") + "/" + username + "-address.txt";

		response.getWriter().println("<h3>Your Addresses:</h3>");
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				response.getWriter().println(line + "<br><br>");
			}
			response.getWriter().print("<br><br><br>");
		} catch (IOException e) {
			// Handle file reading exception
			System.err.println("Address NOT Found");
			;
		}
	}
}
