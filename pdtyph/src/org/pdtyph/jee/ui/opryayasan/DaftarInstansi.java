package org.pdtyph.jee.ui.opryayasan;

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
		
	@SuppressWarnings("serial")
	public DaftarInstansi() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setSpacing(true);
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(getTable());
		Button tambah=new Button("Tambah",FontAwesome.PLUS_SQUARE_O);
		root.addComponent(tambah);
		tambah.addClickListener(new ClickListener() {
			@Override public void buttonClick(ClickEvent event) {
				tambahInstansibaru(new Instansi());
			}

		});
		tambah.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		

	}
	
	
	@SuppressWarnings("serial")
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
		
		tabel.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button edit = new Button(FontAwesome.EDIT);
				Button hapus = new Button(FontAwesome.TRASH_O);
				edit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				edit.addStyleName(ValoTheme.BUTTON_SMALL);
				hapus.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				hapus.addStyleName(ValoTheme.BUTTON_SMALL);
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Instansi o = (Instansi) i.getBean();
				edit.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						tambahInstansibaru(o);
					}

				});
				hl.addComponent(edit);
				hl.addComponent(hapus);
				return hl;
			}
		});
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
		tabel.setColumnHeader("aksi", "AKSI");
		tabel.setVisibleColumns("aksi","noTelp","nama","kode","alamat","jenisLembaga","email","thnBerdiri","stsAkreditasi","pimpinan");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	private void tambahInstansibaru(Instansi i) {
		final Window win = new Window("Tambah Instansi Baru");
		Component c = new ProfilInstansi(i,win);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
