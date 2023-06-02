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
package com.github.abhinavmishra14.site.service.impl;

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

import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.github.abhinavmishra14.site.service.SiteService;

/**
 * The Class SiteServiceImpl.
 */
public class SiteServiceImpl implements SiteService{
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(SiteServiceImpl.class);
	
	/** The Constant SITES_URL. */
	private static final String SITES_URL = "/alfresco/service/api/sites";
	
	/** The Constant SITES_BY_PERSON_URL. */
	private static final String SITES_BY_PERSON_URL = "/alfresco/service/api/people/{0}/sites?roles=user";
	
	/** The server endpoint. */
	private final String serverEndpoint;
	
	/**
	 * The Constructor.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public SiteServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}

	/**
	 * Delete site.
	 *
	 * @param shortName the short name
	 * @param authTicket the auth ticket
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public void deleteSite(final String shortName, final String authTicket)
			throws URISyntaxException,
			ClientProtocolException, IOException {
		final URIBuilder uriBuilder = new URIBuilder(serverEndpoint + SITES_URL + AlfScriptConstants.PATH_SEPERATOR + shortName);
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpDelete(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
	}
	
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
	public List<String> getSiteShortNameList(final String authTicket)
			throws ClientProtocolException, AlfScriptException, URISyntaxException, IOException {
		final JSONArray sites = getAllSites(authTicket);
		final List<String> allSiteShortNames = new ArrayList<String> ();
		for (int each = 0; each <sites.length(); each++) {
			final JSONObject eachSiteObj = sites.getJSONObject(each);
			if(eachSiteObj.has(AlfScriptConstants.SHORTNAME)) {
				allSiteShortNames.add(eachSiteObj.getString(AlfScriptConstants.SHORTNAME));
			}
		}
		return allSiteShortNames;
	}
	
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
	public JSONArray getAllSites(final String authTicket)
			throws URISyntaxException, ClientProtocolException, AlfScriptException, IOException {
		return new JSONArray(getAllSitesAsString(authTicket));
	}

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
	@Override
	public JSONArray getAllSites(final String authTicket, final String userName)
			throws URISyntaxException, ClientProtocolException, AlfScriptException, IOException {
		return new JSONArray(getAllSitesAsString(authTicket, userName));
	}

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
	@Override
	public String getAllSitesAsString(final String authTicket)
			throws URISyntaxException,
			ClientProtocolException, AlfScriptException, IOException {
		LOG.info("Getting all sites..");
		final URIBuilder uriBuilder = new URIBuilder(serverEndpoint + SITES_URL);
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpGet(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		String sites = StringUtils.EMPTY;
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			sites = IOUtils.toString(httpResp.getEntity().getContent(), StandardCharsets.UTF_8);
		} else {
			throw new AlfScriptException(statusMsg);
		}
		return sites;
	}

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
	@Override
	public String getAllSitesAsString(final String authTicket, final String userName)
			throws URISyntaxException, ClientProtocolException, AlfScriptException, IOException {
		LOG.info("Getting all sites for user: "+userName);
		final Object[] uriArgs = {userName};
		final MessageFormat msgFormat = new MessageFormat(SITES_BY_PERSON_URL);
		final URIBuilder uriBuilder = new URIBuilder(serverEndpoint+msgFormat.format(uriArgs));
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpGet(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		String sites = StringUtils.EMPTY;
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			sites = IOUtils.toString(httpResp.getEntity().getContent(), StandardCharsets.UTF_8);
		} else {
			throw new AlfScriptException(statusMsg);
		}
		return sites;
	}
}
