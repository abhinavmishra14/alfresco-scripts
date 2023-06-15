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
package com.github.abhinavmishra14.reports.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.github.abhinavmishra14.json.utils.JSONUtils;
import com.github.abhinavmishra14.reports.exception.UserReportException;
import com.github.abhinavmishra14.reports.pojo.Person;
import com.github.abhinavmishra14.reports.pojo.Users;
import com.github.abhinavmishra14.reports.service.UserReportService;
import com.github.abhinavmishra14.site.service.SiteService;
import com.github.abhinavmishra14.site.service.impl.SiteServiceImpl;

/**
 * The Class UserReportServiceImpl.
 */
public class UserReportServiceImpl implements UserReportService {

	/** The server endpoint. */
	private final String serverEndpoint;
	
	/** The Constant PEOPLE_URI. */
	private static final String PEOPLE_URI = "/alfresco/service/api/people";
	
	/** The Constant PERSON_URI. */
	private static final String PERSON_URI = PEOPLE_URI+"/{0}?groups=true";

	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(UserReportServiceImpl.class);

	
	/**
	 * Instantiates a new user report service impl.
	 *
	 * @param serverEndpoint the server endpoint
	 */
	public UserReportServiceImpl(final String serverEndpoint) {
		super();
		this.serverEndpoint = serverEndpoint;
	}

	/**
	 * Gets the person.
	 *
	 * @param userName the user name
	 * @param authTicket the auth ticket
	 * @return the person
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Person getPerson(final String userName, final String authTicket)
			throws URISyntaxException, ClientProtocolException, IOException {
		LOG.info("Getting user profile for user: "+userName);
		final Object[] uriArgs = {userName};
		final MessageFormat msgFormat = new MessageFormat(PERSON_URI);
		final URIBuilder uriBuilder = new URIBuilder(serverEndpoint+msgFormat.format(uriArgs));
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpGet(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		Person person = null;
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			final String resonseStr = IOUtils.toString(httpResp.getEntity().getContent(),
					StandardCharsets.UTF_8);
			final ObjectMapper mapper = JSONUtils.getJsonObjectMapper();
			person = mapper.readValue(resonseStr, Person.class);
			//Set the user's sites which user has access
			final SiteService siteService = new SiteServiceImpl (serverEndpoint);
			final String sites = siteService.getAllSitesAsString(authTicket, userName);
			person.setSiteInfo(mapper.readValue(sites, List.class));
		} else {
			throw new UserReportException(statusMsg);
		}
		return person;
	}
	
	/**
	 * Gets the all users.
	 *
	 * @param authTicket the auth ticket
	 * @return the all users
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public Users getAllUsers(final String authTicket) throws URISyntaxException, IllegalStateException, IOException {
		LOG.info("Getting all users..");
		final URIBuilder uriBuilder = new URIBuilder(serverEndpoint+PEOPLE_URI);
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpGet(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		Users users = null;
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			final String resonseStr = IOUtils.toString(httpResp.getEntity().getContent(),
					StandardCharsets.UTF_8);
			final ObjectMapper mapper = JSONUtils.getJsonObjectMapper();
			users = mapper.readValue(resonseStr, Users.class);
			final List<Person> people = users.getPeople();
			for (int each = 0; each < people.size(); each++) {
				final Person eachPerson = people.get(each);
				final Person personWithGrp = getPerson(eachPerson.getUserName(), authTicket);
				people.set(each, personWithGrp);
			}
		} else {
			throw new UserReportException(statusMsg);
		}
		return users;
	}

	/**
	 * Gets the person as json.
	 *
	 * @param userName the user name
	 * @param authTicket the auth ticket
	 * @return the person as json
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public String getPersonAsJson(final String userName, final String authTicket)
			throws URISyntaxException, ClientProtocolException, IOException {
		String personJson = JSONUtils.EMPTY_JSONOBJECT;
		final Object[] uriArgs = {userName};
		final MessageFormat msgFormat = new MessageFormat(PERSON_URI);
		final URIBuilder uriBuilder = new URIBuilder(serverEndpoint+msgFormat.format(uriArgs));
		uriBuilder.addParameter(AlfScriptConstants.PARAM_AUTH_TICKET, authTicket);
		final HttpResponse httpResp = HTTPUtils.httpGet(uriBuilder.toString());
		final StatusLine status = httpResp.getStatusLine();
		final int statusCode = status.getStatusCode();
		final String statusMsg = status.getReasonPhrase();
		LOG.info("Status: "+statusCode +" | "+ statusMsg);
		if (statusCode == HTTPUtils.HTTP_CODE_200) {
			personJson = IOUtils.toString(httpResp.getEntity().getContent(), StandardCharsets.UTF_8);
		} else {
			throw new UserReportException(statusMsg);
		}
		return personJson;
	}

	/**
	 * Gets the all users as json.
	 *
	 * @param authTicket the auth ticket
	 * @return the all users as json
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public String getAllUsersAsJson(final String authTicket)
			throws URISyntaxException, IllegalStateException, IOException {
		final Users users = getAllUsers(authTicket);
		final ObjectMapper mapper = JSONUtils.getJsonObjectMapper();
		return mapper.writeValueAsString(users);
	}
}
