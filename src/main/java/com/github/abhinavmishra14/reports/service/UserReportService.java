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
package com.github.abhinavmishra14.reports.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import com.github.abhinavmishra14.reports.pojo.Person;
import com.github.abhinavmishra14.reports.pojo.Users;

/**
 * The Interface UserReportService.
 */
public interface UserReportService {
	
	
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
	Person getPerson(final String userName, final String authTicket)
			throws URISyntaxException, ClientProtocolException, IOException;
	
	/**
	 * Gets the all users.
	 *
	 * @param authTicket the auth ticket
	 * @return the all users
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Users getAllUsers(final String authTicket) throws URISyntaxException, IllegalStateException, IOException;	
	
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
	String getPersonAsJson(final String userName, final String authTicket)
			throws URISyntaxException, ClientProtocolException, IOException;
	
	/**
	 * Gets the all users as json.
	 *
	 * @param authTicket the auth ticket
	 * @return the all users as json
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	String getAllUsersAsJson(final String authTicket) throws URISyntaxException, IllegalStateException, IOException;	
}
