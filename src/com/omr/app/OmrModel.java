package com.omr.app;
import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvScalarAll;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_highgui.cvLoadImage;
import static org.bytedeco.javacpp.opencv_highgui.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static org.bytedeco.javacpp.opencv_imgproc.cv2DRotationMatrix;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvWarpAffine;
import helper.Options;
import helper.Questions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacv.Blob;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.omr.exceptions.CellsWrongDetection;
import com.omr.exceptions.ResolutionNotCorrect;
import com.omr.exceptions.UnableToDetectMarkers;
import com.omr.exceptions.UnableToDetectOptions;
import com.omr.exceptions.UnableToLoadImage;
import com.omr.exceptions.WrongFileAttributes;
import com.omr.exceptions.WrongMarkers;

import config.Config;
import couch.Seq;



public class OmrModel extends Config{
	/*
	* Attributes
	*/
	private Logger logger;
	private String filename,path,nameonly,aid,dkey;
	private CvPoint corntl,corntr,cornbl,cornbr;
	private Point orig;
	private int unit,avgradi,rthresh;
	private Options options;
	double rot=0.0,uerr;
	private Questions questions;
	private Seq seq;
	private String QrCode;
	public String getQrCode() {
		return QrCode;
	}

	public void setQrCode(String qrCode) {
		QrCode = qrCode;
	}

	private IplImage imgx,imgxd1, imgxc1;
	/*
	* Constructors
	*/
	public OmrModel(FileHandler fh){
		logger = Logger.getLogger(OmrModel.class.getName());
		logger.addHandler(fh);
		unit = 0;
		orig = new Point();
	}
	
	/*
	 * Methods
	 */
	public void setseq(Seq in){
		seq = in;
	}
	public void setqrinfo(String qrstr){
		setaid(qrstr);
		setdkey(getaid().substring(getaid().length() - 5));
	}
	public void setaid(String aid){
		this.aid = aid;
	}
	public void setdkey(String dkey){
		this.dkey = dkey;
	}
	public String getdkey(){
		return dkey;
	}
	public String getaid(){
		return aid;
	}
	public JsonObject getQSeq(){
		return seq.getqSeq();
	}
	public String getQnameat(int i){
		return seq.getqnameat(i);
	}
	public JsonObject getOptSeq(){
		return seq.getoptSeq();
	}
	public JsonObject getOptSeq(String queid){
		return (JsonObject) seq.getoptSeq().get(queid);
	}
	public int getOptCount(String queid){
		JsonObject ans = (JsonObject) seq.getoptSeq().get(queid);
		int count =0;
		if(ans.get("A")!=null) count++;
		if(ans.get("B")!=null) count++;
		if(ans.get("C")!=null) count++;
		if(ans.get("D")!=null) count++;
		if(ans.get("E")!=null) count++;
		if(ans.get("F")!=null) count++;
		return count;
	}
	public String getstudent(){
		return seq.getStudentName();
	}
	public int getQuestions(){
		JsonObject ques = getQSeq();
		int count =0;
		for (int i = 0; i <= 100; i++) {
			if(ques.get(String.valueOf(i)) != null){
				count++;
			}else{
				break;
			}
		}
		return count;
	}
	public int[] getoptions(){
		int opts[] = new int[getQuestions()];
		String label[] = {"A","B","C","D","E","F"};
		for (int i = 0; i < opts.length; i++) {
			int count = 0;
			for (int j = 0; j < 6; j++) {
				if(getOptSeq(getQnameat(i)).get(label[j]) != null){
					count++;
					opts[i] = count;
				}else{
					break;
				}
			}
			
		}
		return opts;
	}
	/***
	 * Initializing Model
	 * @return boolean
	 * @throws UnableToLoadImage 
	 */
	protected boolean init () throws UnableToLoadImage{
		imgx = new IplImage();
		imgxc1 = new IplImage();
		imgxd1 = new IplImage();
		imgx = cvLoadImage(path+DR+filename);
		if(imgx == null)throw new UnableToLoadImage(path+DR+filename);
		//cvSaveImage("debug/"+filename, imgx);
		imgxc1 = cvCreateImage(cvGetSize(imgx), imgx.depth(), imgx.nChannels());
		imgxc1 = imgx.clone();
		imgxd1 = cvCreateImage(cvGetSize(imgx), IPL_DEPTH_8U, 1);
		cvCvtColor(imgxc1, imgxd1, CV_BGR2GRAY);
		return (imgx != null && imgxc1 != null && imgxd1 != null)?true:false;
	}
	

	/***
	 * Looking for reference rectangles
	 * @param string 
	 * @throws UnableToDetectMarkers 
	 * @throws WrongFileAttributes 
	 * @throws WrongMarkers 
	 * 
	 */
	public boolean lookref(String stage) throws UnableToDetectMarkers, WrongFileAttributes, WrongMarkers{
		Blob blob = new Blob();
		setpoints(blob.detect(path+DR+filename,"debug/corners-"+getfilename()));
		return false;
	}

	private void setpoints(int[][] coll) {
		corntl = new CvPoint();
		corntl.x(coll[0][0] );corntl.y(coll[0][1] );
		corntr = new CvPoint();
		corntr.x(coll[1][0] );corntr.y(coll[1][1] );
		cornbl = new CvPoint();
		cornbl.x(coll[2][0] );cornbl.y(coll[2][1] );
		cornbr = new CvPoint();
		cornbr.x(coll[3][0] );cornbr.y(coll[3][1] );
		rthresh = coll[0][2];
		System.gc();
	}


	public String getfilename(){
		return filename;
	}
	public boolean setQuestions(int count){
		if(count<=99){
			logger.log(Level.INFO,"Actual Quesitions in Quiz are "+count);
			questions = new Questions(count, unit, imgxc1,orig,getcols(),getrows(),avgr());
			return true;
		}
		logger.log(Level.SEVERE,"Total Questions found from Server exceeded the limit of 99");
		return true;
	}
	public void fillQuestions(int[] optionscount) throws CellsWrongDetection{
		questions.addAllQuestions(optionscount,filename);
		//ShowImage(questions.getQgiven(), "QUESTION SHEET", 512);
	}
	public void setpaths(String name, String directory) throws WrongFileAttributes {
		try{
			filename = name;
			path = directory;
			nameonly = filename.substring(0,filename.lastIndexOf("."));
			logger.log(Level.INFO, "Selected Image "+path+DR+filename);
		} catch (StringIndexOutOfBoundsException e){
			throw new WrongFileAttributes(path,filename,nameonly);
		}
		
	}

	public String getPath(){
		return path;
	}
	public void circle() throws UnableToDetectOptions {
		
		CvRect r = new CvRect();
		r.x(corntl.x() +rthresh);
		r.y(corntl.y() +rthresh);
		r.height(Math.abs(corntl.y() - cornbl.y())+rthresh);
		r.width(Math.abs(corntl.x() - corntr.x())- (2*rthresh));
		Blob blob = new Blob();
		
		int[][] aryCircle = blob.detect(path+DR+filename,30,100,35,10,30,r,"debug/small-"+getfilename());
		avgradi = aryCircle[0][2];
		List<JsonObject> points = new ArrayList<JsonObject>();

		for(int i = 0; i < aryCircle.length; i++){
			JsonObject point = new JsonObject();
			point.addProperty("x", aryCircle[i][0]);
			point.addProperty("y", aryCircle[i][1]);
			point.addProperty("r", aryCircle[i][2]);
			point.addProperty("s", aryCircle[i][3]);
			points.add(point);
		}
		options = new Options(points, blob.getarx(), blob.getary(),65,45,65,45,330,300);
		options.organise(corntl.x());
	}
	

	public JsonArray getcols(){
		return options.getcols();
	}
	public JsonArray getrows(){
		return options.getrows();
	}
	public int avgr(){
		return avgradi;
	}

	public String[] getresults() {
		return questions.getAllOptions();
	}
	public void drawgrid(){
		cvSaveImage("debug/"+filename+"-rtQGrid.jpg",questions.drawQgrid());
	}
	public int[] getQrrect(){
		return new int[] {0, 0, corntr.x() - corntl.x(), corntl.y()};
	}

	public IplImage getiplimg() {
		IplImage imgxtmp2 = cvCreateImage(cvGetSize(imgxc1), imgxc1.depth(), imgxc1.nChannels());
		imgxtmp2 = imgxc1.clone();
		return imgxtmp2;
	}

	public void scale() {
		if(cornbl.x()-corntl.x()  != 0 ){
			double angle = Math.toDegrees(Math.atan((cornbl.y()-corntl.y())/(cornbl.x()-corntl.x())));
			double actual = (angle < 0 )?(int) 90+angle:angle-90;
			System.out.println("ROTATION WITH DEGREEEE"+ actual);
			imgxc1 = rotateImage(imgxc1.clone(),actual).clone();
			imgxd1 = cvCreateImage(cvGetSize(imgxc1), IPL_DEPTH_8U, 1);
			cvCvtColor(imgxc1, imgxd1, CV_BGR2GRAY);
		}else{
			logger.log(Level.SEVERE, "Not Scalling done as ");
		}
	}
	public IplImage rotateImage(IplImage my_image,double angle) {
		CvPoint2D32f my_center = new CvPoint2D32f();
		my_center.x(my_image.width()/2);
		my_center.y(my_image.height()/2);
		int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
		CvScalar fillval = cvScalarAll(0);
		CvMat map_matrix = cvCreateMat(2, 3, CV_32FC1);
		cv2DRotationMatrix(my_center, angle, 1, map_matrix);
		cvWarpAffine(my_image, my_image, map_matrix, flags, fillval);
		return my_image;
	}

	public void dpicheck() throws ResolutionNotCorrect {
		int	dpi = (int) ((int) imgx.height()/11.69);
		System.out.println("Resolution is "+dpi);
		if(dpi < 145 || 155 < dpi) throw new ResolutionNotCorrect(dpi);
	}

	public void release() {
		if(imgx != null){
			
			imgx.release();
			imgxc1.release();
			imgxd1.release();
			cvReleaseImage(imgx);
		cvReleaseImage(imgxc1);
		cvReleaseImage(imgxd1);
		}
	}

}
