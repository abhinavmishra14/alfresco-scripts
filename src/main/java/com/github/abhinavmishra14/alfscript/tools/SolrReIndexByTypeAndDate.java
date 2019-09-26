/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2019. Abhinav Kumar Mishra. 
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
package com.github.abhinavmishra14.alfscript.tools;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_ENDDT;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_HOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_STARTDT;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_TYPE;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;

/**
 * The Class SolrReIndex.
 */
public class SolrReIndexByTypeAndDate {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(SolrReIndexByTypeAndDate.class);

	/** The Constant REINDEX_BY_CREATEED_DATE. */
	private static final String REINDEX_BY_CREATEED_DATE = "%s/solr4/admin/cores?action=REINDEX&query=%s";
	
	/** The Constant QUERY. */
	private static final String QUERY = "TYPE:%s AND @cm\\:created:[%s TO %s]";
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		LOG.info("USAGE: {com.github.abhinavmishra14.alfscript.tools.SolrReIndexByTypeAndDate <host> <objectType> <startDateTime> <endDateTime> } \n -> example: \n http://127.0.0.1:8083 cm:content 2019-09-01T00:00:00 NOW \n -> Date Time format must be in YYYY-MM-DDThh:mm:ss \n Refer for supported date formats: https://lucene.apache.org/solr/guide/6_6/working-with-dates.html \n");
		
		String host = DEFAULT_HOST;
		//Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
		}
		
		String objectType = DEFAULT_TYPE;
		//Get the content type
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			objectType = args[1].trim();
		}
		
		String startDateTime = DEFAULT_STARTDT;
		//Get the host
		if (args.length >= 3 && StringUtils.isNotBlank(args[2])) {
			host = args[2].trim();
		}
		
		String endDateTime = DEFAULT_ENDDT;
		//Get the host
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			host = args[3].trim();
		}
		
		final String query = URLEncoder.encode(String.format(QUERY, objectType, startDateTime, endDateTime), HTTPUtils.UTF8);
		final String reindexURL = String.format(REINDEX_BY_CREATEED_DATE, host, query);
		LOG.info("REINDEX URL: " + reindexURL);
		try (final CloseableHttpResponse httpResp = HTTPUtils.httpGet(reindexURL)) {
			final StatusLine status = httpResp.getStatusLine();
			final int statusCode = status.getStatusCode();
			final String statusMsg = status.getReasonPhrase();
			LOG.info("Status: "+statusCode +" | "+ statusMsg);
			if (statusCode == HTTPUtils.HTTP_CODE_200) {
				final String result = IOUtils.toString(httpResp.getEntity().getContent(), StandardCharsets.UTF_8);
				LOG.info("Result: \n"+result);
			} else {
				throw new AlfScriptException("Failed to start reindexing due to:  "+statusMsg);
			}
		}
	}
}
