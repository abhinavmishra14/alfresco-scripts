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
package com.github.abhinavmishra14.upload.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.upload.service.UploadService;

/**
 * The Class UploadServiceImpl.
 */
public class UploadServiceImpl implements UploadService {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(UploadServiceImpl.class);
	
	/** The Constant UPLOAD_URL. */
	// e.g.:
	// "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/4cccf037-03e9-49ff-93fe-a65f20bc0ff3/children?alf_ticket=TICKET_83b8254f978ca50488a0c5b0a84f8d7347dc8bed"
	private static final String UPLOAD_URL = "%s/alfresco/api/-default-/public/alfresco/versions/1/nodes/%s/children?alf_ticket=%s";

	/** The server endpoint. */
	private final String serverEndpoint;
	
	/**
	 * Instantiates a new upload service impl.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public UploadServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}
	
	/**
	 * Upload file.
	 *
	 * @param fileToUpload the file to upload
	 * @param metadata the metadata
	 * @param accessToken the access token
	 * @param parentNode the parent node
	 * @return the JSON object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public JSONObject uploadFile(final File fileToUpload, final List<String> metadata, final String accessToken,
			final String parentNode) throws IOException {
		JSONObject uploadResp = null;
		final String uploadUrl = String.format(UPLOAD_URL, this.serverEndpoint, parentNode, accessToken);
		LOG.info("Upload URL: " + uploadUrl);
		try (final CloseableHttpClient httpclient = HttpClientBuilder.create().build();) {
			final HttpPost httpPost = new HttpPost(uploadUrl);
			final MultipartEntityBuilder mimeEntity = MultipartEntityBuilder.create();
			if (metadata != null && !metadata.isEmpty()) {
				for (final Iterator<String> iterator = metadata.iterator(); iterator.hasNext();) {
					final String[] eachMetadata = iterator.next().split(AlfScriptConstants.EQUALS);
					mimeEntity.addTextBody(eachMetadata[0], eachMetadata[1]);
				}
			}
			final FileBody filedata = new FileBody(fileToUpload);
			mimeEntity.addPart(AlfScriptConstants.FILEDATA, filedata);
			final HttpEntity reqEntity = mimeEntity.build();
			httpPost.setEntity(reqEntity);
			LOG.info("Executing upload request:" + httpPost.getRequestLine());
			try (final CloseableHttpResponse httpResp = httpclient.execute(httpPost)) {
				final StatusLine status = httpResp.getStatusLine();
				final int statusCode = status.getStatusCode();
				final String statusMsg = status.getReasonPhrase();
				LOG.info("Status: " + statusCode + " | " + statusMsg);
				final HttpEntity resEntity = httpResp.getEntity();
				if (resEntity != null) {
					uploadResp = new JSONObject(EntityUtils.toString(resEntity));
				}
			}
		}
		return uploadResp;
	}
}
