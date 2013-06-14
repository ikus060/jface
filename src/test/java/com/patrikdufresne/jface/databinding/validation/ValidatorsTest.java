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

import static org.junit.Assert.*;

import java.util.Locale;

import org.eclipse.core.databinding.validation.IValidator;
import org.junit.Test;

public class ValidatorsTest {

    @Test
    public void testPercentToFloat() {

        IValidator validator = Validators.percentToFloat(0, 3, Locale.CANADA_FRENCH);

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

        assertTrue(validator.validate(" 5 ").isOK()); //$NON-NLS-1$

        assertTrue(validator.validate("18.505").isOK()); //$NON-NLS-1$

        assertTrue(validator.validate("5,7").isOK()); //$NON-NLS-1$

        assertFalse(validator.validate("5 a").isOK()); //$NON-NLS-1$

        assertTrue(validator.validate("").isOK()); //$NON-NLS-1$

        assertTrue(validator.validate(" ").isOK()); //$NON-NLS-1$

        assertTrue(validator.validate("  ").isOK()); //$NON-NLS-1$

        assertTrue(validator.validate(null).isOK());

    }

}
