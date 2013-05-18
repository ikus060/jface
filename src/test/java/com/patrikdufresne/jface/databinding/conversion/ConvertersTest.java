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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;
import org.junit.Test;

public class ConvertersTest {

    @Test
    public void testPercentToFloat() {
        IConverter converter = Converters.percentToFloat(true, 0, 3, Locale.CANADA_FRENCH);

        assertEquals(Float.valueOf(0.05f), converter.convert("5 %")); //$NON-NLS-1$

        assertEquals(Float.valueOf(0.055f), converter.convert("5,5 %")); //$NON-NLS-1$

        assertEquals(Float.valueOf(0.05f), converter.convert("5")); //$NON-NLS-1$

        assertEquals(Float.valueOf(0.055f), converter.convert("5,5")); //$NON-NLS-1$

    }

    @Test
    public void testPercentToBigDecimal() {
        IConverter converter = Converters.percentToBigDecimal(0, 3, Locale.CANADA_FRENCH);

        assertEquals(BigDecimal.valueOf(5, 2), converter.convert("5 %")); //$NON-NLS-1$

        assertEquals(BigDecimal.valueOf(55, 3), converter.convert("5,5 %")); //$NON-NLS-1$

        assertEquals(BigDecimal.valueOf(5, 2), converter.convert("5")); //$NON-NLS-1$

        assertEquals(BigDecimal.valueOf(55, 3), converter.convert("5,5")); //$NON-NLS-1$

    }

    @Test
    public void testFloatToPercent() {

        IConverter converter = Converters.floatToPercent(true, 0, 3, Locale.CANADA_FRENCH);

        assertEquals("5 %", converter.convert(Float.valueOf(0.05f))); //$NON-NLS-1$

        assertEquals("5,5 %", converter.convert(Float.valueOf(0.055f))); //$NON-NLS-1$

        assertEquals("5,123 %", converter.convert(Float.valueOf(0.0512345f))); //$NON-NLS-1$

    }

    @Test
    public void testFloatToCurrency() {

        IConverter converter = Converters.floatToCurrency(true, Locale.CANADA_FRENCH);

        assertEquals("12,05 $", converter.convert(Float.valueOf(12.05f))); //$NON-NLS-1$

        assertEquals("13,06 $", converter.convert(Float.valueOf(13.055f))); //$NON-NLS-1$

        assertEquals("18,05 $", converter.convert(Float.valueOf(18.0512345f))); //$NON-NLS-1$

    }

    @Test
    public void testCurrencyToFloat() {
        IConverter converter = Converters.currencyToFloat(true, Locale.CANADA_FRENCH);

        assertEquals(Float.valueOf(5f), converter.convert("5 $")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5f), converter.convert("5$")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5.5f), converter.convert("5,5 $")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5.5f), converter.convert("5,5$")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5.5f), converter.convert("5.5 $")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5.5f), converter.convert("5.5$")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5f), converter.convert("5")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5.5f), converter.convert("5,5")); //$NON-NLS-1$

        assertEquals(Float.valueOf(5.5f), converter.convert("5.5")); //$NON-NLS-1$

    }

    @Test
    public void testStringToFloat() {
        IConverter converter = Converters.stringToFloat(true, 0, 0, Locale.CANADA_FRENCH);

        assertEquals(Float.valueOf(7f), converter.convert("7")); //$NON-NLS-1$

        assertEquals(Float.valueOf(7.6f), converter.convert("7,6")); //$NON-NLS-1$

        assertEquals(Float.valueOf(7.7f), converter.convert("7.7")); //$NON-NLS-1$

        assertEquals(Float.valueOf(7.7f), converter.convert(" 7.7 ")); //$NON-NLS-1$

        assertEquals(Float.valueOf(0f), converter.convert("")); //$NON-NLS-1$

        assertEquals(Float.valueOf(0f), converter.convert(" ")); //$NON-NLS-1$

        // With not primitive.
        converter = Converters.stringToFloat(false, 0, 0, Locale.CANADA_FRENCH);

        assertEquals(null, converter.convert("")); //$NON-NLS-1$

        assertEquals(null, converter.convert(" ")); //$NON-NLS-1$

    }

}
