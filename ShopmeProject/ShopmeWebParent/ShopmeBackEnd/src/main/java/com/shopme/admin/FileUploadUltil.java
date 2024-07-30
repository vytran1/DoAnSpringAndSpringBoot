package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;

public class FileUploadUltil {
   public static void saveFile(String uploadDir,String fileName,MultipartFile multipartFile) throws IOException {
	   Path uploadPath = Paths.get(uploadDir);
	   if(!Files.exists(uploadPath)) {
		   Files.createDirectories(uploadPath);
	   }
	   try(InputStream inputStream = multipartFile.getInputStream()) {
		   Path filePath = uploadPath.resolve(fileName);
		   Files.copy(inputStream,filePath,StandardCopyOption.REPLACE_EXISTING);
	   }catch(IOException ex) {
		   throw new IOException("Could not save file: " + fileName, ex);
	   }
   }
   
   public static void cleanDir(String dir) {
	   Path dirPath = Paths.get(dir);
	   try {
		   Files.list(dirPath).forEach(file -> {
			   if(!Files.isDirectory(file)) {
				   try {
					   
					   Files.delete(file);
				   }catch(IOException e) {
					   System.out.println("Could not delete file " + file);
				   }
			   }
		   });
	   }catch(IOException e) {
		   System.out.println("Could not clean directory " + dirPath);
	   }
   }
   
   public static void removeDir(String dir) {
	   cleanDir(dir);
	   try {
		   Files.delete(Paths.get(dir));
	   }catch(IOException e) {
		   System.out.println("Could not remove directory "+ dir);
	   }
   }
}
