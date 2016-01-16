package org.stth.siak.jee.genericview;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewFactory {
	public static Component footer(){
		VerticalLayout footer = new VerticalLayout();
		//footer.addStyleName("viewheader");
		Label titleLabel2;
		footer.setSpacing(true);
		titleLabel2 = new Label("INTEGRATED ONLINE SYSTEM : SISTEM INFORMASI AKADEMIK");
		titleLabel2.setSizeUndefined();
		titleLabel2.addStyleName(ValoTheme.LABEL_H3);
		titleLabel2.addStyleName(ValoTheme.LABEL_COLORED);
		footer.addComponent(titleLabel2);
		
		String s = "http://iosys.stthamzanwadi.ac.id/siakademik <br> "
				+ "<b>powered by : Ubuntu, JavaEE, JBossAS (Wildfly), Vaadin, Hibernate, MySQL, JasperReport</b>";
		Label lblCredit = new Label(s, ContentMode.HTML);
		footer.addComponent(lblCredit);
		return footer;
	}
	public static Component header(String label){
		HorizontalLayout header = new HorizontalLayout();
		//ooter.addStyleName("viewheader");
		Label titleLabel2;
		header.setSpacing(true);
		titleLabel2 = new Label(label);
		titleLabel2.setSizeUndefined();
		titleLabel2.addStyleName(ValoTheme.LABEL_H3);
		titleLabel2.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(titleLabel2);
		return header;
	}

}
