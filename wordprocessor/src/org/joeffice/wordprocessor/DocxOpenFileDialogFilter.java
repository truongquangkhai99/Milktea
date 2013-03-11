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
