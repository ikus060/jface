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
package com.patrikdufresne.jface.databinding.validation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.internal.databinding.BindingMessages;
import org.eclipse.core.runtime.IStatus;

import com.patrikdufresne.jface.databinding.conversion.Converters;

public class Validators {

    /**
     * Validator used to check the format of an input string against multiple number format.
     * 
     * @author Patrik Dufresne
     * 
     */
    private static final class StringToNumberValidator implements IValidator {

        private final List<NumberFormat> formats;

        private StringToNumberValidator(List<NumberFormat> formats) {
            this.formats = formats;
        }

        @Override
        public IStatus validate(Object value) {
            String input;
            if (value == null || (input = value.toString().trim()).isEmpty()) {
                return ValidationStatus.ok();
            }
            // Try to parse the string using the percent format
            for (NumberFormat format : formats) {
                ParsePosition pos = new ParsePosition(0);
                format.parse(input, pos);
                if (pos.getIndex() >= input.length()) {
                    return ValidationStatus.ok();
                }
            }
            return ValidationStatus.error(getExampleErrorMessage(formats.get(0).format(0.123456f)));
        }
    }

    protected static final String VALIDATE_NOT_NULL = "Validate_NotNull"; //$NON-NLS-1$

    /**
     * Return a validator to check the format of a string previous to a conversion of a percent string using default
     * locale.
     * 
     * @return the validator
     */
    public static IValidator currencyToFloat() {
        return currencyToFloat(Locale.getDefault());
    }

    /**
     * Return a validator to check the format of a string previous to a conversion of a currency string using the given
     * locale. This validator use multiple number format to parse the input string. It tries to parse the string using
     * different decimal separator : dot (.) or comma (,)
     * 
     * @return the validator
     */
    public static IValidator currencyToFloat(Locale locale) {
        // Create a list of number format to support a wide range of number
        // format.
        final List<NumberFormat> formats = Converters.createFormats(NumberFormat.getCurrencyInstance(locale));
        formats.addAll(Converters.createFormats(NumberFormat.getNumberInstance(locale)));

        // Create the validator.
        return new StringToNumberValidator(formats);
    }

    /**
     * Create an examples string using the format.
     * 
     * @return
     */
    static String getExampleErrorMessage(String example) {
        StringBuilder buf = new StringBuilder();
        buf.append(BindingMessages.getString(BindingMessages.EXAMPLES));
        buf.append(" "); //$NON-NLS-1$
        buf.append(example);
        return buf.toString();
    }

    /**
     * Return a validator checking if the value is null. When null this validator return an error.
     * 
     * @return the validator.
     */
    public static IValidator notNull() {
        return new IValidator() {
            @Override
            public IStatus validate(Object value) {
                if (value == null) {
                    return ValidationStatus.error(BindingMessages.getString(VALIDATE_NOT_NULL));
                }
                return ValidationStatus.ok();
            }
        };
    }

    /**
     * Return a validator to check the format of a string previous to a conversion of a percent string using default
     * locale.
     * 
     * @return the validator
     * @see Converters#percentToFloat(boolean)
     */
    public static IValidator percentToFloat() {
        return percentToFloat(0, 0);
    }

    /**
     * Return a validator to check the format of a string previous to a conversion of a percent string using default
     * locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number.
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number.
     * @return the validator
     * @see Converters#percentToFloat(boolean)
     */
    public static IValidator percentToFloat(int minDecimal, int maxDecimal) {
        return percentToFloat(minDecimal, maxDecimal, Locale.getDefault());
    }

    /**
     * Return a validator to check the format of a string previous to a conversion of a percent string using the given
     * locale.
     * 
     * @param minDecimal
     *            the minimum number of digits allowed in the fraction portion of a number.
     * @param maxDecimal
     *            the maximum number of digits allowed in the fraction portion of a number.
     * @return the validator
     * @see Converters#percentToFloat(boolean)
     */
    public static IValidator percentToFloat(int minDecimal, int maxDecimal, Locale locale) {

        final NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
        percentFormat.setMinimumFractionDigits(minDecimal);
        percentFormat.setMaximumFractionDigits(maxDecimal);

        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(minDecimal);
        format.setMaximumFractionDigits(maxDecimal);

        return new IValidator() {

            @Override
            public IStatus validate(Object value) {
                if (value == null) {
                    return ValidationStatus.ok();
                }
                String input = value.toString();
                // Try to parse the string using the percent format
                ParsePosition pos = new ParsePosition(0);
                percentFormat.parse(input, pos);
                if (pos.getIndex() >= input.length()) {
                    return ValidationStatus.ok();
                }
                // Try to parse the string using a number format
                pos = new ParsePosition(0);
                format.parse(input, pos);
                if (pos.getIndex() >= input.length()) {
                    return ValidationStatus.ok();
                }
                return ValidationStatus.error(getExampleErrorMessage(percentFormat.format(0.123456f)));
            }
        };
    }

    /**
     * Create a new validator to parse date from a string. This should be use in conjunction with stringToDateConverter.
     * 
     * @param format
     *            the date format.
     * @return the validator
     */
    public static IValidator stringToDateValidator(final DateFormat format) {
        return new IValidator() {

            @Override
            public IStatus validate(Object value) {
                String input = value.toString().trim();
                ParsePosition pos = new ParsePosition(0);
                Date result = format.parse(input, pos);
                if (pos.getIndex() >= input.length()) {
                    return ValidationStatus.ok();
                }
                return ValidationStatus.error(getExampleErrorMessage(format.format(new Date())));
            }

        };
    }

    /**
     * Return a validator to check the number format using default locale.
     * 
     * @return
     */
    public static IValidator stringToFloat() {
        return stringToFloat(Locale.getDefault());
    }

    /**
     * Return a validator to check the format of a string using the specified locale.
     * 
     * @return
     */
    public static IValidator stringToFloat(Locale locale) {
        // Create a list of number format to support a wide range of number
        // format.
        final List<NumberFormat> formats = Converters.createFormats(NumberFormat.getNumberInstance(locale));

        // Create the validator.
        return new StringToNumberValidator(formats);
    }
}
