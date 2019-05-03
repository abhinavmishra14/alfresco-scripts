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
package com.github.abhinavmishra14.auth.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.auth.service.AuthenticationService;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.github.abhinavmishra14.json.utils.JSONUtils;

/**
 * The Class AuthenticationServiceImpl.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

	/** The server endpoint. */
	private final String serverEndpoint;
	
	/** The Constant AUTH_URI. */
	private static final String AUTH_URI = "/alfresco/service/api/login";
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(AuthenticationServiceImpl.class);

	/**
	 * The Constructor.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public AuthenticationServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}
	
	/**
	 * Gets the auth ticket.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the auth ticket
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws AlfScriptException the alf script exception
	 */
	public String getAuthTicket(final String userName, final String password)
			throws ClientProtocolException, IOException, AlfScriptException {
		LOG.info("Getting auth ticket for user: "+userName);
		String alfTicket = StringUtils.EMPTY;
		final Map<String,String> authPayload = new ConcurrentHashMap<String,String>();
		authPayload.put(AlfScriptConstants.PARAM_USER, userName);
		authPayload.put(AlfScriptConstants.PARAM_PASSWORD, password);
		final String mapAsJson = JSONUtils.getJsonObjectMapper().writeValueAsString(authPayload);	
		final HttpResponse httpResp = HTTPUtils.httpPost(serverEndpoint+AUTH_URI, mapAsJson);
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			final String resonseStr = IOUtils.toString(httpResp.getEntity().getContent(),
					StandardCharsets.UTF_8);
			final JSONObject jsonObj = new JSONObject(resonseStr);
			final JSONObject data = (JSONObject) jsonObj.get(AlfScriptConstants.DATA);
			alfTicket = data.getString(AlfScriptConstants.TICKET);
		} else {
			throw new AlfScriptException(statusMsg);
		}
		return alfTicket;
	}
}
