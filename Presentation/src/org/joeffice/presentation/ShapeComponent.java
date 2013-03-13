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

import static org.apache.poi.xslf.usermodel.TextAlign.CENTER;
import static org.apache.poi.xslf.usermodel.TextAlign.JUSTIFY;
import static org.apache.poi.xslf.usermodel.TextAlign.RIGHT;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.poi.xslf.usermodel.*;
import org.openide.awt.UndoRedo;

import org.openide.util.Exceptions;

/**
 * Component that displays a presentation shape.
 *
 * @author Anthony Goubard - Japplis
 */
public class ShapeComponent extends JPanel implements DocumentListener {

    private XSLFShape shape;
    private SlideComponent slideComponent;
    private boolean editable;

    public ShapeComponent(XSLFShape shape, SlideComponent slideComponent) {
        this.shape = shape;
        this.slideComponent = slideComponent;
        setOpaque(false);
        // setBorder(BorderFactory.createLineBorder(Color.RED)); // for debug

        Rectangle shapeBounds = shape.getAnchor().getBounds();
        double scale = slideComponent.getScale();
        Rectangle.Double scaledBounds = new Rectangle.Double(shapeBounds.x * scale, shapeBounds.y * scale, shapeBounds.width * scale, shapeBounds.height * scale);
        setBounds(scaledBounds.getBounds());

        setOpaque(false);
        setLayout(new BorderLayout());
        editable = slideComponent.getSlidesComponent() != null;
        initComponent();
    }

    private void initComponent() {
        if (shape instanceof XSLFTextShape && !"".equals(((XSLFTextShape) shape).getText().trim())) {
            handleTextShape((XSLFTextShape) shape);
        } else {

            double scale = slideComponent.getScale();
            BufferedImage img = shapeToImage(shape, scale);
            JLabel shapeLabel = new JLabel(new ImageIcon(img));
            // shapeLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE)); // for debug
            add(shapeLabel);
        }
    }

    public static BufferedImage shapeToImage(XSLFShape shape, double scale) {
        BufferedImage img = new BufferedImage((int) (shape.getAnchor().getWidth() * scale), (int) (shape.getAnchor().getHeight() * scale), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = img.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.translate(-shape.getAnchor().getX() * scale, -shape.getAnchor().getY() * scale);
        graphics.scale(scale, scale);
        shape.draw(graphics);
        graphics.dispose();
        return img;
    }

    private void handleTextShape(XSLFTextShape textShape) {
        JTextPane textField = new JTextPane();
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setOpaque(false);
        java.util.List<XSLFTextParagraph> paragraphs = textShape.getTextParagraphs();
        boolean newLine = false;
        for (XSLFTextParagraph paragraph : paragraphs) {
            applyAlignment(paragraph, textField);
            java.util.List<XSLFTextRun> textParts = paragraph.getTextRuns();
            for (XSLFTextRun textPart : textParts) {
                try {
                    String text = textPart.getText();
                    AttributeSet attributes = getFontAttributes(textPart);
                    String simpleBullet = getBullet(paragraph);
                    if (!text.isEmpty()) {
                        text = simpleBullet + text;
                    }
                    if (newLine) {
                        text = "\r\n" + text;
                    }
                    int documentLength = textField.getDocument().getLength();
                    textField.getDocument().insertString(documentLength, text, attributes);
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            newLine = true;
        }

        add(textField);
        if (editable) {
            textField.getDocument().addDocumentListener(this);
            textField.getDocument().addUndoableEditListener((UndoRedo.Manager) getSlideComponent().getSlidesComponent().getUndoRedo());
        } else {
            textField.setEditable(false);
        }
    }

    private AttributeSet getFontAttributes(XSLFTextRun textPart) {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        try {
            String fontFamily = textPart.getFontFamily();
            if (fontFamily != null) {
                StyleConstants.setFontFamily(attributes, fontFamily);
            }
            Color textColor = textPart.getFontColor();
            if (textColor != null) {
                StyleConstants.setForeground(attributes, textColor);
            }
            double fontSize = textPart.getFontSize();
            if (fontSize > 0) {
                StyleConstants.setFontSize(attributes, (int) fontSize);
            }
            boolean italic = textPart.isItalic();
            if (italic) {
                StyleConstants.setItalic(attributes, true);
            }
            boolean bold = textPart.isBold();
            if (bold) {
                StyleConstants.setBold(attributes, true);
            }
            boolean underlined = textPart.isUnderline();
            if (underlined) {
                StyleConstants.setUnderline(attributes, true);
            }
            boolean strikeThrough = textPart.isStrikethrough();
            if (strikeThrough) {
                StyleConstants.setStrikeThrough(attributes, true);
            }
            boolean subScript = textPart.isSubscript();
            if (subScript) {
                StyleConstants.setSubscript(attributes, true);
            }
            boolean superScript = textPart.isSuperscript();
            if (superScript) {
                StyleConstants.setSuperscript(attributes, true);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
        }
        return attributes;
    }

    private void applyAlignment(XSLFTextParagraph paragraph, JTextPane textField) {
        try {
            TextAlign alignment = paragraph.getTextAlign();
            switch (alignment) {
                case CENTER:
                    align(textField, StyleConstants.ALIGN_CENTER);
                    break;
                case RIGHT:
                    align(textField, StyleConstants.ALIGN_RIGHT);
                    break;
                case LEFT:
                    align(textField, StyleConstants.ALIGN_LEFT);
                    break;
                case JUSTIFY:
                case JUSTIFY_LOW:
                    align(textField, StyleConstants.ALIGN_JUSTIFIED);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void align(JTextPane textField, int swingAlignment) {
        StyledDocument doc = textField.getStyledDocument();
        SimpleAttributeSet alignmentAttibute = new SimpleAttributeSet();
        StyleConstants.setAlignment(alignmentAttibute, swingAlignment);
        doc.setParagraphAttributes(0, doc.getLength(), alignmentAttibute, false);
    }

    private String getBullet(XSLFTextParagraph paragraph) {
        if (paragraph.isBullet()) {
            return paragraph.getBulletCharacter();
        }
        return ""; // No bullets
    }

    public SlideComponent getSlideComponent() {
        return slideComponent;
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
        try {
            ((XSLFTextShape) shape).setText(de.getDocument().getText(0, de.getDocument().getLength()));
            getSlideComponent().getSlidesComponent().setModified(true);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
