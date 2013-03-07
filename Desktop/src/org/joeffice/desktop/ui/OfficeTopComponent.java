package org.joeffice.desktop.ui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.jdesktop.swingx.scrollpaneselector.ScrollPaneSelector;

import org.joeffice.desktop.file.OfficeDataObject;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.windows.CloneableTopComponent;

/**
 * Generic TopComponent to show the opened documents.
 *
 * @author Anthony Goubard - Japplis
 */
public abstract class OfficeTopComponent extends CloneableTopComponent {

    private OfficeDataObject dataObject;

    private JComponent mainComponent;

    /**
     * Empty constructor used for (de)serialization.
     */
    public OfficeTopComponent() {
    }

    public OfficeTopComponent(OfficeDataObject dataObject) {
        this.dataObject = dataObject;
        init();
    }

    public OfficeDataObject getDataObject() {
        return dataObject;
    }

    protected void init() {
        initComponents();
        FileObject docxFileObject = dataObject.getPrimaryFile();
        String fileDisplayName = FileUtil.getFileDisplayName(docxFileObject);
        setToolTipText(fileDisplayName);
        setName(docxFileObject.getName());
        loadDocument();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    protected void initComponents() {
        setLayout(new BorderLayout());
        JToolBar topToolbar = createToolbar();
        mainComponent = createMainComponent();
        JScrollPane mainPane = new JScrollPane(mainComponent);
        mainPane.getVerticalScrollBar().setUnitIncrement(16);
        ScrollPaneSelector.installScrollPaneSelector(mainPane);

        add(topToolbar, BorderLayout.NORTH);
        add(mainPane);
    }

    private JToolBar createToolbar() {
        JToolBar editorToolbar = new JToolBar();
        return editorToolbar;
    }

    protected abstract JComponent createMainComponent();

    public JComponent getMainComponent() {
        return mainComponent;
    }

    protected abstract void loadDocument();

    @Override
    protected CloneableTopComponent createClonedObject() {
        try {
            // Use reflection
            Constructor componentContructor = getClass().getConstructor(OfficeDataObject.class);
            Object newComponent = componentContructor.newInstance(dataObject);
            return (CloneableTopComponent) newComponent;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    protected boolean closeLast() {
        int answer = OfficeUIUtils.checkSaveBeforeClosing(dataObject, this);
        boolean canClose = answer == JOptionPane.YES_OPTION || answer == JOptionPane.NO_OPTION;
        if (canClose && dataObject != null) {
            dataObject.setContent(null);
        }
        return canClose;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    public void setModified(boolean modified) {
        dataObject.setModified(modified);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("------- office readExternal");
        super.readExternal(in);
        dataObject = (OfficeDataObject) in.readObject();
        init();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("------- office writeExternal");
        super.writeExternal(out);
        out.writeObject(getDataObject());
    }

    /*@Override
    public Object writeReplace() {
        System.out.println("------- office writeReplace");
        return new ResolvableHelper();
    }

    final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;
        private final OfficeDataObject dataObject;

        ResolvableHelper() {
            this.dataObject = OfficeTopComponent.this.dataObject;
        }

        public Object readResolve() {
            System.out.println("------- office readResolve");
            OfficeTopComponent.this.dataObject = this.dataObject;
            OfficeTopComponent topComponent = (OfficeTopComponent) cloneTopComponent();
            return topComponent;
        }
    }*/
}
