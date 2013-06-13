package edu.um.umflix.stubs;

import edu.um.umflix.LoginServlet;
import edu.umflix.usermanager.UserManager;

/**
 *Stub for testing LoginServlet. Defines the setUserManager simulating the injection.
 */
public class LoginServletStub extends LoginServlet {

    public void setUserManager(UserManager userManager){
        this.uManager = userManager;
    }
}
