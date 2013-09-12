package wpReassign;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * ButtonAction der GUI Klasse zum einfügen von Arbeitspaketen in einer Unterstruktur
 * 
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 13.05.2011
 */



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

public class WPReassignButtonAction {

	
	private WPReassign wpreassign;
	
	public WPReassignButtonAction(WPReassign wpreassign) {
		this.wpreassign = wpreassign;
		addButtonAction();
	}
	
	
	public void addButtonAction(){
		
		wpreassign.gui.btnSpeichern.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				if(wpreassign.checkFieldsFilled()){
					wpreassign.setRekPakete();
				}else{
					JOptionPane.showMessageDialog(wpreassign.gui, "Felder wurden nicht vollständig eingegeben",null,
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		wpreassign.gui.btnSchliessen.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				wpreassign.gui.dispose();
			}
		});
		
		
		wpreassign.gui.txfBac.addKeyListener(new KeyAdapter() {			

			public void keyReleased(KeyEvent e){
				//es werden nur Ziffern aktzepiert
				if(!Character.isDigit(e.getKeyChar())){
					wpreassign.gui.txfBac.setText(wpreassign.gui.txfBac.getText().substring(0,wpreassign.gui.txfBac.getText().length()-1));							
				}
			}
					
		});
		
	}
}
