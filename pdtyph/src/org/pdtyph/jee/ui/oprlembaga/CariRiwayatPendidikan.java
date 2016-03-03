package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

public class CariRiwayatPendidikan extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("DAFTAR RIWAYAT PENDIDIKAN PEGAWAI YPHPPD NW PANCOR");
	private VerticalLayout dashboardPanels;
	private VerticalLayout root;
	private BeanItem<CariRiwayatPendidikan> item;
	private FieldGroup fg;
	private BeanContainer<Integer, RiwayatPendidikan> beans = new BeanContainer<Integer, RiwayatPendidikan>(RiwayatPendidikan.class);
	private ComboBox  pendidikan = new ComboBox ("Pendidikan");
	private TextField nama=new TextField("Nama");
	private BeanItem<RiwayatPendidikan> bimp;

	public CariRiwayatPendidikan() {
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
//		form.addComponent(fg.buildAndBind("Nama", "nmaPegawai"));
		
		pendidikan.addItem("SD");
		pendidikan.addItem("SMP");
		pendidikan.addItem("SMA");
		pendidikan.addItem("Profesi");
		pendidikan.addItem("D1");
		pendidikan.addItem("D2");
		pendidikan.addItem("D3");
		pendidikan.addItem("MDQH");
		pendidikan.addItem("S1");
		pendidikan.addItem("S2");
		pendidikan.addItem("S3");
		nama.setIcon(FontAwesome.USER);
		nama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		pendidikan.setIcon(FontAwesome.GRADUATION_CAP);
		pendidikan.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		pendidikan.setFilteringMode(FilteringMode.CONTAINS);
		fg.bind(pendidikan, "jenjangPendTerakhir");		

		final Button signin = new Button("Cari",FontAwesome.SEARCH);
		signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		form.addComponents(pendidikan,signin);
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
		BeanItem<RiwayatPendidikan> bi = (BeanItem<RiwayatPendidikan>) fg.getItemDataSource();
		RiwayatPendidikan m = bi.getBean();
		List<RiwayatPendidikan> lm = PegawaiPersistence.cariRiwayatPendidikan(m);
//		List<RiwayatPendidikan>lm =PegawaiPersistence.cariRiwayatPrndidikan(m);
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new RiwayatPendidikan());
		}
		tabel = new Table("Hasil pencarian");
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("nmaPegawai", "NAMA PEGAWAI");
		tabel.setColumnHeader("nmInstansi", "NAMA INSTANSI");
		tabel.setColumnHeader("jenjangPendidikan", "JENJANG PENDIDIKAN");
		tabel.setColumnHeader("jurusan", "PRODI");
		tabel.setColumnHeader("tahunLulus", "TAHUN LULUS");
		tabel.setColumnHeader("namaInstansi", "NAMA INSTITUSI");		
		tabel.setColumnHeader("gelarAkademis", "GELAR AKADEMIS");
		tabel.setColumnHeader("aksi", "AKSI");
		tabel.setVisibleColumns("aksi","nmaPegawai","nmInstansi","jenjangPendidikan","jurusan","tahunLulus","namaInstansi","gelarAkademis");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	private void tambahRiwayatPendidikanbaru(RiwayatPendidikan rp) {
		final Window win = new Window("Riwayat Pendidikan");
		Component c = new TambahRiwayatPendidikan(rp,win);
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
