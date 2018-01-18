package com.app.gui;

import java.awt.Color;

import javax.swing.JPanel;

import com.app.global.Glob;

public class InfoPanel extends JPanel{

	private final int W = Glob.WI*2/7;
	private final int H = Glob.HI-35;
	private InfoPanel() {
		init();
	}
	private static InfoPanel infoPanel = new InfoPanel();
	public static InfoPanel getInfoPanel(){
		return infoPanel;
	}
	public void init(){
		setLocation(Glob.WI*5/7, 0);
		setSize (W, H);
		setVisible(true);
		setBackground(Color.blue);
	}
}
