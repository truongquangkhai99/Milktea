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

import org.openide.util.Exceptions;

/**
 * Component that displays a presentation shape.
 *
 * @author Anthony Goubard - Japplis
 */
public class ShapeComponent extends JPanel implements DocumentListener {

    private XSLFShape shape;
    private SlideComponent slideComponent;

    public ShapeComponent(XSLFShape shape, SlideComponent slideComponent) {
        this.shape = shape;
        this.slideComponent = slideComponent;
        setBorder(BorderFactory.createLineBorder(Color.RED)); // for debug 
        setBounds(shape.getAnchor().getBounds());
        setOpaque(false);
        setLayout(new BorderLayout());
        initComponent();
    }

    private void initComponent() {
        if (shape instanceof XSLFTextShape) {
            handleTextShape((XSLFTextShape) shape);
        } else {
            BufferedImage img = new BufferedImage((int) shape.getAnchor().getWidth(), (int) shape.getAnchor().getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = img.createGraphics();
            shape.draw(graphics);
            graphics.dispose();
            JLabel shapeLabel = new JLabel(new ImageIcon(img));
            add(shapeLabel);
        }
        // else paintComponent should display the shape (but it doesn't work)
    }

    private void handleTextShape(XSLFTextShape textShape) {
        JTextPane textField = new JTextPane();
        textField.setBorder(BorderFactory.createEmptyBorder());
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

        add(textField, BorderLayout.CENTER);
        textField.getDocument().addDocumentListener(this);
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

    /*@Override
    public void paintComponent(Graphics g) {
        // The draw and drawContent methods don't display anything
        if (shape instanceof XSLFTextShape) {
            super.paintComponent(g);
        } else {
            shape.draw((Graphics2D) g);
        }
    }*/

    @Override
    public Dimension getPreferredSize() {
        Rectangle2D bounds = shape.getAnchor();
        return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
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
