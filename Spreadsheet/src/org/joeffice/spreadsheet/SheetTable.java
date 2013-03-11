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
package org.joeffice.spreadsheet;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * The JTable used to display data.
 * This class is only to fix bugs or improve existing functionalities.
 *
 * @author Anthony Goubard - Japplis
 */
public class SheetTable extends JTable {

    public SheetTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public void setRowHeight(int row, int rowHeight) {
        super.setRowHeight(row, rowHeight);
        // Fire the row changed
        firePropertyChange("singleRowHeight", -1, row);
    }
}
