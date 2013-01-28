/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.conversion;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.resource.DataFormatException;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * Utility class to create converters.
 * 
 * @author Patrik Dufresne
 * 
 */
public class Converters {

	private static final String EMPTY = ""; //$NON-NLS-1$

	/**
	 * Create a converter to convert an integer value to an SWT RGB.
	 * 
	 * @return the converter
	 */
	public static IConverter RGBToString() {
		return new Converter(RGB.class, String.class) {
			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null) {
					return null;
				}
				return StringConverter.asString((RGB) fromObject);
			}
		};
	}

	public static IConverter StringToRGB() {
		return new Converter(String.class, RGB.class) {
			@Override
			public Object convert(Object fromObject) {
				try {
					return StringConverter.asRGB((String) fromObject);
				} catch (DataFormatException e) {
					// Don't throw an error within the converter.
					return null;
				}

			}
		};
	}

	/**
	 * Create a converter to convert a date into it's string representation.
	 * i.e.: 13 sep 2012.
	 * <p>
	 * If the date value is null, an empty string is returned.
	 * 
	 * @return the converter
	 */
	public static IConverter dateToStringConverter(final DateFormat format) {
		return new Converter(Date.class, String.class) {

			@Override
			public Object convert(Object fromObject) {
				if (!(fromObject instanceof Date)) {
					return EMPTY;
				}
				return format.format((Date) fromObject);
			}
		};
	}

	public static IConverter stringToDateConverter(final DateFormat format) {
		return new Converter(String.class, Date.class) {

			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null) {
					return null;
				}
				// Parse the date
				ParsePosition pos = new ParsePosition(0);
				Date result = format.parse(fromObject.toString(), pos);
				if (pos.getIndex() != 0) {
					return result;
				}
				return null;
			}
		};
	}

	/**
	 * Create a converter to convert a boolean value to a string value.
	 * 
	 * @return the converter
	 */
	public static IConverter booleanConverter() {
		return new Converter(Boolean.class, String.class) {

			private static final String X = "X"; //$NON-NLS-1$
			private static final String EMPTY = ""; //$NON-NLS-1$

			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null) {
					return null;
				}
				return ((Boolean) fromObject).booleanValue() ? X : EMPTY;
			}
		};
	}

	/**
	 * Singleton converter.
	 */
	private static IConverter removeFrontNumber;

	/**
	 * Return a converter to remove the front number of the team or section's
	 * name. This function always return the same instance of the converter.
	 * 
	 * @return the converter.
	 */
	public static IConverter removeFrontNumber() {
		if (removeFrontNumber == null) {
			removeFrontNumber = new Converter(String.class, String.class) {

				Pattern pattern;

				@Override
				public Object convert(Object fromObject) {
					if (fromObject == null) {
						return null;
					}
					if (this.pattern == null) {
						this.pattern = Pattern.compile("^[0-9]+[ \\.]*"); //$NON-NLS-1$
					}
					return this.pattern.matcher(fromObject.toString())
							.replaceFirst(EMPTY);
				}
			};
		}
		return removeFrontNumber;
	}

}
