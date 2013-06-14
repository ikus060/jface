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

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * This abstract class may be specialise to work on specific object type. It's
 * provide the all feature set required by the <code>Transfer</code> class.
 * <p>
 * Sub-class must implement objectClass to define on which object type this
 * class can be used to transfer object.
 * </p>
 * 
 * @author patapouf
 * 
 */
public abstract class AbstractObjectsTransfer extends ByteArrayTransfer {

    private List<?> dndObjects;
    private String type;
    private int typeid;

    protected AbstractObjectsTransfer() {
        this.type = objectClass().getName();
        this.typeid = registerType(this.type);
    }

    boolean checkMyType(Object object) {
        if (object == null || !(object instanceof List<?>) || ((List<?>) object).size() == 0) {
            return false;
        }
        List<?> list = (List<?>) object;
        Iterator<?> iter = list.iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (!objectClass().isInstance(item)) return false;
        }
        return true;
    }

    @Override
    protected int[] getTypeIds() {
        return new int[] { this.typeid };
    }

    @Override
    protected String[] getTypeNames() {
        return new String[] { this.type };
    }

    /**
     * This implementation doesn't try to convert java object to native data.
     * It's keep an internal list of object. It's limit the drag & drop to this
     * application.
     * 
     * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object,
     *      org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public void javaToNative(Object object, TransferData transferData) {
        if (!checkMyType(object) || !isSupportedType(transferData)) {
            DND.error(DND.ERROR_INVALID_DATA);
        }
        byte[] bytes = this.type.getBytes();
        super.javaToNative(bytes, transferData);
        this.dndObjects = (List<?>) object;
    }

    /**
     * This implementation return the list of object stored previously by
     * <code>javaToNative</code>.
     * 
     * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
     */
    @Override
    public Object nativeToJava(TransferData transferData) {
        if (!isSupportedType(transferData)) return null;
        byte[] bytes = (byte[]) super.nativeToJava(transferData);
        if (bytes == null) return null;
        return this.dndObjects;
    }

    /**
     * Return the object type supported by this transfer class.
     * <p>
     * Sub class must return a specific object type to work with.
     * </p>
     * 
     * @return the object type.
     */
    protected abstract Class<?> objectClass();

    /**
     * This implement check if the given object can be assign from
     * <code>objectClass</code>.
     * 
     * @param object
     *            object to test
     * @return True if the object is valid
     * @see org.eclipse.swt.dnd.Transfer#validate(java.lang.Object)
     */
    @Override
    public boolean validate(Object object) {
        boolean val = checkMyType(object);
        return val;
    }

}