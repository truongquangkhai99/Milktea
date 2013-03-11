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
package org.joeffice.tools;

import java.awt.BorderLayout;
import javax.swing.*;

public class JTableCellSelection {

    public static void showDemo(JComponent demo, String title) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle(title);
        JPanel contentPanel = new JPanel(new BorderLayout());

        contentPanel.add(demo);
        
        mainFrame.add(contentPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JTable table = new JTable(10, 10);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(true);
        table.addRowSelectionInterval(6, 7); // Select 2 lines
        showDemo(new JScrollPane(table), "Select a block and some rows");
    }
}