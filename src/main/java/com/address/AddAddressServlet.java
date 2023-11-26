package com.address;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AddAddressServlet")
public class AddAddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");

		if (username != null) {
			String address = request.getParameter("address");

			// Process the address as needed (e.g., store it in a database)
			saveAddressToFile(username, address);

			// Redirect back to the WelcomeServlet after processing the address
			response.sendRedirect("WelcomeServlet");
		} else {
			response.sendRedirect("login.html");
		}
	}

	private void saveAddressToFile(String username, String address) throws IOException {
		// Build the file path
		String filePath = getServletContext().getRealPath("/META-INF/userdata/") + "/" + username + "-address.txt";

		// Write the address to the file
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
			writer.println(address);
		}
	}


}
