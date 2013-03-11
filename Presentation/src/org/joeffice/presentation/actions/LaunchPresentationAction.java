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
package org.joeffice.presentation.actions;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import org.apache.poi.xslf.usermodel.XMLSlideShow;

import org.joeffice.presentation.FullScreenFrame;
import org.joeffice.presentation.SlidesTopComponent;

import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;


/**
 * Action that launch the presentation in full screen mode when invoked.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "View/Office/Presentation",
        id = "org.joeffice.presentation.actions.LaunchPresentationAction")
@ActionRegistration(
        displayName = "#CTL_LaunchPresentationAction")
@Messages("CTL_LaunchPresentationAction=Launch Presentation")
public final class LaunchPresentationAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("LaunchPresentationAction called");
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SlidesTopComponent) {
            XMLSlideShow currentPresentation = ((SlidesTopComponent) currentTopComponent).getPresentation();
            FullScreenFrame presentationFrame = new FullScreenFrame();
            DisplayMode slideshowDisplay = presentationFrame.getScreen().getDisplayMode();
            //currentPresentation.setPageSize(new Dimension(slideshowDisplay.getWidth(), slideshowDisplay.getHeight()));
            presentationFrame.showSlides(currentPresentation);
        }
    }
}
