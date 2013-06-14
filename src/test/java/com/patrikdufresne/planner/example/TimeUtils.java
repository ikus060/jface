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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {

    public static Date date(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = new GregorianCalendar();
        cal.setTime(format.parse(date));
        return cal.getTime();
    }

    public static Date dateTime(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd EEE HH:mm");
        Calendar cal = new GregorianCalendar();
        cal.setTime(format.parse(date));
        return cal.getTime();
    }

    public static Date time(String time) throws ParseException {
        DateFormat format = new SimpleDateFormat("EEE HH:mm");
        Calendar cal = new GregorianCalendar();
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        cal.setTime(format.parse(time));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        return cal.getTime();
    }

}
