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
package com.github.abhinavmishra14.http.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * The Class HTTPUtils.
 */
public final class HTTPUtils {
	
	/** The Constant HTTP_CODE_401. */
	public static final int HTTP_CODE_401 = 401;

	/** The Constant HTTP_CODE_400. */
	public static final int HTTP_CODE_400 = 400;
	
	/** The Constant HTTP_CODE_403. */
	public static final int HTTP_CODE_403 = 403;

	/** The Constant HTTP_CODE_404. */
	public static final int HTTP_CODE_404 = 404;
	
	/** The Constant HTTP_CODE_500. */
	public static final int HTTP_CODE_500 = 500;
	
	/** The Constant HTTP_CODE_409. */
	public static final int HTTP_CODE_409 = 409;
	
	/** The Constant HTTP_CODE_200. */
	public static final int HTTP_CODE_200 = 200;
	
	/** The Constant HTTP_CODE_201. */
    public static final int HTTP_CODE_201 = 201;

	/** The Constant MIME_JSON. */
	public static final String MIME_JSON = "application/json";
	
	/** The Constant UTF8. */
	public static final String UTF8 = "UTF-8";

	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(HTTPUtils.class);
	
	/**
	 * The Constructor.
	 */
	private HTTPUtils() {
		super();
	}
		
	/**
	 * Execute http get.
	 *
	 * @param url the url
	 * @return the input stream
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpGet(final String url)
			throws ClientProtocolException, IOException {
		LOG.info("Sending http get with url: "+ url);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		final HttpGet request = new HttpGet(url);
		return httpclient.execute(request);
	}
	
	/**
	 * Http get tls.
	 *
	 * @param url the url
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static CloseableHttpResponse httpGetTLS(final String url)
			throws ClientProtocolException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http get with url: "+ url);
		final CloseableHttpClient httpclient = createNewClientWithTLS();
		final HttpGet request = new HttpGet(url);
		return httpclient.execute(request);
	}
	
	/**
	 * Execute http get.
	 *
	 * @param url the url
	 * @param userName the user name
	 * @param password the password
	 * @return the input stream
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpGet(final String url,
			final String userName, final String password)
			throws ClientProtocolException, IOException {
		LOG.info("Sending http get with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		final HttpGet request = new HttpGet(url);
		return httpclient.execute(request);
	}
	
	/**
	 * Http get tls.
	 *
	 * @param url the url
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpGetTLS(final String url,
			final String userName, final String password)
			throws ClientProtocolException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http get with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = createNewClientWithCredentialsAndTLS(provider);
		final HttpGet request = new HttpGet(url);
		return httpclient.execute(request);
	}

	/**
	 * Http post.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpPost(final String url,
			final String jsonReqData, final String userName,
			final String password) throws ClientProtocolException, IOException {
		LOG.info("Sending http post with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		final HttpPost httpPost = new HttpPost(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPost.setEntity(httpEntity);		
		return httpclient.execute(httpPost);
	}
	
	/**
	 * Http post tls.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpPostTLS(final String url,
			final String jsonReqData, final String userName,
			final String password) throws ClientProtocolException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http post with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = createNewClientWithCredentialsAndTLS(provider);
		final HttpPost httpPost = new HttpPost(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPost.setEntity(httpEntity);		
		return httpclient.execute(httpPost);
	}
			
	/**
	 * Http post.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpPost(final String url,
			final String jsonReqData) throws ClientProtocolException,
			IOException {
		LOG.info("Sending http post with url: "+ url);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		final HttpPost httpPost = new HttpPost(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPost.setEntity(httpEntity);		
		return httpclient.execute(httpPost);
	}
	
	/**
	 * Http post tls.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpPostTLS(final String url,
			final String jsonReqData) throws ClientProtocolException,
			IOException, KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http post with url: "+ url);
		final CloseableHttpClient httpclient = createNewClientWithTLS();
		final HttpPost httpPost = new HttpPost(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPost.setEntity(httpEntity);		
		return httpclient.execute(httpPost);
	}
	
	/**
	 * Http post.
	 *
	 * @param url the url
	 * @param params the params
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpPost(final String url,
			final List<NameValuePair> params) throws ClientProtocolException,
			IOException {
		LOG.info("Sending http post with url: "+ url+ " params: "+params);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params, UTF8));	
		return httpclient.execute(httpPost);
	}
	
	/**
	 * Http post tls.
	 *
	 * @param url the url
	 * @param params the params
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpPostTLS(final String url,
			final List<NameValuePair> params) throws ClientProtocolException,
			IOException, KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http post with url: "+ url+ " params: "+params);
		final CloseableHttpClient httpclient = createNewClientWithTLS();
		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(params, UTF8));	
		return httpclient.execute(httpPost);
	}
	
	/**
	 * Http put.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpPut(final String url,
			final String jsonReqData) throws ClientProtocolException, IOException {
		LOG.info("Sending http put with url: "+ url);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		final HttpPut httpPut = new HttpPut(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPut.setEntity(httpEntity);
		return httpclient.execute(httpPut);
	}
	
	/**
	 * Http put tls.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpPutTLS(final String url,
			final String jsonReqData) throws ClientProtocolException,
			IOException, KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http put with url: "+ url);
		final CloseableHttpClient httpclient = createNewClientWithTLS();
		final HttpPut httpPut = new HttpPut(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPut.setEntity(httpEntity);
		return httpclient.execute(httpPut);
	}
		
	/**
	 * Http put.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpPut(final String url,
			final String jsonReqData, final String userName,
			final String password) throws ClientProtocolException, IOException {
		LOG.info("Sending http put with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		final HttpPut httpPut = new HttpPut(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPut.setEntity(httpEntity);
		return httpclient.execute(httpPut);
	}
	
	/**
	 * Http put tls.
	 *
	 * @param url the url
	 * @param jsonReqData the json req data
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpPutTLS(final String url,
			final String jsonReqData, final String userName,
			final String password) throws ClientProtocolException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http put with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = createNewClientWithCredentialsAndTLS(provider);
		final HttpPut httpPut = new HttpPut(url);
		final StringEntity httpEntity = new StringEntity(jsonReqData);
		httpEntity.setContentType(MIME_JSON);
		httpPut.setEntity(httpEntity);
		return httpclient.execute(httpPut);
	}
		
	/**
	 * Http delete.
	 *
	 * @param url the url
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpDelete(final String url)
			throws ClientProtocolException, IOException {
		LOG.info("Sending http delete with url: "+ url);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		final HttpDelete httpDelete = new HttpDelete(url);	
		return httpclient.execute(httpDelete);
	}
	
	/**
	 * Http delete tls.
	 *
	 * @param url the url
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpDeleteTLS(final String url)
			throws ClientProtocolException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http delete with url: "+ url);
		final CloseableHttpClient httpclient = createNewClientWithTLS();
		final HttpDelete httpDelete = new HttpDelete(url);	
		return httpclient.execute(httpDelete);
	}
	
	/**
	 * Http delete.
	 *
	 * @param url the url
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 */
	public static CloseableHttpResponse httpDelete(final String url, final String userName,
			final String password) throws ClientProtocolException, IOException {
		LOG.info("Sending http delete with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		final HttpDelete httpDelete = new HttpDelete(url);	
		return httpclient.execute(httpDelete);
	}
	
	/**
	 * Http delete tls.
	 *
	 * @param url the url
	 * @param userName the user name
	 * @param password the password
	 * @return the http response
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	public static CloseableHttpResponse httpDeleteTLS(final String url, final String userName,
			final String password) throws ClientProtocolException, IOException,
			KeyManagementException, NoSuchAlgorithmException {
		LOG.info("Sending http delete with url: "+ url);
		final CredentialsProvider provider = new BasicCredentialsProvider();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				userName, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		final CloseableHttpClient httpclient = createNewClientWithCredentialsAndTLS(provider);
		final HttpDelete httpDelete = new HttpDelete(url);	
		return httpclient.execute(httpDelete);
	}
	
	/**
	 * Convert stream to string.
	 *
	 * @param responseStream the response stream
	 * @return the string
	 * @throws IOException the IO exception
	 */
	public static String convertStreamToString(final InputStream responseStream)
			throws IOException {
		String eachLine = StringUtils.EMPTY;
		try (final Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
				final BufferedReader buffReader = new BufferedReader(reader)) {
			final StringBuffer responseBuff = new StringBuffer();
			while ((eachLine = buffReader.readLine()) != null) {
				responseBuff.append(eachLine);
			}
			return responseBuff.toString();
		}
	}
	
	/**
	 * Gets the string from response.
	 *
	 * @param httpResponse the http response
	 * @return the string from response
	 * @throws IllegalStateException the illegal state exception
	 * @throws IOException the IO exception
	 */
	public static String getStringFromResponse(
			final CloseableHttpResponse httpResponse)
			throws IllegalStateException, IOException {
		try(final InputStream responseStream = httpResponse.getEntity().getContent()) {
			return convertStreamToString(responseStream);
		}
	}
	
	/**
     * Creates the new client.
     *
     * @return the http client
     * @throws KeyManagementException the key management exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
	@Deprecated
	private static CloseableHttpClient createNewClientWithTLS() throws KeyManagementException,
			NoSuchAlgorithmException {
    	final SSLContext sslContext = SSLContexts.custom().useTLS().build();
		final SSLConnectionSocketFactory sslConnFactory = new SSLConnectionSocketFactory(
			    sslContext,  new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},   
			    null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		//Returns the CloseableHttpClient
        return HttpClientBuilder.create().setSSLSocketFactory(sslConnFactory).build();
    }
	
	/**
	 * Creates the new client with credentials and tls.
	 *
	 * @param provider the provider
	 * @return the http client
	 * @throws KeyManagementException the key management exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	@Deprecated
	private static CloseableHttpClient createNewClientWithCredentialsAndTLS(final CredentialsProvider provider)
			throws KeyManagementException, NoSuchAlgorithmException {
		final SSLContext sslContext = SSLContexts.custom().useTLS().build();
		final SSLConnectionSocketFactory sslConnFactory = new SSLConnectionSocketFactory(
				sslContext,  new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},   
				null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		//Returns the CloseableHttpClient
		return HttpClientBuilder.create().setDefaultCredentialsProvider(provider).setSSLSocketFactory(sslConnFactory).build();
	}
}
