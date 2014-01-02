package placeholder.gui;

import javax.swing.JFrame;
import java.awt.Container;

/**
 * GUI class for a project assistant.
 * A project assistant is used to create new projects and create necessary
 * database structures.
 * Created by Maxi on 02.01.14.
 */
public class ProjectAssistantGUI extends JFrame {
    /**
     * Interface for GUI actions.
     */
    public interface ProjectAssistantMethods {

    }

    /**
     * Default constructor for a ProjectAssistantGUI.
     * @param newDelegate the controller instance.
     */
    public ProjectAssistantGUI(final ProjectAssistantMethods newDelegate) {
        super();
        delegate = newDelegate;
        contentPane = getContentPane();
        setTitle("Projektassistent");
        createUiObjects();
        pack();
        //setPreferredSize(preferredSize);
        setVisible(true);
    }

    /**
     * Creates UI elements and places them on the gui.
     */
    private void createUiObjects() {

    }

    /**
     * Interface to the controller.
     */
    private final ProjectAssistantMethods delegate;

    /**
     * The content pane of this JFrame.
     * {@link javax.swing.JFrame#getContentPane()}
     */
    private final Container contentPane;

    //public static final preferredSize = new Dimension(100, 100);
}
