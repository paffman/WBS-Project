package de.fhbingen.wbs.gui.projectsetupassistant;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Container;
import java.awt.Dimension;

/**
 * GUI class for a project assistant.
 * A project assistant is used to create new projects and create necessary
 * database structures.
 * Created by Maxi on 02.01.14.
 */
public class ProjectProperties extends JDialog {
    /**
     * Interface for GUI actions.
     */
    public interface Actions {

    }

    /**
     * Default constructor for a ProjectProperties.
     * @param newDelegate the controller instance.
     */
    public ProjectProperties(final Actions newDelegate) {
        super();
        delegate = newDelegate;
        contentPane = getContentPane();
        createUiObjects();

        //Configuration of Dialog
        setTitle("Projektassistent");
        setModal(true);
        setPreferredSize(PREFERRED_SIZE);
        setVisible(true);
        pack();
    }

    /**
     * Creates UI elements and places them on the gui.
     */
    private void createUiObjects() {
        final JLabel labelButtonNext = new JLabel();
        final JLabel labelButtonCancel = new JLabel();
        final JLabel labelButtonBack = new JLabel();
    }

    /**
     * Interface to the controller.
     */
    private final Actions delegate;

    /**
     * The content pane of this JFrame.
     * {@link javax.swing.JFrame#getContentPane()}
     */
    private final Container contentPane;

    /**
     * Default preferred size.
     */
    private static final Dimension PREFERRED_SIZE = new Dimension();
}
