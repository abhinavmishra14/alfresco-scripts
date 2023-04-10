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

package com.github.abhinavmishra14.node.test;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_HOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PASSWORD;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PGHOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PGPASSWORD;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PGPORT;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PGUSER;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_USER;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.auth.service.AuthenticationService;
import com.github.abhinavmishra14.auth.service.impl.AuthenticationServiceImpl;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.node.service.NodeScriptService;
import com.github.abhinavmishra14.node.service.impl.NodeScriptServiceImpl;

/**
 * The Class DeleteNodesBasedOnContentUrl.
 */
public class DeleteNodesBasedOnContentUrl {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(DeleteNodesBasedOnContentUrl.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the SQL exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public static void main(String[] args) throws JsonProcessingException, ClientProtocolException, AlfScriptException,
			IOException, SQLException, URISyntaxException {
		
		String host = DEFAULT_HOST;
		//Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
		}
		
		String userName = DEFAULT_USER;
		//Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
		}
		
		String password = DEFAULT_PASSWORD;
		//Get password
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			password = args[2].trim();
		}
		
		//Regex to remove line numbers from notepad++ search: Line\s(.*):*Content URL:
		String contenturlsFilePath = StringUtils.EMPTY;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			contenturlsFilePath = args[3].trim();
			if (StringUtils.isEmpty(contenturlsFilePath)) {
				throw new IllegalArgumentException("Please provide a valid text contenturlsFilePath!");
			}
			//A sample input file is available here: /alfresco-scripts/samples/contenturls.txt
			contenturlsFilePath = FilenameUtils.separatorsToSystem(contenturlsFilePath);
		}
		
		String dbHost = DEFAULT_PGHOST;
		//Get database host
		if (args.length >= 5 && StringUtils.isNotBlank(args[4])) {
			dbHost = args[4].trim();
		}
		
		String dbPort = DEFAULT_PGPORT;
		//Get database port
		if (args.length >= 6 && StringUtils.isNotBlank(args[5])) {
			dbPort = args[5].trim();
		}
		
		String dbUser = DEFAULT_PGUSER;
		//Get database user
		if (args.length >= 7 && StringUtils.isNotBlank(args[6])) {
			dbUser = args[6].trim();
		}
		
		String dbPassword = DEFAULT_PGPASSWORD;
		//Get database password
		if (args.length >= 8 && StringUtils.isNotBlank(args[7])) {
			dbPassword = args[7].trim();
		}
		
		final NodeScriptService nodeScriptService = new NodeScriptServiceImpl(host);
		final JSONArray nodesInfo = nodeScriptService.getNodesReportForContentUrl(contenturlsFilePath, dbHost, dbPort,
				dbUser, dbPassword);
		final File reportsFile = new File("nodesInfo.json");
		LOG.info("NodesInfoReport written to the file: "+reportsFile);
		FileUtils.writeStringToFile(reportsFile, nodesInfo.toString(), StandardCharsets.UTF_8);
		
		final AuthenticationService authServ = new AuthenticationServiceImpl(host);
		final String authTicket = authServ.getAuthTicket(userName, password);
		nodeScriptService.deleteNode(authTicket, nodesInfo);
	}
}