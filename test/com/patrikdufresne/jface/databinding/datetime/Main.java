package com.patrikdufresne.jface.databinding.datetime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main extends ApplicationWindow {

	public Main(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				Main win = new Main(null);
				win.setBlockOnOpen(true);
				win.open();
			}
		});

	}

	@Override
	protected Control createContents(Composite parent) {

		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(3, false));

		Combo time1 = new Combo(comp, SWT.BORDER);
		time1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		time1.setVisibleItemCount(6);
		time1.setTextLimit(15);

		Combo time2 = new Combo(comp, SWT.SIMPLE);
		time2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		time2.setVisibleItemCount(6);

		CCombo time3 = new CCombo(comp, SWT.BORDER);
		time3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		time3.setVisibleItemCount(6);

		Combo time4 = new Combo(comp, SWT.BORDER);
		time4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		time4.setVisibleItemCount(6);

		DataBindingContext dbc = new DataBindingContext();
		WritableValue date = new WritableValue(new Date(), Date.class);

		DateTimeSupport.create(time1, dbc, date, null, DateTimeSupport.STEP_30);
		DateTimeSupport.create(time2, dbc, date, DateFormat.getTimeInstance(
				DateFormat.SHORT, Locale.CANADA_FRENCH),
				DateTimeSupport.STEP_30);
		DateTimeSupport.create(time3, dbc, date,
				DateFormat.getTimeInstance(DateFormat.SHORT),
				DateTimeSupport.STEP_30);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date from = cal.getTime();
		cal.add(Calendar.MONTH, 24);
		Date to = cal.getTime();
		DateTimeSupport.create(time4, dbc, date,
				new SimpleDateFormat("MMMM yyyy"), from, to,
				Calendar.MONTH, 1);

		return comp;
	}
}