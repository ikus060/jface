/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.conversion;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.ValidationStatus;
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

	private static final class NumberToStringConverter extends Converter {
		private final NumberFormat format;

		private NumberToStringConverter(Object fromType, Object toType,
				NumberFormat format) {
			super(fromType, toType);
			this.format = format;
		}

		@Override
		public Object convert(Object fromObject) {
			if (!(fromObject instanceof Float)) {
				return ""; //$NON-NLS-1$
			}
			return format.format(fromObject);
		}
	}

	/**
	 * Converter used to converter string into number using different
	 * NumberFormat instances..
	 * 
	 * @author Patrik Dufresne
	 * 
	 */
	private static final class StringToNumberConverter extends Converter {
		private final List<NumberFormat> formats;

		private StringToNumberConverter(Object fromType, Object toType,
				List<NumberFormat> formats) {
			super(fromType, toType);
			this.formats = formats;
		}

		@Override
		public Object convert(Object fromObject) {
			if (fromObject == null) {
				return ValidationStatus.ok();
			}
			String input = fromObject.toString();
			// Try to parse the string using the percent format
			for (NumberFormat format : formats) {
				ParsePosition pos = new ParsePosition(0);
				Number number = format.parse(input, pos);
				if (pos.getIndex() >= input.length()) {
					return Float.valueOf(number.floatValue());
				}
			}
			throw new IllegalArgumentException();
		}
	}

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
	 * Create multiple number format from a given number format to support a
	 * wider range of string input.
	 * 
	 * @param currencyInstance
	 * @return
	 */
	public static List<NumberFormat> createFormats(NumberFormat format) {
		List<NumberFormat> list = new ArrayList<NumberFormat>();
		list.add(format);
		if (!(format instanceof DecimalFormat)) {
			return list;
		}
		list.add(reverseDecimalSeparator((DecimalFormat) format));
		DecimalFormat withoutSpace = removeSpace((DecimalFormat) format);
		list.add(withoutSpace);
		list.add(reverseDecimalSeparator(withoutSpace));
		return list;
	}

	/**
	 * Return a converter to transform a formated currency string into a float
	 * using default locale.
	 * 
	 * @return the converter
	 */
	public static IConverter currencyToFloat(boolean primitive) {
		return currencyToFloat(primitive, Locale.getDefault());
	}

	/**
	 * Return a converter to transform a formated string into a float using
	 * specified locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @param locale
	 *            the specified locale
	 * @return the converter
	 */
	public static IConverter currencyToFloat(boolean primitive, Locale locale) {
		// Create a list of number format to support a wide range of number
		// format.
		final List<NumberFormat> formats = Converters
				.createFormats(NumberFormat.getCurrencyInstance(locale));
		formats.addAll(Converters.createFormats(NumberFormat
				.getNumberInstance(locale)));
		return new StringToNumberConverter(String.class, primitive ? Float.TYPE
				: Float.class, formats);
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
	 * Convert a float into a formated currency string using the default locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @return the converter
	 */
	public static IConverter floatToCurrency(boolean primitive) {
		return floatToCurrency(primitive, Locale.getDefault());
	}

	/**
	 * Convert a float into a formated currency string using the specfiied
	 * locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @param locale
	 *            the specified locale
	 * @return the converter
	 */
	public static IConverter floatToCurrency(boolean primitive, Locale locale) {

		final NumberFormat format = NumberFormat.getCurrencyInstance(locale);

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
		return new NumberToStringConverter(
				primitive ? Float.TYPE : Float.class, String.class, format);
	}

	public static IConverter floatToString(boolean primitive) {
		return floatToString(primitive, 0, 0, Locale.getDefault());
	}

	public static IConverter floatToString(boolean primitive, int minDecimal,
			int maxDecimal) {
		return floatToString(primitive, minDecimal, maxDecimal,
				Locale.getDefault());
	}

	public static IConverter floatToString(boolean primitive, int minDecimal,
			int maxDecimal, Locale locale) {
		final NumberFormat format = NumberFormat.getNumberInstance(locale);
		format.setMinimumFractionDigits(minDecimal);
		format.setMaximumFractionDigits(maxDecimal);
		return new NumberToStringConverter(
				primitive ? Float.TYPE : Float.class, String.class, format);
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
	 * Clone the given number format and remove the space from the prefix ans
	 * suffix.
	 * 
	 * @param format
	 *            the format
	 * @return a cloned format without space.
	 */
	private static DecimalFormat removeSpace(DecimalFormat format) {
		DecimalFormat clone = (DecimalFormat) format.clone();
		clone.setNegativePrefix(clone.getNegativePrefix().trim());
		clone.setNegativeSuffix(clone.getNegativeSuffix().trim());
		clone.setPositivePrefix(clone.getPositivePrefix().trim());
		clone.setPositiveSuffix(clone.getPositiveSuffix().trim());
		return clone;
	}

	/**
	 * Clone the given number format and reverse the decimal separator from
	 * comma to dot or from dot to comma.
	 * 
	 * @param format
	 *            the format
	 * @return a cloned format with different decimal separator.
	 */
	private static DecimalFormat reverseDecimalSeparator(DecimalFormat format) {
		DecimalFormat clone = (DecimalFormat) format.clone();
		DecimalFormatSymbols symbols = clone.getDecimalFormatSymbols();
		symbols.setDecimalSeparator(symbols.getDecimalSeparator() == '.' ? ','
				: '.');
		symbols.setMonetaryDecimalSeparator(symbols
				.getMonetaryDecimalSeparator() == '.' ? ',' : '.');
		clone.setDecimalFormatSymbols(symbols);
		return clone;
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

	/**
	 * Return a converter to transform a formated string into a float using
	 * default locale.
	 * 
	 * @param primitive
	 *            True to converter into primitive type.
	 * @return the converter
	 */
	public static IConverter stringToFloat(boolean primitive) {
		return stringToFloat(primitive, 0, 0, Locale.getDefault());
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
	public static IConverter stringToFloat(boolean primitive, int minDecimal,
			int maxDecimal) {
		return stringToFloat(primitive, minDecimal, maxDecimal,
				Locale.getDefault());
	}

	/**
	 * Return a converter to transform a formated string into a float using
	 * specified locale.
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
	public static IConverter stringToFloat(boolean primitive, int minDecimal,
			int maxDecimal, Locale locale) {
		NumberFormat format = NumberFormat.getNumberInstance(locale);
		format.setMinimumFractionDigits(minDecimal);
		format.setMaximumFractionDigits(maxDecimal);
		final List<NumberFormat> formats = Converters.createFormats(format);
		return new StringToNumberConverter(String.class, primitive ? Float.TYPE
				: Float.class, formats);
	}

	public static IConverter stringToRGB() {
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
