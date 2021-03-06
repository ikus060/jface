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
package com.patrikdufresne.jface.databinding.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.action.Action;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.patrikdufresne.jface.databinding.DatabindingClassRunner;

@RunWith(DatabindingClassRunner.class)
public class JFacePropertiesTest {

    static class MockAction extends Action {

        public static final String LIST = "list";

        public static final String ITEMS = "items";

        public static final String COLLECTION = "collection";

        private String[] items;

        public String[] getItems() {
            return items;
        }

        public void setItems(String[] items) {
            String[] old = this.items;
            firePropertyChange(ITEMS, old, this.items = Arrays.copyOf(items, items.length));
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            List<String> old = this.list;
            firePropertyChange(ITEMS, old, this.list = list);
        }

        public Collection<String> getCollection() {
            return collection;
        }

        public void setCollection(Collection<String> collection) {
            this.collection = collection;
        }

        List<String> list = null;

        Collection<String> collection = null;

    }

    @Test
    public void testList() {

        // Bind the action property list to a model
        MockAction action = new MockAction();

        IObservableList list = JFaceProperties.list(MockAction.class, MockAction.LIST, MockAction.LIST).observe(action);
        assertNotNull(list);
        assertEquals(null, action.getList());

        // Check when adding item
        list.add("a");
        assertEquals(1, action.getList().size());
        assertTrue(action.getList().contains("a"));

        // Check when adding items
        list.addAll(Arrays.asList("b", "c"));
        assertEquals(3, action.getList().size());
        assertTrue(action.getList().contains("a"));
        assertTrue(action.getList().contains("b"));
        assertTrue(action.getList().contains("c"));

        // Check when remove item
        list.remove("b");
        assertEquals(2, action.getList().size());
        assertTrue(action.getList().contains("a"));
        assertTrue(action.getList().contains("c"));

        // Check when remove items
        list.clear();
        assertEquals(0, action.getList().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testList_withWrongPropertyReturnType() {

        // Bind the action property list to a model
        MockAction action = new MockAction();

        IObservableList list = JFaceProperties.list(MockAction.class, MockAction.COLLECTION, MockAction.COLLECTION).observe(action);

    }

    @Test
    public void testList_withBinding() {

        DataBindingContext dbc = new DataBindingContext();

        // Bind the action property list to a model
        MockAction action = new MockAction();

        WritableList list = new WritableList(new ArrayList<String>(), String.class);
        dbc.bindList(JFaceProperties.list(MockAction.class, MockAction.LIST, MockAction.LIST).observe(action), list);

        list.addAll(Arrays.asList("a", "b"));

        assertEquals(2, action.getList().size());
        assertTrue(action.getList().contains("a"));
        assertTrue(action.getList().contains("b"));
    }

    @Test
    public void testArray_withBinding() {

        DataBindingContext dbc = new DataBindingContext();

        // Bind the action property list to a model
        MockAction action = new MockAction();

        WritableList list = new WritableList(new ArrayList<String>(), String.class);
        dbc.bindList(JFaceProperties.list(MockAction.class, MockAction.ITEMS, MockAction.ITEMS).observe(action), list);

        list.addAll(Arrays.asList("a", "b"));

        assertEquals(2, action.getItems().length);
        assertTrue(Arrays.asList(action.getItems()).contains("a"));
        assertTrue(Arrays.asList(action.getItems()).contains("b"));
    }

}
