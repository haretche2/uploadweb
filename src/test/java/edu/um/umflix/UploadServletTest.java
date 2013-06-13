package edu.um.umflix;

import edu.um.umfix.vendormanager.VendorManager;
import edu.um.umflix.stubs.UploadServletStub;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.ClipData;
import edu.umflix.model.Movie;
import edu.umflix.model.Role;
import junit.framework.Assert;
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
 * Test for the UploadServlet
 */
public class UploadServletTest {
    static HttpServletRequest request;
    static HttpServletResponse response;

    @Before
    public void setUp(){
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getRequestDispatcher("/upload.jsp")).thenReturn(Mockito.mock(RequestDispatcher.class));
        Mockito.when(request.getRequestDispatcher("/index.jsp")).thenReturn(Mockito.mock(RequestDispatcher.class));
    }
    @Test
    public void testUploadClipNormalBehaviour(){
        UploadServletStub servlet = new UploadServletStub("");
        VendorManager vManager = Mockito.mock(VendorManager.class);
        servlet.setVendorManager(vManager);
        try {
            servlet.doPost(request,response);
            ArgumentCaptor<ClipData> clip = ArgumentCaptor.forClass(ClipData.class);
            ArgumentCaptor<Role> role = ArgumentCaptor.forClass(Role.class);
            ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);
            Mockito.verify(vManager,Mockito.times(1)).uploadClip(token.capture(),role.capture(),clip.capture());

            Assert.assertEquals("validToken",token.getValue());
            Assert.assertEquals(0, clip.getValue().getClip().getPosition());
            Assert.assertNotNull(clip.getValue().getBytes());
            Assert.assertNotNull(role.getValue());

            Mockito.verify(request,Mockito.times(1)).getRequestDispatcher("/upload.jsp");

        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }catch (InvalidTokenException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void testUploadMovieNormalBehaviour(){
        UploadServletStub servlet = new UploadServletStub("");
        VendorManager vManager = Mockito.mock(VendorManager.class);
        servlet.setVendorManager(vManager);
        try {
            servlet.doPost(request,response);
            ArgumentCaptor<Movie> movie = ArgumentCaptor.forClass(Movie.class);
            ArgumentCaptor<Role> role = ArgumentCaptor.forClass(Role.class);
            ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);
            Mockito.verify(vManager, Mockito.times(1)).uploadMovie(token.capture(), role.capture(), movie.capture());

            Assert.assertEquals("validToken", token.getValue());
            Assert.assertEquals(Long.valueOf(11),movie.getValue().getClips().get(0).getDuration());

            Mockito.verify(request,Mockito.times(1)).getRequestDispatcher("/upload.jsp");
        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }catch (InvalidTokenException e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void testInvalidDate(){
        UploadServletStub servlet = new UploadServletStub("date");
        VendorManager vManager = Mockito.mock(VendorManager.class);
        servlet.setVendorManager(vManager);
        try {
            servlet.doPost(request,response);
            Mockito.verify(request,Mockito.times(1)).getRequestDispatcher("/upload.jsp");
        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void testUploadException(){
        UploadServletStub servlet = new UploadServletStub("fileUpload");
        VendorManager vManager = Mockito.mock(VendorManager.class);
        servlet.setVendorManager(vManager);
        try {
            servlet.doPost(request,response);
            Mockito.verify(request,Mockito.times(1)).getRequestDispatcher("/upload.jsp");
        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
     public void testInvalidTokenUploadClip(){
        UploadServletStub servlet = new UploadServletStub("token");
        VendorManager vManager = Mockito.mock(VendorManager.class);

        ArgumentCaptor<ClipData> clip = ArgumentCaptor.forClass(ClipData.class);
        ArgumentCaptor<Role> role = ArgumentCaptor.forClass(Role.class);
        ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);

        try {
            Mockito.doThrow(new InvalidTokenException()).when(vManager).uploadClip(token.capture(),role.capture(),clip.capture());
        } catch (InvalidTokenException e) {
            e.printStackTrace();
        }
        servlet.setVendorManager(vManager);
        try {
            servlet.doPost(request,response);
            Assert.assertEquals("invalidToken",token.getValue());
            Mockito.verify(request,Mockito.times(1)).getRequestDispatcher("/index.jsp");
            Mockito.verify(request,Mockito.times(1)).setAttribute("error","Unknown user, please try again.");
        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void testInvalidTokenUploadMovie(){
        UploadServletStub servlet = new UploadServletStub("token");
        VendorManager vManager = Mockito.mock(VendorManager.class);

        ArgumentCaptor<Movie> movie = ArgumentCaptor.forClass(Movie.class);
        ArgumentCaptor<Role> role = ArgumentCaptor.forClass(Role.class);
        ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);

        try {
            Mockito.doThrow(new InvalidTokenException()).when(vManager).uploadMovie(token.capture(),role.capture(),movie.capture());
        } catch (InvalidTokenException e) {
            e.printStackTrace();
        }
        servlet.setVendorManager(vManager);
        try {
            servlet.doPost(request,response);
            Mockito.verify(vManager).uploadMovie(token.getValue(),role.getValue(),movie.getValue());
            Assert.assertEquals("invalidToken",token.getValue());
            Mockito.verify(request,Mockito.times(1)).getRequestDispatcher("/index.jsp");
            Mockito.verify(request,Mockito.times(1)).setAttribute("error","Unknown user, please try again.");
        } catch (ServletException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (InvalidTokenException e){
            e.printStackTrace();
            Assert.fail();
        }
    }


}
