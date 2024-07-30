package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shopme.admin.paging.PagingAndSortingArgumentResolver;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    //Show Image 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		String dirName = "user-photos";
//		Path userPhotoDir = Paths.get(dirName);
//		String userPhotoPath = userPhotoDir.toFile().getAbsolutePath();
//		registry.addResourceHandler("/"+dirName+"/**")
//		        .addResourceLocations("file:/" + userPhotoPath + "/");
		
		exposeDirectory("user-photos", registry);
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


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		// TODO Auto-generated method stub
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	
}
