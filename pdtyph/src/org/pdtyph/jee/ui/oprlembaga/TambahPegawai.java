package org.pdtyph.jee.ui.oprlembaga;


import java.util.List;

import org.pdtyph.cummon.ValidatorContent;
import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.Prodi;
import org.pdtyph.entity.UserOprInstansi;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.persistence.PegawaiPersistence;

import sun.security.provider.certpath.OCSP.RevocationStatus.CertStatus;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class TambahPegawai extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	//	
	private Label titleLabel;
	private VerticalLayout root;
	private BeanItem<Pegawai> item;
	private FieldGroup fieldGroup;
	private ComboBox instansi = new ComboBox("Instansi");
	
	@SuppressWarnings("serial")
	public TambahPegawai(Pegawai p,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		item = new BeanItem<Pegawai>(p);
		root.addComponent(buildHeader());
		root.addComponent(buildForm());
		Button simpan=new Button("Simpan",FontAwesome.SAVE);
		root.addComponent(simpan);
		simpan.addClickListener(new ClickListener() {
			@Override public void buttonClick(ClickEvent event) {
				try {
					
						fieldGroup.commit();
						BeanItem<?> bi = (BeanItem<?>) fieldGroup.getItemDataSource();
						GenericPersistence.merge(bi.getBean());					
						
						parent.close();
					
					
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//GenericPersistence.merge(item.getBean());
			}

		});
	}
	

	private Component buildForm() {
		UserOprInstansi user = (UserOprInstansi) VaadinSession.getCurrent().getAttribute(
				UserOprInstansi.class);
		List<Instansi> i = GenericPersistence.findList(Instansi.class);
		List<Jabatan> j = PegawaiPersistence.getJabatanByLembaga(user.getInstansi());
		VerticalLayout pnl = new VerticalLayout();
		
		FormLayout form = new FormLayout();
		final GridLayout fly=new GridLayout(3,8);
		fly.setMargin(true);
		fly.setSpacing(true);
		form.addComponent(fly);
		fieldGroup = new FieldGroup(item);
		//1
		
		TextField txtNiy=new TextField("NIY");
		fieldGroup.bind(txtNiy, "niy");
		txtNiy.setWidth("300px");
		fly.addComponent(txtNiy);
		TextField txtNidn=new TextField("NIDN/ NUPN");
		fieldGroup.bind(txtNidn, "nidn");
		txtNidn.setWidth("300px");
		fly.addComponent(txtNidn);
		TextField txtNis=new TextField("NIP/ NUPTK");
		fieldGroup.bind(txtNis, "nis");
		txtNis.setWidth("300px");
		fly.addComponent(txtNis);
		//2
		TextField txtNama=new TextField("Nama");
		fieldGroup.bind(txtNama, "namaPegawai");
		txtNama.setWidth("300px");
		fly.addComponent(txtNama);
		TextField txtAlias=new TextField("Alias");
		fieldGroup.bind(txtAlias, "alias");
		txtAlias.setWidth("300px");
		fly.addComponent(txtAlias);	
		HorizontalLayout hLy=new HorizontalLayout();
		ComboBox jnsKelamin = new ComboBox("Jenis Kelamin");
		jnsKelamin.addItem("Pria");
		jnsKelamin.addItem("Wanita");
		jnsKelamin.setFilteringMode(FilteringMode.CONTAINS);
		jnsKelamin.setImmediate(true);
		jnsKelamin.setWidth("147px");
		fieldGroup.bind(jnsKelamin, "kelamin");
		hLy.addComponent(jnsKelamin);
		ComboBox agama = new ComboBox("Agama");
		agama.setWidth("147px");
		agama.addItem("Islam");
		agama.addItem("Kristen");
		agama.addItem("Budha");
		agama.addItem("Hindu");
		agama.addItem("Katolik");
		agama.addItem("Konghucu");
		agama.setFilteringMode(FilteringMode.CONTAINS);
		agama.setImmediate(true);
		fieldGroup.bind(agama, "agama");
		hLy.setSpacing(true);
		hLy.addComponent(agama);
		fly.addComponent(hLy);
		//3
		TextField txtTempat=new TextField("Tempat Lahir");
		fieldGroup.bind(txtTempat, "tempatLahir");
		txtTempat.setWidth("300px");
		fly.addComponent(txtTempat);
		DateField txtTanggal=new DateField("Tanggal Lahir");
		fieldGroup.bind(txtTanggal, "tanggalLahir");
		txtTanggal.setWidth("300px");
		fly.addComponent(txtTanggal);	
		
		//4
		TextField txtEmail=new TextField("Email");
		fieldGroup.bind(txtEmail, "email");
		txtEmail.setWidth("300px");
		fly.addComponent(txtEmail);	
		fieldGroup.getField("email").addValidator(new EmailValidator("e-mail invalid"));
		
		TextField txtKtp=new TextField("Nomor KTP");
		txtKtp.setWidth("300px");
		fieldGroup.bind(txtKtp,  "nomorKtp");
		fly.addComponent(txtKtp);
		//5
		TextField txtAlamat=new TextField("Alamat Rumah");
		fieldGroup.bind(txtAlamat, "alamatRumah");
		txtAlamat.setWidth("300px");
		fly.addComponent(txtAlamat);	
		HorizontalLayout hLo=new HorizontalLayout();
		hLo.setSpacing(true);
		TextField txtNotelp=new TextField("No Telpon");
		txtNotelp.setWidth("147px");
		fieldGroup.bind(txtNotelp, "nomorTelepon");
		hLo.addComponent(txtNotelp);
		TextField txtThunmasuk=new TextField("Tahun Masuk");
		txtThunmasuk.setWidth("147px");
		fieldGroup.bind(txtThunmasuk, "thnMasuk");
		hLo.addComponent(txtThunmasuk);
		fly.addComponent(hLo);
		BeanItemContainer<Instansi> instansiContainer = 
				new BeanItemContainer<>(Instansi.class, i);
		instansi.setContainerDataSource(instansiContainer);
		instansi.setItemCaptionPropertyId("nama");
		instansi.setFilteringMode(FilteringMode.CONTAINS);
		instansi.setImmediate(true);
		fieldGroup.bind(instansi, "instansi");
		instansi.setRequiredError("Don't empty value!");
		instansi.setWidth("300px");
		fly.addComponent(instansi);
		
		//6
		ComboBox jStruktural = new ComboBox("Jabatan Struktural");
		BeanItemContainer<Jabatan> JStrukturalContainer = 
				new BeanItemContainer<>(Jabatan.class, j);
		jStruktural.setContainerDataSource(JStrukturalContainer);
		jStruktural.setItemCaptionPropertyId("namaJabatan");
		jStruktural.setFilteringMode(FilteringMode.CONTAINS);
		jStruktural.setImmediate(true);
		jStruktural.setWidth("300px");
		fieldGroup.bind(jStruktural, "jbtnStruktural");
		fly.addComponent(jStruktural);
		ComboBox jFungsional = new ComboBox("Jabatan Fungsional");
		BeanItemContainer<Jabatan> JFungsionalContainer = 
				new BeanItemContainer<>(Jabatan.class, j);
		jFungsional.setContainerDataSource(JFungsionalContainer);
		jFungsional.setItemCaptionPropertyId("namaJabatan");
		jFungsional.setFilteringMode(FilteringMode.CONTAINS);
		jFungsional.setImmediate(true);
		jFungsional.setWidth("300px");
		fieldGroup.bind(jFungsional, "jbtnFungsional");
		fly.addComponent(jFungsional);
		ComboBox jnsKepegawaian = new ComboBox("Jenis Profesi");
		jnsKepegawaian.addItem("Dosen");
		jnsKepegawaian.addItem("Guru");
		jnsKepegawaian.addItem("Karyawan");
		jnsKepegawaian.setFilteringMode(FilteringMode.CONTAINS);
		jnsKepegawaian.setImmediate(true);
		jnsKepegawaian.setWidth("300px");
		fieldGroup.bind(jnsKepegawaian, "namaKepegawaian");
		fly.addComponent(jnsKepegawaian);
		HorizontalLayout hl=new HorizontalLayout();
		ComboBox stsKepegawaian = new ComboBox("Jenis Jabatan");
		stsKepegawaian.addItem("PTY");
		stsKepegawaian.addItem("PTT");
		stsKepegawaian.addItem("PNS");
		stsKepegawaian.setFilteringMode(FilteringMode.CONTAINS);
		stsKepegawaian.setImmediate(true);
		fieldGroup.bind(stsKepegawaian, "statusKepegawaian");
		hl.addComponent(stsKepegawaian);
		stsKepegawaian.setWidth("147px");
		TextField txtGol=new  TextField("Golongan");
		txtGol.setWidth("147px");
		fieldGroup.bind(txtGol, "golongan");
		hl.addComponent(txtGol);
		hl.setSpacing(true);
		fly.addComponent(hl);
				
		TextField txtProdi=new TextField("Prodi Pendidikan Terakhir");
		txtProdi.setWidth("300px");
		fieldGroup.bind(txtProdi, "prodiPendTerakhir");
		fly.addComponent(txtProdi);
		TextField txtInstitusi=new TextField("Institusi Pendidikan Terakhir");
		txtInstitusi.setWidth("300px");
		fieldGroup.bind(txtInstitusi, "institusiPendTerakhir");
		fly.addComponent(txtInstitusi);
		ComboBox  pendidikan = new ComboBox ("Pendidikan Terakhir");
		pendidikan.addItem("SD");
		pendidikan.addItem("SMP");
		pendidikan.addItem("SMA");
		pendidikan.addItem("D1");
		pendidikan.addItem("D2");
		pendidikan.addItem("D3");
		pendidikan.addItem("MDQH");
		pendidikan.addItem("S1");
		pendidikan.addItem("S2");
		pendidikan.addItem("S3");
		pendidikan.setFilteringMode(FilteringMode.CONTAINS);
		pendidikan.setImmediate(true);
		pendidikan.setWidth("147px");
		fieldGroup.bind( pendidikan, "jenjangPendTerakhir");
		fly.addComponent(pendidikan);
		fly.addComponent(new UploadExample());
		
		
		pnl.addComponent(form);
		return pnl;
	}

	private Component buildHeader() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		titleLabel = new Label("Profil Pegawai");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H4);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);

		header.addComponent(titleLabel);

		return header;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
