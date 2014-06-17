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

import java.util.Arrays;
import java.util.List;

/**
 * A valid bean.
 */
public final class ValidBean {
	private String        aString;                                    // set and get.
	private String        aStringWithValue        = "aValue";         // set and get.
	private List<String>  aListOfString;                              // set and get.
	private int           anInt;                                      // set and get.
	private Integer       anInteger;                                  // set and get.
	private boolean       aBooleanPrimitive;                          // set and get.
	private Boolean       aBoolean;                                   // set and get.
	private String[]      anArray;                                    // set and get.
	private int[]         anArrayOfPrimitives;                       // set and get.
	private String[]      anArrayWithValues = new String[] {"default1", "default2", "default3"}; // set and get.

	private final Integer aReadOnlyProperty       = Integer.MAX_VALUE; // get only.
	private final Integer anotherReadOnlyProperty = null;             // get only.

	public void setAString(String string) {
		aString = string;
	}

	public String getAString() {
		return aString;
	}

	public void setAStringWithValue(String stringWithValue) {
		aStringWithValue = stringWithValue;
	}

	public String getAStringWithValue() {
		return aStringWithValue;
	}

	public void setAListOfString(List<String> listOfString) {
		aListOfString = listOfString;
	}

	public List<String> getAListOfString() {
		return aListOfString;
	}

	public void setAnInt(int anInt) {
		this.anInt = anInt;
	}

	public int getAnInt() {
		return anInt;
	}

	public void setAnInteger(Integer anInteger) {
		this.anInteger = anInteger;
	}

	public Integer getAnInteger() {
		return anInteger;
	}

	public void setABooleanPrimitive(boolean booleanPrimitive) {
		aBooleanPrimitive = booleanPrimitive;
	}

	public boolean isABooleanPrimitive() {
		return aBooleanPrimitive;
	}

	public void setABoolean(Boolean boolean1) {
		aBoolean = boolean1;
	}

	public Boolean isABoolean() {
		return aBoolean;
	}

	public void setAnArray(String[] array) { anArray = array; }

	public String[] getAnArray() { return anArray; }

	public void setAnArrayOfPrimitives(int[] array) { anArrayOfPrimitives = array; }

	public int[] getAnArrayOfPrimitives() { return anArrayOfPrimitives; }

	public void setAnArrayWithValues(String[] array) { anArrayWithValues = array; }

	public String[] getAnArrayWithValues() { return anArrayWithValues; }

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
		result = prime * result + ((aStringWithValue == null) ? 0 : aStringWithValue.hashCode());
		result = prime * result + anInt;
		result = prime * result + ((anInteger == null) ? 0 : anInteger.hashCode());
		result = prime * result + ((anArray == null) ? 0 : Arrays.deepHashCode(anArray));
		result = prime * result + ((anArrayOfPrimitives == null) ? 0 : Arrays.hashCode(anArrayOfPrimitives));
		result = prime * result + ((anArrayWithValues == null) ? 0 : Arrays.deepHashCode(anArrayWithValues));
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
		if (!(obj instanceof ValidBean)) {
			return false;
		}
		final ValidBean other = (ValidBean) obj;
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
		if (anArray == null) {
			if (other.anArray != null) {
				return false;
			}
		}
		else if (!Arrays.deepEquals(anArray, other.anArray)) {
			return false;
		}
		if (anArrayOfPrimitives == null) {
			if (other.anArrayOfPrimitives != null) {
				return false;
			}
		}
		else if (!Arrays.equals(anArrayOfPrimitives, other.anArrayOfPrimitives)) {
			return false;
		}
		if (anArrayWithValues == null) {
			if (other.anArrayWithValues != null) {
				return false;
			}
		}
		else if (!Arrays.deepEquals(anArrayWithValues, other.anArrayWithValues)) {
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
		return String.format("[aString=%s, aStringWithValue=%s, , aListOfString=%s, , anInt=%s, , anInteger=%s, aBooleanPrimitive=%s, aBoolean=%s, anArray=%s, anArrayOfPrimitives=%s, anArrayWithValues=%s]",
		                     aString,
		                     aStringWithValue,
		                     aListOfString,
		                     anInt,
		                     anInteger,
		                     aBooleanPrimitive,
		                     aBoolean,
		                     Arrays.deepToString(anArray),
		                     Arrays.toString(anArrayOfPrimitives),
		                     Arrays.deepToString(anArrayWithValues));
	}

}
