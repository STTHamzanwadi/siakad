package org.pdtyph.jee.ui.opryayasan;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.Prodi;
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

public class CariProdi extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("Daftar Prodi YPH PPD NW PANCOR");
	private Label titleLabel;
	private VerticalLayout dashboardPanels;
	private FieldGroup fg;
	private VerticalLayout root;
	private BeanContainer<Integer, Prodi> beans= new BeanContainer<Integer, Prodi>(Prodi.class);
	private BeanItem<Prodi> bimp;
	private ComboBox instansi = new ComboBox("Instansi");
	private TextField nama=new TextField("Nama Prodi");
	private TextField status=new TextField("Status Akreditasi");

	public CariProdi() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		bimp = new BeanItem<Prodi>(new Prodi());
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
		instansi = new ComboBox("Instansi");
		List<Instansi> i = GenericPersistence.findList(Instansi.class);
		BeanItemContainer<Instansi> instansiContainer = 
				new BeanItemContainer<>(Instansi.class, i);
		instansi.setContainerDataSource(instansiContainer);
		instansi.setItemCaptionPropertyId("nama");
		instansi.setFilteringMode(FilteringMode.CONTAINS);
		instansi.setImmediate(true);
		fg.bind(instansi, "instansi");
		form.addComponent(instansi);
		instansi.setIcon(FontAwesome.UNIVERSITY);
		instansi.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		form.addComponent(fg.buildAndBind("Nama", "namaProdi"));		
		nama.setIcon(FontAwesome.USER);
		nama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);		
		form.addComponent(fg.buildAndBind("Status Akreditasi", "stsAkreditasi"));		
		nama.setIcon(FontAwesome.BUILDING);
		nama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);		
		final Button signin = new Button("Cari",FontAwesome.SEARCH);
		signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();
		
		form.addComponents(instansi,signin);
		
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
		BeanItem<Prodi> bi = (BeanItem<Prodi>) fg.getItemDataSource();
		Prodi m = bi.getBean();
		List<Prodi> lm = PegawaiPersistence.cariProdi(m);
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new Prodi());
		}
		tabel = new Table("Hasil pencarian");
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("instansi", "NAMA LEMBAGA");
		tabel.setColumnHeader("namaProdi", "NAMA PRODI");
		tabel.setColumnHeader("kodeProdi", "KODE");
		tabel.setColumnHeader("tahunBerdiri", "TAHUN BERDIRI");
		tabel.setColumnHeader("stsAkreditasi", "SETATUS AKREDITASI");
		tabel.setColumnHeader("kaprodi", "NAMA KAPRODI");
		tabel.setVisibleColumns("instansi","namaProdi","kodeProdi","tahunBerdiri","stsAkreditasi","kaprodi");
		
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
