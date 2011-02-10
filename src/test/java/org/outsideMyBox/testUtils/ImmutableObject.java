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
 * A valid immutable object with read only properties and several constructors. 
 */
public final class ImmutableObject {
	private String        aString                 = null;             // Can only be set by constructor1 and 3.
	private List<String>  aListOfString           = null;             // Can only be set by constructor1.
	private int           anInt                   = 0;                // Can only be set by constructor2.
	private Integer       anInteger               = null;             // Can only be set by constructor3. 
	private final boolean aBooleanPrimitive       = false;            // get only.
	private final Boolean aBoolean                = null;             // get only.
	private final Integer aReadOnlyProperty       = Integer.MAX_VALUE; // get only.
	private final Integer anotherReadOnlyProperty = null;             // get only.

	// Constructor1;
	public ImmutableObject(String aString, List<String> aListOfString) {
		this.aString = aString;
		this.aListOfString = aListOfString;
	}

	// Constructor2.
	public ImmutableObject(int anInt) {
		this.anInt = anInt;
	}

	// Constructor3.
	public ImmutableObject(Integer anInteger) {
		this.anInteger = anInteger;
	}

	public String getAString() {
		return aString;
	}

	public List<String> getAListOfString() {
		return aListOfString;
	}

	public int getAnInt() {
		return anInt;
	}

	public Integer getAnInteger() {
		return anInteger;
	}

	public boolean isABooleanPrimitive() {
		return aBooleanPrimitive;
	}

	public Boolean isABoolean() {
		return aBoolean;
	}

	public Integer getAReadOnlyProperty() {
		return aReadOnlyProperty;
	}

	public Integer getAnotherReadOnlyProperty() {
		return anotherReadOnlyProperty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aListOfString == null) ? 0 : aListOfString.hashCode());
		result = prime * result + ((aString == null) ? 0 : aString.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ImmutableObject other = (ImmutableObject) obj;
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
		return "ImmutableObject [aString=" + aString + ", aListOfString=" + aListOfString + ", anInt=" + anInt + ", anInteger=" + anInteger
		       + ", aBooleanPrimitive=" + aBooleanPrimitive + ", aBoolean=" + aBoolean + ", aReadOnlyProperty=" + aReadOnlyProperty
		       + ", anotherReadOnlyProperty=" + anotherReadOnlyProperty + "]";
	}

}
