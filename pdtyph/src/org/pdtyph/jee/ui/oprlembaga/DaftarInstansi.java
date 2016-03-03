package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.yph.jee.persistence.GenericPersistence;

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
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarInstansi extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("DAFTAR INSTANSI YPHPPD NW PANCOR");
	private VerticalLayout dashboardPanels;
	private Label titleLabel;
	private VerticalLayout root;
	private BeanContainer<Integer, Instansi> beans = new BeanContainer<Integer, Instansi>(Instansi.class);
		
	public DaftarInstansi() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(getTable());

	}
	
	private Component getTable(){
		List<Instansi> lm =GenericPersistence.findList(Instansi.class);
		
		dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new Instansi());
		}
		
		
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("noTelp", "NO TELPON");
		tabel.setColumnHeader("nama", "NAMA LEMBAGA");
		tabel.setColumnHeader("kode", "KODE");
		tabel.setColumnHeader("alamat", "ALAMAT");
		tabel.setColumnHeader("jenisLembaga", "JENIS LEMBAGA");
		tabel.setColumnHeader("email", "EMAIL");
		tabel.setColumnHeader("thnBerdiri", "TAHUN BERDIRI");
		tabel.setColumnHeader("stsAkreditasi", "STATUS AKREDITASI");
		tabel.setColumnHeader("pimpinan", "PIMPINAN");
		tabel.setVisibleColumns("noTelp","nama","kode","alamat","jenisLembaga","email","thnBerdiri","stsAkreditasi","pimpinan");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
