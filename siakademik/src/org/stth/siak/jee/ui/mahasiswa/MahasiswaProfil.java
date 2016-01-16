package org.stth.siak.jee.ui.mahasiswa;


import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.genericview.MahasiswaProfilView;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class MahasiswaProfil extends Panel implements View{
	TextField txtNama,txtNIDN,txtNIS,txtTanggalLahir,txtEmail,txtAlamat,txtKtp;
	TextField txtJenjangPendidikanTerakhir,txtProdiPendidikanTerakhir,txtInstitusiPendidikanTerakhir;
	private Label titleLabel;
	private CssLayout dashboardPanels;
	private VerticalLayout root;
	private Window notificationsWindow;
	private BeanItem<Mahasiswa> item;

	public MahasiswaProfil() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		Mahasiswa m = (Mahasiswa) VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		item = new BeanItem<Mahasiswa>(m);
		root.addComponent(buildHeader());
		root.addComponent(new MahasiswaProfilView(m));
		//root.addComponent(buildPanel());
		//root.addComponent(buildForm());
	}
	private Component buildForm() {
		VerticalLayout pnl = new VerticalLayout();
		FormLayout fly = new FormLayout();
		FieldGroup binder = new FieldGroup(item);
		fly.addComponent(binder.buildAndBind("Nomor Induk Mahasiswa", "npm"));
		fly.addComponent(binder.buildAndBind("Nama", "nama"));
		fly.addComponent(binder.buildAndBind("Program Studi", "prodi"));
		pnl.addComponent(fly);
		return pnl;
	}
	
	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		titleLabel = new Label("Profil Mahasiswa");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(titleLabel);
		return header;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
