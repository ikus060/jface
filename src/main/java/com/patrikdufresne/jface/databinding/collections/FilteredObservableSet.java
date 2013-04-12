/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.collections;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.StaleEvent;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * Filter an observable set using a text filter.
 * 
 * @author Patrik Dufresne
 * 
 */
@SuppressWarnings("rawtypes")
public class FilteredObservableSet extends AbstractFilteredObservableSet {
	/**
	 * Private listener to avoid exposing interfaces.
	 * 
	 * @author Patrik Dufresne
	 * 
	 */
	private class PrivateInterface implements IChangeListener, IStaleListener {
		public PrivateInterface() {
			// Nothing to do
		}

		/**
		 * Notify this class about the pattern being changed.
		 */
		@Override
		public void handleChange(ChangeEvent event) {
			patternChange();
		}

		@Override
		public void handleStale(StaleEvent staleEvent) {
			if (!dirty)
				makeStale();
		}
	}

	/**
	 * Private listener
	 */
	private PrivateInterface privateInterface = new PrivateInterface();

	private static final String EMPTY = ""; //$NON-NLS-1$

	private static final String SPACE = " "; //$NON-NLS-1$

	/**
	 * The pattern use as a filter
	 */
	private IObservableValue pattern;
	/**
	 * The converter to convert element to string.
	 */
	private IConverter converter;

	private Pattern[] patterns;

	/**
	 * Create an observable filter for the set specified.
	 * 
	 * @param set
	 *            the observable set to filter
	 * @param pattern
	 *            the observable pattern
	 * @param converter
	 *            the converter or null
	 */
	public FilteredObservableSet(IObservableSet set, IObservableValue pattern,
			IConverter converter) {
		super(set);
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		this.pattern = pattern;
		this.converter = converter;
	}

	public void patternChange() {
		this.patterns = null;
		makeDirty();
	}

	/**
	 * Create an observable filter.
	 * 
	 * @param managers
	 *            the managers
	 * @param set
	 *            the observable set to filter
	 * @param pattern
	 *            the pattern value
	 * 
	 */
	public FilteredObservableSet(IObservableSet set, IObservableValue pattern) {
		this(set, pattern, null);
	}

	/**
	 * This implementation remove listener.
	 */
	@Override
	public synchronized void dispose() {
		super.dispose();
		this.pattern = null;
		this.converter = null;
	}

	/**
	 * Query the database.
	 * <p>
	 * Sub classes may override this function to query the database using
	 * something else then list().
	 * 
	 * @return a collection
	 */
	@Override
	protected Iterator doCompute() {
		// Return the current set iterator
		if (getPattern() == null || getPattern().length() == 0) {
			return getInnerSet().iterator();
		}
		return super.doCompute();
	}

	/**
	 * Return the current pattern value or null if not set
	 * 
	 * @return
	 */
	protected String getPattern() {
		if (this.pattern.getValue() instanceof String) {
			return (String) this.pattern.getValue();
		}
		return null;
	}

	/**
	 * Init alise the Filter
	 * 
	 * @param patternString
	 *            the pattern
	 */
	protected Pattern[] compilePatterns(String patternString) {
		if (patternString == null || patternString.equals("")) { //$NON-NLS-1$
			return null;
		}
		String[] strings = patternString
				.replaceAll("[^a-zA-Z0-9àâäéèêëìîïòôöùûüỳŷÿç]", SPACE) //$NON-NLS-1$
				.replaceAll("[aàâä]", "[aàâä]") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[cç]", "[cç]") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[eéèêë]", "[eéèêë]") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[iìîï]", "[iìîï]") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[oòôö]", "[oòôö]") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[uùûü]", "[uùûü]") //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll("[yỳŷÿ]", "[yỳŷÿ]") //$NON-NLS-1$ //$NON-NLS-2$
				.split(SPACE);
		Pattern[] patterns = new Pattern[strings.length];
		for (int i = 0; i < strings.length; i++) {
			patterns[i] = Pattern.compile(
					".*" + strings[i] + ".*", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return patterns;
	}

	/**
	 * Check if an elements matches the provided patterns.
	 * <p>
	 * Sub-classes
	 * 
	 * @param element
	 *            the element
	 * @return True if the pattern matches.
	 */
	@Override
	protected boolean doSelect(Object element) {
		// Compile the patterns
		if (this.patterns == null || this.patterns.length == 0) {
			this.patterns = compilePatterns(getPattern());
			if (this.patterns == null || this.patterns.length == 0) {
				return true;
			}
		}

		// Filter
		int i = 0;
		while (i < this.patterns.length
				&& this.patterns[i].matcher(toString(element)).matches()) {
			i++;
		}
		if (i < this.patterns.length)
			return false;
		return true;
	}

	/**
	 * Convert the given element to a string representation using the convert is
	 * set or {@link #toString()}
	 * 
	 * @param element
	 * @return
	 */
	protected String toString(Object element) {
		if (element == null) {
			return EMPTY;
		}
		if (this.converter == null) {
			return element.toString();
		}
		return this.converter.convert(element).toString();
	}

	/**
	 * 
	 * Add listener to dependencies
	 */
	@Override
	protected void startListening() {
		super.startListening();
		if (this.pattern != null) {
			this.pattern.addChangeListener(this.privateInterface);
			this.pattern.addStaleListener(this.privateInterface);
		}
	}

	/**
	 * Remove listener from dependencies.
	 */
	@Override
	protected void stopListening() {
		super.stopListening();
		if (this.pattern != null) {
			this.pattern.removeChangeListener(this.privateInterface);
			this.pattern.addStaleListener(this.privateInterface);
		}
	}
}
