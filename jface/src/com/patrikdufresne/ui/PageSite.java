/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.swt.widgets.Shell;

/**
 * Concrete implementation of IViewSite for a PageBook.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PageSite extends ViewSite implements IPageSite {

	private PageBookViewPart parentPart;

	/**
	 * Create a new PageSite.
	 * 
	 * @param parentViewSite
	 */
	public PageSite(PageBookViewPart parentPart, IViewPart part, IToolBarProvider parent) {
		super(parentPart.getSite().getMainWindow(), part, parent);
		this.parentPart = parentPart;
	}

	@Override
	public PageBookViewPart getParentPart() {
		return this.parentPart;
	}

	@Override
	public <T> T getService(Class<T> serviceClass) {
		return getParentPart().getSite().getService(serviceClass);
	}

	@Override
	public Shell getShell() {
		return getParentPart().getSite().getShell();
	}

}
