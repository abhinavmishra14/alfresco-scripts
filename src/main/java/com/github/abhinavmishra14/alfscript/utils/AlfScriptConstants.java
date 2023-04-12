/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2017. Abhinav Kumar Mishra. 
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.abhinavmishra14.alfscript.utils;


/**
 * The Class AlfScriptConstants.
 */
public final class AlfScriptConstants {
	
	public static final String DEFAULT_USER = "admin";
	public static final String DEFAULT_PASSWORD = "admin";
	public static final String DEFAULT_HOST = "http://127.0.0.1:8080";
	
	public static final String DEFAULT_PGUSER = "alfresco";
	public static final String DEFAULT_PGPASSWORD = "alfresco";
	public static final String DEFAULT_PGHOST = "localhost";
	public static final String DEFAULT_PGPORT = "5432";
	
	public static final int DEFAULT_BATCH_SIZE = 1000;
	
	//Use -1 for everything 
	public static final int DEFAULT_DAYS = 30;
	public static final String DEFAULT_SKIP_COUNT = "0";

	public static final String PARAM_USER = "username";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_AUTH_TICKET = "alf_ticket";
	public static final String PARAM_MAX_ITEMS = "maxItems";
	public static final String PARAM_SKIP_COUNT = "skipCount";
	
	public static final String DATA = "data";
	public static final String TICKET = "ticket";
	public static final String PATH_SEPERATOR = "/";
	public static final String SHORTNAME = "shortName";
	public static final String DELETED_NODES = "deletedNodes";
	public static final String ARCHIEVED_DATE = "archivedDate";
	public static final String NODE_REF = "nodeRef";
	public static final String ARCHIVE_STOREREF = "archive://SpacesStore/";
	
	public static final String DEFAULT_TYPE = "cm:content";
	public static final String DEFAULT_STARTDT = "NOW-2MONTHS";
	public static final String DEFAULT_ENDDT = "NOW";
	
	public static final String CARRIAGE_AND_NEWLINE_REGEX = "\\r|\\n";
	public static final String COMMA = ",";
	
	public static final String VERSION2STORE_ID = "4";
	public static final String ARCHIVESTORE_ID = "5";
	public static final String WORKSPACESTORE_ID = "6";
	public static final String STORE_ID = "storeId";
	public static final String NODE_ID = "nodeId";
	public static final String DB_STORE_ID = "Store ID";
	public static final String DB_DOC_ID = "Document ID (UUID)";
	public static final String DB_CREATOR = "Creator";
	public static final String DB_CREATEDDATE = "Creation Date";
	public static final String DB_MODIFIER = "Modifier";
	public static final String DB_MODIFIED_DATE = "Modification Date";
	public static final String DB_DOCNAME = "Document Name";
	public static final String DB_DOCSIZE = "Size (MB)";
	public static final String CREATOR = "creator";
	public static final String CREATEDDATE = "createdDate";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIED_DATE = "modifiedDate";
	public static final String NAME = "name";
	public static final String CONTENTURL = "contentUrl";
	public static final String STORE = "storeName";
	public static final String SIZEMB = "sizeAsMB";
	
	
	
	/**
	 * The Constructor.
	 */
	private AlfScriptConstants() {
		super();
	}
}
