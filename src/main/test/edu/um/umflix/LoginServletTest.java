package edu.um.umflix;

import edu.umflix.usermanager.UserManager;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Test for the LoginServlet
 */
public class LoginServletTest {

    @Test
    public void test1(){
        LoginServletStub servlet = new LoginServletStub();
        UserManager userManager = Mockito.mock(UserManager.class);

        servlet.setUserManager(userManager);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        try {
            servlet.doPost(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
