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
package com.github.abhinavmishra14.download.service;

import org.json.JSONObject;

/**
 * The Interface DownloadService.
 */
public interface SNDService {
	
	/**
	 * Process download request.
	 *
	 * @param downloadPath the download path
	 * @param authTicket the auth ticket
	 * @param fileName the file name
	 * @param nodeRef the node ref
	 * @return true, if successful
	 */
	boolean processDownloadRequest(final String downloadPath, final String authTicket,
			final String fileName, final String nodeRef);
	
	/**
	 * Gets the search result.
	 *
	 * @param authTicket the auth ticket
	 * @param query the query
	 * @param maxItems the max items
	 * @param skipCount the skip count
	 * @return the search result
	 */
	JSONObject getSearchResult(final String authTicket, final String query, final String maxItems,
			final String skipCount);
	
	/**
	 * Download metadata.
	 *
	 * @param nodeId the node id
	 * @param downloadPath the download path
	 * @param fileName the file name
	 * @param alfTicket the alf ticket
	 * @return true, if successful
	 */
	boolean downloadMetadata(final String nodeId, final String downloadPath, final String fileName,
			final String alfTicket);

}
