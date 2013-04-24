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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * This implementation of {@link ViewerFilter} is filtering the elements using regular expression pattern matching with
 * a combination of delimiters.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PatternsFilter extends ViewerFilter implements IFilter {

    private static final String OR_DELIMITER = ",|";

    private static final String AND_DELIMITER = " +&";

    private static final String NOT_DELIMITER = "-";

    /**
     * Create a new filter using the given string to build the patterns. The string should be a list of patterns
     * Separated by space.
     * 
     * @param patterns
     *            a separated list of pattern
     * @return the filter.
     */
    public static PatternsFilter create(String patterns) {
        return new PatternsFilter(createPatterns(patterns));
    }

    public static List<List<Pattern>> createPatterns(String patterns) {
        return createPatterns(patterns, OR_DELIMITER, AND_DELIMITER, NOT_DELIMITER);
    }

    /**
     * Create patterns from the given string.
     * 
     * The and operators as precedence on the or operators.
     * 
     * @param patterns
     * @param andDelimiters
     * @param orDelimiters
     * @param notDelimiters
     * @return
     */
    public static List<List<Pattern>> createPatterns(String patterns, String orDelimiters, String andDelimiters, String notDelimiters) {
        // Create a regex pattern for filtering
        List<List<Pattern>> regexPatterns = new ArrayList<List<Pattern>>();
        StringTokenizer stor = new StringTokenizer(patterns, orDelimiters, false);
        while (stor.hasMoreTokens()) {
            List<Pattern> list = new ArrayList<Pattern>();
            StringTokenizer stand = new StringTokenizer(stor.nextToken(), andDelimiters, false);
            while (stand.hasMoreTokens()) {
                String pattern = stand.nextToken();
                pattern = normalizeString(pattern);
                try {
                    list.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
                } catch (PatternSyntaxException e) {
                    list.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.LITERAL));
                }
            }
            if (list.size() > 0) {
                regexPatterns.add(list);
            }
        }
        return regexPatterns;
    }

    /**
     * Remove weird character.
     * 
     * @param string
     *            the input string
     * @return the string without weird character
     */
    protected static String normalizeString(String string) {
        String temp = Normalizer.normalize(string, Normalizer.Form.NFD);
        return temp.replaceAll("[^\\p{ASCII}]", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private final List<List<Pattern>> patterns;

    /**
     * Create a new filter using the given list of patterns.
     * 
     * @param patterns
     *            the patterns.
     */
    public PatternsFilter(List<List<Pattern>> patterns) {
        this.patterns = new ArrayList<List<Pattern>>();
        for (Collection<Pattern> c : patterns) {
            this.patterns.add(new ArrayList<Pattern>(c));
        }

    }

    /**
     * Return true if the given object matches against one or more of the patterns.
     */
    @Override
    public boolean select(Object toTest) {
        if (toTest == null) {
            return false;
        }
        // Normalize the input string.
        String value = normalizeString(toTest.toString());

        Iterator<List<Pattern>> iter = patterns.iterator();
        // Or loop
        while (iter.hasNext()) {
            List<Pattern> list = iter.next();
            int i = 0;
            while (i < list.size() && list.get(i).matcher(value).find()) {
                i++;
            }
            if (i >= list.size()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This implementation of {@link ViewerFilter#select(Viewer, Object, Object)} is filtering the element againts the
     * patterns.
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        return select(element);
    }
}
