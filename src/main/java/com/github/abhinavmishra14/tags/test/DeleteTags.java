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
package com.github.abhinavmishra14.tags.test;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_BATCH_SIZE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_HOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PASSWORD;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_USER;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.google.common.net.UrlEscapers;

/**
 * The Class DeleteTags.
 */
public class DeleteTags {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(DeleteTags.class);

	/**
	 * The main method.
	 *
	 * @param args the args
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 */
	public static void main(String[] args) throws
			ClientProtocolException, AlfScriptException, IOException {
		
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
		
		int batchSize = DEFAULT_BATCH_SIZE;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			batchSize = Integer.parseInt(args[3].trim());
		}
		final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
		final String tagURL = host+"/alfresco/service/api/tags/workspace/SpacesStore?details=true&from=0&size="+batchSize+"&alf_ticket="+authTicket;
		try (final CloseableHttpResponse httpResp = HTTPUtils.httpGet(tagURL)) {
			final StatusLine status = httpResp.getStatusLine();
			final int statusCode = status.getStatusCode();
			final String statusMsg = status.getReasonPhrase();
			LOG.info("Status: "+statusCode +" | "+ statusMsg);
			if (statusCode == HTTPUtils.HTTP_CODE_200) {
				final String resonseStr = IOUtils.toString(httpResp.getEntity().getContent(),
						StandardCharsets.UTF_8);
				final JSONObject jsonObj = new JSONObject(resonseStr);
				final JSONObject data = jsonObj.getJSONObject("data");
				final JSONArray dataArry = data.getJSONArray("items");
				final ExecutorService exec = Executors.newFixedThreadPool(dataArry.length()+1);
				for (int each = 0; each < dataArry.length(); each++) {
					final JSONObject eachTagObj = dataArry.getJSONObject(each);
					final String eachTagName = eachTagObj.getString("name");
					final String deleteTagURL = host+"/alfresco/service/api/tags/workspace/SpacesStore/"+UrlEscapers.urlFragmentEscaper().escape(eachTagName)+"?alf_ticket="+authTicket;
					exec.submit(new Runnable() {
						public void run() {
							try (final CloseableHttpResponse httpDelResp = HTTPUtils.httpDelete(deleteTagURL)) {
								final StatusLine delStatus = httpResp.getStatusLine();
								final int delStatusCode = delStatus.getStatusCode();
								final String delStatusMsg = delStatus.getReasonPhrase();
								LOG.info("Status: "+delStatusCode +" | "+ delStatusMsg);
							} catch (IOException excp) {
								LOG.error("Error occurred while deleting the tag", excp);
							}
						}
					});				
				}
				
				exec.shutdown();
				try {
					exec.awaitTermination(1, TimeUnit.HOURS);
				} catch (InterruptedException excp) {
					LOG.debug("Thread interrupted: "+excp.getMessage());
				}
			}
		}
	}
}
