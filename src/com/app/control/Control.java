package com.app.control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import com.app.global.Glob;
import com.app.gui.CameraPanel;
import com.app.gui.FtFrame;
import com.arcsoft.ASVLOFFSCREEN;
import com.arcsoft.FaceInfo;
import com.arcsoft.demo.AFRTest;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
public class Control extends Thread{
	static int camflag = 0;
	String flag;
	private final static int W = Glob.WI * 5 / 7;
	private final static int H = Glob.HI - 35;
	private static boolean isOpen = true;
	public Control(String flag) {
		this.flag=flag;
	}

//	private static Control con;
//
//	public static Control getControl() {
//		if (con == null) {
//			con = new Control();
//		}
//		return con;
//	}
	@Override
	public void run() {
		if(flag.equals("cam")){
			System.out.println("开启摄像头线程");
			runCam();
		}
	}
	private void runCam() {
		FtFrame ff = FtFrame.getFf();
		MatOfByte mob = new MatOfByte();
		Mat frame = new Mat();// 创建一个输出帧
		BufferedImage bf;
		VideoCapture camera = new VideoCapture(0);// 创建Opencv中的视频捕捉对象
//		CvCapture cv = opencv_highgui.cvCreateCameraCapture(0);
//		opencv_highgui.cvSetCaptureProperty(cv, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 300);
//		opencv_highgui.cvSetCaptureProperty(cv, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH, 200);
//		IplImage im;
		ASVLOFFSCREEN inputImagA;
//		int i = 0;
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-ddHHmmsss");
//		String date = sf.format(new Date()) + "_";
		if (!camera.isOpened()) {// isOpened函数用来判断摄像头调用是否成功
			System.out.println("Camera Error");// 如果摄像头调用失败，输出错误信息
		} else {
			while (isOpen) {
				if (!ff.curPalInfo.equals("camPal")) {
					
					break;
				} else {
					try {
					if(camera.read(frame)){// read方法读取摄像头的当前帧
							bf = getImage(frame, mob);
							//Highgui.imwrite(Glob.PATH + "images" + File.separator + date + (i++) + ".jpg", frame);
							if(bf!=null){
								inputImagA=AFRTest.loadImage(bf);
								if(inputImagA != null){
									FaceInfo[] faceInfosA =AFRTest.doFaceTrace(inputImagA);
									//FaceInfo[] faceInfosA = AFRTest.doFaceDetection(inputImagA);
									if(faceInfosA.length!=0){
										for (FaceInfo rect : faceInfosA) {
											Core.rectangle(frame, new Point(rect.left, rect.top), new Point(rect.right, rect.bottom),
													new Scalar(0, 255, 0));
										}
										bf = getImage(frame, mob);
									}
								}
								CameraPanel.label.setIcon(new ImageIcon(bf));// 转换图像格式并输出
							}
						}
					
						Thread.sleep(100);// 线程暂停100ms
					} catch (Exception e) {
						e.printStackTrace();
						camera.release();
						mob.release();
						frame.release();
					}
				}
			}
			System.out.println("关闭摄像头...");
		}
		camera.release();
		mob.release();
		frame.release();
	}

	public static BufferedImage getImage(Mat frame, MatOfByte mob) throws Exception{
		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		
		try {
			Highgui.imencode(".png", frame, mob);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		// convert the "matrix of bytes" into a byte array
		byte[] byteArray = mob.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImage;
	}

	public static boolean isOpen() {
		return isOpen;
	}

	public static void setOpen(boolean isOpen) {
		Control.isOpen = isOpen;
	}



}
