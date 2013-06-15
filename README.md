upload-web
=============

---
###UploadWeb for UMFlix

---
**Creating database**

1. Download postgreSQL
2. Create user: webuser password: webuser
3. Create database umflix-persistence

**Configuring tomEE**

1. Download hibernate jars.
From:  https://www.wetransfer.com/downloads/e086f50ee1e1fa77c5ce11a7963c895e20130530211808/d7e215a20bdb5d29e26ff006fc6ca1d520130530211808/10bae4
2. Drop jars in /lib of tomEE 
3. Add resource to tomee.xml and openejb.xml on /conf of tomee:

	`<Resource id="umflix-persistence" type="DataSource">`<br />
		&nbsp;`JdbcDriver org.postgresql.Driver`<br />
		&nbsp;`JdbcUrl jdbc:postgresql://localhost:5432/umflix-persistence<br />
		&nbsp;`UserName webuser`<br />
		&nbsp;`Password webuser`<br />
	`</Resource>`
	
4. Add postgreSQL driver to tomee's /lib. 
 From: http://jdbc.postgresql.org/download.html
 
**Installing Dependecies** 
(All dependencies must have jar packing in their pom.xml)

1. git clone git@github.com:marshhxx/modelstorage.git
2. cd modelstorage/
3. mvn clean install
4. cd..
5. git clone git@github.com:haretche2/autenticationhandler.git
6. cd authenticationhandler/
7. mvn clean install
8. cd .. 
9. git clone git@github.com:martinbomio/usermanager.git
10. cd usermanager/
11. mvn clean install
12. cd ..
13. git clone git@github.com:haretche2/clipstorage.git
14. cd clipstorage/
15. mvn clean install
16. cd ..
17. git clone git@github.com:fpinvidio/vendor-manager.git
18. cd vendormanager/
19. mvn clean install
20. cd ..

**Running UploadWeb** 

1. git clone git@github.com:haretche2/uploadweb.git.
2. cd upload-web/
3. mvn clean install
4. drop upload-web.war on tomEE webapps
5. Enjoy!
