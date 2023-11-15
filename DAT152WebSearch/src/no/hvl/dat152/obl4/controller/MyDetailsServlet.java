package no.hvl.dat152.obl4.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat152.obl4.database.AppUser;
import no.hvl.dat152.obl4.database.SearchItem;
import no.hvl.dat152.obl4.database.SearchItemDAO;


@WebServlet("/mydetails")
public class MyDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (RequestHelper.isLoggedIn(request)) {
			
			 generateAndStoreCSRFToken(request);
			
			AppUser user = (AppUser) request.getSession().getAttribute("user");
			
			String sortkey = request.getParameter("sortkey");

			SearchItemDAO searchItemDAO = new SearchItemDAO();
			
			List<SearchItem> myhistory = null;
			if(sortkey == null)
				myhistory = searchItemDAO.getSearchHistoryForUser(user.getUsername());
			else
				myhistory = searchItemDAO.getSearchHistoryForUser(user.getUsername(), sortkey);

			request.setAttribute("myhistory", myhistory);

			request.getRequestDispatcher("mydetails.jsp").forward(request,
					response);
		} else {
			request.getSession().invalidate();
			request.getRequestDispatcher("index.jsp").forward(request,
					response);
		}
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		
        // Validate CSRF token
        if (!validateCSRFToken(request)) {
            request.setAttribute("message", "CSRF Attack Detected");
            request.getRequestDispatcher("mydetails.jsp").forward(request, response);
            return;
        }

        doGet(request, response);
	}
	
	
	private String generateCSRFToken() {
	    SecureRandom secureRandom = new SecureRandom();
	    byte[] randomBytes = new byte[32];

	    secureRandom.nextBytes(randomBytes);

	    String csrfToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

	    return csrfToken;
	}
	
    private void generateAndStoreCSRFToken(HttpServletRequest request) {
        // Generate a CSRF token
        String csrfToken = generateCSRFToken();

        // Store CSRF token in the session
        request.getSession().setAttribute("csrfToken", csrfToken);
    }
	
    private boolean validateCSRFToken(HttpServletRequest request) {
        String clientCSRFToken = request.getParameter("csrfToken");
        String serverCSRFToken = (String) request.getSession().getAttribute("csrfToken");

        // Validate CSRF token
        return clientCSRFToken != null && clientCSRFToken.equals(serverCSRFToken);
    }
	
	
}
