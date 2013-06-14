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
package com.patrikdufresne.jface.databinding.viewers;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.internal.databinding.property.value.SelfValueProperty;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * This viewer comparator doesn't based it's comparison on the element string
 * representation or label. It's compare the element it self. When a comparator
 * is provided in the constructor, the comparator should support the element
 * type.
 * 
 * @author Patrik Dufresne
 * 
 */
@SuppressWarnings("rawtypes")
public class PropertyViewerComparator extends ViewerComparator {

    /**
     * Comparator to used or null.
     */
    private Comparator comparator;

    /**
     * Create a new viewer comparator with a self value property and the given
     * comparator.
     * 
     * @param comparator
     *            the comparator.
     */
    public PropertyViewerComparator(Comparator comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException();
        }
        this.comparator = comparator;
    }

    /**
     * Create a new viewer comparator with a natural order comparator and the
     * given value property.
     * 
     * @param property
     */
    public PropertyViewerComparator(IValueProperty property) {
        this(property, null);
    }

    /**
     * Create a new viewer comparator based on a comparator.
     * 
     * @param comparator
     */
    public PropertyViewerComparator(final IValueProperty property, final Comparator comparator) {
        this(new Comparator() {
            final Comparator finalComparator = comparator == null ? ColumnSupport.naturalComparator() : comparator;

            @Override
            public int compare(Object o1, Object o2) {
                return finalComparator.compare(property.getValue(o1), property.getValue(o2));
            }
        });
    }

    /**
     * This implementation used the given comparator.
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        int cat1 = category(e1);
        int cat2 = category(e2);
        if (cat1 != cat2) {
            return cat1 - cat2;
        }
        return this.comparator.compare(e1, e2);
    }

    /**
     * This implementation used the given comparator directly.
     */
    @Override
    public void sort(final Viewer viewer, Object[] elements) {
        Arrays.sort(elements, this.comparator);
    }

}
