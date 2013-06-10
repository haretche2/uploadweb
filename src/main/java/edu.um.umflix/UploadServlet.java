package edu.um.umflix;


import edu.um.umfix.vendormanager.VendorManager;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class UploadServlet extends HttpServlet{
    @EJB(name="VendorManager")
    VendorManager vManager;

    private DivideAlgorithm algorithm;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        algorithm = new DevideInFixPartsAlgorithm();
        HashMap<String,String> mapValues = new HashMap<String, String>();
        List<Byte[]> arrays = null;
        try{
        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                // Process normal fields here.

                mapValues.put(item.getFieldName(),item.getString());
//                w.print("Field name: " + item.getFieldName());
//                w.print("Field value: " + item.getString());
            } else {
                // Process <input type="file"> here.

                InputStream movie =item.getInputStream();
                byte[] bytes = IOUtils.toByteArray(movie);
                arrays = divide(bytes);
            }
        }
        }catch (FileUploadException e){
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate=null;
        Date startDate=null;
        Date premiereDate =null;
        try {
            endDate = formatter.parse(mapValues.get("endDate"));
            startDate = formatter.parse(mapValues.get("startDate"));
            premiereDate = formatter.parse(mapValues.get("premiere"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        License license = new License(false,Long.valueOf(0),mapValues.get("licenseDescription"),endDate,
                Long.valueOf(mapValues.get("maxViews")),Long.valueOf(1),startDate, mapValues.get("licenseName"));
        List<License> licenses = new ArrayList<License>();
        licenses.add(license);

        if(arrays!=null){
            List<Clip> clips = new ArrayList<Clip>();
            for(int i=0;i<arrays.size();i++){
                Clip clip = new Clip(Long.valueOf(mapValues.get("duration"))/3,i);
                clips.add(clip);
                ClipData clipData = new ClipData(arrays.get(i),clip);
                //vendorManager.saveClip
                try {
                    vManager.uploadClip(mapValues.get("token"),new Role(Role.RoleType.MOVIE_PROVIDER.getRole()),clipData);
                } catch (InvalidTokenException e) {
                    e.printStackTrace();
                    request.getSession().setAttribute("error","Unknown user, please try again.");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }

            }
            ArrayList<String> cast = new ArrayList<String>();
            cast.add(mapValues.get("actor"));
            Movie movie = new Movie(cast,clips, mapValues.get("director"),Long.valueOf(mapValues.get("duration")),
                    false, mapValues.get("genre"),licenses,premiereDate, mapValues.get("title"));

            try {
                vManager.uploadMovie(mapValues.get("token"),new Role(Role.RoleType.MOVIE_PROVIDER.getRole()),movie);
            } catch (InvalidTokenException e) {
                e.printStackTrace();
                request.getSession().setAttribute("error","Unknown user, please try again.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }
    }

    public List<Byte[]> divide(byte[] bytes){
        return algorithm.devide(bytes);
    }
}

