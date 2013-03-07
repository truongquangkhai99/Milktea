package org.joeffice.presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JSeparator;

/**
 * A fancy(?) separator between 2 slides.
 *
 * @author Anthony Goubard - Japplis
 */
public class SlideSeparator extends JSeparator {

    private boolean startSeparator;
    private boolean endSeparator;

    public SlideSeparator() {
        super(JSeparator.HORIZONTAL);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension separatorDimension = super.getPreferredSize();
        Component[] parentComponents = getParent().getComponents();
        int heigth = 32;
        startSeparator = parentComponents.length > 0 && parentComponents[0] == this;
        endSeparator = parentComponents.length > 0 && parentComponents[parentComponents.length - 1] == this;
        if (startSeparator || endSeparator) {
            heigth = heigth / 2;
        }
        return new Dimension(separatorDimension.width, heigth);
    }

    @Override
    public void paintComponent(Graphics g2) {
        int rectY = 0;
        if (!startSeparator) {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, rectY, getWidth(), 6);
            rectY = 6;
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, rectY, getWidth(), 10);
            rectY += 10;
        }
        if (!endSeparator) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, rectY, getWidth(), 10);
            rectY += 10;
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, rectY, getWidth(), 6);
        }
    }
}
