package org.pdtyph.jee.ui.opryayasan;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Pegawai;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.persistence.PegawaiPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class CariInstansi extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("Daftar Instansi YPH PPD NW PANCOR");
	private Label titleLabel;
	private VerticalLayout dashboardPanels;
	private FieldGroup fg;
	private VerticalLayout root;
	private BeanContainer<Integer, Instansi> beans= new BeanContainer<Integer, Instansi>(Instansi.class);
	private BeanItem<Instansi> bimp;
	private TextField instansi = new TextField("Instansi");
	private TextField status = new TextField("Status Akreditasi");
	private ComboBox jenisLembaga = new ComboBox("Jenis Instansi");
	private TextField nama=new TextField("Nama Pimpinan");

	public CariInstansi() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		bimp = new BeanItem<Instansi>(new Instansi());
		root = new VerticalLayout();
		//root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		titleLabel = new Label("Kriteria Pencarian");
		titleLabel.addStyleName(ValoTheme.LABEL_H4);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);

		root.addComponent(titleLabel);
		root.addComponent(buildFilterUI());


	}
	@SuppressWarnings("serial")
	private Component buildFilterUI(){
		fg = new FieldGroup(bimp);
		HorizontalLayout form = new HorizontalLayout();
		form.setSpacing(true);		
		form.addStyleName("fields");
		form.addComponent(fg.buildAndBind("Nama Instansi", "nama"));
		instansi.setIcon(FontAwesome.UNIVERSITY);
		instansi.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		form.addComponent(fg.buildAndBind("Status Akreditasi", "stsAkreditasi"));
		status.setIcon(FontAwesome.TREE);
		status.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		form.addComponent(fg.buildAndBind("Nama Pimpinan", "pimpinan"));
		nama.setIcon(FontAwesome.USER);
		nama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		jenisLembaga.addItem("SD/MI");
		jenisLembaga.addItem("SMP/MTs");
		jenisLembaga.addItem("SMA/MA/SMK");
		jenisLembaga.addItem("Perguruan Tinggi");
		jenisLembaga.addItem("Non Pendidikan");
		fg.bind(jenisLembaga, "jenisLembaga");
		form.addComponent(jenisLembaga);

		final Button signin = new Button("Cari",FontAwesome.SEARCH);
		signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		form.addComponent(signin);

		form.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

		signin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (dashboardPanels != null){
					root.removeComponent(dashboardPanels);
					dashboardPanels = null;
				}
				root.addComponent(getTable());
			}
		});
		return form;
	}
	
	@SuppressWarnings({"unchecked" })
	private Component getTable(){
		try {
			fg.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BeanItem<Instansi> bi = (BeanItem<Instansi>) fg.getItemDataSource();
		Instansi m = bi.getBean();
		List<Instansi> lm = PegawaiPersistence.cariInstansi(m);
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
		tabel = new Table("Hasil pencarian");
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
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
