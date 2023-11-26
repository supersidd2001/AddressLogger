package com.address;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 781079050450903940L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = hashPassword(request.getParameter("password"));

		RequestCounterListener listener = (RequestCounterListener) getServletContext()
				.getAttribute("RequestCounterListener");
		listener.incrementTotalRequests();
		listener.addUniqueUser(username);

		UserLoginListener userLoginListener = (UserLoginListener) getServletContext().getAttribute("UserLoginListener");
		userLoginListener.updateUserBalance(username);

		if (isValidUser(username, password)) {
			HttpSession session = request.getSession();
			session.setAttribute("username", username);

			// Set the balance cookie
			double balance = userLoginListener.getUserBalance(username);
			userLoginListener.setCookieBalance(response, username, balance);

			response.sendRedirect("WelcomeServlet");
		} else {
			response.sendRedirect("login.html");
		}
	}

	private boolean isValidUser(String username, String password) throws IOException {
		String filePath = getServletContext().getRealPath("/WEB-INF/user_credentials.txt");
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");
			if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
				reader.close();
				return true;
			}
		}

		reader.close();
		return false;
	}

	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();

			for (byte hashedByte : hashedBytes) {
				String hex = Integer.toHexString(0xff & hashedByte);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
