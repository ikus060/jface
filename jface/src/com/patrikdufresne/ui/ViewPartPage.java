/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;


/**
 * Instance of this class represent one page to be displayed within a
 * PageBookView.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class ViewPartPage extends ViewPart implements IViewPartPage {

	/**
	 * Default constructor
	 * 
	 * @param id
	 *            the part id
	 */
	public ViewPartPage(String id) {
		super(id);
	}

	/**
	 * This implementation type cast the IViewSite into a IPageSite.
	 */
	@Override
	public IPageSite getSite() {
		return (IPageSite) super.getSite();
	}

	/**
	 * This implementation check if the given site is an instance of a
	 * IPageSite. This allows the part/page to get access to it's parent part.
	 */
	@Override
	public void init(IViewSite newsite) {
		if (!(newsite instanceof IPageSite)) {
			throw new ClassCastException();
		}
		super.init(newsite);
	}

}
