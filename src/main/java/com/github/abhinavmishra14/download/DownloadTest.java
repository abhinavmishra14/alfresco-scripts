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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.github.abhinavmishra14.json.utils.JSONUtils;

/**
 * The Class DownloadTest.
 */
public class DownloadTest {
		
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(DownloadTest.class);
	
	/** The Constant DOWNLOAD_API_URI. */
	public static final String DOWNLOAD_API_URI = "{0}/alfresco/service/api/node/workspace/SpacesStore/{1}/content/{2}?c=force&alf_ticket={3}";
	
	/**
	 * The main method.
	 *
	 * @param args the args
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 */
	public static void main(String[] args) throws JsonProcessingException,
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
		
		String inputFilePath = StringUtils.EMPTY;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			inputFilePath = args[3].trim();
			if (StringUtils.isEmpty(inputFilePath)) {
				throw new IllegalArgumentException("Please provide a valid json inputFilePath");
			}
			//A sample input file is available here: /alfresco-scripts/samples/files-to-download.json
			inputFilePath = FilenameUtils.separatorsToSystem(inputFilePath);
		}
		
		String downloadPath = StringUtils.EMPTY;
		if (args.length >= 5 && StringUtils.isNotBlank(args[4])) {
			downloadPath = args[4].trim();
			if (StringUtils.isEmpty(downloadPath)) {
				throw new IllegalArgumentException("Please provide a valid downloadPath");
			}
			downloadPath = FilenameUtils.separatorsToSystem(downloadPath);
		}
		if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(userName)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(inputFilePath)
				&& StringUtils.isNotBlank(downloadPath)) {
			LOG.info("Downloading files using input json: "+inputFilePath +" from host: "+host+" at: "+downloadPath);
			try (final FileInputStream fis = new FileInputStream(new File(inputFilePath));) {
				final Map<String, Object> jsonMap = JSONUtils.getMapFromJsonInputStream(fis);
				final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
				for (final Iterator<Entry<String, Object>> iterator = jsonMap.entrySet().iterator(); iterator
						.hasNext();) {
					final Entry<String, Object> eachEntry = iterator.next();
					final String fileName = eachEntry.getKey();
					final String nodeRef = (String) eachEntry.getValue();
					final String downloadURL = prepareDownloadUrl(host,
							StringUtils.substringAfter(nodeRef, "workspace://SpacesStore/"), fileName, authTicket);
					final boolean isDownloaded = download(downloadURL, downloadPath, fileName);
					if (isDownloaded) {
						LOG.info("File: "+fileName+" has been downloaded at: "+downloadPath);
					}
				}
			}		
		} else {
			throw new IllegalArgumentException(
					"Please check if you have provided the parameters required for the download test. \n Download test invocation command: java com.github.abhinavmishra14.download.DownloadTest <host> <user> <password> <inputJsonFilePath> <downloadPath>");
		}
	}
	
	/**
	 * Download.
	 *
	 * @param downloadURL the download url
	 * @param downloadPath the download path
	 * @param fileName the file name
	 * @return true, if download
	 */
	public static boolean download(final String downloadURL,
			final String downloadPath, final String fileName) {
		boolean isDownloaded = false;
		try {
			final HttpResponse httpResp = HTTPUtils.httpGet(downloadURL);
			final StatusLine status = httpResp.getStatusLine();
			final int statusCode = status.getStatusCode();
			final String statusMsg = status.getReasonPhrase();
			LOG.info("Status: "+statusCode +" | "+ statusMsg);
			if (statusCode == HTTPUtils.HTTP_CODE_200) {
				final InputStream content = httpResp.getEntity().getContent();
				final String downloadFilePath = getFileName(fileName, downloadPath);
				LOG.info("DownloadFilePath: "+downloadFilePath);
				final File outputFile = new File(downloadFilePath);
				copyInputStreamToFile(content, outputFile);
				isDownloaded = outputFile.exists();
			}
		} catch (IOException excp) {
			LOG.error("Failed to download the file: "+downloadURL, excp);
		}
		return isDownloaded;
	}
	
	/**
	 * Gets the file name.
	 *
	 * @param fileName the file name
	 * @param downloadPath the download path
	 * @return the file name
	 */
	private static String getFileName(final String fileName, final String downloadPath) {
		return StringUtils.stripEnd(downloadPath, File.separator)+"/"+fileName;
	}
	
	/**
	 * Prepare download url.
	 *
	 * @param host the host
	 * @param nodeId the node id
	 * @param fileName the file name
	 * @param token the token
	 * @return the string
	 */
	private static String prepareDownloadUrl(final String host, final String nodeId,
			final String fileName, final String token) {
		final Object[] uriArgs = {host, nodeId, fileName, token};
		final MessageFormat msgFormat = new MessageFormat(DOWNLOAD_API_URI);
		return msgFormat.format(uriArgs);
	}
	
	/**
	 * Copy input stream to file.<br>
	 * After copy it will flush the output stream and closes it. It will also close the source inputStream after copy
	 *
	 * @param source the source
	 * @param destination the destination
	 * @throws IOException the IO exception
	 */
	private static void copyInputStreamToFile(final InputStream source,
			final File destination) throws IOException {
		try {
			try (final FileOutputStream output = FileUtils.openOutputStream(destination);) {
				IOUtils.copy(source, output);
				try {
					if (output != null) {
						output.flush();
						output.close();
					}
				} catch (IOException ioexIgnore) {
					LOG.warn("[Ignore] Failed to close the output stream", ioexIgnore);
				}
			}
		} finally {
			try {
				if (source != null) {
					source.close();
				}
			} catch (IOException ioexIgnore) {
				LOG.warn("[Ignore] Failed to close the source stream", ioexIgnore);
			}
		}
	}
}
