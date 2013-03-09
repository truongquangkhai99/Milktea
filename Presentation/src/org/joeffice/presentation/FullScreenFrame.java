package org.joeffice.presentation;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 * The frame showing the presentation in full screen.
 *
 * @author Anthony Goubard - Japplis
 */
public class FullScreenFrame extends JFrame {

    private FullScreenListener eventListener;

    private int screenIndex = -1;

    private int slideIndex = 0;

    private XMLSlideShow presentation;

    public FullScreenFrame() {
        setUndecorated(true);
        eventListener = new FullScreenListener(this);
        JPanel transparentPanel = new JPanel() {
            public Dimension getPreferredSize() {
                GraphicsDevice screen = getScreen();
                return new Dimension(screen.getDisplayMode().getWidth(), screen.getDisplayMode().getHeight());
            }
        };
        JLayeredPane layeredPane = getRootPane().getLayeredPane();
        Component glassPane = getRootPane().getGlassPane();
        layeredPane.add(transparentPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.setVisible(true);
        glassPane.addKeyListener(eventListener);
        glassPane.addMouseListener(eventListener);
        glassPane.addMouseMotionListener(eventListener);
        glassPane.setFocusable(true);
        glassPane.setVisible(true);
        ImageIcon icon = new ImageIcon(getClass().getResource("/org/joeffice/presentation/presentation-16.png"));
        setIconImage(icon.getImage());
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
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
            DisplayMode display = getScreen().getDisplayMode();
            XSLFSlide slide = presentation.getSlides()[slideIndex];
            Dimension displaySize = new Dimension(display.getWidth(), display.getHeight());
            SlideComponent slidePanel = new SlideComponent(slide, null, displaySize);
            if (getContentPane().getComponentCount() > 0) {
                getContentPane().remove(0);
            }
            add(slidePanel);
            setSize(displaySize);
            revalidate();
        }
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public XMLSlideShow getPresentation() {
        return presentation;
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
