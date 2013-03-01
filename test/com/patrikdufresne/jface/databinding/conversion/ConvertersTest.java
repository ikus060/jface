package com.patrikdufresne.jface.databinding.conversion;

import static org.junit.Assert.*;

import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;
import org.junit.Test;

public class ConvertersTest {

	@Test
	public void testPercentToFloat() {
		IConverter converter = Converters.percentToFloat(true, 0, 3,
				Locale.CANADA_FRENCH);

		assertEquals(Float.valueOf(0.05f), converter.convert("5 %")); //$NON-NLS-1$

		assertEquals(Float.valueOf(0.055f), converter.convert("5,5 %")); //$NON-NLS-1$

		assertEquals(Float.valueOf(0.05f), converter.convert("5")); //$NON-NLS-1$

		assertEquals(Float.valueOf(0.055f), converter.convert("5,5")); //$NON-NLS-1$

	}

	@Test
	public void testFloatToPercent() {

		IConverter converter = Converters.floatToPercent(true, 0, 3,
				Locale.CANADA_FRENCH);

		assertEquals("5 %", converter.convert(Float.valueOf(0.05f))); //$NON-NLS-1$

		assertEquals("5,5 %", converter.convert(Float.valueOf(0.055f))); //$NON-NLS-1$

		assertEquals("5,123 %", converter.convert(Float.valueOf(0.0512345f))); //$NON-NLS-1$

	}

	@Test
	public void testFloatToCurrency() {

		IConverter converter = Converters.floatToCurrency(true,
				Locale.CANADA_FRENCH);

		assertEquals("12,05 $", converter.convert(Float.valueOf(12.05f))); //$NON-NLS-1$

		assertEquals("13,06 $", converter.convert(Float.valueOf(13.055f))); //$NON-NLS-1$

		assertEquals("18,05 $", converter.convert(Float.valueOf(18.0512345f))); //$NON-NLS-1$

	}

	@Test
	public void testCurrencyToFloat() {
		IConverter converter = Converters.currencyToFloat(true,
				Locale.CANADA_FRENCH);

		assertEquals(Float.valueOf(5f), converter.convert("5 $")); //$NON-NLS-1$

		assertEquals(Float.valueOf(5.5f), converter.convert("5,5 $")); //$NON-NLS-1$

		assertEquals(Float.valueOf(5f), converter.convert("5")); //$NON-NLS-1$

		assertEquals(Float.valueOf(5.5f), converter.convert("5,5")); //$NON-NLS-1$

	}

}
