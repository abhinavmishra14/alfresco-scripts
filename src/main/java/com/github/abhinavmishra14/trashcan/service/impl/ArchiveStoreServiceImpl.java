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
package com.github.abhinavmishra14.trashcan.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.github.abhinavmishra14.trashcan.service.ArchiveStoreService;

/**
 * The Class ArchiveStoreServiceImpl.
 */
public class ArchiveStoreServiceImpl implements ArchiveStoreService {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(ArchiveStoreServiceImpl.class);
	
	/** The Constant ALL_ARCHIEVES_URI. */
	private static final String ALL_ARCHIEVES_URI = "{0}/alfresco/service/api/archive/workspace/SpacesStore";
	
	/** The Constant DELETE_ARCHIVED_NODE_URI. */
	private static final String DELETE_ARCHIVED_NODE_URI = "{0}/alfresco/service/api/archive/archive/SpacesStore/{1}";
	
	/** The server endpoint. */
	private final String serverEndpoint;
	
	/**
	 * The Constructor.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public ArchiveStoreServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}
	
	/**
	 * Gets the archieved nodes after days.
	 *
	 * @param authTicket the auth ticket
	 * @param batchSize the batch size
	 * @param olderThanDays the older than days
	 * @return the archieved nodes after days
	 * @throws ClientProtocolException the client protocol exception
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException the IO exception
	 */
	public List<String> getArchievedNodesAfterDays(final String authTicket,
			final int batchSize, final int olderThanDays)
			throws ClientProtocolException, URISyntaxException, IOException {
		final JSONObject archiveData = getArchiveSpaceStoreData(authTicket, batchSize);
		final JSONObject data = archiveData.getJSONObject(AlfScriptConstants.DATA);
		final List<String> nodeList = new ArrayList<String>(batchSize);
		if (data.has(AlfScriptConstants.DELETED_NODES)) {
			final JSONArray deletedNodes = data.getJSONArray(AlfScriptConstants.DELETED_NODES);
			for (int each = 0; each <deletedNodes.length(); each++) {
				final JSONObject eachNodeObj = deletedNodes.getJSONObject(each);
				if (eachNodeObj.has(AlfScriptConstants.ARCHIEVED_DATE)) {
					final String archivedDate = eachNodeObj.getString(AlfScriptConstants.ARCHIEVED_DATE);
					if (eachNodeObj.has(AlfScriptConstants.NODE_REF) && olderThanDays > 0) {
						if (AlfScriptUtils.olderThanDays(archivedDate, olderThanDays)) {
							nodeList.add(eachNodeObj.getString(AlfScriptConstants.NODE_REF));
						}
					} else {
						nodeList.add(eachNodeObj.getString(AlfScriptConstants.NODE_REF));
					}
				}
			}
		}
		return nodeList;
	}
	
	/**
	 * Gets the archieved nodes.
	 *
	 * @param authTicket the auth ticket
	 * @param batchSize the batch size
	 * @return the archieved nodes
	 * @throws ClientProtocolException the client protocol exception
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException the IO exception
	 */
	public List<String> getArchievedNodes(final String authTicket,
			final int batchSize) throws ClientProtocolException,
			URISyntaxException, IOException {
		final JSONObject archiveData = getArchiveSpaceStoreData(authTicket, batchSize);
		final JSONObject data = archiveData.getJSONObject(AlfScriptConstants.DATA);
		final List<String> nodeList = new ArrayList<String>(batchSize);
		if (data.has(AlfScriptConstants.DELETED_NODES)) {
			final JSONArray deletedNodes = data.getJSONArray(AlfScriptConstants.DELETED_NODES);
			for (int each = 0; each <deletedNodes.length(); each++) {
				final JSONObject eachNodeObj = deletedNodes.getJSONObject(each);
				if(eachNodeObj.has(AlfScriptConstants.NODE_REF)) {
					nodeList.add(eachNodeObj.getString(AlfScriptConstants.NODE_REF));
				}
			}
		}
		return nodeList;
	}
	
	/**
	 * Gets the archive space store data.
	 *
	 * @param authTicket the auth ticket
	 * @param batchSize the batch size
	 * @return the archive store data
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public JSONObject getArchiveSpaceStoreData(final String authTicket, final int batchSize)
			throws URISyntaxException, ClientProtocolException, IOException {
		LOG.info("Getting archived data from archive space store @ batchSize: "+batchSize);
		final Object[] uriArgs = {serverEndpoint};
		final MessageFormat msgFormat = new MessageFormat(ALL_ARCHIEVES_URI);
		final String archivesUrl = msgFormat.format(uriArgs);
		final URIBuilder uriBuilder = new URIBuilder(archivesUrl);
		uriBuilder.addParameter(AlfScriptConstants.PARAM_MAX_ITEMS, Integer.toString(batchSize));
		uriBuilder.addParameter(AlfScriptConstants.PARAM_SKIP_COUNT, AlfScriptConstants.DEFAULT_SKIP_COUNT);
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpGet(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		JSONObject archiveData = null;
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			final String resonseStr = IOUtils.toString(httpResp.getEntity().getContent(),
					StandardCharsets.UTF_8);
			archiveData = new JSONObject(resonseStr);
		} else {
			throw new AlfScriptException(statusMsg);
		}
		return archiveData;
	}
	
	/**
	 * Delete archived node.
	 *
	 * @param archivedNode the archived node
	 * @param authTicket the auth ticket
	 * @throws URISyntaxException the URI syntax exception
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public void deleteArchivedNode(final String archivedNode,
			final String authTicket) throws URISyntaxException,
			JsonProcessingException, ClientProtocolException, IOException {
		LOG.info("Deleting archived node: "+archivedNode);
		final String archiveNodeToDelete = StringUtils.contains(archivedNode, AlfScriptConstants.ARCHIVE_STOREREF)
				? StringUtils.substringAfter(archivedNode, AlfScriptConstants.ARCHIVE_STOREREF)
				: archivedNode;
		final Object[] uriArgs = {serverEndpoint, archiveNodeToDelete};
		final MessageFormat msgFormat = new MessageFormat(DELETE_ARCHIVED_NODE_URI);
		final String deleteArcUrl = msgFormat.format(uriArgs);
		final URIBuilder uriBuilder = new URIBuilder(deleteArcUrl);
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpDelete(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
	}
}
