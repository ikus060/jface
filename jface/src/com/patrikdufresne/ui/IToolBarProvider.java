/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.jface.action.IToolBarManager;

/**
 * Used to deferred the creation of a toolbar for a view.
 * 
 * @author Patrik Dufresne
 * 
 */
public interface IToolBarProvider {

	/**
	 * Return a tool bar manager.
	 * <p>
	 * Return or create a tool bar manager contributing to the cool bar manager.
	 * 
	 * @return
	 */
	IToolBarManager getToolBarManager();

}
