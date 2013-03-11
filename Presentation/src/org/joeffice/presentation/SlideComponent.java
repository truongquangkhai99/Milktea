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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

import org.apache.poi.xslf.usermodel.XSLFBackground;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 * A component to show one slide.
 *
 * @author Anthony Goubard - Japplis
 */
public class SlideComponent extends JPanel {

    private XSLFSlide slide;

    private SlidesTopComponent slidesComponent;

    private double scale;

    public SlideComponent(XSLFSlide slide, SlidesTopComponent slidesComponent) {
        this(slide, slidesComponent, new Dimension(1280, 720));
    }

    public SlideComponent(XSLFSlide slide, SlidesTopComponent slidesComponent, Dimension maxSize) {
        this.slide = slide;
        this.slidesComponent = slidesComponent;

        Rectangle2D backgroundSize = slide.getBackground().getAnchor();
        double scaleX = maxSize.getWidth() / backgroundSize.getWidth();
        double scaleY = maxSize.getHeight() / backgroundSize.getHeight();
        scale = Math.min(scaleX, scaleY);
        Dimension preferredSize = new Dimension((int) (backgroundSize.getWidth() * scale), (int) (backgroundSize.getHeight() * scale));
        setPreferredSize(preferredSize);
        initComponent();
    }

    private void initComponent() {
        setLayout(null);
        //setOpaque(false);
        XSLFBackground background = slide.getBackground();
        /*ShapeComponent backgroundShape = new ShapeComponent(background, this);
        backgroundShape.setOpaque(false);
        add(backgroundShape);*/
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*XSLFBackground background = slide.getBackground();
        ShapeComponent backgroundShape = new ShapeComponent(background, this);
        backgroundShape.paintAll(g);*/
        /*XSLFBackground background = slide.getBackground();
            BufferedImage img = new BufferedImage((int) (background.getAnchor().getWidth() * scale), (int) (background.getAnchor().getHeight() * scale), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = img.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            //graphics.translate(-background.getAnchor().getX() * scale, -background.getAnchor().getY() * scale);
            //graphics.scale(scale, scale);
            background.draw(graphics);*/
    }

    public double getScale() {
        return scale;
    }

    public XSLFSlide getSlide() {
        return slide;
    }

    public SlidesTopComponent getSlidesComponent() {
        return slidesComponent;
    }
}
