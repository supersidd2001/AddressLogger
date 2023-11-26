package com.address;

/**
 * Servlet implementation class RegisterServlet
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = -3521245743032937307L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// Hash the password before storing it
		String hashedPassword = hashPassword(password);

		// Store the user details in a file
		storeUserDetails(username, hashedPassword);

		// Redirect to a registration success page or login page
		response.sendRedirect("login.html");
	}

	private String hashPassword(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(password.getBytes());
			byte[] hashedBytes = messageDigest.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// Handle the exception appropriately (e.g., log or throw)
			System.err.println("");
			;
			return null;
		}
	}

	private void storeUserDetails(String username, String hashedPassword) throws IOException {
		// Store the user credentials in a file in the WEB-INF directory
		String filePath = getServletContext().getRealPath("/WEB-INF/user_credentials.txt");

		try (PrintWriter out = new PrintWriter(new FileWriter(filePath, true))) {
			// Append the username and hashed password to the file
			out.println(username + "," + hashedPassword);
		}
	}
}
