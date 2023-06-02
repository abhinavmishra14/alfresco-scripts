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
package com.github.abhinavmishra14.trashcan.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

/**
 * The Interface ArchiveStoreService.
 */
public interface ArchiveStoreService {

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
	List<String> getArchievedNodesAfterDays(final String authTicket,
			final int batchSize, final int olderThanDays)
			throws ClientProtocolException, URISyntaxException, IOException;
	
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
	List<String> getArchievedNodes(final String authTicket,
			final int batchSize) throws ClientProtocolException,
			URISyntaxException, IOException;
	
	/**
	 * Gets the archive space store data.
	 *
	 * @param authTicket the auth ticket
	 * @param batchSize the batch size
	 * @return the archive space store data
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	JSONObject getArchiveSpaceStoreData(final String authTicket,
			final int batchSize) throws URISyntaxException,
			ClientProtocolException, IOException;
	
	/**
	 * Delete archived node.
	 *
	 * @param archivedNode the archived node
	 * @param authTicket the auth ticket
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void deleteArchivedNode(final String archivedNode, final String authTicket)
			throws URISyntaxException, ClientProtocolException, IOException;
}
