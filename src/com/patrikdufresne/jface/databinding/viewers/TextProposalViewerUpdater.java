/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.databinding.viewers;

import org.eclipse.jface.databinding.viewers.IViewerUpdater;

import com.patrikdufresne.jface.viewers.TextProposalViewer;

// TODO This should be deprecated since the viewer will be deprecated.
public class TextProposalViewerUpdater implements IViewerUpdater {
	/**
	 * The viewer to update.
	 */
	private final TextProposalViewer viewer;

	/**
	 * Constructs a ViewerUpdater for updating the specified viewer.
	 * 
	 * @param viewer
	 *            the viewer which will be updated through this instance.
	 */
	public TextProposalViewerUpdater(TextProposalViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void insert(Object element, int position) {
		this.viewer.refresh();
	}

	@Override
	public void remove(Object element, int position) {
		this.viewer.refresh();
	}

	@Override
	public void replace(Object oldElement, Object newElement, int position) {
		this.viewer.refresh();
	}

	@Override
	public void move(Object element, int oldPosition, int newPosition) {
		this.viewer.refresh();
	}

	@Override
	public void add(Object[] elements) {
		this.viewer.refresh();
	}

	@Override
	public void remove(Object[] elements) {
		this.viewer.refresh();
	}

}
