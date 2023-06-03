/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2023. Abhinav Kumar Mishra. 
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
package com.github.abhinavmishra14.download;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.download.service.SNDService;
import com.github.abhinavmishra14.download.service.impl.SNDServiceImpl;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.json.utils.JSONUtils;

/**
 * The Class SNDContentAndMetadataTest.<br>
 * Search the content based on query and then download the content to given
 * download path and also download the metadata as json to same download path
 */
public class SNDContentAndMetadataTest {

	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(SNDContentAndMetadataTest.class);
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException 
	 * @throws AlfScriptException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public static void main(String[] args)
			throws JSONException, ClientProtocolException, AlfScriptException, IOException {
		String host = StringUtils.EMPTY;
		// Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
			if (StringUtils.isEmpty(host) || !host.contains("://")) {
				throw new IllegalArgumentException("Please provide a valid host");
			}
		}

		String userName = StringUtils.EMPTY;
		// Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
			if (StringUtils.isEmpty(userName)) {
				throw new IllegalArgumentException("Please provide a valid userName");
			}
		}

		String password = StringUtils.EMPTY;
		// Get password
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			password = args[2].trim();
			if (StringUtils.isEmpty(password)) {
				throw new IllegalArgumentException("Please provide a valid password");
			}
		}
		
		String downloadLocation = StringUtils.EMPTY;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			downloadLocation = args[3].trim();
			if (StringUtils.isEmpty(downloadLocation)) {
				throw new IllegalArgumentException("Please provide a valid downloadLocation");
			}
			downloadLocation = FilenameUtils.separatorsToSystem(downloadLocation);
		}
		
		String searchQuery = StringUtils.EMPTY;
		//Example: "PATH:'/app:company_home/st:sites/cm:test-site/cm:documentLibrary//*' AND TYPE:'cm:content'"
		//Make sure to pass the query in "" in case there are spaces between AND/OR queries when passing from command line as shown in above example.
		if (args.length >= 5 && StringUtils.isNotBlank(args[4])) {
			searchQuery = args[4].trim();
			if (StringUtils.isEmpty(searchQuery)) {
				throw new IllegalArgumentException("Please provide a valid searchQuery");
			}
		}
		
		String maxItems = AlfScriptConstants.MAX_ITEMS;
		if (args.length >= 6 && StringUtils.isNotBlank(args[5])) {
			maxItems = args[5].trim();
		}
		
		String skipCount = AlfScriptConstants.DEFAULT_SKIP_COUNT;
		if (args.length >= 7 && StringUtils.isNotBlank(args[6])) {
			skipCount = args[6].trim();
		}
		
		if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(userName)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(downloadLocation)
				&& StringUtils.isNotBlank(searchQuery)) {
			LOG.info("Searching and downloading metadata and content using searchQuery: "+searchQuery +" from host: "+host+" at: "+downloadLocation);
			final String alfTicket = AlfScriptUtils.getTicket(host, userName, password);
			final SNDService sndServ = new SNDServiceImpl(host);
			final JSONObject searchResult = sndServ.getSearchResult(alfTicket, searchQuery, maxItems, skipCount);
			
			final JSONObject listResult = searchResult.getJSONObject(AlfScriptConstants.LIST);
			final JSONObject page = listResult.getJSONObject(AlfScriptConstants.PAGINATION);
			LOG.info("Search result details: "+ page);
			final JSONArray entries = listResult.getJSONArray(AlfScriptConstants.ENTRIES);
			for (final Object eachEntry : entries) {
				final JSONObject entryJson = (JSONObject) eachEntry;
				final JSONObject entry = entryJson.getJSONObject(AlfScriptConstants.ENTRY);
				final String nodeId = entry.getString(AlfScriptConstants.ID_NODE);
				final String fileName = entry.getString(AlfScriptConstants.NAME);
				final boolean isMetaDownloaded = sndServ.downloadMetadata(nodeId, downloadLocation,
						StringUtils.substringBeforeLast(fileName, AlfScriptConstants.DOT) + JSONUtils.JSON_EXTN,
						alfTicket);
				LOG.info("Has metadata downloaded?: " + isMetaDownloaded);
				final boolean isContentDownloaded = sndServ.processDownloadRequest(downloadLocation, alfTicket, fileName, nodeId);
				LOG.info("Has content downloaded?: "+isContentDownloaded);
			}
		} else {
			throw new IllegalArgumentException(
					"Please check if you have provided the parameters required for the SNDContentAndMetadataTest. \n SNDContentAndMetadataTest invocation command: java com.github.abhinavmishra14.download.SNDContentAndMetadataTest <host> <user> <password> <downloadLocation> <searchQuery>");
		}
	}

}
