package com.patrikdufresne.jface.databinding.viewers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;

import com.patrikdufresne.jface.viewers.AbstractColumnSorter;
import com.patrikdufresne.jface.viewers.TableViewerUpdater;
import com.patrikdufresne.jface.viewers.TreeViewerUpdater;
import com.patrikdufresne.jface.viewers.ViewerColumnUpdater;

/**
 * Utility class to create columns and define it's properties using chaining
 * function.
 * 
 * @author Patrik Dufresne
 * 
 */
public class ColumnSupport {

	private static class BooleanCellEditor extends CheckboxCellEditor {

		WritableValue value = new WritableValue(Boolean.FALSE, Boolean.TYPE);

		public BooleanCellEditor(Composite parent) {
			super(parent);
		}

		@Override
		public void activate() {
			this.value
					.setValue(this.value.getValue() == null
							|| Boolean.TRUE.equals(this.value.getValue()) ? Boolean.FALSE
							: Boolean.TRUE);
			setValueValid(true);
			fireApplyEditorValue();
		}

		@Override
		protected Object doGetValue() {
			return this.value.getValue();
		}

		@Override
		protected void doSetValue(Object value) {
			Assert.isTrue(value instanceof Boolean);
			this.value.setValue(value);
		}

		public IObservableValue getObservableValue() {
			return this.value;
		}

	}

	private static final String DEFAULT_COLUMN_WIDTH = "column.width"; //$NON-NLS-1$

	private static final String EMPTY = ""; //$NON-NLS-1$

	/**
	 * The singleton natural comparator.
	 */
	private static Comparator naturalComparator;

	/**
	 * Create a new columns for the given table viewer. The column content will
	 * be map using the given value property.
	 * 
	 * @param viewer
	 *            the table viewer
	 * @param style
	 *            the column style SWT.NONE, SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 * @param columnLabel
	 *            the column label
	 * @param knownElements
	 *            known elements (should be the content provider known elements)
	 * @param property
	 *            the value property used as label
	 * @return a new instance of {@link ColumnSupport} for chaining.
	 */
	public static ColumnSupport create(TableViewer viewer, int style,
			String columnLabel, IObservableSet knownElements,
			IValueProperty property) {
		return new ColumnSupport(new TableViewerColumn(viewer, style),
				new TableViewerUpdater(), columnLabel, knownElements, property);
	}

	/**
	 * Create a new columns for the given viewer. The column content will be map
	 * using the given attributeMap.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param columnLabel
	 *            the column label
	 * @param knownElements
	 *            known elements (should be the content provider known elements)
	 * @param property
	 *            the value property used as label
	 * @return a new instance of {@link ColumnSupport} for chaining.
	 */
	public static ColumnSupport create(TableViewer viewer, String columnLabel,
			IObservableSet knownElements, IValueProperty property) {
		return create(viewer, SWT.NONE, columnLabel, knownElements, property);
	}

	/**
	 * Create a new columns for the given tree viewer. The column content will
	 * be map using the given value property.
	 * 
	 * @param viewer
	 *            the tree viewer
	 * @param style
	 *            the column style SWT.NONE, SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 * @param columnLabel
	 *            the column label
	 * @param knownElements
	 *            known elements (should be the content provider known elements)
	 * @param property
	 *            the value property used as label
	 * @return a new instance of {@link ColumnSupport} for chaining.
	 */
	public static ColumnSupport create(TreeViewer viewer, int style,
			String columnLabel, IObservableSet knownElements,
			IValueProperty property) {
		return new ColumnSupport(new TreeViewerColumn(viewer, style),
				new TreeViewerUpdater(), columnLabel, knownElements, property);
	}

	/**
	 * Create a new columns for the given tree viewer. The column content will
	 * be map using the given value property.
	 * 
	 * @param viewer
	 *            the tree viewer
	 * @param columnLabel
	 *            the column label
	 * @param knownElements
	 *            known elements (should be the content provider known elements)
	 * @param property
	 *            the value property used as label
	 * @return a new instance of {@link ColumnSupport} for chaining.
	 */
	public static ColumnSupport create(TreeViewer viewer, String columnLabel,
			IObservableSet knownElements, IValueProperty property) {
		return create(viewer, SWT.NONE, columnLabel, knownElements, property);
	}

	/**
	 * Using reflection, this function return the viewer associated with the
	 * given cell editor by calling the function <code>getViewer()</code>.
	 * 
	 * @param cellEditor
	 *            the cell editor
	 * @return the associated viewer or null
	 */
	public static Viewer getViewer(CellEditor cellEditor) {
		Method method = internalFindMethod(cellEditor.getClass(), "getViewer", //$NON-NLS-1$
				0, null);
		try {
			return (Viewer) method.invoke(cellEditor, (Object[]) null);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
	}

	/**
	 * Internal support for finding a target methodName with a given parameter
	 * list on a given class.
	 */
	static Method internalFindMethod(Class start, String methodName,
			int argCount, Class args[]) {
		// For overriden methods we need to find the most derived version.
		// So we start with the given class and walk up the superclass chain.

		Method method = null;

		for (Class cl = start; cl != null; cl = cl.getSuperclass()) {

			Method[] methods = cl.getMethods();

			for (int i = 0; i < methods.length; i++) {
				method = methods[i];
				if (method == null) {
					continue;
				}

				// make sure method signature matches.
				Class params[] = method.getParameterTypes();
				if (method.getName().equals(methodName)
						&& params.length == argCount) {
					if (args != null) {
						boolean different = false;
						if (argCount > 0) {
							for (int j = 0; j < argCount; j++) {
								if (params[j] != args[j]) {
									different = true;
									continue;
								}
							}
							if (different) {
								continue;
							}
						}
					}
					return method;
				}
			}
		}
		return method;
	}

	/**
	 * Return a natural comparator.
	 * 
	 * @return
	 */
	public static Comparator naturalComparator() {
		if (naturalComparator == null) {
			naturalComparator = new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					if (o1 == null && o2 == null) {
						return 0;
					} else if (o1 == null) {
						return -1;
					} else if (o2 == null) {
						return 1;
					}
					return ((Comparable) o1).compareTo(o2);
				}
			};
		}
		return naturalComparator;
	}

	/**
	 * The column instance.
	 */
	private ViewerColumn column;

	/**
	 * The property.
	 */
	private IValueProperty property;

	/**
	 * The associated column sorter.
	 */
	private AbstractColumnSorter sorter;

	private ViewerColumnUpdater updater;

	/**
	 * Create a new instance of the utility class for chaining.
	 * <p>
	 * Calling this constructor will create a new column for the given viewer.
	 * 
	 * @param viewer
	 *            the viewer (Table or Tree)
	 * @param columnLabel
	 *            the column label
	 * @param attributeMap
	 *            the attribute map
	 * @param converter
	 *            the converter or null
	 */
	private ColumnSupport(ViewerColumn column, ViewerColumnUpdater updater,
			String columnLabel, IObservableSet knownElements,
			IValueProperty property) {

		this.column = column;
		this.updater = updater;

		// Sets default property value to the column.
		this.setText(columnLabel);
		this.setToolTipText(columnLabel);
		this.setMoveable(true);
		this.setResizable(true);
		this.setWidth(70);

		// Add default label provider
		this.column.setLabelProvider(new ObservableMapCellLabelProvider(
				property.observeDetail(knownElements)));
		this.property = property;

	}

	/**
	 * Used to activate the previously created sorting. Does nothing if no
	 * sorting exists.
	 * 
	 * @return same object for chaining
	 */
	public ColumnSupport activateSorting() {
		if (this.sorter != null) {
			this.sorter.activate();
		}
		return this;
	}

	/**
	 * Add a checkbox editing support to this column. Same as calling the
	 * function
	 * <code>addCheckboxEditingSupport(dbc, property, null, null)</code<.
	 * 
	 * @param dbc
	 *            the databinding context
	 * @param property
	 *            the property to be edited
	 * @return same object for chaining
	 */
	public ColumnSupport addCheckboxEditingSupport(
			final DataBindingContext dbc, final IValueProperty property) {
		return addCheckboxEditingSupport(dbc, property, null, null);
	}

	/**
	 * Add editing support to the column.
	 * <p>
	 * It's recommended to provide an update strategy for target to model to
	 * persist the data. It should be created with
	 * UpdateValueStrategy.POLICY_CONVERT.
	 * <p>
	 * It's recommended to provide a null strategy for model to target.
	 * 
	 * @param dbc
	 *            the data binding context
	 * @param property
	 *            the property to be edited. Should be a boolean type.
	 * @param targetToModel
	 *            strategy to employ when the target is the source of the change
	 *            and the model is the destination or null to use default
	 * @param modelToTarget
	 *            strategy to employ when the model is the source of the change
	 *            and the target is the destination or null to use default.
	 * @return same object for chaining
	 */
	public ColumnSupport addCheckboxEditingSupport(
			final DataBindingContext dbc, final IValueProperty property,
			final UpdateValueStrategy targetToModel,
			final UpdateValueStrategy modelToTarget) {

		this.column.setEditingSupport(new ObservableValueEditingSupport(
				this.column.getViewer(), dbc) {

			/**
			 * This implementation create the binding using the update strategy
			 * when available.
			 */
			@Override
			protected Binding createBinding(IObservableValue target,
					IObservableValue model) {
				return dbc.bindValue(target, model,
						targetToModel != null ? targetToModel
								: new UpdateValueStrategy(
										UpdateValueStrategy.POLICY_CONVERT),
						modelToTarget != null ? modelToTarget : null);
			}

			/**
			 * This implementation return an observable on the text widget.
			 */
			@Override
			protected IObservableValue doCreateCellEditorObservable(
					CellEditor cellEditor) {
				return ((BooleanCellEditor) cellEditor).getObservableValue();
			}

			@Override
			protected IObservableValue doCreateElementObservable(
					Object element, ViewerCell cell) {
				return property.observe(element);
			}

			/**
			 * This implementation create a TextCellEditor.
			 */
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new BooleanCellEditor(getComposite());
			}

		});
		return this;

	}

	/**
	 * Add property editing support to the column using the default property.
	 * 
	 * @return
	 */
	public ColumnSupport addPropertySorting() {
		return addPropertySorting(null, null);
	}

	/**
	 * Add sorting capability to the column.
	 * 
	 * @param property
	 *            the property to be sorted
	 */
	public ColumnSupport addPropertySorting(IValueProperty property) {
		return addPropertySorting(property, null);
	}

	/**
	 * Add sorting capability to the column.
	 * 
	 * @param comparator
	 *            the comparator or null to use natural order
	 * @return same object for chaining
	 */
	public ColumnSupport addPropertySorting(Comparator comparator) {
		return addPropertySorting(null, comparator);
	}

	/**
	 * Add sorting capability to the column
	 * 
	 * @param property
	 *            the property or null to use the internal property value
	 * @param comparator
	 *            the comparator or null to use natural order
	 * @return same object for chaining
	 */
	public ColumnSupport addPropertySorting(IValueProperty property,
			Comparator comparator) {
		if (this.sorter != null) {
			this.sorter.dispose();
		}
		final IValueProperty finalProperty = property != null ? property
				: this.property;
		final Comparator finalComparator = comparator != null ? comparator
				: naturalComparator();
		this.sorter = new AbstractColumnSorter(this.column, this.updater) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return finalComparator.compare(finalProperty.getValue(e1),
						finalProperty.getValue(e2));
			}
		};
		return this;
	}

	/**
	 * Add text editing support to the column using the default property and
	 * default update strategy. Same as calling the function
	 * <code>addTextEditingSupport(dbc, null, null,null)</code>.
	 * 
	 * @param dbc
	 *            the data binding context.
	 * @return same object for chaining
	 */
	public ColumnSupport addTextEditingSupport(DataBindingContext dbc) {
		return addTextEditingSupport(dbc, null, null, null);
	}

	/**
	 * Add text editing support to the column.
	 * <p>
	 * It's recommended to provide an update strategy for target to model to
	 * persist the data. It should be created with
	 * UpdateValueStrategy.POLICY_CONVERT.
	 * <p>
	 * It's recommended to provide a null strategy for model to target.
	 * 
	 * @param dbc
	 *            the data binding context
	 * @param style
	 *            the text widget's style.
	 * @param property
	 *            the property to be edited or null to use default property. The
	 *            property value should be a String.
	 * @param targetToModel
	 *            strategy to employ when the target is the source of the change
	 *            and the model is the destination or null to use default
	 * @param modelToTarget
	 *            strategy to employ when the model is the source of the change
	 *            and the target is the destination or null to use default.
	 * @return same object for chaining
	 */
	public ColumnSupport addTextEditingSupport(final DataBindingContext dbc,
			final int style, IValueProperty property,
			final UpdateValueStrategy targetToModel,
			final UpdateValueStrategy modelToTarget) {
		final IValueProperty finalProperty = property != null ? property
				: this.property;

		this.column.setEditingSupport(new ObservableValueEditingSupport(
				this.column.getViewer(), dbc) {

			TextCellEditor cellEditor;

			ControlDecorationSupport decoration;

			/**
			 * Listen to target disposal to disepo the control decoration.
			 */
			private IDisposeListener targetDisposeListener = new IDisposeListener() {
				@Override
				public void handleDispose(DisposeEvent event) {
					// The target is disposed, let dispose the
					// decorator.
					if (decoration != null) {
						decoration.dispose();
						decoration = null;
					}
				}
			};

			/**
			 * This implementation create the binding using the update strategy
			 * when available.
			 */
			@Override
			protected Binding createBinding(IObservableValue target,
					IObservableValue model) {
				// Create the binding with our update strategy
				Binding binding = dbc.bindValue(target, model,
						targetToModel != null ? targetToModel
								: new UpdateValueStrategy(
										UpdateValueStrategy.POLICY_CONVERT),
						modelToTarget != null ? modelToTarget : null);

				// Attach a dispose listener to dispose the control
				// decoration too.
				target.addDisposeListener(this.targetDisposeListener);

				// While creating the binding, also create the
				// decoration
				if (this.decoration == null) {
					this.decoration = ControlDecorationSupport.create(binding,
							SWT.TOP | SWT.LEFT);
				}

				return binding;
			}

			/**
			 * This implementation return an observable on the text widget.
			 */
			@Override
			protected IObservableValue doCreateCellEditorObservable(
					CellEditor cellEditor) {
				return WidgetProperties.text(SWT.Modify).observe(
						cellEditor.getControl());
			}

			@Override
			protected IObservableValue doCreateElementObservable(
					Object element, ViewerCell cell) {
				return finalProperty.observe(element);
			}

			/**
			 * This implementation create a TextCellEditor.
			 */
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (this.cellEditor == null) {
					this.cellEditor = new TextCellEditor(getComposite(), style);
				}
				return this.cellEditor;
			}

		});
		return this;

	}

	/**
	 * Add text editing support to the column using the property specified and
	 * default update strategies. Same as calling the function
	 * <code>addTextEditingSupport(dbc, property, null,null)</code>.
	 * 
	 * @param dbc
	 *            the data binding context.
	 * @param property
	 *            the property or null to use the internal property value
	 * @return same object for chaining
	 */
	public ColumnSupport addTextEditingSupport(DataBindingContext dbc,
			IValueProperty property) {
		return addTextEditingSupport(dbc, property, null, null);
	}

	/**
	 * Add text editing support to the column.
	 * <p>
	 * It's recommended to provide an update strategy for target to model to
	 * persist the data. It should be created with
	 * UpdateValueStrategy.POLICY_CONVERT.
	 * <p>
	 * It's recommended to provide a null strategy for model to target.
	 * 
	 * @param dbc
	 *            the data binding context
	 * @param property
	 *            the property to be edited or null to use default property. The
	 *            property value should be a String.
	 * @param targetToModel
	 *            strategy to employ when the target is the source of the change
	 *            and the model is the destination or null to use default
	 * @param modelToTarget
	 *            strategy to employ when the model is the source of the change
	 *            and the target is the destination or null to use default.
	 * @return same object for chaining
	 */
	public ColumnSupport addTextEditingSupport(final DataBindingContext dbc,
			IValueProperty property, final UpdateValueStrategy targetToModel,
			final UpdateValueStrategy modelToTarget) {
		return addTextEditingSupport(dbc, SWT.NONE, property, targetToModel,
				modelToTarget);
	}

	/**
	 * Add editing support to the column. Same as calling the function
	 * <code>addTextEditingSupport(dbc, null, targetToModel, modelToTarget)</code>
	 * .
	 * 
	 * @param dbc
	 *            the data binding context to be used during the editing.
	 * @param targetToModel
	 *            strategy to employ when the target is the source of the change
	 *            and the model is the destination or null to use default
	 * @param modelToTarget
	 *            strategy to employ when the model is the source of the change
	 *            and the target is the destination or null to use default.
	 * @return same object for chaining
	 */
	public ColumnSupport addTextEditingSupport(DataBindingContext dbc,
			UpdateValueStrategy targetToModel, UpdateValueStrategy modelToTarget) {
		return addTextEditingSupport(dbc, null, targetToModel, modelToTarget);
	}

	/**
	 * Add viewer editing support to the column. Same as calling the function
	 * <code>addViewerEditingSupport(dbc, property, cellEditor,null,null)</code>
	 * .
	 * 
	 * @param dbc
	 *            the data binding context
	 * @param property
	 *            the property to be edited
	 * @param cellEditor
	 *            the cell editor. Should have a getViewer() function returning
	 *            the internal viewer object.
	 * @return same object for chaining
	 */
	public ColumnSupport addViewerEditingSupport(DataBindingContext dbc,
			final IValueProperty property, final CellEditor cellEditor) {
		return addViewerEditingSupport(dbc, property, cellEditor, null, null);
	}

	/**
	 * Add viewer editing support to the column.
	 * <p>
	 * It's recommended to provide an update strategy for target to model to
	 * persist the data. It should be created with
	 * UpdateValueStrategy.POLICY_CONVERT.
	 * <p>
	 * It's recommended to provide a null strategy for model to target.
	 * 
	 * @param dbc
	 *            the data binding context
	 * @param property
	 *            the property to be edited. Should be a string type.
	 * @param cellEditor
	 *            the cell editor. Should have a getViewer() function returning
	 *            the internal viewer object.
	 * @param targetToModel
	 *            strategy to employ when the target is the source of the change
	 *            and the model is the destination or null to use default
	 * @param modelToTarget
	 *            strategy to employ when the model is the source of the change
	 *            and the target is the destination or null to use default.
	 * @return same object for chaining
	 */
	public ColumnSupport addViewerEditingSupport(final DataBindingContext dbc,
			final IValueProperty property, final CellEditor cellEditor,
			final UpdateValueStrategy targetToModel,
			final UpdateValueStrategy modelToTarget) {
		return addViewerEditingSupport(dbc, property, null, cellEditor,
				targetToModel, modelToTarget);
	}

	/**
	 * Add viewer editing support to the column.
	 * <p>
	 * It's recommended to provide an update strategy for target to model to
	 * persist the data. It should be created with
	 * UpdateValueStrategy.POLICY_CONVERT.
	 * <p>
	 * It's recommended to provide a null strategy for model to target.
	 * 
	 * @param dbc
	 *            the data binding context
	 * @param property
	 *            the property to be edited.
	 * @param canEditProperty
	 *            a property to verify if the property may be edited or null to
	 *            always allow editing. The property should return False or null
	 *            to present editing.
	 * @param cellEditor
	 *            the cell editor. Should have a getViewer() function returning
	 *            the internal viewer object.
	 * @param targetToModel
	 *            strategy to employ when the target is the source of the change
	 *            and the model is the destination or null to use default
	 * @param modelToTarget
	 *            strategy to employ when the model is the source of the change
	 *            and the target is the destination or null to use default.
	 * @return same object for chaining
	 */
	public ColumnSupport addViewerEditingSupport(final DataBindingContext dbc,
			final IValueProperty property,
			final IValueProperty canEditProperty, final CellEditor cellEditor,
			final UpdateValueStrategy targetToModel,
			final UpdateValueStrategy modelToTarget) {

		final Viewer cellEditorViewer = getViewer(cellEditor);
		if (cellEditorViewer == null) {
			throw new IllegalArgumentException(
					"Can't determine cell editor's viewer."); //$NON-NLS-1$
		}

		// Sets the editing support
		this.column.setEditingSupport(new ObservableValueEditingSupport(
				this.column.getViewer(), dbc) {

			/**
			 * This implementation use the canEditProperty to determine if we
			 * can edit the element.
			 */
			@Override
			protected boolean canEdit(Object element) {
				if (canEditProperty == null) {
					return true;
				}
				Object value = canEditProperty.getValue(element);
				if (value instanceof Boolean) {
					return ((Boolean) value).booleanValue();
				}
				return value != null;
			}

			/**
			 * This implementation create the binding using the update strategy
			 * when available.
			 */
			@Override
			protected Binding createBinding(IObservableValue target,
					IObservableValue model) {
				return dbc.bindValue(target, model,
						targetToModel != null ? targetToModel
								: new UpdateValueStrategy(
										UpdateValueStrategy.POLICY_CONVERT),
						modelToTarget != null ? modelToTarget : null);
			}

			/**
			 * This implementation return an observable on the text widget.
			 */
			@Override
			protected IObservableValue doCreateCellEditorObservable(
					CellEditor cellEditor) {
				return ViewerProperties.singleSelection().observe(
						cellEditorViewer);
			}

			@Override
			protected IObservableValue doCreateElementObservable(
					Object element, ViewerCell cell) {
				return property.observe(element);
			}

			/**
			 * This implementation create a TextCellEditor.
			 */
			@Override
			protected CellEditor getCellEditor(Object element) {
				return cellEditor;
			}

		});
		return this;

	}

	/**
	 * Return the tree viewer column instance
	 * 
	 * @return the column
	 */
	public ViewerColumn column() {
		return this.column;
	}

	/**
	 * Easy way to access the composite, either the Tree or the Table.
	 * 
	 * @return composite instance
	 */
	protected Composite getComposite() {
		return this.updater.getComposite(getViewer());
	}

	public Object getData(String key) {
		return this.updater.getData(this.column, key);
	}

	/**
	 * Returns the column's viewer
	 * 
	 * @return the columns's viewer
	 */
	private ColumnViewer getViewer() {
		return this.column.getViewer();
	}

	/**
	 * Returns the column width.
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.updater.getWidth(this.column);
	}

	/**
	 * Sets the application defined property of the receiver with the specified
	 * name to the given value.
	 * <p>
	 * Applications may associate arbitrary objects with the receiver in this
	 * fashion. If the objects stored in the properties need to be notified when
	 * the widget is disposed of, it is the application's responsibility to hook
	 * the Dispose event on the widget and do so.
	 * </p>
	 * 
	 * @param key
	 *            the name of the property
	 * @param value
	 *            the new value for the property
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the key is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 */
	public ColumnSupport setData(String key, Object value) {
		this.updater.setData(this.column, key, value);
		return this;
	}

	/**
	 * Sets the moveable attribute. A column that is moveable can be reordered
	 * by the user by dragging the header. A column that is not moveable cannot
	 * be dragged by the user but may be reordered by the programmer.
	 * 
	 * @param moveable
	 *            the moveable attribute
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public ColumnSupport setMoveable(boolean moveable) {
		this.updater.setMoveable(this.column, moveable);
		return this;
	}

	/**
	 * Sets the resizable attribute.
	 * 
	 * @param resizable
	 * @return same object for chaining
	 */
	public ColumnSupport setResizable(boolean resizable) {
		this.updater.setResizable(this.column, resizable);
		return this;
	}

	/**
	 * Sets the column label.
	 * 
	 * @param string
	 *            the new label value.
	 * @return
	 */
	public ColumnSupport setText(String string) {
		this.updater.setText(this.column, string != null ? string : EMPTY);
		return this;
	}

	/**
	 * Sets the column tooltip text
	 * 
	 * @param string
	 *            the text
	 * @return same object
	 */
	public ColumnSupport setToolTipText(String string) {
		this.updater.setToolTipText(this.column, string);
		return this;
	}

	/**
	 * Change the visibility of the column.
	 * 
	 * @param visible
	 *            the new visibility state
	 * @return
	 */
	public ColumnSupport setVisible(boolean visible) {
		// Store old width value
		int width = getWidth();
		if (width != 0) {
			setData(DEFAULT_COLUMN_WIDTH, Integer.valueOf(width));
		}
		// Set visibility
		if (visible) {
			width = getData(DEFAULT_COLUMN_WIDTH) != null ? ((Integer) getData(DEFAULT_COLUMN_WIDTH))
					.intValue() : 70;
			setWidth(width);
			setResizable(true);
		} else {
			setWidth(0);
			setResizable(false);
		}
		return this;
	}

	/**
	 * Sets the column's width
	 * 
	 * @param width
	 *            the width
	 * @return same object
	 */
	public ColumnSupport setWidth(int width) {
		this.updater.setWidth(this.column, width);
		return this;
	}

}
