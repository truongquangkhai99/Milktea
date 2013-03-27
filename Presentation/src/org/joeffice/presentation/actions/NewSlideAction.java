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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import org.apache.poi.xslf.usermodel.*;

import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.presentation.SlideComponent;
import org.joeffice.presentation.SlidesTopComponent;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit/Office/Presentation",
        id = "org.joeffice.presentation.actions.NewSlideAction")
@ActionRegistration(
        iconBase = "org/joeffice/presentation/actions/application_add.png",
        displayName = "#CTL_NewSlideAction")
@ActionReferences(value = {
    @ActionReference(path = "Office/Presentation/Toolbar", position = 400),
    @ActionReference(path = "Shortcuts", name = "D-Plus")
})
@Messages("CTL_NewSlideAction=Add Slide")
public final class NewSlideAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        SlidesTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SlidesTopComponent.class);
        if (currentTopComponent != null) {
            XMLSlideShow presentation = currentTopComponent.getPresentation();
            XSLFSlideMaster defaultMaster = presentation.getSlideMasters()[0];
            XSLFSlideLayout slideLayout = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
            if (slideLayout == null) {
                slideLayout = defaultMaster.getLayout(SlideLayout.BLANK);
            }
            XSLFSlide newSlide = presentation.createSlide(slideLayout);
            int selectedSlide = currentTopComponent.getSelectedSlide();
            presentation.setSlideOrder(newSlide, selectedSlide);

            SlideComponent slideComp = new SlideComponent(newSlide, currentTopComponent);
            String indexInCard = String.valueOf(presentation.getSlides().length);
            currentTopComponent.getMainComponent().add(slideComp, indexInCard);
        }
    }
}
