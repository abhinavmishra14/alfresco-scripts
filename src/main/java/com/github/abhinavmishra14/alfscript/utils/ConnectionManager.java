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
	 * @param globalProperties the global properties
	 */
	private static void initialize(final String dbHost, final String dbPort,
			final String dbUser, final String dbPassword) {
		LOG.info("Establishing connection with DataSource at host:" + dbHost + " , port: " + dbPort + " and dbUser: "
				+ dbUser);
		final HikariConfig hikariCfg = new HikariConfig();
		hikariCfg.setPoolName("alfrescoConnectionPool");
		hikariCfg.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
		hikariCfg.setConnectionTestQuery("SELECT 1");
		//call to getConnection after two unclosed connections will be aborted with error
		hikariCfg.setMaximumPoolSize(2); 
		
		hikariCfg.getDataSourceProperties().put("user",  dbUser);
		hikariCfg.getDataSourceProperties().put("password",  dbPassword);
		hikariCfg.getDataSourceProperties().put("serverName", dbHost);
		hikariCfg.getDataSourceProperties().put("portNumber", dbPort);
		hikariCfg.getDataSourceProperties().put("databaseName", "alfresco");
		hikariDs = new HikariDataSource(hikariCfg);
	}
}
