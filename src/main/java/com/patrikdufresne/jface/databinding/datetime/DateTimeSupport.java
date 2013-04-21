/**
 * Copyright(C) 2013 Patrik Dufresne Service Logiciel <info@patrikdufresne.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrikdufresne.jface.databinding.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.internal.databinding.BindingMessages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

/**
 * Utility class to provide support for date time select using {@link Combo} or
 * {@link CCombo}. This class allows to bind a widget text property with an
 * {@link IObservableValue} containing a date instance.
 * 
 * @author Patrik Dufresne
 * 
 */
public class DateTimeSupport {

    /**
     * Convert a date into a strin.
     * 
     * @author Patrik Dufresne
     * 
     */
    protected class DateToStringConverter extends Converter {

        public DateToStringConverter() {
            super(Date.class, String.class);
        }

        @Override
        public Object convert(Object from) {
            if (from instanceof Date) {
                String value = format.format(from);
                return value;
            }
            return null;
        }

    }

    /**
     * Validate the string value.
     * 
     * @author Patrik Dufresne
     * 
     */
    protected class DateValidator implements IValidator {

        /**
         * This implementation use the date format to validate the string value.
         */
        @Override
        public IStatus validate(Object input) {
            try {
                format.parse(input.toString());
                return ValidationStatus.ok();
            } catch (ParseException e) {
                return ValidationStatus.error(String.format(BindingMessages.getString("Validate_DateInvalid"), //$NON-NLS-1$
                        input.toString()));
            }
        }

    }

    /**
     * Converter parsing a date.
     * 
     * @author Patrik Dufresne
     * 
     */
    protected class StringToDateConverter extends Converter {

        /**
         * Create a new converter.
         * 
         */
        public StringToDateConverter() {
            super(String.class, Date.class);
        }

        /**
         * This implementation use the date format to parse the string and
         * return a date. Parsing exception are not throws.
         */
        @Override
        public Object convert(Object from) {
            if (from instanceof String) {
                try {
                    return format.parse((String) from);
                } catch (ParseException e) {
                    return null;
                }
            }
            return null;
        }

    }

    /**
     * Approximative size used to compute the width.
     */
    protected static final int ARROW_WIDTH = 34;

    /**
     * Approximative size used to compute the width.
     */
    protected static final int BORDER_WIDTH = 1;

    /**
     * Number of millisec in a day
     */
    private static final int DAY = 86400000 /* 24 x 60 x 60 x 1000 */;

    /**
     * Constant value used to sets a step of 30 min.
     */
    public static final int STEP_30 = 1800000 /* 30 x 60 x 1000 */;

    /**
     * Constant value used to sets a step of 60 min.
     */
    public static final int STEP_60 = 3600000 /* 60 x 60 x 1000 */;

    /**
     * Create a new date time support for the {@link CCombo} widget with a
     * specific date format and date values.
     * 
     * @param combo
     *            the widget combo
     * @param dbc
     *            the data binding context to be used
     * @param date
     *            The observable value to bind with (the model)
     * @param dateFormat
     *            the date format or null for default
     * @param values
     *            the values to select from.
     * @return a new instance of this class
     */
    public static DateTimeSupport create(CCombo combo, DataBindingContext dbc, IObservableValue date, DateFormat dateFormat, Collection<Date> values) {
        // Set the items value.
        DateFormat format = dateFormat != null ? dateFormat : DateFormat.getTimeInstance();
        combo.setItems(createItems(format, values));
        // Create the observable.
        DateTimeSupport support = new DateTimeSupport(combo, dbc, date);
        support.setDateFormat(format);
        support.setValues(values);
        return support;
    }

    /**
     * Create a new time support for the {@link CCombo} widget.
     * 
     * @param combo
     *            the widget to adapt
     * @param dbc
     *            the data binding context
     * @param date
     *            the observable date to bind with (the model)
     * @param dateFormat
     *            the date format
     * @param step
     *            the step in milliseconds
     * @return a new instance of this class.
     */
    public static DateTimeSupport create(CCombo combo, DataBindingContext dbc, IObservableValue date, DateFormat dateFormat, int step) {
        return create(combo, dbc, date, dateFormat, createValues(step));
    }

    /**
     * Create a new date time support for the {@link Combo} widget with a
     * specific date format and date values.
     * 
     * @param combo
     *            the widget combo
     * @param dbc
     *            the data binding context to be used
     * @param date
     *            The observable value to bind with (the model)
     * @param dateFormat
     *            the date format or null for default
     * @param values
     *            the values to select from.
     * @return a new instance of this class
     */
    public static DateTimeSupport create(Combo combo, DataBindingContext dbc, IObservableValue date, DateFormat dateFormat, Collection<Date> values) {
        // Set the items value.
        DateFormat format = dateFormat != null ? dateFormat : DateFormat.getTimeInstance();
        combo.setItems(createItems(format, values));
        // Create the observable.
        DateTimeSupport support = new DateTimeSupport(combo, dbc, date);
        support.setDateFormat(format);
        support.setValues(values);
        return support;
    }

    /**
     * Create a new time support for the combo widget.
     * 
     * @param combo
     *            the widget to adapt
     * @param dbc
     *            the data binding context
     * @param date
     *            the observable date to bind with (the model)
     * @param dateFormat
     *            the date format
     * @param step
     *            the step in milliseconds
     * @return a new instance of this class.
     */
    public static DateTimeSupport create(Combo combo, DataBindingContext dbc, IObservableValue date, DateFormat dateFormat, int step) {
        return create(combo, dbc, date, dateFormat, createValues(step));
    }

    /**
     * This function create a new date time support for a combo widget. This
     * function may be used to adapt a combo to select any time range. i.e.:
     * year, month, week, days, hours.
     * <p>
     * This function will display a list of date between <code>from</code> and
     * <code>code</code>. The number of items and the step between each date is
     * compute using the parameters <code>field</code> and <code>step</code>.
     * 
     * @param combo
     *            the widget to adapt
     * @param dbc
     *            the data binding context
     * @param date
     *            the observable date value (the model)
     * @param dateFormat
     *            the date format to display
     * @param from
     *            the starting date to be display.
     * @param to
     *            the ending date to be display.
     * @param field
     *            the field to be increase. Should be a {@link Calendar} field.
     * @param step
     *            the amount
     * @return a new instance of this class.
     */
    public static DateTimeSupport create(
            Combo combo,
            DataBindingContext dbc,
            IObservableValue date,
            DateFormat dateFormat,
            Date from,
            Date to,
            int field,
            int step) {
        return create(combo, dbc, date, dateFormat, createValues(from, to, field, step));
    }

    /**
     * Create all the items in the combo.
     */
    private static String[] createItems(DateFormat format, Collection<Date> values) {
        int i = 0;
        String[] items = new String[values.size()];
        for (Date date : values) {
            items[i++] = format.format(date);
        }
        return items;
    }

    /**
     * Static function used to create the date time value. This function create
     * date value between <code>from</code> and <code>to</code> with the
     * specified step value in milliseconds.
     * 
     * @param from
     *            the start date
     * @param to
     *            the end date
     * @param field
     *            the field to increase
     * @param step
     *            the increase value
     * @return collection of date value.
     */
    private static Collection<Date> createValues(Date from, Date to, int field, int step) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        List<Date> dates = new ArrayList<Date>();
        while (cal.getTime().compareTo(to) < 0) {
            dates.add(cal.getTime());
            cal.add(field, step);
        }
        return dates;
    }

    /**
     * Static function used to create the date time value to adapt a combo into
     * a time widget selection. This function create the value between 0:00 to
     * 24:00 with the given step.
     * 
     * @param step
     *            the step in milliseconds.
     * @return the date value.
     */
    private static Collection<Date> createValues(int step) {
        // TODO Create an abstract collection to generate the data.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        List<Date> dates = new ArrayList<Date>(DAY / step);
        int max = DAY / step;
        for (int count = 0; count < max; count++) {
            dates.add(cal.getTime());
            cal.add(Calendar.MILLISECOND, step);
        }
        return dates;
    }

    Binding binding;

    IObservableValue date;

    ControlDecorationSupport decorationSupport;

    IDisposeListener disposeListener = new IDisposeListener() {
        public void handleDispose(DisposeEvent staleEvent) {
            dispose();
        }
    };

    /**
     * The date format used to render the date values.
     */
    DateFormat format;

    private ISWTObservableValue text;

    /**
     * The date value of this widget.
     */
    List<Date> values;

    /**
     * The widget.
     */
    Control widget;

    /**
     * Create a new instance of this class.
     */
    public DateTimeSupport(Control widget, DataBindingContext dbc, IObservableValue date) {
        if (widget == null || dbc == null || date == null) {
            throw new NullPointerException();
        }
        this.widget = widget;
        this.format = DateFormat.getTimeInstance();

        // Add a dispose listener to the observable.
        this.date = date;
        this.date.addDisposeListener(this.disposeListener);

        // Create customs update strategies to converter the data
        UpdateValueStrategy targetToModel = new UpdateValueStrategy();
        targetToModel.setConverter(new StringToDateConverter());
        targetToModel.setAfterGetValidator(new DateValidator());

        UpdateValueStrategy modelToTarget = new UpdateValueStrategy();
        modelToTarget.setConverter(new DateToStringConverter());

        // Bind the observable value
        this.text = WidgetProperties.text().observe(this.widget);
        this.text.addDisposeListener(this.disposeListener);
        this.binding = dbc.bindValue(text, this.date, targetToModel, modelToTarget);

    }

    /**
     * Add a decoration support to the binding with default position and default
     * updater.
     */
    public void addDecorationSupport() {
        addDecorationSupport(SWT.LEFT | SWT.TOP, null);
    }

    /**
     * Add a decoration support to the binding to display an error icon if the
     * string is not valid.
     * 
     * @param position
     *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
     *            constructing {@link ControlDecoration} instances.
     * @param updater
     *            custom strategy for updating the {@link ControlDecoration}(s)
     *            whenever the validation status changes or null to use the
     *            default.
     */
    public void addDecorationSupport(int position, ControlDecorationUpdater updater) {
        if (this.decorationSupport == null) {
            // Add a decorator to display error
            this.decorationSupport = ControlDecorationSupport.create(binding, position, null, updater);
        }
    }

    /**
     * Used to dispose this class.
     */
    public void dispose() {
        if (this.date != null) {
            this.date = null;
        }
        if (this.decorationSupport != null) {
            this.decorationSupport.dispose();
            this.decorationSupport = null;
        }
        this.widget = null;
        this.format = null;
        this.values = null;
        this.binding = null;
    }

    /**
     * Return the binding.
     * 
     * @return
     */
    protected Binding getBinding() {
        return this.binding;
    }

    /**
     * Return the current date format.
     * 
     * @return the date format
     */
    public DateFormat getDateFormat() {
        return this.format;
    }

    /**
     * Returns the current list of values or null if not set
     * 
     * @return the values
     */
    public List<Date> getValues() {
        return this.values;
    }

    /**
     * This function is used to compute the widget width according to the items.
     */
    public int getWidthHint() {

        // Get the list of items
        String[] items;
        if (widget instanceof Combo) {
            items = ((Combo) widget).getItems();
        } else if (widget instanceof CCombo) {
            items = ((CCombo) widget).getItems();
        } else {
            // Unsupported control
            return SWT.DEFAULT;
        }

        // Compute the width for each item
        GC gc = new GC(this.widget);
        int spacer = gc.stringExtent(" ").x; //$NON-NLS-1$
        int textWidth = 0;
        for (int i = 0; i < items.length; i++) {
            textWidth = Math.max(gc.stringExtent(items[i]).x, textWidth);
        }
        gc.dispose();

        return textWidth + 2 * spacer + ARROW_WIDTH + 2 * BORDER_WIDTH;

    }

    /**
     * If previously added, remove the decoration support.
     */
    public void removeDecorationSupport() {
        if (this.decorationSupport != null) {
            this.decorationSupport.dispose();
        }
    }

    /**
     * Used to sets a new date format.
     * 
     * @param format
     *            the new date format or null to retore default.
     */
    public void setDateFormat(DateFormat format) {
        DateFormat newFormat = format;
        if (newFormat == null) {
            newFormat = DateFormat.getTimeInstance();
        }
        this.format = format;
        updateItems();
    }

    /**
     * Sets the available date value. If the widget is a {@link Combo} or
     * {@link CCombo}, this function will update the widget items using the
     * values specified.
     * 
     * @param values
     *            the new values.
     */
    public void setValues(Collection<Date> values) {
        this.values = new ArrayList<Date>(values);
        updateItems();
    }

    /**
     * This function is used to update the items of the widget. Support the
     * {@link Combo} and {@link CCombo} widgets.
     */
    protected void updateItems() {
        // Create the items list
        String[] newItems;
        if (this.values != null) {
            newItems = createItems(format, values);
        } else {
            newItems = new String[0];
        }
        String newText = "";
        if (this.date.getValue() instanceof Date) {
            newText = this.format.format(this.date.getValue());
        }

        // Update the widget
        if (widget instanceof Combo) {
            ((Combo) widget).setItems(newItems);
            ((Combo) widget).setText(newText);
        } else if (widget instanceof CCombo) {
            ((CCombo) widget).setItems(newItems);
            ((CCombo) widget).setText(newText);
        }
    }
}
