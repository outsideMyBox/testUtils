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
package org.outsideMyBox.testUtils.examples;

import java.util.List;

// START SNIPPET: beanLike
public final class MyBeanLike {
	private final String       property1;                      // Can only be set by a constructor.
	private int                property2;                      // Can be set by either a constructor or a setter.
	private boolean            property3;                      // Can only be set by a setter.
	private final Integer      property4  = Integer.MAX_VALUE; // get only.

	public MyBeanLike(String property1) {
		this.property1 = property1;
	}

	public MyBeanLike(String property1, int property2) {
		this.property1 = property1;
		this.property2 = property2;
	}

	public String getProperty1() {
		return property1;
	}

	public int getProperty2() {
		return property2;
	}

	public void setProperty2(int property2) {
		this.property2 = property2;
	}

	public boolean isProperty3() {
		return property3;
	}

	public void setProperty3(boolean property3) {
		this.property3 = property3;
	}

	public Integer getAReadOnlyProperty() {
		return property4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (property3 ? 1231 : 1237);
		result = prime * result + ((property1 == null) ? 0 : property1.hashCode());
		result = prime * result + property2;
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
		if (!(obj instanceof MyBeanLike)) {
			return false;
		}
		final MyBeanLike other = (MyBeanLike) obj;
		if (property3 != other.property3) {
			return false;
		}
		if (property1 == null) {
			if (other.property1 != null) {
				return false;
			}
		}
		else if (!property1.equals(other.property1)) {
			return false;
		}
		if (property2 != other.property2) {
			return false;
		}
		return true;
	}
}
// END SNIPPET: beanLike
