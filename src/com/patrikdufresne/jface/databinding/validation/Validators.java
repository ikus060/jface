package com.patrikdufresne.jface.databinding.validation;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.internal.databinding.BindingMessages;
import org.eclipse.core.runtime.IStatus;

import com.patrikdufresne.jface.databinding.conversion.Converters;

public class Validators {

	protected static final String VALIDATE_NOT_NULL = "Validate_NotNull"; //$NON-NLS-1$

	/**
	 * Return a validator to check the format of a string previous to a
	 * conversion of a percent string using default locale.
	 * 
	 * @return the validator
	 */
	public static IValidator currency() {
		return currency(Locale.getDefault());
	}

	/**
	 * Return a validator to check the format of a string previous to a
	 * conversion of a percent string using the given locale.
	 * 
	 * @return the validator
	 */
	public static IValidator currency(Locale locale) {

		final NumberFormat currencyFormat = NumberFormat
				.getCurrencyInstance(locale);

		final NumberFormat format = NumberFormat.getNumberInstance(locale);

		return new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value == null) {
					return ValidationStatus.ok();
				}
				String input = value.toString();
				// Try to parse the string using the percent format
				ParsePosition pos = new ParsePosition(0);
				currencyFormat.parse(input, pos);
				if (pos.getIndex() >= input.length()) {
					return ValidationStatus.ok();
				}
				// Try to parse the string using a number format
				pos = new ParsePosition(0);
				format.parse(input, pos);
				if (pos.getIndex() >= input.length()) {
					return ValidationStatus.ok();
				}
				return ValidationStatus
						.error(getExampleErrorMessage(currencyFormat
								.format(0.123456f)));
			}
		};
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
	 * Return a validator to check the format of a string previous to a
	 * conversion of a percent string using default locale.
	 * 
	 * @return the validator
	 * @see Converters#percentToFloat(boolean)
	 */
	public static IValidator percent() {
		return percent(0, 0);
	}

	/**
	 * Return a validator checking if the value is null. When null this
	 * validator return an error.
	 * 
	 * @return the validator.
	 */
	public static IValidator notNull() {
		return new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value == null) {
					return ValidationStatus.error(BindingMessages
							.getString(VALIDATE_NOT_NULL));
				}
				return ValidationStatus.ok();
			}
		};
	}

	/**
	 * Return a validator to check the format of a string previous to a
	 * conversion of a percent string using default locale.
	 * 
	 * @param minDecimal
	 *            the minimum number of digits allowed in the fraction portion
	 *            of a number.
	 * @param maxDecimal
	 *            the maximum number of digits allowed in the fraction portion
	 *            of a number.
	 * @return the validator
	 * @see Converters#percentToFloat(boolean)
	 */
	public static IValidator percent(int minDecimal, int maxDecimal) {
		return percent(minDecimal, maxDecimal, Locale.getDefault());
	}

	/**
	 * Return a validator to check the format of a string previous to a
	 * conversion of a percent string using the given locale.
	 * 
	 * @param minDecimal
	 *            the minimum number of digits allowed in the fraction portion
	 *            of a number.
	 * @param maxDecimal
	 *            the maximum number of digits allowed in the fraction portion
	 *            of a number.
	 * @return the validator
	 * @see Converters#percentToFloat(boolean)
	 */
	public static IValidator percent(int minDecimal, int maxDecimal,
			Locale locale) {

		final NumberFormat percentFormat = NumberFormat
				.getPercentInstance(locale);
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
				return ValidationStatus
						.error(getExampleErrorMessage(percentFormat
								.format(0.123456f)));
			}
		};
	}

	/**
	 * Create a new validator to parse date from a string. This should be use in
	 * conjunction with stringToDateConverter.
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
				return ValidationStatus.error(getExampleErrorMessage(format
						.format(new Date())));
			}

		};
	}
}
