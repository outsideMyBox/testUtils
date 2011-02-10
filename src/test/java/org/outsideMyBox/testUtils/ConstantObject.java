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
 * An object whose properties cannot be changed. 
 */
public final class ConstantObject {
	private final String       aString                 = null;             // get only.
	private final List<String> aListOfString           = null;             // get only.
	private final int          anInt                   = 0;                // get only.
	private final Integer      anInteger               = null;             // get only. 
	private final boolean      aBooleanPrimitive       = false;            // get only.
	private final Boolean      aBoolean                = null;             // get only.
	private final Integer      aReadOnlyProperty       = Integer.MAX_VALUE; // get only.
	private final Integer      anotherReadOnlyProperty = null;             // get only.

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
	public String toString() {
		return "ImmutableObject [aString=" + aString + ", aListOfString=" + aListOfString + ", anInt=" + anInt + ", anInteger=" + anInteger
		       + ", aBooleanPrimitive=" + aBooleanPrimitive + ", aBoolean=" + aBoolean + ", aReadOnlyProperty=" + aReadOnlyProperty
		       + ", anotherReadOnlyProperty=" + anotherReadOnlyProperty + "]";
	}

}
