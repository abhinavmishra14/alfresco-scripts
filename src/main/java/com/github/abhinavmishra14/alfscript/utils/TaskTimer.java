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

import java.util.concurrent.TimeUnit;

/**
 * The Class TaskTimer.<br>
 * This class will be used to get time taken in a particular task.<br>
 * 
 * Here is a pseudo sample for timer:<br><br>
 *       startTimer()<br>
 *       //perform the task<br>
 *       endTimer()<br>
 *       print(getTotalTime())<br>
 */
public class TaskTimer {
	
	/** The start time. */
	private long startTime;
	
	/** The end time. */
	private long endTime;
	
    /** The start nano time. */
    private long startNanoTime;
	
	/** The end nano time. */
	private long endNanoTime;

	/**
	 * Start timer.
	 */
	public void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * End timer.
	 */
	public void endTimer() {
		this.endTime = System.currentTimeMillis();
	}

	/**
	 * Start nano timer.
	 */
	public void startNanoTimer() {
		this.startNanoTime = System.nanoTime();
	}
	
	/**
	 * End nano timer.
	 */
	public void endNanoTimer() {
		this.endNanoTime = System.nanoTime();
	}
	
	/**
	 * Gets the total time.
	 *
	 * @return the total time
	 */
	public long getTotalTime() {
		return this.endTime - this.startTime;
	}

	/**
	 * Gets the total nano time.
	 *
	 * @return the total nano time
	 */
	public long getTotalNanoTime() {
		return this.endNanoTime - this.startNanoTime;
	}
	
	/**
	 * Gets the total time millis.
	 *
	 * @return the total time millis
	 */
	public String getTotalTimeMillis() {
		return getTotalTime()+" (millis)";
	}
	
	/**
	 * Gets the total time nanos.
	 *
	 * @return the total time nanos
	 */
	public String getTotalTimeNanos() {
		return getTotalNanoTime()+" (nanos)";
	}
	
	/**
	 * Gets the formatted total time.<br>
	 * 
	 * @return Returns the total time in minute and seconds
	 */
	public String getFormattedTotalTime() {
		return String.format("%d (min), %d (sec)",
				TimeUnit.MILLISECONDS.toSeconds(getTotalTime()) / 60,
				TimeUnit.MILLISECONDS.toSeconds(getTotalTime()) % 60);
	}	
}