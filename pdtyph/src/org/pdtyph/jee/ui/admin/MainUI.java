package org.pdtyph.jee.ui.admin;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import org.pdtyph.entity.Admin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

@Title("PDT YPH PPD NW PANCOR")
@Theme("tests-valo-flat")
@SuppressWarnings("serial")
public class MainUI extends UI {

	@WebServlet(value = "/admin/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MainUI.class)
	public static class Servlet extends VaadinServlet {
	}
	@Override
	protected void init(VaadinRequest request) {
		//getSession().setConverterFactory(new MyConverterFactory());
		setLocale(Locale.US);


		Responsive.makeResponsive(this);

		updateContent();

		Page.getCurrent().addBrowserWindowResizeListener(
				new BrowserWindowResizeListener() {
					@Override
					public void browserWindowResized(
							BrowserWindowResizeEvent event) {

					}
				});
	}




	public void updateContent() {
		Admin user = (Admin) VaadinSession.getCurrent().getAttribute(
				Admin.class);
		if (user!=null) {
//			if (true) {
			//			 Authenticated user
			setContent(new MainView());
			removeStyleName("loginview");
			getNavigator().navigateTo(getNavigator().getState());
		} else {
			setContent(new LoginView());
			addStyleName("loginview");
		}
	}


}
