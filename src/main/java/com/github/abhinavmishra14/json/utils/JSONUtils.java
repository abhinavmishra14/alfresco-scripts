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
package com.github.abhinavmishra14.json.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The Class JSONUtils.
 */
public final class JSONUtils {

	/** The Constant LOG. */
	private static final Log LOG = LogFactory.getLog(JSONUtils.class);

	/** The Constant EMPTY_JSONOBJECT. */
	public static final String EMPTY_JSONOBJECT = "{}";
	
	/**
	 * The Constructor.
	 */
	private JSONUtils() {
		super();
	}
	
	/**
	 * Convert to json object from file.
	 *
	 * @param filePath the file path
	 * @param aClass the a class
	 * @return the object
	 */
	public static Object convertToJsonObjectFromFile(final String filePath,
			final Class<?> aClass) {
		final ObjectMapper objMapper = getJsonObjectMapper();
		Object jsonObject = null;
		try {
			jsonObject = objMapper.readValue(new File(filePath), aClass);
		} catch (IOException excp) {
			LOG.error("Exception occurred while converting: "+filePath+" to json object ", excp);
		}
		return jsonObject;
	}
	
	/**
	 * Convert to json object from input stream.
	 *
	 * @param inStream the in stream
	 * @param aClass the a class
	 * @return the object
	 */
	public static Object convertToJsonObjectFromInputStream(final InputStream inStream,
			final Class<?> aClass) {
		final ObjectMapper objMapper = getJsonObjectMapper();
		Object jsonObject = null;
		try {
			jsonObject = objMapper.readValue(inStream, aClass);
		} catch (IOException excp) {
			LOG.error("Exception occurred while parsing json ", excp);
		}
		return jsonObject;
	}
	
	/**
	 * Convert to json object from string.
	 *
	 * @param jsonString the json string
	 * @param aClass the a class
	 * @return the object
	 */
	public static Object convertToJsonObjectFromString(final String jsonString,
			final Class<?> aClass) {
		final ObjectMapper objMapper = getJsonObjectMapper();
		Object jsonObject = null;
		try {
			jsonObject = objMapper.readValue(jsonString, aClass);
		} catch (IOException excp) {
			LOG.error("Exception occurred while parsing json ", excp);
		}
		return jsonObject;
	}
	
	/**
	 * Convert from json object to string.<br/>
	 * Here jsonObject parameter could be a Map<K, V> or POJO.
	 *
	 * @param jsonObject the json object
	 * @return the string
	 */
	public static String convertFromJsonObjectToString(final Object jsonObject) {
		return convertFromJsonObjectToString(jsonObject, false);
	}
	
	/**
	 * Convert from json object to string.
	 *
	 * @param jsonObject the json object
	 * @param prettyPrint the pretty print
	 * @return the string
	 */
	public static String convertFromJsonObjectToString(final Object jsonObject,
			final boolean prettyPrint) {
        final ObjectMapper objMapper = getJsonObjectMapper();
        String jsonString = EMPTY_JSONOBJECT;
		try {
			if(prettyPrint) {
				jsonString = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
			} else {
				jsonString = objMapper.writeValueAsString(jsonObject);
			}
		} catch (JsonProcessingException excp) {
			LOG.error("Exception occurred while writting as json string ", excp);
		}
		return jsonString;
	}
	
	/**
	 * Convert from json object to file.<br/>
	 * Here jsonObject parameter could be a Map<K, V> or POJO.
	 *
	 * @param jsonObject the json object
	 * @param filePath the file path
	 */
	public static void convertFromJsonObjectToFile(final Object jsonObject,
			final String filePath) {
        final ObjectMapper objMapper = getJsonObjectMapper();
		try {
			objMapper.writeValue(new File(filePath), jsonObject);
		} catch (IOException excp) {
			LOG.error("Exception occurred while writting as json file ", excp);
		}
	}
	
	/**
	 * Convert from json object to byte array.<br/>
	 * Here jsonObject parameter could be a Map<K, V>  or POJO.
	 *
	 * @param jsonObject the json object
	 * @return the byte[]
	 * @throws FileNotFoundException the file not found exception
	 */
	public static byte[] convertFromJsonObjectToByteArray(
			final Object jsonObject) throws FileNotFoundException {
        final ObjectMapper objMapper = getJsonObjectMapper();
		byte[] jsonByte = new byte[0];
		try {
			jsonByte = objMapper.writeValueAsBytes(jsonObject);
		} catch (JsonProcessingException excp) {
			LOG.error("Exception occurred while writting as json byte [] ", excp);
		}
		return jsonByte;
	}
	
	/**
	 * Gets the json object mapper.
	 *
	 * @return the json object mapper
	 */
	public static ObjectMapper getJsonObjectMapper() {
		final ObjectMapper objMapper = new ObjectMapper();
		objMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		objMapper.enable(SerializationFeature.INDENT_OUTPUT); //Indent the output
		return objMapper;
	}
	
	/**
	 * Combine json objects into one json object.
	 *
	 * @param jsonObjects the json objects
	 * @return the JSON object
	 */
	public static JSONObject combineJsonObjects(final List<JSONObject> jsonObjects) {
        JSONObject combinedJson = null;
        try {
        	combinedJson = new JSONObject();
            for (final JSONObject eachJsonObj : jsonObjects) {
                final Iterator<?> keys = eachJsonObj.keys();
                String temp;
                while (keys.hasNext()) {
                    temp = (String) keys.next();
                    combinedJson.put(temp, eachJsonObj.get(temp));
                }
            }
        } catch (JSONException jsonex) {
        	LOG.error("Exception occurred while combining json objects", jsonex);
        }
        return combinedJson;
    }
	
	/**
	 * Sort json.
	 *
	 * @param jsonArray the json array
	 * @param sortedBy the sorted by
	 * @return the JSON array
	 * @throws JSONException the JSON exception
	 */
	public static JSONArray sortJson(final JSONArray jsonArray,
			final String sortedBy) throws JSONException {
		final JSONArray sortedJsonArray = new JSONArray();
		final List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int each = 0; each < jsonArray.length(); each++) {
			jsonValues.add(jsonArray.getJSONObject(each));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			@Override
	        public int compare(final JSONObject firstObj, final JSONObject secondObj) {
	            String firstVal = StringUtils.EMPTY;
	            String secondVal = StringUtils.EMPTY;
	            try {
	                firstVal = (String) firstObj.get(sortedBy);
	                secondVal = (String) secondObj.get(sortedBy);
	            } catch (JSONException excp) {
	            	LOG.error("Failed to parse json while sorting", excp);
	            }
	            return firstVal.compareTo(secondVal);
	        }
		});
		for (int each = 0; each < jsonValues.size(); each++) {
			sortedJsonArray.put(jsonValues.get(each));
		}
		return sortedJsonArray;
	}
	
	/**
	 * Gets the map fom json string.
	 *
	 * @param jsonString the json string
	 * @return the map fom json string
	 */
	public static Map<String, Object> getMapFomJsonString(final String jsonString) {
		Map<String, Object> map = null;
		try {
			map = getJsonObjectMapper().readValue(jsonString, new TypeReference<Map<String, Object>>(){});
		} catch (IOException ioex) {
			LOG.error("Failed to parse and populate map from jsonString", ioex);
		}
		return map;
	}
	
	/**
	 * Gets the map from json input stream.
	 *
	 * @param inputStreamJson the input stream json
	 * @return the map from json input stream
	 */
	public static Map<String, Object> getMapFromJsonInputStream(
			final InputStream inputStreamJson) {
		Map<String, Object> map = null;
		try {
			final String jsonString = IOUtils.toString(inputStreamJson, StandardCharsets.UTF_8);
			map = getMapFomJsonString(jsonString);
		} catch (IOException ioex) {
			LOG.error("Failed to parse and populate map from json input stream", ioex);
		}
		return map;
	}
	
	/**
	 * Gets the map from json file.
	 *
	 * @param classpathFilePath the class path file path
	 * @return the map from json file
	 */
	public static Map<String, Object> getMapFromJsonFile(
			final String classpathFilePath) {
		final InputStream inputStreamJson = JSONUtils.class.getResourceAsStream(classpathFilePath);		
		return getMapFromJsonInputStream(inputStreamJson);
	}
}
