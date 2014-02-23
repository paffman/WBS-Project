package de.fhbingen.wbs.gui;

import de.fhbingen.wbs.translation.ApplicationMetadata;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Static utility methods for general UI tasks.
 */
public final class SwingUtilityMethods {
    /**
     * Private constructor to prevent instantiation.
     */
    private SwingUtilityMethods() {

    }
    /**
     * Gridbaglayout helper method.
     * @param container container that component gets added on.
     * @param component component to be added and set constraints for.
     * @param gridx GridBagConstraints argument.
     * @param gridy GridBagConstraints argument.
     * @param gridwidth GridBagConstraints argument.
     * @param gridheight GridBagConstraints argument.
     * @param weightx GridBagConstraints argument.
     * @param weighty GridBagConstraints argument.
     * @param anchor GridBagConstraints argument.
     * @param fill GridBagConstraints argument.
     * @param insets GridBagConstraints argument.
     * @param ipadx GridBagConstraints argument.
     * @param ipady GridBagConstraints argument.
     */
    public static void addWithAllGridBagConstraints(final Container container,
                                                    final Component component,
                                                    final int gridx,
                                                    final int gridy,
                                                    final int gridwidth,
                                                    final int gridheight,
                                                    final double weightx,
                                                    final double weighty,
                                                    final int anchor,
                                                    final int fill,
                                                    final Insets insets,
                                                    final int ipadx,
                                                    final int ipady) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx        = gridx;
        gridBagConstraints.gridy        = gridy;
        gridBagConstraints.gridheight   = gridheight;
        gridBagConstraints.gridwidth    = gridwidth;
        gridBagConstraints.weightx      = weightx;
        gridBagConstraints.weighty      = weighty;
        gridBagConstraints.anchor       = anchor;
        gridBagConstraints.fill         = fill;
        gridBagConstraints.insets       = insets;
        gridBagConstraints.ipadx        = ipadx;
        gridBagConstraints.ipady        = ipady;
        container.add(component, gridBagConstraints);
    }

    /**
     * Helper method that taked a component and adds it to a container using
     * {@link java.awt.GridBagConstraints}.
     * @param container The container to add to.
     * @param component The componend to be added.
     * @param gridx GridBagConstraints value.
     * @param gridy GridBagConstraints value.
     * @param weightx GridBagConstraints value.
     * @param weighty GridBagConstraints value.
     * @param fill GridBagConstraints value.
     * @param insets GridBagConstraints value.
     */
    public static void addWithGridBagConstraints(final Container container,
                                                 final Component component,
                                                 final int gridx,
                                                 final int gridy,
                                                 final double weightx,
                                                 final double weighty,
                                                 final int fill,
                                                 final Insets insets) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx        = gridx;
        gridBagConstraints.gridy        = gridy;
        gridBagConstraints.weightx      = weightx;
        gridBagConstraints.weighty      = weighty;
        gridBagConstraints.fill         = fill;
        gridBagConstraints.insets       = insets;
        container.add(component, gridBagConstraints);
    }

    /**
     * Helper method that taked a component and adds it to a container using
     * {@link java.awt.GridBagConstraints}.
     * @param container The container to add to.
     * @param component The componend to be added.
     * @param gridx GridBagConstraints value.
     * @param gridy GridBagConstraints value.
     * @param fill GridBagConstraints value.
     */
    public static void addWithLesserGridBagConstraints(final Container
                                                              container,
                                                 final Component component,
                                                 final int gridx,
                                                 final int gridy,
                                                 final int fill) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx        = gridx;
        gridBagConstraints.gridy        = gridy;
        gridBagConstraints.fill         = fill;
        container.add(component, gridBagConstraints);
    }

    /**
     * Method that sets the look and feel native to the running os.
     * @param toBeUpdated Component tree to reload.
     */
    public static void setNativeLookAndFeel(final Component toBeUpdated) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(toBeUpdated);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            System.err.println("PLAF Error"); //NON-NLS
        }
    }

    public static GPLAboutDialog getGplDialog(final Component parent) {
        ApplicationMetadata metadata = LocalizedStrings
                .getApplicationMetadata();
        return new GPLAboutDialog(parent, metadata.applicationName(),
                getVersionName(), metadata.shortDescriptionHTML(),
                metadata.copyrightHolder());
    }

    public static String getVersionName() {

        InputStream resource = SwingUtilityMethods.class.getClassLoader()
                .getResourceAsStream("version");
        BufferedReader inputFile = new BufferedReader(new InputStreamReader(
                resource));
        try {
            return inputFile.readLine();
        } catch (IOException | NullPointerException e) {
            System.err.println(
                    "Version number file could not be read."); //NON-NLS
            return "";
        }
    }
}
