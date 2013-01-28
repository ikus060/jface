/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.SubToolBarManager;

/**
 * This implementation of {@link IToolBarProvider} allow creating sub tool bar
 * manager into an existing tool bar.
 * 
 * @author Patrik Dufresne
 * 
 */
public class SubToolBarProvider implements IToolBarProvider {

	private IToolBarProvider parent;

	private SubToolBarManager subtoolbar;

	/**
	 * Create a new sub tool bar provider.
	 * 
	 * @param parentthe
	 *            parent toolbar provider.
	 */
	public SubToolBarProvider(IToolBarProvider parent) {
		if (parent == null) {
			throw new IllegalArgumentException();
		}
		this.parent = parent;
	}

	/**
	 * This implementation return a sub cool bar manager.
	 */
	@Override
	public IToolBarManager getToolBarManager() {
		if (this.subtoolbar == null) {
			this.subtoolbar = new SubToolBarManager(
					this.parent.getToolBarManager());
			this.subtoolbar.setVisible(true);
		}
		return this.subtoolbar;
	}

}
