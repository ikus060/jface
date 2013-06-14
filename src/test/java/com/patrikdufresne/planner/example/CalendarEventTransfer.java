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

import com.patrikdufresne.planner.example.data.CalendarEvent;

/**
 * This implementation is specialise to transfer <code>CompetitionDay</code>
 * object.
 * 
 * @author patapouf
 * 
 */
public class CalendarEventTransfer extends AbstractObjectsTransfer {

    private CalendarEventTransfer() {
        // Private constructor to avoid creation of singleton class
    }

    private static CalendarEventTransfer instance = new CalendarEventTransfer();

    /**
     * Return the unique instance of this class.
     * 
     * @return the unique instance of this class
     */
    public static CalendarEventTransfer getInstance() {
        return instance;
    }

    /**
     * This implementation return a <code>CompetitionDay</code> class type.
     * 
     * @see net.ekwos.gymkhana.ui.viewers.dnd.AbstractObjectListTransfer#objectClass()
     */
    @Override
    protected Class<?> objectClass() {
        return CalendarEvent.class;
    }

}