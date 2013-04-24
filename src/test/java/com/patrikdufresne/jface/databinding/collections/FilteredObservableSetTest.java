package com.patrikdufresne.jface.databinding.collections;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.internal.databinding.conversion.ObjectToStringConverter;
import org.eclipse.core.internal.databinding.property.value.SelfValueProperty;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.patrikdufresne.jface.databinding.DatabindingClassRunner;

@RunWith(DatabindingClassRunner.class)
public class FilteredObservableSetTest {

    /**
     * Check if the filtering is working as expected.
     */
    @Test
    public void testFiltering() {
        // Create the filter observable set
        WritableSet set = new WritableSet(new HashSet<String>(), String.class);
        set.add("Patrik Dufresne");
        set.add("Nicole Daoust");
        set.add("Michel Roy");
        set.add("Luc Fortin");
        WritableValue patterns = new WritableValue("Pat, For", String.class);
        FilteredObservableSet filter = new FilteredObservableSet(set, patterns, new SelfValueProperty(String.class));

        assertEquals(2, filter.size());

        HashSet<String> filteredSet = new HashSet<String>(filter);
        assertTrue(filteredSet.contains("Patrik Dufresne"));
        assertTrue(filteredSet.contains("Luc Fortin"));

    }

    /**
     * Check if the filtering is working as expected.
     */
    @Test
    public void testFiltering_WithEmptyPatterns() {
        // Create the filter observable set
        WritableSet set = new WritableSet(new HashSet<String>(), String.class);
        set.add("Patrik Dufresne");
        set.add("Nicole Daoust");
        set.add("Michel Roy");
        set.add("Luc Fortin");
        WritableValue patterns = new WritableValue(" ", String.class);
        FilteredObservableSet filter = new FilteredObservableSet(set, patterns, new SelfValueProperty(String.class));

        assertEquals(4, filter.size());

        HashSet<String> filteredSet = new HashSet<String>(filter);
        assertTrue(filteredSet.contains("Patrik Dufresne"));
        assertTrue(filteredSet.contains("Nicole Daoust"));
        assertTrue(filteredSet.contains("Michel Roy"));
        assertTrue(filteredSet.contains("Luc Fortin"));
    }

    /**
     * Check if the filtering is working as expected.
     */
    @Test
    public void testFiltering_WithPatternsChange() {
        // Create the filter observable set
        WritableSet set = new WritableSet(new HashSet<String>(), String.class);
        set.add("Patrik Dufresne");
        set.add("Nicole Daoust");
        set.add("Michel Roy");
        set.add("Luc Fortin");
        WritableValue patterns = new WritableValue("Pat, For", String.class);
        FilteredObservableSet filter = new FilteredObservableSet(set, patterns, new SelfValueProperty(String.class));

        // Check data
        assertEquals(2, filter.size());
        HashSet<String> filteredSet = new HashSet<String>(filter);
        assertTrue(filteredSet.contains("Patrik Dufresne"));
        assertTrue(filteredSet.contains("Luc Fortin"));

        // Change patterns
        patterns.setValue("Ni, ro");

        // Check data
        assertEquals(2, filter.size());
        filteredSet = new HashSet<String>(filter);
        assertTrue(filteredSet.contains("Nicole Daoust"));
        assertTrue(filteredSet.contains("Michel Roy"));

    }

    /**
     * Check if the filtering is working as expected.
     */
    @Test
    public void testFiltering_WithConverter() {
        // Create the filter observable set
        WritableSet set = new WritableSet(new HashSet<String>(), String.class);
        set.add("Patrik Dufresne");
        set.add("Nicole Daoust");
        set.add("Michel Roy");
        set.add("Luc Fortin");
        WritableValue patterns = new WritableValue("Pat, For", String.class);
        FilteredObservableSet filter = new FilteredObservableSet(set, patterns, new ObjectToStringConverter());

        // Check data
        assertEquals(2, filter.size());
        HashSet<String> filteredSet = new HashSet<String>(filter);
        assertTrue(filteredSet.contains("Patrik Dufresne"));
        assertTrue(filteredSet.contains("Luc Fortin"));

    }

}
