package org.pdtyph.jee.ui.opryayasan;

import java.io.File;
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
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
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

public class CariPegawai extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("Daftar Pegawai YPH PPD NW PANCOR");
	private Label titleLabel;
	private VerticalLayout dashboardPanels;
	private FieldGroup fg;
	private VerticalLayout root;
	private BeanContainer<Integer, Pegawai> beans= new BeanContainer<Integer, Pegawai>(Pegawai.class);
	private BeanItem<Pegawai> bimp;
	private ComboBox instansi = new ComboBox("Instansi");
	private ComboBox  pendidikan = new ComboBox ("Pendidikan");
	private TextField nama=new TextField("Nama");
	private ComboBox stsKepegawaian = new ComboBox("Jenis Jabatan");
	private ComboBox jnsKepegawaian = new ComboBox("Jenis Profesi");
	private ComboBox jnsKelamin = new ComboBox("Jenis Kelamin");

	public CariPegawai() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		bimp = new BeanItem<Pegawai>(new Pegawai());
		root = new VerticalLayout();
		root.setSizeFull();
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
		form.addComponent(fg.buildAndBind("Nama", "namaPegawai"));

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
		nama.setIcon(FontAwesome.USER);
		nama.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		pendidikan.setIcon(FontAwesome.GRADUATION_CAP);
		pendidikan.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		pendidikan.setFilteringMode(FilteringMode.CONTAINS);
		fg.bind(pendidikan, "jenjangPendTerakhir");		


		stsKepegawaian.addItem("PTY");
		stsKepegawaian.addItem("PTT");
		stsKepegawaian.addItem("PNS");
		stsKepegawaian.setFilteringMode(FilteringMode.CONTAINS);
		stsKepegawaian.setImmediate(true);
		fg.bind(stsKepegawaian, "statusKepegawaian");


		jnsKepegawaian.addItem("Dosen");
		jnsKepegawaian.addItem("Guru");
		jnsKepegawaian.addItem("Karyawan");
		jnsKepegawaian.setFilteringMode(FilteringMode.CONTAINS);
		jnsKepegawaian.setImmediate(true);
		fg.bind(jnsKepegawaian, "namaKepegawaian");

		jnsKelamin.addItem("Pria");
		jnsKelamin.addItem("Wanita");
		jnsKelamin.setFilteringMode(FilteringMode.CONTAINS);
		jnsKelamin.setImmediate(true);
		fg.bind(jnsKelamin, "kelamin");
		final Button signin = new Button("Cari",FontAwesome.SEARCH);
		signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signin.setClickShortcut(KeyCode.ENTER);
		signin.focus();

		form.addComponents(instansi,jnsKelamin, pendidikan,stsKepegawaian, jnsKepegawaian, signin);
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

	@SuppressWarnings({ "serial", "unchecked" })
	private Component getTable(){
		try {
			fg.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BeanItem<Pegawai> bi = (BeanItem<Pegawai>) fg.getItemDataSource();
		Pegawai m = bi.getBean();
		List<Pegawai> lm = PegawaiPersistence.cariPegawai(m);
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new Pegawai());
		}
		tabel = new Table("Hasil pencarian");
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		
		tabel.addGeneratedColumn("chek", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.addComponent(new CheckBox());
				return hl;
			}
		});
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("instansi", "NAMA LEMBAGA");
		tabel.setColumnHeader("namaPegawai","NAMA PEGAWAI");
		tabel.setColumnHeader("jbtnStruktural","JABATAN STRUKTURAL");
		tabel.setColumnHeader("jbtnFungsional","JABATAN FUNGSIONAL");
		tabel.setColumnHeader( "alias","NAMA ALIAS");
		tabel.setColumnHeader("niy","NIY");
		tabel.setColumnHeader("nidn","NIDN/NUPN");
		tabel.setColumnHeader("nis","NIP/NUPTK");
		tabel.setColumnHeader("tempatLahir","TEMPAT LAHIR");
		tabel.setColumnHeader("tanggalLahir","TANGGAL LAHIR");
		tabel.setColumnHeader( "kelamin","JENIS KELAMIN");
		tabel.setColumnHeader("agama","AGAMA");
		tabel.setColumnHeader("email","E-MAIL");
		tabel.setColumnHeader("alamatRumah","ALAMAT");
		tabel.setColumnHeader("nomorTelepon","TELPON");
		tabel.setColumnHeader("nomorKtp","NIK");
		tabel.setColumnHeader("namaKepegawaian","JENIS KEPEGAWAIAN");
		tabel.setColumnHeader("statusKepegawaian","SETATUS KEPEGAWAIAN");
		tabel.setColumnHeader("golongan","GOLONGAN");
		tabel.setColumnHeader("thnMasuk","TAHUN MASUK");
		tabel.setColumnHeader("jenjangPendTerakhir","PENDIDIKAN TERAKHIR");
		tabel.setColumnHeader("prodiPendTerakhir","PRODI TERAKHIR");
		tabel.setColumnHeader("institusiPendTerakhir","INSTITUSI TERAKHIR");
		tabel.setColumnHeader("chek", "CHEK");
		tabel.setVisibleColumns("instansi","namaPegawai","jbtnStruktural","jbtnFungsional","alias","niy","nidn","nis","tempatLahir","tanggalLahir","agama","email","alamatRumah","nomorTelepon","nomorKtp","namaKepegawaian","statusKepegawaian","golongan","thnMasuk","jenjangPendTerakhir","prodiPendTerakhir","institusiPendTerakhir","chek");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
