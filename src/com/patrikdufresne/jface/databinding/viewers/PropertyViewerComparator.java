/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.viewers;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.internal.databinding.property.value.SelfValueProperty;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * This viewer comparator doesn't based it's comparison on the element string
 * representation or label. It's compare the element it self. When a comparator
 * is provided in the constructor, the comparator should support the element
 * type.
 * 
 * @author Patrik Dufresne
 * 
 */
@SuppressWarnings("rawtypes")
public class PropertyViewerComparator extends ViewerComparator {

	/**
	 * Comparator to used or null.
	 */
	private Comparator comparator;

	/**
	 * The property used to compare.
	 */
	private IValueProperty property;

	/**
	 * Create a new viewer comparator with a self value property and the given
	 * comparator.
	 * 
	 * @param comparator
	 *            the comparator.
	 */
	public PropertyViewerComparator(Comparator comparator) {
		this(null, comparator);
	}

	/**
	 * Create a new viewer comparator with a natural order comparator and the
	 * given value property.
	 * 
	 * @param property
	 */
	public PropertyViewerComparator(IValueProperty property) {
		this(property, null);
	}

	/**
	 * Create a new viewer comparator based on a comparator.
	 * 
	 * @param comparator
	 */
	public PropertyViewerComparator(IValueProperty property,
			Comparator comparator) {
		this.property = property;
		if (this.property == null) {
			this.property = new SelfValueProperty(null);
		}

		this.comparator = comparator;
		if (this.comparator == null) {
			this.comparator = ColumnSupport.naturalComparator();
		}
	}

	/**
	 * This implementation used the given comparator.
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int cat1 = category(e1);
		int cat2 = category(e2);
		if (cat1 != cat2) {
			return cat1 - cat2;
		}
		return this.comparator.compare(e1, e2);
	}

	/**
	 * This implementation used the given comparator directly.
	 */
	@Override
	public void sort(final Viewer viewer, Object[] elements) {
		Arrays.sort(elements, this.comparator);
	}

}
