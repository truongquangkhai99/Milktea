package org.joeffice.presentation;

import java.awt.*;
import javax.swing.JFrame;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 * The frame showing the presentation in full screen.
 *
 * @author Anthony Goubard - Japplis
 */
public class FullScreenFrame extends JFrame {

    private int screenIndex = -1;

    private int slideIndex = 0;

    private XMLSlideShow presentation;

    public FullScreenFrame() {
        setUndecorated(true);
        FullScreenListener eventListener = new FullScreenListener(this);
        addKeyListener(eventListener);
        addMouseListener(eventListener);
        addMouseMotionListener(eventListener);
    }

    public void showSlides(XMLSlideShow presentation) {
        this.presentation = presentation;
        setVisible(true);
        setSlideIndex(0);
    }

    public void setScreenIndex(int screenIndex) {
        this.screenIndex = screenIndex;
        if (isVisible()) {
            GraphicsDevice screen = getScreen();
            screen.setFullScreenWindow(this);
        }
    }

    public int getScreenIndex() {
        return screenIndex;
    }

    public void setSlideIndex(int slideIndex) {
        if (slideIndex < 0) return;
        if (slideIndex >= presentation.getSlides().length) return;
        this.slideIndex = slideIndex;
        if (isVisible()) {
            SlideComponent slidePanel = new SlideComponent(presentation.getSlides()[slideIndex], null);
            add(slidePanel);
            GraphicsDevice screen = getScreen();
            setBounds(0, 0, screen.getDisplayMode().getWidth(), (int) screen.getDisplayMode().getHeight());
        }
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public GraphicsDevice getScreen() {
        GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        GraphicsDevice screen;
        if (screenIndex == -1 || screenIndex >= screens.length) {
            screen = screens[screens.length - 1];
        } else {
            screen = screens[screenIndex];
        }
        return screen;
    }
}
