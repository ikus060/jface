package com.patrikdufresne.jface.databinding.validation;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.internal.databinding.BindingMessages;
import org.eclipse.core.runtime.IStatus;

public class Validators {

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
				return ValidationStatus.error(getErrorMessage());
			}

			/**
			 * Create an examples string using the format.
			 * 
			 * @return
			 */
			public String getErrorMessage() {
				Date date = new Date();
				StringBuilder buf = new StringBuilder();
				buf.append(BindingMessages.getString(BindingMessages.EXAMPLES));
				buf.append(" "); //$NON-NLS-1$
				buf.append(format.format(date));
				return buf.toString();
			}
		};

	}
}
