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
package org.joeffice.database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import org.h2.tools.Console;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 * An action that launch the H2 console to manage the opened database.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Tools",
        id = "org.joeffice.database.ManageDatabaseAction")
@ActionRegistration(
        displayName = "#CTL_ManageDatabaseAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1450),
    @ActionReference(path = "Loaders/application/h2/Actions", position = 150)
})
@Messages("CTL_ManageDatabaseAction=Manage Database in Browser")
public final class ManageDatabaseAction implements ActionListener, Runnable {

    private final DataObject context;

    public ManageDatabaseAction(H2DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        // TODO use Task that can be stopped ?
        Thread consoleThread = new Thread(this);
        consoleThread.start();
    }

    @Override
    public void run() {
        File databaseFile = FileUtil.toFile(context.getPrimaryFile());
        try {
            String url = "jdbc:h2:" + databaseFile.toURI().toURL();
            if (url.endsWith(".h2.db")) {
                url = url.substring(0, url.length() - 6);
            }
            Console.main("-url", url, "-user", "sa");
        } catch (MalformedURLException | SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
