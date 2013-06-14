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

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.util.DelegatingDragAdapter;
import org.eclipse.jface.util.DelegatingDropAdapter;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.patrikdufresne.planner.example.data.CalendarEvent;
import com.patrikdufresne.planner.example.data.Models;
import com.patrikdufresne.planner.viewer.PlannerLabelProvider;
import com.patrikdufresne.planner.viewer.PlannerViewer;

public class PlannerExample extends ApplicationWindow {

    class EditEventAction extends Action implements ISelectionChangedListener {
        ISelectionProvider provider;
        IShellProvider shell;

        public EditEventAction(ISelectionProvider provider) {
            provider.addSelectionChangedListener(this);
            setText("EditEvent");
            this.provider = provider;
        }

        public void run() {
            EventDialog dlg = new EventDialog(getShell(), EventDialog.EDIT);

            CalendarEvent event = (CalendarEvent) ((IStructuredSelection) provider.getSelection()).getFirstElement();
            dlg.setStartDate(event.getStartDate());
            dlg.setEndDate(event.getEndDate());
            dlg.setSummary(event.getSummary());

            if (dlg.open() != Dialog.OK) {
                return;
            }

            event.setStartDate(dlg.getStartDate());
            event.setEndDate(dlg.getEndDate());
            event.setSummary(dlg.getSummary());

            model.update(Arrays.asList(event));

        }

        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            setEnabled(((IStructuredSelection) event.getSelection()).size() == 1);
        }
    }

    static Models model;

    /**
     * 
     * @param itemtext
     * @param start
     *            in the form yyyy-MM-dd EEE HH:mm
     * @param end
     *            in the form yyyy-MM-dd EEE HH:mm
     * @return
     * @throws ParseException
     */
    protected static CalendarEvent createEvent(String itemtext, String start, String end) throws ParseException {
        CalendarEvent event = new CalendarEvent();
        event.setSummary(itemtext);
        event.setStartDate(TimeUtils.dateTime(start));
        event.setEndDate(TimeUtils.dateTime(end));
        return event;
    }

    protected static CalendarEvent createEvent(String itemtext, double start, double lenght) {

        CalendarEvent event = new CalendarEvent();
        event.setSummary(itemtext);

        // Convert float to date
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, (int) Math.floor(start));
        cal.set(Calendar.MINUTE, (int) (start - Math.floor(start)) * 60);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.setTime(cal.getTime());
        cal2.add(Calendar.HOUR_OF_DAY, (int) Math.floor(lenght));
        cal2.add(Calendar.MINUTE, (int) (lenght - Math.floor(lenght)) * 60);

        event.setStartDate(cal.getTime());
        event.setEndDate(cal2.getTime());

        return event;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        final Display display = new Display();
        Realm.runWithDefault(SWTObservables.getRealm(Display.getCurrent()), new Runnable() {
            @Override
            public void run() {
                // Populate the models
                model = new Models();

                try {
                    model.add(createEvent("test0", "2013-03-09 Sat 20:00", "2013-03-10 Sun 3:00"));
                    model.add(createEvent("test1", "2013-03-10 Sun 9:00", "2013-03-10 Sun 10:00"));
                    model.add(createEvent("test2", "2013-03-09 Sat 9:00", "2013-03-09 Sat 10:00"));
                    model.add(createEvent("test1", "2013-03-11 Mon 9:00", "2013-03-11 Mon 10:00"));
                    model.add(createEvent("test1", "2013-01-06 Sun 0:00", "2013-01-20 Sun 0:00"));
                    model.add(createEvent("COUCOU", "2013-01-12 Sat 18:00", "2013-01-13 Sat 0:00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                model.add(createEvent("test0", 0, 1));
                model.add(createEvent("test1", 13, 1));
                model.add(createEvent("test2", 14, 8.5));
                model.add(createEvent("test3", 15, 4));
                model.add(createEvent("test4", 15, 2));
                model.add(createEvent("test5", 16, 4));
                model.add(createEvent("test6", 20, 2));
                model.add(createEvent("Full day event", 0, 24));
                model.add(createEvent("multi day 1", -40, 22));
                model.add(createEvent("multi day 2", -40, 28));
                model.add(createEvent("multi day 3", -48, 60));
                model.add(createEvent("multi day 4", 21, 24 * 7));
                model.add(createEvent("Previous day", -24, 24));
                model.add(createEvent("Jour 2", 8.25, 6));
                model.add(createEvent("1234 - jour \r\ncoucou 1234 sadf with a very long statement making the text wrapping.\r\n123 - boubou", 5, 10));
                model.add(createEvent("Jour 1", 15, 10));

                PlannerExample win = new PlannerExample(null);
                // win.setBlockOnOpen(false);
                // win.open();
                //
                // win = new Main(null);
                win.setBlockOnOpen(true);
                win.open();
            }
        });
    }

    public PlannerExample(Shell parentShell) {
        super(parentShell);
        addToolBar(SWT.FLAT);
    }

    @Override
    protected Control createContents(Composite parent) {

        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new FillLayout());

        final PlannerViewer planner = new PlannerViewer(comp, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        planner.setContentProvider(new CalendarEventContentProvider());
        planner.setLabelProvider(new PlannerLabelProvider() {
            @Override
            public Date getEndTime(Object element) {
                return ((CalendarEvent) element).getEndDate();
            }

            @Override
            public Date getStartTime(Object element) {
                return ((CalendarEvent) element).getStartDate();
            }

            public String getText(Object element) {
                return ((CalendarEvent) element).getSummary();
            }

        });
        planner.setInput(this.model);

        /*
         * Events
         */
        planner.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                System.out.println("select change listener");
            }
        });
        planner.addOpenListener(new IOpenListener() {
            @Override
            public void open(OpenEvent event) {
                System.out.println("open event");
            }
        });
        planner.getPlanner().addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                System.out.println("default selection event");
            }
        });
        planner.getPlanner().addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                System.out.println("selection event");
            }
        });

        /*
         * Add Drag and drop support
         */
        TransferDragSourceListener[] dragSources = createDragSource(planner);
        if (dragSources != null && dragSources.length > 0) {
            DelegatingDragAdapter dragAdapter = new DelegatingDragAdapter();
            for (int i = 0; i < dragSources.length; i++) {
                dragAdapter.addDragSourceListener(dragSources[i]);
            }
            planner.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, dragAdapter.getTransfers(), dragAdapter);
        }

        TransferDropTargetListener[] dropTargets = createDropTarget(planner);
        if (dropTargets != null && dropTargets.length > 0) {
            DelegatingDropAdapter dropAdapter = new DelegatingDropAdapter();
            for (int i = 0; i < dropTargets.length; i++) {
                dropAdapter.addDropTargetListener(dropTargets[i]);
            }
            planner.addDropSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, dropAdapter.getTransfers(), dropAdapter);
        }

        /*
         * Actions
         */
        getToolBarManager().add(new Action("Previous") {
            public void run() {
                Date newDate = new Date(planner.getPlanner().getDateSelection().getTime() - (1000 * 60 * 60 * 24 * 7));
                planner.getPlanner().setDateSelection(newDate);
            }
        });

        getToolBarManager().add(new Action("Next") {
            public void run() {
                Date newDate = new Date(planner.getPlanner().getDateSelection().getTime() + (1000 * 60 * 60 * 24 * 7));
                planner.getPlanner().setDateSelection(newDate);
            }
        });

        getToolBarManager().add(new Action("New Event") {
            public void run() {
                EventDialog dlg = new EventDialog(PlannerExample.this.getShell(), EventDialog.NEW);
                if (dlg.open() != Dialog.OK) {
                    return;
                }
                Date start = dlg.getStartDate();
                Date end = dlg.getEndDate();
                String summary = dlg.getSummary();

                if (start.compareTo(end) > 0) {
                    Date t = start;
                    start = end;
                    end = t;
                }

                CalendarEvent event = new CalendarEvent();
                event.setStartDate(start);
                event.setEndDate(end);
                event.setSummary(summary);

                model.add(event);
            }
        });

        getToolBarManager().add(new EditEventAction(planner));

        getToolBarManager().update(true);

        return comp;
    }

    protected TransferDragSourceListener[] createDragSource(PlannerViewer planner) {
        return new TransferDragSourceListener[] { new ObjectListDragSourceListener(planner, CalendarEventTransfer.getInstance()) };
    };

    private TransferDropTargetListener[] createDropTarget(PlannerViewer planner) {
        return new TransferDropTargetListener[] { new CalendarEventDropTargetListener(planner) };
    }

}
