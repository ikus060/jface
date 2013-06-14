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

import java.util.Date;

/**
 * @uml.dependency supplier="com.planimou.core.CalendarRecurrence"
 */
public class CalendarEvent {

    /**
     * @uml.property name="calendarEntry"
     * @uml.associationEnd inverse="events:com.planimou.core.CalendarEntry"
     */
    private CalendarEntry calendarEntry;

    /**
     * The end date and time of this event.
     * 
     * @uml.property name="endDate"
     */
    private Date endDate;

    /**
     * An array of calendar dates that are exceptions to the recurrence of this
     * event—dates where this event will not occur.
     * 
     * @uml.property name="exceptionDates"
     */
    private Date[] exceptionDates;

    /**
     * The parent event of this event. Used only if this is a detached event.
     * 
     * @uml.property name="mainEvent"
     */
    private CalendarEvent mainEvent;

    /**
     * The original date of a detached event. When you create a recurrent event,
     * a list of occurrences of the event are created. If you change a property
     * of an occurrence of the event, it becomes detached and this attribute is
     * used to record the original occurrence date.
     * 
     * @uml.property name="originalDate"
     */
    private Date originalDate;

    /**
     * Recurrence that contains frequency details about this event.
     * 
     * @uml.property name="recurrence"
     */
    private CalendarRecurrence recurrence;

    /**
     * The start date and time of this event.
     * 
     * @uml.property name="startDate"
     */
    private Date startDate;

    /**
     * A short description of this event.
     * 
     * @uml.property name="summary"
     */
    private String summary;

    /**
     * Getter of the property <tt>calendarEntry</tt>
     * 
     * @return Returns the calendarEntry.
     * @uml.property name="calendarEntry"
     */
    public CalendarEntry getCalendarEntry() {
        return this.calendarEntry;
    }

    /**
     * Getter of the property <tt>endDate</tt>
     * 
     * @return Returns the endDate.
     * @uml.property name="endDate"
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Getter of the property <tt>exceptionDates</tt>
     * 
     * @return Returns the exceptionDates.
     * @uml.property name="exceptionDates"
     */
    public Date[] getExceptionDates() {
        return this.exceptionDates;
    }

    /**
     * Getter of the property <tt>mainEvent</tt>
     * 
     * @return Returns the mainEvent.
     * @uml.property name="mainEvent"
     */
    public CalendarEvent getMainEvent() {
        return this.mainEvent;
    }

    /**
     * Getter of the property <tt>originalDate</tt>
     * 
     * @return Returns the originalDate.
     * @uml.property name="originalDate"
     */
    public Date getOriginalDate() {
        return this.originalDate;
    }

    /**
     * Getter of the property <tt>recurrence</tt>
     * 
     * @return Returns the recurrence.
     * @uml.property name="recurrence"
     */
    public CalendarRecurrence getRecurrence() {
        return this.recurrence;
    }

    /**
     * Getter of the property <tt>startDate</tt>
     * 
     * @return Returns the startDate.
     * @uml.property name="startDate"
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Getter of the property <tt>summary</tt>
     * 
     * @return Returns the summary.
     * @uml.property name="summary"
     */
    public String getSummary() {
        return this.summary;
    }

    /**
     * Setter of the property <tt>calendarEntry</tt>
     * 
     * @param calendarEntry
     *            The calendarEntry to set.
     * @uml.property name="calendarEntry"
     */
    public void setCalendarEntry(CalendarEntry calendarEntry) {
        this.calendarEntry = calendarEntry;
    }

    /**
     * Setter of the property <tt>endDate</tt>
     * 
     * @param endDate
     *            The endDate to set.
     * @uml.property name="endDate"
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Setter of the property <tt>exceptionDates</tt>
     * 
     * @param exceptionDates
     *            The exceptionDates to set.
     * @uml.property name="exceptionDates"
     */
    public void setExceptionDates(Date[] exceptionDates) {
        this.exceptionDates = exceptionDates;
    }

    /**
     * Setter of the property <tt>mainEvent</tt>
     * 
     * @param mainEvent
     *            The mainEvent to set.
     * @uml.property name="mainEvent"
     */
    public void setMainEvent(CalendarEvent mainEvent) {
        this.mainEvent = mainEvent;
    }

    /**
     * Setter of the property <tt>originalDate</tt>
     * 
     * @param originalDate
     *            The originalDate to set.
     * @uml.property name="originalDate"
     */
    public void setOriginalDate(Date originalDate) {
        this.originalDate = originalDate;
    }

    /**
     * Setter of the property <tt>recurrence</tt>
     * 
     * @param recurrence
     *            The recurrence to set.
     * @uml.property name="recurrence"
     */
    public void setRecurrence(CalendarRecurrence recurrence) {
        this.recurrence = recurrence;
    }

    /**
     * Setter of the property <tt>startDate</tt>
     * 
     * @param startDate
     *            The startDate to set.
     * @uml.property name="startDate"
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Setter of the property <tt>summary</tt>
     * 
     * @param summary
     *            The summary to set.
     * @uml.property name="summary"
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

}
