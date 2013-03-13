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
package org.joeffice.wordprocessor;

import static java.awt.font.TextAttribute.*;
import static org.joeffice.desktop.actions.ParagraphAttributes.*;
import static javax.swing.text.StyleConstants.*;

import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.Enumeration;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import org.joeffice.desktop.ui.Styleable;
import org.openide.windows.TopComponent;

/**
 * Class that applies the style to the editor.
 *
 * This involves converting the {@link AttributedString} to an {@link AttributeSet} and vice versa.
 *
 * @author Anthony Goubard - Japplis
 */
public class EditorStyleable implements Styleable {

    @Override
    public void setFontAttributes(AttributedString attributes) {
        WordpTopComponent currentTopComponent = (WordpTopComponent) TopComponent.getRegistry().getActivated();
        JTextPane textPane = (JTextPane) currentTopComponent.getMainComponent();
        StyledDocument document = textPane.getStyledDocument();
        int startSelection = textPane.getSelectionStart();
        int selectionLength = textPane.getSelectionEnd() - startSelection;

        AttributedString currentAttributes = getCommonFontAttributes();

        // Mapping AttibutedString to AttributeSet
        MutableAttributeSet editorAttributes = ((StyledEditorKit) textPane.getEditorKit()).getInputAttributes();
        AttributedCharacterIterator attributesIterator = attributes.getIterator();
        boolean changeParagraph = false;
        for (Attribute attribute : attributesIterator.getAllAttributeKeys()) {
            Object value = attributesIterator.getAttribute(attribute);
            changeParagraph |= attribute == JUSTIFICATION | attribute == ALIGNMENT;
            addAttribute(attribute, value, editorAttributes, currentAttributes);
        }

        document.setCharacterAttributes(startSelection, selectionLength, editorAttributes, false);
        if (changeParagraph) {
            cleanCharacterAttributes(editorAttributes);
            document.setParagraphAttributes(startSelection, selectionLength, editorAttributes, false);
        }
    }

    /**
     * Add the attribute as defined in {@link AttributedString} to the {@link MutableAttributeSet} for the JTextPane.
     *
     * @see java.awt.font.TextAttribute
     */
    protected void addAttribute(Attribute attribute, Object attributeValue, MutableAttributeSet editorAttributes, AttributedString currentAttributes) {
        if (attribute == FAMILY) {
            editorAttributes.addAttribute(Family, attributeValue);
        } else if (attribute == FOREGROUND) {
            editorAttributes.addAttribute(Foreground, attributeValue);
        } else if (attribute == BACKGROUND) {
            editorAttributes.addAttribute(Background, attributeValue);
        } else if (attribute == WEIGHT) {
            boolean bold = attributeValue == WEIGHT_BOLD && !hasAttribute(currentAttributes, attribute, WEIGHT_BOLD);
            editorAttributes.addAttribute(Bold, bold);
        } else if (attribute == UNDERLINE) {
            boolean italic = attributeValue == UNDERLINE_ON && !hasAttribute(currentAttributes, attribute, UNDERLINE_ON);
            editorAttributes.addAttribute(Underline, italic);
        } else if (attribute == POSTURE) {
            boolean underlined = attributeValue == POSTURE_OBLIQUE && !hasAttribute(currentAttributes, attribute, POSTURE_OBLIQUE);
            editorAttributes.addAttribute(Italic, underlined);
        } else if (attribute == SIZE) {
            editorAttributes.addAttribute(FontSize, attributeValue);
        } else if (attribute == JUSTIFICATION) {
            boolean justified = attributeValue == JUSTIFICATION_FULL && !hasAttribute(currentAttributes, attribute, JUSTIFICATION_FULL);
            editorAttributes.addAttribute(Alignment, justified ? ALIGN_JUSTIFIED : ALIGN_LEFT);
        } else if (attribute == ALIGNMENT) {
            editorAttributes.addAttribute(Alignment, attributeValue);
        }
    }

    private boolean hasAttribute(AttributedString attributes, AttributedCharacterIterator.Attribute attribute, Object expectedValue) {
        AttributedCharacterIterator values = attributes.getIterator(new Attribute[]{attribute});
        boolean hasAttribute = values.getAttribute(attribute) != null;
        if (expectedValue != null && hasAttribute) {
            hasAttribute &= values.getAttribute(attribute).equals(expectedValue);
        }
        return hasAttribute;
    }

    private void cleanCharacterAttributes(MutableAttributeSet editorAttributes) {
        editorAttributes.removeAttribute(Family);
        editorAttributes.removeAttribute(FontFamily);
        editorAttributes.removeAttribute(Bold);
        editorAttributes.removeAttribute(Italic);
        editorAttributes.removeAttribute(Underline);
        editorAttributes.removeAttribute(Foreground);
        editorAttributes.removeAttribute(Background);
    }

    @Override
    public AttributedString getCommonFontAttributes() {
        WordpTopComponent currentTopComponent = (WordpTopComponent) TopComponent.getRegistry().getActivated();
        JTextPane textPane = (JTextPane) currentTopComponent.getMainComponent();
        MutableAttributeSet editorAttributes = textPane.getInputAttributes();
        AttributedString commonAttributes = new AttributedString("Selection");
        Enumeration attributes = editorAttributes.getAttributeNames();
        while (attributes.hasMoreElements()) {
            Object nextAttribute = attributes.nextElement();
            Object attributeValue = editorAttributes.getAttribute(nextAttribute);
            addAttribute(attributeValue, attributeValue, commonAttributes);
        }
        return commonAttributes;
    }

    /**
     * Add the attribute as defined in the JTextPane to the {@link AttributedString}.
     *
     * @see StyleConstants
     */
    protected void addAttribute(Object attribute, Object value, AttributedString attributes) {
        if (attribute == Italic && ((Boolean) value).booleanValue()) {
            attributes.addAttribute(POSTURE, POSTURE_OBLIQUE);
        } else if (attribute == Bold && ((Boolean) value).booleanValue()) {
            attributes.addAttribute(WEIGHT, WEIGHT_BOLD);
        } else if (attribute == Underline && ((Boolean) value).booleanValue()) {
            attributes.addAttribute(UNDERLINE, UNDERLINE_ON);
        } else if (attribute == Family) {
            attributes.addAttribute(FAMILY, value);
        } else if (attribute == Size) {
            attributes.addAttribute(SIZE, value);
        } else if (attribute == Foreground) {
            attributes.addAttribute(FOREGROUND, value);
        } else if (attribute == Background) {
            attributes.addAttribute(BACKGROUND, value);
        } else if (attribute == Alignment) {
            if (value == ALIGN_JUSTIFIED) {
                attributes.addAttribute(JUSTIFICATION, JUSTIFICATION_FULL);
            } else {
                attributes.addAttribute(ALIGNMENT, value);
            }
        }
    }
}
