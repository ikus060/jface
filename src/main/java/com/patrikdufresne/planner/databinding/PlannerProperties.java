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
package com.patrikdufresne.planner.databinding;

import org.eclipse.core.databinding.property.value.IValueProperty;

import com.patrikdufresne.planner.databinding.internal.PlannerDateSelectionProperty;
import com.patrikdufresne.planner.databinding.internal.PlannerEndDateProperty;
import com.patrikdufresne.planner.databinding.internal.PlannerStartDateProperty;

/**
 * Utility class to create observable value for planner or planner viewer.
 * 
 * @author Patrik Dufresne
 * 
 */
public class PlannerProperties {
    /**
     * Private constructor to avoid creating a utility class.
     */
    private PlannerProperties() {
        // Nothing to do.
    }

    /**
     * Create an observable value for the planner selected date.
     * 
     * @param planner
     *            the planner widget
     * @return the observable value
     */
    public static IValueProperty dateSelection() {
        return new PlannerDateSelectionProperty();
    }

    public static IValueProperty startDateValue() {
        return new PlannerStartDateProperty();
    }

    public static IValueProperty endDateValue() {
        return new PlannerEndDateProperty();
    }

}
