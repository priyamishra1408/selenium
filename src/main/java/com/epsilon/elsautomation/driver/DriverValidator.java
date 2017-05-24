package com.epsilon.elsautomation.driver;

import java.io.File;

public class DriverValidator {

	private String testLocation;
	
	DriverValidator() {
		
	}

	public boolean validateFolderExist(String testLocation)

	{
		String[] filenames = { "DataFile", "Log", "Mapping", "Script", "TestFile" };

		for (int i = 0; i < filenames.length; i++)

		{

			File dir1 = new File(testLocation + filenames[i]);

			if (!dir1.exists()) {
				return false;
			}

		}

		return true;

	}

	public boolean startFileValidator(String testLocation, String startFileName) {
		
		File startdir = new File(testLocation + startFileName);

		if (startdir.exists()) {
			return true;
		} else {
			return false;

		}
	}
}
