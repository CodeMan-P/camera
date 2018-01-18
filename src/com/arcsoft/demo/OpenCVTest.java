package com.arcsoft.demo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import com.arcsoft.ASVLOFFSCREEN;
import com.arcsoft.FaceInfo;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.sun.jna.Pointer;

public class OpenCVTest {
	public static String PATH = System.getProperty("user.dir") + File.separatorChar;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(Core.NATIVE_LIBRARY_NAME);
		// opencv_java249.dll
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// run();
		// runCam();
		opencvFD();
	}

	public static void opencvFD() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("\nRunning FaceDetector");
		AFRTest.initFDEngine();
		Pointer hFDEngine = AFRTest.hFDEngine;
		ASVLOFFSCREEN inputImgA;
		String filePathA = "0.jpg";
		inputImgA = AFRTest.loadImage(filePathA);
		FaceInfo[] faceInfosA = AFRTest.doFaceDetection(inputImgA);
		Mat image = Highgui.imread(filePathA);

		for (FaceInfo rect : faceInfosA) {
			Core.rectangle(image, new Point(rect.left, rect.top), new Point(rect.right, rect.bottom),
					new Scalar(0, 255, 0));
		}

		String filename = "ouput.png";
		System.out.println(String.format("Writing %s", filename));
		Highgui.imwrite(filename, image);
		image.release();
		AFRTest.UninitialAllEngine();
	}

	public static void runCam() {
		// opencv_ffmpeg249_64.dll
		VideoCapture cap = new VideoCapture(0);

		System.out.println(cap.isOpened());
		// 判断视频是否打开
		if (cap.isOpened()) {

			Mat frame = new Mat();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-ddHHmmsss");
			String date = sf.format(new Date()) + "_";
			for (int i = 0; i < 10; i++) {
				// 设置视频的位置(单位:毫秒)
				cap.set(opencv_highgui.CV_CAP_PROP_POS_MSEC, i * 1000);
				// 读取下一帧画面
				if (cap.read(frame)) {
					System.out.println("正在保存");
					// 保存画面到本地目录

					Highgui.imwrite(PATH + "images" + File.separator + date + i + ".jpg", frame);
				} else {
					break;
				}
			}
		}
		// 关闭视频文件
		cap.release();

	}

	public static void run() {
		VideoCapture cap = new VideoCapture("SomethingJustLikeThis.mp4");
		System.out.println(cap.isOpened());
		// 判断视频是否打开
		if (cap.isOpened()) {
			// 总帧数
			double frameCount = cap.get(opencv_highgui.CV_CAP_PROP_FRAME_COUNT);
			System.out.println("视频总帧数:" + frameCount);
			// 帧率
			double fps = cap.get(opencv_highgui.CV_CAP_PROP_FPS);
			System.out.println("视频帧率" + fps);
			// 时间长度
			double len = frameCount / fps;
			System.out.println("视频总时长:" + len);
			Double d_s = new Double(len);
			System.out.println(d_s.intValue());
			Mat frame = new Mat();
			for (int i = 0; i < d_s.intValue(); i++) {
				// 设置视频的位置(单位:毫秒)
				cap.set(opencv_highgui.CV_CAP_PROP_POS_MSEC, i * 30000);
				// 读取下一帧画面
				if (cap.read(frame)) {
					System.out.println("正在保存");
					// 保存画面到本地目录
					Highgui.imwrite(PATH + "images" + File.separator + i + ".jpg", frame);
				} else {
					break;
				}
			}
			// 关闭视频文件
			cap.release();
		}
	}
}
