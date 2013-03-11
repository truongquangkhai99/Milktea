package org.joeffice.wordprocessor;

import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import org.joeffice.desktop.ui.Styleable;
import org.openide.windows.TopComponent;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class EditorStyleable implements Styleable {

    @Override
    public void setFontAttributes(AttributedString attributes) {
        WordpTopComponent currentTopComponent  = (WordpTopComponent) TopComponent.getRegistry().getActivated();
        JTextPane textPane = (JTextPane) currentTopComponent.getMainComponent();
        StyledDocument document = textPane.getStyledDocument();

        // Mapping AttibutedString to AttributeSet
        MutableAttributeSet editorAttributes = ((StyledEditorKit) textPane.getEditorKit()).getInputAttributes();
        AttributedCharacterIterator attributesIterator = attributes.getIterator();
        for (Attribute attribute : attributesIterator.getAllAttributeKeys()) {
            if (attribute == TextAttribute.FAMILY) {
                String fontName = (String) attributesIterator.getAttribute(attribute);
                editorAttributes.addAttribute(StyleConstants.Family, fontName);
            }
        }

        int startSelection = textPane.getSelectionStart();
        int selectionLength = textPane.getSelectionEnd() - startSelection;
        document.setCharacterAttributes(startSelection, selectionLength, editorAttributes, false);
    }

    @Override
    public AttributedString getCommonFontAttributes() {
        return new AttributedString("Test");
    }

}
