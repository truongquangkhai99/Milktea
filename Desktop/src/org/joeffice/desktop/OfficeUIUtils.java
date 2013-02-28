package org.joeffice.desktop;

import java.io.IOException;
import javax.swing.JOptionPane;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class OfficeUIUtils {

    @NbBundle.Messages({"MSG_SAVE_BEFORE_CLOSE=Save before closing?"})
    public static int checkSaveBeforeClosing(DataObject dataObject, TopComponent component) {
        SaveCookie saveCookie = dataObject.getCookie(SaveCookie.class);
        if (saveCookie != null) {
            String question = NbBundle.getMessage(OfficeUIUtils.class, "MSG_SAVE_BEFORE_CLOSE");
            int answer = JOptionPane.showConfirmDialog(component, question, question, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    saveCookie.save();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            return answer;
        }
        return JOptionPane.NO_OPTION;
    }
}
