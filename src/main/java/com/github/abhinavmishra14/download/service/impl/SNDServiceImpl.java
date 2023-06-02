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
package com.github.abhinavmishra14.download.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.download.service.SNDService;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.google.common.net.UrlEscapers;

/**
 * The Class SNDServiceImpl.
 */
public class SNDServiceImpl implements SNDService {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(SNDServiceImpl.class);
	
	/** The Constant SEARCH_URI. */
	private static final String SEARCH_URI = "%s/alfresco/api/-default-/public/search/versions/1/search?alf_ticket=%s";
	
	/** The Constant METADATA_URI. */
	private static final String METADATA_URI = "%s/alfresco/service/api/metadata?nodeRef=workspace://SpacesStore/%s&alf_ticket=%s";
	
	/** The Constant DOWNLOAD_API_URI. */
	private static final String DOWNLOAD_API_URI = "{0}/alfresco/service/api/node/workspace/SpacesStore/{1}/content/{2}?c=force&alf_ticket={3}";
	
	/** The server endpoint. */
	private final String serverEndpoint;
	
	/**
	 * Instantiates a new SND service impl.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public SNDServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}
	
	/**
	 * Process download request.
	 *
	 * @param downloadPath the download path
	 * @param authTicket the auth ticket
	 * @param fileName the file name
	 * @param nodeRef the node ref
	 * @return true, if successful
	 */
	@Override
	public boolean processDownloadRequest(final String downloadPath, final String authTicket,
			final String fileName, final String nodeRef) {
		final String nodeId = nodeRef.contains(AlfScriptConstants.WORKSPACE_PROTOCOL)
				? StringUtils.substringAfter(nodeRef, AlfScriptConstants.WORKSPACE_PROTOCOL)
				: nodeRef;
		final String downloadURL = prepareDownloadUrl(serverEndpoint, nodeId, fileName, authTicket);
		final boolean isDownloaded = download(downloadURL, downloadPath, fileName);
		if (isDownloaded) {
			LOG.info("File: "+fileName+" has been downloaded at: "+downloadPath);
		}
		return isDownloaded;
	}
	
	/**
	 * Download.
	 *
	 * @param downloadURL the download url
	 * @param downloadPath the download path
	 * @param fileName the file name
	 * @return true, if download
	 */
	private boolean download(final String downloadURL,
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
				final String downloadFilePath = AlfScriptUtils.getFileName(fileName, downloadPath);
				LOG.info("DownloadFilePath: "+downloadFilePath);
				final File outputFile = new File(downloadFilePath);
				AlfScriptUtils.copyInputStreamToFile(content, outputFile);
				isDownloaded = outputFile.exists();
			}
		} catch (IOException excp) {
			LOG.error("Failed to download the file: "+downloadURL, excp);
		}
		return isDownloaded;
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
	private String prepareDownloadUrl(final String host, final String nodeId,
			String fileName, final String token) {
		fileName = UrlEscapers.urlPathSegmentEscaper().escape(fileName);
		final Object[] uriArgs = {host, nodeId, fileName, token};
		final MessageFormat msgFormat = new MessageFormat(DOWNLOAD_API_URI);
		return msgFormat.format(uriArgs);
	}

	/**
	 * Gets the search result.
	 *
	 * @param authTicket the auth ticket
	 * @param query the query
	 * @param maxItems the max items
	 * @param skipCount the skip count
	 * @return the search result
	 */
	@Override
	public JSONObject getSearchResult(final String authTicket, final String query, final String maxItems,
			final String skipCount) {
		JSONObject searchResult = null;
		final String searchUrl = String.format(SEARCH_URI, serverEndpoint, authTicket);
		final String searchPayload = AlfScriptUtils.getSearchPayload(query, maxItems, skipCount);
		LOG.info("Searching at: "+searchUrl+ "| payLoad: "+searchPayload);
		try {
			final HttpResponse httpResp = HTTPUtils.httpPost(searchUrl, searchPayload);
			final StatusLine status = httpResp.getStatusLine();
			final int statusCode = status.getStatusCode();
			final String statusMsg = status.getReasonPhrase();
			LOG.info("Status: "+statusCode +" | "+ statusMsg);
			if (statusCode == HTTPUtils.HTTP_CODE_200) {
				final String resonseStr = IOUtils.toString(httpResp.getEntity().getContent(),
						StandardCharsets.UTF_8);
				searchResult = new JSONObject(resonseStr);
			} else {
				throw new AlfScriptException(statusMsg);
			}
		} catch (IOException | JSONException excp) {
			LOG.error("Failed to perform search via: " + searchUrl + " | jsonPayload: " + searchPayload, excp);
		}
		return searchResult;
	}

	/**
	 * Download metadata.
	 *
	 * @param nodeId the node id
	 * @param downloadPath the download path
	 * @param fileName the file name
	 * @param alfTicket the alf ticket
	 * @return true, if successful
	 */
	@Override
	public boolean downloadMetadata(final String nodeId, final String downloadPath, final String fileName,
			final String alfTicket) {
		boolean isMetaDownloaded = false;
		final String metaDownUrl = String.format(METADATA_URI, serverEndpoint, nodeId, alfTicket);
		try {
			final HttpResponse httpResp = HTTPUtils.httpGet(metaDownUrl);
			final StatusLine status = httpResp.getStatusLine();
			final int statusCode = status.getStatusCode();
			final String statusMsg = status.getReasonPhrase();
			LOG.info("Status: "+statusCode +" | "+ statusMsg);
			if (statusCode == HTTPUtils.HTTP_CODE_200) {
				final InputStream content = httpResp.getEntity().getContent();
				final String downloadFilePath = AlfScriptUtils.getFileName(fileName, downloadPath);
				LOG.info("DownloadMetadataFilePath: "+downloadFilePath);
				final File outputFile = new File(downloadFilePath);
				AlfScriptUtils.copyInputStreamToFile(content, outputFile);
				isMetaDownloaded = outputFile.exists();
			}
		} catch (IOException excp) {
			LOG.error("Failed to download the metadata from: "+metaDownUrl, excp);
		}
		return isMetaDownloaded;
	}
}
