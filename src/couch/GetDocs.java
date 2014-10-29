package couch;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.omr.exceptions.AssessmentNotExist;
import com.omr.exceptions.MappingNotCorrect;

import config.Config;

public class GetDocs extends Config{
	private String serial,aid,dkey,student,qrstr;
	private Seq seqdoc;
	private CouchDbClient dbtang;
	private JsonObject 	rslt;
	private Logger logger;
	public GetDocs(){
		logger = Logger.getLogger(GetDocs.class.getName());
	}
	public void setKey(String aid){
		this.aid = aid;
		this.dkey = this.aid.substring(this.aid.length() - 5);
	}
	
	public String getqrstr(){
		return qrstr;
	}
	public boolean subtst(String enu,String name, String[] results,int Questionscount) throws AssessmentNotExist{
		dbtang = new CouchDbClient(ASSDb,false,HEADER,HOST,PORT,USR,PWD);
		List<JsonObject> json = dbtang.view("tangerine/byDKey")
										.includeDocs(true)
										.key(dkey)
										.query(JsonObject.class);
		List<JsonObject> json2 = new ArrayList<JsonObject>();
		rslt = new JsonObject();
		String col ="";
		JsonArray colls = new JsonArray();
		for (int i = 0; i < json.size(); i++) {
			JsonObject el = json.get(i);
			SubtestDoc subDoc = new SubtestDoc();
			if(el.get("collection") != null){
				col = el.get("collection").getAsString();
				switch (col){
					case "assessment":
						rslt.add("assessmentId", el.get("assessmentId"));
						rslt.add("assessmentName", el.get("name"));
						rslt.addProperty("start_time", System.currentTimeMillis());
						rslt.addProperty("enumerator", enu);
						rslt.addProperty("collection", "result");
						break;
					case "subtest":
						//System.out.println(el.get("name").getAsString());
						subDoc.setname(el.get("name").getAsString());
						subDoc.setprototype(el.get("prototype").getAsString());
						subDoc.setsubtestId(el.get("_id").getAsString());
						if (el.get("prototype").getAsString().equals("location") || el.get("prototype").getAsString().equals("datetime")){
							subDoc.setsum(1, 0, 0, 1);
						}else{
							subDoc.setsum(0, 0, 0, 0);
						}
						if(el.get("prototype").getAsString().equals("location")){
							subDoc.setloc(name);
						}else{
							subDoc.setdata(json,el.get("prototype").getAsString(),seqdoc.getqSeq(),results,seqdoc.getoptSeq(),Questionscount);
						}
						
						colls.add(subDoc.toJson());
						break;
					default:
						break;
				}
			}
		}
		System.out.println(colls.size());
		if(colls.size() < 3) throw new AssessmentNotExist(""+dkey);
		JsonObject 	tmptime = colls.get(colls.size()-1).getAsJsonObject(),
					tmpname = colls.get(colls.size()-2).getAsJsonObject();
		System.out.println(tmpname.toString());
		System.out.println(tmptime.toString());
		JsonArray ordcolls = new JsonArray();
		ordcolls.add(tmpname);
		ordcolls.add(tmptime);
		
		for (int i = 0; i < colls.size()-2; i++) {
			ordcolls.add(colls.get(i));
		}
		SubtestDoc comp = new SubtestDoc();
		comp.setname("Assessment complete");
		comp.setprototype("complete");
		comp.setsubtestId("result");
		comp.setsum(1, 0, 0, 1);
		comp.setdata("complete");
		ordcolls.add(comp.toJson());
		json2.add(comp.toJson());
		rslt.add("subtestData", ordcolls);
		System.out.println(rslt);
		dbtang.shutdown();
		return true;
	}
	public boolean readQRCode(String imagePath){
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		Map<DecodeHintType,Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.of(BarcodeFormat.QR_CODE));
		
        BufferedImage tmpBfrImage;
        try 
        {
            tmpBfrImage = ImageIO.read(new File(imagePath));
            LuminanceSource tmpSource = new BufferedImageLuminanceSource(tmpBfrImage);
            BinaryBitmap tmpBitmap = new BinaryBitmap(new HybridBinarizer(tmpSource));
            MultiFormatReader tmpBarcodeReader = new MultiFormatReader();
            Result tmpResult;
            String tmpFinalResult;
            tmpResult = tmpBarcodeReader.decode(tmpBitmap,  tmpHintsMap);
            //if(tmpResult == null) return false;
            tmpFinalResult = String.valueOf(tmpResult.getText());
            qrstr = tmpFinalResult;
        } 
        catch (IOException | NotFoundException tmpIoe) 
        {
        	return false;
        }
		return true;
	}
	public Seq getMap() throws MappingNotCorrect,CouchDbException{
			logger.log(Level.WARNING, "Getting Sheet Sequences DB:"+MAPDb+" HEADER: "+HEADER+" HOST: "+HOST+" PORT: "+PORT+" USR:"+USR+" PWD:"+PWD);
			CouchDbClient dbClient = new CouchDbClient(MAPDb,false,HEADER,HOST,PORT,USR,PWD);
			seqdoc = dbClient.find(Seq.class, serial);
			seqdoc.validate();
			setKey(seqdoc.getqid());
			student = seqdoc.getStudentName();
			logger.log(Level.INFO, "Student Name "+student);
		return seqdoc;
	}
	public void push(){
		dbtang = new CouchDbClient(ASSDb,false,HEADER,HOST,PORT,USR,PWD);
		dbtang.save(rslt);
		dbtang.shutdown();
	}
	/***
	 * Overloaded Show Image
	 * @param image
	 * @param caption
	 * @param size
	 */
	public static void ShowImage(IplImage image, String caption, int size){
		if(size < 128) size = 128;
		CvMat mat = image.asCvMat();
		int width = mat.cols(); if(width < 1) width = 1;
		int height = mat.rows(); if(height < 1) height = 1;
		double aspect = 1.0 * width / height;
		if(height != size) { height = size; width = (int) ( height * aspect ); }
		if(width != size) width = size;
		height = (int) ( width / aspect );
		//ShowImage(image, caption, width, height);
	}
	/***
	 * Show Image
	 * @param image
	 * @param caption
	 * @param width
	 * @param height
	 */
	public static void ShowImage(IplImage image, String caption, int width, int height)
	{
		CanvasFrame canvas = new CanvasFrame(caption, 1);   // gamma=1
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvas.setCanvasSize(width, height);
		canvas.showImage(image);
	}

	public void Qrdone() {
		if( qrstr != null){
			serial = qrstr;
		}else{
			logger.log(Level.SEVERE, "No Information in QrRCode");
		}
	}

	public void Qrfailed(String str) {
		qrstr = str;
		Qrdone();
	}

}
