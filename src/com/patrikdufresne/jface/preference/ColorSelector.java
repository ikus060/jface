/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.preference;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * This implementation of ColorSelection is used to fix a problem involving
 * JFaceProperty.
 * 
 * @author Patrik Dufresne
 * 
 */
// TODO Should be move to a different package
public class ColorSelector extends EventManager {

	/**
	 * Property name that signifies the selected color of this
	 * <code>ColorSelector</code> has changed.
	 * 
	 * @since 3.0
	 */
	public static final String COLOR_VALUE = "colorValue"; //$NON-NLS-1$

	/**
	 * Property name that signified the selected custom color.
	 */
	public static final String COLOR_VALUES = "colorValues"; //$NON-NLS-1$

	private Button fButton;

	private Color fColor;

	private RGB fColorValue;

	private Point fExtent;

	private Image fImage;

	private RGB[] rgbs;

	/**
	 * Create a new instance of the reciever and the button that it wrappers in
	 * the supplied parent <code>Composite</code>.
	 * 
	 * @param parent
	 *            The parent of the button.
	 */
	public ColorSelector(Composite parent) {
		fButton = new Button(parent, SWT.PUSH);
		fExtent = computeImageSize(parent);
		fImage = new Image(parent.getDisplay(), fExtent.x, fExtent.y);
		GC gc = new GC(fImage);
		gc.setBackground(fButton.getBackground());
		gc.fillRectangle(0, 0, fExtent.x, fExtent.y);
		gc.dispose();
		fButton.setImage(fImage);
		fButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				open();
			}
		});
		fButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (fImage != null) {
					fImage.dispose();
					fImage = null;
				}
				if (fColor != null) {
					fColor.dispose();
					fColor = null;
				}
			}
		});
		fButton.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.accessibility.AccessibleAdapter#getName(org.eclipse
			 * .swt.accessibility.AccessibleEvent)
			 */
			public void getName(AccessibleEvent e) {
				e.result = JFaceResources.getString("ColorSelector.Name"); //$NON-NLS-1$
			}
		});
	}

	/**
	 * Adds a property change listener to this <code>ColorSelector</code>.
	 * Events are fired when the color in the control changes via the user
	 * clicking an selecting a new one in the color dialog. No event is fired in
	 * the case where <code>setColorValue(RGB)</code> is invoked.
	 * 
	 * @param listener
	 *            a property change listener
	 * @since 3.0
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	/**
	 * Compute the size of the image to be displayed.
	 * 
	 * @param window
	 *            - the window used to calculate
	 * @return <code>Point</code>
	 */
	private Point computeImageSize(Control window) {
		GC gc = new GC(window);
		Font f = JFaceResources.getFontRegistry().get(
				JFaceResources.DIALOG_FONT);
		gc.setFont(f);
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();
		Point p = new Point(height * 3 - 6, height);
		return p;
	}

	/**
	 * Get the button control being wrappered by the selector.
	 * 
	 * @return <code>Button</code>
	 */
	public Button getButton() {
		return fButton;
	}

	/**
	 * Return the currently displayed color.
	 * 
	 * @return <code>RGB</code>
	 */
	public RGB getColorValue() {
		return fColorValue;
	}

	/**
	 * Returns an array of <code>RGB</code>s which are the list of custom colors
	 * selected by the user.
	 * 
	 * @return the color or null if not set.
	 */
	public RGB[] getColorValues() {
		return rgbs;
	}

	/**
	 * Removes the given listener from this <code>ColorSelector</code>. Has no
	 * effect if the listener is not registered.
	 * 
	 * @param listener
	 *            a property change listener
	 * @since 3.0
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		removeListenerObject(listener);
	}

	/**
	 * Set the current color value and update the control.
	 * 
	 * @param rgb
	 *            The new color.
	 */
	public void setColorValue(RGB rgb) {
		fColorValue = rgb;
		updateColorImage();
	}

	/**
	 * Sets the receiver's list of custom colors.
	 * 
	 * @param rgbs
	 *            the colors or null to unset.
	 */
	public void setColorValues(RGB[] rgbs) {
		firePropertyChange(COLOR_VALUES, this.rgbs, this.rgbs = rgbs);
	}

	/**
	 * Fire a property change event.
	 * 
	 * @param property
	 *            the property name
	 * @param oldValue
	 *            the new value
	 * @param newValue
	 *            the old value
	 */
	protected void firePropertyChange(String property, Object oldValue,
			Object newValue) {
		final Object[] finalListeners = getListeners();
		if (finalListeners.length > 0) {
			PropertyChangeEvent pEvent = new PropertyChangeEvent(this,
					property, oldValue, newValue);
			for (int i = 0; i < finalListeners.length; ++i) {
				IPropertyChangeListener listener = (IPropertyChangeListener) finalListeners[i];
				listener.propertyChange(pEvent);
			}
		}
	}

	/**
	 * Set whether or not the button is enabled.
	 * 
	 * @param state
	 *            the enabled state.
	 */
	public void setEnabled(boolean state) {
		getButton().setEnabled(state);
	}

	/**
	 * Update the image being displayed on the button using the current color
	 * setting.
	 */
	protected void updateColorImage() {
		Display display = fButton.getDisplay();
		GC gc = new GC(fImage);
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);
		if (fColor != null) {
			fColor.dispose();
			fColor = null;
		}
		gc.setBackground(fColorValue != null ? (fColor = new Color(display,
				fColorValue)) : fButton.getBackground());
		gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);
		gc.dispose();
		fButton.setImage(fImage);
	}

	/**
	 * Activate the editor for this selector. This causes the color selection
	 * dialog to appear and wait for user input.
	 * 
	 * @since 3.2
	 */
	public void open() {
		ColorDialog colorDialog = new ColorDialog(fButton.getShell());
		colorDialog.setRGB(fColorValue);
		colorDialog.setRGBs(rgbs);
		RGB newColor = colorDialog.open();
		if (newColor != null) {
			RGB oldValue = fColorValue;
			fColorValue = newColor;
			firePropertyChange(COLOR_VALUE, oldValue, newColor);
			updateColorImage();
			// TODO Fire a property change event
			this.rgbs = colorDialog.getRGBs();
		}
	}

}
