package edu.um.umflix.stubs;

import edu.um.umfix.vendormanager.VendorManager;
import edu.um.umflix.UploadServlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stub for testing UploadServlet. Defines the setVendorManager simulating the injection.
 */
public class UploadServletStub extends UploadServlet{
    String[] fields = {"title","genre","actor","duration","premiere","licenseName","licenseDescription","maxViews","startDate","endDate"
    ,"clipduration0"};
    String error;

    public UploadServletStub(String error){
        this.error = error;
    }
    public void setVendorManager(VendorManager vManager){
        this.vManager=vManager;
    }
    protected List<FileItem> getFileItems(HttpServletRequest request) throws FileUploadException{
        if(error.equals("fileUpload")) new FileUploadException();

        File file = null;
        FileItem item1 = null;
        List<FileItem> list = new ArrayList<FileItem>();
        for(int i=0;i<fields.length;i++){
            FileItem item = Mockito.mock(FileItem.class);
            Mockito.when(item.isFormField()).thenReturn(true);
            Mockito.when(item.getFieldName()).thenReturn(fields[i]);
            if(fields[i].equals("premiere") || fields[i].equals("endDate") || fields[i].equals("startDate")) {
                Mockito.when(item.getString()).thenReturn("2013-06-01");
            }else{
                Mockito.when(item.getString()).thenReturn("11");
            }
            if(fields[i].equals("premiere") && error.equals("date"))
                Mockito.when(item.getString()).thenReturn("11");
            list.add(item);
        }
        try{
            file = File.createTempFile("aaaa","aaaatest");
             item1 = Mockito.mock(FileItem.class);
             Mockito.when(item1.getInputStream()).thenReturn(new FileInputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }

        list.add(item1);
        FileItem token = Mockito.mock(FileItem.class);
        Mockito.when(token.isFormField()).thenReturn(true);
        Mockito.when(token.getFieldName()).thenReturn("token");
        if(error.equals("token")){
            Mockito.when(token.getString()).thenReturn("invalidToken");
        }else
        Mockito.when(token.getString()).thenReturn("validToken");

        list.add(token);

        return list;
    }

}
