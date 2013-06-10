<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html5; charset=UTF-8" />
<title>UMFlix-UploadWeb</title>
<link rel="stylesheet" href="css/style.css" type="text/css" media="screen"/>
</head>


<body>

<div class="container">

  <div class="sidebar1">
    <!-- end .sidebar1 --></div>
  <div class="content">
 	 <div class="image">
     	<img src="images/logo.png" width="100%"  />
        <center><h1>Upload-Web</h1></center>
 	 </div>
     <div class="movie">
     <h1>Ingresar una nueva pelicula</h1>
     	<form method="post" action="uploadservlet" enctype="multipart/form-data">
         <table border="0">
         	<tr>
            <td>Title: </td>
            <td><input type="text" name="title" id="title"  required/></td>
            </tr>
            <tr>
            <td>Genre: </td>
            <td><input type="text" name="genre" id="genre" required /></td>
            </tr>
            <tr>
            <td>Director: </td>
            <td><input type="text" name="director" id="director" required /></td>
            </tr>
            <tr>
            <td><h3>Cast</h3></td>
            </tr>
            <tr>
            <td>&nbsp &nbsp &nbsp Actor:</td>
            <td><input type="text" name="actor" id="actor"  required /></td>
            <tr><td><br/></td></tr>
            </tr>
            <tr>
            <td height="30px" style="margin-top:20px">Duration:</td>
            <td><input  style="width:100px" type="time" name="duracion" id="duracion" required /></td>
            </tr>
            <tr>
            <td>Premiere: </td>
            <td><input style="width:120px" type="date" name="premiere" id="premiere" required /></td>
            </tr>
            <tr>
            <td><h3>License</h3></td>
            </tr>
            <tr>
            <td>&nbsp &nbsp &nbsp Name:</td>
            <td><input type="text" name="licenseName" id="licenseName" required /></td>
            </tr>
            <tr>
            <td>&nbsp &nbsp &nbsp Description:</td>
            <td><input type="text" name="licenseDescription" id="licenseDescription" /></td>
            </tr>
            <tr>
            <td>&nbsp &nbsp &nbsp Max Views:</td>
            <td><input style="width:100px" type="number" name="maxViews" id="maxViews" required /></td>
            </tr>
             <tr>
            <td>&nbsp &nbsp &nbsp Start Date:</td>
            <td><input style="width:120px" type="date" name="startDate" id="startDate" required /></td>
            </tr>
             <tr>
            <td>&nbsp &nbsp &nbsp End Date:</td>
            <td><input style="width:120px" type="date" name="endDate" id="endDate"  required /></td>
            </tr>
         </table>
         <br/>
         <h3>File Upload:</h3>
            <p>Select a file to upload: </p>
            <input type="file" name="file" id="file" size="50" style="width:400px;margin-left:10px;font-size:14px" required />
         <br />
        <center><input style="width:120px;font-size:16px;margin-bottom:20px;" type="submit" name="ingresar" value="Upload" style="font-size:16px"/></center>
        <input type="hidden" name="token" id="token" value="<%=session.getAttribute("token")%>"
    	 </form>
     </div>

    <!-- end .content --></div>
  <div class="sidebar2">

    <!-- end .sidebar2 --></div>
  <div class="footer">

    <!-- end .footer --></div>
  <!-- end .container --></div>
</body>
</html>
