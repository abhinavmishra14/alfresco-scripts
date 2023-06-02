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
package com.github.abhinavmishra14.node.service.impl;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.ARCHIVESTORE_ID;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.CARRIAGE_AND_NEWLINE_REGEX;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.COMMA;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.CONTENTURL;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.CREATEDDATE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.CREATOR;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_CREATEDDATE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_CREATOR;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_DOCNAME;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_DOCSIZE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_DOC_ID;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_MODIFIED_DATE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_MODIFIER;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DB_STORE_ID;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.MODIFIED_DATE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.MODIFIER;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.NAME;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.NODE_ID;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.NODE_REF;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.SIZEMB;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.STORE_ID;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.VERSION2STORE_ID;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.WORKSPACESTORE_ID;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.alfscript.utils.ConnectionManager;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.github.abhinavmishra14.node.service.NodeScriptService;
import com.github.abhinavmishra14.trashcan.service.ArchiveStoreService;
import com.github.abhinavmishra14.trashcan.service.impl.ArchiveStoreServiceImpl;
import com.google.common.collect.Sets;


/**
 * The Class NodeScriptServiceImpl.
 */
public class NodeScriptServiceImpl implements NodeScriptService {
	
	/** The Constant DELETE_NODE_API. */
	private static final String DELETE_NODE_API = "/alfresco/api/-default-/public/alfresco/versions/1/nodes/%s?permanent=true&alf_ticket=%s";
	
	/** The Constant STORE_MAP. */
	private static final Map<String, String> STORE_MAP = new HashMap<String, String>(5) {
		private static final long serialVersionUID = 1L;

		{
			put(WORKSPACESTORE_ID, "workspace://SpacesStore");
			put(ARCHIVESTORE_ID, "archive://SpacesStore");
			put(VERSION2STORE_ID, "workspace://version2Store");
		}
	};
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(NodeScriptServiceImpl.class);
	
	/** The server endpoint. */
	private final String serverEndpoint;
	
	/**
	 * The Constructor.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public NodeScriptServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}

	/**
	 * Gets the nodes report for content url.
	 *
	 * @param inputFilePath the input file path
	 * @param dbHost the db host
	 * @param dbPort the db port
	 * @param dbUser the db user
	 * @param dbPassword the db password
	 * @return the nodes report for content url
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the SQL exception
	 */
	@Override
	public JSONArray getNodesReportForContentUrl(final String inputFilePath, final String dbHost, final String dbPort,
			final String dbUser, final String dbPassword) throws IOException, SQLException {
		// Read the content urls from input
		final String[] contentUrls = AlfScriptUtils.readFileToString(inputFilePath)
				.replaceAll(CARRIAGE_AND_NEWLINE_REGEX, StringUtils.EMPTY)
				.split(COMMA);
		final Set<String> uniqueContentUrls = Sets.newHashSet(contentUrls);
		LOG.info("Unique ContentUrls Size: " + uniqueContentUrls.size());
		LOG.info("Unique ContentUrls: "+ uniqueContentUrls);
		final JSONArray nodesInfo = new JSONArray();
		for (final String eachContentUrl : uniqueContentUrls) {
			getNodeInfo(eachContentUrl.trim(), nodesInfo, dbHost, dbPort, dbUser, dbPassword);
		}
		if(LOG.isDebugEnabled()) {
		   LOG.debug("Nodes Info JSON: "+nodesInfo);
		}
		return nodesInfo;
	}
	
	/**
	 * Gets the node info.
	 *
	 * @param eachContentUrl the each content url
	 * @param nodesInfo the nodes info
	 * @param dbHost the db host
	 * @param dbPort the db port
	 * @param dbUser the db user
	 * @param dbPassword the db password
	 * @return the node info
	 * @throws SQLException the SQL exception
	 */
	private void getNodeInfo(final String eachContentUrl, final JSONArray nodesInfo, final String dbHost,
			final String dbPort, final String dbUser, final String dbPassword) throws SQLException {
		LOG.info("Extracting node info for content url: "+eachContentUrl);
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			conn = ConnectionManager.getConnection(dbHost, dbPort, dbUser, dbPassword);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			final String selectStmt = String.format("SELECT n.id AS \"Node ID\",\r\n"
					+ "  n.store_id AS \"Store ID\",\r\n"
					+ "  round(u.content_size/1024/1024,2) AS \"Size (MB)\",\r\n"
					+ "  n.uuid AS \"Document ID (UUID)\",\r\n" + "  n.audit_creator AS \"Creator\",\r\n"
					+ "  n.audit_created AS \"Creation Date\",\r\n" + "  n.audit_modifier AS \"Modifier\",\r\n"
					+ "  n.audit_modified AS \"Modification Date\",\r\n"
					+ "  p1.string_value AS \"Document Name\",\r\n" + "  u.content_url AS \"Location\"\r\n"
					+ "FROM alf_node AS n,\r\n" + "  alf_node_properties AS p,\r\n"
					+ "  alf_node_properties AS p1,\r\n" + "  alf_namespace AS ns,\r\n"
					+ "  alf_qname AS q,\r\n" + "  alf_content_data AS d,\r\n" + "  alf_content_url AS u\r\n"
					+ "WHERE n.id=p.node_id\r\n" + "  AND ns.id=q.ns_id\r\n" + "  AND p.qname_id=q.id\r\n"
					+ "  AND p.long_value=d.id\r\n" + "  AND d.content_url_id=u.id\r\n"
					+ "  AND p1.node_id=n.id\r\n"
					+ "  AND p1.qname_id IN (SELECT id FROM alf_qname WHERE local_name='name')\r\n"
					+ "  AND u.content_url='%s';", eachContentUrl);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing query: " + selectStmt);
			}
			resultSet = stmt.executeQuery(selectStmt);
			while (resultSet.next()) {
				final String storeId = resultSet.getString(DB_STORE_ID);
				final String nodeId = resultSet.getString(DB_DOC_ID);
				if (VERSION2STORE_ID.equals(storeId)) {
					//Skip populating workspace://version2store info 
					LOG.info("Skipping version2Store node: "+nodeId);
					continue;
				} else {
					//Populate only workspace://SpaceStore and archive://SpaceStore node info.
					final JSONObject contentNode = new JSONObject();
					contentNode.put(NODE_ID, nodeId);
					contentNode.put(CREATOR, resultSet.getString(DB_CREATOR));
					contentNode.put(CREATEDDATE, resultSet.getString(DB_CREATEDDATE));
					contentNode.put(MODIFIER, resultSet.getString(DB_MODIFIER));
					contentNode.put(MODIFIED_DATE, resultSet.getString(DB_MODIFIED_DATE));
					contentNode.put(NAME, resultSet.getString(DB_DOCNAME));
					contentNode.put(CONTENTURL, eachContentUrl);
					contentNode.put(STORE_ID, storeId);
					final String storeName = STORE_MAP.get(storeId);
					contentNode.put(NODE_REF, String.format("%s/%s", storeName, nodeId));
					contentNode.put(SIZEMB, resultSet.getString(DB_DOCSIZE));
					nodesInfo.put(contentNode);
				}
				
			}
		} catch (SQLException | JSONException excp) {
			LOG.error(excp.getClass().getName() + " : Failed to get the info info due to: " + excp.getMessage(), excp);
			System.exit(0);
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	/**
	 * Delete node.
	 *
	 * @param authTicket the auth ticket
	 * @param nodeJson the node json
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 */
	@Override
	public void deleteNode(final String authTicket, final JSONArray nodeJson)
			throws ClientProtocolException, IOException, URISyntaxException {
		final ArchiveStoreService arcStoreServ = new ArchiveStoreServiceImpl(serverEndpoint);
		for (final Object eachNode : nodeJson) {
			final JSONObject jsonForEachNode = (JSONObject) eachNode;
			final String storeId = jsonForEachNode.getString(STORE_ID);
			final String nodeId = jsonForEachNode.getString(NODE_ID);
			String nodeDelUrl = StringUtils.EMPTY;
			if (ARCHIVESTORE_ID.equals(storeId)) {
				//delete the node from archive://SpaceStore
				arcStoreServ.deleteArchivedNode(nodeId, authTicket);
			} else {
				//delete the node from workspace://SpaceStore
				nodeDelUrl = serverEndpoint + String.format(DELETE_NODE_API, nodeId, authTicket);
				LOG.info("Workspace NodeDelete URL: "+nodeDelUrl);
				try (final CloseableHttpResponse httpDelResp = HTTPUtils.httpDelete(nodeDelUrl)) {
					final StatusLine delStatus = httpDelResp.getStatusLine();
					final int delStatusCode = delStatus.getStatusCode();
					final String delStatusMsg = delStatus.getReasonPhrase();
					LOG.info("Status: "+delStatusCode +" | "+ delStatusMsg);
				} catch (IOException excp) {
					LOG.error("Error occurred while deleting the node", excp);
				}
			}
		}
	}
}
