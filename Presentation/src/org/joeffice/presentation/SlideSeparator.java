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
        int width = separatorDimension.width;

        return new Dimension(width, heigth);
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
