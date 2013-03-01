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

}
