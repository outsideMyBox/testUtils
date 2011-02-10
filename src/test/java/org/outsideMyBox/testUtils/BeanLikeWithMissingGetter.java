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
 * A invalid beanLike that doesn't have a getter for a property defined in a constructor. 
 */
public final class BeanLikeWithMissingGetter {
	private final String       aString;                                    // Can only be set by a constructor.
	private final List<String> aListOfString;                              // Can only be set by a constructor BUT HAS NOT GETTER.
	private int                anInt;                                      // Can be set by either a constructor or a setter.
	private Integer            anInteger;                                  // Can be set by either a constructor or a setter.
	private boolean            aBooleanPrimitive;                          // Can only be set by a setter.
	private Boolean            aBoolean;                                   // Can only be set by a setter BUT HAS NOT GETTER.
	private final Integer      aReadOnlyProperty       = Integer.MAX_VALUE; // get only.
	private final Integer      anotherReadOnlyProperty = null;             // get only.

	public BeanLikeWithMissingGetter(String aString, List<String> aListOfString) {
		this.aString = aString;
		this.aListOfString = aListOfString;
	}

	// Change the order of the arguments.
	public BeanLikeWithMissingGetter(List<String> aListOfString, String aString) {
		this.aString = aString;
		this.aListOfString = aListOfString;
	}

	public BeanLikeWithMissingGetter(String aString, List<String> aListOfString, int anInt, Integer anInteger) {
		this.aString = aString;
		this.aListOfString = aListOfString;
		this.anInt = anInt;
		this.anInteger = anInteger;
	}

	public String getAString() {
		return aString;
	}

	// Must exist!
	//	public List<String> getAListOfString() {
	//		return aListOfString;
	//	}

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

	// Must exist!
	//	public Boolean isABoolean() {
	//		return aBoolean;
	//	}

	public void setABoolean(Boolean boolean1) {
		aBoolean = boolean1;
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
		result = prime * result + ((aBoolean == null) ? 0 : aBoolean.hashCode());
		result = prime * result + (aBooleanPrimitive ? 1231 : 1237);
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
		if (!(obj instanceof BeanLikeWithMissingGetter)) {
			return false;
		}
		final BeanLikeWithMissingGetter other = (BeanLikeWithMissingGetter) obj;
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

}
