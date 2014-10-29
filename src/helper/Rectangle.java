package helper;

import static org.bytedeco.javacpp.opencv_core.cvGet2D;

import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;

import config.Config;

public class Rectangle extends Config{
	private IplImage in;
	public CvPoint tl,br;
	public Rectangle(IplImage in){
		this.in = in;
	}

	public boolean isBlack(){
		double b=0,w=0;
		for (int y = tl.y(); y < br.y(); y++) {
			for (int x = tl.x(); x < br.x(); x++) {
				if (isblackp(x,y)) b++;
				else w++;
			}
		}
		double per = (b/w)*100 ;
		return (per <= percent)?false:true;
	}
	/***
	 * Setting Edges points
	 * @param p1 top-left corner
	 * @param p2 bottom-right corner
	 */
	public void setCorn(CvPoint p1, CvPoint p2) {
		tl = p1;
		br = p2;
	}
	public void setCorn(int x0,int y0,int x1,int y1){
		//System.out.println("Setting points x0= "+x0+",y0="+y0+",x1= "+x1+",y1="+y1);
		CvPoint p1 = new CvPoint(),
				p2 = new CvPoint();
		p1.x(x0);p1.y(y0);
		p2.x(x1);p2.y(y1);
		setCorn(p1, p2);
	}
	public String displayCorners(){
		return "TopLeft ("+tl.x()+","+tl.y()+") BottomRight ("+br.x()+","+br.y()+")";
	}
	public double getheight(){
		return br.x() - tl.x();
	}
	public double getwidth(){
		return br.y() - tl.y();
	}
	/*
	 * Detecting if pixel is black
	 * @return boolean
	 */
	public boolean isblackp(int x,int y){
		CvScalar s=cvGet2D(in,y,x);
		//System.out.println( "B:"+ s.val(0) + " G:" + s.val(1) + " R:" + s.val(2));//Print values
		return (s.val(2) <= cB && s.val(0) <= cG
				&& s.val(1) <= cR)?true :false;
	}
}