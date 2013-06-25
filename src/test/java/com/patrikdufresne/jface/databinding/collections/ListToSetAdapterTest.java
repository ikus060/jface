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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.patrikdufresne.jface.databinding.DatabindingClassRunner;

@RunWith(DatabindingClassRunner.class)
public class ListToSetAdapterTest {

    @Test
    public void test() {
        // Create the observables
        WritableList list = new WritableList(new ArrayList(Arrays.asList("item1", "item2", "item3", "item4")), String.class);
        IObservableSet set = new ListToSetAdapter(list);

        // Check if synch.
        assertEquals(4, set.size());
        assertTrue(set.containsAll(Arrays.asList("item1", "item2", "item3", "item4")));

        // Remove item from list
        list.remove("item2");
        assertEquals(3, set.size());
        assertTrue(set.containsAll(Arrays.asList("item1", "item3", "item4")));

        // Remove item from set
        set.remove("item3");
        assertEquals(2, set.size());
        assertEquals(2, list.size());
        assertTrue(list.containsAll(Arrays.asList("item1", "item4")));
        assertTrue(set.containsAll(Arrays.asList("item1", "item4")));

        // Remove item from set
        set.clear();
        assertEquals(0, set.size());
        assertEquals(0, list.size());

        // Add item
        list.add("item5");
        assertEquals(1, set.size());
        assertEquals(1, list.size());
        assertTrue(list.contains("item5"));
        assertTrue(set.contains("item5"));

        // Add item
        set.add("item6");
        assertEquals(2, set.size());
        assertEquals(2, list.size());
        assertTrue(list.containsAll(Arrays.asList("item5", "item6")));
        assertTrue(set.containsAll(Arrays.asList("item5", "item6")));

    }
}
