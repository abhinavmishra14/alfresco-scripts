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
package com.github.abhinavmishra14.alfscript.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * The Class CopyXMLsIntoAnXMLAtTheEnd.
 */
public class CopyXMLsIntoAnXMLAtTheEnd {

	public static void main(String[] args) throws IOException, XMLStreamException {
		try (FileOutputStream out = new FileOutputStream("C:\\Users\\Abhinav\\Downloads\\XMLTest\\mergedFile2.xml")) {
			XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream("C:\\Users\\Abhinav\\Downloads\\XMLTest\\input.xml"), "UTF-8");
			XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(out, "UTF-8");
			int depth = 0;
			while (reader.hasNext()) {
				XMLEvent outerEvent = reader.nextEvent();
				int eventType = outerEvent.getEventType();
				if (eventType == XMLStreamConstants.START_ELEMENT) {
					depth++;
				} else if (eventType == XMLStreamConstants.END_ELEMENT) {
					depth--;
					if (depth == 0) {
						File dir = new File("C:\\Users\\Abhinav\\Downloads\\XMLTest\\files");
						File[] rootFiles = dir.listFiles();
						for (File rootFile : rootFiles) {
							XMLEventReader xmlEventReader1 = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(rootFile), "UTF-8");
							XMLEvent event = xmlEventReader1.nextEvent();
							// Skip ahead in the input to the opening document element
							while (event.getEventType() != XMLEvent.START_ELEMENT) {
								event = xmlEventReader1.nextEvent();
							}
							do {
								writer.add(event);
								event = xmlEventReader1.nextEvent();
							} while (event.getEventType() != XMLEvent.END_DOCUMENT);
							xmlEventReader1.close();
						}
					}
				}
				writer.add(outerEvent);
			}
			writer.flush();
			writer.close();
		} catch (XMLStreamException | FactoryConfigurationError e) {
			e.printStackTrace();
		}
	}
}
