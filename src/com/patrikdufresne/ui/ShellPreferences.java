package com.patrikdufresne.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;

/**
 * Utility class to save restore Array value in a preference store.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ShellPreferences {
	/**
	 * Preference key used to store shell location value.
	 */
	private static final String LOCATION = ".location"; //$NON-NLS-1$

	/**
	 * Preference key used to store shell maximized value.
	 */
	private static final String MAXIMIZED = ".maximized"; //$NON-NLS-1$

	/**
	 * Preference key used to store point value.
	 */
	private static final String POINT_X = ".x"; //$NON-NLS-1$

	/**
	 * Preference key used to store point value.
	 */
	private static final String POINT_Y = ".y"; //$NON-NLS-1$
	/**
	 * Preference key used to store shell size value.
	 */
	private static final String SIZE = ".size"; //$NON-NLS-1$

	/**
	 * Returns the shell's location with the given name. Returns null if there
	 * is no preference with the given name, or if the current value cannot be
	 * treated as a Point.
	 * 
	 * @param key
	 *            the name of the preference
	 * @return the point-valued preference
	 */
	public static Point getLocation(IPreferenceStore store, String key) {
		return getPoint(store, key + LOCATION);
	}

	/**
	 * Returns the shell's maximized state with the given name. Returns false if
	 * there is no preference with the given name, or if the current value
	 * cannot be treated as a boolean.
	 * 
	 * @param key
	 *            the name of the preference
	 * @return the point-valued preference
	 */
	public static boolean getMaximized(IPreferenceStore store, String key) {
		return store.getBoolean(key + MAXIMIZED);
	}

	/**
	 * Returns the point-value preference with the given name. Returns the
	 * default-default value (0, 0) if there is no preference with the given
	 * name, or if the current value cannot be treated as a Point.
	 * 
	 * @param key
	 *            the name of the preference
	 * @return the point-valued preference
	 */
	private static Point getPoint(IPreferenceStore prefstore, String key) {
		if (!prefstore.contains(key + POINT_X)
				|| !prefstore.contains(key + POINT_Y)) {
			return null;
		}
		int x = prefstore.getInt(key + POINT_X);
		int y = prefstore.getInt(key + POINT_Y);
		return new Point(x, y);
	}

	/**
	 * Returns the shell's size with the given name. Returns null if there is no
	 * preference with the given name, or if the current value cannot be treated
	 * as a Point.
	 * 
	 * @param key
	 *            the name of the preference
	 * @return the point-valued preference
	 */
	public static Point getSize(IPreferenceStore store, String key) {
		return getPoint(store, key + SIZE);
	}

	/**
	 * Sets the shell's location with the given name.
	 * 
	 * Note that the preferred way of re-initializing a preference to its
	 * default value is to call setToDefault.
	 * 
	 * @param prefStore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param location
	 *            the point-value preference
	 */
	public static void setLocation(IPreferenceStore prefStore, String key,
			Point location) {
		setValue(prefStore, key + LOCATION, location);
	}

	/**
	 * Sets the current maximized value with the given name.
	 * 
	 * @param store
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param maximized
	 *            the maximized state
	 */
	public static void setMaximized(IPreferenceStore prefstore, String key,
			boolean maximized) {
		prefstore.setValue(key + MAXIMIZED, maximized);
	}

	/**
	 * Sets the shell's size with the given name.
	 * 
	 * Note that the preferred way of re-initializing a preference to its
	 * default value is to call setToDefault.
	 * 
	 * @param prefStore
	 *            the preference store
	 * @param key
	 *            the name of the preference
	 * @param size
	 *            the point-value preference
	 */
	public static void setSize(IPreferenceStore prefStore, String key,
			Point size) {
		setValue(prefStore, key + SIZE, size);
	}

	/**
	 * Sets the current value of the point-valued preference with the given
	 * name.
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
	 * @param point
	 *            the point-value preference
	 */
	private static void setValue(IPreferenceStore prefstore, String key,
			Point point) {
		prefstore.setValue(key + POINT_X, point.x);
		prefstore.setValue(key + POINT_Y, point.y);
	}
}
