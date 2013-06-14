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
package com.patrikdufresne.planner.example;

import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.patrikdufresne.jface.databinding.datetime.DateTimeSupport;

/**
 * Dialog to create or edit an event.
 * 
 * @author patapouf
 * 
 */
public class EventDialog extends Dialog {
    /**
     * Style to used with EventDialog to edit an event.
     */
    public static int EDIT = 2;
    /**
     * Style to used with EventDialog to create a new event.
     */
    public static int NEW = 1;

    private WritableValue end;

    private DateTime endDate;

    private Combo endTime;

    private WritableValue start;

    private DateTime startDate;

    private Combo startTime;

    private int style;

    private WritableValue summary;

    private Text summaryText;

    /**
     * Create a new dialog.
     * 
     * @param parentShell
     * @param style
     *            the dialog style (NEW or EDIT constant)
     */
    protected EventDialog(Shell parentShell, int style) {
        super(parentShell);
        this.style = style;
        this.summary = new WritableValue("", String.class);
        this.start = new WritableValue(new Date(), Date.class);
        this.end = new WritableValue(new Date(), Date.class);
    }

    /**
     * This implementation add a window title.
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        if (this.style == EDIT) {
            newShell.setText("Edit event");
        } else {
            newShell.setText("New event");
        }
    }

    /**
     * This implementation add controls to create or edit an event.
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        // Summary
        summaryText = new Text(composite, SWT.BORDER);
        summaryText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        // Start date To End date
        Composite compDates = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout(5, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        compDates.setLayout(layout);

        this.startDate = new DateTime(compDates, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
        this.startDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        this.startTime = new Combo(compDates, SWT.BORDER);
        this.startTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Label label = new Label(compDates, SWT.NONE);
        label.setText("To");
        this.endDate = new DateTime(compDates, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
        this.endDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        this.endTime = new Combo(compDates, SWT.BORDER);
        this.endTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        /*
         * Bind start date time
         */
        DataBindingContext dbc = new DataBindingContext();

        WritableValue startTimeValue = new WritableValue(null, Date.class);
        WritableValue endTimeValue = new WritableValue(null, Date.class);
        DateTimeSupport startTimeSupport = DateTimeSupport.create(this.startTime, dbc, startTimeValue, null, DateTimeSupport.STEP_60);

        dbc.bindValue(new DateAndTimeObservableValue(SWTObservables.observeSelection(this.startDate), startTimeValue), this.start);
        if (this.startTime.getLayoutData() instanceof GridData) {
            ((GridData) this.startTime.getLayoutData()).widthHint = startTimeSupport.getWidthHint();
        }

        DateTimeSupport endTimeSupport = DateTimeSupport.create(this.endTime, dbc, endTimeValue, null, DateTimeSupport.STEP_60);
        dbc.bindValue(new DateAndTimeObservableValue(SWTObservables.observeSelection(this.endDate), endTimeValue), this.end);
        if (this.endTime.getLayoutData() instanceof GridData) {
            ((GridData) this.endTime.getLayoutData()).widthHint = endTimeSupport.getWidthHint();
        }

        /*
         * Bind Summary
         */
        dbc.bindValue(WidgetProperties.text().observe(summaryText), this.summary);

        return composite;
    }

    public Date getEndDate() {
        return (Date) end.getValue();
    }

    public Date getStartDate() {
        return (Date) start.getValue();
    }

    public String getSummary() {
        return (String) summary.getValue();
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new NullPointerException();
        }
        this.end.setValue(endDate);
    }

    public void setStartDate(Date startDate) {
        if (end == null) {
            throw new NullPointerException();
        }
        this.start.setValue(startDate);
    }

    public void setSummary(String summary) {
        if (summary == null) {
            this.summary.setValue("");
        } else {
            this.summary.setValue(summary);
        }
    }

}
