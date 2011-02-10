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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class provides methods to easily and quickly test and improve the test coverage of 'bean like'
 * objects (i.e. objects with setters and getters but that not necessarily implement the Serializable 
 * interface and can have non default constructors).
 * The beanLike objects:
 *    <ul>
 *    <li>have a default constructor and/or constructors that only initialise properties,</li>
 *    <li>have optional setters (setXXX()),</li>
 *    <li>have accessors (isXXX(), getXXX()) for all the properties,</li>
 *    <li>properties are either readonly or set by a either a constructor or a setter (or both).</li>
 *    </ul>
 * <p>
 * All the initial values, setters, getters, non default constructors, hashCode(), equals(), toString()
 * can be automatic tested from a map of default and non default property/value.
 */
public final class BeanLikeTester {
	// ------------------------------------  Class variables  ----------------------------------------

	/** Convenient class to define a list of property names with their corresponding value. */
	public static final class PropertiesAndValues extends HashMap<String, Object> {
		private static final long serialVersionUID = 1L;

		public PropertiesAndValues(PropertiesAndValues defaultValues) {
			super(defaultValues);
		}

		public PropertiesAndValues() {
			super();
		}
	}

	/** 
	 * Convenient class to define a list of constructor signatures with their corresponding property name.
	 * @param List<Class<?> List of the constructor's parameter types.
	 * @param List<String> List of corresponding property names.
	 * */
	public static final class ConstructorSignatureAndPropertiesMapping extends HashMap<List<Class<?>>, List<String>> {
		private static final long serialVersionUID = 1L;
	}

	private static final ConstructorSignatureAndPropertiesMapping NOARG_SIGNATUREANDPROPS = new ConstructorSignatureAndPropertiesMapping();
	static {
		NOARG_SIGNATUREANDPROPS.put(Collections.<Class<?>> emptyList(), Collections.<String> emptyList());
	}

	private static final class AClassToBeTestedAgainst {
		private static AClassToBeTestedAgainst instance = new AClassToBeTestedAgainst();

		private AClassToBeTestedAgainst() {
		}
	}

	// -----------------------------------  Instance variables  ------------------------------------
	private final Class<?>                                 beanLikeClass;
	private final ConstructorSignatureAndPropertiesMapping constructorsSignaturesAndProperties;
	private final Set<String>                              gettablePropertyNames;
	private final Set<String>                              settablePropertyNames;
	private final Set<String>                              mutablePropertyNames;
	private final Map<String, Method>                      accessors;
	private final Map<String, Method>                      setters;

	// ------------------------------------  Constructors  -------------------------------------------
	/**
	 * Create a BeanLikeTester with a specific beanLike to test.
	 * @param beanLikeClass The 'beanLike' to test.
	 * @param constructorsSignaturesAndProperties The signature of all the possible constructors
	 *        (The parameters of the constructors must only set properties).<br/>
	 *        key: constructor's signature. value: corresponding property name.<br/>
	 *        For beans the map can be empty or null.
	 * @throws BeanLikeTesterException if at least one of the signatures doesn't correspond to a constructor or
	 *         the constructors doesn't only define properties or setters or accessors are invalid.
	 */
	public BeanLikeTester(Class<?> beanLikeClass, ConstructorSignatureAndPropertiesMapping constructorsSignaturesAndProperties) {
		this.beanLikeClass = beanLikeClass;
		this.constructorsSignaturesAndProperties = constructorsSignaturesAndProperties == null ? NOARG_SIGNATUREANDPROPS : constructorsSignaturesAndProperties;
		accessors = getAccessors();
		setters = getSetters();
		gettablePropertyNames = accessors.keySet();
		settablePropertyNames = setters.keySet();
		mutablePropertyNames = getMutableProperyNames();
		verifyConstructorSignaturesMatchSignaturesFromArgs();
		verifySettersAndAccessorsAreValid();
		verifyAllPropertiesHaveAnAccessor();
	}

	/**
	 * Create a BeanLikeTester with a specific bean to test.
	 * @param beanClass The bean class to test.
	 * @throws BeanLikeTesterException See {@link #BeanLikeTester(Class, ConstructorSignatureAndPropertiesMapping) }
	 */
	public BeanLikeTester(Class<?> beanClass) {
		this(beanClass, null);
	}

	// ------------------------------------  Private methods  ----------------------------------------

	/**
	 * Get the properties that can be set by either a constructor or a setter.
	 * @return Set of mutable properties.
	 */
	private Set<String> getMutableProperyNames() {
		final Set<String> settableProperties = new HashSet<String>();
		for (final List<String> props : constructorsSignaturesAndProperties.values()) {
			settableProperties.addAll(props);
		}
		settableProperties.addAll(settablePropertyNames);
		return settableProperties;
	}

	/**
	 * Verify that the beanLike constructors' signatures to test are the same as the ones defined by the beanLike class.
	  * @throws BeanLikeTesterException if at least one of the signatures doesn't correspond to a constructor.
	 */
	private void verifyConstructorSignaturesMatchSignaturesFromArgs() {
		final Set<List<Class<?>>> signaturesFromArgs = constructorsSignaturesAndProperties.keySet();
		final Set<List<Class<?>>> signaturesFromConstructor = new HashSet<List<Class<?>>>();

		for (final Constructor<?> constructor : beanLikeClass.getConstructors()) {
			final List<Class<?>> signature = Arrays.asList(constructor.getParameterTypes());
			signaturesFromConstructor.add(signature);

		}
		if (!signaturesFromArgs.equals(signaturesFromConstructor)) {
			throw new BeanLikeTesterException("The signatures from the constructor's argument must be the same as the bean:\nFrom args:  " + signaturesFromArgs
			                                  + "\nFrom object:" + signaturesFromConstructor);
		}
	}

	/**
	 * Test that all the setters return void, the 'is' accessors return a boolean and the other getters an object.
	 * @throws BeanLikeTesterException if one of the previous condition is not met.
	 */
	private void verifySettersAndAccessorsAreValid() {
		final Method[] methods = beanLikeClass.getMethods();

		for (final Method method : methods) {
			final String methodName = method.getName();
			if (isSetter(method)) {
					throw new BeanLikeTesterException("The method '" + methodName + "' must not return an object.");
			}
			if (isIsGetter(method)) {
					throw new BeanLikeTesterException("The method '" + methodName + "' doesn't return a boolean");
			}
			else if (isGetGetter(method)) {
					throw new BeanLikeTesterException("The method '" + methodName + "' doesn't return an object");
			}
		}
	}

	private boolean isSetter(Method method) {
	   return method.getName().startsWith("set") && !method.getReturnType().equals(Void.TYPE);
   }

	private boolean isIsGetter(Method method) {
	   return method.getName().startsWith("is") && !method.getReturnType().equals(Boolean.class) && !method.getReturnType().equals(boolean.class);
   }
	
	private boolean isGetGetter(Method method) {
	   return method.getName().startsWith("get") && !method.getName().equals("getClass") && (method.getParameterTypes().length == 0) && method.getReturnType().equals(Void.TYPE);
   }
	
	/**
	 * Verify that all properties have an accessor
	  * @throws BeanLikeTesterException if the verification fails.
	 */
	private void verifyAllPropertiesHaveAnAccessor() {
		final Set<String> nonAccessibleProperties = new HashSet<String>(mutablePropertyNames);
		nonAccessibleProperties.removeAll(gettablePropertyNames);
		if (!nonAccessibleProperties.isEmpty()) {
			throw new BeanLikeTesterException("The following properties don't have any accessor:" + nonAccessibleProperties);
		}
	}

	/**
	 * Create a new instance of a class using a constructor with parameters.
	 * @param fullyQualifiedClassName Fully qualified class name of the instance to create.
	 * @param constructorSignature Signature of the constructor to use.
	 * @param constructorParams Constructor's parameters.
	 * @return New instance.
	 * @throws BeanLikeTesterException if the instance couldn't be created.
	 */
	private static Object createNewInstance(String fullyQualifiedClassName, Class<?>[] constructorSignature, Object... constructorParams) {
		try {
			final Class<?> classToInstantiate = Class.forName(fullyQualifiedClassName);
			final Constructor<? extends Object> classConstructor = classToInstantiate.getConstructor(constructorSignature);
			return classConstructor.newInstance(constructorParams);
		} catch (final Exception e) {
			final String msg = MessageFormat.format("exception msg: {0} \n\tfullyQualifiedClassName: {1} \n\tconstructorSignature: {2} \n\tconstructorParams: {3}",
			                                        e,
			                                        fullyQualifiedClassName,
			                                        Arrays.asList(constructorSignature),
			                                        Arrays.asList(constructorParams));
			throw new BeanLikeTesterException(msg, e);
		}
	}

	/**
	 * Invoke an object's method.
	 * @param object Object.
	 * @param method Method to invoke.
	 * @param args Method's arguments.
	 * @return Object returned by the method invocation.
	 * @throws BeanLikeTesterException if an exception occurred.
	 */
	private static Object invokeMethod(Object object, Method method, Object... args) {
		try {
			return method.invoke(object, args);
		} catch (final Exception e) {
			throw new BeanLikeTesterException(e.getMessage(), e);
		}
	}

	private static String getPropertyNameFromMethodName(String methodName) {
		final String propertyName = methodName.replaceFirst("^is|set|get", "");
		return Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
	}

	private static boolean areValuesDifferent(Object value1, Object value2) {
		final boolean conditionToFail1 = (value1 != null) && ((value2 == null) || !value1.equals(value2));
		final boolean conditionToFail2 = (value2 != null) && ((value1 == null) || !value2.equals(value1));
		return conditionToFail1 || conditionToFail2;
	}

	/**
	 * Set a property via its setter.
	 * @param beanLike beanLike to set the property to.
	 * @param propertyName Property name.
	 * @param value Property's value.
	 * @throws BeanLikeTesterException if the property couldn't be set.
	 */
	private void setProperty(Object beanLike, String propertyName, Object value) {
		final Method setter = setters.get(propertyName);
		invokeMethod(beanLike, setter, new Object[] { value });
	}

	/**
	 * Get all the accessors.
	 * @return List of accessors.
	 */
	private Map<String, Method> getAccessors() {
		final Map<String, Method> propsAndAccessors = new HashMap<String, Method>();
		final Method[] methods = beanLikeClass.getMethods();

		for (final Method method : methods) {
			final String methodName = method.getName();
			if (methodName.startsWith("is")) {
				propsAndAccessors.put(getPropertyNameFromMethodName(methodName), method);
			}
			else if (methodName.startsWith("get") && !methodName.equals("getClass") && (method.getParameterTypes().length == 0)) {
				propsAndAccessors.put(getPropertyNameFromMethodName(methodName), method);
			}
		}
		return propsAndAccessors;
	}

	/**
	 * Get a new beanLike instance using a specific constructor and properties values.
	 * @param constructor Constructor used to create the new instance.
	 * @param propertiesAndValues List of property names and their corresponding values.<br/>
	 *                            The properties unused by the constructor are ignored.
	 * @return New beanLike instance.
	 */
	private Object getNewInstance(Constructor<?> constructor, PropertiesAndValues propertiesAndValues) {
		final Class<?>[] constructorSignature = constructor.getParameterTypes();

		final List<String> propertyNames = constructorsSignaturesAndProperties.get(Arrays.asList(constructorSignature));
		final List<Object> propertyValues = new ArrayList<Object>();

		if (propertyNames != null) {
			for (final String propertyName : propertyNames) {
				propertyValues.add(propertiesAndValues.get(propertyName));
			}
		}
		return createNewInstance(beanLikeClass.getName(), constructorSignature, propertyValues.toArray());
	}

	/**
	 * Return all the setters.
	 * @return List of setters
	 */
	private Map<String, Method> getSetters() {
		final Map<String, Method> propsAndSetters = new HashMap<String, Method>();
		final Method[] methods = beanLikeClass.getMethods();

		for (final Method method : methods) {
			final String methodName = method.getName();
			if (methodName.startsWith("set")) {
				propsAndSetters.put(getPropertyNameFromMethodName(methodName), method);
			}
		}
		return propsAndSetters;
	}

	/**
	 * Verify that the beanLike object returns the expected value for the properties 'propertiesToVerify'.
	 * @param beanLike Object to test.
	 * @param propertiesToVerify List of properties to verify.
	 * @param propertiesAndValuesExpected Property names and their corresponding value expected.
	 * @throws BeanLikeTesterException if the object doesn't return an expected value.
	 */
	private void verifyPropertyValuesFromAccessors(Object beanLike, List<String> propertiesToVerify, final PropertiesAndValues propertiesAndValuesExpected) {
		for (final String propertyToVerify : propertiesToVerify) {
			verifyPropertyValueFromAccessor(beanLike, propertyToVerify, propertiesAndValuesExpected.get(propertyToVerify));
		}
	}

	/**
	 * Verify that the beanLike object returns the expected value for the property 'propertyName'.
	 * @param beanLike Instance to test.
	 * @param propertyName Property to verify.
	 * @param expectedValue Expected value.
	 * @throws BeanLikeTesterException if the object doesn't return the expected value.
	 */
	private void verifyPropertyValueFromAccessor(Object beanLike, String propertyName, Object expectedValue) {
		final Object returnedValue = getProperty(beanLike, propertyName);
		if (areValuesDifferent(expectedValue, returnedValue)) {
			throw new BeanLikeTesterException("The value of the property '" + propertyName + "' returned (" + returnedValue
			                                  + ") is not the same as the one expected (" + expectedValue + ")");
		}
	}

	private Object getProperty(Object beanLike, String propertyName) {
		final Method accessor = accessors.get(propertyName);
		final Object returnedValue = invokeMethod(beanLike, accessor, (Object[]) null);
		return returnedValue;
	}

	/**
	 * Verify that the property names to test are the same as the beanLike properties.
	 * @param propertyNamesToTest Property names to test.
	 * @throws BeanLikeTesterException if the set of properties to test is different from the properties accessible from the object.
	 */
	private void verifyPropertyNamesAreTheSameAs(Set<String> propertyNamesToTest) {
		if (!gettablePropertyNames.equals(propertyNamesToTest)) {
			throw new BeanLikeTesterException("The set of properties to test is different from the properties accessible from the object:\nFrom object: "
			                                  + gettablePropertyNames + "\nTo test:     " + propertyNamesToTest);
		}
	}

	private void verifyContainsAtLeastAllMutableProperties(Set<String> properties) {
		if (!properties.containsAll(mutablePropertyNames)) {
			throw new BeanLikeTesterException("The properties defined in parameter must at least contain all the settable properties of the object.\nParameter:"
			                                  + properties + "\nObject:" + mutablePropertyNames);
		}
	}

	private Object createObjectWithSecificPropertySet(PropertiesAndValues propsWithDefaultValue, String property, Object value) {
		final PropertiesAndValues propsWithValue = new PropertiesAndValues(propsWithDefaultValue);
		propsWithValue.put(property, value);

		// If possible create an object with default values and then set the property.
		if (settablePropertyNames.contains(property)) {
			final Object object = createObjectWithDefaultValues(propsWithDefaultValue);
			setProperty(object, property, value);
			return object;
		}

		// If no setters existed then find the first combination constructor that could set the property.
		for (final Entry<List<Class<?>>, List<String>> entry : constructorsSignaturesAndProperties.entrySet()) {
			final List<String> constructorPropertyNames = entry.getValue();
			if (constructorPropertyNames.contains(property)) {
				final Class<?>[] constructorSignature = entry.getKey().toArray(new Class[0]);
				final List<Object> constructorValues = new ArrayList<Object>();
				for (final String propertyName : constructorPropertyNames) {
					constructorValues.add(propsWithValue.get(propertyName));
				}
				return createNewInstance(beanLikeClass.getName(), constructorSignature, constructorValues.toArray());
			}
		}
		throw new RuntimeException("The property '" + property + "' must be settable by either a setter or a constructor!");
	}

	private Object createObjectWithDefaultValues(PropertiesAndValues propsWithDefaultValue) {
		// Create the object with default values (any constructor will do).
		final Constructor<?> constructor = beanLikeClass.getConstructors()[0];
		return getNewInstance(constructor, propsWithDefaultValue);
	}

	private void verifyAllValuesFromMutablePropsAreDifferent(PropertiesAndValues propsWithValue, PropertiesAndValues propsWithOtherValue) {
		for (final Entry<String, Object> entry : propsWithValue.entrySet()) {
			final String property = entry.getKey();
			final Object value = entry.getValue();
			if (mutablePropertyNames.contains(property) && !areValuesDifferent(value, propsWithOtherValue.get(property))) {
				throw new BeanLikeTesterException("The value of the  property '" + property + "' must be different in the parameters.");
			}
		}
	}

	// ------------------------------------  Public methods  -----------------------------------------

	/**
	 * Test that the default value of the properties (returned by the accessors) 
	 * are the same as the one defined by the parameter.<br/>
	 * The properties are also tested for objects created with all the possible constructors.
	 * 
	 * @param expectedDefaultValues Property names and their expected default value. 
	 * @throws BeanLikeTesterException if the test fails.<br/>
	 *                        i.e. if one of the values returned by one of the accessors is different from the expected default value 
	 *                        or the properties defined by 'expectedDefaultValues' don't correspond to the beanLike properties.
	 */
	public void testDefaultValues(PropertiesAndValues expectedDefaultValues) {

		verifyPropertyNamesAreTheSameAs(expectedDefaultValues.keySet());
		// Test the initial value for all the possible ways to create the object. 
		for (final Constructor<?> constructor : beanLikeClass.getConstructors()) {
			final Object beanLike = getNewInstance(constructor, expectedDefaultValues);

			for (final Entry<String, Method> entry : accessors.entrySet()) {
				final String propertyName = entry.getKey();
				final Object expected = expectedDefaultValues.get(propertyName);
				final Object returned = getProperty(beanLike, propertyName);
				if (areValuesDifferent(returned, expected)) {
					throw new BeanLikeTesterException("The value of the property '" + propertyName + "' returned (" + returned
					                                  + ") is not the same as the one expected (" + expected + ")");
				}
			}
		}
	}

	/**
	 * Test that all the mutators (setters, and constructors with arguments) can change their property
	 * and that the accessors reflect the change.
	 * 
	 * @param propsWithValue Property names (keys) and their value.<br/>
	 *                       It must at least contain all the settable properties.
	 * @param otherPropsWithValue Property names (keys) and their value different from 'propsWithValue'.</br>
	 *                            It must at least contain all the settable properties.
	 * @throws BeanLikeTesterException if the test fails.
	 */
	public void testMutatorsAndAccessors(PropertiesAndValues propsWithValue, PropertiesAndValues otherPropsWithValue) {
		verifyContainsAtLeastAllMutableProperties(propsWithValue.keySet());
		verifyContainsAtLeastAllMutableProperties(otherPropsWithValue.keySet());

		// --- Test the modification from all the constructors.
		for (final Constructor<?> constructor : beanLikeClass.getConstructors()) {
			final List<Class<?>> constructorSignature = Arrays.asList(constructor.getParameterTypes());

			final Object beanLike = getNewInstance(constructor, otherPropsWithValue);
			// If it's not a constructor by default, test that the properties defined by the constructor's arguments are effectively set.
			if (!constructorSignature.isEmpty()) {
				final List<String> propertyNamesInConstructor = constructorsSignaturesAndProperties.get(constructorSignature);
				verifyPropertyValuesFromAccessors(beanLike, propertyNamesInConstructor, otherPropsWithValue);
			}

			// --- Test the modifications from the setters.
			// As a non default value may have already been set by the constructor, we first set the default value, check it and
			// then set another value and check it again.
			final List<PropertiesAndValues> rounds = new ArrayList<PropertiesAndValues>();
			rounds.add(propsWithValue);
			rounds.add(otherPropsWithValue);
			// For default values and then other values:
			for (final PropertiesAndValues propertiesAndValues : rounds) {
				// For all the setters:
				for (final Entry<String, Method> entry : setters.entrySet()) {
					final String propertyName = entry.getKey();
					final Object valueToSet = propertiesAndValues.get(propertyName);
					setProperty(beanLike, propertyName, valueToSet);
					verifyPropertyValueFromAccessor(beanLike, propertyName, valueToSet);
				}
			}
		}
	}

	/**
	 * Test that equals() and hashCode() take into account all the properties and return the correct values.<br/>
	 * 
	 * @param propsWithDefaultValue Property names (keys) and their value.<br/>
	 *                              It must at least contain all the settable properties.
	 * @param propsWithOtherValue Property names (keys) and their value different from 'propsWithValue'.</br>
	 *                            It must at least contain all the settable properties.
	 * @throws BeanLikeTesterException if the test fails.
	 */
	public void testEqualsAndHash(PropertiesAndValues propsWithDefaultValue, PropertiesAndValues propsWithOtherValue) {
		verifyContainsAtLeastAllMutableProperties(propsWithDefaultValue.keySet());
		verifyContainsAtLeastAllMutableProperties(propsWithOtherValue.keySet());
		verifyAllValuesFromMutablePropsAreDifferent(propsWithDefaultValue, propsWithOtherValue);

		final Object defaultObj = createObjectWithDefaultValues(propsWithDefaultValue);
		final int defaultHashCode = defaultObj.hashCode();

		// Verify that the bean is equal to itself.
		if (!defaultObj.equals(defaultObj)) {
			throw new BeanLikeTesterException("The equals method must return true when the object is compared to itself:\nObject:" + defaultObj);
		}

		if (defaultObj.equals(null)) {
			throw new BeanLikeTesterException("The comparison with null must return false.\nObject:" + defaultObj);
		}

		if (defaultObj.equals(AClassToBeTestedAgainst.instance)) {
			throw new BeanLikeTesterException("The comparison with another class must return false.\nObject:" + defaultObj);
		}

		for (final Entry<String, Object> entry : propsWithOtherValue.entrySet()) {
			final String property = entry.getKey();
			final Object value = entry.getValue();
			// Test only the mutable properties.
			if (mutablePropertyNames.contains(property)) {
				final Object otherObject1 = createObjectWithSecificPropertySet(propsWithDefaultValue, property, value);
				final int otherHashCode1 = otherObject1.hashCode();

				if (!areValuesDifferent(defaultHashCode, otherHashCode1)) {
					throw new BeanLikeTesterException("The hashcodes of different objects should be different for the tests, please change the values or check that hashcode() is correct\nobject1:"
					                                  + defaultObj + " hashcode:" + defaultHashCode + "\nobject2:" + otherObject1 + " hashcode:" + otherHashCode1);
				}

				// Verify that the bean is not equal to the default one as one property has been changed.
				final boolean equals1 = otherObject1.equals(defaultObj);
				final boolean equals2 = defaultObj.equals(otherObject1);
				if (equals1 || equals2) {
					throw new BeanLikeTesterException("The equals method must return false for the comparison between objects with different properties:\nobject1:"
					                                  + otherObject1 + "\nobject2:" + defaultObj);
				}

				// Create another object exactly the same properties to test equals() and hashCode()
				final Object otherObject2 = createObjectWithSecificPropertySet(propsWithDefaultValue, property, value);
				final int otherHashCode2 = otherObject2.hashCode();
				// Two beans with the same properties must be equal.
				if (!otherObject1.equals(otherObject2)) {
					throw new BeanLikeTesterException("The equals method should return true for the comparison between objects with the same properties:\nobject1:"
					                                  + otherObject1 + "\nobject2:" + otherObject2);
				}

				// Two beans with the same properties must have the same hashcode.
				if (!(otherHashCode1 == otherHashCode2)) {
					throw new BeanLikeTesterException("The hashcodes must be equal:\nobject1:" + otherObject1 + " hashcode:" + otherHashCode1 + "\nobject2:"
					                                  + otherObject2 + " hashcode:" + otherHashCode2);
				}

			}
		}
	}

	/**
	 * Test that the method toString() returns a different String if one of the settable properties has changed.
	 * 
	 * @param propsWithDefaultValue Property names (keys) and their value.<br/>
	 *                              It must at least contain all the settable properties.
	 * @param propsWithOtherValue Property names (keys) and their value different from 'propsWithDefaultValue'.</br>
	 *                            It must at least contain all the settable properties.
	 * @throws BeanLikeTesterException if the test fails.
	 */
	public void testToString(PropertiesAndValues propsWithDefaultValue, PropertiesAndValues propsWithOtherValue) {
		verifyContainsAtLeastAllMutableProperties(propsWithDefaultValue.keySet());
		verifyContainsAtLeastAllMutableProperties(propsWithOtherValue.keySet());
		verifyAllValuesFromMutablePropsAreDifferent(propsWithDefaultValue, propsWithOtherValue);

		final Object defaultObj = createObjectWithDefaultValues(propsWithDefaultValue);
		final String toStringFromDefaultValues = defaultObj.toString();

		for (final Entry<String, Object> entry : propsWithOtherValue.entrySet()) {
			final String property = entry.getKey();
			final Object value = entry.getValue();
			// Test only the mutable properties.
			if (mutablePropertyNames.contains(property)) {
				final Object object = createObjectWithSecificPropertySet(propsWithDefaultValue, property, value);
				final String toStringFromOtherValues = object.toString();
				if (!areValuesDifferent(toStringFromDefaultValues, toStringFromOtherValues)) {
					throw new BeanLikeTesterException("The result of toString() should depend on the property '" + property + "'");
				}
			}
		}
	}

	/**
	 * Run all the tests:
	 * <ul>
	 * <li>{@link #testDefaultValues(PropertiesAndValues)}</li>
	 * <li>{@link #testMutatorsAndAccessors(PropertiesAndValues, PropertiesAndValues)}</li>
	 * <li>{@link #testEqualsAndHash(PropertiesAndValues, PropertiesAndValues)}</li>
	 * <li>{@link #testToString(PropertiesAndValues, PropertiesAndValues)}</li>
	 * </ul>
	 * 
	 * @param propsWithDefaultValue Property names (keys) and their value.<br/>
	 *                              It must at least contain all the settable properties.
	 * @param propsWithOtherValue Property names (keys) and their value different from 'propsWithValue'.</br>
	 *                            It must at least contain all the settable properties.
	 * @throws BeanLikeTesterException if the test fails.
	 */
	public void testBeanLike(PropertiesAndValues propsWithDefaultValue, PropertiesAndValues propsWithOtherValue) {
		testDefaultValues(propsWithDefaultValue);
		testMutatorsAndAccessors(propsWithDefaultValue, propsWithOtherValue);
		testEqualsAndHash(propsWithDefaultValue, propsWithOtherValue);
		testToString(propsWithDefaultValue, propsWithOtherValue);
	}

}