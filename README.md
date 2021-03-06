# alfresco-scripts

These are commonly used scripts which can be executed from java command line. There are services which can be consumed by any external application if needed. These scripts uses OOTB Alfresco REST APIs to get the information and performs the operations according to the need. 



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


