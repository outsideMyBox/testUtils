/**
 * Copyright (C) 2011 Franck Valentin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.outsideMyBox.testUtils;

/**
 * Exception returned when a test fails.
 */
public class BeanLikeTesterException extends RuntimeException {
	private static final long serialVersionUID = 2516780470782371714L;

	public BeanLikeTesterException(String message) {
		super(message);
	}

	public BeanLikeTesterException(String message, Exception e) {
		super(message, e);
	}

}
