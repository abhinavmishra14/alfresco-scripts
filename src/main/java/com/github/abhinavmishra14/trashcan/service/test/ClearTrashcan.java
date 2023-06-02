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
package com.github.abhinavmishra14.trashcan.service.test;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_BATCH_SIZE;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_DAYS;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_HOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PASSWORD;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_USER;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.trashcan.service.ArchiveStoreService;
import com.github.abhinavmishra14.trashcan.service.impl.ArchiveStoreServiceImpl;

/**
 * The Class ClearTrashcan.
 */
public class ClearTrashcan {
		
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(ClearTrashcan.class);
	
	/**
	 * The main method.
	 *
	 * @param args the args
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public static void main(String[] args) throws
			ClientProtocolException, AlfScriptException, IOException,
			URISyntaxException {
		
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
		
		int olderThanDays = DEFAULT_DAYS;
		if (args.length >= 5 && StringUtils.isNotBlank(args[4])) {
			olderThanDays = Integer.parseInt(args[4].trim());
		}
		LOG.info("Clearing trashcan at host: "+host);	
		final String authTicket = AlfScriptUtils.getTicket(host, userName, password);
		final ArchiveStoreService arcStoreServ = new ArchiveStoreServiceImpl(host);
		final List<String> archivedNodes = arcStoreServ.getArchievedNodesAfterDays(authTicket, batchSize, olderThanDays);
		LOG.info("Total number of nodes found: " +archivedNodes.size()+" older than days: "+olderThanDays);
		final ExecutorService exec = Executors.newFixedThreadPool(archivedNodes.size()+1);
		for (final Iterator<String> iterator = archivedNodes.iterator(); iterator.hasNext();) {
			final String eachNode = iterator.next();
			exec.submit(new Runnable() {
				public void run() {
					try {
						arcStoreServ.deleteArchivedNode(eachNode, authTicket);
					} catch (URISyntaxException | IOException excp) {
						LOG.error("Error occurred while deleting the node", excp);
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
