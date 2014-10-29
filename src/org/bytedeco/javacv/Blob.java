package org.bytedeco.javacv;

import static org.bytedeco.javacpp.opencv_core.CV_AA;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCircle;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvPointFrom32f;
import static org.bytedeco.javacpp.opencv_core.cvResetImageROI;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_HOUGH_GRADIENT;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCanny;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvHoughCircles;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvPoint3D32f;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;

public class Blob {
	public static final boolean  PREVIEW_OFF = false
								,PREVIEW_ON = true;
	private int maxthresh,minthresh
	,mincanny,maxcanny
	,smoothparam1,smoothparam2,smoothparam3,smoothparam4
	,minDistance,minRadius,maxRadius,houghparam1,houghparam2;
	private int[] arx,ary;
	public Blob(){
		maxthresh = 255;
		minthresh = 210;
		mincanny = 0;
		maxcanny = 0;
		smoothparam1 = 9;
		smoothparam2 = 9;
		smoothparam3 = 2;
		smoothparam4 = 2;
		
		minDistance = 155;
		houghparam1 = 100;
		houghparam2 = 45;
		minRadius = 30;
		maxRadius = 55;
	}
	public void setattributes(
			int maxthresh
			,int minthresh
			,int mincanny
			,int maxcanny
			,int smoothparam1
			,int smoothparam2
			,int smoothparam3
			,int smoothparam4
			,int minDistance
			,int houghparam1
			,int houghparam2
			,int minRadius
			,int maxRadius){
		this.maxthresh = maxthresh;
		this.minthresh = minthresh;
		this.mincanny = mincanny;
		this.maxcanny = maxcanny;
		this.smoothparam1 = smoothparam1;
		this.smoothparam2 = smoothparam2;
		this.smoothparam3 = smoothparam3;
		this.smoothparam4 = smoothparam4;
		this.minDistance = minDistance;
		this.houghparam1 = houghparam1;
		this.houghparam2 = houghparam2;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
	}
	public int[][] detect(String image_source){
		IplImage imgxc1 = cvLoadImage(image_source);
		IplImage imgxd1 = cvCreateImage(cvGetSize(imgxc1), IPL_DEPTH_8U, 1);
		cvCvtColor(imgxc1, imgxd1, CV_BGR2GRAY);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);
		cvThreshold(imgxd1, imgxd1, minthresh, maxthresh, CV_THRESH_BINARY);
		CvMemStorage storage =CvMemStorage.create();
		cvCanny(imgxd1,imgxd1,mincanny,maxcanny);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);
		//minDistance = (int) (imgxd1.height()*.085);
		//Input image,Memory Storage, Detection method,Inverse ratio, Minimum distance, threshold,min radius,max radius
		CvSeq circles = cvHoughCircles(imgxd1
				,storage
				,CV_HOUGH_GRADIENT
				,1
				,minDistance
				,houghparam1
				,houghparam2
				,minRadius
				,maxRadius
				);
		ArrayList<CvPoint> centers = new ArrayList<CvPoint>();
		int radius = 0;
		for(int i = 0; i < circles.total(); i++){
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
			CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle));
			centers.add(center);
			radius = Math.round(circle.z());
//			System.out.println("Circles Detected info Point"+center.x()+","+center.y()+" Radius = "+circle.z());
//			cvCircle(imgxc1, center, radius, CvScalar.GREEN, 3, CV_AA, 0);
		}
//		ShowImage(imgxc1, "BWImage",512);
		imgxc1.release();
		imgxd1.release();
		cvReleaseImage(imgxc1);
		cvReleaseImage(imgxd1);
		return sortCircles(circles, centers,radius);
		
	}
	public int[][] detect(String image_source, String image_destination){
		IplImage imgxc1 = cvLoadImage(image_source);
		IplImage imgxd1 = cvCreateImage(cvGetSize(imgxc1), IPL_DEPTH_8U, 1);
		cvCvtColor(imgxc1, imgxd1, CV_BGR2GRAY);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);
		cvThreshold(imgxd1, imgxd1, minthresh, maxthresh, CV_THRESH_BINARY);
		CvMemStorage storage =CvMemStorage.create();
		cvCanny(imgxd1,imgxd1,mincanny,maxcanny);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);
		//minDistance = (int) (imgxd1.height()*.085);
		//Input image,Memory Storage, Detection method,Inverse ratio, Minimum distance, threshold,min radius,max radius
		CvSeq circles = cvHoughCircles(imgxd1
				,storage
				,CV_HOUGH_GRADIENT
				,1
				,minDistance
				,houghparam1
				,houghparam2
				,minRadius
				,maxRadius
				);
		ArrayList<CvPoint> centers = new ArrayList<CvPoint>();
		int radius = 0;
		for(int i = 0; i < circles.total(); i++){
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
			CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle));
			centers.add(center);
			radius = Math.round(circle.z());
//			System.out.println("Circles Detected info Point"+center.x()+","+center.y()+" Radius = "+circle.z());
			cvCircle(imgxc1, center, radius, CvScalar.GREEN, 3, CV_AA, 0);
		}
//		ShowImage(imgxc1, "BWImage",512);
		cvSaveImage(image_destination, imgxc1);
		imgxc1.release();
		imgxd1.release();
		cvReleaseImage(imgxc1);
		cvReleaseImage(imgxd1);
		return sortCircles(circles, centers,radius);
	}
	private int[][] sortCircles(CvSeq circles, ArrayList<CvPoint> centers,int radius) {
		CvPoint tmp = new CvPoint();
		tmp.x(centers.get(1).x());
		tmp.y(centers.get(1).y());
		Integer[][] data = new Integer[][] { 
			new Integer[] { centers.get(0).x(), centers.get(0).y() },
			new Integer[] { centers.get(1).x(), centers.get(1).y() },
			new Integer[] { centers.get(2).x(), centers.get(2).y() },
			new Integer[] { centers.get(3).x(), centers.get(3).y() }
		};
		Arrays.sort(data, new ArrayComparator(1));
		Integer[][] col1 = new Integer[][] { 
				new Integer[] { data[0][0], data[0][1] },
				new Integer[] { data[1][0], data[1][1] }
		};
		
		Integer[][] col2 = new Integer[][] { 
				new Integer[] { data[2][0], data[2][1] },
				new Integer[] { data[3][0], data[3][1] }
		};
		Arrays.sort(col1, new ArrayComparator(0));
		Arrays.sort(col2, new ArrayComparator(0));
		int[ ][ ] aryNumbers = { {col1[0][0],col1[0][1], radius},{col1[1][0],col1[1][1], radius}
								,{col2[0][0],col2[0][1], radius},{col2[1][0],col2[1][1], radius} };
		return aryNumbers;
	}

	
	private class ArrayComparator implements Comparator<Integer[]> {
		private int col;
		public ArrayComparator(int col){
			this.col = col;
		}
		public int compare(Integer[] s1, Integer[] s2) {
			if (s1[col] > s2[col])
				return 1;	// tells Arrays.sort() that s1 comes after s2
			else if (s1[col] < s2[col])
			return -1;	// tells Arrays.sort() that s1 comes before s2
			else {
			/*
			 * s1 and s2 are equal.  Arrays.sort() is stable,
			 * so these two rows will appear in their original order.
			 * You could take it a step further in this block by comparing
			 * s1[1] and s2[1] in the same manner, but it depends on how
			 * you want to sort in that situation.
			 */
				return 0;
			}
		}
	}
//	/***
//	 * Overloaded Show Image
//	 * @param image
//	 * @param caption
//	 * @param size
//	 */
//	private static void ShowImage(IplImage image, String caption, int size){
//		if(size < 128) size = 128;
//		CvMat mat = image.asCvMat();
//		int width = mat.cols(); if(width < 1) width = 1;
//		int height = mat.rows(); if(height < 1) height = 1;
//		double aspect = 1.0 * width / height;
//		if(height != size) { height = size; width = (int) ( height * aspect ); }
//		if(width != size) width = size;
//		height = (int) ( width / aspect );
//		ShowImage(image, caption, width, height);
//	}
//	/***
//	 * Show Image
//	 * @param image
//	 * @param caption
//	 * @param width
//	 * @param height
//	 */
//	private static void ShowImage(IplImage image, String caption, int width, int height)
//	{
//		CanvasFrame canvas = new CanvasFrame(caption, 1);	// gamma=1
//		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//		canvas.setCanvasSize(width, height);
//		canvas.showImage(image);
//	}
//	
//	
	
	
	public int[][] detect(String image_source,int param1,int param2,int param3,int param4,int param5,CvRect r){
		IplImage imgxc1 = cvLoadImage(image_source);
		IplImage imgxd1 = cvCreateImage(cvGetSize(imgxc1), IPL_DEPTH_8U, 1);
		cvCvtColor(imgxc1, imgxd1, CV_BGR2GRAY);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);
		cvThreshold(imgxd1, imgxd1, minthresh, maxthresh, CV_THRESH_BINARY);
		CvMemStorage storage =CvMemStorage.create();
		cvCanny(imgxd1,imgxd1,mincanny,maxcanny);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);

		cvSetImageROI(imgxd1, r);
		CvSeq circles = cvHoughCircles(imgxd1
				,storage
				,CV_HOUGH_GRADIENT
				,1
				,param1//minDistance
				,param2//houghparam1
				,param3//houghparam2
				,param4//minRadius
				,param5//maxRadius
				);
		cvResetImageROI(imgxd1);
		
		arx = new int[circles.total()];
		ary = new int[circles.total()];
		
		int total = 0;
		for(int i = 0; i < circles.total(); i++){
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
//			CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle));
			int radius = Math.round(circle.z());
			if(radius<=80){
				total++;
			}
		}
		int[ ][ ] aryCircles = new int[total][4];
		int j = 0;
		for(int i = 0; i < circles.total(); i++){
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
			CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle));
			//Fixing Centers
			center.x(center.x()+r.x());
			center.y(center.y()+r.y());
			int radius = Math.round(circle.z());
			if(radius<=80){
				aryCircles[j][0] = center.x();
				aryCircles[j][1] = center.y();
				aryCircles[j][2] = radius;
				aryCircles[j][3] = 0;
				j++;
				arx[i] = center.x();
				ary[i] = center.y();
				cvCircle(imgxc1, center, radius, CvScalar.GREEN, 3, CV_AA, 0);
			}
		}
//		ShowImage(imgxc1, "BWImage",512);
		imgxc1.release();
		imgxd1.release();
		cvReleaseImage(imgxc1);
		cvReleaseImage(imgxd1);
		return aryCircles;
	}
	public int[][] detect(String image_source,int param1,int param2,int param3,int param4,int param5,CvRect r,String image_destination){
		IplImage imgxc1 = cvLoadImage(image_source);
		IplImage imgxd1 = cvCreateImage(cvGetSize(imgxc1), IPL_DEPTH_8U, 1);
		cvCvtColor(imgxc1, imgxd1, CV_BGR2GRAY);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);
		cvThreshold(imgxd1, imgxd1, minthresh, maxthresh, CV_THRESH_BINARY);
		CvMemStorage storage =CvMemStorage.create();
		cvCanny(imgxd1,imgxd1,mincanny,maxcanny);
		cvSmooth(imgxd1,imgxd1,CV_GAUSSIAN,smoothparam1,smoothparam2,smoothparam3,smoothparam4);

		cvSetImageROI(imgxd1, r);
		CvSeq circles = cvHoughCircles(imgxd1
				,storage
				,CV_HOUGH_GRADIENT
				,1
				,param1//minDistance
				,param2//houghparam1
				,param3//houghparam2
				,param4//minRadius
				,param5//maxRadius
				);
		cvResetImageROI(imgxd1);
		
		arx = new int[circles.total()];
		ary = new int[circles.total()];
		
		int total = 0;
		int avg = 0;
		for(int i = 0; i < circles.total(); i++){
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
//			CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle));
			int radius = Math.round(circle.z());
			if(radius<=80){
				total++;
				avg += radius;
			}
		}
		avg = avg/total;
		int[ ][ ] aryCircles = new int[total][4];
//		aryCircles[0][2] = 0;
		int j = 0;
		for(int i = 0; i < circles.total(); i++){
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, i));
			CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle));
			//Fixing Centers
			center.x(center.x()+r.x());
			center.y(center.y()+r.y());
			int radius = Math.round(circle.z());
			if(radius<=80){
				aryCircles[j][0] = center.x();
				aryCircles[j][1] = center.y();
				aryCircles[j][2] = avg;
				aryCircles[j][3] = 0;
				j++;
				arx[i] = center.x();
				ary[i] = center.y();
				cvCircle(imgxc1, center, radius, CvScalar.GREEN, 3, CV_AA, 0);
			}
		}
//		aryCircles[0][2] = aryCircles[circles.total()-1][2]/ circles.total();
		cvSaveImage(image_destination, imgxc1);
		imgxc1.release();
		imgxd1.release();
		cvReleaseImage(imgxc1);
		cvReleaseImage(imgxd1);
		return aryCircles;
	}
	public int[] getarx(){
		return arx.clone();
	}

	public int[] getary(){
		return ary.clone();
	}

}
