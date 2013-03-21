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
package org.joeffice.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which the slides in a list as thumbnails.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.presentation//SlideThumbnails//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "SlideThumbnailsTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "commonpalette", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.presentation.SlideThumbnailsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SlideThumbnailsAction",
        preferredID = "SlideThumbnailsTopComponent")
@Messages({
    "CTL_SlideThumbnailsAction=SlideThumbnails",
    "CTL_SlideThumbnailsTopComponent=SlideThumbnails Window",
    "HINT_SlideThumbnailsTopComponent=This is a SlideThumbnails window"
})
public final class SlideThumbnailsTopComponent extends TopComponent implements ListSelectionListener {

    private JList slidesList;

    public SlideThumbnailsTopComponent() {
        initComponents();
        setName(Bundle.CTL_SlideThumbnailsTopComponent());
        setToolTipText(Bundle.HINT_SlideThumbnailsTopComponent());
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);

    }

    private void initComponents() {
        setLayout(new BorderLayout());
        SlidesTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SlidesTopComponent.class);
        if (currentTopComponent == null) return;
        XSLFSlide[] slides = currentTopComponent.getPresentation().getSlides();
        slidesList = new JList(slides);
        slidesList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                XSLFSlide slide = (XSLFSlide) value;
                JComponent renderer = new SlideComponent(slide, null, new Dimension(200, 160));
                if (isSelected) {
                    renderer.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                return renderer;
            }
        });
        slidesList.setFocusable(false);
        slidesList.addListSelectionListener(this);
        add(new JScrollPane(slidesList));
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        SlidesTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SlidesTopComponent.class);
        if (currentTopComponent != null) {
            currentTopComponent.setSelectedSlide(slidesList.getSelectedIndex());
        }
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }
}
