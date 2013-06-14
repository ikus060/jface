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
package com.patrikdufresne.jface.viewers;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

// TODO This cell editor doesn't work properly. An alternative implementation
// should be created using the Observable / databinding framework.
public class TextProposalViewerCellEditor extends TextCellEditor {

    /**
     * Default ComboBoxCellEditor style
     */
    private static final int defaultStyle = SWT.NONE;

    private boolean popupOpen = false; // true, if popup is currently open

    private TextProposalViewer viewer;

    /**
     * Creates a new cell editor with a combo viewer and a default style
     * 
     * @param parent
     *            the parent control
     */
    public TextProposalViewerCellEditor(Composite parent) {
        this(parent, defaultStyle);
    }

    /**
     * Creates a new cell editor with a combo viewer and the given style
     * 
     * @param parent
     *            the parent control
     * @param style
     *            the style bits
     */
    public TextProposalViewerCellEditor(Composite parent, int style) {
        super(parent, style);
        setValueValid(true);
    }

    @Override
    protected Control createControl(Composite parent) {
        Control ctrl = super.createControl(parent);
        if (ctrl instanceof Text) {
            Text text = (Text) ctrl;
            this.viewer = new TextProposalViewer(text);

            this.viewer.getContentProposalAdapter().addContentProposalListener(new IContentProposalListener2() {
                @Override
                public void proposalPopupClosed(ContentProposalAdapter adapter) {
                    TextProposalViewerCellEditor.this.popupOpen = false;
                }

                @Override
                public void proposalPopupOpened(ContentProposalAdapter adapter) {
                    TextProposalViewerCellEditor.this.popupOpen = true;
                }
            });
        }
        return ctrl;
    }

    @Override
    protected boolean dependsOnExternalFocusListener() {
        // Always return false;
        // Otherwise, the ColumnViewerEditor will install an additional focus
        // listener
        // that cancels cell editing on focus lost, even if focus gets lost due
        // to
        // activation of the completion proposal popup. See also bug 58777.
        return false;
    }

    /**
     * The <code>TextFieldCellEditor</code> implementation of this
     * <code>CellEditor</code> framework method returns the zero-based index of
     * the current selection.
     * 
     * @return the zero-based index of the current selection wrapped as an
     *         <code>Integer</code>
     */
    @Override
    protected Object doGetValue() {
        Assert.isTrue(this.viewer != null);
        if (this.viewer.getSelection() instanceof StructuredSelection) {
            return ((StructuredSelection) this.viewer.getSelection()).getFirstElement();
        }
        return null;
    }

    /**
     * Set a new value
     * 
     * @param value
     *            the new value
     */
    protected void doSetValue(Object value) {
        Assert.isTrue(this.viewer != null);
        if (value == null) {
            this.viewer.setSelection(StructuredSelection.EMPTY);
        } else {
            this.viewer.setSelection(new StructuredSelection(value));
        }
    }

    @Override
    protected void focusLost() {
        if (!this.popupOpen) {
            // Focus lost deactivates the cell editor.
            // This must not happen if focus lost was caused by activating
            // the completion proposal popup.
            super.focusLost();
        }
    }

    public TextProposalViewer getViewer() {
        return this.viewer;
    }

    /**
     * @param provider
     *            the content provider used
     * @see StructuredViewer#setContentProvider(IContentProvider)
     */
    public void setContentProvider(IStructuredContentProvider provider) {
        this.viewer.setContentProvider(provider);
    }

    /**
     * @param input
     *            the input used
     * @see StructuredViewer#setInput(Object)
     */
    public void setInput(Object input) {
        this.viewer.setInput(input);
    }

    /**
     * @param labelProvider
     *            the label provider used
     * @see StructuredViewer#setLabelProvider(IBaseLabelProvider)
     */
    public void setLabelProvider(IBaseLabelProvider labelProvider) {
        this.viewer.setLabelProvider(labelProvider);
    }

    /**
     * Sets the viewer comparator.
     * 
     * @param comparator
     *            the comparator or null
     */
    public void setComparator(ViewerComparator comparator) {
        this.viewer.setComparator(comparator);
    }
}
