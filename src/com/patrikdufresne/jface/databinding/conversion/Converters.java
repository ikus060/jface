/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.conversion;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
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
	 * Singleton converter.
	 */
	private static IConverter removeFrontNumber;

	/**
	 * Create a converter to convert a boolean value to a string value.
	 * 
	 * @return the converter
	 */
	public static IConverter booleanConverter() {
		return new Converter(Boolean.class, String.class) {

			private static final String EMPTY = ""; //$NON-NLS-1$
			private static final String X = "X"; //$NON-NLS-1$

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

	/**
	 * Convert a float into a formated percentage string using the default
	 * locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @return the converter
	 */
	public static IConverter floatToPercent(boolean primitive) {
		return floatToPercent(primitive, 0, 0, Locale.getDefault());
	}

	/**
	 * Convert a float into a formated percentage string using the default
	 * locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @param minDecimal
	 *            the minimum number of digits allowed in the fraction portion
	 *            of a number
	 * @param maxDecimal
	 *            the maximum number of digits allowed in the fraction portion
	 *            of a number
	 * @return the converter
	 */
	public static IConverter floatToPercent(boolean primitive, int minDecimal,
			int maxDecimal) {
		return floatToPercent(primitive, minDecimal, maxDecimal,
				Locale.getDefault());
	}

	/**
	 * Convert a float into a formated percentage string using the specfiied
	 * locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @param minDecimal
	 *            the minimum number of digits allowed in the fraction portion
	 *            of a number
	 * @param maxDecimal
	 *            the maximum number of digits allowed in the fraction portion
	 *            of a number
	 * @param locale
	 *            the specified locale
	 * @return the converter
	 */
	public static IConverter floatToPercent(boolean primitive, int minDecimal,
			int maxDecimal, Locale locale) {
		final NumberFormat format = NumberFormat.getPercentInstance(locale);
		format.setMinimumFractionDigits(minDecimal);
		format.setMaximumFractionDigits(maxDecimal);

		return new Converter(primitive ? Float.TYPE : Float.class, String.class) {

			@Override
			public Object convert(Object fromObject) {
				if (!(fromObject instanceof Float)) {
					return ""; //$NON-NLS-1$
				}
				return format.format(fromObject);
			}
		};
	}

	/**
	 * Return a converter to transform a formated string into a float using
	 * default locale.
	 * 
	 * @return the converter
	 */
	public static IConverter percentToFloat(boolean primitive) {
		return percentToFloat(primitive, 0, 0, Locale.getDefault());
	}

	/**
	 * Return a converter to transform a formated string into a float using
	 * default locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @param minDecimal
	 *            the minimum number of digits allowed in the fraction portion
	 *            of a number
	 * @param maxDecimal
	 *            the maximum number of digits allowed in the fraction portion
	 *            of a number
	 * @return the converter
	 */
	public static IConverter percentToFloat(boolean primitive, int minDecimal,
			int maxDecimal) {
		return percentToFloat(primitive, minDecimal, maxDecimal,
				Locale.getDefault());
	}

	/**
	 * Return a converter to transform a formated string into a float using
	 * specified locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * 
	 * @param minDecimal
	 *            the minimum number of digits allowed in the fraction portion
	 *            of a number
	 * @param maxDecimal
	 *            the maximum number of digits allowed in the fraction portion
	 *            of a number
	 * @param locale
	 *            the specified locale
	 * @return the converter
	 */
	public static IConverter percentToFloat(boolean primitive, int minDecimal,
			int maxDecimal, Locale locale) {

		final NumberFormat percentFormat = NumberFormat
				.getPercentInstance(locale);
		percentFormat.setMinimumFractionDigits(minDecimal);
		percentFormat.setMaximumFractionDigits(maxDecimal);

		final NumberFormat format = NumberFormat.getNumberInstance(locale);
		format.setMinimumFractionDigits(minDecimal);
		format.setMaximumFractionDigits(maxDecimal);

		return new Converter(String.class, primitive ? Float.TYPE : Float.class) {

			@Override
			public Object convert(Object fromObject) {
				if (fromObject == null) {
					return null;
				}
				try {
					return Float.valueOf(percentFormat.parse(
							fromObject.toString()).floatValue());
				} catch (ParseException e) {
					try {
						return Float
								.valueOf(format.parse(fromObject.toString())
										.floatValue() / 100f);
					} catch (ParseException e2) {
						return Float.valueOf(0f);
					}
				}
			}
		};
	}

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

}
