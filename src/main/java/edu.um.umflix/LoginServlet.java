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
 *
 */

public class LoginServlet extends HttpServlet{
    @EJB(name = "UserManager")
    UserManager uManager;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         String name = req.getParameter("name");
         String password = req.getParameter("password");

        User user =  new User(null,name,password,null);
        try {
            String token = uManager.login(user);
            req.getSession().setAttribute("token", token);
            req.getRequestDispatcher("/upload.jsp").forward(req, resp);
        } catch (InvalidUserException e) {
            req.getSession().setAttribute("error","Unknown user, please try again.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
