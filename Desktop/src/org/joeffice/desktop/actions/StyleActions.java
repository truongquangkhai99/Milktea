package org.joeffice.desktop.actions;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 * Registration of the stye actions
 *
 * @author Anthony Goubard - Japplis
 */
public class StyleActions {

    @ActionRegistration(displayName = "#CTL_ChooseFontAction2")
    @ActionID(category = "Edit/Office", id = "org.joeffice.desktop.actions.StyleActions.CHOOSE_FONT_ACTION_MAP_KEY")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1515),
    @ActionReference(path = "Toolbars/Font", position = 3005)
})
    @NbBundle.Messages("CTL_ChooseFontAction2=Choose Font 2...")
    public static final String CHOOSE_FONT_ACTION_MAP_KEY = "ChooseFont";
}
