# alfresco-scripts
Alfresco commonly used scripts which can be executed from java command line. There are services which can be consumed by any external application if needed


###Currently available scripts:

 1- Delete sites by excluding a list of site (sites which you don't want to delete)
 
 com.github.abhinavmishra14.site.service.test.DeleteSites
 
 2- Generate User report along with user groups. OOTB Alfresco doesn't have direct API to do get all users along with their groups at once. Though you can get user profile of a particular user along with group. This report also includes information of all the sites which the user has access. See the sample report here: samples/userReports.json
 
 
 com.github.abhinavmishra14.reports.test.GenerateUserReport
 
 
 3- Cleanup trash can 

 
 com.github.abhinavmishra14.trashcan.service.test.ClearTrashcan