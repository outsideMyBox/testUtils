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
 * Bean with invalid toString() method that doesn't take account a property.
 */
public final class BeanWithInvalidToString1 {
	private String       aString;
	private String       aStringWithValue = "aValue";
	private List<String> aListOfString;
	private int          anInt;
	private Integer      anInteger;
	private boolean      aBooleanPrimitive;
	private Boolean      aBoolean;

	public String getAString() {
		return aString;
	}

	public void setAString(String string) {
		aString = string;
	}

	public String getAStringWithValue() {
		return aStringWithValue;
	}

	public void setAStringWithValue(String stringWithValue) {
		aStringWithValue = stringWithValue;
	}

	public List<String> getAListOfString() {
		return aListOfString;
	}

	public void setAListOfString(List<String> listOfString) {
		aListOfString = listOfString;
	}

	public int getAnInt() {
		return anInt;
	}

	public void setAnInt(int anInt) {
		this.anInt = anInt;
	}

	public Integer getAnInteger() {
		return anInteger;
	}

	public void setAnInteger(Integer anInteger) {
		this.anInteger = anInteger;
	}

	public boolean isABooleanPrimitive() {
		return aBooleanPrimitive;
	}

	public void setABooleanPrimitive(boolean booleanPrimitive) {
		aBooleanPrimitive = booleanPrimitive;
	}

	public Boolean isABoolean() {
		return aBoolean;
	}

	public void setABoolean(Boolean boolean1) {
		aBoolean = boolean1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aBoolean == null) ? 0 : aBoolean.hashCode());
		result = prime * result + (aBooleanPrimitive ? 1231 : 1237);
		result = prime * result + ((aListOfString == null) ? 0 : aListOfString.hashCode());
		result = prime * result + ((aString == null) ? 0 : aString.hashCode());
		result = prime * result + ((aStringWithValue == null) ? 0 : aStringWithValue.hashCode());
		result = prime * result + anInt;
		result = prime * result + ((anInteger == null) ? 0 : anInteger.hashCode());
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
		if (!(obj instanceof BeanWithInvalidToString1)) {
			return false;
		}
		final BeanWithInvalidToString1 other = (BeanWithInvalidToString1) obj;
		if (aBoolean == null) {
			if (other.aBoolean != null) {
				return false;
			}
		}
		else if (!aBoolean.equals(other.aBoolean)) {
			return false;
		}
		if (aBooleanPrimitive != other.aBooleanPrimitive) {
			return false;
		}
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
		if (aStringWithValue == null) {
			if (other.aStringWithValue != null) {
				return false;
			}
		}
		else if (!aStringWithValue.equals(other.aStringWithValue)) {
			return false;
		}
		if (anInt != other.anInt) {
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

	@Override
	public String toString() {
		return String.format("[aString=%s, aStringWithValue=%s, , aListOfString=%s, , anInt='Missing!', , anInteger=%s, aBooleanPrimitive=%s, aBoolean=%s]",
		                     aString,
		                     aStringWithValue,
		                     aListOfString,
		                     // Missing! anInt,
		                     anInteger,
		                     aBooleanPrimitive,
		                     aBoolean);
	}

}
