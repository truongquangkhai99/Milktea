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
package org.joeffice.wordprocessor.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Represents simple dialog for editing tables' margins.
 *
 * @author Stanislav Lapitsky
 */
public class MarginsDialog extends JDialog {

    private int option=JOptionPane.CANCEL_OPTION;

    private DoubleSpinEdit top=new DoubleSpinEdit(0,100);
    private DoubleSpinEdit bottom=new DoubleSpinEdit(0,100);
    private DoubleSpinEdit left=new DoubleSpinEdit(0,100);
    private DoubleSpinEdit right=new DoubleSpinEdit(0,100);

    /**
     * Constructs new dialog instance with specified parent frame.
     * @param owner parent frame
     */
    public MarginsDialog(JFrame owner) {
        super(owner);
        init();
    }

    /**
     * Constructs new dialog instance with specified parent dialog.
     * @param owner parent dialog
     */
    public MarginsDialog(JDialog owner) {
        super(owner);
        init();
    }

    /**
     * Initializes all dialogs' controls (button, edits etc.).
     */
    protected void init() {
        setModal(true);
        setTitle("Document margins");

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(new JLabel("Top:"),new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,0,0),0,0));
        getContentPane().add(new JLabel("Bottom:"),new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0));
        getContentPane().add(new JLabel("Left:"),new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0));
        getContentPane().add(new JLabel("Right:"),new GridBagConstraints(0,3,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0));

        getContentPane().add(top,new GridBagConstraints(1,0,2,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,0,0,5),0,0));
        getContentPane().add(bottom,new GridBagConstraints(1,1,2,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,5),0,0));
        getContentPane().add(left,new GridBagConstraints(1,2,2,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,5),0,0));
        getContentPane().add(right,new GridBagConstraints(1,3,2,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,5),0,0));

        JPanel p=new JPanel();
        JButton b=new JButton("Ok");
        p.add(b);
        ActionListener lst=new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                option=JOptionPane.OK_OPTION;
                setVisible(false);
            }
        };
        b.addActionListener(lst);

        b=new JButton("Cancel");
        lst=new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        option=JOptionPane.CANCEL_OPTION;
                        setVisible(false);
                    }
        };
        b.addActionListener(lst);
        p.add(b);

        getContentPane().add(p,new GridBagConstraints(2,4,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));

        getContentPane().add(new JLabel(),new GridBagConstraints(1,5,1,1,0,1,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0));
        pack();
    }

    /**
     * @return user's selection
     */
    public int getOption() {
        return option;
    }

    /**
     * @return margins which was set.
     */
    public Insets getMargins() {
        return new Insets(PixelConverter.converInchesToPixels(top.getValue())
                          ,PixelConverter.converInchesToPixels(left.getValue())
                          ,PixelConverter.converInchesToPixels(bottom.getValue())
                          ,PixelConverter.converInchesToPixels(right.getValue()));
    }

    /**
     * Sets margins and reflect them in the dialog.
     *
     * @param margins margins values.
     */
    public void setMargins(Insets margins) {
        top.setValue(PixelConverter.converPixelsToInches(margins.top));
        bottom.setValue(PixelConverter.converPixelsToInches(margins.bottom));
        left.setValue(PixelConverter.converPixelsToInches(margins.left));
        right.setValue(PixelConverter.converPixelsToInches(margins.right));
    }
}