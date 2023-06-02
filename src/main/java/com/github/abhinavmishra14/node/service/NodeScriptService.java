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
package com.github.abhinavmishra14.node.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;


/**
 * The Interface NodeScriptService.
 */
public interface NodeScriptService {

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
	JSONArray getNodesReportForContentUrl(final String inputFilePath, final String dbHost, final String dbPort,
			final String dbUser, final String dbPassword) throws IOException, SQLException;
	
	/**
	 * Delete node.
	 *
	 * @param authTicket the auth ticket
	 * @param nodeJson the node json
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 */
	void deleteNode(final String authTicket, final JSONArray nodeJson)
			throws ClientProtocolException, IOException, URISyntaxException;
}
