/*	Solarys Calc - Console & graphical calculator written in Java
 * 	Copyright 2018 Yeshe Santos García <civyshk@gmail.com>
 *	
 *	This file is part of Solarys Calc
 *	
 *	Solarys Calc is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.project104.solaryscalc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class SolarysCalcGui extends JFrame {

	private JPanel panelMain;
	private JTextField txtInput;
	private JTextField txtResult;
	private ImageIcon iconOK, iconWrong;
	private JLabel lblOK;
	
	private Calculator calc;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SolarysCalcGui frame = new SolarysCalcGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SolarysCalcGui() {
		setTitle("Solaris Calc");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		setMinimumSize(new Dimension(300, 150));
		
		iconOK = new ImageIcon(getClass().getResource("/icon_true.png"));
		iconWrong = new ImageIcon(getClass().getResource("/icon_false.png"));
		
		panelMain = new JPanel();
		panelMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelMain);
		panelMain.setLayout(new BorderLayout(10, 10));
		
		JPanel panelTop = new JPanel();
//		panelMain.add(panelTop, BorderLayout.CENTER);
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.X_AXIS));
		
		JList panelStack = new JList();
		panelTop.add(panelStack);
		
		JTabbedPane panelFunctions = new JTabbedPane(JTabbedPane.TOP);
		panelTop.add(panelFunctions);
		
		JPanel tabF1 = new JPanel();
		panelFunctions.addTab("F1", null, tabF1, null);		
		tabF1.setLayout(new BoxLayout(tabF1, BoxLayout.X_AXIS));
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		tabF1.add(horizontalGlue_3);
		
		JLabel lblUnused = new JLabel("Tab functions 1 not implemented");
		lblUnused.setHorizontalAlignment(SwingConstants.CENTER);
		tabF1.add(lblUnused);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		tabF1.add(horizontalGlue_2);
		JPanel tabF2 = new JPanel();
		panelFunctions.addTab("F2", null, tabF2, null);
		tabF2.setLayout(new BoxLayout(tabF2, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		tabF2.add(horizontalGlue);
		
		JLabel lblTabFunctions = new JLabel("Tab functions 2 not implemented");
		lblTabFunctions.setHorizontalAlignment(SwingConstants.CENTER);
		tabF2.add(lblTabFunctions);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		tabF2.add(horizontalGlue_1);
		
		JPanel panelBottom = new JPanel();
//		panelMain.add(panelBottom, BorderLayout.PAGE_END);
		panelMain.add(panelBottom, BorderLayout.CENTER);
		panelBottom.setLayout(new BorderLayout(0, 5));
		
		JPanel panelResults = new JPanel();
		panelBottom.add(panelResults, BorderLayout.CENTER);
		panelResults.setLayout(new BorderLayout(0, 5));
		
		lblOK = new JLabel();
		lblOK.setIcon(iconOK);
		panelResults.add(lblOK, BorderLayout.LINE_START);
		
		txtResult = new JTextField();
		txtResult.setHorizontalAlignment(SwingConstants.CENTER);
		txtResult.setEditable(false);
		txtResult.setBorder(null);
		panelResults.add(txtResult, BorderLayout.CENTER);
		txtResult.setPreferredSize(new Dimension(txtResult.getMaximumSize()));
		
		txtInput = new JTextField();
		txtInput.setHorizontalAlignment(SwingConstants.CENTER);
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateResult(txtInput.getText());
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
		});

		panelBottom.add(txtInput, BorderLayout.PAGE_END);
		txtInput.setColumns(10);
		
		calc = new Calculator();
	}
	
	private void updateResult(String unparsedText) {
		unparsedText = unparsedText.trim();
		if(unparsedText.length() == 0) {
			txtResult.setText("");
			lblOK.setIcon(iconOK);			
		}else {
			RawText raw = new RawText(unparsedText, calc);
			try {
				String result = raw.getValue().toString();
				txtResult.setText(result);
				lblOK.setIcon(iconOK);
			}catch(Exception e) {
//				System.out.println(e);
				lblOK.setIcon(iconWrong);
			}
		}
	}

}
