package hu.akoel.mgu.drawnblock;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import hu.akoel.mgu.grid.Grid;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class DrawnBlockSnapControl extends JPanel{

	private static final long serialVersionUID = -7405525427175628680L;

	private static final boolean DEFAULT_GRIDSNAP = true;
	private static final boolean DEFAULT_SIDEEXTENTIONSNAP = true;
	private static final int DEFAULT_SNAPDELTA = 15;
	
	private DrawnBlockCanvas canvas;
	private Grid grid;

	public DrawnBlockSnapControl( DrawnBlockCanvas canvas, Grid grid ){
		this.canvas = canvas;
		this.grid = grid;
		
		//Nem engedelyezett a GridSnap
		DrawnBlockSnapControl.this.canvas.setNeededSnapGrid( DEFAULT_GRIDSNAP, grid );
		
		//Nem engedelyezett az oldal meghosszabitasra igazitas
		DrawnBlockSnapControl.this.canvas.setNeededSnapSideExtention( DEFAULT_SIDEEXTENTIONSNAP );
		
		//Alapertelmezett snap tartomany
		DrawnBlockSnapControl.this.canvas.setSnapDelta( DEFAULT_SNAPDELTA );
	}
	
	public JPanel getControl(){
		
		JTextField snapDeltaField = new JTextField();
		snapDeltaField.setColumns( 8 );
		snapDeltaField.setText( String.valueOf( DrawnBlockSnapControl.this.canvas.getSnapDelta() ) );
		snapDeltaField.setInputVerifier( new InputVerifier() {
			String goodValue =  String.valueOf( DrawnBlockSnapControl.this.canvas.getSnapDelta() );
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
	            String possibleValue = text.getText();
	            try{
	            	Integer.valueOf(possibleValue);
	            	goodValue = possibleValue;
	            }catch(NumberFormatException e){
	            	text.setText(goodValue);
	            	return false;
	            }
	            DrawnBlockSnapControl.this.canvas.setSnapDelta( Integer.valueOf( goodValue ) );
	            DrawnBlockSnapControl.this.canvas.revalidateAndRepaintCoreCanvas();
	            return true;
			}
		});
		
		JCheckBox turnOnGridSnap = new JCheckBox("Grid");
		turnOnGridSnap.setSelected( DEFAULT_GRIDSNAP );
		turnOnGridSnap.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					DrawnBlockSnapControl.this.canvas.setNeededSnapGrid( false, null );
				}else{
					DrawnBlockSnapControl.this.canvas.setNeededSnapGrid( true, grid );
				}
				
			}
		});
		
		JCheckBox turnOnSideExtentionSnap = new JCheckBox("Side extention");
		turnOnSideExtentionSnap.setSelected( DEFAULT_SIDEEXTENTIONSNAP );
		turnOnSideExtentionSnap.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if( e.getStateChange() == ItemEvent.DESELECTED){
					DrawnBlockSnapControl.this.canvas.setNeededSnapSideExtention( false );
				}else{
					DrawnBlockSnapControl.this.canvas.setNeededSnapSideExtention( true );
				}				
			}
		});
		
		JPanel snapPanel = new JPanel();
		snapPanel.setLayout(new GridBagLayout());
		snapPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Snap", TitledBorder.LEFT, TitledBorder.TOP));
		GridBagConstraints snapPanelConstraints = new GridBagConstraints();

		//1. sor - Snap Range
		snapPanelConstraints.gridx = 0;
		snapPanelConstraints.gridy = 0;
		snapPanelConstraints.gridwidth = 1;
		snapPanelConstraints.weightx = 0;
		snapPanel.add( new JLabel("Snap range: "), snapPanelConstraints);

		snapPanelConstraints.gridx = 1;
		snapPanelConstraints.gridwidth = 1;
		snapPanelConstraints.weightx = 1;
		snapPanel.add( snapDeltaField, snapPanelConstraints);
				
		snapPanelConstraints.gridx = 2;
		snapPanelConstraints.gridwidth = 1;
		snapPanelConstraints.weightx = 0;
		snapPanel.add( new JLabel(" px" ), snapPanelConstraints );	
				
		//2. sor - GridSnap
		snapPanelConstraints.gridx = 0;
		snapPanelConstraints.gridy++;
		snapPanelConstraints.gridwidth = 4;
		snapPanelConstraints.anchor = GridBagConstraints.WEST;
		snapPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		snapPanelConstraints.weightx = 1;
		snapPanel.add(turnOnGridSnap, snapPanelConstraints);
		
		//3. sor - SideExtentionSnap
		snapPanelConstraints.gridx = 0;
		snapPanelConstraints.gridy++;
		snapPanelConstraints.gridwidth = 4;
		snapPanelConstraints.anchor = GridBagConstraints.WEST;
		snapPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		snapPanelConstraints.weightx = 1;
		snapPanel.add( turnOnSideExtentionSnap, snapPanelConstraints);
		
		return snapPanel;
	}
}
