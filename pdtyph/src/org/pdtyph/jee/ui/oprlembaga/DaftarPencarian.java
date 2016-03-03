package org.pdtyph.jee.ui.oprlembaga;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
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
		rpLayout.addComponent(new CariRiwayatPendidikan());
		tab.addTab(rpLayout, "Riwayat Pendidikan",FontAwesome.GRADUATION_CAP);
		VerticalLayout rjLayout=new VerticalLayout();
		rjLayout.addComponent(new CariRiwayatJabatan());
		tab.addTab(rjLayout, "Riwayat Jabatan",FontAwesome.ADJUST);
		header.addComponents(titleLabel, tab);
		return header;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
