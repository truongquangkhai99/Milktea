/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.wordprocessor;

import java.awt.BorderLayout;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays the docx documents.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.wordprocessor//Wordp//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "WordpTopComponent",
        iconBase="org/joeffice/wordprocessor/wordp-16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "org.joeffice.wordprocessor.WordpTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_WordpAction",
        preferredID = "WordpTopComponent")
@Messages({
    "CTL_WordpAction=Wordp",
    "CTL_WordpTopComponent=Word processor Window",
    "HINT_WordpTopComponent=This is a Word processor window"
})
public final class WordpTopComponent extends TopComponent implements LookupListener {

    public WordpTopComponent() {
        initComponents();
        setName(Bundle.CTL_WordpTopComponent());
        setToolTipText(Bundle.HINT_WordpTopComponent());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        setLayout(new BorderLayout());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void resultChanged(LookupEvent le) {

    }
}
