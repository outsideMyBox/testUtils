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

import java.util.List;

/**
 * Bean with invalid hashCode() that doesn't take account a property.
 */
public final class BeanWithInvalidHash1 {
	private String       aString;
	private List<String> aListOfString;
	private Integer      anInteger;

	public String getAString() {
		return aString;
	}

	public void setAString(String string) {
		aString = string;
	}

	public List<String> getAListOfString() {
		return aListOfString;
	}

	public void setAListOfString(List<String> listOfString) {
		aListOfString = listOfString;
	}

	public Integer getAnInteger() {
		return anInteger;
	}

	public void setAnInteger(Integer anInteger) {
		this.anInteger = anInteger;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aListOfString == null) ? 0 : aListOfString.hashCode());
		result = prime * result + ((aString == null) ? 0 : aString.hashCode());
		// Missing! result = prime * result + ((anInteger == null) ? 0 : anInteger.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BeanWithInvalidHash1)) {
			return false;
		}
		final BeanWithInvalidHash1 other = (BeanWithInvalidHash1) obj;
		if (aListOfString == null) {
			if (other.aListOfString != null) {
				return false;
			}
		}
		else if (!aListOfString.equals(other.aListOfString)) {
			return false;
		}
		if (aString == null) {
			if (other.aString != null) {
				return false;
			}
		}
		else if (!aString.equals(other.aString)) {
			return false;
		}
		if (anInteger == null) {
			if (other.anInteger != null) {
				return false;
			}
		}
		else if (!anInteger.equals(other.anInteger)) {
			return false;
		}
		return true;
	}

}
