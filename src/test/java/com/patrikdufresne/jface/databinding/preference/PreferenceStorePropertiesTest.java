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
package com.patrikdufresne.jface.databinding.preference;

import static org.junit.Assert.*;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.patrikdufresne.jface.databinding.DatabindingClassRunner;

/**
 * Test the utility class {@link PreferenceStoreProperties}.
 * 
 * @author Patrik Dufresne
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class PreferenceStorePropertiesTest {

    private static final String TEST_PROPERTY = "testValue";

    int eventCount;

    @Before
    public void resetEventCount() {
        this.eventCount = 0;
    }

    /**
     * Check if a string value property is working as expected.
     */
    @Test
    public void value() {

        PreferenceStore store = new PreferenceStore();
        store.setValue(TEST_PROPERTY, "value1");

        IValueProperty property = PreferenceStoreProperties.value(TEST_PROPERTY, String.class);
        assertNotNull(property);

        IObservableValue value = property.observe(store);
        assertNotNull(value);

        value.addValueChangeListener(new IValueChangeListener() {
            @Override
            public void handleValueChange(ValueChangeEvent event) {
                eventCount++;
            }
        });

        assertEquals("value1", value.getValue());

        value.setValue("value1");
        assertEquals(0, eventCount);

        // Test from obervable to store
        value.setValue("value2");
        assertEquals(1, eventCount);
        assertEquals("value2", store.getString(TEST_PROPERTY));
        this.eventCount = 0;

        // Test from store to observable
        store.setValue(TEST_PROPERTY, "value3");
        assertEquals(1, eventCount);
        assertEquals("value3", value.getValue());

    }
}
