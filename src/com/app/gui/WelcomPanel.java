package com.app.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.app.global.Glob;

public class WelcomPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	JButton jb1 = new JButton("摄像识别");
	JButton jb2 = new JButton("照片识别");
	JButton jb3 = new JButton("视频识别");
	JLabel jlb = new JLabel("人脸识别系统");
	public WelcomPanel (){
		
		this.setLayout(null);
		this.setName("Welcom");
		this.setBounds(50, 55,Glob.WI-100, Glob.HI-170);
		this.setBackground(new Color(1.0f,1.0f,1.0f,0.3f));
		init();
	}
	void init(){
		Font f = new Font("宋体", Font.BOLD, 20);
		jb1.setFont(f);
		jb2.setFont(f);
		jb3.setFont(f);
		Font f2 = new Font("华文行楷", Font.ITALIC, 55);
		jlb.setFont(f2);
		jlb.setBounds(211, 27, 500, 70);
		jlb.setForeground(Color.BLACK);
		
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FtFrame.getFf().camPal.doClick();
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FtFrame.getFf().picPal.doClick();
			}
		});
		jb3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FtFrame.getFf().videoPal.doClick();
			}
		});
		int high = 300;
		int bgHigh = 50;
		int wap=20;
		jb1.setBounds(269, high, 250, bgHigh);
		jb2.setBounds(268, high+bgHigh+wap, 250, bgHigh);
		jb3.setBounds(269, (bgHigh+wap)*2+high, 250, bgHigh);
		this.add(jb1);
		this.add(jb2);
		this.add(jb3);
		this.add(jlb);
	}

}
