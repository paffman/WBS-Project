package chooseDB;

import de.fhbingen.wbs.controller.ProjectSetupAssistant;
import de.fhbingen.wbs.translation.Menu;
import de.fhbingen.wbs.translation.Messages;
import globals.InfoBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import c10n.C10N;

/**
 * Studienprojekt: WBS Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder:
 * Andre Paffenholz, Peter Lange, Daniel Metzler, Samson von Graevenitz fügt
 * Funktionalitäten zur DBChooserGUI hinzu
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.3 - 09.12.2010
 */
public class DBChooserButtonAction {

    /**
     * The dbChooser which called the Class.
     */
    private DBChooser dbChooser;

    /**
     * The translations for messages.
     */
    private Messages messages;

    /**
     * Constructor
     * 
     * @param aDBChooser
     *            calling DBChooser
     */
    public DBChooserButtonAction(final DBChooser aDBChooser) {
        messages = C10N.get(Messages.class);
        this.dbChooser = aDBChooser;
        addButtonAction();
    }

    /**
     * void addButtonAction() fügt actionListener zum "Schließen" und "Weiter"
     * Buttons hinzu fügt actionListener für das Menü hinzu (Weiter, Oeffnen,
     * Schließen, Info und Hilfe Schließen-Button/Menü: beendet das Programm
     * Weiter-Button/Menü: führt Methode weiter() aus Oeffnen-Button/Menü:
     * führt Methode oeffnen() aus Info-Menü: gibt Angaben per JOptionPane
     * über das Projekt aus Hilfe-Menü: gibt per JOptionPane eine Hilfe für
     * die DBChooserGUI aus
     */
    public final void addButtonAction() {
        dbChooser.getGui().getCloseButton()
                .addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        System.exit(0);
                    }
                });

        dbChooser.getGui().getOkButton()
                .addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        dbChooser.next();
                    }
                });

        dbChooser.getGui().getOkMenuItem()
                .addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        dbChooser.next();
                    }
                });

        dbChooser.getGui().getCloseMenuItem()
                .addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        System.exit(0);
                    }
                });

        dbChooser.getGui().getHelpMenuItem()
                .addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent arg0) {
                        JOptionPane.showMessageDialog(dbChooser.getGui(),
                                messages.loginHelpMsg());
                    }
                });

        dbChooser.getGui().getInfoMenuItem()
                .addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent arg0) {
                        new InfoBox();
                    }
                });

        dbChooser.getGui().getNewDbMenuItem()
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ProjectSetupAssistant.newProject(dbChooser.getGui());
                    }
                });
    }
}
