package de.fhbingen.wbs.wpShow;

import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.FilterJTextField;
import de.fhbingen.wbs.translation.*;
import de.fhbingen.wbs.translation.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The GUI to edit the description for an workpackage.
 */
public class DescriptionGUI extends JFrame {

    /**
     * WPShow controller.
     */
    private WPShow wpShow;

    /**
     * Panel for the whole content.
     */
    private JPanel pnlContent;

    /**
     * Panel for the bottom content.
     */
    private JPanel pnlBottom;

    /**
     * Textarea for the description.
     */
    private JTextArea txaDesc;

    /**
     * Button to save the description.
     */
    private JButton btnSave;

    /**
     * Button to cancel the GUI.
     */
    private JButton btnCancel;

    /**
     * Strings for the buttons.
     */
    private Button buttonStrings;


    /**
     *
     * @param title from the GUI.
     * @param wpShow Controller.
     */
    public DescriptionGUI(String title, WPShow wpShow){
        super(title);

        this.wpShow = wpShow;
        this.buttonStrings = LocalizedStrings.getButton();

        pnlContent = new JPanel(new BorderLayout());
        getContentPane().add(pnlContent, BorderLayout.CENTER);

        txaDesc = new JTextArea(){
            private static final long serialVersionUID = -3874188090738553731L;

            public String getText() {
                return FilterJTextField.filterText(super.getText());
            }
        };
        txaDesc.setText(wpShow.getWPShowGUI().getDescription());
        txaDesc.setBorder(new EmptyBorder(5, 5, 5, 3));
        JScrollPane scrollDesc = new JScrollPane(txaDesc);
        pnlContent.add(scrollDesc, BorderLayout.CENTER);

        pnlBottom = new JPanel(new GridLayout(1, 2));
        pnlContent.add(pnlBottom, BorderLayout.SOUTH);

        btnSave = new JButton(buttonStrings.save(""));
        btnSave.addActionListener(wpShow.getBtnSaveDescListener());
        pnlBottom.add(btnSave);

        btnCancel = new JButton(buttonStrings.cancel());
        btnCancel.addActionListener(wpShow.getBtnCancelDescListener());
        pnlBottom.add(btnCancel);

        Toolkit tool = Toolkit.getDefaultToolkit();
        this.setPreferredSize(tool.getScreenSize());
        this.pack();
        Controller.centerComponent(wpShow.getWPShowGUI(), this);
        this.setVisible(true);
    }

    /**
     * Return the content from the description testxarea.
     * @return String from descritpion.
     */
    public String getDesc(){
        return txaDesc.getText();
    }


}