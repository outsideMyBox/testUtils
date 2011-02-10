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
 * Bean with invalid hashCode() that doesn't depend on properties.
 */
public final class BeanWithInvalidHash2 {
	private String aString;

	public String getAString() {
		return aString;
	}

	public void setAString(String string) {
		aString = string;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + 1; // Must not depend on the instance id!
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BeanWithInvalidHash2)) {
			return false;
		}
		final BeanWithInvalidHash2 other = (BeanWithInvalidHash2) obj;
		if (aString == null) {
			if (other.aString != null) {
				return false;
			}
		}
		else if (!aString.equals(other.aString)) {
			return false;
		}
		return true;
	}

}
