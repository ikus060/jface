/**
 * Copyright(C) 2013 Patrik Dufresne Service Logiciel <info@patrikdufresne.com>
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
package com.patrikdufresne.jface.databinding.conversion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private static class NumberToStringConverter extends Converter {
        private final NumberFormat format;

        private NumberToStringConverter(Object fromType, Object toType, NumberFormat format) {
            super(fromType, toType);
            this.format = format;
        }

        @Override
        public Object convert(Object fromObject) {
            if (!(fromObject instanceof Number)) {
                return ""; //$NON-NLS-1$
            }
            return format.format(fromObject);
        }
    }

    /**
     * Converter used to converter string into number using different NumberFormat instances..
     * 
     * @author Patrik Dufresne
     * 
     */
    private static class StringToNumberConverter extends Converter {
        private final List<NumberFormat> formats;

        private StringToNumberConverter(Object fromType, Object toType, List<NumberFormat> formats) {
            super(fromType, toType);
            this.formats = formats;
        }

        @Override
        public Object convert(Object fromObject) {
            // If the source is null, convert it to null or 0.
            String input;
            if (fromObject == null || (input = fromObject.toString().trim()).isEmpty()) {
                return getToType() instanceof Class && ((Class) getToType()).isPrimitive() ? typeCast(0) : null;
            }
            // Try to parse the string using the percent format
            for (NumberFormat format : formats) {
                ParsePosition pos = new ParsePosition(0);
                Number number = format.parse(input, pos);
                if (pos.getIndex() >= input.length()) {
                    return typeCast(number);
                }
            }
            throw new IllegalArgumentException();
        }

        private Object typeCast(Number n) {
            if (Integer.class.equals(getToType()) || Integer.TYPE.equals(getToType())) {
                return Integer.valueOf(n.intValue());
            } else if (Double.class.equals(getToType()) || Double.TYPE.equals(getToType())) {
                return Double.valueOf(n.doubleValue());
            } else if (Long.class.equals(getToType()) || Long.TYPE.equals(getToType())) {
                return Long.valueOf(n.longValue());
            } else if (Float.class.equals(getToType()) || Float.TYPE.equals(getToType())) {
                return Float.valueOf(n.floatValue());
            } else if (BigInteger.class.equals(getToType())) {
                if (n instanceof Long) return BigInteger.valueOf(n.longValue());
                else if (n instanceof BigInteger) return n;
                else if (n instanceof BigDecimal) return ((BigDecimal) n).toBigInteger();
                else
                    return new BigDecimal(n.doubleValue()).toBigInteger();
            } else if (BigDecimal.class.equals(getToType())) {
                if (n instanceof Long) return BigDecimal.valueOf(n.longValue());
                else if (n instanceof BigInteger) return new BigDecimal((BigInteger) n);
                else if (n instanceof BigDecimal) return n;
                else if (n instanceof Double) return BigDecimal.valueOf(n.doubleValue());
            } else if (Short.class.equals(getToType()) || Short.TYPE.equals(getToType())) {
                return Short.valueOf(n.shortValue());
            } else if (Byte.class.equals(getToType()) || Byte.TYPE.equals(getToType())) {
                return Byte.valueOf(n.byteValue());
            }
            return n;
        }
    }

    private static final String EMPTY = ""; //$NON-NLS-1$

    /**
     * Singleton converter.
     */
    private static IConverter removeFrontNumber;

    /**
     * Convert {@link BigDecimal} into a formated currency string using the default locale.
     * 
     * @return the converter
     */
    public static IConverter bigDecimalToCurrency() {
        return bigDecimalToCurrency(Locale.getDefault());
    }

    /**
     * Convert {@link BigDecimal} into a formated currency string using the specified locale.
     * 
     * @param locale
     *            the locale
     * @return the converter
     */
    public static IConverter bigDecimalToCurrency(Locale locale) {
        return numberToCurrency(BigDecimal.class, locale);
    }

    /**
     * Convert a {@link BigDecimal} into a formated percentage string using the default locale.
     * 
     * @return the converter
     */
    public static IConverter bigDecimalToPercent() {
        return numberToPercent(BigDecimal.class, 0, 0, Locale.getDefault());
    }

    /**
     * Convert a {@link BigDecimal} into a formated percentage string using the default locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter bigDecimalToPercent(int minDecimal, int maxDecimal) {
        return numberToPercent(BigDecimal.class, minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Convert a {@link BigDecimal} into a formated percentage string using the specified locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter bigDecimalToPercent(int minDecimal, int maxDecimal, Locale locale) {
        return numberToPercent(BigDecimal.class, minDecimal, maxDecimal, locale);
    }

    /**
     * Convert {@link BigDecimal} into a formated number using default locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter bigDecimalToString(int minDecimal, int maxDecimal) {
        return bigDecimalToString(minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Convert {@link BigDecimal} into a formated number using the specified locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the locale
     * @return the converter
     */
    public static IConverter bigDecimalToString(int minDecimal, int maxDecimal, Locale locale) {
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);
        return new NumberToStringConverter(BigDecimal.class, String.class, format);
    }

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
     * Create multiple number format from a given number format to support a wider range of string input.
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
     * Return a converter to transformed a formated string in a {@link BigDecimal} using the default locale.
     * 
     * @return the converter
     */
    public static IConverter currencyToBigDecimal() {
        return currencyToBigDecimal(Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a {@link BigDecimal} number using the specified locale.
     * 
     * @param locale
     *            the locale used for conversion.
     * @return the converter
     */
    public static IConverter currencyToBigDecimal(Locale locale) {
        return currencyToNumber(BigDecimal.class, locale);
    }

    /**
     * Return a converter to transform a formated currency string into a float using default locale.
     * 
     * @return the converter
     */
    public static IConverter currencyToFloat(boolean primitive) {
        return currencyToFloat(primitive, Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a float using specified locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter currencyToFloat(boolean primitive, Locale locale) {
        return currencyToNumber(primitive ? Float.TYPE : Float.class, locale);
    }

    /**
     * Return a converter to transform a formated string into a Number using the specified locale.
     * 
     * @param toType
     *            the target class type.
     * @param locale
     *            the locale to use for conversion
     * @return the converter
     */
    public static IConverter currencyToNumber(Class toType, Locale locale) {
        // Create a list of number format to support a wide range of input.
        final List<NumberFormat> formats = Converters.createFormats(NumberFormat.getCurrencyInstance(locale));
        formats.addAll(Converters.createFormats(NumberFormat.getNumberInstance(locale)));
        return new StringToNumberConverter(String.class, toType, formats);
    }

    /**
     * Create a converter to convert a date into it's string representation. i.e.: 13 sep 2012.
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
     * Convert a float into a formated currency string using the specfiied locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter floatToCurrency(boolean primitive, Locale locale) {
        return numberToCurrency(primitive ? Float.TYPE : Float.class, locale);
    }

    /**
     * Convert a float into a formated percentage string using the default locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @return the converter
     */
    public static IConverter floatToPercent(boolean primitive) {
        return floatToPercent(primitive, 0, 0, Locale.getDefault());
    }

    /**
     * Convert a float into a formated percentage string using the default locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter floatToPercent(boolean primitive, int minDecimal, int maxDecimal) {
        return floatToPercent(primitive, minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Convert a float into a formated percentage string using the specfiied locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter floatToPercent(boolean primitive, int minDecimal, int maxDecimal, Locale locale) {
        return numberToPercent(primitive ? Float.TYPE : Float.class, minDecimal, maxDecimal, locale);
    }

    public static IConverter floatToString(boolean primitive) {
        return floatToString(primitive, 0, 0, Locale.getDefault());
    }

    public static IConverter floatToString(boolean primitive, int minDecimal, int maxDecimal) {
        return floatToString(primitive, minDecimal, maxDecimal, Locale.getDefault());
    }

    public static IConverter floatToString(boolean primitive, int minDecimal, int maxDecimal, Locale locale) {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);
        return new NumberToStringConverter(primitive ? Float.TYPE : Float.class, String.class, format);
    }

    /**
     * Convert number of the given <code>fromType</code> into a curreny string.
     * 
     * @param fromType
     *            the source class type.
     * @param locale
     *            the locale to use for conversion
     * @return the converter
     */
    public static IConverter numberToCurrency(Class fromType, Locale locale) {
        return new NumberToStringConverter(fromType, String.class, NumberFormat.getCurrencyInstance(locale));
    }

    /**
     * Convert a float into a formated percentage string using the specfiied locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter numberToPercent(Class fromType, int minDecimal, int maxDecimal, Locale locale) {
        final NumberFormat format = NumberFormat.getPercentInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);
        return new NumberToStringConverter(fromType, String.class, format);
    }

    /**
     * Return a converter to transform a formated string into a {@link BigDecimal} using default locale.
     * 
     * @return the converter
     */
    public static IConverter percentToBigDecimal() {
        return percentToBigDecimal(0, 0);
    }

    /**
     * Return a converter to transform a formated string into a {@link BigDecimal} using default locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter percentToBigDecimal(int minDecimal, int maxDecimal) {
        return percentToNumber(BigDecimal.class, minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a {@link BigDecimal} using default locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the localed used for conversion.
     * @return the converter
     */
    public static IConverter percentToBigDecimal(int minDecimal, int maxDecimal, Locale locale) {
        return percentToNumber(BigDecimal.class, minDecimal, maxDecimal, locale);
    }

    /**
     * Return a converter to transform a formated string into a float using default locale.
     * 
     * @return the converter
     */
    public static IConverter percentToFloat(boolean primitive) {
        return percentToFloat(primitive, 0, 0, Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a float using default locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter percentToFloat(boolean primitive, int minDecimal, int maxDecimal) {
        return percentToFloat(primitive, minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a float using specified locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter percentToFloat(boolean primitive, int minDecimal, int maxDecimal, Locale locale) {
        return percentToNumber(primitive ? Float.TYPE : Float.class, minDecimal, maxDecimal, locale);
    }

    /**
     * Return a converter to transform a formated string into a number using specified locale.
     * 
     * @param toType
     *            the target class type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter percentToNumber(Class toType, int minDecimal, int maxDecimal, Locale locale) {

        // Create percent format.
        NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
        percentFormat.setMinimumFractionDigits(minDecimal);
        percentFormat.setMaximumFractionDigits(maxDecimal);
        if (BigDecimal.class.equals(toType) && percentFormat instanceof DecimalFormat) {
            ((DecimalFormat) percentFormat).setParseBigDecimal(true);
        }
        List<NumberFormat> formats = Converters.createFormats(percentFormat);

        // Create basic number format with 100 multiplier
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setMultiplier(100);
            if (BigDecimal.class.equals(toType)) {
                ((DecimalFormat) percentFormat).setParseBigDecimal(true);
            }
        }
        formats.addAll(Converters.createFormats(format));

        return new StringToNumberConverter(String.class, toType, formats);

    }

    /**
     * Return a converter to remove the front number of the team or section's name. This function always return the same
     * instance of the converter.
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
                    return this.pattern.matcher(fromObject.toString()).replaceFirst(EMPTY);
                }
            };
        }
        return removeFrontNumber;
    }

    /**
     * Clone the given number format and remove the space from the prefix ans suffix.
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
     * Clone the given number format and reverse the decimal separator from comma to dot or from dot to comma.
     * 
     * @param format
     *            the format
     * @return a cloned format with different decimal separator.
     */
    private static DecimalFormat reverseDecimalSeparator(DecimalFormat format) {
        DecimalFormat clone = (DecimalFormat) format.clone();
        DecimalFormatSymbols symbols = clone.getDecimalFormatSymbols();
        symbols.setDecimalSeparator(symbols.getDecimalSeparator() == '.' ? ',' : '.');
        symbols.setMonetaryDecimalSeparator(symbols.getMonetaryDecimalSeparator() == '.' ? ',' : '.');
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

    /**
     * Convert string into {@link BigDecimal}
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter stringToBigDecimal(int minDecimal, int maxDecimal) {
        return stringToBigDecimal(minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Convert string into {@link BigDecimal}
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the locale
     * @return the converter
     */
    public static IConverter stringToBigDecimal(int minDecimal, int maxDecimal, Locale locale) {
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);
        final List<NumberFormat> formats = Converters.createFormats(format);
        return new StringToNumberConverter(String.class, BigDecimal.class, formats);
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
     * Return a converter to transform a formated string into a float using default locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @return the converter
     */
    public static IConverter stringToFloat(boolean primitive) {
        return stringToFloat(primitive, 0, 0, Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a float using default locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @return the converter
     */
    public static IConverter stringToFloat(boolean primitive, int minDecimal, int maxDecimal) {
        return stringToFloat(primitive, minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Return a converter to transform a formated string into a float using specified locale.
     * 
     * @param primitive
     *            True to converter into primitive type.
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number
     * @param locale
     *            the specified locale
     * @return the converter
     */
    public static IConverter stringToFloat(boolean primitive, int minDecimal, int maxDecimal, Locale locale) {
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);
        final List<NumberFormat> formats = Converters.createFormats(format);
        return new StringToNumberConverter(String.class, primitive ? Float.TYPE : Float.class, formats);
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
