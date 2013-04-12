/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.jface.viewers;

import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;


/**
 * Implementation of the {@link ViewerColumnUpdater} interface for the
 * {@link TableViewer}.
 * 
 * @author Patrik Dufresne
 * 
 */
public class TableViewerUpdater implements ViewerColumnUpdater {

	static String COLUMN_VIEWER_KEY = Policy.JFACE + ".columnViewer";//$NON-NLS-1$

	@Override
	public void addListener(ViewerColumn column, int type, Listener listener) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().addListener(type, listener);
	}

	@Override
	public ViewerColumn getColumn(ColumnViewer viewer, int index) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		TableColumn column = ((TableViewer) viewer).getTable().getColumn(index);
		return (ViewerColumn) column.getData(COLUMN_VIEWER_KEY);
	}

	@Override
	public int getColumnCount(ColumnViewer viewer) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		return ((TableViewer) viewer).getTable().getColumnCount();
	}

	@Override
	public int[] getColumnOrder(ColumnViewer viewer) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		return ((TableViewer) viewer).getTable().getColumnOrder();
	}

	@Override
	public Composite getComposite(ColumnViewer viewer) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		return ((TableViewer) viewer).getTable();
	}

	@Override
	public Object getData(ViewerColumn column, String key) {
		if (column == null) {
			throw new NullPointerException();
		}
		return ((TableViewerColumn) column).getColumn().getData(key);
	}

	@Override
	public ViewerColumn getSortColumn(ColumnViewer viewer) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		TableColumn column = ((TableViewer) viewer).getTable().getSortColumn();
		if (column == null) {
			return null;
		}
		return (ViewerColumn) column.getData(COLUMN_VIEWER_KEY);
	}

	@Override
	public int getSortDirection(ColumnViewer viewer) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		return ((TableViewer) viewer).getTable().getSortDirection();
	}

	@Override
	public int getWidth(ViewerColumn column) {
		if (column == null) {
			throw new NullPointerException();
		}
		return ((TableViewerColumn) column).getColumn().getWidth();
	}

	@Override
	public int indexOf(ViewerColumn column) {
		if (column == null) {
			throw new NullPointerException();
		}
		return ((TableViewer) column.getViewer()).getTable().indexOf(
				((TableViewerColumn) column).getColumn());
	}

	@Override
	public void setColumnOrder(ColumnViewer viewer, int[] orders) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		((TableViewer) viewer).getTable().setColumnOrder(orders);
	}

	@Override
	public void setData(ViewerColumn column, String key, Object data) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().setData(key, data);
	}

	@Override
	public void setMoveable(ViewerColumn column, boolean moveable) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().setMoveable(moveable);
	}

	@Override
	public void setResizable(ViewerColumn column, boolean resizable) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().setResizable(resizable);
	}

	@Override
	public void setSortColumn(ColumnViewer viewer, ViewerColumn column) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		((TableViewer) viewer).getTable().setSortColumn(
				column != null ? ((TableViewerColumn) column).getColumn()
						: null);
	}

	@Override
	public void setSortDirection(ColumnViewer viewer, int direction) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		((TableViewer) viewer).getTable().setSortDirection(direction);
	}

	@Override
	public void setText(ViewerColumn column, String string) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().setText(string);
	}

	@Override
	public void setToolTipText(ViewerColumn column, String string) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().setToolTipText(string);
	}

	@Override
	public void setWidth(ViewerColumn column, int width) {
		if (column == null) {
			throw new NullPointerException();
		}
		((TableViewerColumn) column).getColumn().setWidth(width);
	}

	@Override
	public void removeListener(ViewerColumn column, int type, Listener listener) {
		// TODO Auto-generated method stub

	}
}
