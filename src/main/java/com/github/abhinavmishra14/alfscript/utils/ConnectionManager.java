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
package com.github.abhinavmishra14.alfscript.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.abhinavmishra14.exception.AlfScriptException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * The Class ConnectionManager.
 */
public class ConnectionManager {
	
	/** The LOG. */	
	private final static Log LOG = LogFactory.getLog(ConnectionManager.class);
	
	/** The Constant ALF_CONN_POLL_NAME. */
	private static final String ALF_CONN_POLL_NAME = "alfrescoConnectionPool";
	
	/** The Constant PG_DS_NAME. */
	private static final String PG_DS_NAME = "org.postgresql.ds.PGSimpleDataSource";
	
	/** The Constant INIT_QUERY. */
	private static final String INIT_QUERY = "SELECT 1";
	
	/** The Constant DBUSER. */
	private static final String DBUSER = "user";
	
	/** The Constant DBPASS. */
	private static final String DBPASS = "password";
	
	/** The Constant DBSERVERNAME. */
	private static final String DBSERVERNAME = "serverName";
	
	/** The Constant DBPORT. */
	private static final String DBPORT = "portNumber";
	
	/** The Constant DBNAME. */
	private static final String DBNAME = "databaseName";
	
	/** The Constant DB. */
	private static final String DB = "alfresco";

	/** The Constant hikariDs. */
	private static HikariDataSource hikariDs;

	/**
	 * The Constructor.
	 */
	private ConnectionManager() {
		super();
	}

	/**
	 * Gets the connection.
	 *
	 * @param dbHost the db host
	 * @param dbPort the db port
	 * @param dbUser the db user
	 * @param dbPassword the db password
	 * @return the connection
	 * @throws AlfScriptException the alf script exception
	 */
	public static Connection getConnection(final String dbHost, final String dbPort,
			final String dbUser, final String dbPassword) throws AlfScriptException {
		LOG.info("Getting connection from Data Source..");
		Connection connection = null;
		try {
			if (hikariDs == null) {
				initialize(dbHost, dbPort, dbUser, dbPassword);
			}
			connection = hikariDs.getConnection();
			if (connection == null) {
				throw new AlfScriptException("Maximum number of connections has been utilized from pool. Connection will be issues once available!");
			}
			LOG.info("Data Source connection established.");
		} catch (SQLException excp) {
			LOG.error("Exception occurred while getting connection from dataSource", excp);
		}
		return connection;
	}

	/**
	 * Sets the global properties.
	 *
	 * @param dbHost the db host
	 * @param dbPort the db port
	 * @param dbUser the db user
	 * @param dbPassword the db password
	 */
	private static void initialize(final String dbHost, final String dbPort,
			final String dbUser, final String dbPassword) {
		LOG.info("Establishing connection with DataSource at host:" + dbHost + " , port: " + dbPort + " and dbUser: "
				+ dbUser);
		final HikariConfig hikariCfg = new HikariConfig();
		hikariCfg.setPoolName(ALF_CONN_POLL_NAME);
		hikariCfg.setDataSourceClassName(PG_DS_NAME);
		hikariCfg.setConnectionTestQuery(INIT_QUERY);
		//call to getConnection after two unclosed connections will be aborted with error
		hikariCfg.setMaximumPoolSize(2); 
		
		hikariCfg.getDataSourceProperties().put(DBUSER,  dbUser);
		hikariCfg.getDataSourceProperties().put(DBPASS,  dbPassword);
		hikariCfg.getDataSourceProperties().put(DBSERVERNAME, dbHost);
		hikariCfg.getDataSourceProperties().put(DBPORT, dbPort);
		hikariCfg.getDataSourceProperties().put(DBNAME, DB);
		hikariDs = new HikariDataSource(hikariCfg);
	}
}
