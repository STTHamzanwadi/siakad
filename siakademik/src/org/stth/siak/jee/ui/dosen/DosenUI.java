package org.stth.siak.jee.ui.dosen;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import org.stth.siak.entity.DosenKaryawan;

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

@Title("Dosen Dashboard")
@Theme("siakademik")
@SuppressWarnings("serial")
public class DosenUI extends UI {

	@WebServlet(value = "/dosen/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DosenUI.class)
	public static class Servlet extends VaadinServlet {
	}
    @Override
    protected void init(VaadinRequest request) {
        //getSession().setConverterFactory(new MyConverterFactory());
        setLocale(Locale.US);
        setTheme("tests-valo-reindeer");
    
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
        DosenKaryawan user = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(
        		DosenKaryawan.class);
        if (user != null) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    
}
