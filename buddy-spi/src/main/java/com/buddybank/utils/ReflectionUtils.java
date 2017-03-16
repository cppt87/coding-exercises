package com.buddybank.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.buddybank.SystemException;

/**
 * The class <code>ClassReflection</code> provides utility methods for the
 * handling of Java reflection, It can be seen as an extension to the standard
 * features of <code>java.lang.reflect</code>.
 */
public final class ReflectionUtils {

	private final static Map<String, Class<?>> primitives = new HashMap<String, Class<?>>();
	
	private static Class<?> clazz;

	static {
		primitives.put("int", int.class);
		primitives.put("boolean", boolean.class);
		primitives.put("long", long.class);
		primitives.put("short", short.class);
		primitives.put("byte", byte.class);
		primitives.put("char", char.class);
		primitives.put("double", double.class);
		primitives.put("float", float.class);
	}

	private final static Map<String, String> primitivesToWrapper = new HashMap<String, String>();

	static {
		primitivesToWrapper.put("int", "java.lang.Integer");
		primitivesToWrapper.put("boolean", "java.lang.Boolean");
		primitivesToWrapper.put("long", "java.lang.Long");
		primitivesToWrapper.put("short", "java.lang.Short");
		primitivesToWrapper.put("byte", "java.lang.Byte");
		primitivesToWrapper.put("char", "java.lang.Character");
		primitivesToWrapper.put("double", "java.lang.Double");
		primitivesToWrapper.put("float", "java.lang.Float");
	}

	private final static Map<Class<?>, Class<?>> wrappersToPrimitives = new HashMap<Class<?>, Class<?>>();

	static {
		wrappersToPrimitives.put(Integer.class, int.class);
		wrappersToPrimitives.put(Boolean.class, boolean.class);
		wrappersToPrimitives.put(Long.class, long.class);
		wrappersToPrimitives.put(Short.class, short.class);
		wrappersToPrimitives.put(Byte.class, byte.class);
		wrappersToPrimitives.put(Character.class, char.class);
		wrappersToPrimitives.put(Double.class, double.class);
		wrappersToPrimitives.put(Float.class, float.class);
	}


	private final static class CacheEntry {

		Map<String, Method> methods = new ConcurrentHashMap<String, Method>();

		Map<String, Field> fields = new ConcurrentHashMap<String, Field>();

		void put(Method method) {
			this.methods.put(method.getName(), method);
		}

		Method getMethod(String name) {
			return (Method) this.methods.get(name);
		}

		void put(Field field) {
			this.fields.put(field.getName(), field);
		}

		Field getField(String name) {
			return (Field) this.fields.get(name);
		}
	}

	public final static Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

	public final static Class<?>[] ONE_OBJECT_CLASS_ARRAY = new Class[] { Object.class };

	public final static Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/**
	 * The cached classes.
	 */
	private static Map<Class<?>, CacheEntry> classes = new ConcurrentHashMap<Class<?>, CacheEntry>();

	public static Object newInstance(String className) {
		return newInstance(getClass(className));
	}

	public static Object newInstance(String className, Object param) {
		return newInstance(getClass(className), param);
	}

	public static Object newInstance(String className, Object param,
			Object param1) {
		return newInstance(getClass(className), param, param1);
	}

	/**
	 * Returns all declared fields of the given class and its superclass(es).
	 *
	 * @param aClass
	 *            java.lang.Class
	 * @return an arry of <code>java.lang.reflect.Field</code>
	 */
	public static Field[] getAllFields(Class<?> objectClass) {
		Class<?> currentClass = objectClass;
		List<Field> result = new ArrayList<Field>(20);
		while (currentClass != null && currentClass != Object.class) {
			Field[] fields = currentClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				int mod = fields[i].getModifiers();
				if (!(Modifier.isStatic(mod))) {
					fields[i].setAccessible(true);
					result.add(fields[i]);
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return (Field[]) result.toArray(new Field[result.size()]);
	}

	/**
	 * Returns all declared public methods of the given class and its superclass(es).
	 *
	 * @param aClass java.lang.Class
	 * @return an array of <code>java.lang.reflect.Method</code>
	 */
	public static Method[] getAllPublicMethods(Class<?> objectClass) {
		Class<?> currentClass = objectClass;
		List<Method> result = new ArrayList<Method>(20);
		while (currentClass != null && currentClass != Object.class) {
			Method[] methods = currentClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				int mod = methods[i].getModifiers();
				if (!Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
					methods[i].setAccessible(true);
					result.add(methods[i]);
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return (Method[]) result.toArray(new Method[result.size()]);
	}
	
	/**
	 * Returns all declared methods of the given class and its superclass(es).
	 *
	 * @param aClass java.lang.Class
	 * @return an array of <code>java.lang.reflect.Method</code>
	 */
	public static Method[] getAllMethods(Class<?> objectClass) {
		Class<?> currentClass = objectClass;
		List<Method> result = new ArrayList<Method>(20);
		while (currentClass != null && currentClass != Object.class) {
			Method[] methods = currentClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				int mod = methods[i].getModifiers();
				if (!Modifier.isStatic(mod)) {
					methods[i].setAccessible(true);
					result.add(methods[i]);
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return (Method[]) result.toArray(new Method[result.size()]);
	}

	public static Object newInstance(Class<?> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException ex) {
			throw new SystemException(ex,"Error at instantiation of class "
					+ cls.getName());
		} catch (IllegalAccessException ex) {
			throw new SystemException(ex,"Error accessing class "
					+ cls.getName());
		} catch (NoClassDefFoundError ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " was not found");
		}
	}

	public static Object newInstance(Class<?> cls, Object param) {
		try {
			Constructor<?> constructor = cls
					.getConstructor(new Class[] { Object.class });
			return constructor.newInstance(new Object[] { param });
		} catch (InstantiationException ex) {
			throw new SystemException(ex,"Error at instantiation of class "
					+ cls.getName());
		} catch (IllegalAccessException ex) {
			throw new SystemException(ex,"Error accessing class "
					+ cls.getName());
		} catch (NoClassDefFoundError ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " was not found");
		} catch (NoSuchMethodException ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " has no constructor for 1 object.");
		} catch (InvocationTargetException ex) {
			throw new SystemException(ex,
					"InvocationTargetException for class " + cls.getName()
							+ ".");
		}

	}
	
	/*
	 * To use if the constructor of Class cls has one parameter of type 'clazz'
	 */
	public static Object newInstanceClass(Class<?> cls, Object param, Class<?> clazz) {
		try {
			Constructor<?> constructor = cls
					.getConstructor(new Class[] { clazz });
			return constructor.newInstance(new Object[] { param });
		} catch (InstantiationException ex) {
			throw new SystemException(ex,"Error at instantiation of class "
					+ cls.getName());
		} catch (IllegalAccessException ex) {
			throw new SystemException(ex,"Error accessing class "
					+ cls.getName());
		} catch (NoClassDefFoundError ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " was not found");
		} catch (NoSuchMethodException ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " has no constructor for 1 object.");
		} catch (InvocationTargetException ex) {
			throw new SystemException(ex,
					"InvocationTargetException for class " + cls.getName()
							+ ".");
		}

	}


	public static Object newInstance(Class<?> cls, Object param, Object param1) {
		try {
			Constructor<?> constructor = cls.getConstructor(new Class[] {
					Object.class, Object.class });
			return constructor.newInstance(new Object[] { param, param1 });
		} catch (InstantiationException ex) {
			throw new SystemException(ex,"Error at instantiation of class "
					+ cls.getName());
		} catch (IllegalAccessException ex) {
			throw new SystemException(ex,"Error accessing class "
					+ cls.getName());
		} catch (NoClassDefFoundError ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " was not found");
		} catch (NoSuchMethodException ex) {
			throw new SystemException(ex,"The class " + cls.getName()
					+ " has no constructor for 1 object.");
		} catch (InvocationTargetException ex) {
			throw new SystemException(ex,
					"InvocationTargetException for class " + cls.getName()
							+ ".");
		}

	}

	public static Object invokeMethod(Object target, String name,
			Class<?>[] types, Object[] args) {
		Method method = getMethod(target.getClass(), name, types);
		return invokeMethod(target, method, args);
	}

	public static Object invokeMethod(Object target, Method method,
			Object[] args) {
		try {
			return method.invoke(target, args);
		} catch (InvocationTargetException ex) {
			Throwable nested = ex.getTargetException();
			if (nested instanceof SystemException)
				throw (RuntimeException) nested;
			throw new SystemException(
					"Unknown error while invoking method " + method.getName()
							+ " of class "
							+ method.getDeclaringClass().getName());
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot invoke method "
					+ method.getName() + " of class "
					+ method.getDeclaringClass().getName());
		}
	}

	public static Object readField(Object target, String name) {
		Field field = getField(target.getClass(), name);
		return readField(target, field);
	}

	public static Object readField(Object target, Field field) {
		try {
			return field.get(target);
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot read field "
					+ field.getName() + " of class "
					+ field.getDeclaringClass().getName());
		}
	}

	public static void writeField(Object target, String name, Object value) {
		Field field = getField(target.getClass(), name);
		writeField(target, field, value);
	}

	public static void writeField(Object target, Field field, Object value) {
		try {
			field.set(target, value);
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot write to field '"
					+ field.getName() + "' of class '"
					+ field.getDeclaringClass().getName()
					+ "'. Argument type was '"
					+ (value == null ? null : value.getClass())
					+ "' expected type is '" + field.getType() + "'.");
		}
	}

	public static Object invokeGetter(Object target, String name) {
		Method getter = getMethod(target.getClass(), name,
				ReflectionUtils.EMPTY_CLASS_ARRAY);
		return invokeGetter(target, getter);
	}

	public static Object invokeGetter(Object target, Method method) {
		try {
			return method.invoke(target, EMPTY_OBJECT_ARRAY);
		} catch (InvocationTargetException ex) {
			throw new SystemException(
					"Unknown error while invoking method " + method.getName()
							+ " of class "
							+ method.getDeclaringClass().getName());
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot invoke method "
					+ method.getName() + " of class "
					+ method.getDeclaringClass().getName());
		}
	}

	public static Object invokeSetter(Object target, String name, Object value) {
		Method setter = getUniqueMethodByName(target.getClass(), name);
		return invokeSetter(target, setter, value);
	}

	public static Object invokeSetter(Object target, String name, Object value, Class<?> type) {
		Method setter = getMethod(target.getClass(), name, new Class[]{type});
		return invokeSetter(target, setter, value);
	}





	public static Object invokeSetter(Object target, Method method, Object value) {
		try {
			return method.invoke(target, new Object[] { value });
		} catch (InvocationTargetException ex) {
			throw new SystemException(
					"Unknown error while invoking method " + method.getName()
							+ " of class "
							+ method.getDeclaringClass().getName());
		} catch( IllegalArgumentException ex) {
			if (method.getParameterTypes().length == 1
					&& method.getParameterTypes()[0] == boolean.class
					&& (value.toString().equalsIgnoreCase("true") || 
						value.toString().equalsIgnoreCase("false"))) {
				return invokeSetter(target, method, Boolean.parseBoolean(value.toString()));
			}
			else
				throw new SystemException(ex,"Cannot invoke method "
						+ method.getName() + " of class "
						+ method.getDeclaringClass().getName());
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot invoke method "
					+ method.getName() + " of class "
					+ method.getDeclaringClass().getName());
		}
	}

	public static Object invokeSetter(Object target, String name, int index,
			Object value) {
		Method method = getUniqueMethodByName(target.getClass(), name);
		try {
			return method.invoke(target, new Object[] { new Integer(index),
					value });
		} catch (InvocationTargetException ex) {
			throw new SystemException(
					"Unknown error while invoking method " + method.getName()
							+ " of class "
							+ method.getDeclaringClass().getName());
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot invoke method "
					+ method.getName() + " of class "
					+ method.getDeclaringClass().getName());
		}
	}

	public static Object invokeSetter(Object target, String name, int index,
			Object value, Class<?> type) {
		Method method = getMethod(target.getClass(), name, new Class[]{int.class, type});
		try {
			return method.invoke(target, new Object[] { new Integer(index),
					value });
		} catch (InvocationTargetException ex) {
			throw new SystemException(
					"Unknown error while invoking method " + method.getName()
							+ " of class "
							+ method.getDeclaringClass().getName());
		} catch (Exception ex) {
			throw new SystemException(ex,"Cannot invoke method "
					+ method.getName() + " of class "
					+ method.getDeclaringClass().getName());
		}
	}

	/**
	 * Return the <code>java.lang.reflect.Field</code> for the given
	 * identifiers.
	 *
	 * @param cls
	 *            the <code>java.lang.Class</code>
	 * @param name
	 *            the name of the field
	 * @return the according field
	 */
	public static Field getField(Class<?> cls, String name) {
		Field f = null;
		CacheEntry cacheEntry = (CacheEntry) classes.get(cls);
		if (cacheEntry == null) {
			f = searchField(cls, name, true);
			cacheEntry = new CacheEntry();
			cacheEntry.put(f);
			classes.put(cls, cacheEntry);
		} else {
			f = cacheEntry.getField(name);
			if (f == null) {
				f = searchField(cls, name, true);
				cacheEntry.put(f);
			}
		}
		return f;
	}

	/**
	 * Return the <code>java.lang.reflect.Method</code> for the given
	 * identifiers.
	 *
	 * @param cls
	 *            the <code>java.lang.Class</code>
	 * @param name
	 *            the name of the method
	 * @return the according method
	 */
	public static Method getMethod(Class<?> cls, String name) {
		Method m = null;
		CacheEntry cacheEntry = (CacheEntry) classes.get(cls);
		if (cacheEntry == null) {
			m = searchMethod(cls, name, EMPTY_CLASS_ARRAY);
			//if (m == null)
			//	return null;
			cacheEntry = new CacheEntry();
			cacheEntry.put(m);
			classes.put(cls, cacheEntry);
		} else {
			m = cacheEntry.getMethod(name);
			if (m == null) {
				m = searchMethod(cls, name, EMPTY_CLASS_ARRAY);
				//if (m == null)
				//	return null;
				cacheEntry.put(m);
			}
		}
		return m;
	}

	/**
	 * Return the <code>java.lang.reflect.Method</code> for the given
	 * identifiers.
	 *
	 * @param cls
	 *            the <code>java.lang.Class</code>
	 * @param name
	 *            the name of the method
	 * @param params
	 *            the parameter types
	 * @return the according method
	 */
	public static Method getMethod(Class<?> cls, String name, Class<?>[] params) {
		// if the method has parameters caching is to expensive
		if (params == EMPTY_CLASS_ARRAY)
			return getMethod(cls, name);
		else
			return searchMethod(cls, name, params);
	}

	public static Method getUniqueMethodByName(Class<?> cls, String name) {
		Method m = null;
		CacheEntry cacheEntry = (CacheEntry) classes.get(cls);
		if (cacheEntry == null) {
			m = searchUniqueMethodByName(cls, name);
			cacheEntry = new CacheEntry();
			cacheEntry.put(m);
			classes.put(cls, cacheEntry);
		} else {
			m = cacheEntry.getMethod(name);
			if (m == null) {
				m = searchUniqueMethodByName(cls, name);
				cacheEntry.put(m);
			}
		}
		return m;
	}

	public static Method searchUniqueMethodByName(Class<?> cls, String name) {
		Class<?> currentClass = cls;
		while (currentClass != null && currentClass != Object.class) {
			Method method = null;
			try {
				final Method[] methods = currentClass.getDeclaredMethods();
				for (int i = 0; i < methods.length; i++) {
					if (name.equals(methods[i].getName())) {
						if (method != null)
							throw new SystemException("The method " + name
									+ " is not defined only once in class "
									+ cls.getName());
						method = methods[i];
					}
				}
			} catch (Exception e) {
				// Ignore
			}
			if (method != null) {
				method.setAccessible(true);
				return method;
			}
			currentClass = currentClass.getSuperclass();
		}
		throw new SystemException("The method " + name
				+ " is not defined in class " + cls.getName());
	}

	public static Class<?> getClass(String className) {
		Class<?> primitive = (Class<?>) primitives.get(className);
		if (primitive != null)
			return primitive;
		try {
			if (clazz != null)
				return Class.forName(className, true, clazz.getClassLoader());
			return Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new SystemException(ex,"The class " + className
					+ " was not found");
		} catch (NoClassDefFoundError ex) {
			throw new SystemException(ex,"The class " + className
					+ " was not found");
		} 
	}

	public static String convertPrimitiveClassName(String shortName) {
		String wrapper = (String) primitivesToWrapper.get(shortName);
		if (wrapper != null)
			return wrapper;
		else
			return shortName;
	}

	public static Class<?> convertToPrimitive(Class<?> clazz) {
		Class<?> wrapper = (Class<?>) wrappersToPrimitives.get(clazz);
		if (wrapper != null)
			return wrapper;
		else
			return clazz;
	}

	public static Class<?> existsClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	/**
	 * Returns the class name for the given class without the prefix 'class '
	 * and without it's package name.
	 *
	 * @param objectClass
	 *            the class
	 * @return the simple class name without package
	 */
	public static String getSimpleClassName(String objectClass) {
		String fullName = objectClass;
		return fullName.substring(fullName.lastIndexOf('.') + 1, fullName
				.length());
	}

	/**
	 * Returns the class name for the given class without the prefix 'class '.
	 *
	 * @param objectClass
	 *            the class
	 * @return the simple class name without class prefix
	 */
	public static String getClassName(Class<?> objectClass) {
		return objectClass.getPackage().getName() + "."
				+ getSimpleClassName(objectClass.getName());
	}

	private static Method searchMethod(Class<?> cls, String name, Class<?>[] params) {
		Class<?> currentClass = cls;
		while (currentClass != null && currentClass != Object.class) {
			Method method = null;
			try {
				method = currentClass.getDeclaredMethod(name, params);
			} catch (Exception e) {
				// Ignore
			}
			if (method != null) {
				method.setAccessible(true);
				return method;
			}
			currentClass = currentClass.getSuperclass();
		}
		StringBuffer string = new StringBuffer("The method " + name
				+ " is not defined in class " + cls.getName() + " with parameters ");
		for (int i = 0; i < params.length; i++) {
			string.append(params[i].getName());
			if (i < params.length - 1)
				string.append(", ");
		}
		throw new SystemException(string.toString());
	}

	public static Field searchField(Class<?> cls, String name) {
		return searchField(cls, name, false);
	}
	
	public static Field searchField(Class<?> cls, String name, boolean throwEx) {
		Class<?> currentClass = cls;
		while (currentClass != null && currentClass != Object.class) {
			Field field = null;
			try {
				field = currentClass.getDeclaredField(name);
			} catch (Exception e) {
				// Ignore
			}
			if (field != null) {
				field.setAccessible(true);
				return field;
			}
			currentClass = currentClass.getSuperclass();
		}
		if (throwEx)
			throw new SystemException("The field " + name
				+ " is not defined in class " + cls.getName());
		else
			return null;
	}

	public static Object getValue(Object target, String fieldName) {
		try {
			Field field = getField(target.getClass(), fieldName);
			return field.get(target);
		} catch (Exception ex) {
			throw new SystemException("The value " + fieldName
					+ " is not defined in class " + target.getClass().getName());
		}
	}

	public static void setValue(Object target, String fieldName, Object value) {
		try {
			Field field = getField(target.getClass(), fieldName);
			field.set(target, value);
		} catch (Exception ex) {
			throw new SystemException("The value " + fieldName
					+ " is not defined in class " + target.getClass().getName());
		}
	}

	public static Class<?> getClazz() {
		return clazz;
	}

	public static void setClazz(Class<?> classLoader) {
		ReflectionUtils.clazz = classLoader;
	}
	
	public static Constructor<?> getConstructor(Class<?> clz, Class<?>[] param) {
		try {
			return clz.getConstructor(param);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
	
	public static String getFileName(String className) {
		return "/" + className.replace('.', '/') + ".class";
	}
 
}