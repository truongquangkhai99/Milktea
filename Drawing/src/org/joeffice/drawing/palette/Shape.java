package org.joeffice.drawing.palette;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class Shape implements Transferable {

    private final static DataFlavor[] SHAPE_FLAVORS = new DataFlavor[0];

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SHAPE_FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return Arrays.asList(SHAPE_FLAVORS).contains(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }
}
