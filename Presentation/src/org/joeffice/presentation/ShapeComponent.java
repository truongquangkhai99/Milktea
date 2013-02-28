package org.joeffice.presentation;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

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
        setBorder(BorderFactory.createLineBorder(Color.RED));
        setBounds(shape.getAnchor().getBounds());
        setOpaque(false);
        setLayout(new BorderLayout());
        initComponent();
    }

    private void initComponent() {
        if (shape instanceof XSLFTextShape) {
            XSLFTextShape textShape = (XSLFTextShape) shape;
            JEditorPane textField = new JTextPane();
            textField.setText(textShape.getText());
            textField.setBorder(BorderFactory.createEmptyBorder());
            XSLFTextParagraph paragraph = textShape.getTextParagraphs().get(0);
            /*Color textColor = paragraph.getTextRuns().get(0).getFontColor();
            if (textColor != null) {
                textField.setForeground(textColor);
            }*/
            textField.getDocument().addDocumentListener(this);
            //textField.setAlignmentX(textShape.);
            add(textField, BorderLayout.CENTER);
        }
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
