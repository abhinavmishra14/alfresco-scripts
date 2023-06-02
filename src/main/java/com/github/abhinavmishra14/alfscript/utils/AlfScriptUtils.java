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
package com.github.abhinavmishra14.alfscript.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONException;

import com.github.abhinavmishra14.auth.service.AuthenticationService;
import com.github.abhinavmishra14.auth.service.impl.AuthenticationServiceImpl;
import com.github.abhinavmishra14.download.pojo.BasicSearchPayload;
import com.github.abhinavmishra14.download.pojo.Paging;
import com.github.abhinavmishra14.download.pojo.Query;
import com.github.abhinavmishra14.exception.AlfScriptException;
import com.github.abhinavmishra14.json.utils.JSONUtils;

/**
 * The Class AlfScriptUtils.
 */
public final class AlfScriptUtils {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(AlfScriptUtils.class);
	
	/**
	 * Gets the search payload.
	 *
	 * @param searchQ the search Q
	 * @param maxItems the max items
	 * @param skipCount the skip count
	 * @return the search payload
	 */
	public static String getSearchPayload(final String searchQ, final String maxItems, final String skipCount) {
		//Example payload:
		/*
		 * {
			  "query" : {
			    "query" : "PATH:'/app:company_home/st:sites/cm:test-site/cm:documentLibrary//*' AND TYPE:'cm:content'",
			    "language" : "afts"
			  },
			  "paging" : {
			    "maxItems" : "1000",
			    "skipCount" : "0"
			  }
			}
		 */
		final BasicSearchPayload searchPayload = new BasicSearchPayload();
		final Query query = new Query();
		query.setQuery(searchQ);
		query.setLanguage(AlfScriptConstants.SEARCH_LANG);
		searchPayload.setQuery(query);
		
		final Paging paging = new Paging();
		paging.setMaxItems(maxItems);
		paging.setSkipCount(skipCount);
		searchPayload.setPaging(paging);
		return JSONUtils.convertFromJsonObjectToString(searchPayload);
	}
	
	/**
	 * Copy input stream to file.<br>
	 * After copy it will flush the output stream and closes it. It will also close the source inputStream after copy
	 *
	 * @param source the source
	 * @param destination the destination
	 * @throws IOException the IO exception
	 */
	public static void copyInputStreamToFile(final InputStream source,
			final File destination) throws IOException {
		try {
			try (final FileOutputStream output = FileUtils.openOutputStream(destination);) {
				IOUtils.copy(source, output);
				try {
					if (output != null) {
						output.flush();
						output.close();
					}
				} catch (IOException ioexIgnore) {
					LOG.warn("[Ignore] Failed to close the output stream", ioexIgnore);
				}
			}
		} finally {
			try {
				if (source != null) {
					source.close();
				}
			} catch (IOException ioexIgnore) {
				LOG.warn("[Ignore] Failed to close the source stream", ioexIgnore);
			}
		}
	}
	
	/**
	 * Gets the file name.
	 *
	 * @param fileName the file name
	 * @param downloadPath the download path
	 * @return the file name
	 */
	public static String getFileName(final String fileName, final String downloadPath) {
		return StringUtils.stripEnd(downloadPath, File.separator) + "/" + fileName;
	}
	
	/**
	 * Sub string using pattern.
	 *
	 * @param aString the a string
	 * @param regEx the reg ex
	 * @return the string
	 */
	public static String subStringUsingPattern(final String aString, final String regEx) {
		String matchedString = StringUtils.EMPTY;
		final Pattern pattern = Pattern.compile(regEx);
		final Matcher matcher = pattern.matcher(aString);
		if (matcher.find()) {                                  
		    matchedString = matcher.group();
		}
		return matchedString;
	}
	
	/**
	 * Gets the ticket.
	 *
	 * @param host the host
	 * @param userName the user name
	 * @param password the password
	 * @return the ticket
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException the IO exception
	 * @throws AlfScriptException the alf script exception
	 */
	public static String getTicket(final String host, final String userName,
			final String password) throws JSONException,
			ClientProtocolException, IOException, AlfScriptException {
		final AuthenticationService authServ = new AuthenticationServiceImpl(host);
		return authServ.getAuthTicket(userName, password);
	}
	
	/**
	 * Gets the start time based on units.<br>
	 * Examples: now, PT10m (10 minutes from now), PT10h (10 hours from now), PT10h10m (10 hours and 10 minutes from now),
	 * P1DT10m (1 day and 10 minutes from now) etc.
	 *
	 * @param input the input
	 * @return the start time based on units
	 */
	public static long getStartTimeBasedOnUnits(final String input) {
		if (AlfScriptConstants.NOW.equalsIgnoreCase(input)) {
			return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		} else {
			final TemporalAmount tempAm = Duration.parse(input);
			LOG.info("Parsed input: " + tempAm);
			final LocalDateTime now = LocalDateTime.now();
			final LocalDateTime nowPlus = now.plus(tempAm);
			return nowPlus.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		}
	}
	
	/**
	 * Gets the date time after number of days.
	 *
	 * @param numberOfDays the number of days
	 * @return the date time after number of days
	 */
	public static String getDateTimeAfterNumberOfDays(final int numberOfDays) {
        final Calendar calendar = Calendar.getInstance();
        // Adding number of days
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);        
        return String.valueOf(calendar.getTime().getTime());
	}
	
	/**
	 * Older than30 days.
	 *
	 * @param givenDate the given date
	 * @param olderThanDays the older than days
	 * @return true, if older than days
	 */
	public static boolean olderThanDays(final String givenDate, final int olderThanDays) {
		final DateTime givenDateTime = new DateTime(givenDate);
		final DateTime currentDateTime = new DateTime();
		if(LOG.isDebugEnabled()) {
			LOG.debug("Comparing date older than days, givenDate: "+givenDateTime+" and currentDate: "+currentDateTime);
		}
	    return Days.daysBetween(givenDateTime, currentDateTime).isGreaterThan(Days.days(olderThanDays));
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
			throw new NoSuchFileException(path, StringUtils.EMPTY, "File is unreadable or doesn't exist!");
		}
		return strContent;
	}
	
	/**
	 * The Constructor.
	 */
	private AlfScriptUtils() {
		super();
	}
}
