package org.joeffice.desktop.actions;

import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import javax.swing.text.StyleConstants;

/**
 * Contants to define any extra attributes that don't exist in TextAttribute.
 *
 * @see TextAttribute
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
