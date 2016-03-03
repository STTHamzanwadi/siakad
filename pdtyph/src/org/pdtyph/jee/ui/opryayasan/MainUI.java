package org.pdtyph.jee.ui.opryayasan;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.UserOprYayasan;

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

	@WebServlet(value = "/opryayasan/*", asyncSupported = true)
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
		UserOprYayasan user = (UserOprYayasan) VaadinSession.getCurrent().getAttribute(
				UserOprYayasan.class);
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
