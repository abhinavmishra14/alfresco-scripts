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
package com.github.abhinavmishra14.auth.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.exception.AlfScriptException;

/**
 * The Interface AuthenticationService.
 */
public interface AuthenticationService {

	/**
	 * Gets the auth ticket.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the auth ticket
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws AlfScriptException the alf script exception
	 */
	String getAuthTicket(final String userName, final String password)
			throws JsonProcessingException, ClientProtocolException, IOException, AlfScriptException;
}
