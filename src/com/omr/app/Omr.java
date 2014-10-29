package com.omr.app;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.omr.app.userinterface.MainFrame;

public class Omr {
	public static FileHandler fh;
	private static Logger logger;
	private static OmrController control;
	private static String session;
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = new Date();
		session = dateFormat.format(date);
		if(mk("./debug") && mk("./log") && mk("./"+session) && mk("./"+session+"/success") && mk("./"+session+"/fail") && mk("./"+session+"/code") ){
			setLog();
			logger = Logger.getLogger(Omr.class.getName());
			logger.addHandler(fh);
			//logger.setUseParentHandlers(false);
			logger.setLevel(Level.ALL);
			logger.log(Level.INFO, "Session Started:"+session);
			MainFrame view = new MainFrame(session);
			view.addWindowListener(exitListener);
			control=new OmrController(fh, view,session);
			control.startApp();
		}else{
			System.out.println("Failed to start Application Can not Create Directory");
		}
	}
	public static boolean mk(String name){
		File file = new File(name);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println(name+" Directory is created!");
			} else {
				System.out.println(name+" Failed to create directory!");
				return false;
			}
		}
		return true;
	}
	public static void setLog(){
		try {
			fh = new FileHandler("log/logs.txt",true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
	}
	static WindowAdapter exitListener = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			/*int confirm = JOptionPane.showOptionDialog(null, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (confirm == 0) {
				System.out.println("ASAS");
				//System.exit(0);
			}*/
			fh.close();
			try {
				control.closeFiles();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
			return;
		}
	};

}
