package org.joeffice.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.openide.util.Exceptions;

/**
 * Component that displays a presentation shape.
 *
 * @author Anthony Goubard - Japplis
 */
public class ShapeComponent extends JPanel implements DocumentListener {

    private XSLFShape shape;

    public ShapeComponent(XSLFShape shape) {
        this.shape = shape;
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
            Color textColor = textShape.getFillColor();
            if (textColor != null) {
                textField.setForeground(textColor);
            }
            textField.getDocument().addDocumentListener(this);
            //textField.setAlignmentX(textShape.);
            add(textField, BorderLayout.CENTER);
        }
    }

    /*@Override
    public void paintComponent(Graphics g) {
        // The draw and drawContent methods don't display anything
        if (shape instanceof XSLFSimpleShape) {
            ((XSLFSimpleShape) shape).drawContent((Graphics2D) g);
        } else {
            shape.draw((Graphics2D) g);
        }
    }*/

    @Override
    public Dimension getPreferredSize() {
        Rectangle2D bounds = shape.getAnchor();
        return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
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
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
