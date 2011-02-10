package org.outsideMyBox.testUtils.examples;

import java.util.Arrays;
import java.util.List;

import org.outsideMyBox.testUtils.BeanLikeTester;
import org.outsideMyBox.testUtils.BeanLikeTester.ConstructorSignatureAndPropertiesMapping;
import org.outsideMyBox.testUtils.BeanLikeTester.PropertiesAndValues;
import org.testng.annotations.Test;

/**
 * Examples
 *
 */
public final class ExampleTest {
	

	@Test
	public void testBean() {
		// START SNIPPET: snippet1
		// 1. Define the default values expected:
		PropertiesAndValues defaultValues = new PropertiesAndValues();
		defaultValues.put("property1", "defaultValue");
		defaultValues.put("property2", false);
		defaultValues.put("property3", -1L);
		
		// 2. Give another value for each of the properties:
		PropertiesAndValues otherValues = new PropertiesAndValues();
		otherValues.put("property1", "anotherValue");
		otherValues.put("property2", true);
		otherValues.put("property3", 0L);
		
		// 3. Create the tester:
		BeanLikeTester blt = new BeanLikeTester(MyBean.class);

		// 4a. Test the bean's methods:
		blt.testDefaultValues(defaultValues);
		blt.testMutatorsAndAccessors(defaultValues, otherValues);
		blt.testEqualsAndHash(defaultValues, otherValues);
		blt.testToString(defaultValues, otherValues);
		
		// 4b. Or test everything at once.
		blt.testBeanLike(defaultValues, otherValues);
		
		// 5. Check the code coverage. The method equals() may not have been totally covered.
		// In this case create another set of values and run testEqualsAndHash() against it:
		otherValues.put("property1", null);
		otherValues.put("property2", true);
		otherValues.put("property3", 0L);
		blt.testEqualsAndHash(defaultValues, otherValues);

		// If any of the tests fails or the values given in parameter are incorrect an 
		// exception BeanLikeTesterException is thrown.
		
		// END SNIPPET: snippet1
	}
	
	@Test
	public void testBeanLike() {
		
		// START SNIPPET: snippet2
		// 1. Define the mapping between the constructors' signatures and the properties:
		ConstructorSignatureAndPropertiesMapping mapping = new ConstructorSignatureAndPropertiesMapping();
		List<Class<?>> signature1 = Arrays.<Class<?>> asList(String.class);
		mapping.put(signature1, Arrays.asList("property1"));

		List<Class<?>> signature2 = Arrays.<Class<?>> asList(String.class, int.class);
		mapping.put(signature2, Arrays.asList("property1", "property2"));

		// 2. Define the default values expected:
		PropertiesAndValues defaultValues = new PropertiesAndValues();
		defaultValues.put("property1", null);
		defaultValues.put("property2", 0);
		defaultValues.put("property3", false);
		defaultValues.put("aReadOnlyProperty", Integer.MAX_VALUE);

		// 3. Give another value for each of the properties:
		PropertiesAndValues otherValues = new PropertiesAndValues();
		otherValues.put("property1", "aNewString");
		otherValues.put("property2", 3);
		otherValues.put("property3", true);
		// 'property4' is a read only property and can be omitted here.

		// 4. Create the tester:
		BeanLikeTester blt = new BeanLikeTester(MyBeanLike.class, mapping);
		
		// 5. Test the object:
		blt.testBeanLike(defaultValues, otherValues);
		
		// END SNIPPET: snippet2
	}

}
