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
package com.github.abhinavmishra14.alfscript.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class DirectoryTraverser.<br>
 * It returns set of files by traversing directories recursively.
 *
 * @author Abhinav Kumar Mishra
 * @since 2014
 */
public final class DirectoryTraverser {

	
	/**
	 * Gets the file uris.
	 *
	 * @param startDir the start dir
	 * @return the file uris
	 * @throws FileNotFoundException the file not found exception
	 */
	public static Set<File> getFileUris(final File startDir)
			throws FileNotFoundException {
		checkDirectories(startDir); // throw exception if not valid.
		return getUrisRecursive(startDir);
	}

	/**
	 * Gets the uris recursive.<br>
	 * Recursively traverse each directory and uris of files.
	 *
	 * @param startDir the a starting dir
	 * @return the uris recursive
	 * @throws FileNotFoundException the file not found exception
	 */
	private static Set<File> getUrisRecursive(final File startDir)
			throws FileNotFoundException {
		final Set<File> sortedSetOfFiles = new HashSet<File>();
		final File[] filesAndDirs = startDir.listFiles();
		final List<File> filesDirs = Arrays.asList(filesAndDirs);
		final Iterator<File> filesDirsItr = filesDirs.iterator();
		while (filesDirsItr.hasNext()) {
			final File file = filesDirsItr.next();
			sortedSetOfFiles.add(file); // Add files and directory URIs both
			// If uri is a directory the revisit it recursively.
			if (!file.isFile()) {
				// Call 'getUrisRecursive' to extract uris from directory
				final Set<File> innerSet = getUrisRecursive(file);
				sortedSetOfFiles.addAll(innerSet);
			}
		}
		return sortedSetOfFiles;
	}

	/**
	 * Checks if is valid directory.<br>
	 * If directory exists then it is valid. If directory is valid then it can
	 * be read.
	 *
	 * @param directoryUri the a directory
	 * @throws FileNotFoundException the file not found exception
	 */
	private static void checkDirectories(final File directoryUri)
			throws FileNotFoundException {
		if (directoryUri == null) {
			throw new IllegalArgumentException("Directory should not be null.");
		}if (!directoryUri.exists()) {
			throw new FileNotFoundException("Directory does not exist: "+ directoryUri);
		}if (!directoryUri.isDirectory()) {
			throw new IllegalArgumentException("Is not a directory: "+ directoryUri);
		}if (!directoryUri.canRead()) {
			throw new IllegalArgumentException("Directory cannot be read: "
					+ directoryUri);
		}
	}

	private DirectoryTraverser() {
		super();
	}	
}