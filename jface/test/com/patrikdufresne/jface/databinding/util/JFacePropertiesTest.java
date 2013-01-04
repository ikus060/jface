package com.patrikdufresne.jface.databinding.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.action.Action;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.patrikdufresne.jface.databinding.DatabindingClassRunner;

@RunWith(DatabindingClassRunner.class)
public class JFacePropertiesTest {

	static class MockAction extends Action {

		public static final String LIST = "list";

		public static final String COLLECTION = "collection";

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
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

		IObservableList list = JFaceProperties.list(MockAction.class,
				MockAction.LIST, MockAction.LIST).observe(action);
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

		IObservableList list = JFaceProperties.list(MockAction.class,
				MockAction.COLLECTION, MockAction.COLLECTION).observe(action);

	}
}
