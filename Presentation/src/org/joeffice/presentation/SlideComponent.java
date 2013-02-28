package org.joeffice.presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.poi.xslf.usermodel.XSLFBackground;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * A component show one slide.
 *
 * @author Anthony Goubard - Japplis
 */
public class SlideComponent extends JPanel {

    private XSLFSlide slide;

    public SlideComponent(XSLFSlide slide) {
        this.slide = slide;
        initComponent();
    }

    private void initComponent() {
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        XSLFBackground background = slide.getBackground();
        if (background != null) {
            Color backgroundColor = background.getFillColor();
            setBackground(backgroundColor);
        } else {
            setBackground(Color.WHITE);
        }
        XSLFTextShape[] textShapes = slide.getPlaceholders();
        for (XSLFTextShape textShape : textShapes) {
            ShapeComponent shapeComponent = new ShapeComponent(textShape);
            //JComponent shapeComponent = new JLabel(textShape.getText());
            add(shapeComponent);
        }
        XSLFShape[] shapes = slide.getShapes();
        for (XSLFShape shape : shapes) {
            ShapeComponent shapeComponent = new ShapeComponent(shape);
            add(shapeComponent);
        }
    }
}