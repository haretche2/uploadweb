package edu.um.umflix;


import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

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
    edu.um.umfix.vendormanager.VendorManager vManager;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String,String> mapValues = new HashMap<String, String>();
        List<Clip> clips = new ArrayList<Clip>();
        int i=0;
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
                Byte[] clipBytes = ArrayUtils.toObject(bytes);
                Clip clip = new Clip(Long.valueOf(mapValues.get("duration"))/3,i);
                clips.add(clip);
                ClipData clipData = new ClipData(clipBytes,clip);
                try {
                    vManager.uploadClip(mapValues.get("token"),new Role(Role.RoleType.MOVIE_PROVIDER.getRole()),clipData);
                } catch (InvalidTokenException e) {
                    e.printStackTrace();
                    request.getSession().setAttribute("error","Unknown user, please try again.");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
                i++;
            }
        }
        }catch (FileUploadException e){
            e.printStackTrace();
            request.getSession().setAttribute("error","Error uploading file, please try again");
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
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
            request.getSession().setAttribute("error","Invalid date, please try again");
            request.getRequestDispatcher("/upload.jsp").forward(request, response);

        }

        License license = new License(false,Long.valueOf(0),mapValues.get("licenseDescription"),endDate,
                Long.valueOf(mapValues.get("maxViews")),Long.valueOf(1),startDate, mapValues.get("licenseName"));
        List<License> licenses = new ArrayList<License>();
        licenses.add(license);

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

