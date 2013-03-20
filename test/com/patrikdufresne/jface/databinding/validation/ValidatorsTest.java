package com.patrikdufresne.jface.databinding.validation;

import static org.junit.Assert.*;

import java.util.Locale;

import org.eclipse.core.databinding.validation.IValidator;
import org.junit.Test;

public class ValidatorsTest {

	@Test
	public void testPercentToFloat() {

		IValidator validator = Validators.percentToFloat(0, 3,
				Locale.CANADA_FRENCH);

		assertTrue(validator.validate("5 %").isOK());

		assertTrue(validator.validate("5,5 %").isOK());

		assertTrue(validator.validate("5").isOK());

		assertTrue(validator.validate("5,5").isOK());

		assertFalse(validator.validate("5 a").isOK());

		assertTrue(validator.validate(null).isOK());

	}

	@Test
	public void testCurrencyToFloat() {

		IValidator validator = Validators.currencyToFloat(Locale.CANADA_FRENCH);

		assertTrue(validator.validate("5 $").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5$").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5,5 $").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5,5$").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5.5 $").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5.5$").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5,5").isOK()); //$NON-NLS-1$

		assertFalse(validator.validate("5 a").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate(null).isOK());

	}

	@Test
	public void testStringToFloat() {

		IValidator validator = Validators.stringToFloat(Locale.CANADA_FRENCH);

		assertTrue(validator.validate("5").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5.6").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate("5,7").isOK()); //$NON-NLS-1$

		assertFalse(validator.validate("5 a").isOK()); //$NON-NLS-1$

		assertTrue(validator.validate(null).isOK());

	}

}
