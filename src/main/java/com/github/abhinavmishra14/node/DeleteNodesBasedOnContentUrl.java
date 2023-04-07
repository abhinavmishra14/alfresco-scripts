package com.github.abhinavmishra14.node;

import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_HOST;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_PASSWORD;
import static com.github.abhinavmishra14.alfscript.utils.AlfScriptConstants.DEFAULT_USER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abhinavmishra14.auth.service.AuthenticationService;
import com.github.abhinavmishra14.auth.service.impl.AuthenticationServiceImpl;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.http.utils.HTTPUtils;
import com.google.common.collect.Sets;

/**
 * The Class DeleteNodesBasedOnContentUrl.
 */
public class DeleteNodesBasedOnContentUrl {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(DeleteNodesBasedOnContentUrl.class);
	
	/** The Constant DELETE_NODE_API. */
	public static final String DELETE_NODE_API = "/alfresco/api/-default-/public/alfresco/versions/1/nodes/%s?permanent=true&alf_ticket=%s";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws AlfScriptException the alf script exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SQLException the SQL exception
	 */
	public static void main(String[] args)
			throws JsonProcessingException, ClientProtocolException, AlfScriptException, IOException, SQLException {
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
		
		//Regex to remove line numbers from notepad++ search: Line\s(.*):*Content URL:
		String inputFilePath = StringUtils.EMPTY;
		if (args.length >= 4 && StringUtils.isNotBlank(args[3])) {
			inputFilePath = args[3].trim();
			if (StringUtils.isEmpty(inputFilePath)) {
				throw new IllegalArgumentException("Please provide a valid inputFilePath");
			}
			//A sample input file is available here: /alfresco-scripts/samples/contenturls.txt
			inputFilePath = FilenameUtils.separatorsToSystem(inputFilePath);
		}
		
		if(StringUtils.isNotBlank(host) && StringUtils.isNotBlank(userName)
				&& StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(inputFilePath)) {

			final String[] contentUrls = readFileToString(inputFilePath).split(",");
			final Set<String> uniqueContentUrls = Sets.newHashSet(contentUrls);
			LOG.info("UniqueContentUrls Size: " + uniqueContentUrls.size());

			final JSONArray nodesInfo = new JSONArray();
			for (String eachContentUrl : uniqueContentUrls) {
				nodesInfo.put(getNodeInfo(eachContentUrl.trim()));
			}
			LOG.info("NodeJSON: "+nodesInfo);
			final File reportsFile = new File("nodesInfo.json");
			FileUtils.writeStringToFile(reportsFile, nodesInfo.toString(), StandardCharsets.UTF_8); 
			// Read the content urls from input
			deleteNode(host, userName, password, nodesInfo);
		}
	}

	/**
	 * Gets the node info.
	 *
	 * @param eachContentUrl the each content url
	 * @return the node info
	 * @throws SQLException the SQL exception
	 */
	private static JSONObject getNodeInfo(final String eachContentUrl) throws SQLException {
		LOG.info("Extracting node info for content url: "+eachContentUrl);
		Connection conn = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		JSONObject contentNode = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/alfresco",
					"alfresco", "alfresco");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			final String selectStmt = String.format("SELECT n.id AS \"Node ID\",\r\n"
					+ "  n.store_id AS \"Store ID\",\r\n"
					+ "  round(u.content_size/1024/1024,2) AS \"Size (MB)\",\r\n"
					+ "  n.uuid AS \"Document ID (UUID)\",\r\n" + "  n.audit_creator AS \"Creator\",\r\n"
					+ "  n.audit_created AS \"Creation Date\",\r\n" + "  n.audit_modifier AS \"Modifier\",\r\n"
					+ "  n.audit_modified AS \"Modification Date\",\r\n"
					+ "  p1.string_value AS \"Document Name\",\r\n" + "  u.content_url AS \"Location\"\r\n"
					+ "FROM alf_node AS n,\r\n" + "  alf_node_properties AS p,\r\n"
					+ "  alf_node_properties AS p1,\r\n" + "  alf_namespace AS ns,\r\n"
					+ "  alf_qname AS q,\r\n" + "  alf_content_data AS d,\r\n" + "  alf_content_url AS u\r\n"
					+ "WHERE n.id=p.node_id\r\n" + "  AND ns.id=q.ns_id\r\n" + "  AND p.qname_id=q.id\r\n"
					+ "  AND p.long_value=d.id\r\n" + "  AND d.content_url_id=u.id\r\n"
					+ "  AND p1.node_id=n.id\r\n"
					+ "  AND p1.qname_id IN (SELECT id FROM alf_qname WHERE local_name='name')\r\n"
					+ "  AND u.content_url='%s';", eachContentUrl);

			resultSet = stmt.executeQuery(selectStmt);
			while (resultSet.next()) {
				contentNode = new JSONObject();
				contentNode.put("nodeId", resultSet.getString("Document ID (UUID)"));
				contentNode.put("creator", resultSet.getString("Creator"));
				contentNode.put("createdDate", resultSet.getString("Creation Date"));
				contentNode.put("modifier", resultSet.getString("Modifier"));
				contentNode.put("modifiedDate", resultSet.getString("Modification Date"));
				contentNode.put("name", resultSet.getString("Document Name"));
				contentNode.put("contentUrl", eachContentUrl);
			}
		} catch (Exception excp) {
			LOG.error(excp.getClass().getName() + ": " + excp.getMessage(), excp);
			System.exit(0);
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return contentNode;
	}

	/**
	 * Delete node.
	 *
	 * @param host the host
	 * @param userName the user name
	 * @param password the password
	 * @param nodeJson the node json
	 * @throws JsonProcessingException the json processing exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static void deleteNode(final String host, final String userName, final String password, final JSONArray nodeJson)
			throws JsonProcessingException, ClientProtocolException, IOException {
		final AuthenticationService authServ = new AuthenticationServiceImpl(host);
		final String authTicket = authServ.getAuthTicket(userName, password);
		for (final Object eachNode : nodeJson) {
			final JSONObject jsonForEachNode = (JSONObject) eachNode;
			final String nodeDelUrl = host
					+ String.format(DELETE_NODE_API, jsonForEachNode.get("nodeId"), authTicket);
			LOG.info("NodeDelURL: "+nodeDelUrl);
			try (final CloseableHttpResponse httpDelResp = HTTPUtils.httpDelete(nodeDelUrl)) {
				final StatusLine delStatus = httpDelResp.getStatusLine();
				final int delStatusCode = delStatus.getStatusCode();
				final String delStatusMsg = delStatus.getReasonPhrase();
				LOG.info("Status: "+delStatusCode +" | "+ delStatusMsg);
			} catch (IOException excp) {
				LOG.error("Error occurred while deleting the node", excp);
			}
		}
	}
	
	/**
	 * Read file to string.
	 *
	 * @param path the path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String readFileToString(final String path) throws IOException {
		String strContent = StringUtils.EMPTY;
		final Path filePath = Paths.get(path); 
		if (Files.isRegularFile(filePath)) {
			// Open file with read option only to allow for file deletion and
			// modifications from other programs.
			try (final InputStream inStream = Files.newInputStream(filePath, StandardOpenOption.READ)) {
				strContent = IOUtils.toString(inStream, StandardCharsets.UTF_8);
			}
		} else {
			throw new NoSuchFileException(path, StringUtils.EMPTY, "Invalid file!");
		}
		return strContent;
	}
}