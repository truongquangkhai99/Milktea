package org.joeffice.desktop;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Locale;
import java.util.StringTokenizer;
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
        if (dataObject == null) {
            return JOptionPane.NO_OPTION;
        }

        boolean isModified = dataObject.isModified();
        int answer = JOptionPane.NO_OPTION;
        if (isModified) {
            String question = NbBundle.getMessage(OfficeUIUtils.class, "MSG_SAVE_BEFORE_CLOSE");
            answer = JOptionPane.showConfirmDialog(component, question, question, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                try {
                    SaveCookie saveCookie = dataObject.getCookie(SaveCookie.class);
                    saveCookie.save();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        // This seams to get rid of the data object from the memory, otherwise reopening the file would open the one in memory
        try {
            dataObject.setValid(false);
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
        return answer;
    }

    /**
     * String utility method that changes TEST_NAME to Test Name
     *
     * @param technicalName the technical name (e.g. a database table name)
     * @return the display name
     */
    public static String toDisplayable(String technicalName) {
        String noUnderscoreName = technicalName.replace('_', ' ');
        StringTokenizer wordsIterator = new StringTokenizer(noUnderscoreName, " ");

        StringBuilder displayableName = new StringBuilder(technicalName.length());
        boolean hasNextWord = wordsIterator.hasMoreTokens();
        while (hasNextWord) {
            String nextWord = wordsIterator.nextToken();
            if (nextWord.toUpperCase().equals(nextWord) && nextWord.length() > 1) {
                nextWord = nextWord.charAt(0) + nextWord.substring(1, nextWord.length()).toLowerCase();
            }
            displayableName.append(nextWord);
            hasNextWord = wordsIterator.hasMoreTokens();
            if (hasNextWord) {
                displayableName.append(' ');
            }
        }
        return displayableName.toString();
    }
}
