package com.arcsoft.demo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.arcsodt.ft.AFT_FSDKLibrary;
import com.arcsodt.ft.AFT_FSDK_FACERES;
import com.arcsodt.ft.AFT_FSDK_Version;
import com.arcsodt.ft._AFT_FSDK_OrientPriority;
import com.arcsoft.AFD_FSDKLibrary;
import com.arcsoft.AFD_FSDK_FACERES;
import com.arcsoft.AFD_FSDK_Version;
import com.arcsoft.AFR_FSDKLibrary;
import com.arcsoft.AFR_FSDK_FACEINPUT;
import com.arcsoft.AFR_FSDK_FACEMODEL;
import com.arcsoft.AFR_FSDK_Version;
import com.arcsoft.ASVLOFFSCREEN;
import com.arcsoft.ASVL_COLOR_FORMAT;
import com.arcsoft.CLibrary;
import com.arcsoft.FaceInfo;
import com.arcsoft.MRECT;
import com.arcsoft._AFD_FSDK_OrientPriority;
import com.arcsoft.utils.BufferInfo;
import com.arcsoft.utils.ImageLoader;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.PointerByReference;

public class AFRTest {
	public static final String APPID = "DiQc4sAMaYkmq2ipVW3mBJqAYCUqkdUygDULGA7Pig3Q";
	public static final String FD_SDKKEY = "2mQ4QsbrfJxih1LUTKE1ivLkQVf7APPeF4dd9QghmeZj";
	public static final String FR_SDKKEY = "2mQ4QsbrfJxih1LUTKE1ivM7thSacxJ3UvcuZdkUNjf7";
	public static final String FT_SDKKEY = "2mQ4QsbrfJxih1LUTKE1ivLdF6Pt2JqyQZYqZPagjtm8";
	public static final String AGE_SDKKEY = "2mQ4QsbrfJxih1LUTKE1ivMVNuE7pFPthUe3zMq8bw2i";
	public static final String GENDER_SDKKEY = "2mQ4QsbrfJxih1LUTKE1ivMcYJVH78JvUWyiEQoaYjof";
	public static final int FD_WORKBUF_SIZE = 20 * 1024 * 1024;
	public static final int FR_WORKBUF_SIZE = 40 * 1024 * 1024;
	public static final int FT_WORKBUF_SIZE = 40 * 1024 * 1024;
	public static final int MAX_FACE_NUM = 10;

	public static final boolean bUseRAWFile = false;
	public static final boolean bUseBGRToEngine = true;
	public static Pointer hFDEngine;
	public static Pointer hFREngine;
	public static Pointer hFTEngine;
	public static Pointer pFDWorkMem = CLibrary.INSTANCE.malloc(FD_WORKBUF_SIZE);
	public static Pointer pFRWorkMem = CLibrary.INSTANCE.malloc(FR_WORKBUF_SIZE);
	public static Pointer pFTWorkMem = CLibrary.INSTANCE.malloc(FT_WORKBUF_SIZE);

	public AFRTest() {
	}

	@Test
	public void compareFromByteFile() {
		String imgPath = "0.jpg";
		
		try {
			
			initAllEngine();
			File f;
			FileInputStream fis;
			AFR_FSDK_FACEMODEL faceFeatureX;
			float result;
			
			ASVLOFFSCREEN asvl = loadImage(imgPath);
			System.out.println(asvl.i32Height);
			LinkedList<byte[]> temp = getFaceBytes(asvl);
			for(byte[] b:temp){
				System.out.println("-------+++++++++++++-----------------");
				faceFeatureX = AFR_FSDK_FACEMODEL.fromByteArray(b);	
				for(int i = 1;i<=4;i++){
					asvl=loadImage("0"+i+".jpg");
					result= compareFaceSimilarity(faceFeatureX,asvl);
					System.out.println(result);
				};
				System.out.println("-------+++++++++++++-----------------");
			}
			
			System.out.println("-------+++++++++++++-----------------");
			ASVLOFFSCREEN asvl2=loadImage(imgPath);
			for(int i = 1;i<=4;i++){
				asvl=loadImage("0"+i+".jpg");
				result= compareFaceSimilarity(asvl2,asvl);
				System.out.println(result);
			};
			System.out.println("-------+++++++++++++-----------------");
			
//			byte[] b;
//			for(int i = 0;i<=3;i++){
//				f = new File(i+"_Face");
//				fis = new FileInputStream(f);
//				b = new byte[22020];
//				fis.read(b);
//				System.out.println("读取：" + i);
//				fis.close();
//				faceFeatureX = AFR_FSDK_FACEMODEL.fromByteArray(b);
//				
//				result= compareFaceSimilarity(faceFeatureX, asvl);
//				System.out.println(result);
//				
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		UninitialAllEngine();
	}

	@Test
	public void testM() {
		String imgPath = "0.jpg";

		try {
			File f = new File(imgPath);
			FileOutputStream fos;
			if (f.isFile()) {
				initAllEngine();
				System.out.println(f.getAbsolutePath());
				System.out.println();
				ASVLOFFSCREEN asvl = loadImage(imgPath);
				System.out.println(asvl.i32Height);
				LinkedList<byte[]> temp = getFaceBytes(asvl);
				System.out.println(temp.size());
				int i = 0;
				for (byte[] b : temp) {
					f = new File(i + "_Face");
					i++;
					f.createNewFile();
					fos = new FileOutputStream(f);
					fos.write(b);
					System.out.println(b.length);
					fos.flush();
					fos.close();
				}
			} else {

				System.out.println(f.getAbsolutePath());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UninitialAllEngine();
	}

	public static void main(String[] args) {

		initFDEngine();
		initFREngine();

		// load Image Data
		ASVLOFFSCREEN inputImgA;
		ASVLOFFSCREEN inputImgB;
		if (bUseRAWFile) {
			String filePathA = "001_640x480_I420.YUV";
			int yuv_widthA = 640;
			int yuv_heightA = 480;
			int yuv_formatA = ASVL_COLOR_FORMAT.ASVL_PAF_I420;

			String filePathB = "003_640x480_I420.YUV";
			int yuv_widthB = 640;
			int yuv_heightB = 480;
			int yuv_formatB = ASVL_COLOR_FORMAT.ASVL_PAF_I420;

			inputImgA = loadRAWImage(filePathA, yuv_widthA, yuv_heightA, yuv_formatA);
			inputImgB = loadRAWImage(filePathB, yuv_widthB, yuv_heightB, yuv_formatB);
		} else {
			String filePathA = "00.jpg";
			String filePathB = "01.jpg";

			inputImgA = loadImage(filePathA);
			inputImgB = loadImage(filePathB);
		}
		System.out.println("#####################相似度####################");
		System.out.println(
				String.format("similarity between faceA and faceB is %f", compareFaceSimilarity(inputImgA, inputImgB)));
		System.out.println("##############################################");
		UninitialAllEngine();

	}

	public static void UninitialAllEngine() {
		if (hFDEngine != null) {
			UninitialFDEngine();
		}
		if (hFTEngine != null) {
			UninitialFTEngine();
		}
		if (hFREngine != null) {
			UninitialFREngine();
		}
	}

	public static void UninitialFTEngine() {
		System.out.println("销毁FT引擎...");
		AFT_FSDKLibrary.INSTANCE.AFT_FSDK_UninitialFaceEngine(hFTEngine);
		System.out.println("释放FT内存...");
		CLibrary.INSTANCE.free(pFTWorkMem);
	}

	public static void UninitialFDEngine() {
		System.out.println("销毁FD引擎...");
		AFD_FSDKLibrary.INSTANCE.AFD_FSDK_UninitialFaceEngine(hFDEngine);
		System.out.println("释放FD内存...");
		CLibrary.INSTANCE.free(pFDWorkMem);
	}

	public static void UninitialFREngine() {
		System.out.println("销毁FR引擎...");
		AFR_FSDKLibrary.INSTANCE.AFR_FSDK_UninitialEngine(hFREngine);
		System.out.println("释放FR内存...");
		CLibrary.INSTANCE.free(pFRWorkMem);
	}

	public static void initFDEngine() {

		PointerByReference phFDEngine = new PointerByReference();
		// 初始化人脸检测引擎
		NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_InitialFaceEngine(APPID, FD_SDKKEY, pFDWorkMem,
				FD_WORKBUF_SIZE, phFDEngine, _AFD_FSDK_OrientPriority.AFD_FSDK_OPF_0_HIGHER_EXT, 32, MAX_FACE_NUM);
		if (ret.longValue() != 0) {
			CLibrary.INSTANCE.free(pFDWorkMem);
			System.out.println(String.format("AFD_FSDK_InitialFaceEngine ret 0x%x", ret.longValue()));
			System.exit(0);
		}

		// print FDEngine version
		hFDEngine = phFDEngine.getValue();
		AFD_FSDK_Version versionFD = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_GetVersion(hFDEngine);
		System.out.println("#####################################################");
		System.out.println("人脸检测（FD）引擎版本：");
		System.out.println(String.format("%d %d %d %d", versionFD.lCodebase, versionFD.lMajor, versionFD.lMinor,
				versionFD.lBuild));
		System.out.println(versionFD.Version);
		System.out.println(versionFD.BuildDate);
		System.out.println(versionFD.CopyRight);
		System.out.println("#####################################################");
	}

	public static void initFREngine() {
		PointerByReference phFREngine = new PointerByReference();
		NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_InitialEngine(APPID, FR_SDKKEY, pFRWorkMem, FR_WORKBUF_SIZE,
				phFREngine);
		if (ret.longValue() != 0) {
			UninitialAllEngine();
			System.out.println(String.format("AFR_FSDK_InitialEngine ret 0x%x", ret.longValue()));
			System.exit(0);
		}
		hFREngine = phFREngine.getValue();

		AFR_FSDK_Version versionFR = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_GetVersion(hFREngine);
		System.out.println("#####################################################");
		System.out.println("人脸识别（FR）引擎版本：");
		System.out.println(String.format("%d %d %d %d", versionFR.lCodebase, versionFR.lMajor, versionFR.lMinor,
				versionFR.lBuild));
		System.out.println(versionFR.Version);
		System.out.println(versionFR.BuildDate);
		System.out.println(versionFR.CopyRight);
		System.out.println("#####################################################");
	}

	public static void initFTEngine() {
		PointerByReference phFTEngine = new PointerByReference();
		NativeLong ret = AFT_FSDKLibrary.INSTANCE.AFT_FSDK_InitialFaceEngine(APPID, FT_SDKKEY, pFTWorkMem,
				FT_WORKBUF_SIZE, phFTEngine, _AFT_FSDK_OrientPriority.AFT_FSDK_OPF_0_HIGHER_EXT, 16, MAX_FACE_NUM);
		if (ret.longValue() != 0) {
			UninitialAllEngine();
			System.out.println(String.format("AFT_FSDK_InitialEngine ret 0x%x", ret.longValue()));
			System.exit(0);
		}
		hFTEngine = phFTEngine.getValue();

		AFT_FSDK_Version version = AFT_FSDKLibrary.INSTANCE.AFT_FSDK_GetVersion(hFTEngine);
		System.out.println("#####################################################");
		System.out.println("人脸跟踪（FT）引擎版本：");
		System.out.println(
				String.format("%d %d %d %d", version.lCodebase, version.lMajor, version.lMinor, version.lBuild));
		System.out.println(version.Version);
		System.out.println(version.BuildDate);
		System.out.println(version.CopyRight);
		System.out.println("#####################################################");
	}

	public static void initAllEngine() {
		initFDEngine();
		initFREngine();
		initFTEngine();
	}

	public static FaceInfo[] doFaceDetection(ASVLOFFSCREEN inputImg) {
		FaceInfo[] faceInfo = new FaceInfo[0];

		PointerByReference ppFaceRes = new PointerByReference();
		NativeLong ret = AFD_FSDKLibrary.INSTANCE.AFD_FSDK_StillImageFaceDetection(hFDEngine, inputImg, ppFaceRes);
		if (ret.longValue() != 0) {
			System.out.println(String.format("AFD_FSDK_StillImageFaceDetection ret 0x%x", ret.longValue()));
			return faceInfo;
		}

		AFD_FSDK_FACERES faceRes = new AFD_FSDK_FACERES(ppFaceRes.getValue());
		if (faceRes.nFace > 0) {
			faceInfo = new FaceInfo[faceRes.nFace];
			for (int i = 0; i < faceRes.nFace; i++) {
				MRECT rect = new MRECT(
						new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
				int orient = faceRes.lfaceOrient.getPointer().getInt(i * 4);
				faceInfo[i] = new FaceInfo();

				faceInfo[i].left = rect.left;
				faceInfo[i].top = rect.top;
				faceInfo[i].right = rect.right;
				faceInfo[i].bottom = rect.bottom;
				faceInfo[i].orient = orient;
				// 存储人脸模型
				// byte[] fFA = faceFeatureA.toByteArray();
				// AFR_FSDK_FACEMODEL faceFeatureX =
				// AFR_FSDK_FACEMODEL.fromByteArray(fFA);

				// System.out.println(String.format("%d (%d %d %d %d) orient
				// %d", i, rect.left, rect.top, rect.right,rect.bottom,
				// orient));
			}
		}
		return faceInfo;
	}

	public static FaceInfo[] doFaceTrace(ASVLOFFSCREEN inputImg) {
		FaceInfo[] faceInfo = new FaceInfo[0];

		PointerByReference ppFaceRes = new PointerByReference();
		NativeLong ret = AFT_FSDKLibrary.INSTANCE.AFT_FSDK_FaceFeatureDetect(hFTEngine, inputImg, ppFaceRes);
		if (ret.longValue() != 0) {
			System.out.println(String.format("AFT_FSDK_StillImageFaceDetection ret 0x%x", ret.longValue()));
			return faceInfo;
		}
		AFT_FSDK_FACERES faceRes = new AFT_FSDK_FACERES(ppFaceRes.getValue());
		// System.out.println(faceRes.nFace);
		// System.out.println(faceRes.rcFace);
		if (faceRes.nFace > 0) {
			faceInfo = new FaceInfo[faceRes.nFace];
			for (int i = 0; i < faceRes.nFace; i++) {
				MRECT rect = new MRECT(
						new Pointer(Pointer.nativeValue(faceRes.rcFace.getPointer()) + faceRes.rcFace.size() * i));
				faceInfo[i] = new FaceInfo();
				faceInfo[i].left = rect.left;
				faceInfo[i].top = rect.top;
				faceInfo[i].right = rect.right;
				faceInfo[i].bottom = rect.bottom;
				// System.out.println(String.format("%d (%d %d %d %d)", i,
				// rect.left, rect.top, rect.right,rect.bottom));
			}
		}
		return faceInfo;
	}

	public static LinkedList<byte[]> getFaceBytes(ASVLOFFSCREEN inputImg) throws Exception {
		LinkedList<byte[]> result = new LinkedList<byte[]>();
		FaceInfo[] faces = doFaceDetection(inputImg);
		AFR_FSDK_FACEMODEL tempModel;
		byte[] tempByte;
		for (FaceInfo temp : faces) {
			tempModel = extractFRFeature(inputImg, temp);
			tempByte = tempModel.toByteArray();
			result.add(tempByte);
		}
		return result;
	}

	public static AFR_FSDK_FACEMODEL extractFRFeature(ASVLOFFSCREEN inputImg, FaceInfo faceInfo) {

		AFR_FSDK_FACEINPUT faceinput = new AFR_FSDK_FACEINPUT();
		faceinput.lOrient = faceInfo.orient;
		faceinput.rcFace.left = faceInfo.left;
		faceinput.rcFace.top = faceInfo.top;
		faceinput.rcFace.right = faceInfo.right;
		faceinput.rcFace.bottom = faceInfo.bottom;

		AFR_FSDK_FACEMODEL faceFeature = new AFR_FSDK_FACEMODEL();
		NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_ExtractFRFeature(hFREngine, inputImg, faceinput,
				faceFeature);
		if (ret.longValue() != 0) {
			System.out.println(String.format("AFR_FSDK_ExtractFRFeature ret 0x%x", ret.longValue()));
			return null;
		}

		try {
			return faceFeature.deepCopy();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static float compareFaceSimilarity(ASVLOFFSCREEN inputImgA, ASVLOFFSCREEN inputImgB) {
		// Do Face Detect
		FaceInfo[] faceInfosA = doFaceDetection(inputImgA);
		if (faceInfosA.length < 1) {
			System.out.println("no face in Image A ");
			return 0.0f;
		}

		FaceInfo[] faceInfosB = doFaceDetection(inputImgB);
		if (faceInfosB.length < 1) {
			System.out.println("no face in Image B ");
			return 0.0f;
		}

		// Extract Face Feature
		AFR_FSDK_FACEMODEL faceFeatureA = extractFRFeature(inputImgA, faceInfosA[0]);
		if (faceFeatureA == null) {
			System.out.println("extract face feature in Image A failed");
			return 0.0f;
		}
		// 存储人脸模型
		// byte[] fFA = faceFeatureA.toByteArray();
		// AFR_FSDK_FACEMODEL faceFeatureX =
		// AFR_FSDK_FACEMODEL.fromByteArray(fFA);

		AFR_FSDK_FACEMODEL faceFeatureB = extractFRFeature(inputImgB, faceInfosB[0]);
		if (faceFeatureB == null) {
			System.out.println("extract face feature in Image B failed");
			faceFeatureA.freeUnmanaged();
			return 0.0f;
		}

		// calc similarity between faceA and faceB
		// 比对人脸相似度
		FloatByReference fSimilScore = new FloatByReference(0.0f);
		NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, faceFeatureA, faceFeatureB,
				fSimilScore);
		faceFeatureA.freeUnmanaged();
		faceFeatureB.freeUnmanaged();

		if (ret.longValue() != 0) {
			System.out.println(String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x", ret.longValue()));
			return 0.0f;
		}
		return fSimilScore.getValue();
	}

	public static float compareFaceSimilarity(AFR_FSDK_FACEMODEL faceFeatureA, ASVLOFFSCREEN inputImgB) {

		FaceInfo[] faceInfosB = doFaceDetection(inputImgB);
		if (faceInfosB.length < 1) {
			System.out.println("no face in Image B ");
			return 0.0f;
		}

		if (faceFeatureA == null) {
			System.out.println("extract face feature in Image A failed");
			return 0.0f;
		}
		// 存储人脸模型
		// byte[] fFA = faceFeatureA.toByteArray();
		// AFR_FSDK_FACEMODEL faceFeatureX =
		// AFR_FSDK_FACEMODEL.fromByteArray(fFA);

		AFR_FSDK_FACEMODEL faceFeatureB = extractFRFeature(inputImgB, faceInfosB[0]);
		if (faceFeatureB == null) {
			System.out.println("extract face feature in Image B failed");
			faceFeatureA.freeUnmanaged();
			return 0.0f;
		}

		// calc similarity between faceA and faceB
		// 比对人脸相似度
		FloatByReference fSimilScore = new FloatByReference(0.0f);
		NativeLong ret = AFR_FSDKLibrary.INSTANCE.AFR_FSDK_FacePairMatching(hFREngine, faceFeatureA, faceFeatureB,
				fSimilScore);
		faceFeatureA.freeUnmanaged();
		faceFeatureB.freeUnmanaged();

		if (ret.longValue() != 0) {
			System.out.println(String.format("AFR_FSDK_FacePairMatching failed:ret 0x%x", ret.longValue()));
			return 0.0f;
		}
		return fSimilScore.getValue();
	}

	public static ASVLOFFSCREEN loadRAWImage(String yuv_filePath, int yuv_width, int yuv_height, int yuv_format) {
		int yuv_rawdata_size = 0;

		ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();
		inputImg.u32PixelArrayFormat = yuv_format;
		inputImg.i32Width = yuv_width;
		inputImg.i32Height = yuv_height;
		if (ASVL_COLOR_FORMAT.ASVL_PAF_I420 == inputImg.u32PixelArrayFormat) {
			inputImg.pi32Pitch[0] = inputImg.i32Width;
			inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
			inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
			yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV12 == inputImg.u32PixelArrayFormat) {
			inputImg.pi32Pitch[0] = inputImg.i32Width;
			inputImg.pi32Pitch[1] = inputImg.i32Width;
			yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV21 == inputImg.u32PixelArrayFormat) {
			inputImg.pi32Pitch[0] = inputImg.i32Width;
			inputImg.pi32Pitch[1] = inputImg.i32Width;
			yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3 / 2;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_YUYV == inputImg.u32PixelArrayFormat) {
			inputImg.pi32Pitch[0] = inputImg.i32Width * 2;
			yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 2;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8 == inputImg.u32PixelArrayFormat) {
			inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
			yuv_rawdata_size = inputImg.i32Width * inputImg.i32Height * 3;
		} else {
			System.out.println("unsupported  yuv format");
			System.exit(0);
		}

		// load YUV Image Data from File
		byte[] imagedata = new byte[yuv_rawdata_size];
		File f = new File(yuv_filePath);
		InputStream ios = null;
		try {
			ios = new FileInputStream(f);
			ios.read(imagedata, 0, yuv_rawdata_size);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in loading yuv file");
			System.exit(0);
		} finally {
			try {
				if (ios != null) {
					ios.close();
				}
			} catch (IOException e) {
			}
		}

		if (ASVL_COLOR_FORMAT.ASVL_PAF_I420 == inputImg.u32PixelArrayFormat) {
			inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height,
					inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[2].write(0, imagedata,
					inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2,
					inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[3] = Pointer.NULL;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV12 == inputImg.u32PixelArrayFormat) {
			inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height,
					inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[2] = Pointer.NULL;
			inputImg.ppu8Plane[3] = Pointer.NULL;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_NV21 == inputImg.u32PixelArrayFormat) {
			inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[1].write(0, imagedata, inputImg.pi32Pitch[0] * inputImg.i32Height,
					inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[2] = Pointer.NULL;
			inputImg.ppu8Plane[3] = Pointer.NULL;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_YUYV == inputImg.u32PixelArrayFormat) {
			inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[0].write(0, imagedata, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[1] = Pointer.NULL;
			inputImg.ppu8Plane[2] = Pointer.NULL;
			inputImg.ppu8Plane[3] = Pointer.NULL;
		} else if (ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8 == inputImg.u32PixelArrayFormat) {
			inputImg.ppu8Plane[0] = new Memory(imagedata.length);
			inputImg.ppu8Plane[0].write(0, imagedata, 0, imagedata.length);
			inputImg.ppu8Plane[1] = Pointer.NULL;
			inputImg.ppu8Plane[2] = Pointer.NULL;
			inputImg.ppu8Plane[3] = Pointer.NULL;
		} else {
			System.out.println("unsupported yuv format");
			System.exit(0);
		}

		inputImg.setAutoRead(false);
		return inputImg;
	}

	public static ASVLOFFSCREEN loadImage(String filePath) {
		ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

		if (bUseBGRToEngine) {
			BufferInfo bufferInfo = ImageLoader.getBGRFromFile(filePath);
			inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
			inputImg.i32Width = bufferInfo.width;
			inputImg.i32Height = bufferInfo.height;
			inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
			inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[1] = Pointer.NULL;
			inputImg.ppu8Plane[2] = Pointer.NULL;
			inputImg.ppu8Plane[3] = Pointer.NULL;
		} else {
			BufferInfo bufferInfo = ImageLoader.getI420FromFile(filePath);
			inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_I420;
			inputImg.i32Width = bufferInfo.width;
			inputImg.i32Height = bufferInfo.height;
			inputImg.pi32Pitch[0] = inputImg.i32Width;
			inputImg.pi32Pitch[1] = inputImg.i32Width / 2;
			inputImg.pi32Pitch[2] = inputImg.i32Width / 2;
			inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
			inputImg.ppu8Plane[1] = new Memory(inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[1].write(0, bufferInfo.buffer, inputImg.pi32Pitch[0] * inputImg.i32Height,
					inputImg.pi32Pitch[1] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[2] = new Memory(inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[2].write(0, bufferInfo.buffer,
					inputImg.pi32Pitch[0] * inputImg.i32Height + inputImg.pi32Pitch[1] * inputImg.i32Height / 2,
					inputImg.pi32Pitch[2] * inputImg.i32Height / 2);
			inputImg.ppu8Plane[3] = Pointer.NULL;
		}

		inputImg.setAutoRead(false);
		return inputImg;
	}

	public static ASVLOFFSCREEN loadImage(BufferedImage img) {
		ASVLOFFSCREEN inputImg = new ASVLOFFSCREEN();

		BufferInfo bufferInfo = ImageLoader.getBGRFromFile(img);
		if (bufferInfo == null) {
			return null;
		}
		inputImg.u32PixelArrayFormat = ASVL_COLOR_FORMAT.ASVL_PAF_RGB24_B8G8R8;
		inputImg.i32Width = bufferInfo.width;
		inputImg.i32Height = bufferInfo.height;
		inputImg.pi32Pitch[0] = inputImg.i32Width * 3;
		inputImg.ppu8Plane[0] = new Memory(inputImg.pi32Pitch[0] * inputImg.i32Height);
		inputImg.ppu8Plane[0].write(0, bufferInfo.buffer, 0, inputImg.pi32Pitch[0] * inputImg.i32Height);
		inputImg.ppu8Plane[1] = Pointer.NULL;
		inputImg.ppu8Plane[2] = Pointer.NULL;
		inputImg.ppu8Plane[3] = Pointer.NULL;

		inputImg.setAutoRead(false);
		return inputImg;
	}
}
