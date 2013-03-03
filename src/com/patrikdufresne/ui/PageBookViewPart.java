/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * This implementation of {@link ViewPart} create a part that can contains
 * multiple view part. Sub-classes should override the function
 * {@link #addViews()} and add new view part to this class using the function
 * {@link #addView(IViewPart)}.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class PageBookViewPart extends ViewPart {

	private ViewBook book;

	public PageBookViewPart(String id) {
		super(id);
	}

	@Override
	public void activate(Composite parent) {

		// Create the tabFolder
		this.book = createViewBook(parent, SWT.BOTTOM | SWT.FLAT);

		// Add view to the book.
		addViews();
	}

	public boolean activateView(String id) {
		return getBook().activateView(id);
	}

	protected void addView(IViewPart view) {
		initView(view);
		this.book.addView(view);
	}

	/**
	 * This function should be implement by subclasses.
	 */
	protected void addViews() {
		// TODO Auto-generated method stub
	}

	/**
	 * Create the view book control.
	 * 
	 * @return
	 */
	protected ViewBook createViewBook(Composite parent, int style) {
		ViewBook book = new ViewBook(parent, style);
		book.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return book;
	}

	public IViewPart getActive() {
		return getBook().getActive();
	}

	/**
	 * Returns the view book. This may be used to change the current view page
	 * 
	 * @return the view book
	 */
	protected ViewBook getBook() {
		return this.book;
	}

	public IViewPart[] getViews() {
		return getBook().getViews();
	}

	protected void initView(IViewPart part) {
		IViewSite site = new PageSite(this, part,
				this.book.getToolBarProvider());
		part.init(site);
		// Check if the site is properly sets
		if (part.getSite() != site) {
			throw new RuntimeException("site not properly set"); //$NON-NLS-1$
		}
	}

	protected void removeView(IViewPart part) {
		getBook().removeView(part);
	}

	protected void removeView(String id) {
		getBook().removeView(id);
	}

}
