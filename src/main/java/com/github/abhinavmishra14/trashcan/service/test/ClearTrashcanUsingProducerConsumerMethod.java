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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.alfscript.utils.AlfScriptUtils;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.reports.exception.UserReportException;
import com.github.abhinavmishra14.trashcan.service.ArchiveStoreService;
import com.github.abhinavmishra14.trashcan.service.impl.ArchiveStoreServiceImpl;

/**
 * The Class ClearTrashcanUsingProducerConsumerMethod.
 */
public class ClearTrashcanUsingProducerConsumerMethod {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(ClearTrashcanUsingProducerConsumerMethod.class);
	
	/**
	 * The main method.
	 *
	 * @param args the args
	 * @throws InterruptedException the interrupted exception
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException the IO exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	public static void main(String[] args) throws InterruptedException,
			JsonProcessingException, ClientProtocolException,
			AlfScriptException, IOException, URISyntaxException {

		String host = DEFAULT_HOST;
		// Get the host
		if (args.length >= 1 && StringUtils.isNotBlank(args[0])) {
			host = args[0].trim();
		}
		String userName = DEFAULT_USER;
		// Get userName
		if (args.length >= 2 && StringUtils.isNotBlank(args[1])) {
			userName = args[1].trim();
		}
		String password = DEFAULT_PASSWORD;
		// Get password
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
		final TrashCanBuffer buffer = new TrashCanBuffer(batchSize, authTicket, arcStoreServ, olderThanDays);

		final Thread nodeProducer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					buffer.produceNodes();
				} catch (InterruptedException | URISyntaxException
						| IOException excp) {
					LOG.error("Failed during production of nodes", excp);
				}
			}
		});

		final Thread nodeConsumer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					buffer.deleteNodes();
				} catch (InterruptedException | UserReportException
						| URISyntaxException | IOException excp) {
					LOG.error("Failed during consumption of nodes", excp);
				}
			}
		});

		nodeProducer.start();
		nodeConsumer.start();
		nodeProducer.join();
		nodeConsumer.join();
	}

	/**
	 * The Class Buffer.
	 */
	static class TrashCanBuffer {
		
		/** The list. */
		private Queue<String> list;
		
		/** The batch size. */
		private int batchSize;
		
		/** The arc store serv. */
		private ArchiveStoreService arcStoreServ;
		
		/** The alf ticket. */
		private String alfTicket;
		
		/** The older than days. */
		private int olderThanDays;
		
		/**
		 * The Constructor.
		 *
		 * @param batchSize the batch size
		 * @param alfTicket the alf ticket
		 * @param arcStoreServ the arc store serv
		 * @param olderThanDays the older than days
		 */
		public TrashCanBuffer(final int batchSize, final String alfTicket,
				final ArchiveStoreService arcStoreServ, final int olderThanDays) {
			this.list = new LinkedList<String>();
			this.batchSize = batchSize;
			this.alfTicket = alfTicket;
			this.arcStoreServ = arcStoreServ;
			this.olderThanDays = olderThanDays;
		}

		/**
		 * Produce nodes.
		 *
		 * @throws InterruptedException the interrupted exception
		 * @throws ClientProtocolException the client protocol exception
		 * @throws URISyntaxException the URI syntax exception
		 * @throws IOException the IO exception
		 */
		public void produceNodes() throws InterruptedException,
				ClientProtocolException, URISyntaxException, IOException {
			while (true) {
				synchronized (this) {
					while (list.size() >= batchSize) {
						// wait for the consumer
						wait();
					}
					LOG.info("Producing nodes in a batch of: "+batchSize);
					final List<String> archivedNodes = arcStoreServ.getArchievedNodesAfterDays(alfTicket, batchSize, olderThanDays);
					LOG.info("Total number of nodes found: " +archivedNodes.size()+" older than days: "+olderThanDays);
					if (archivedNodes != null && !archivedNodes.isEmpty()) {
						list.addAll(archivedNodes);
					} else {
						LOG.info("No more nodes to produce, exiting the process..");
						System.exit(0);
					}
					
					// notify the consumer
					notify();
					Thread.sleep(1000);
				}
			}
		}

		/**
		 * Delete nodes.
		 *
		 * @throws InterruptedException the interrupted exception
		 * @throws UserReportException the user report exception
		 * @throws JsonProcessingException the json processing exception
		 * @throws ClientProtocolException the client protocol exception
		 * @throws URISyntaxException the URI syntax exception
		 * @throws IOException the IO exception
		 */
		public void deleteNodes() throws InterruptedException,
				UserReportException, JsonProcessingException,
				ClientProtocolException, URISyntaxException, IOException {
			while (true) {
				synchronized (this) {
					while (list.size() == 0) {
						// wait for the producer
						wait();
					}
					LOG.info("Deleting node..");
					arcStoreServ.deleteArchivedNode(list.poll(), alfTicket);

					// notify the producer
					notify();
					Thread.sleep(1000);
				}
			}
		}
	}
}