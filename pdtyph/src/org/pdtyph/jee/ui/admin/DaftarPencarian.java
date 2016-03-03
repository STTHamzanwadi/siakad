package org.pdtyph.jee.ui.admin;

import java.awt.Font;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.UserOprYayasan;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.util.PasswordEncryptionService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarPencarian extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Label titleLabel;
	private VerticalLayout root;

	public DaftarPencarian() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(buildHeader());


	}
	private Component buildHeader() {
		VerticalLayout header = new VerticalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		titleLabel = new Label("Daftar Pencarian");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H3);

		TabSheet tab=new TabSheet();
		VerticalLayout inlLayout=new VerticalLayout();
		inlLayout.addComponent(new CariInstansi());
		tab.addTab(inlLayout,"Instansi",FontAwesome.BUILDING_O);
		VerticalLayout pegawaiLayout=new VerticalLayout();
		pegawaiLayout.addComponent(new CariPegawai());
		tab.addTab(pegawaiLayout,"Pegawai",FontAwesome.USER);
		VerticalLayout jabatanLayout=new VerticalLayout();
		jabatanLayout.addComponent(new CariJabatan());
		tab.addTab(jabatanLayout, "Jabatan",FontAwesome.TREE);
		VerticalLayout prodiLayout=new VerticalLayout();
		prodiLayout.addComponent(new CariProdi());
		tab.addTab(prodiLayout, "Prodi",FontAwesome.BUILDING_O);
		VerticalLayout rpLayout=new VerticalLayout();
		tab.addTab(rpLayout, "Riwayat Pendidikan",FontAwesome.GRADUATION_CAP);
		VerticalLayout rjLayout=new VerticalLayout();
		tab.addTab(rjLayout, "Riwayat Jabatan",FontAwesome.ADJUST);
		header.addComponents(titleLabel, tab);
		return header;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
