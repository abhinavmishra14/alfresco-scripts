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
package com.github.abhinavmishra14.upload.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

/**
 * The Interface UploadService.
 */
public interface UploadService {

	/**
	 * Upload file.
	 *
	 * @param fileToUpload the file to upload
	 * @param metadata the metadata
	 * @param accessToken the access token
	 * @param parentNode the parent node
	 * @return the JSON object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	JSONObject uploadFile(final File fileToUpload, final List<String> metadata, final String accessToken,
			final String parentNode) throws IOException;

}
