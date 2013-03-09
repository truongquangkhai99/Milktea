package org.joeffice.presentation;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

import org.apache.poi.xslf.usermodel.XSLFBackground;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * A component to show one slide.
 *
 * @author Anthony Goubard - Japplis
 */
public class SlideComponent extends JPanel {

    private XSLFSlide slide;

    private SlidesTopComponent slidesComponent;

    public SlideComponent(XSLFSlide slide, SlidesTopComponent slidesComponent) {
        this(slide, slidesComponent, new Dimension(1280, 720));
    }

    public SlideComponent(XSLFSlide slide, SlidesTopComponent slidesComponent, Dimension slideSize) {
        this.slide = slide;
        this.slidesComponent = slidesComponent;
        setPreferredSize(slideSize);
        initComponent();
    }

    private void initComponent() {
        setLayout(null);
        XSLFBackground background = slide.getBackground();
        if (background != null) {
            Color backgroundColor = background.getFillColor();
            setBackground(backgroundColor);
        } else {
            setBackground(Color.WHITE);
        }
        XSLFShape[] shapes = slide.getShapes();
        for (XSLFShape shape : shapes) {
            ShapeComponent shapeComponent = new ShapeComponent(shape, this);
            add(shapeComponent);
        }
    }

    public XSLFSlide getSlide() {
        return slide;
    }

    public SlidesTopComponent getSlidesComponent() {
        return slidesComponent;
    }
}
