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

import org.junit.Test;

public class AcronymConverterTest {

    @Test
    public void testConvert() {

        AcronymConverter converter = AcronymConverter.getInstance();

        assertEquals("JS", converter.convert("Jours de semaine"));

        assertEquals("JFS", converter.convert("Jours de fin de semaine"));

        assertEquals("JFS", converter.convert("1. Jours de fin de semaine"));

        assertEquals("NSDJS", converter.convert("9. Nuit semaine Dim - Jeu - Salubrité"));

        assertEquals("JRFS", converter.convert("6. Jours remplacement fin de semaine"));

    }

}
