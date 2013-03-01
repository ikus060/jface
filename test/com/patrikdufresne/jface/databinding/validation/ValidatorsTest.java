package com.patrikdufresne.jface.databinding.validation;

import static org.junit.Assert.*;

import java.util.Locale;

import org.eclipse.core.databinding.validation.IValidator;
import org.junit.Test;

public class ValidatorsTest {

	@Test
	public void testPercentIntInt() {

		IValidator validator = Validators.percent(0, 3, Locale.CANADA_FRENCH);

		assertTrue(validator.validate("5 %").isOK());

		assertTrue(validator.validate("5,5 %").isOK());

		assertTrue(validator.validate("5").isOK());

		assertTrue(validator.validate("5,5").isOK());

		assertFalse(validator.validate("5 a").isOK());

		assertTrue(validator.validate(null).isOK());

	}

	@Test
	public void testCurrency() {

		IValidator validator = Validators.currency(Locale.CANADA_FRENCH);

		assertTrue(validator.validate("5 $").isOK());

		assertTrue(validator.validate("5,5 $").isOK());

		assertTrue(validator.validate("5").isOK());

		assertTrue(validator.validate("5,5").isOK());

		assertFalse(validator.validate("5 a").isOK());

		assertTrue(validator.validate(null).isOK());

	}

}
