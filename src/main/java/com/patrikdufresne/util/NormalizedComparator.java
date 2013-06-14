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
package com.patrikdufresne.util;

import java.text.Normalizer;
import java.util.Comparator;

/**
 * Comparator used to compare string without accent. i.e.: e == é == è
 * 
 * @author Patrik Dufresne
 * 
 */
public class NormalizedComparator implements Comparator<String> {

    private static final String EMPTY = ""; //$NON-NLS-1$

    private static final String PATTERN = "[^\\p{ASCII}]"; //$NON-NLS-1$

    @Override
    public int compare(String s1, String s2) {
        return Normalizer.normalize(s1, Normalizer.Form.NFD).replaceAll(PATTERN, EMPTY).toLowerCase().compareTo(
                Normalizer.normalize(s2, Normalizer.Form.NFD).replaceAll(PATTERN, EMPTY).toLowerCase());
    }
}
