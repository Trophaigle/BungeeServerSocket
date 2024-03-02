package fr.trophaigle.ordermand.servers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesUtils {

	
	public static void copyFolder(File source, File dest){
		try {
			if(source.isDirectory()){
				
				if(!dest.exists()){
					dest.mkdir();
				}
				
				String[] childrens = source.list();
				for(int i = 0; i < childrens.length; i++){
					copyFolder(new File(source, childrens[i]), new File(dest, childrens[i]));
				}
			} else {
				
				InputStream in = new FileInputStream(source);
				OutputStream out = new FileOutputStream(dest);
				
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete a folder
	 * @param source new File(chemin absolu)
	 * @throws Exception
	 */
	public static void deleteFolder(File source) throws Exception{
		File[] files = source.listFiles();
		if(files != null){
			File[] arrayOffile1;
			int j = (arrayOffile1 = files).length;
			for(int i = 0; i < j; i++){
				File f = arrayOffile1[i];
				if(f.isDirectory()){
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		source.delete();
	}
}
