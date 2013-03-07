package org.joeffice.presentation;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import javax.swing.Timer;

/**
 * Listeners to events when the presentation is in full screen mode.
 * 
 * @author Anthony Goubard - Japplis
 */
public class FullScreenListener implements KeyListener, MouseMotionListener, MouseListener {

    public final static int HIDE_MOUSE_DELAY_MS = 1500;

    private FullScreenFrame frame;

    private Cursor transparentCursor;

    private Timer hideMouseTime;

    public FullScreenListener(FullScreenFrame frame) {
        this.frame = frame;
        int[] pixels = new int[16 * 16];
        Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
        transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "invisibleCursor");
        hideMouseTime = new Timer(HIDE_MOUSE_DELAY_MS, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMouseVisible(false);
            }
        });
        hideMouseTime.setRepeats(false);
    }

    public void setMouseVisible(boolean visible) {
        if (visible) {
            Cursor defaultCursor = Cursor.getDefaultCursor();
            frame.setCursor(defaultCursor);
        } else {
            frame.setCursor(transparentCursor);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                setMouseVisible(true);
                frame.dispose();
                break;
            case KeyEvent.VK_SPACE:
                frame.setSlideIndex(frame.getSlideIndex() + 1);
                break;
            case KeyEvent.VK_P:
            case KeyEvent.VK_DELETE:
                frame.setSlideIndex(frame.getSlideIndex() - 1);
                break;
            case KeyEvent.VK_HOME:
                frame.setSlideIndex(0);
                break;
            case KeyEvent.VK_END:
                //frame.setSlideIndex(0);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        setMouseVisible(true);
        hideMouseTime.restart();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON1) {
            frame.setSlideIndex(frame.getSlideIndex() + 1);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
