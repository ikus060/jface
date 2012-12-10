/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding;

import org.eclipse.swt.widgets.Control;

/**
 * This interface is used to update the style of the control according to a
 * given status.
 * 
 * @author Patrik Dufresne
 * 
 */
public interface ControlStyleUpdater {

	public void update(Control control, Object status);

}
