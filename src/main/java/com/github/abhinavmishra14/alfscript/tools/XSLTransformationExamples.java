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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.abhinavmishra14.alfscript.utils.TaskTimer;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

/**
 * The Class XSLTransformationExamples.
 */
public class XSLTransformationExamples {
	
	/** The Constant LOG. */
	private final static Log LOG = LogFactory.getLog(XSLTransformationExamples.class);

	/**
	 * Transform xsl.
	 *
	 * @param inputXml the input xml
	 * @param inputXSL the input xsl
	 * @param outputFilePath the output file path
	 * @param params the params
	 * @throws TransformerFactoryConfigurationError the transformer factory configuration error
	 * @throws TransformerConfigurationException the transformer configuration exception
	 * @throws TransformerException the transformer exception
	 * @throws IOException the IO exception
	 */
	public static void transformXSL(final InputStream inputXml,
			final InputStream inputXSL, final String outputFilePath,
			final Map<String, Object> params)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException,
			IOException {
		final TaskTimer timer = new TaskTimer();
		timer.startTimer();
		try (final FileOutputStream outputStream = new FileOutputStream(outputFilePath);
				final OutputStreamWriter opStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
				final InputStreamReader inStreamReader = new InputStreamReader(inputXml, StandardCharsets.UTF_8);) {
			final StreamSource inputSnapshot = new StreamSource(inStreamReader);
			final StreamSource styleSheetSrc = new StreamSource(inputXSL);
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();

			transformerFactory.setURIResolver(new ClasspathResourceURIResolver());
			final Transformer transformer = transformerFactory.newTransformer(styleSheetSrc);
			final StreamResult result = new StreamResult(outputStream);
			if (params != null && !params.isEmpty()) {
				for (final Iterator<Entry<String, Object>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
					final Entry<String, Object> eachEntry = iterator.next();
					transformer.setParameter(eachEntry.getKey(), eachEntry.getValue());
				}
			}

			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(inputSnapshot, result);
		}
		timer.endTimer();
		LOG.info("Output written into file. Time taken: "+ timer.getFormattedTotalTime());
	}

	/**
	 * Transform xsl saxon api.
	 *
	 * @param inputXml the input xml
	 * @param xslPath the xsl path
	 * @param outputFilePath the output file path
	 * @param params the params
	 * @throws SaxonApiException the saxon api exception
	 */
	public static void transformXSLSaxonAPI(final InputStream inputXml,
			final String xslPath, final String outputFilePath,
			final Map<String, Object> params) throws SaxonApiException {
		final TaskTimer timer = new TaskTimer();
		timer.startTimer();
		final Processor proc = new Processor(false);
		final XsltCompiler comp = proc.newXsltCompiler();
		final XsltExecutable exp = comp.compile(new StreamSource(xslPath));
		final XdmNode source = proc.newDocumentBuilder().build(new StreamSource(inputXml));
		final Serializer out = proc.newSerializer(new File(outputFilePath));
		out.setOutputProperty(Serializer.Property.METHOD, "xml");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		final XsltTransformer trans = exp.load();

		if (params != null && !params.isEmpty()) {
			for (final Iterator<Entry<String, Object>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
				final Entry<String, Object> eachEntry = iterator.next();
				trans.setParameter(new QName(eachEntry.getKey()), new XdmAtomicValue(eachEntry.getValue().toString()));
			}
		}

		trans.setInitialContextNode(source);
		trans.setDestination(out);
		trans.transform();
		timer.endTimer();
		LOG.info("Output written into file. Time taken: "+ timer.getFormattedTotalTime());
	}

	/**
	 * Xsl transformation using saxon api via uri.
	 *
	 * @param inputXml the input xml
	 * @param inputXSL the input xsl
	 * @param outputFilePath the output file path
	 * @param params the params
	 * @param outputMethod the output method
	 * @param indent the indent
	 * @throws SaxonApiException the saxon api exception
	 */
	public static void transformXSLSaxonAPIViaURI(final InputStream inputXml,
			final InputStream inputXSL, final File outputFilePath,
			final Map<String, Object> params, final String outputMethod,
			final boolean indent) throws SaxonApiException {
		final TaskTimer timer = new TaskTimer();
		timer.startTimer();
		Serializer serializerOutput = null;
		XsltTransformer transformer = null;
		try {
			final Processor proc = new Processor(false);
			final XsltCompiler comp = proc.newXsltCompiler();
			comp.setURIResolver(new ClasspathResourceURIResolver());
			final XsltExecutable exp = comp.compile(new StreamSource(inputXSL));
			final XdmNode source = proc.newDocumentBuilder().build(new StreamSource(inputXml));
			serializerOutput = proc.newSerializer(outputFilePath);
			serializerOutput.setOutputProperty(Serializer.Property.METHOD, outputMethod);
			if (indent) {
				serializerOutput.setOutputProperty(Serializer.Property.INDENT, "yes");
			}

			transformer = exp.load();
			if (params != null && !params.isEmpty()) {
				for (final Iterator<Entry<String, Object>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
					final Entry<String, Object> eachEntry = iterator.next();
					transformer.setParameter(new net.sf.saxon.s9api.QName(eachEntry.getKey()), new XdmAtomicValue(eachEntry.getValue().toString()));
				}
			}

			transformer.setInitialContextNode(source);
			transformer.setDestination(serializerOutput);
			transformer.transform();
		} finally {
			if (transformer != null) {
				transformer.close();
			}
			if (serializerOutput != null) {
				serializerOutput.close();
			}
		}

		timer.endTimer();
		LOG.info("Output written into file. Time taken: "+ timer.getFormattedTotalTime());
	}
}

/**
 * The Class ClasspathResourceURIResolver.
 */
class ClasspathResourceURIResolver implements URIResolver {
	
	/* (non-Javadoc)
	 * @see javax.xml.transform.URIResolver#resolve(java.lang.String, java.lang.String)
	 */
	@Override
	public Source resolve(final String href, final String base) throws TransformerException {
		return new StreamSource(getClass().getClassLoader().getResourceAsStream(href));
	}
}
