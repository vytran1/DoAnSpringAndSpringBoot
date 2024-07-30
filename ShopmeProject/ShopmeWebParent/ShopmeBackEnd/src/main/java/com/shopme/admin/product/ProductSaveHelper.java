package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUltil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	  static void deletExtraImageWasRemovedFromForm(Product product) {
			// TODO Auto-generated method stub
			String extraImageDir = "../products-images/" + product.getId() + "/extras";
			Path dirPath = Paths.get(extraImageDir);
			try {
				Files.list(dirPath).forEach(file -> {
					String fileName = file.toFile().getName();
					if(!product.containsImageName(fileName)) {
						try {
							Files.delete(file);
							LOGGER.info("Deleted extra image: " + fileName);
						}catch(IOException ex) {
							LOGGER.error("Could not delete extra image " + fileName);
						}
					}
				});
			}catch(IOException ex) {
				LOGGER.error("Could not list directory " + dirPath);
			}
		}

		static void setExistingExtraImageName(String[] imageIDs, String[] imageNames, Product product) {
			// TODO Auto-generated method stub
			if(imageIDs == null || imageIDs.length == 0) return;
			Set<ProductImage> images = new HashSet<>(); 
			for(int count =0; count < imageIDs.length;count ++) {
				Integer id = Integer.parseInt(imageIDs[count]);
				String name = imageNames[count];
				images.add(new ProductImage(id,name,product));
			}
			product.setImages(images);
		}

		static void setProductDetail(String[] detailIDs,String[] detailNames, String[] detailValues, Product product) {
			// TODO Auto-generated method stub
			if(detailNames == null || detailNames.length == 0) {
				return;
			}else {
				for(int count =0; count < detailNames.length; count ++) {
					String name = detailNames[count];
					String value = detailValues[count];
					Integer id = Integer.parseInt(detailIDs[count]);
					if(id != 0) {
						product.addDetail(id,name,value);
					}else if(!name.isEmpty() && !value.isEmpty()) {
						product.addDetail(name, value);
					}
				}
			}
		}

		static void savedUploadImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
				Product saveProduct) throws IOException {
			// TODO Auto-generated method stub
	    	   if(!mainImageMultipart.isEmpty()) {
	 		     String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
	 		     String uploadDir = "../products-images/" + saveProduct.getId();
	 		     FileUploadUltil.cleanDir(uploadDir);
			     FileUploadUltil.saveFile(uploadDir, fileName, mainImageMultipart);
	    	   }
	    	   if(extraImageMultiparts.length > 0) {
	    		String uploadDir = "../products-images/" + saveProduct.getId() +"/extras";   
	   			for(MultipartFile multipart : extraImageMultiparts) {
	   				if(multipart.isEmpty()) continue; 
	   			    String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
	   			    FileUploadUltil.saveFile(uploadDir, fileName, multipart);
	   			}
	    	 }
		}

		static void setNewExtraImageName(MultipartFile[] extraImageMultiparts, Product product) {
			// TODO Auto-generated method stub
			if(extraImageMultiparts.length > 0) {
				for(MultipartFile multipart : extraImageMultiparts) {
					if(!multipart.isEmpty()) {
						 String fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
						 if(!product.containsImageName(fileName)) {
							 product.addExtraImage(fileName);
						 }
					}
				}
			}
		}

		static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
			// TODO Auto-generated method stub
	    	 if(!mainImageMultipart.isEmpty()) {
	    		   String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
	    		   product.setMainImage(fileName);
	    	 }
		}
}
