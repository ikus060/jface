/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding;

import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ControlStyleSupport {

	/**
	 * Class implementing interface we don't want to expose publicly.
	 * 
	 * @author Patrik Dufresne
	 * 
	 */
	private class PrivateInterface implements IValueChangeListener,
			IDisposeListener, Listener {

		PrivateInterface() {
			// Nothing to do
		}

		@Override
		public void handleDispose(DisposeEvent event) {
			ControlStyleSupport.this.dispose();
		}

		@Override
		public void handleEvent(Event event) {
			if (event.type == SWT.Dispose) {
				ControlStyleSupport.this.dispose();
			}
		}

		@Override
		public void handleValueChange(ValueChangeEvent event) {
			handleChange();
		}

	}

	public static ControlStyleSupport create(
			Control control, IObservableValue text, ControlStyleUpdater updater) {
		return new ControlStyleSupport(control, text, updater);
	}

	private Control control;

	private IObservableValue observableValue;

	private PrivateInterface privateInterface = new PrivateInterface();

	private ControlStyleUpdater updater;

	/**
	 * Create a new control style support.
	 * 
	 * @param dbc
	 * @param control
	 * @param value
	 * @param updater
	 */
	protected ControlStyleSupport(Control control,
			IObservableValue value, ControlStyleUpdater updater) {
		if (control == null || value == null || updater == null) {
			throw new NullPointerException();
		}
		this.updater = updater;
		this.control = control;
		this.observableValue = value;
		
		// Add dispose listener on the control
		control.addListener(SWT.Dispose, this.privateInterface);

		// Add dispose listener on the observable value.
		value.addDisposeListener(this.privateInterface);
		value.addValueChangeListener(this.privateInterface);

	}

	/**
	 * Dispose this class.
	 */
	public void dispose() {
		if (this.observableValue != null) {
			this.observableValue
					.removeValueChangeListener(this.privateInterface);
		}
		this.observableValue = null;
		if (this.control != null) {
			this.control.removeListener(SWT.Dispose, this.privateInterface);
		}
		this.control = null;
	}

	protected void handleChange() {
		this.updater.update(this.control, this.observableValue.getValue());
	}
}
