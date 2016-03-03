package org.pdtyph.jee.ui.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class CetakData extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Label titleLabel;
	private VerticalLayout root;
	@SuppressWarnings("serial")
	public CetakData() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(buildHeader());
		root.addComponent(buildCari());
	}
	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		titleLabel = new Label("Rekap Data");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H4);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(titleLabel);
		return header;
	}
	
	private HorizontalLayout buildCari(){
		HorizontalLayout lo=new HorizontalLayout();
		setSizeFull();
		
		Label nik=new Label("Instansi");
		ComboBox comboBox=new ComboBox();
		comboBox.addStyleName(ValoTheme.COMBOBOX_SMALL);
		comboBox.addItem("STTH");
		comboBox.addItem("MDQH");
		comboBox.addItem("STKIP");
		comboBox.addItem("MA. Mu'allimin");
		Label instansi=new Label(" Jenis Pekerjaan ");
		ComboBox combo=new ComboBox();
		combo.addStyleName(ValoTheme.COMBOBOX_SMALL);
		Button cari=new Button("Cari");
		cari.setIcon(FontAwesome.HISTORY);
		cari.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		cari.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		cari.addStyleName(ValoTheme.BUTTON_SMALL);
		combo.addItem("Dosen");
		combo.addItem("Guru");
		combo.addItem("Karyawan");
		lo.addComponent(nik);		
		lo.addComponent(comboBox);
		lo.addComponent(instansi);
		lo.addComponent(combo);
		lo.addComponent(cari);
		return lo;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
