package org.joeffice.wordprocessor;

import java.io.File;
import javax.swing.filechooser.FileFilter;
//import org.netbeans.modules.openide.filesystems.FileFilterSupport;
//import org.openide.util.lookup.ServiceProvider;

/**
 * Add .docx to the open dialog choices.
 * Not working at the moment.
 * 
 * @author Anthony Goubard - Japplis
 */
//@ServiceProvider(service = FileFilter.class, path=FileFilterSupport.FILE_FILTER_LOOKUP_PATH)
public class DocxOpenFileDialogFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".docx");
    }

    @Override
    public String getDescription() {
        return "Word Document (*.docx)"; // NO I18N
    }
}
