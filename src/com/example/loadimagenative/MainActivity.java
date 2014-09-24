package com.example.loadimagenative;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter.LengthFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageThread threadCamera = null;
	private Mat inframe;
	private File mCascadeFile; 
	
	long previousTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Native.loadlibs();
		Native.initCamera();
		initOpenCV();
		Native.initCascade(mCascadeFile.getAbsolutePath());
		inframe = new Mat(640, 480, CvType.CV_32F);
		threadCamera = new ImageThread();
		new Thread(threadCamera).start();

	}

	private void initOpenCV(){
		 InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
		 File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
		 mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
		 FileOutputStream os = null;
 		 try {
			os = new FileOutputStream(mCascadeFile);
		 
		 
		 byte[] buffer = new byte[4096];
		 int bytesRead;
		 
			while ((bytesRead = is.read(buffer)) != -1) {
				 os.write(buffer, 0, bytesRead);
			 }
		 
			is.close();
			os.close();
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 } catch (Exception e){
			 e.printStackTrace();
		 }
 		 Log.d("HHQ", "HHQ " + mCascadeFile.getAbsolutePath());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class ImageThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				getImage();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void getImage() {
			// TODO Auto-generated method stub
			while (true){
//				if(System.currentTimeMillis() - previousTime > 1000/30)
//				{
//					Log.d("HHQ", "IN GET IMAGE %d" + (System.currentTimeMillis() - previousTime));
//					previousTime = System.currentTimeMillis();
//					Native.getBytes(inframe.getNativeObjAddr());
//				}
				Native.getBytes(inframe.getNativeObjAddr());
				runOnUiThread(new Runnable() {
					public void run() {
						ImageView image = (ImageView) findViewById(R.id.imageView);
						image.setScaleType(ImageView.ScaleType.FIT_XY);

						Bitmap yourSelectedImage = Bitmap.createBitmap(
								inframe.cols(), inframe.rows(),
								Bitmap.Config.ARGB_8888);
						Utils.matToBitmap(inframe, yourSelectedImage);

						image.setImageBitmap(yourSelectedImage);
						image.invalidate();

					}
				});
			}
		}
	}
}
