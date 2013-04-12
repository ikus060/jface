/*
 * Copyright (c) 2011, Patrik Dufresne. All rights reserved.
 * Patrik Dufresne PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.patrikdufresne.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.patrikdufresne.jface.databinding.collections.FilteredObservableSet;
import com.patrikdufresne.jface.preference.PreferenceConverter;
import com.patrikdufresne.ui.ViewPart;

/**
 * This implementation of ViewPart provide default function {@link #bind()} and
 * {@link #bindValues()} to properly handle the binding using a databinding
 * context. It also provide an function to filter an observable set :
 * {@link #filter(IObservableSet, IConverter)}.
 * 
 * @author Patrik Dufresne
 * 
 */
public abstract class AbstractViewPart extends ViewPart {

	/**
	 * Create a new SashForm handling the restore and save process of the
	 * preferred weight.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param style
	 *            the sash form style. Either SWT.VERTICAL or SWT.HORIZONTAL
	 * @param preferenceStore
	 *            the preference store. Can't be null.
	 * @param prefKey
	 *            the preference key. Can't be null
	 * @param defaultWeights
	 *            the default weights
	 * @return the sash form
	 */
	public static SashForm createSashForm(Composite parent, int style,
			final IPreferenceStore preferenceStore, final String prefKey,
			final int[] defaultWeights) {
		if (preferenceStore == null || prefKey == null) {
			throw new NullPointerException();
		}
		final SashForm sash = new SashForm(parent, style);

		// Restore weights from preferences
		sash.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				PreferenceConverter.setDefault(preferenceStore, prefKey,
						defaultWeights);
				int[] weights = PreferenceConverter.getIntArray(preferenceStore,
						prefKey);
				if (weights.length == sash.getWeights().length) {
					sash.setWeights(weights);
				}

				sash.removeListener(SWT.Resize, this);
			}
		});

		// Save sash weights
		sash.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(Event event) {
				PreferenceConverter.setValue(preferenceStore, prefKey,
						((SashForm) event.widget).getWeights());
			}
		});

		return sash;

	}

	/**
	 * Create a table viewer.
	 * 
	 * @param parent
	 * @return
	 */
	public static TableViewer createTableViewer(Composite parent) {
		return createTableViewer(parent, SWT.NONE);
	}

	/**
	 * Create a table viewer with default editor activation event and traversal.
	 * The table widget is create with the following style :
	 * <code>SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION</code>
	 * <p>
	 * This function should be used to create a uniform table viewer in every
	 * view.
	 * 
	 * @param parent
	 *            the parent composite
	 * 
	 * @param style
	 *            extra style for the table widget
	 * 
	 * @return the table viewer
	 */
	public static TableViewer createTableViewer(Composite parent, int style) {
		// Create TableViewer
		TableViewer viewer = new TableViewer(parent, style | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.getTable().setHeaderVisible(true);

		// Change the editor strategy
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				viewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		TableViewerEditor.create(viewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		return viewer;
	}

	/**
	 * Create a table viewer.
	 * 
	 * @param parent
	 * 
	 * @return
	 */
	public static TreeViewer createTreeViewer(Composite parent) {
		return createTreeViewer(parent, SWT.NONE);
	}

	/**
	 * Create a tree viewer with default editor activation event and traversal.
	 * The tree widget is create with the following style :
	 * <code>SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION</code>
	 * <p>
	 * This function should be used to create a uniform table viewer in every
	 * view.
	 * 
	 * @param parent
	 *            the parent composite
	 * 
	 * @param style
	 *            extra style for the tree widget
	 * 
	 * @return the tree viewer
	 */
	public static TreeViewer createTreeViewer(Composite parent, int style) {
		// Create TableViewer
		TreeViewer viewer = new TreeViewer(parent, style | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.getTree().setHeaderVisible(true);

		// Change the editor strategy
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				viewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		TreeViewerEditor.create(viewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		return viewer;
	}

	private DataBindingContext dbc;

	/**
	 * Pattern used for filtering.
	 */
	IObservableValue filterPattern;

	/**
	 * Observable manager.
	 */
	private ObservablesManager om;

	/**
	 * Create a new view-part.
	 * 
	 * @param id
	 *            the part id
	 */
	public AbstractViewPart(String id) {
		super(id);
	}

	/**
	 * 
	 */
	protected void bind() {
		this.dbc = new DataBindingContext();
		this.om = new ObservablesManager();
		this.om.runAndCollect(new Runnable() {
			@Override
			public void run() {
				bindValues();
			}
		});
		this.om.addObservablesFromContext(this.dbc, true, true);
	}

	protected void bindValues() {
		// Nothing to do
	}

	/**
	 * Create a new contribution item to display a text filter.
	 * 
	 * @return
	 */
	protected IContributionItem createTextFilterContributionItem() {
		// Create a custom implementation of Control Contribution to contribute
		// to a CoolBar.
		return new ControlContribution("AbstractViewPart.filter") { //$NON-NLS-1$

			@Override
			protected Control createControl(Composite parent) {

				// Create a composite to adapt the size of the text zone.
				Composite comp = new Composite(parent, SWT.NONE);
				comp.setLayoutData(new RowData());
				GridLayout layout = new GridLayout(1, false);
				layout.marginHeight = 0;
				layout.marginWidth = 5;
				comp.setLayout(layout);

				// Create the widget.
				Text textFilter = new Text(comp, SWT.SINGLE | SWT.BORDER);
				textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
						true, true));

				// Bind the widget with the pattern.
				getDbc().bindValue(
						WidgetProperties.text(SWT.Modify).observe(textFilter),
						AbstractViewPart.this.filterPattern);

				return comp;

			}
		};
	}

	@Override
	public void deactivate() {
		try {
			if (this.om != null) {
				this.om.dispose();
				this.om = null;
			}
			if (this.dbc != null) {
				this.dbc.dispose();
				this.dbc = null;
			}
			if (this.filterPattern != null) {
				this.filterPattern.dispose();
				this.filterPattern = null;
			}
		} finally {
			super.deactivate();
		}
	}

	/**
	 * This function may be called by subclasses to add a text filter to the
	 * toolbar.
	 */
	protected void fillToolbarWithTextFilter() {
		// Set a value to pattern.
		if (this.filterPattern == null) {
			this.filterPattern = new WritableValue(null, String.class);

			// Add text filter to cool bar.
			getSite().getToolBarManager().add(
					createTextFilterContributionItem());
		}

	}

	/**
	 * Create a filtered observable set with the filter pattern.
	 * 
	 * @param set
	 * 
	 * @return
	 */
	protected IObservableSet filter(IObservableSet set, IConverter converter) {
		// Add a text filter
		if (this.filterPattern == null) {
			throw new RuntimeException("filterPattern is not created."); //$NON-NLS-1$
		}

		// Use the created pattern to create a filtered observable set.
		return new FilteredObservableSet(set, this.filterPattern, converter);
	}

	protected DataBindingContext getDbc() {
		return this.dbc;
	}

	/**
	 * Return the observables managers instance of this view part.
	 * 
	 * @return
	 */
	protected ObservablesManager getOm() {
		return om;
	}
}