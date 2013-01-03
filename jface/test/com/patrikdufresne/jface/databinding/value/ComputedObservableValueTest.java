package com.patrikdufresne.jface.databinding.value;

import static org.junit.Assert.*;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.patrikdufresne.jface.databinding.DatabindingClassRunner;

/**
 * Test the ComputedObservableValue class.
 * 
 * @author Patrik Dufresne
 * 
 */
@RunWith(DatabindingClassRunner.class)
public class ComputedObservableValueTest {

	int eventCount;

	@Before
	public void resetEventCount() {
		this.eventCount = 0;
	}

	@Test
	public void test() {

		final IObservableValue dep1 = new WritableValue("a", String.class);
		final IObservableValue dep2 = new WritableValue("b", String.class);

		IObservableValue value = new ComputedObservableValue(dep1, dep2) {
			@Override
			protected Object calculate() {
				StringBuilder buf = new StringBuilder();
				if (dep1.getValue() != null) {
					buf.append(dep1.getValue());
				}
				if (dep2.getValue() != null) {
					buf.append(dep2.getValue());
				}
				return buf.toString();
			}
		};

		// Check value
		assertEquals("ab", value.getValue());

		// Add listener
		value.addValueChangeListener(new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				eventCount++;
			}
		});

		// Update dependencies
		dep1.setValue("c");
		// Check value
		assertEquals("cb", value.getValue());
		assertEquals(1, this.eventCount);
		this.eventCount = 0;

		// Update dependencies
		dep2.setValue("d");
		// Check value
		assertEquals("cd", value.getValue());
		assertEquals(1, this.eventCount);
		this.eventCount = 0;

	}

}
