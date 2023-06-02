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
package com.github.abhinavmishra14.site.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;

import com.github.abhinavmishra14.exception.AlfScriptException;

/**
 * The Interface SiteService.
 */
public interface SiteService {

	/**
	 * Delete site.
	 *
	 * @param shortName the short name
	 * @param authTicket the auth ticket
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	void deleteSite(final String shortName, final String authTicket)
			throws URISyntaxException,
			ClientProtocolException, IOException;
	
	/**
	 * Gets the site short name list.
	 *
	 * @param authTicket the auth ticket
	 * @return the site short name list
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException the IO exception
	 */
	List<String> getSiteShortNameList(final String authTicket)
			throws ClientProtocolException,
			AlfScriptException, URISyntaxException, IOException;
	
	/**
	 * Gets the all sites.
	 *
	 * @param authTicket the auth ticket
	 * @return the all sites
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 */
	JSONArray getAllSites(final String authTicket)
			throws URISyntaxException, 
			ClientProtocolException, AlfScriptException, IOException;
	
	/**
	 * Gets the all sites.
	 *
	 * @param authTicket the auth ticket
	 * @param userName the user name
	 * @return the all sites
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 */
	JSONArray getAllSites(final String authTicket, final String userName)
			throws URISyntaxException, 
			ClientProtocolException, AlfScriptException, IOException;
	
	
	/**
	 * Gets the all sites as string.
	 *
	 * @param authTicket the auth ticket
	 * @return the all sites as string
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 */
	String getAllSitesAsString(final String authTicket)
			throws URISyntaxException, 
			ClientProtocolException, AlfScriptException, IOException;
	
	
	/**
	 * Gets the all sites as string.
	 *
	 * @param authTicket the auth ticket
	 * @param userName the user name
	 * @return the all sites as string
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 */
	String getAllSitesAsString(final String authTicket, final String userName)
			throws URISyntaxException, 
			ClientProtocolException, AlfScriptException, IOException;
}
