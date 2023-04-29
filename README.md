# alfresco-scripts

These are commonly used scripts which can be executed from java command line. There are services which can be consumed by any external apps/scripts if needed. These scripts uses Alfresco OOTB REST APIs.



# Delete sites by excluding a list of sites (sites which you don't want to delete)
 
 ```java
  java -cp alfresco-scripts-jar-with-dependencies.jar  com.github.abhinavmishra14.site.service.test.DeleteSites [HOST] [USER] [PASSWORD] [EXCLUDED_SITES_DEFAULT_COMMA_SEPARATED]
  
 ```
 #### Delete site parameter information:

   - HOST -> Is the alfresco host base web url e.g.: http://127.0.0.1:8080
   
   - USER -> Is the user name for authentication. e.g.: admin
   
   - PASSWORD -> Is the password for authentication
   
   - EXCLUDED_SITES_DEFAULT_COMMA_SEPARATED -> Is the sites you want to exclude from deletion. e.g.: test-site, test-site2
   
 

# Generate User report along with user groups.

#### OOTB Alfresco doesn't have direct API to do get all users along with their groups at once. Though you can get user profile of a particular user along with group. This report also includes information of all the sites which the user has access. Report is generated as a json. See the sample report here: samples/userReports.json
 
 
 ```java 
 java -cp alfresco-scripts-jar-with-dependencies.jar com.github.abhinavmishra14.reports.test.GenerateUserReport [HOST] [USER] [PASSWORD]
 ```
 
 #### Generate user report parameter information:

   - HOST -> Is the alfresco host base web url e.g.: http://127.0.0.1:8080
   
   - USER -> Is the user name for authentication. e.g.: admin
   
   - PASSWORD -> Is the password for authentication
   
 

# Cleanup trash can script

 ```java
 java -cp alfresco-scripts-jar-with-dependencies.jar com.github.abhinavmishra14.trashcan.service.test.ClearTrashcan [HOST] [USER] [PASSWORD] [BATCH_SIZE] [OLDER_THAN_NUM_OF_DAYS]
 ```
 
 #### Delete trash can parameter information:

   - HOST -> Is the alfresco host base web url e.g.: http://127.0.0.1:8080
   
   - USER -> Is the user name for authentication. e.g.: admin
   
   - PASSWORD -> Is the password for authentication
   
   - BATCH_SIZE -> Number of items in one batch
   
   - OLDER_THAN_NUM_OF_DAYS -> How many days older items needs deletion
 
 

#  Delete all tags script

 ```java
 java -cp alfresco-scripts-jar-with-dependencies.jar com.github.abhinavmishra14.tags.test.DeleteTags [HOST] [USER] [PASSWORD] [BATCH_SIZE]
 ```
 
  #### Delete tags parameter information:

   - HOST -> Is the alfresco host base web url e.g.: http://127.0.0.1:8080
   
   - USER -> Is the user name for authentication. e.g.: admin
   
   - PASSWORD -> Is the password for authentication
   
   - BATCH_SIZE -> Number of items in one batch


#  Delete nodes based on given content url (Used database to query nodes based on content urls)

 ```java
 java -cp alfresco-scripts-jar-with-dependencies.jar com.github.abhinavmishra14.node.test.DeleteNodesBasedOnContentUrl [HOST] [USER] [PASSWORD] [CONTENTURLS_TXT_FILE] [DB_HOST] [DB_PORT] [DB_USER] [DB_PASSWORD]
 ```
 
  #### Delete Nodes Based On ContentUrl parameter information:

   - HOST -> Is the alfresco host base web url e.g.: http://127.0.0.1:8080
   
   - USER -> Is the user name for authentication. e.g.: admin
   
   - PASSWORD -> Is the password for authentication
   
   - CONTENTURLS_TXT_FILE -> Filesystem path of a text file containing comma separated content urls in bulk. [See sample text file here](https://github.com/abhinavmishra14/alfresco-scripts/blob/master/samples/contenturls.txt)
   
   - DB_HOST -> Is the database host to be used for JDBC URL. e.g.: 127.0.0.1/localhost/dev.postgres.com
   
   - DB_PORT -> Is the database port to be used for JDBC URL. e.g.: 5432
   
   - DB_USER -> Is the database user name to be used for JDBC URL. e.g.: alfresco
   
   - DB_PASSWORD -> Is the database password to be used for JDBC URL. e.g.: alfresco
   

#  Upload files in parallel

 ```java
 java -cp alfresco-scripts-jar-with-dependencies.jar com.github.abhinavmishra14.upload.test.UploadTest [HOST] [USER] [PASSWORD] [SOURCE_FILEFOLDER_PATH] [PARENT_NODEID] [ADDITIONAL_MATADATA]
 ```
 
  #### Upload test parameter information:

   - HOST -> Is the alfresco host base web url e.g.: http://127.0.0.1:8080
   
   - USER -> Is the user name for authentication. e.g.: admin
   
   - PASSWORD -> Is the password for authentication
   
   - SOURCE_FILEFOLDER_PATH -> Folder path which contains multiple files or a single file path. e.g.: C:\Users\Abhinav\Downloads
   
   - PARENT_NODEID -> Parent Node Ref ID of the folder where content needs to be uploaded. e.g.: 4cccf037-03e9-49ff-93fe-a65f20bc0ff3
   
   - ADDITIONAL_MATADATA -> Any additional metadata properties (comma separated) you want to apply while uploading the content. e.g.: cm:title=SampleTitle,cm:description=sample desc
  

