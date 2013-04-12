/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.preference;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Utility class to save restore Array value in a preference store.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PreferenceConverter {

	/**
	 * Returns the current value of the integer-array-valued preference with the
	 * given name. Returns an empty array if there is no preference with the
	 * given name, or if the current value cannot be treated as an array.
	 * 
	 * @param prefstore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @return the int-array-valued preference
	 */
	public static int[] getIntArray(IPreferenceStore prefstore, String key) {
		int size = prefstore.getInt(key + ".size"); //$NON-NLS-1$
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = prefstore.getInt(key + "." + Integer.toString(i)); //$NON-NLS-1$
		}
		return array;
	}

	/**
	 * Returns the current value of the String-array-valued preference with the
	 * given name. Returns an empty array if there is no preference with the
	 * given name, or if the current value cannot be treated as an array.
	 * 
	 * @param prefstore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @return the String-array-valued preference
	 */
	public static String[] getStringArray(IPreferenceStore prefstore, String key) {
		int size = prefstore.getInt(key + ".size"); //$NON-NLS-1$
		String[] array = new String[size];
		for (int i = 0; i < size; i++) {
			array[i] = prefstore.getString(key + "." + Integer.toString(i)); //$NON-NLS-1$
		}
		return array;
	}

	/**
	 * Sets the default value for the integer-array-valued preference with the
	 * given name.
	 * 
	 * Note that the current value of the preference is affected if the
	 * preference's current value was its old default value, in which case it
	 * changes to the new default value. If the preference's current is
	 * different from its old default value, its current value is unaffected. No
	 * property change events are reported by changing default values.
	 * 
	 * @param prefstore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param array
	 *            the new default value for the preference
	 */
	public static void setDefault(IPreferenceStore prefstore, String key,
			int[] array) {
		prefstore.setDefault(key + ".size", array.length); //$NON-NLS-1$
		for (int i = 0; i < array.length; i++) {
			prefstore.setDefault(key + "." + Integer.toString(i), array[i]); //$NON-NLS-1$
		}
	}

	/**
	 * Sets the default value for the String-array-valued preference with the
	 * given name.
	 * 
	 * Note that the current value of the preference is affected if the
	 * preference's current value was its old default value, in which case it
	 * changes to the new default value. If the preference's current is
	 * different from its old default value, its current value is unaffected. No
	 * property change events are reported by changing default values.
	 * 
	 * @param prefstore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param array
	 *            the new default value for the preference
	 */
	public static void setDefault(IPreferenceStore prefstore, String key,
			String[] array) {
		prefstore.setValue(key + ".size", array.length); //$NON-NLS-1$
		for (int i = 0; i < array.length; i++) {
			prefstore.setDefault(key + "." + Integer.toString(i), array[i]); //$NON-NLS-1$
		}
	}

	/**
	 * Sets the current value of the integer-array-valued preference with the
	 * given name.
	 * 
	 * A property change event is reported if the current value of the
	 * preference actually changes from its previous value. In the event object,
	 * the property name is the name of the preference, and the old and new
	 * values are wrapped as objects.
	 * 
	 * Note that the preferred way of re-initializing a preference to its
	 * default value is to call setToDefault.
	 * 
	 * @param prefstore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param array
	 *            the new current value of the preference
	 */
	public static void setValue(IPreferenceStore prefstore, String key,
			int[] array) {
		// TODO call firePropertyChangeEvent to raised an event
		prefstore.setValue(key + ".size", array.length); //$NON-NLS-1$
		for (int i = 0; i < array.length; i++) {
			prefstore.setValue(key + "." + Integer.toString(i), array[i]); //$NON-NLS-1$
		}
	}

	/**
	 * Sets the current value of the string-array-valued preference with the
	 * given name.
	 * 
	 * A property change event is reported if the current value of the
	 * preference actually changes from its previous value. In the event object,
	 * the property name is the name of the preference, and the old and new
	 * values are wrapped as objects.
	 * 
	 * Note that the preferred way of re-initializing a preference to its
	 * default value is to call setToDefault.
	 * 
	 * @param prefstore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param array
	 *            the new current value of the preference
	 */
	public static void setValue(IPreferenceStore prefstore, String key,
			String[] array) {
		prefstore.setValue(key + ".size", array.length); //$NON-NLS-1$
		for (int i = 0; i < array.length; i++) {
			prefstore.setValue(key + "." + Integer.toString(i), array[i]); //$NON-NLS-1$
		}
	}

}
