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
package com.patrikdufresne.jface.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class PatternsFilterTest {

    /**
     * Check the select function.
     */
    @Test
    public void testSelect() {

        PatternsFilter filter = PatternsFilter.create("This, test, pat duf");

        // Exact matches
        assertTrue(filter.select("This"));
        assertFalse(filter.select("is"));
        assertFalse(filter.select("a"));
        assertTrue(filter.select("test"));

        // Matches on one work
        assertFalse(filter.select("Patrik"));
        assertFalse(filter.select("Dufresne"));
        assertTrue(filter.select("Patrik Dufresne"));
        assertTrue(filter.select("Testing"));

        // Matches on multiple word
        assertTrue(filter.select("My super testing"));
        assertTrue(filter.select("My super Testing"));

        // Matche with unnormalized word
        assertTrue(filter.select("something tést something "));
        assertTrue(filter.select("something tèst something "));
        assertTrue(filter.select("something têst something "));
        assertTrue(filter.select("something tëst something "));

        // Unmatches
        assertFalse(filter.select("something"));

        // With null
        assertFalse(filter.select(null));

    }

}
