package org.joeffice.desktop.actions;

import java.text.AttributedCharacterIterator;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class ParagraphAttributes extends AttributedCharacterIterator.Attribute {

    public final static ParagraphAttributes ALIGNMENT = new ParagraphAttributes("Alignment");
    public final static int ALIGNMENT_LEFT = StyleConstants.ALIGN_LEFT;
    public final static int ALIGNMENT_RIGHT = StyleConstants.ALIGN_RIGHT;
    public final static int ALIGNMENT_CENTER = StyleConstants.ALIGN_CENTER;

    public final static ParagraphAttributes INCREASE_FONT_SIZE = new ParagraphAttributes("FontSizeIncrement");
    
    ParagraphAttributes(String name) {
        super(name);
    }
}
