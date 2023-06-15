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
package com.github.abhinavmishra14.site.service.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.reports.exception.UserReportException;
import com.github.abhinavmishra14.site.service.SiteService;
import com.github.abhinavmishra14.site.service.impl.SiteServiceImpl;

/**
 * The Class GenerateSiteUsersMembershipReport.<br>
 * This report will provide the list of users with their role in a given site.
 */
public class GenerateSiteUsersMembershipReport {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(GenerateSiteUsersMembershipReport.class);
	
	/**
	 * The main method.
	 *
	 * @param args the args
	 * @throws ClientProtocolException the client protocol exception
	 * @throws UserReportException the user report exception
	 * @throws IOException the IO exception
	 * @throws IllegalStateException the illegal state exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public static void main(final String args[]) throws ClientProtocolException, UserReportException, IOException,
			IllegalStateException, URISyntaxException {
		
		String host = StringUtils.EMPTY;
		// Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
			if (StringUtils.isEmpty(host) || !host.contains("://")) {
				throw new IllegalArgumentException("Please provide a valid host");
			}
		}

		String userName = StringUtils.EMPTY;
		// Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
			if (StringUtils.isEmpty(userName)) {
				throw new IllegalArgumentException("Please provide a valid userName");
			}
		}

		String password = StringUtils.EMPTY;
		// Get password
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			password = args[2].trim();
			if (StringUtils.isEmpty(password)) {
				throw new IllegalArgumentException("Please provide a valid password");
			}
		}
		
		String siteShortName = StringUtils.EMPTY;
		//Get siteShortName
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			siteShortName = args[3].trim();
			if (StringUtils.isEmpty(siteShortName)) {
				throw new IllegalArgumentException("Please provide a valid siteShortName");
			}
		}
		
		if (StringUtils.isNotBlank(host) && StringUtils.isNotBlank(userName)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(siteShortName)) {
			final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
			final SiteService siteService = new SiteServiceImpl(host);
			final String siteMembership = siteService.getSiteMembershipPersonInfo(siteShortName, authTicket);
			final File reportsFile = new File("siteUsersMembershipReport.json");
			FileUtils.writeStringToFile(reportsFile, siteMembership, StandardCharsets.UTF_8);
			LOG.info("Site Users Membership Report generated successfully.");
		} else {
			throw new IllegalArgumentException(
					"Please check if you have provided the parameters required for site user membership report. \n GenerateSiteUsersMembershipReport generation command: java com.github.abhinavmishra14.site.service.test.GenerateSiteUsersMembershipReport <host> <user> <password> <siteShortName>");
		}
	}
}