package com.shopme;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    //Show Image 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		String dirName = "user-photos";
//		Path userPhotoDir = Paths.get(dirName);
//		String userPhotoPath = userPhotoDir.toFile().getAbsolutePath();
//		registry.addResourceHandler("/"+dirName+"/**")
//		        .addResourceLocations("file:/" + userPhotoPath + "/");
		
//		exposeDirectory("user-photos", registry);
//		
//		String categoryImagesDirName = "../category-image";
//		Path categoryImagesDir = Paths.get(categoryImagesDirName);
//		//absolute 
//		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
//		registry.addResourceHandler("/category-image/**")
//		        .addResourceLocations("file:/" + categoryImagesPath + "/");
//		
		exposeDirectory("../category-image", registry);
		
		
//		String brandLogosDirName = "../brand-logos";
//		Path brandLogosDir = Paths.get(brandLogosDirName);
//		String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
//		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandLogosPath + "/");
		
		exposeDirectory("../brand-logos", registry);
		exposeDirectory("../products-images", registry);
		exposeDirectory("../site-logo", registry);
	}
   
	
	private void exposeDirectory(String pathPattern,ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern.replace("..", "")+"/**";
		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" +absolutePath + "/");
	}
}
