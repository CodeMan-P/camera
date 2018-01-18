package com.app.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.app.control.Control;
import com.app.global.Glob;

public class CameraPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6875654409834155784L;
	private final int W = Glob.WI*5/7;
	private final int H = Glob.HI-35;
	private static CameraPanel camPal;
//	private JScrollPane jsp = new JScrollPane();
//	private JScrollPane jsp2 = new JScrollPane();

	Font fontXK = new Font("华文行楷",Font.BOLD,18);
	Font fontK = new Font("宋体",Font.BOLD,18);
	
	public static JLabel label = new JLabel(new ImageIcon(Glob.PATH+"Icon"+File.separatorChar+"cam.jpg"));
	
	private CameraPanel() {
		init();
	}
	public static CameraPanel getCamPal(){
		if(camPal == null){
			camPal = new CameraPanel();
		}
		return camPal;
	}
	public void init(){
		FlowLayout f = new FlowLayout(FlowLayout.CENTER,40,10);
		setSize(W, H);
		setBackground(Color.GRAY);
		setLayout(null);
		setLocation(0, 0);
//		jsp.setLocation(0, 0);
//		jsp.setSize(W, (int)(H*1/2));
//		jsp.setWheelScrollingEnabled(true);
//		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//		jsp.getVerticalScrollBar().setUnitIncrement(20);
		
//		jsp2.setSize(W, (int)(H*2/6));
//		jsp2.setWheelScrollingEnabled(true);
//		jsp2.setAutoscrolls(true);
//		jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//		jsp2.getVerticalScrollBar().setUnitIncrement(20);
//		this.add(jsp);
//		this.add(jsp2);
		this.setLayout(null);
		label.setBounds(0, 0, this.getWidth(),this.getHeight());
		label.addMouseListener(new camListen());
		this.add(label);
	}
	public JLabel getLabel() {
		return label;
	}
	class camListen implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			//CameraPanel.label.setIcon(new ImageIcon(Glob.PATH+"Icon"+File.separatorChar+"0.jpg"));
			if(Control.isOpen()){
				Control.setOpen(false);
			}else{
				Control.setOpen(true);
				new Control("cam").start();	
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
