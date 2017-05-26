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
package com.patrikdufresne.jface.fieldassist;

import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.viewers.IViewerObservable;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.patrikdufresne.ui.ColorUtil;

public class ControlDecorationSupportUtils {

    private static final String DECORATION_WARNING_COLOR = "DECORATION_WARNING_COLOR";

    private static final String DECORATION_ERROR_COLOR = "DECORATION_ERROR_COLOR";

    private static final String DECORATION_WARNING_IMAGE = "DECORATION_WARNING_IMAGE";

    private static final String DECORATION_ERROR_IMAGE = "DECORATION_ERROR_IMAGE";

    private static final String DECORATION_INFO_IMAGE = "DECORATION_INFO_IMAGE";

    static {
        JFaceResources.getImageRegistry().put(DECORATION_WARNING_IMAGE, ImageDescriptor.createFromFile(ControlDecorationSupportUtils.class, "warning-16.png"));
        JFaceResources.getImageRegistry().put(DECORATION_ERROR_IMAGE, ImageDescriptor.createFromFile(ControlDecorationSupportUtils.class, "error-16.png"));
        JFaceResources.getImageRegistry().put(DECORATION_INFO_IMAGE, ImageDescriptor.createFromFile(ControlDecorationSupportUtils.class, "info-16.png"));
    }

    private static class ControlDecorationUpdater2 extends ControlDecorationUpdater {

        /**
         * Updates the visibility, image, and description text of the given
         * ControlDecoration to represent the given status.
         * 
         * @param decoration
         *            the ControlDecoration to update
         * @param status
         *            the status to be displayed by the decoration
         */
        @Override
        protected void update(ControlDecoration decoration, IStatus status) {
            // Update text and image.
            if (status == null || status.isOK()) {
                decoration.hide();
            } else {
                decoration.setImage(getImage(status));
                decoration.setDescriptionText(getDescriptionText(status));
                decoration.show();
            }
            // Update background color.
            if (status == null || status.isOK()) {
                Control c = decoration.getControl();
                if (c instanceof Text) {
                    c.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
                }
            } else {
                Control c = decoration.getControl();
                if (c instanceof Text) {
                    c.setBackground(getColor(status));
                }
            }
        }

        @Override
        protected Image getImage(IStatus status) {
            if (status == null) {
                return null;
            }
            String fieldDecorationID = null;
            switch (status.getSeverity()) {
            case IStatus.INFO:
                fieldDecorationID = DECORATION_INFO_IMAGE;
                break;
            case IStatus.WARNING:
                fieldDecorationID = DECORATION_WARNING_IMAGE;
                break;
            case IStatus.ERROR:
            case IStatus.CANCEL:
                fieldDecorationID = DECORATION_ERROR_IMAGE;
                break;
            }
            return JFaceResources.getImageRegistry().get(fieldDecorationID);
        }

        /**
         * Build a nice color to represent the status. Moslty red or yellow.
         * 
         * @param status
         * @return
         */
        protected Color getColor(IStatus status) {
            if (status.getSeverity() == Status.WARNING) {
                // Creates a blend of yellow and widget background
                if (!JFaceResources.getColorRegistry().hasValueFor(DECORATION_WARNING_COLOR)) {
                    RGB widgetB1 = ColorUtil.blend(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB(), new RGB(255, 255, 0), 75);
                    JFaceResources.getColorRegistry().put(DECORATION_WARNING_COLOR, widgetB1);
                }
                return JFaceResources.getColorRegistry().get(DECORATION_WARNING_COLOR);
            } else if (status.getSeverity() == Status.ERROR) {
                // Creates a blend of red and widget background
                if (!JFaceResources.getColorRegistry().hasValueFor(DECORATION_ERROR_COLOR)) {
                    RGB widgetB1 = ColorUtil.blend(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB(), new RGB(255, 0, 0), 75);
                    JFaceResources.getColorRegistry().put(DECORATION_ERROR_COLOR, widgetB1);
                }
                return JFaceResources.getColorRegistry().get(DECORATION_ERROR_COLOR);
            }
            return null;
        }

    }

    /**
     * Creates a ControlDecorationSupport which observes the validation status
     * of the specified {@link ValidationStatusProvider}, and displays a
     * {@link ControlDecoration} over the underlying SWT control of all target
     * observables that implement {@link ISWTObservable} or
     * {@link IViewerObservable}.
     * 
     * @param validationStatusProvider
     *            the {@link ValidationStatusProvider} to monitor.
     * @param position
     *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
     *            constructing {@link ControlDecorationSupportUtils}
     * @return a ControlDecorationSupport which observes the validation status
     *         of the specified {@link ValidationStatusProvider}, and displays a
     *         {@link ControlDecoration} over the underlying SWT control of all
     *         target observables that implement {@link ISWTObservable} or
     *         {@link IViewerObservable}.
     */
    public static org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport create(ValidationStatusProvider validationStatusProvider, int position) {
        return create(validationStatusProvider, position, null, new ControlDecorationUpdater2());
    }

    /**
     * Creates a ControlDecorationSupport which observes the validation status
     * of the specified {@link ValidationStatusProvider}, and displays a
     * {@link ControlDecoration} over the underlying SWT control of all target
     * observables that implement {@link ISWTObservable} or
     * {@link IViewerObservable}.
     * 
     * @param validationStatusProvider
     *            the {@link ValidationStatusProvider} to monitor.
     * @param position
     *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
     *            constructing {@link ControlDecoration} instances.
     * @param composite
     *            the composite to use when constructing
     *            {@link ControlDecoration} instances.
     * @return a ControlDecorationSupport which observes the validation status
     *         of the specified {@link ValidationStatusProvider}, and displays a
     *         {@link ControlDecoration} over the underlying SWT control of all
     *         target observables that implement {@link ISWTObservable} or
     *         {@link IViewerObservable}.
     */
    public static org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport create(
            ValidationStatusProvider validationStatusProvider,
            int position,
            Composite composite) {
        return create(validationStatusProvider, position, composite, new ControlDecorationUpdater2());
    }

    /**
     * Creates a ControlDecorationSupport which observes the validation status
     * of the specified {@link ValidationStatusProvider}, and displays a
     * {@link ControlDecoration} over the underlying SWT control of all target
     * observables that implement {@link ISWTObservable} or
     * {@link IViewerObservable}.
     * 
     * @param validationStatusProvider
     *            the {@link ValidationStatusProvider} to monitor.
     * @param position
     *            SWT alignment constant (e.g. SWT.LEFT | SWT.TOP) to use when
     *            constructing {@link ControlDecoration} instances.
     * @param composite
     *            the composite to use when constructing
     *            {@link ControlDecoration} instances.
     * @param updater
     *            custom strategy for updating the {@link ControlDecoration}(s)
     *            whenever the validation status changes.
     * @return a ControlDecorationSupport which observes the validation status
     *         of the specified {@link ValidationStatusProvider}, and displays a
     *         {@link ControlDecoration} over the underlying SWT control of all
     *         target observables that implement {@link ISWTObservable} or
     *         {@link IViewerObservable}.
     */
    public static org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport create(
            ValidationStatusProvider validationStatusProvider,
            int position,
            Composite composite,
            ControlDecorationUpdater updater) {
        return org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport.create(validationStatusProvider, position, composite, updater);
    }

}
