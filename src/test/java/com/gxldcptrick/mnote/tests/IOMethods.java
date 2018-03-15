package com.gxldcptrick.mnote.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class IOMethods {

	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T loadFile(File saveFile) {

		T data = null;

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile));) {

			data = (T) in.readObject();

		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();

		}

		return data;

	}

	public static <T extends Serializable> File  saveFile(String path, T data) {

		File save = new File(path);

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(save));) {

			out.writeObject(data);

		} catch (IOException e) {
			
			e.printStackTrace();
		
		}

		return save;
	}
}
