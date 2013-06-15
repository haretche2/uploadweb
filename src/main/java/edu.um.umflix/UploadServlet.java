package edu.um.umflix;


import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

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
 * Servlet that uploads the movie to UMFlix.
 */
public class UploadServlet extends HttpServlet{
    Logger log = Logger.getLogger(UploadServlet.class);

    @EJB(beanName = "VendorManager")
    protected edu.um.umfix.vendormanager.VendorManager vManager;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String,String> mapValues = new HashMap<String, String>();
        List<Clip> clips = new ArrayList<Clip>();
        List<Byte[]> listData = new ArrayList<Byte[]>();
        int i=0;

        try{
        List<FileItem> items = getFileItems(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                // Process normal fields here.

                mapValues.put(item.getFieldName(),item.getString());
               // w.print("Field name: " + item.getFieldName());
               // w.print("Field value: " + item.getString());
            } else {
                // Process <input type="file"> here.

                InputStream movie =item.getInputStream();
                byte[] bytes = IOUtils.toByteArray(movie);
                Byte[] clipBytes = ArrayUtils.toObject(bytes);
                Clip clip = new Clip(Long.valueOf(0),i);
                clips.add(i,clip);
                listData.add(i,clipBytes);
                i++;
               // w.print(movie);

            }
        }
        }catch (FileUploadException e){
           log.error("Error uploading file, please try again.");
            request.setAttribute("error", "Error uploading file, please try again");
            request.setAttribute("token",mapValues.get("token"));
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
        }
        //Sets duration of clip and saves clipdata
        for(int j =0;j<clips.size();j++) {
            int duration = timeToInt(mapValues.get("clipduration"+j));
            clips.get(j).setDuration(Long.valueOf(duration));
            ClipData clipData = new ClipData(listData.get(j),clips.get(j));

            try {
               vManager.uploadClip(mapValues.get("token"),new Role(Role.RoleType.MOVIE_PROVIDER.getRole()),clipData);
                log.info("ClipData uploader!");
            }catch (InvalidTokenException e) {
               log.error("Unknown user, please try again.");
                request.setAttribute("error", "Unknown user, please try again.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }


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
            log.error("Error parsing date");
            request.setAttribute("token",mapValues.get("token"));
            request.setAttribute("error","Invalid date, please try again");
            request.getRequestDispatcher("/upload.jsp").forward(request, response);
            return;

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
                log.info("Movie uploaded!");
        } catch (InvalidTokenException e) {
           log.error("Unknown user, please try again.");
           request.setAttribute("error", "Unknown user, please try again.");
           request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        request.setAttribute("message","Movie uploaded successfully.");
        request.setAttribute("token", mapValues.get("token"));
        request.getRequestDispatcher("/upload.jsp").forward(request, response);
    }

    protected List<FileItem> getFileItems(HttpServletRequest request) throws FileUploadException {
        return new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
    }

    private int timeToInt(String time){
        String[] split = time.split(":");
        int seconds = Integer.valueOf(split[0])*360 + Integer.valueOf(split[1])*60 + Integer.valueOf(split[2]);
        return seconds;
    }


}

