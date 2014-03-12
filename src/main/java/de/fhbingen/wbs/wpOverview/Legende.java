/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpOverview;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Caption on the package overview to explain the color disposition of the
 * cpi.
 */
public class Legende extends JPanel {
    /**
     * Color used in the legend and work package overview for a high cpi.
     */
    public static final Color HIGH_CPI_COLOR = new Color(0, 80, 0);

    /**
     * Color used in the legend and work package overview for an even cpi.
     */
    public static final Color EVEN_CPI_COLOR = Color.GREEN;

    /**
     * Color used in the legend and work package overview for a low cpi.
     */
    public static final Color LOW_CPI_COLOR = Color.YELLOW;

    /**
     * Color used in the legend and work package overview for a very low cpi.
     */
    public static final Color VERY_LOW_CPI_COLOR = Color.RED;

    /**
     * Color used in the legend and work package overview for an ultra low cpi.
     */
    public static final Color ULTRA_LOW_CPI_COLOR = new Color(80, 0, 0);

    /**
     * Color used in work package overview when no cpi is calculated.
     */
    public static final Color NO_CPI_COLOR = Color.WHITE;

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 1L;


    /**
     * Default-Constructor: Creates a JPanel with the different colors of
     * the cpi and its values.
     */
    public Legende() {
        super();

        setLayout(new FlowLayout());

        add(new JLabel(LocalizedStrings.getWbs().cpiColors() + ":"));
        JLabel text = new JLabel();
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setOpaque(true);

        add(new JLabel());

        final Dimension canvasSize = new Dimension(20, 20);



        /* The canvas for the color of the single cpi levels. */
        Canvas color = new Canvas();
        color.setBackground(HIGH_CPI_COLOR);
        color.setSize(canvasSize);
        add(color);
        text.setText("1.03 >");
        add(text);

        color = new Canvas();
        color.setBackground(EVEN_CPI_COLOR);
        color.setSize(canvasSize);
        add(color);
        text = new JLabel("1.03 - 0.97");
        add(text);

        color = new Canvas();
        color.setBackground(LOW_CPI_COLOR);
        color.setSize(canvasSize);
        add(color);
        text = new JLabel("0.97 - 0.94");
        add(text);

        color = new Canvas();
        color.setBackground(VERY_LOW_CPI_COLOR);
        color.setSize(canvasSize);
        add(color);
        text = new JLabel("0.94 - 0.6");
        add(text);

        color = new Canvas();
        color.setBackground(ULTRA_LOW_CPI_COLOR);
        color.setSize(canvasSize);
        add(color);
        text = new JLabel("< 0.6");
        add(text);
    }

    /**
     * Convert the visibility of the caption. (Caption will shown in the
     * package overview but will not shown in the employees and baseline
     * view)
     * @param state
     *            - true: visible, false: not visible
     */
    public final void setVisible(final boolean state) {
        super.setVisible(state);
    }

}
