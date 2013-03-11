/*
 * Copyright 2013 Japplis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joeffice.desktop.ui;

import java.awt.BorderLayout;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.jdesktop.swingx.scrollpaneselector.ScrollPaneSelector;

import org.joeffice.desktop.file.OfficeDataObject;
import org.openide.awt.UndoRedo;
import org.openide.explorer.ExplorerUtils;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.CloneableTopComponent;

/**
 * Generic TopComponent to show the opened documents.
 *
 * @author Anthony Goubard - Japplis
 */
public abstract class OfficeTopComponent extends CloneableTopComponent {

    private JComponent mainComponent;
    private UndoRedo.Manager manager = new UndoRedo.Manager();
    private InstanceContent services;
    private Lookup servicesLookup;

    /**
     * Empty constructor used for (de)serialization.
     */
    public OfficeTopComponent() {
    }

    public OfficeTopComponent(OfficeDataObject dataObject) {
        init(dataObject);
    }

    public OfficeDataObject getDataObject() {
        OfficeDataObject dataObject = getLookup().lookup(OfficeDataObject.class);
        return dataObject;
    }

    protected void init(OfficeDataObject dataObject) {
        associateLookup(dataObject.getLookup());
        services = new InstanceContent();
        servicesLookup = new AbstractLookup(services);

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
        if (mainComponent instanceof JScrollPane || mainComponent instanceof JTabbedPane) {
            add(mainComponent);
        } else {
            JScrollPane mainPane = new JScrollPane(mainComponent);
            mainPane.getVerticalScrollBar().setUnitIncrement(16);
            ScrollPaneSelector.installScrollPaneSelector(mainPane);
            add(mainPane);
        }

        add(topToolbar, BorderLayout.NORTH);
    }

    protected JToolBar createToolbar() {
        JToolBar editorToolbar = new JToolBar();
        return editorToolbar;
    }

    protected abstract JComponent createMainComponent();

    public JComponent getMainComponent() {
        return mainComponent;
    }

    protected abstract void loadDocument();

    public InstanceContent getServices() {
        return services;
    }

    @Override
    protected CloneableTopComponent createClonedObject() {
        try {
            // Use reflection
            Constructor componentContructor = getClass().getConstructor(OfficeDataObject.class);
            Object newComponent = componentContructor.newInstance(getDataObject());
            return (CloneableTopComponent) newComponent;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    // http://wiki.netbeans.org/DevFaqEditorTopComponent
    @Override
    public String getHtmlDisplayName() {
        DataObject dob = getLookup().lookup(DataObject.class);
        if (dob != null && dob.isModified()) {
            return "<html><body><b>" + dob.getName() + "</b></body></html>";
        }
        return super.getHtmlDisplayName();
    }

    @Override
    protected boolean closeLast() {
        OfficeDataObject dataObject = getDataObject();
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
        getDataObject().setModified(modified);
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(super.getLookup(), servicesLookup);
    }

    @Override
    public UndoRedo getUndoRedo() {
        return manager;
    }

    public void writeProperties(Properties properties) {
        properties.setProperty("version", "1.0");
        File closingFile = FileUtil.toFile(getDataObject().getPrimaryFile());
        properties.setProperty("path", closingFile.getAbsolutePath());
    }

    public void readProperties(Properties properties) {
        String version = properties.getProperty("version");
        try {
            String path = properties.getProperty("path");
            File openingFile = FileUtil.normalizeFile(new File(path));
            FileObject openingFileObject = FileUtil.toFileObject(openingFile);
            OfficeDataObject openingDataObject = (OfficeDataObject) DataObject.find(openingFileObject);
            init(openingDataObject);

            // If the file has moved or has been deleted
        } catch (DataObjectNotFoundException ex) {
            close();
        }
    }
}
