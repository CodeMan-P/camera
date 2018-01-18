package com.app.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.opencv.core.Core;

import com.app.global.Glob;
import com.arcsoft.demo.AFRTest;

public class FtFrame extends JFrame{
	int WI = Glob.WI;
	int HI = Glob.HI ;
	JMenuBar jmb = new JMenuBar();
	JMenu main = new JMenu("菜单");
	JMenu pal = new JMenu("功能面板");
	JMenuItem camPal = new JMenuItem("摄像识别");
	JMenuItem picPal = new JMenuItem("照片识别");
	JMenuItem videoPal = new JMenuItem("视频识别");
	InfoPanel infoPanel;
	JMenuItem mainPanel = new JMenuItem("首页");
	JPanel wp= new WelcomPanel();
	final int camFlag = 1;
	final int picFlag = 2;
	final int videoFlag = 3;
	public JPanel currPanel = wp; 
	public String curPalInfo = "wp";
	private FtFrame() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		AFRTest.initAllEngine();
		//AFRTest.initFDEngine();
		//AFRTest.initFTEngine();
		initJMBar();
		initJFrame();
	}
	private static FtFrame ff = new FtFrame();
	public static FtFrame getFf() {
		return ff;
	}
	private void initJFrame() {
		
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.setTitle("人脸识别");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WI,HI);
		this.getContentPane().add(currPanel);
		this.setLocation((int)(this.getToolkit().getScreenSize().width/2-WI/2), 
				(int)(this.getToolkit().getScreenSize().height/2-HI/2));
		this.getContentPane().setBackground(new Color(185,211,238));
		this.addWindowListener(new closeWinow());
		infoPanel = InfoPanel.getInfoPanel();
		infoPanel.setVisible(false);
		this.add(infoPanel);
	}
	
	private void initJMBar() {
		main.add(mainPanel);
		pal.add(camPal);
		pal.add(picPal);
		pal.add(videoPal);
		jmb.add(main);
		jmb.add(pal);
		mainPanel.addActionListener(new changPanel(0));
		camPal.addActionListener(new changPanel(camFlag));
		picPal.addActionListener(new changPanel(picFlag));
		videoPal.addActionListener(new changPanel(videoFlag));
		
		this.setJMenuBar(jmb);
	}
	class changPanel implements ActionListener{

		JPanel jp;
		int flag;
		changPanel(int flag){
			this.flag = flag;
		}
		void initPanel() {//按需加载单实例，提升启动速度~
			
			if(flag == 0) {
				this.jp = wp;
				infoPanel.setVisible(false);
			}else if(flag == camFlag) {
				this.jp = CameraPanel.getCamPal();
				ff.curPalInfo="camPal";
				infoPanel.setVisible(true);
			}else if(flag == picFlag) {
				this.jp = PicPanel.getpicPal();
				infoPanel.setVisible(true);
			}else if(flag == videoFlag){
				this.jp = VideoPanel.getvideoPal();
				infoPanel.setVisible(true);
			}
		
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			initPanel();
			ff.setVisible(false);
			ff.getContentPane().remove(currPanel);
			ff.currPanel = jp;
			ff.getContentPane().add(currPanel);
			ff.setVisible(true);
		}
	}
	
	class closeWinow extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			
			AFRTest.UninitialAllEngine();
			System.out.println("关闭窗口ing.......");
			//DbConn.closeBds();
			//System.out.println("连接池关闭!");
		}
	}
}
