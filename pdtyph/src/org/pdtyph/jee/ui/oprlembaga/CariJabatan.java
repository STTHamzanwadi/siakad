package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.UserOprInstansi;
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
import com.vaadin.server.VaadinSession;
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

public class CariJabatan extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("Daftar Jabatan YPH PPD NW PANCOR");
	private Label titleLabel;
	private VerticalLayout dashboardPanels;
	private FieldGroup fg;
	private VerticalLayout root;
	private BeanContainer<Integer, Jabatan> beans= new BeanContainer<Integer, Jabatan>(Jabatan.class);
	private BeanItem<Jabatan> bimp;
	private ComboBox jnsJabatan = new ComboBox("Jenis Jabatan");
	private TextField nama=new TextField("Nama Jabatan");
	public CariJabatan() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		bimp = new BeanItem<Jabatan>(new Jabatan());
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
		form.addComponent(fg.buildAndBind("Nama", "namaJabatan"));
		
		nama.setIcon(FontAwesome.USER);
		nama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
		jnsJabatan.addItem("Struktural");
		jnsJabatan.addItem("Fungsional");
		jnsJabatan.setFilteringMode(FilteringMode.CONTAINS);
		jnsJabatan.setImmediate(true);
		fg.bind(jnsJabatan, "jnsJabatan");
		
		final Button signin = new Button("Cari",FontAwesome.SEARCH);
		signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();
		
		form.addComponents(jnsJabatan,signin);
		
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
	@SuppressWarnings({ "unchecked" })
	private Component getTable(){
		try {
			fg.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserOprInstansi user=(UserOprInstansi) VaadinSession.getCurrent().getAttribute(UserOprInstansi.class);
		BeanItem<Jabatan> bi = (BeanItem<Jabatan>) fg.getItemDataSource();
		Jabatan m = bi.getBean();
		m.setInstansi(user.getInstansi());
		List<Jabatan> lm = PegawaiPersistence.cariJabatan(m);
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new Jabatan());
		}
		tabel = new Table("Hasil pencarian");
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("instansi", "NAMA LEMBAGA");
		tabel.setColumnHeader("namaJabatan", "NAMA JABATAN");
		
		tabel.setColumnHeader("jnsJabatan", "JENIS JABATAN");
		tabel.setVisibleColumns("instansi","namaJabatan","jnsJabatan");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
