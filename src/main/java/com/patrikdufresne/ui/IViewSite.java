/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.window.IShellProvider;

/**
 * A view site is the primary interface for the view part to interact with the
 * application components. For each view part instance there are a view site.
 * 
 * @author Patrik Dufresne
 * 
 */
public interface IViewSite extends IShellProvider, IToolBarProvider {

	/**
	 * Dispose resource allocated by this view site.
	 */
	public void dispose();

	/**
	 * Returns the main window.
	 * 
	 * @return
	 */
	MainWindow getMainWindow();
	

	/**
	 * Return the associated view part.
	 * 
	 * @return the view part
	 */
	IViewPart getPart();

	/**
	 * Used to retrieve a service for this view site.
	 * 
	 * @param serviceClass
	 *            the service class
	 * @return the service
	 */
	public <T> T getService(Class<T> serviceClass);

	/**
	 * Return the view toolbar.
	 * 
	 * @return
	 */
	@Override
	IToolBarManager getToolBarManager();

}