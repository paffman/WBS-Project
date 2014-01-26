package wpAddAufwand;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 * ButtonActions der AddAufwandGUI werden hier verwaltet.
 */
public class AddAufwandButtonAction {
    /**
     * The logic class.
     */
    private final AddAufwand addaufwand;

    /**
     * Konstruktor.
     *
     * @param addAufwand - Objekt vom Typ AddAufwand
     */
    public AddAufwandButtonAction(final AddAufwand addAufwand) {
        this.addaufwand = addAufwand;
        addButtonAction();
    }

    /**
     * Fügt actionListener zum "Schließen", "Editieren" Buttons und den
     * Keylistener zu dem Textfeld "Aufwand" hinzu.
     */
    private void addButtonAction() {
        addaufwand.getGui().getBtnEdit().addActionListener(new ActionListener
                () {
            public void actionPerformed(final ActionEvent arg0) {
                if (addaufwand.checkFieldsFilled()) {
                    try {
                        if (!addaufwand.addAufwand()) {
                            JOptionPane.showMessageDialog(addaufwand.getGui()
                                    , "Bitte prüfen Sie Ihre Eingaben!");
                        } else {
                            //berechnet den neuen ETC und schreibt den Wert
                            // in das Textfeld ETC der WPShow damit die Werte
                            // mit dem neu errechneten ETC berechnet werden
                            double etc = addaufwand.getWpshow().getETCfromGUI
                                    () - Double.parseDouble(addaufwand.getGui
                                    ().getTxfAufwand().getText());
                            addaufwand.getWpshow().updateETCInGUI(etc);
                            //Methode setChanges der Klasse WPShow
                            // aktualisiert die neuen Werte und berechnet
                            // alles neu mit dem neuen Aufwand und ETC
                            //JOptionPane.showMessageDialog(addaufwand.gui,
                            // "Aufwand wurde erfolgreich eingetragen");
                            addaufwand.getWpshow().save();
                            addaufwand.getGui().dispose();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(addaufwand.getGui(),
                            "Felder wurden nicht vollständig eingegeben",
                            null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        addaufwand.getGui().getBtnSchliessen().addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                addaufwand.getGui().dispose();
            }
        });

        addaufwand.getGui().getTxfAufwand().addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                //Kommas werden direkt durch Punkte ersetzt,
                // damit es zu keine Fehlereingaben kommt
                addaufwand.getGui().getTxfAufwand().setText(addaufwand.getGui
                        ().getTxfAufwand().getText().replace(",", "."));
            }

            public void keyReleased(final KeyEvent e) {
                //Es darf nur ein Punkt gesetzt sein,
                // ansonsten wird der letzte Punkt gelöscht
                boolean vorhanden = false;
                final StringBuilder test = new StringBuilder(addaufwand
                        .getGui().getTxfAufwand().getText());
                for (int i = 0; i < test.length(); i++) {
                    if (test.charAt(i) == '.') {
                        if (vorhanden) {
                            test.deleteCharAt(i);
                        }
                        vorhanden = true;

                    }
                }
                addaufwand.getGui().getTxfAufwand().setText(test.toString());
                char c = e.getKeyChar();
                //es werden nur Ziffern, Kommas und Punkte bei der Eingabe
                // aktzepiert
                if (!(Character.isDigit(e.getKeyChar()) || c == KeyEvent.VK_COMMA || c == KeyEvent.VK_PERIOD)) {
                    addaufwand.getGui().getTxfAufwand().setText(addaufwand.getGui().getTxfAufwand().getText().substring(0, addaufwand.getGui().getTxfAufwand().getText().length() - 1));
                }
                addaufwand.getGui().getTxfAufwand().setText(addaufwand.getGui
                        ().getTxfAufwand().getText().replace(",", "."));
            }

        });
    }
}
