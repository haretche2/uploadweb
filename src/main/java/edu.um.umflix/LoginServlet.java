package edu.um.umflix;

import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet that handles the login of the user to the upload system.
 * Return the token if the user is register in the system.
 */
public class LoginServlet extends HttpServlet{
    @EJB(beanName = "UserManager")
    protected UserManager uManager;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String email = req.getParameter("email");
         String password = req.getParameter("password");

        User user =  new User(email,null,password,null);
        try {
            String token = uManager.login(user);
            req.setAttribute("token", token);
            req.getRequestDispatcher("/upload.jsp").forward(req, resp);
        } catch (InvalidUserException e) {
            req.setAttribute("error","Unknown user, please try again.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
