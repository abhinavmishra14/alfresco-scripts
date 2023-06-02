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
package com.github.abhinavmishra14.site.service.test;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_HOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PASSWORD;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_USER;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.reports.exception.UserReportException;
import com.github.abhinavmishra14.site.service.SiteService;
import com.github.abhinavmishra14.site.service.impl.SiteServiceImpl;

/**
 * The Class DeleteSites.
 */
public class DeleteSites {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(DeleteSites.class);
		
	/** The Constant ALLOWED_SITES_DEFAULT. */
	private static final String ALLOWED_SITES_DEFAULT [] = { "test-site",
			"test-site-content" };
	
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
		
		String host = DEFAULT_HOST;
		//Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
		}
		String userName = DEFAULT_USER;
		//Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
		}
		String password = DEFAULT_PASSWORD;
		//Get password
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			password = args[2].trim();
		}
		
		String allowedSites [] = ALLOWED_SITES_DEFAULT;
		//Get comma seperated allowed sites. e.g. test-site, test-site2
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			allowedSites = args[3].trim().split(",");
		}
		
		LOG.info("Deleting sites at host: "+host);	
		final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
		final SiteService siteService = new SiteServiceImpl (host);
		final List<String> allSites = siteService.getSiteShortNameList(authTicket);
		LOG.info("Allsites: "+allSites.size()+" |-> "+allSites);
		final List<String> excludeSites = Arrays.asList(allowedSites);
		LOG.info("Excluding: "+excludeSites.size()+" |-> "+excludeSites);

		allSites.removeAll(excludeSites);
		LOG.info("Excluded: "+allSites.size()+" |-> "+allSites);
		for (String shortName : allSites) {
			LOG.info("Deleting: "+shortName);
			siteService.deleteSite(shortName, authTicket);
		}
	}
}
