package com.univlyon1.sma.model;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author khalid Ouhmaid
 */
public class View extends JComponent {

    private Environement myEnvironement;
    private int sizeSlot;

    public View(Environement myEnvironement) {
        this.myEnvironement = myEnvironement;
        this.sizeSlot = 800 / myEnvironement.getSizeM();
        this.setFocusable(true);
    }

    public static Color getColor(int value) {
        if (value == 0) {
            return Color.WHITE;
        } else if (value == 1) {
          //  return new Color((value * 1000) % 256, (value + 50) % 256, (value +0) % 256, 200);
            return new Color(255, 0, 0, 200);

        } else if (value == 2){

            return new Color(0, 250, 100);
        }else if (value == 3){

            return new Color(0, 20, 255);
        }
        else {
            return new Color(10, 22, 25, 200);

        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        sizeSlot = 800 / myEnvironement.getSizeM();

        g2D.setStroke(new BasicStroke(3));
        g2D.setFont(new Font("Times", Font.BOLD, sizeSlot / 4));
        for (int i = 0; i < myEnvironement.getSizeM(); i++) {
            for (int j = 0; j < myEnvironement.getSizeN(); j++) {
                g2D.setColor(getColor(myEnvironement.getSlot(i, j)));
                g2D.fillRect(j * sizeSlot, i * sizeSlot, sizeSlot, sizeSlot);
                g2D.setColor(Color.BLACK);
                g2D.drawRect(j * sizeSlot, i * sizeSlot, sizeSlot, sizeSlot);
                if (myEnvironement.getSlot(i, j) > 0) {
                    g2D.drawString(Integer.toString(myEnvironement.getSlot(i, j)), j * sizeSlot + sizeSlot / 2, i * sizeSlot + sizeSlot / 2);

                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(sizeSlot * myEnvironement.getSizeM(), sizeSlot * myEnvironement.getSizeN());
    }

}
