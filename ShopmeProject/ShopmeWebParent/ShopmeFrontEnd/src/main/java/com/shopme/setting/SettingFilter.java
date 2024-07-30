package com.shopme.setting;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.Menu;
import com.shopme.common.entity.setting.Setting;
import com.shopme.menu.MenuService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
@Component
public class SettingFilter implements Filter {
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private MenuService menuService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		// TODO Auto-generated method stub
		
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		String url = servletRequest.getRequestURI().toString();
	    if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg")) {
	    	chain.doFilter(request, response);
	    	return;
	    }	
	    List<Setting> generalSettings = settingService.getGeneralSetting();
		generalSettings.forEach((x)->{
//			System.out.println(x);
			request.setAttribute(x.getKey(), x.getValue());
//			System.out.println(x.getKey() + " " + x.getValue());
		});
		loadMenuSetting(request);
        chain.doFilter(request, response);
	}
    
	private void loadMenuSetting(ServletRequest request) {
		List<Menu> headerMenuItems = menuService.getHeaderMenuItems();
		request.setAttribute("headerMenuItems", headerMenuItems);
		
		List<Menu> footerMenuItems = menuService.getFooterMenuItems();
		request.setAttribute("footerMenuItems", footerMenuItems);
		
		
	}
}
