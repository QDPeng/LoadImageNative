#include "com_example_loadimagenative_Native.h"
#include "opencv2/opencv.hpp"
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <string>
#include <vector>
#include <android/log.h>
using namespace cv;
// Utility for logging:
#define LOG_TAG    "CAMERA_RENDERER"
#define LOG(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

cv::VideoCapture capture;
cv::Mat inframe;

// Face detection
Mat grayscaleFrame;
cv::CascadeClassifier faceDetector;
int IMG_WIDTH = 240;
int IMG_HEIGHT = 180;

/*
 * Class:     com_example_loadimagenative_Native
 * Method:    initCamera
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_loadimagenative_Native_initCamera(
		JNIEnv * env, jclass clazz) {
	LOG("HHQ: Camera Created");
	capture.open(CV_CAP_ANDROID + 0);
	capture.set(CV_CAP_PROP_FRAME_WIDTH, 640);
	capture.set(CV_CAP_PROP_FRAME_HEIGHT, 480);
}
/*
 * Class:     com_example_loadimagenative_Native
 * Method:    getBytes
 * Signature: ()[B
 */

JNIEXPORT void JNICALL Java_com_example_loadimagenative_Native_getBytes(
		JNIEnv * env, jclass clazz, jlong frameAddress) {
//	LOG("HHQ: get Bytes");
	if (capture.isOpened()) {
		cv::Mat* pMatFrame = (cv::Mat*) frameAddress;

		capture >> inframe;
		cvtColor(inframe, inframe, CV_RGB2BGRA);


//		// PROCESS HERE
//		cvtColor(inframe, grayscaleFrame, CV_BGRA2GRAY);
//		float scale_w = float(IMG_WIDTH) / inframe.cols;
//		float scale_h = float(IMG_HEIGHT) / inframe.rows;
//
//		resize(grayscaleFrame, grayscaleFrame, Size(), scale_w, scale_h);
//		equalizeHist(grayscaleFrame, grayscaleFrame);
//
//		std::vector<cv::Rect> faces;
////		faceDetector.detectMultiScale(grayscaleFrame, faces, 1.1, 3, CV_HAAR_FIND_BIGGEST_OBJECT, cv::Size(30, 30));
//		LOG("HHQ: FACE DETECTION %d", faces.size());
//		for (int i = 0; i < faces.size(); i++)
//		{
//			faces[i].x = faces[i].x / scale_w;
//			faces[i].y = faces[i].y / scale_h;
//			faces[i].width = faces[i].width / scale_w;
//			faces[i].height = faces[i].height / scale_h;
//			cv::Point pt1(faces[i].x + faces[i].width, faces[i].y + faces[i].height);
//			cv::Point pt2(faces[i].x, faces[i].y);
//			cv::rectangle(inframe, pt1, pt2, cvScalar(0, 255, 0, 0), 1, 8, 0);
//		}


		inframe.copyTo(*pMatFrame);
		*pMatFrame = inframe.clone();
//		jsize len = inframe.total()* inframe.channels();
//
//		LOG("HHQ: Before env->NewByte Length: %d", len);
//
//		jbyteArray image = env->NewByteArray(len);
//					// Copy "rgb_image" to java native jbyteArray "image"
//		env->SetByteArrayRegion(image, 0, len, (jbyte*) inframe.data);
//		return image;
	}
	return;
}

/*
 * Class:     com_example_loadimagenative_Native
 * Method:    initCascade
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_example_loadimagenative_Native_initCascade(
		JNIEnv * env, jclass clazz, jstring cascasdePath) {
	try {
		const char *cascasdeString = env->GetStringUTFChars(cascasdePath,0);
		faceDetector.load(cascasdeString);
	} catch (cv::Exception e) {
	}
	if (faceDetector.empty()) {
		LOG("ERROR: Couldn't load Face Detector");
	}
}
