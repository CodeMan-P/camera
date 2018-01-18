package com.arcsodt.ft;

import com.arcsoft.ASVLOFFSCREEN;
import com.arcsoft.LoadUtils;
import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public interface AFT_FSDKLibrary extends Library {
	AFT_FSDKLibrary INSTANCE = (AFT_FSDKLibrary) LoadUtils.loadLibrary(
			Platform.isWindows() ? "libarcsoft_fsdk_face_tracking.dll" : "libarcsoft_fsdk_face_tracking.so",
			AFT_FSDKLibrary.class);

	/**
	 * 
	 * @param appidAppId
	 *            [in] 用户申请SDK时获取的App Id
	 * @param sdkid
	 *            [in] 用户申请SDK时获取的SDK Key
	 * @param pMem
	 *            [in] 分配给引擎使用的内存地址
	 * @param lMemSize
	 *            [in] 分配给引擎使用的内存大小
	 * @param phEngine
	 *            [out] 引擎handle
	 * @param iOrientPriority
	 *            [in] 期望的脸部检测角度的优先级
	 * @param nScale
	 *            [in] 用于数值表示的最小人脸尺寸 有效值范围[2,16] 推荐值 16
	 * @param nMaxFaceNum
	 *            [in] 用户期望引擎最多能检测出的人脸数 有效值范围[1,20]
	 * @return
	 */
	NativeLong AFT_FSDK_InitialFaceEngine(String appid, String sdkid, Pointer pMem, int lMemSize,
			PointerByReference phEngine, int iOrientPriority, int nScale, int nMaxFaceNum);

	/**
	 * 
	 * @param hEngine
	 *            [in] 引擎handle
	 * @param pImgData
	 *            [in] 带检测图像信息
	 * @param pFaceRes
	 *            [out] 人脸检测结果
	 * @return
	 */
	NativeLong AFT_FSDK_FaceFeatureDetect(Pointer hEngine, ASVLOFFSCREEN pImgData, PointerByReference pFaceRes);

	/**
	 * 
	 * @param hEngine
	 *            [in] 引擎handle
	 * @return
	 */
	NativeLong AFT_FSDK_UninitialFaceEngine(Pointer hEngine);

	/**
	 * 
	 * @param hEngine
	 *            [in] 引擎句柄
	 * @return
	 */
	AFT_FSDK_Version AFT_FSDK_GetVersion(Pointer hEngine);
}
