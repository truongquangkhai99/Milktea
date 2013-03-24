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
package org.joeffice.wordprocessor.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.text.*;
import org.joeffice.wordprocessor.BorderAttributes;
import org.joeffice.wordprocessor.DocxDocument;
import org.joeffice.wordprocessor.WordpTopComponent;
import org.joeffice.wordprocessor.app.*;
import org.openide.windows.WindowManager;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class SimpleActionsFactory {

    /**
     * Return an Action which inserts image into document. Used for creating menu item.
     *
     * @return
     */
    public static Action insertImageAction() {
        Action a = new AbstractAction("Insert image") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                Frame mainFrame = WindowManager.getDefault().getMainWindow();
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                ImageIcon icon = new ImageIcon(fc.getSelectedFile().getPath());
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                if (w <= 0 || h <= 0) {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid image!");
                    return;
                }
                DocxDocument doc = (DocxDocument) edit.getDocument();
                doc.insertPicture(icon, edit.getCaretPosition());
            }
        };
        return a;
    }

    /**
     * Return an Action which setVisible(true)s paragraph attributes. Used for creating menu item.
     *
     * @return
     */
    public static Action paragraphAttributesAction() {
        Action a = new AbstractAction("Paragraph...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                JFrame mainFrame = (JFrame) WindowManager.getDefault().getMainWindow();
                ParagraphPanel paragraph = new ParagraphPanel();
                paragraph.showDialog(mainFrame);
                AttributeSet attrs = ((DocxDocument) edit.getDocument()).getParagraphElement(edit.getCaretPosition()).getAttributes();
                paragraph.setAttributes(attrs);
                paragraph.setVisible(true);
                if (paragraph.getOption() == JOptionPane.OK_OPTION) {
                    DocxDocument doc = (DocxDocument) edit.getDocument();
                    AttributeSet attr = paragraph.getAttributes();
                    doc.setParagraphAttributes(edit.getSelectionStart(), edit.getSelectionEnd() - edit.getSelectionStart(), attr, false);
                }
            }
        };
        return a;
    }

    /**
     * Return an Action which inserts table into document. Used for creating menu item.
     *
     * @return
     */
    public static Action insertTableAction() {
        Action a = new AbstractAction("Table...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                JFrame mainFrame = (JFrame) WindowManager.getDefault().getMainWindow();
                InsertTablePanel insertPanel = new InsertTablePanel();
                insertPanel.showDialog(mainFrame);
                if (insertPanel.getOption() == JOptionPane.OK_OPTION) {
                    int pos = edit.getCaretPosition();
                    DocxDocument doc = (DocxDocument) edit.getDocument();
                    Element cell = doc.getCell(pos);
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    BorderAttributes ba = new BorderAttributes();
                    ba.setBorders(1 + 2 + 4 + 8 + 16 + 32);
                    ba.lineColor = insertPanel.getColor();
                    attrs.addAttribute("BorderAttributes", ba);
                    int[] widths = new int[insertPanel.getColumnCount()];
                    if (cell == null) {
                        for (int i = 0; i < widths.length; i++) {
                            widths[i] = 100;
                        }
                    } else {
                        int width = ((DocxDocument.CellElement) cell).getWidth() - 4;
                        for (int i = 0; i < widths.length; i++) {
                            widths[i] = width / insertPanel.getColumnCount();
                        }
                    }
                    int[] heights = new int[insertPanel.getRowCount()];
                    for (int i = 0; i < heights.length; i++) {
                        heights[i] = 1;
                    }
                    doc.insertTable(pos, insertPanel.getRowCount(), insertPanel.getColumnCount(), attrs, widths, heights);
                }
            }
        };
        return a;
    }

    /**
     * Return an Action which inserts row into document's table. Used for creating menu item.
     *
     * @param above if true new row will be inserted above current row.
     * @return
     */
    public static Action insertRowAction(final boolean above) {
        String label = "Row";
        if (above) {
            label += " above";
        } else {
            label += " below";
        }
        Action a = new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                doc.insertRow(edit.getCaretPosition(), above);
            }
        };
        return a;
    }

    /**
     * Return an Action which inserts column into document's table. Used for creating menu item.
     *
     * @param beforeove if true new column will be inserted before current column.
     * @return
     */
    public static Action insertColumnAction(final boolean before) {
        String label = "Column";
        if (before) {
            label += " to the left";
        } else {
            label += " to the right";
        }
        Action a = new AbstractAction(label) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                doc.insertColumn(edit.getCaretPosition(), 50, before);
            }
        };
        return a;
    }

    /**
     * Return an Action which removes table from document. Used for creating menu item.
     *
     * @return
     */
    public static Action deleteTableAction() {
        Action a = new AbstractAction("Table") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                doc.deleteTable(edit.getCaretPosition());
            }
        };
        return a;
    }

    /**
     * Return an Action which removes row from document's table. Used for creating menu item.
     *
     * @return
     */
    public static Action deleteRowAction() {
        Action a = new AbstractAction("Row") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                doc.deleteRow(edit.getCaretPosition());
            }
        };
        return a;
    }

    /**
     * Return an Action which removes column from document's table. Used for creating menu item.
     *
     * @return
     */
    public static Action deleteColumnAction() {
        Action a = new AbstractAction("Column") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                doc.deleteColumn(edit.getCaretPosition());
            }
        };
        return a;
    }

    /**
     * Return an Action which sets margins of document. Used for creating menu item.
     *
     * @return
     */
    public static Action setMarginsAction() {
        Action a = new AbstractAction("Margins...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame mainFrame = (JFrame) WindowManager.getDefault().getMainWindow();
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                MarginsPanel marginsPanel = new MarginsPanel();
                marginsPanel.setMargins(doc.getDocumentMargins());
                JDialog dlg = marginsPanel.showDialog(mainFrame);
                if (marginsPanel.getOption() == JOptionPane.OK_OPTION) {
                    doc.setDocumentMargins(marginsPanel.getMargins());
                }
            }
        };
        return a;
    }

    /**
     * Return an Action which setVisible(true)s table's properties Used for creating menu item.
     *
     * @return
     */
    public static Action tablePropertiesAction() {
        Action a = new AbstractAction("Table...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                Element table = doc.getTable(edit.getCaretPosition());
                if (table == null) {
                    JOptionPane.showMessageDialog(null, "Invalid table offset!");
                    return;
                }
                TableProperties tp = new TableProperties();
                tp.setTable(table);
                tp.setVisible(true);
            }
        };
        return a;
    }

    /**
     * Return an Action which setVisible(true)s row's properties Used for creating menu item.
     *
     * @return
     */
    public static Action rowPropertiesAction() {
        Action a = new AbstractAction("Row...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                Element row = doc.getRow(edit.getCaretPosition());
                if (row == null) {
                    JOptionPane.showMessageDialog(null, "Invalid row offset!");
                    return;
                }
                TableProperties tp = new TableProperties();
                tp.setRow(row);
                tp.setVisible(true);
            }
        };
        return a;
    }

    /**
     * Return an Action which setVisible(true)s cell's properties Used for creating menu item.
     *
     * @return
     */
    public static Action cellPropertiesAction() {
        Action a = new AbstractAction("Cell...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane edit = WordpTopComponent.getTextPane();
                DocxDocument doc = (DocxDocument) edit.getDocument();
                Element cell = doc.getCell(edit.getCaretPosition());
                if (cell == null) {
                    JOptionPane.showMessageDialog(null, "Invalid cell offset!");
                    return;
                }
                TableProperties tp = new TableProperties();
                tp.setCell(cell);
                tp.setVisible(true);
            }
        };
        return a;
    }
}
