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
package com.patrikdufresne.planner.example.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Models {

    public static final int ADD = 1;
    public static final int UPDATE = 2;
    public static final int REMOVE = 3;

    public ArrayList<ModelObserver> observers = new ArrayList<ModelObserver>();

    private ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();

    public void add(CalendarEvent event) {
        add(Arrays.asList(event));
    }

    public void add(Collection<CalendarEvent> events) {
        this.events.addAll(events);
        notifyObservers(ADD, events);
    }

    public void remove(Collection<CalendarEvent> events) {
        this.events.removeAll(events);
        notifyObservers(REMOVE, events);
    }

    public void update(Collection<CalendarEvent> events) {
        List<CalendarEvent> toUpdate = new ArrayList<CalendarEvent>(events);
        toUpdate.retainAll(this.events);
        notifyObservers(UPDATE, toUpdate);
    }

    private void notifyObservers(int type, Collection<CalendarEvent> events) {
        for (ModelObserver o : observers) {
            o.modelChanged(this, type, events);
        }
    }

    public Collection<CalendarEvent> list() {
        return Collections.unmodifiableList(this.events);
    }

}
