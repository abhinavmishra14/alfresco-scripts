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
package com.github.abhinavmishra14.alfscript.utils;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.auth.service.AuthenticationService;
import com.github.abhinavmishra14.auth.service.impl.AuthenticationServiceImpl;
import com.github.abhinavmishra14.exception.AlfScriptException;

/**
 * The Class AlfScriptUtils.
 */
public final class AlfScriptUtils {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(AlfScriptUtils.class);

	/**
	 * Gets the ticket.
	 *
	 * @param host the host
	 * @param userName the user name
	 * @param password the password
	 * @return the ticket
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws AlfScriptException the alf script exception
	 */
	public static String getTicket(final String host, final String userName,
			final String password) throws JsonProcessingException,
			ClientProtocolException, IOException, AlfScriptException {
		final AuthenticationService authServ = new AuthenticationServiceImpl(host);
		return authServ.getAuthTicket(userName, password);
	}
	
	/**
	 * Older than30 days.
	 *
	 * @param givenDate the given date
	 * @param olderThanDays the older than days
	 * @return true, if older than days
	 */
	public static boolean olderThanDays(final String givenDate, final int olderThanDays) {
		final DateTime givenDateTime = new DateTime(givenDate);
		final DateTime currentDateTime = new DateTime();
		if(LOG.isDebugEnabled()) {
			LOG.debug("Comparing date older than days, givenDate: "+givenDateTime+" and currentDate: "+currentDateTime);
		}
	    return Days.daysBetween(givenDateTime, currentDateTime).isGreaterThan(Days.days(olderThanDays));
	}
	
	/**
	 * The Constructor.
	 */
	private AlfScriptUtils() {
		super();
	}
}
