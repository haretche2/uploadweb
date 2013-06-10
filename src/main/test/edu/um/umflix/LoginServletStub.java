package edu.um.umflix;

import edu.umflix.usermanager.UserManager;

/**
 * Stub for testing
 */
public class LoginServletStub extends LoginServlet{

    public void setUserManager(UserManager userManager){
        this.uManager = userManager;
    }
}
