package org.joeffice.wordprocessor.nb;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.text.CloneableEditor;
import org.openide.text.CloneableEditorSupport;
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
        iconBase = "org/joeffice/wordprocessor/wordp-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.wordprocessor.WordpTopComponent")
/*@ActionReference(path = "Menu/Window")*/
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_WordpAction",
        preferredID = "WordpTopComponent")
@Messages({
    "CTL_WordpAction=Wordp",
    "CTL_WordpTopComponent=Word processor Window",
    "HINT_WordpTopComponent=This is a Word processor window"
})
public final class WordpTopComponent extends CloneableEditor implements DocumentListener {

    public WordpTopComponent() {
    }

    public WordpTopComponent(CloneableEditorSupport support) {
        super(support);
    }

    public WordpTopComponent(CloneableEditorSupport support, boolean associateLookup) {
        super(support, associateLookup);
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
    public void insertUpdate(DocumentEvent de) {
        changedUpdate(de);
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        changedUpdate(de);
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        // nothing yet
    }
}
