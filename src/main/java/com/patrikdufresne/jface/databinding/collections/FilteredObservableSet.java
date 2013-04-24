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
package com.patrikdufresne.jface.databinding.collections;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.StaleEvent;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.internal.databinding.ConverterValueProperty;
import org.eclipse.jface.viewers.IFilter;

import com.patrikdufresne.jface.databinding.value.ComputedObservableValue;
import com.patrikdufresne.jface.filter.PatternsFilter;

/**
 * Filter an observable set using a text filter.
 * 
 * @author Patrik Dufresne
 * 
 */
@SuppressWarnings("rawtypes")
public class FilteredObservableSet extends AbstractFilteredObservableSet {
    /**
     * Private listener to avoid exposing interfaces.
     * 
     * @author Patrik Dufresne
     * 
     */
    private class PrivateInterface implements IChangeListener, IStaleListener {
        public PrivateInterface() {
            // Nothing to do
        }

        /**
         * Notify this class about the pattern being changed.
         */
        @Override
        public void handleChange(ChangeEvent event) {
            patternChange();
        }

        @Override
        public void handleStale(StaleEvent staleEvent) {
            if (!dirty) makeStale();
        }
    }

    /**
     * Create an observable from the patterns and property.
     * 
     * @param patterns
     *            the
     * 
     * @param property
     * 
     * @return
     */
    private static IObservableValue createObservableFilter(final IObservableValue patterns, final IValueProperty property) {
        return new ComputedObservableValue(patterns) {
            @Override
            protected Object calculate() {
                // Null filter if the patterns is empty.
                if (!(patterns.getValue() instanceof String) || patterns.getValue().toString().trim().isEmpty()) {
                    return null;
                }
                // Create the patterns filter.
                List<List<Pattern>> list = PatternsFilter.createPatterns(((IObservableValue) patterns).getValue().toString());
                if (property == null) {
                    return new PatternsFilter(list);
                }
                return new PatternsFilter(list) {
                    @Override
                    public boolean select(Object toTest) {
                        // Convert the original object using the given value property.
                        return super.select(property.getValue(toTest));
                    }
                };

            }
        };
    }

    /**
     * Observable filter.
     */
    private IObservableValue filter;

    /**
     * Private listener
     */
    private PrivateInterface privateInterface = new PrivateInterface();

    /**
     * Create an observable filter for the set specified.
     * 
     * @param set
     *            the observable set to filter
     * @param filter
     *            an observable on the filter to be used.
     */
    protected FilteredObservableSet(IObservableSet set, IObservableValue filter) {
        super(set);
        if (filter == null) {
            throw new IllegalArgumentException();
        }
        this.filter = filter;
    }

    /**
     * Create a new patterns filter observable set.
     * 
     * @param set
     *            the observable to be filtered
     * @param patterns
     *            the patterns (an observable string)
     * @param converter
     *            a converter to transforme the original element into the string to be filtered.
     */
    public FilteredObservableSet(IObservableSet set, IObservableValue patterns, IConverter converter) {
        this(set, createObservableFilter(patterns, new ConverterValueProperty(converter)));
    }

    /**
     * Create a new patterns filter observable set.
     * 
     * @param set
     *            the observable to be filtered
     * @param patterns
     *            the patterns (an observable string)
     * @param property
     *            the property value used for filtering.
     */
    public FilteredObservableSet(IObservableSet set, IObservableValue patterns, IValueProperty property) {
        this(set, createObservableFilter(patterns, property));
    }

    /**
     * This implementation remove listener.
     */
    @Override
    public synchronized void dispose() {
        super.dispose();
        this.filter = null;
    }

    /**
     * Query the database.
     * <p>
     * Sub classes may override this function to query the database using something else then list().
     * 
     * @return a collection
     */
    @Override
    protected Iterator doCompute() {
        // Return the current set iterator
        if (getFilter() == null) {
            return getInnerSet().iterator();
        }
        return super.doCompute();
    }

    /**
     * Check if an elements matches the provided patterns.
     * <p>
     * Sub-classes
     * 
     * @param element
     *            the element
     * @return True if the pattern matches.
     */
    @Override
    protected boolean doSelect(Object element) {
        return ((IFilter) this.filter.getValue()).select(element);
    }

    /**
     * Return the current pattern value or null if not set
     * 
     * @return
     */
    protected IFilter getFilter() {
        if (this.filter.getValue() instanceof IFilter) {
            return (IFilter) this.filter.getValue();
        }
        return null;
    }

    public void patternChange() {
        makeDirty();
    }

    /**
     * 
     * Add listener to dependencies
     */
    @Override
    protected void startListening() {
        super.startListening();
        if (this.filter != null) {
            this.filter.addChangeListener(this.privateInterface);
            this.filter.addStaleListener(this.privateInterface);
        }
    }

    /**
     * Remove listener from dependencies.
     */
    @Override
    protected void stopListening() {
        super.stopListening();
        if (this.filter != null) {
            this.filter.removeChangeListener(this.privateInterface);
            this.filter.addStaleListener(this.privateInterface);
        }
    }
}
