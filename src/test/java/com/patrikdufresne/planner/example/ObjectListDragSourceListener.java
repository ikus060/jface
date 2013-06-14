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
/**
 * 
 */
package com.patrikdufresne.planner.example;

import java.util.List;

import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

public class ObjectListDragSourceListener implements TransferDragSourceListener {

    private List<?> dragSourceItem;
    private AbstractObjectsTransfer transfer;
    private Viewer viewer;

    public ObjectListDragSourceListener(Viewer viewer, Transfer transfer) {
        this.viewer = viewer;
        if (!(transfer instanceof AbstractObjectsTransfer)) {
            throw new ClassCastException("transfert must extends AbstractObjectsTransfer"); //$NON-NLS-1$
        }
        this.transfer = (AbstractObjectsTransfer) transfer;
    }

    public void dragFinished(DragSourceEvent event) {
        // Nothing to do to move the object, it's done by the DropTarget
        this.dragSourceItem = null;
    }

    public void dragSetData(DragSourceEvent event) {
        if (this.transfer.isSupportedType(event.dataType)) {
            event.data = this.dragSourceItem;
        }
    }

    public void dragStart(DragSourceEvent event) {
        // To start the drag event, we need to make sure the
        // selection contain only valid element for the Transfert
        IStructuredSelection selection = ((IStructuredSelection) this.viewer.getSelection());
        if (!selection.isEmpty()) {
            List<?> list = selection.toList();
            if (this.transfer.validate(list)) {
                event.doit = true;
                this.dragSourceItem = list;
                return;
            }
        }
        event.doit = false;

    }

    @Override
    public Transfer getTransfer() {
        return (Transfer) this.transfer;
    }

    protected Viewer getViewer() {
        return this.viewer;
    }
}