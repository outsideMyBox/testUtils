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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.outsideMyBox.testUtils.BeanLikeTester.ConstructorSignatureAndPropertiesMapping;
import org.outsideMyBox.testUtils.BeanLikeTester.PropertiesAndValues;
import org.testng.annotations.Test;

/**
 * Test {@link BeanLikeTester}
 */
public final class BeanLikeTesterTest {

	// -------------------------------------------------------------------------
	// Test normal beans
	// -------------------------------------------------------------------------

	private PropertiesAndValues returnExpectedDefaultValuesForValidBean() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		defaultValues.put("aStringWithValue", "aValue");
		defaultValues.put("aListOfString", null);
		defaultValues.put("anInt", 0);
		defaultValues.put("anInteger", null);
		defaultValues.put("aBoolean", null);
		defaultValues.put("anArray", null);
		defaultValues.put("anArrayOfPrimitives", null);
		defaultValues.put("anArrayWithValues", new String[] {"default1", "default2", "default3"});
		defaultValues.put("aBooleanPrimitive", false);
		defaultValues.put("aReadOnlyProperty", Integer.MAX_VALUE);
		defaultValues.put("anotherReadOnlyProperty", null);
		return defaultValues;
	}

	private PropertiesAndValues returnCorrectValuesForValidBean() {
		final PropertiesAndValues values = new PropertiesAndValues();
		values.put("aString", "anotherValue");
		values.put("aStringWithValue", "anotherValue");
		values.put("aListOfString", new ArrayList<String>());
		values.put("anInt", 10);
		values.put("anInteger", 11);
		values.put("aBoolean", Boolean.FALSE);
		values.put("anArray", new String[] {"new A", "new B", "new C"});
		values.put("anArrayOfPrimitives", new int[] {1, 2, 3});
		values.put("anArrayWithValues", new String[] {"new 1", "new 2", "new 3"});
		values.put("aBooleanPrimitive", true);
		// 'aReadOnlyProperty' and 'anotherReadOnlyProperty' are read only property and
		// hence not set here.
		return values;
	}

	private PropertiesAndValues returnOtherCorrectValuesForValidBean() {
		final PropertiesAndValues values = new PropertiesAndValues();
		values.put("aString", "anotherValue");
		values.put("aStringWithValue", null);
		values.put("aListOfString", Arrays.asList("anElt"));
		values.put("anInt", -10);
		values.put("anInteger", -11);
		values.put("aBoolean", Boolean.TRUE);
		values.put("anArray", new String[] {"other A", "other B", "other C"});
		values.put("anArrayOfPrimitives", new int[] {100, 200, 300});
		values.put("anArrayWithValues", new String[] {"other 1", "other 2", "other 3"});
		values.put("aBooleanPrimitive", true);
		return values;
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The following properties don't have any accessor:\\[aBoolean\\]")
	public void testBeanWithMissingGetter() {
		new BeanLikeTester(BeanWithMissingGetter.class);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The method 'getNothing' doesn't return an object")
	public void testInvalidAccessorReturningVoid() {
		new BeanLikeTester(BeanWithInvalidAccessor1.class);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The method 'isShouldBeABoolean' doesn't return a boolean")
	public void testInvalidAccessorNotReturningABoolean() {
		new BeanLikeTester(BeanWithInvalidAccessor2.class);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The method 'setAString' must not return an object.")
	public void testInvalidSetterReturningAnObject() {
		new BeanLikeTester(BeanWithInvalidSetter.class);
	}

	@Test
	public void testInitialValuesOfValidBean() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The set of properties to test is different from the properties accessible from the object:\nFrom object: .+\nTo test:     .+")
	public void testInitialValuesOfValidBeanButNotAllPropertiesTested() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		defaultValues.remove("aListOfString");
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The set of properties to test is different from the properties accessible from the object:\nFrom object: .+\nTo test:     .+")
	public void testInitialValuesOfValidBeanButUnknownProperties() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		defaultValues.put("unexepectedProperty", null);
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The value of the property 'aString' returned (.+) is not the same as the one expected (.+)")
	public void testInitialValuesOfValidBeanButUnexpectedValueReturned1() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		defaultValues.put("aString", "notExpected"); // Should be null.
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The value of the property 'aStringWithValue' returned (.+) is not the same as the one expected (.+)")
	public void testInitialValuesOfValidBeanButUnexpectedValueReturned2() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		defaultValues.put("aStringWithValue", null); // Should be "value".
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The value of the property 'aStringWithValue' returned (.+) is not the same as the one expected (.+)")
	public void testInitialValuesOfValidBeanButUnexpectedValueReturned3() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		defaultValues.put("aStringWithValue", "anotherValue"); // Should be "value".
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The value of the property 'anInt' returned (.+) is not the same as the one expected (.+)")
	public void testInitialValuesOfBeanNotReturningAnExpectedDefaultValue() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidAccessor3.class);
		blt.testDefaultValues(defaultValues);
	}

	@Test
	public void testMutatorsAndAccessorsOfValidBean() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues otherValues1 = returnCorrectValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testMutatorsAndAccessors(defaultValues, otherValues1);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The properties defined in parameter must at least contain all the settable properties of the object.\n.+\n.+")
	public void testMutatorsAndAccessorsOfValidBeanButWithMutablePropertiesMissing() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		defaultValues.remove("anInt");
		final PropertiesAndValues otherValues1 = returnCorrectValuesForValidBean();
		otherValues1.remove("anInt");
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testMutatorsAndAccessors(defaultValues, otherValues1);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The value of the property 'anInt' returned (.+) is not the same as the one expected (.+)")
	public void testMutatorsAndAccessorsWithAccessorNotReturningSetValue() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues otherValues = returnCorrectValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidAccessor4.class);
		blt.testMutatorsAndAccessors(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The equals method must return true when the object is compared to itself:\nObject:.+")
	public void testEqualsAndHashOfInvalidEquals1() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		otherValues.put("aString", "aValue");
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidEquals1.class);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The equals method should return true for the comparison between objects with the same properties:\nobject1:.+\nobject2:.+")
	public void testEqualsAndHashOfInvalidEquals3() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		otherValues.put("aString", "aValue");
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidEquals3.class);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The hashcodes must be equal:\nobject1:.+ hashcode:.+\nobject2:.+ hashcode:.+")
	public void testEqualsAndHashOfInvalidHash() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		otherValues.put("aString", "aValue");
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidHash2.class);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test
	public void testEqualsAndHashOfValidBean() {
		final PropertiesAndValues values1 = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues values2 = returnCorrectValuesForValidBean();
		final PropertiesAndValues values3 = returnOtherCorrectValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testEqualsAndHash(values1, values2);
		blt.testEqualsAndHash(values1, values3);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The value of the  property 'anInt' must be different in the parameters.")
	public void testEqualsAndHashOfValidBeanButWithSamePropertyValueInParameter() {
		final PropertiesAndValues values1 = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues values2 = returnCorrectValuesForValidBean();
		values2.put("anInt", values1.get("anInt"));
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testEqualsAndHash(values1, values2);
	}

	@Test
	public void testToStringForBean() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues otherValues = returnCorrectValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(ValidBean.class);
		blt.testToString(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The result of toString\\(\\) should depend on the property 'anInt'")
	public void testInvalidToString1() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues otherValues = returnCorrectValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidToString1.class);
		blt.testToString(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The result of toString\\(\\) should depend on the property 'aBoolean'")
	public void testInvalidToString2() {
		final PropertiesAndValues defaultValues = returnExpectedDefaultValuesForValidBean();
		final PropertiesAndValues otherValues = returnCorrectValuesForValidBean();
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidToString2.class);
		blt.testToString(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The equals method must return false for the comparison between objects with different properties:\nobject1:.+\nobject2:.+")
	public void testMissingPropertyInEquals() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		defaultValues.put("aListOfString", null);
		defaultValues.put("anInteger", null);
		otherValues.put("aString", "aValue");
		otherValues.put("aListOfString", new ArrayList<String>());
		otherValues.put("anInteger", Integer.valueOf(-1));

		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidEquals2.class, null);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The comparison with null must return false.\n.+")
	public void testEqualsReturnsTrueWhenComparedToNull() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		otherValues.put("aString", "aValue");
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidEquals4.class, null);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The comparison with another class must return false.\n.+")
	public void testEqualsReturnsTrueWhenComparedToAnotherClass() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		otherValues.put("aString", "aValue");
		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidEquals5.class, null);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The hashcodes of different objects should be different for the tests, please change the values or check that hashcode\\(\\) is correct\nobject1:.+ hashcode:.+\nobject2.+ hashcode:.+")
	public void testMissingPropertyInHash() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		defaultValues.put("aListOfString", null);
		defaultValues.put("anInteger", null);
		otherValues.put("aString", "aValue");
		otherValues.put("aListOfString", new ArrayList<String>());
		otherValues.put("anInteger", Integer.valueOf(-1));

		final BeanLikeTester blt = new BeanLikeTester(BeanWithInvalidHash1.class, null);
		blt.testEqualsAndHash(defaultValues, otherValues);
	}

	@Test
	public void testConstantObject() {
		final PropertiesAndValues defaultValues = returnCorrectDefaultValueForValidBeanLike();
		final PropertiesAndValues otherValues = returnCorrectOtherValueForValidBeanLike();
		final BeanLikeTester blt = new BeanLikeTester(ConstantObject.class);
		blt.testBeanLike(defaultValues, otherValues);
	}

	// -------------------------------------------------------------------------
	// Test beanLike objects
	// -------------------------------------------------------------------------

	private ConstructorSignatureAndPropertiesMapping returnCorrectSignatureAndPropertiesForValidBeanLike() {
		final ConstructorSignatureAndPropertiesMapping mapping = new ConstructorSignatureAndPropertiesMapping();
		final List<Class<?>> signature1 = Arrays.<Class<?>> asList(String.class, List.class);
		mapping.put(signature1, Arrays.asList("aString", "aListOfString"));

		final List<Class<?>> signature2 = Arrays.<Class<?>> asList(List.class, String.class);
		mapping.put(signature2, Arrays.asList("aListOfString", "aString"));

		final List<Class<?>> signature3 = Arrays.<Class<?>> asList(String.class, List.class, int.class, Integer.class);
		mapping.put(signature3, Arrays.asList("aString", "aListOfString", "anInt", "anInteger"));
		return mapping;
	}

	private PropertiesAndValues returnCorrectDefaultValueForValidBeanLike() {
		final PropertiesAndValues defaultValues = new PropertiesAndValues();
		defaultValues.put("aString", null);
		defaultValues.put("aListOfString", null);
		defaultValues.put("anInt", 0);
		defaultValues.put("anInteger", null);
		defaultValues.put("aBoolean", null);
		defaultValues.put("aBooleanPrimitive", false);
		defaultValues.put("aReadOnlyProperty", Integer.MAX_VALUE);
		defaultValues.put("anotherReadOnlyProperty", null);
		return defaultValues;
	}

	private PropertiesAndValues returnCorrectOtherValueForValidBeanLike() {
		final PropertiesAndValues otherValues = new PropertiesAndValues();
		otherValues.put("aString", "aNewString");
		otherValues.put("aListOfString", new ArrayList<String>());
		otherValues.put("anInt", 3);
		otherValues.put("anInteger", Integer.valueOf(5));
		otherValues.put("aBoolean", true);
		otherValues.put("aBooleanPrimitive", true);
		// 'aReadOnlyProperty' and 'anotherReadOnlyProperty' are read only property and hence not set here.
		return otherValues;
	}

	@Test
	public void testTestInitialValuesWithValidBeanLike() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();
		final PropertiesAndValues defaultValues = returnCorrectDefaultValueForValidBeanLike();
		final BeanLikeTester blt = new BeanLikeTester(ValidBeanLike.class, mapping);
		blt.testDefaultValues(defaultValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The signatures from the constructor's argument must be the same as the bean:\nFrom args:  .+\nFrom object:.+")
	public void testTestInitialValuesWithValidBeanLikeButNotAllConstructorsTakenIntoAccount() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();

		@SuppressWarnings("unchecked")
		final List<Class<?>> signature3 = Arrays.asList(String.class, List.class, int.class, Integer.class);
		mapping.remove(signature3);

		new BeanLikeTester(ValidBeanLike.class, mapping);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The signatures from the constructor's argument must be the same as the bean:\nFrom args:  .+\nFrom object:.+")
	public void testTestInitialValuesWithValidBeanLikeButUnknownConstructor() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();

		// The constructor with this signature doesn't exist!
		@SuppressWarnings("unchecked")
		final List<Class<?>> signature4 = Arrays.asList(List.class, int.class, Integer.class);
		mapping.put(signature4, Arrays.asList("aListOfString", "anInt", "anInteger"));

		new BeanLikeTester(ValidBeanLike.class, mapping);
	}

	@Test
	public void testTestMutatorsAndAccessorsOfBeanLike() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();
		final PropertiesAndValues defaultValues = returnCorrectDefaultValueForValidBeanLike();
		final PropertiesAndValues otherValues = returnCorrectOtherValueForValidBeanLike();
		final BeanLikeTester blt = new BeanLikeTester(ValidBeanLike.class, mapping);
		blt.testMutatorsAndAccessors(defaultValues, otherValues);
	}

	@Test
	public void testTestEqualsAndHashOfValidBeanLike() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();
		final PropertiesAndValues values = returnCorrectDefaultValueForValidBeanLike();
		final PropertiesAndValues otherValues = returnCorrectOtherValueForValidBeanLike();

		final BeanLikeTester blt = new BeanLikeTester(ValidBeanLike.class, mapping);
		blt.testEqualsAndHash(values, otherValues);
	}

	@Test(expectedExceptions = BeanLikeTesterException.class, expectedExceptionsMessageRegExp = "The following properties don't have any accessor:\\[aBoolean, aListOfString\\]")
	public void testBeanLikeWithMissingGetter() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();
		new BeanLikeTester(BeanLikeWithMissingGetter.class, mapping);
	}

	@Test
	public void testToStringForBeanLike() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();
		final PropertiesAndValues values = returnCorrectDefaultValueForValidBeanLike();
		final PropertiesAndValues otherValues = returnCorrectOtherValueForValidBeanLike();
		final BeanLikeTester blt = new BeanLikeTester(ValidBeanLike.class, mapping);
		blt.testToString(values, otherValues);
	}

	@Test
	public void testTestBeanLike() {
		final ConstructorSignatureAndPropertiesMapping mapping = returnCorrectSignatureAndPropertiesForValidBeanLike();
		final PropertiesAndValues values = returnCorrectDefaultValueForValidBeanLike();
		final PropertiesAndValues otherValues = returnCorrectOtherValueForValidBeanLike();
		final BeanLikeTester blt = new BeanLikeTester(ValidBeanLike.class, mapping);
		blt.testBeanLike(values, otherValues);
	}

	@Test
	public void testImmutableObject() {
		final ConstructorSignatureAndPropertiesMapping mapping = new ConstructorSignatureAndPropertiesMapping();
		final List<Class<?>> signature1 = Arrays.<Class<?>> asList(String.class, List.class);
		mapping.put(signature1, Arrays.asList("aString", "aListOfString"));

		final List<Class<?>> signature3 = Arrays.<Class<?>> asList(int.class);
		mapping.put(signature3, Arrays.asList("anInt"));

		final List<Class<?>> signature2 = Arrays.<Class<?>> asList(Integer.class);
		mapping.put(signature2, Arrays.asList("anInteger"));

		final PropertiesAndValues defaultValues = returnCorrectDefaultValueForValidBeanLike();
		final PropertiesAndValues otherValues = returnCorrectOtherValueForValidBeanLike();
		final BeanLikeTester blt = new BeanLikeTester(ImmutableObject.class, mapping);
		blt.testBeanLike(defaultValues, otherValues);
	}

	// ----------- Other tests ---------------------------------------------------------------------

	@Test(expectedExceptions = InvocationTargetException.class)
	public void testCreateNewInstanceThrowingAnException() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Method method = BeanLikeTester.class.getDeclaredMethod("createNewInstance", String.class, Class[].class, Object[].class);
		method.setAccessible(true);
		method.invoke(null, "java.lang.String2", new Class[] { String.class }, new Object[] { "aString" });
	}

	@Test(expectedExceptions = InvocationTargetException.class)
	public void testinvokeMethodThrowingAnException() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Method method = BeanLikeTester.class.getDeclaredMethod("invokeMethod", Object.class, Method.class, Object[].class);
		method.setAccessible(true);
		method.invoke(null, "", null, new Object[0]);
	}

}
