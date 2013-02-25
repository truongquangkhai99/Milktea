/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.wordprocessor;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@MultiViewElement.Registration(
        displayName = "#LBL_Docx_VISUAL",
        iconBase = "org/joeffice/wordprocessor/wordp-16.png",
        mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "DocxVisual",
        position = 2000)
@Messages("LBL_Docx_VISUAL=Visual")
public final class DocxVisualElement extends JPanel implements MultiViewElement {

    private DocxDataObject obj;
    private JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;

    public DocxVisualElement(Lookup lkp) {
        obj = lkp.lookup(DocxDataObject.class);
        assert obj != null;
        initComponents();
    }

    @Override
    public String getName() {
        return "DocxVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        // todo
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
}
