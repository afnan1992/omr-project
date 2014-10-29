package helper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public 	class Filter implements FileFilter {
	public boolean accept(File file){
		List<String> suffixes = new ArrayList<String>();
		suffixes.add("jpg");
		suffixes.add("png");
		for(String s: suffixes){
				//System.out.println("Checking For suffix"+ file.getName()+"Against "+s);
				if(file.getName().endsWith(s)) return file.getName().endsWith(s);
		}
		//System.out.println("Nothing Foun For suffix"+ file.getName());
		return false;
		
	}
}