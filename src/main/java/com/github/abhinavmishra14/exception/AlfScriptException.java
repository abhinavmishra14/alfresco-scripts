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
package com.github.abhinavmishra14.exception;

/**
 * The Class AlfScriptException.
 */
public class AlfScriptException extends RuntimeException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2355465118499812611L;

	/**
	 * Instantiates a new user report exception.
	 *
	 * @param message the message
	 */
	public AlfScriptException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new user report exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public AlfScriptException(final String message, final Throwable cause) {
		super(message, cause);
	}	
}
