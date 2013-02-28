package org.joeffice.presentation;

import static org.apache.poi.xslf.usermodel.TextAlign.CENTER;
import static org.apache.poi.xslf.usermodel.TextAlign.JUSTIFY;
import static org.apache.poi.xslf.usermodel.TextAlign.RIGHT;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
            XSLFTextShape textShape = (XSLFTextShape) shape;
            JTextPane textField = new JTextPane();
            textField.setBorder(BorderFactory.createEmptyBorder());
            String text = textShape.getText();
            if (!textShape.getTextParagraphs().isEmpty()) {
                XSLFTextParagraph paragraph = textShape.getTextParagraphs().get(0);
                applyFontAttributes(paragraph, textField);
                applyAlignment(paragraph, textField);
                String simpleBullet = getBullet(paragraph);
                if (!text.isEmpty()) {
                    text = simpleBullet + text;
                }
            }
            textField.setText(text);

            add(textField, BorderLayout.CENTER);
            textField.getDocument().addDocumentListener(this);
        }
    }

    private void applyFontAttributes(XSLFTextParagraph paragraph, JEditorPane textField) {
        try {
            XSLFTextRun textAttributes = paragraph.getTextRuns().get(0);
            Font textFont = textField.getFont();
            String fontFamily = textAttributes.getFontFamily();
            if (fontFamily != null) {
                textFont = new Font(fontFamily, Font.PLAIN, 21); // 10.5 points
            }
            Color textColor = textAttributes.getFontColor();
            if (textColor != null) {
                textField.setForeground(textColor);
            }
            double fontSize = textAttributes.getFontSize();
            if (fontSize > 0) {
                textFont = textFont.deriveFont((float) fontSize);
            }
            boolean italic = textAttributes.isItalic();
            if (italic) {
                textFont = textFont.deriveFont(Font.ITALIC);
            }
            boolean bold = textAttributes.isBold();
            if (bold) {
                textFont = textFont.deriveFont(Font.BOLD);
            }
            textField.setFont(textFont);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    @Override
    public void paintComponent(Graphics g) {
        // The draw and drawContent methods don't display anything
        if (shape instanceof XSLFTextShape) {
            super.paintComponent(g);
        } else {
            shape.draw((Graphics2D) g);
        }
    }

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
