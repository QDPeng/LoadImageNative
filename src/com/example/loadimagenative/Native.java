package com.example.loadimagenative;
//javah -jni -classpath bin/classes/ -d jni/ com.example.loadimagenative.Native

public class Native {
	public static void loadlibs(){
		System.loadLibrary("opencv_java");
		System.loadLibrary("NativeCamera");
	}
	public static native void initCamera();
	public static native void getBytes(long inFrameAddress);
	public static native void initCascade(String absolutePath);
}
