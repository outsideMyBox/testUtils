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
 * Bean with invalid accessor that returns a String instead of a boolean.
 */
public final class BeanWithInvalidAccessor2 {
	private String aString;
	private String shouldBeABoolean;

	public String getAString() {
		return aString;
	}

	public String isShouldBeABoolean() {
		return shouldBeABoolean;
	}

}
