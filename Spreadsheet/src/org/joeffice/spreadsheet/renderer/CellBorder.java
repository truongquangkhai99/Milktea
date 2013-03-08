package org.joeffice.spreadsheet.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.border.AbstractBorder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.joeffice.spreadsheet.POIUtils;

/**
 * Border for POI Cells.
 *
 * @author Anthony Goubard - Japplis
 */
public class CellBorder extends AbstractBorder {

    private CellStyle style;

    public CellBorder(Cell cell) {
        style = cell.getCellStyle();
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        boolean paintBorder = applyBorderColor(g2, style.getBorderBottom(), style.getBottomBorderColor());
        if (paintBorder) {
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }
        paintBorder = applyBorderColor(g2, style.getBorderTop(), style.getTopBorderColor());
        if (paintBorder) {
            g.drawLine(x, y, x + width, y);
        }
        paintBorder = applyBorderColor(g2, style.getBorderLeft(), style.getLeftBorderColor());
        if (paintBorder) {
            g.drawLine(x, y, x, y + height);
        }
        paintBorder = applyBorderColor(g2, style.getBorderRight(), style.getRightBorderColor());
        if (paintBorder) {
            g.drawLine(x + width - 1, y, x + width - 1, y + height);
        }
    }

    private boolean applyBorderColor(Graphics2D g2, short border, short borderColor) {
        if (border != style.BORDER_NONE) {
            Color awtBorderColor = POIUtils.shortToColor(borderColor);
            if (awtBorderColor == null) {
                awtBorderColor = Color.BLACK;
            }
            g2.setColor(awtBorderColor);
            return true;
        }
        return false;
    }
}
