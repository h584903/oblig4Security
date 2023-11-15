package no.hvl.dat152.obl4.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat152.obl4.database.AppUser;
import no.hvl.dat152.obl4.database.AppUserDAO;
import no.hvl.dat152.obl4.util.Crypto;
import no.hvl.dat152.obl4.util.Role;
import no.hvl.dat152.obl4.util.Validator;

@WebServlet("/newuser")
public class NewUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int MIN_PASS_LENGTH = 8;
	
	private static final String PASS_COMPLEXITY_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

	private static final String COMMON_PATTERN_REGEX = "^(?!.*(123456|abc|password1|qwerty|admin)).*$";
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		int len = request.getRequestURL().length();
		String dicturi = request.getRequestURL().substring(0, len-7)+"v003/";

		request.setAttribute("dictconfig", dicturi);
		request.getRequestDispatcher("newuser.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		boolean successfulRegistration = false;

		String username = Validator.validString(request
				.getParameter("username"));
		String password = Validator.validString(request
				.getParameter("password"));
		String confirmedPassword = Validator.validString(request
				.getParameter("confirm_password"));
		String firstName = Validator.validString(request
				.getParameter("first_name"));
		String lastName = Validator.validString(request
				.getParameter("last_name"));
		String mobilePhone = Validator.validString(request
				.getParameter("mobile_phone"));
		String preferredDict = Validator.validString(request
				.getParameter("dicturl"));
		
		if (password.length() < MIN_PASS_LENGTH || 
				!password.matches(PASS_COMPLEXITY_REGEX) || 
				!password.matches(COMMON_PATTERN_REGEX)) {
			request.setAttribute("message", "Password must be atleast " + MIN_PASS_LENGTH + 
					" characters long and include uppercase, lowercase, digit and special characters!");
			request.getRequestDispatcher("newuser.jsp").forward(request, response);
			return;
		}

		AppUser user = null;
		if (password.equals(confirmedPassword)) {

			AppUserDAO userDAO = new AppUserDAO();

			user = new AppUser(username, Crypto.generateMD5Hash(password),
					firstName, lastName, mobilePhone, Role.USER.toString());						

			successfulRegistration = userDAO.saveUser(user);
		}

		if (successfulRegistration) {
			request.getSession().setAttribute("user", user);
			Cookie dicturlCookie = new Cookie("dicturl", preferredDict);
			dicturlCookie.setMaxAge(60*10);
			response.addCookie(dicturlCookie);

			response.sendRedirect("searchpage");

		} else {
			request.setAttribute("message", "Registration failed!");
			request.getRequestDispatcher("newuser.jsp").forward(request,
					response);
		}
	}

}
