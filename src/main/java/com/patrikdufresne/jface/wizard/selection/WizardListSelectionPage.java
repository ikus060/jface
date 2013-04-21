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
package com.patrikdufresne.jface.wizard.selection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Concrete implementation of {@link WizardSelectionPage}
 * 
 * @author Patrik Dufresne
 * 
 */
public class WizardListSelectionPage extends WizardSelectionPage implements ISelectionChangedListener, IDoubleClickListener {

    private static final String ICON_CATEGORY_16 = "WizardListSelectionPage.iconCategory16"; //$NON-NLS-1$

    /**
     * Path to icons
     */
    private final static String IMAGES_PATH = "images/";//$NON-NLS-1$

    static {
        JFaceResources.getImageRegistry().put(ICON_CATEGORY_16, ImageDescriptor.createFromFile(WizardListSelectionPage.class, IMAGES_PATH + "folder-16.png"));
    }

    /**
     * Content provider used for the tree viewer.
     */
    private ITreeContentProvider contentProvider = new ITreeContentProvider() {
        /**
         * Input for internal use
         */
        private Collection<IWizardNodeFactory> factories;

        @Override
        public void dispose() {
            this.factories = null;
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            String category = (String) parentElement;
            Set<IWizardNodeFactory> elements = new HashSet<IWizardNodeFactory>();
            for (IWizardNodeFactory f : factories) {
                if (category.equals(f.getCategory())) {
                    elements.add(f);
                }
            }
            return elements.toArray();
        }

        @Override
        public Object[] getElements(Object inputElement) {
            Collection<IWizardNodeFactory> factories = (Collection<IWizardNodeFactory>) inputElement;
            Set<String> elements = new HashSet<String>();
            for (IWizardNodeFactory f : factories) {
                if (f.getCategory() != null) {
                    elements.add(f.getCategory());
                }
            }
            return elements.toArray();
        }

        @Override
        public Object getParent(Object element) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof String) {
                return true;
            }
            return false;
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.factories = (Collection<IWizardNodeFactory>) newInput;
        }
    };
    /**
     * List of available wizards.
     */
    private Collection<IWizardNodeFactory> factories;
    /**
     * Message displayed on the dialog.
     */
    private String message;

    /**
     * Viewer displaying the list of wizard.
     */
    private TreeViewer viewer;

    private String emptyMessage;

    /**
     * Create a new wizard list selection page with the given list of wizard
     * factory nodes.
     * 
     * @param title
     *            the wizard page title
     * @param emptyMessage
     *            the message to be displayed when the user didn't select a
     *            node.
     * @param message
     *            a default message to be displayed.
     * @param factories
     *            a collection of wizard node factory to represent each wizard
     *            available to the user.
     */
    public WizardListSelectionPage(String title, String emptyMessage, String message, Collection<IWizardNodeFactory> factories) {
        super("wizardListSelectionPage"); //$NON-NLS-1$
        if (title == null || message == null || factories == null || factories.size() == 0) {
            throw new IllegalArgumentException();
        }
        this.emptyMessage = emptyMessage;
        this.message = message;
        this.factories = factories;
        setTitle(title);
        setMessage(this.message);
    }

    /**
     * This implementation create a list containing the available Wizard.
     */
    @Override
    public void createControl(Composite parent) {
        // create composite for page.
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout());
        comp.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label messageLabel = new Label(comp, SWT.NONE);
        messageLabel.setText(this.message);

        /*
         * Create the tree viewer
         */
        this.viewer = new TreeViewer(comp, SWT.BORDER);
        this.viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.viewer.setContentProvider(this.contentProvider);
        this.viewer.setLabelProvider(new LabelProvider() {
            @Override
            public Image getImage(Object element) {
                if (element instanceof String) {
                    return JFaceResources.getImageRegistry().get(ICON_CATEGORY_16);
                }
                return null;
            }

            @Override
            public String getText(Object element) {
                if (element instanceof IWizardNodeFactory) {
                    return ((IWizardNodeFactory) element).getName();
                }
                return super.getText(element);
            }
        });
        this.viewer.addSelectionChangedListener(this);
        this.viewer.addDoubleClickListener(this);
        this.viewer.setInput(this.factories);

        setControl(comp);
    }

    /**
     * Notify this class about user double-click in the list.
     */
    @Override
    public void doubleClick(DoubleClickEvent event) {
        selectionChanged(new SelectionChangedEvent(event.getViewer(), event.getViewer().getSelection()));
        getContainer().showPage(getNextPage());
    }

    /**
     * Notify this class about user selecting in the list.
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        setErrorMessage(null);
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        Object element = selection.getFirstElement();
        if (element == null || !(element instanceof IWizardNodeFactory)) {
            setMessage(this.emptyMessage);
            setSelectedNode(null);
            return;
        }
        IWizardNodeFactory selectedFactory = (IWizardNodeFactory) element;
        setSelectedNode(selectedFactory.createWizardNode());
        setMessage(selectedFactory.getDescription());
    }

}
