package com.epsilon.elsautomation.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotepadReader {

	public List<String> readNotePad(String startFilePath) {

		FileInputStream fis = null;
		BufferedReader reader = null;
		List<String> list = new ArrayList<String>();
		

		try {

			fis = new FileInputStream(startFilePath);

			reader = new BufferedReader(new InputStreamReader(fis));

//			String line = reader.readLine();
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
                if(!line.isEmpty()){
                	list.add(line);
                }
				//TODO check if line is not empty
			
			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(NotepadReader.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(NotepadReader.class.getName()).log(Level.SEVERE, null, ex);

		} finally {
			try {
				reader.close();
				fis.close();
			} catch (IOException ex) {
				Logger.getLogger(NotepadReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return list;
		

	}
	
}
