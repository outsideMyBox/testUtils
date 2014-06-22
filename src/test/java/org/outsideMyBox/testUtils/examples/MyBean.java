package org.outsideMyBox.testUtils.examples;

// START SNIPPET: bean
public class MyBean {

	private String  property1 = "defaultValue"; // Read/write.
	private boolean property2;                  // Read/write.
	private long    property3 = -1;             // Read only.

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public boolean isProperty2() {
		return property2;
	}

	public void setProperty2(boolean property2) {
		this.property2 = property2;
	}

	public long getProperty3() {
		return property3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property1 == null) ? 0 : property1.hashCode());
		result = prime * result + (property2 ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyBean other = (MyBean) obj;
		if (property1 == null) {
			if (other.property1 != null)
				return false;
		}
		else if (!property1.equals(other.property1))
			return false;
		if (property2 != other.property2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MyBean [property1=" + property1 + ", property2=" + property2 + ", property3=" + property3 + "]";
	}
	// END SNIPPET: bean	
}
