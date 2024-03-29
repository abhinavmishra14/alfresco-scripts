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
package com.github.abhinavmishra14.upload.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.alfscript.utils.DirectoryTraverser;
import com.github.abhinavmishra14.alfscript.utils.TaskTimer;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.upload.service.UploadService;
import com.github.abhinavmishra14.upload.service.impl.UploadServiceImpl;

/**
 * The Class UploadTest.
 */
public class UploadTest {

	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(UploadTest.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws ClientProtocolException, AlfScriptException, IOException {

		//Argument example:
		//http://localhost:8080 admin admin C:\Users\Abhinav\Downloads\TestInfo.json 4cccf037-03e9-49ff-93fe-a65f20bc0ff3 cm:title=SampleTitle,cm:description=sample desc
		String host = StringUtils.EMPTY;
		// Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
			if (StringUtils.isEmpty(host) || !host.contains("://")) {
				throw new IllegalArgumentException("Please provide a valid host!");
			}
		}

		String userName = StringUtils.EMPTY;
		// Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
			if (StringUtils.isEmpty(userName)) {
				throw new IllegalArgumentException("Please provide a valid userName!");
			}
		}

		String password = StringUtils.EMPTY;
		// Get password
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			password = args[2].trim();
			if (StringUtils.isEmpty(password)) {
				throw new IllegalArgumentException("Please provide a valid password!");
			}
		}

		String folderPath = StringUtils.EMPTY;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			folderPath = args[3].trim();
			if (StringUtils.isEmpty(folderPath)) {
				throw new IllegalArgumentException("Please provide a valid folder path to upload files!");
			}
			folderPath = FilenameUtils.separatorsToSystem(folderPath);
		}

		String parentNodeId = StringUtils.EMPTY;
		if (args.length >= 5 && StringUtils.isNotBlank(args[4])) {
			parentNodeId = args[4].trim();
			if (StringUtils.isEmpty(parentNodeId)) {
				throw new IllegalArgumentException("Please provide a parent node id!");
			}
		}
		
		String additionalMetadata = StringUtils.EMPTY; // Can be a comma separated string
		if (args.length >= 6 && StringUtils.isNotBlank(args[5])) {
			additionalMetadata = args[5].trim();
		}

		List<String> metadataList = null;
		if (StringUtils.isNotBlank(additionalMetadata)) {
			metadataList = Arrays.asList(additionalMetadata.split(AlfScriptConstants.COMMA));
			LOG.info("Additional metadata: "+metadataList);
		}
		final File inputPath = new File(folderPath);
		final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
		final UploadService uploadServ = new UploadServiceImpl(host);
		final TaskTimer timer = new TaskTimer();
		timer.startTimer();
		if (inputPath.isDirectory()) {
			LOG.info("Input is a directory: " + inputPath.getAbsolutePath());
			final Set<File> setOfFiles = DirectoryTraverser.getFileUris(inputPath);
			final ExecutorService executer = Executors.newFixedThreadPool(setOfFiles.size() + 1);
			for (final File eachFile : setOfFiles) {
				if (eachFile.getName().contains("desktop.ini")) {// Ignore hidden file found usually on windows platform
					continue;
				} else {
					// Process in parallel
					submitUploadRequests(uploadServ, authTicket, eachFile, parentNodeId, metadataList, executer);
				}
			}

			executer.shutdown();
			try {
				executer.awaitTermination(1, TimeUnit.HOURS);
			} catch (InterruptedException excp) {
				LOG.debug("Thread interrupted: " + excp.getMessage(), excp);
			}
		} else {
			LOG.info("Input is a file: " + inputPath.getAbsolutePath());
			processUpload(uploadServ, authTicket, inputPath, parentNodeId, metadataList);
		}
		
		timer.endTimer();
		LOG.info("Total time spent in upload: "+timer.getFormattedTotalTime());
	}
	
	/**
	 * Process upload request.
	 *
	 * @param uploadServ the upload serv
	 * @param accessToken the access token
	 * @param fileToUpload the file to upload
	 * @param parentNode the parent node
	 * @param metadata the metadata
	 * @param exec the exec
	 */
	private static void submitUploadRequests(final UploadService uploadServ, final String accessToken,
			final File fileToUpload, final String parentNode, final List<String> metadata, final ExecutorService exec) {
		exec.submit(new Runnable() {
			public void run() {
				//upload file
				processUpload(uploadServ, accessToken, fileToUpload, parentNode, metadata);
			}
		});
	}
	
	/**
	 * Process upload.
	 *
	 * @param uploadServ the upload serv
	 * @param accessToken the access token
	 * @param fileToUpload the file to upload
	 * @param parentNode the parent node
	 * @param metadata the metadata
	 */
	private static void processUpload(final UploadService uploadServ, final String accessToken,
			final File fileToUpload, final String parentNode, final List<String> metadata) {
		try {
			final JSONObject uploadResp = uploadServ.uploadFile(fileToUpload, metadata, accessToken,
					parentNode);
			LOG.info("Upload Resonse: "+uploadResp);
		} catch (IOException ioex) {
			final String errMsg = String.format("Failed to complete upload due to %s", ioex.getMessage());
			LOG.error(errMsg, ioex);
			throw new AlfScriptException(errMsg, ioex);
		}
	}
}