package com.app.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.app.global.Glob;

public class PicPanel extends JPanel{
	private final int W = Glob.WI*5/7;
	private final int H = Glob.HI-35;
	private static PicPanel picPal;
	Font fontXK = new Font("华文行楷",Font.BOLD,18);
	Font fontK = new Font("宋体",Font.BOLD,18);
	
	public static JLabel label = new JLabel(new ImageIcon(Glob.PATH+"Icon"+File.separatorChar+"cam.jpg"));
	
	private PicPanel() {
		init();
	}
	public static PicPanel getpicPal(){
		if(picPal == null){
			picPal = new PicPanel();
		}
		return picPal;
	}
	public void init(){
		FlowLayout f = new FlowLayout(FlowLayout.CENTER,40,10);
		setSize(W, H);
		setBackground(Color.GRAY);
		setLayout(null);
		setLocation(0, 0);

		this.setLayout(null);
		label.setBounds(0, 0, this.getWidth(),this.getHeight());
		this.add(label);
	}
}
