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
package com.github.abhinavmishra14.xml.utils;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * The Class XMLUnmarshaller.
 */
public final class XMLUnmarshaller {
 
	/**
	 * The Constructor.
	 */
	private XMLUnmarshaller(){
		super();
	}
	
	/**
	 * Convert xml string to jaxb object.
	 *
	 * @param xmlAsStr the xml as str
	 * @param aClass the a class
	 * @return the object
	 * @throws JAXBException the JAXB exception
	 */
	public static Object convertXmlStringToJaxbObject(final String xmlAsStr,
			final Class<?> aClass) throws JAXBException {
		final StreamSource streamSource = new StreamSource(new StringReader(xmlAsStr));
		return getJaxbObject(streamSource, aClass);
	}
	
	/**
	 * Convert xml input stream to jaxb object.
	 *
	 * @param inStream the in stream
	 * @param aClass the a class
	 * @return the object
	 * @throws JAXBException the JAXB exception
	 */
	public static Object convertXmlInputStreamToJaxbObject(final InputStream inStream,
			final Class<?> aClass) throws JAXBException {
		final StreamSource streamSource = new StreamSource(inStream);
		return getJaxbObject(streamSource, aClass);
	}
		
	/**
	 * Convert xml file to jaxb object.
	 *
	 * @param fileObj the file obj
	 * @param aClass the a class
	 * @return the object
	 * @throws JAXBException the JAXB exception
	 */
	public static Object convertXmlFileToJaxbObject(final File fileObj,
			final Class<?> aClass) throws JAXBException {
		final StreamSource streamSource = new StreamSource(fileObj);
		return getJaxbObject(streamSource, aClass);
	}
	
	/**
	 * Gets the jaxb object.
	 *
	 * @param streamSource the stream source
	 * @param aClass the a class
	 * @return the jaxb object
	 * @throws JAXBException the JAXB exception
	 */
	private static Object getJaxbObject(final StreamSource streamSource,
			final Class<?> aClass) throws JAXBException {
		final JAXBContext jaxbCtx = JAXBContext.newInstance(aClass);
		final Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
		final JAXBElement<?> jaxbElem = unmarshaller.unmarshal(streamSource,aClass);
		return jaxbElem.getValue();
	}
}
