package org.pdtyph.jee.ui.admin;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.RiwayatJabatan;
import org.pdtyph.entity.RiwayatPendidikan;
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
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class CariRiwayatJabatan extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("DAFTAR RIWAYAT JABATAN PEGAWAI YPHPPD NW PANCOR");
	private VerticalLayout dashboardPanels;
	private VerticalLayout root;
	private FieldGroup fg;
	private BeanItem<CariRiwayatJabatan> item;
	private BeanContainer<Integer, RiwayatJabatan> beans = new BeanContainer<Integer, RiwayatJabatan>(RiwayatJabatan.class);
	private ComboBox cmbJabatan=new ComboBox("Nama Jabatan");
	private ComboBox cmbnama=new ComboBox("Nama pegawai");
	private BeanItem<RiwayatJabatan> bimp;
	
	public CariRiwayatJabatan() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(buildFilterUI());

	}
	@SuppressWarnings("serial")
	private Component buildFilterUI(){
		fg = new FieldGroup(bimp);
		HorizontalLayout form = new HorizontalLayout();
		form.setSpacing(true);		
		form.addStyleName("fields");
//		form.addComponent(fg.buildAndBind("Nama jabatan", "nmJabatan"));
		
		List<RiwayatJabatan> i = GenericPersistence.findList(RiwayatJabatan.class);
		BeanItemContainer<RiwayatJabatan> instansiContainer = 
				new BeanItemContainer<>(RiwayatJabatan.class, i);
		cmbnama.setContainerDataSource(instansiContainer);
		cmbnama.setItemCaptionPropertyId("nama");
		cmbnama.setFilteringMode(FilteringMode.CONTAINS);
		cmbnama.setImmediate(true);
		fg.bind(cmbnama, "nmPegawai");
		form.addComponent(cmbnama);
		cmbnama.setIcon(FontAwesome.USER);
		cmbnama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);		
		
		cmbJabatan.setContainerDataSource(instansiContainer);
		cmbJabatan.setItemCaptionPropertyId("nama");
		cmbJabatan.setFilteringMode(FilteringMode.CONTAINS);
		cmbJabatan.setImmediate(true);
		fg.bind(cmbJabatan, "nmJabatan");
		form.addComponent(cmbJabatan);
		cmbJabatan.setIcon(FontAwesome.USER);
		cmbJabatan.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		
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

	private Component getTable(){
		
		try {
			fg.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserOprInstansi user = (UserOprInstansi) VaadinSession.getCurrent().getAttribute(
				UserOprInstansi.class);
		@SuppressWarnings("unchecked")
		BeanItem<RiwayatJabatan> bi = (BeanItem<RiwayatJabatan>) fg.getItemDataSource();
		RiwayatJabatan m = bi.getBean();
		m.setNmInstansi(user.getInstansi());
		List<RiwayatJabatan> lm = PegawaiPersistence.cariRiwayatJabatan(m);
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new RiwayatJabatan());
		}
		tabel = new Table("Hasil pencarian");
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("nmPegawai", "NAMA PEGAWAI");
		tabel.setColumnHeader("nmInstansi", "NAMA INSTANSI");
		tabel.setColumnHeader("nmJabatan", "JABATAN");
		tabel.setColumnHeader("mulai", "MULAI");
		tabel.setColumnHeader("selesai", "SELESAI");
		tabel.setColumnHeader("noSK", "NO SK");
		tabel.setColumnHeader("aksi", "AKSI");
		tabel.setVisibleColumns("aksi","nmPegawai","nmInstansi","nmJabatan","mulai","selesai","noSK");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
