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
package org.joeffice.desktop.file;

import java.io.IOException;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class OfficeOpenSaveSupport extends OpenSupport implements OpenCookie, SaveCookie, CloseCookie {

    public OfficeOpenSaveSupport(MultiDataObject.Entry fileEntry) {
        super(fileEntry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        //return new TopComponent((OfficeDataObject) entry.getDataObject());
        return null;
    }

    @Override
    public void save() throws IOException {
        OfficeDataObject officeDataObject = (OfficeDataObject) entry.getDataObject();
        /* Document doc;
        synchronized (officeDataObject) {
            //synchronize access to the content field
            doc = content;
            officeDataObject.setContent(null);
        }
        FileObject fo = officeDataObject.getPrimaryFile();
        DocxWriter writer = new DocxWriter(doc);
        writer.write(FileUtil.toFile(fo).getAbsolutePath());*/
    }
}
