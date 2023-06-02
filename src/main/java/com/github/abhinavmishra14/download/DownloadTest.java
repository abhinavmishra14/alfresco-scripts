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
package com.github.abhinavmishra14.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.download.service.SNDService;
import com.github.abhinavmishra14.download.service.impl.SNDServiceImpl;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.json.utils.JSONUtils;

/**
 * The Class DownloadTest.
 */
public class DownloadTest {
		
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(DownloadTest.class);
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws JSONException the JSON exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws JSONException,
			ClientProtocolException, AlfScriptException, IOException {
		
		String host = StringUtils.EMPTY;
		//Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
			if (StringUtils.isEmpty(host) || !host.contains("://")) {
				throw new IllegalArgumentException("Please provide a valid host");
			}
		}
		
		String userName = StringUtils.EMPTY;
		//Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
			if (StringUtils.isEmpty(userName)) {
				throw new IllegalArgumentException("Please provide a valid userName");
			}
		}
		
		String password = StringUtils.EMPTY;
		//Get password
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			password = args[2].trim();
			if (StringUtils.isEmpty(password)) {
				throw new IllegalArgumentException("Please provide a valid password");
			}
		}
		
		String inputJsonMappingPath = StringUtils.EMPTY;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			inputJsonMappingPath = args[3].trim();
			if (StringUtils.isEmpty(inputJsonMappingPath)) {
				throw new IllegalArgumentException("Please provide a valid json inputJsonMappingPath");
			}
			//A sample input file is available here: /alfresco-scripts/samples/files-to-download.json
			inputJsonMappingPath = FilenameUtils.separatorsToSystem(inputJsonMappingPath);
		}
		
		String downloadLocation = StringUtils.EMPTY;
		if (args.length >= 5 && StringUtils.isNotBlank(args[4])) {
			downloadLocation = args[4].trim();
			if (StringUtils.isEmpty(downloadLocation)) {
				throw new IllegalArgumentException("Please provide a valid downloadLocation");
			}
			downloadLocation = FilenameUtils.separatorsToSystem(downloadLocation);
		}
		if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(userName)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(inputJsonMappingPath)
				&& StringUtils.isNotBlank(downloadLocation)) {
			LOG.info("Downloading files using input json: "+inputJsonMappingPath +" from host: "+host+" at: "+downloadLocation);
			final SNDService downloadServ = new SNDServiceImpl(host);
			try (final FileInputStream fis = new FileInputStream(new File(inputJsonMappingPath));) {
				final Map<String, Object> jsonMap = JSONUtils.getMapFromJsonInputStream(fis);
				final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
				for (final Iterator<Entry<String, Object>> iterator = jsonMap.entrySet().iterator(); iterator
						.hasNext();) {
					final Entry<String, Object> eachEntry = iterator.next();
					final String fileName = eachEntry.getKey();
					final String nodeRef = (String) eachEntry.getValue();
					downloadServ.processDownloadRequest(downloadLocation, authTicket, fileName, nodeRef);
				}
			}		
		} else {
			throw new IllegalArgumentException(
					"Please check if you have provided the parameters required for the download test. \n Download test invocation command: java com.github.abhinavmishra14.download.DownloadTest <host> <user> <password> <inputJsonMappingPath> <downloadLocation>");
		}
	}
}
