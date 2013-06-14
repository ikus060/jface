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

import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

public class CalendarEventDropTargetListener extends ViewerDropAdapter implements TransferDropTargetListener {

    /**
     * Create a new class to enabled drop of <code>CalendarEvent</code>
     * on the <code>viewer</code>.
     * 
     * @param viewer
     *            the viewer
     */
    public CalendarEventDropTargetListener(Viewer viewer) {
        super(viewer);
    }

    /**
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#determineLocation(org.eclipse
     *      .swt.dnd.DropTargetEvent)
     */
    @Override
    protected int determineLocation(DropTargetEvent event) {
        int location = super.determineLocation(event);
        // if (((location & LOCATION_BEFORE) != 0 || (location & LOCATION_AFTER)
        // != 0)
        // && getCurrentTarget() instanceof CompetitionDay) {
        // location &= ~(LOCATION_BEFORE | LOCATION_AFTER);
        // location |= LOCATION_ON;
        // }
        return location;
    }

    /**
     * This implementation return a <code>CompetitionDisciplineTransfer</code>.
     * 
     * @see org.eclipse.jface.util.TransferDropTargetListener#getTransfer()
     */
    @Override
    public Transfer getTransfer() {
        return CalendarEventTransfer.getInstance();
    }

    /**
     * This implementation test if the given event is a valid drop.
     * 
     * @see org.eclipse.jface.util.TransferDropTargetListener#isEnabled(org.eclipse.swt.dnd.DropTargetEvent)
     */
    @Override
    public boolean isEnabled(DropTargetEvent event) {
        Object target = determineTarget(event);
        return validateDrop(target, event.operations, event.currentDataType);
    }

    /**
     * This implementation use the copyOrMove function of a manager to perform
     * the drop. This operation will then initiate a refresh of the viewer.
     * 
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#performDrop(java.lang.Object)
     */
    @Override
    public boolean performDrop(Object data) {

        // // Perform DROP
        // int curLoc = getCurrentLocation();
        // int curOp = getCurrentOperation();
        // List<CompetitionDiscipline> list = (List<CompetitionDiscipline>)
        // data;
        // if (getCurrentTarget() instanceof CompetitionDay) {
        // int operation = 0;
        // operation |= curOp == DND.DROP_COPY ? IManager.COPY : 0;
        // operation |= curOp == DND.DROP_MOVE ? IManager.MOVE : 0;
        // CompetitionDay competitionDay = (CompetitionDay) getCurrentTarget();
        // try {
        // ManagerFactory.getCompetitionDisciplineManager()
        // .copyOrMoveObjects(operation, list, competitionDay);
        // } catch (ManagerException e) {
        // MessageUtils.handleException(e);
        // return false;
        // }
        // return true;
        // } else if (getCurrentTarget() instanceof CompetitionDiscipline) {
        // int op = 0;
        // op |= curOp == DND.DROP_COPY ? IManager.COPY : 0;
        // op |= curOp == DND.DROP_MOVE ? IManager.MOVE : 0;
        // op |= curLoc == LOCATION_AFTER ? IManager.AFTER : 0;
        // op |= curLoc == LOCATION_BEFORE ? IManager.BEFORE : 0;
        // CompetitionDiscipline compDiscipline = (CompetitionDiscipline)
        // getCurrentTarget();
        // try {
        // ManagerFactory.getCompetitionDisciplineManager()
        // .copyOrMoveObjects(op, list, compDiscipline);
        // } catch (ManagerException e) {
        // MessageUtils.handleException(e);
        // return false;
        // }
        // return true;
        // }
        //
        // return false;
        return true;
    }

    /**
     * This implementation check if the target is valid for a drop.
     * 
     * @see org.eclipse.jface.viewers.ViewerDropAdapter#validateDrop(java.lang.Object,
     *      int, org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public boolean validateDrop(Object target, int operation, TransferData transferType) {
        return true;
        // System.out.println(operation);
        //
        // if ((operation & DND.DROP_COPY) != 0) {
        // return target instanceof CompetitionDiscipline
        // || target instanceof CompetitionDay;
        // } else if ((operation & DND.DROP_MOVE) != 0) {
        // return target instanceof CompetitionDiscipline
        // || target instanceof CompetitionDay;
        // } else if ((operation & DND.DROP_LINK) != 0) {
        // return false;
        // }
        // return false;
    }

}
