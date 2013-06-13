package edu.um.umflix;

import edu.um.umflix.stubs.LoginServletStub;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Test for the LoginServlet
 */
public class LoginServletTest {
    static HttpServletRequest request;
    static HttpServletResponse response;

    @Before
    public void setUp(){
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getParameter("name")).thenReturn("mbomio@gmail.com");
        Mockito.when(request.getParameter("password")).thenReturn("12345");
        Mockito.when(request.getRequestDispatcher("/upload.jsp")).thenReturn(Mockito.mock(RequestDispatcher.class));
        Mockito.when(request.getRequestDispatcher("/index.jsp")).thenReturn(Mockito.mock(RequestDispatcher.class));
    }

    @Test
    public void testNormalBehaviour() throws InvalidUserException {
        LoginServletStub servlet = new LoginServletStub();
        UserManager userManager = Mockito.mock(UserManager.class);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        servlet.setUserManager(userManager);

        Mockito.when(userManager.login(argument.capture())).thenReturn("validToken");


        try {

            servlet.doPost(request, response);
            Mockito.verify(request).setAttribute("token", "validToken");

            Mockito.verify(userManager).login(argument.capture());

            Assert.assertEquals("mbomio@gmail.com", argument.getValue().getEmail());
            Assert.assertEquals("12345", argument.getValue().getPassword());

            Mockito.verify(request).setAttribute("token", "validToken");
            Mockito.verify(request).getRequestDispatcher("/upload.jsp");

        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void testInvalidUser(){
        LoginServletStub servlet = new LoginServletStub();
        UserManager userManager = Mockito.mock(UserManager.class);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        servlet.setUserManager(userManager);

        try {
            Mockito.when(userManager.login(argument.capture())).thenThrow(new InvalidUserException());
        }catch (InvalidUserException e){
            Assert.fail();
        }

        try {
            servlet.doPost(request,response);

            Mockito.verify(userManager).login(argument.capture());
            Mockito.verify(request).setAttribute("error","Unknown user, please try again.");
            Mockito.verify(request).getRequestDispatcher("/index.jsp");
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InvalidUserException e){
            Assert.fail();
        }

    }

}
