package org.pdtyph.jee.ui.opryayasan;


import java.util.List;

import org.pdtyph.cummon.ValidatorContent;
import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.JabatanYayasan;
import org.pdtyph.entity.PegawaiYayasan;
import org.pdtyph.entity.Prodi;
import org.yph.jee.persistence.GenericPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
	private BeanItem<PegawaiYayasan> item;
	private FieldGroup fieldGroup;

	public TambahPegawai(PegawaiYayasan p,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		item = new BeanItem<PegawaiYayasan>(p);
		root.addComponent(buildHeader());
		root.addComponent(buildForm());
		Button simpan=new Button("Simpan",FontAwesome.SAVE);
		simpan.addStyleName(ValoTheme.BUTTON_FRIENDLY);
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
		
		List<JabatanYayasan> j = GenericPersistence.findList(JabatanYayasan.class);
		VerticalLayout pnl = new VerticalLayout();
		
		FormLayout form = new FormLayout();
		GridLayout fly=new GridLayout(3, 6);
		
		
	
		form.addComponent(fly);
		fly.setMargin(true);
		fly.setSpacing(true);
		setSizeFull();
		fieldGroup = new FieldGroup(item);
		TextField txtNiy=new TextField("NIY");
		txtNiy.setWidth("300px");
		fieldGroup.bind(txtNiy, "niy");
		fly.addComponent(txtNiy);
		TextField txtNip=new TextField("NIP");
		txtNip.setWidth("300px");
		fieldGroup.bind(txtNip, "nis");
		fly.addComponent(txtNip);
		TextField txtNama=new TextField("Nama Pegawai");
		txtNama.setWidth("300px");
		fieldGroup.bind(txtNama,"namaPegawai");
		fly.addComponent(txtNama);
		TextField txtNamaAlias=new TextField("Nama Alias");
		txtNamaAlias.setWidth("300px");
		fieldGroup.bind(txtNamaAlias,"alias");
		fly.addComponent(txtNamaAlias);
		TextField txtTempatlahir=new TextField("Tempat Lahir");
		txtTempatlahir.setWidth("300px");
		fieldGroup.bind(txtTempatlahir,"tempatLahir");
		fly.addComponent(txtTempatlahir);
		HorizontalLayout hl0=new HorizontalLayout();
		TextField txtTelpon=new TextField("No Telpon");
		txtTelpon.setWidth("165px");
		fieldGroup.bind(txtTelpon, "nomorTelepon");
		hl0.addComponent(txtTelpon);
		hl0.setSpacing(true);
		hl0.addComponent(fieldGroup.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		fly.addComponent(hl0);
		
		HorizontalLayout hl1=new HorizontalLayout();
		hl1.setSpacing(true);
		ComboBox jnsKelamin = new ComboBox("Jenis Kelamin");
		jnsKelamin.addItem("Pria");
		jnsKelamin.addItem("Wanita");
		jnsKelamin.setFilteringMode(FilteringMode.CONTAINS);
		jnsKelamin.setImmediate(true);
		jnsKelamin.setWidth("147px");
		fieldGroup.bind(jnsKelamin, "kelamin");
		hl1.addComponent(jnsKelamin);
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
		hl1.addComponent(agama);
		fly.addComponent(hl1);
		TextField txtEmail=new TextField("e-Mail");
		txtEmail.setWidth("300px");
		fieldGroup.bind(txtEmail, "email");
		fly.addComponent(txtEmail);
		fieldGroup.getField("email").addValidator(new EmailValidator("e-mail invalid"));
		TextField txtAlamat=new TextField("Alamat Rumah");
		txtAlamat.setWidth("300px");
		fieldGroup.bind(txtAlamat, "alamatRumah");
		fly.addComponent(txtAlamat);
		
		TextField txtKtp=new TextField("Nomor KTP");
		txtKtp.setWidth("300px");
		fieldGroup.bind(txtKtp, "nomorKtp");
		fly.addComponent(txtKtp);
		
		ComboBox jStruktural = new ComboBox("Jabatan Struktural");
		BeanItemContainer<JabatanYayasan> JStrukturalContainer = 
				new BeanItemContainer<>(JabatanYayasan.class, j);
		jStruktural.setContainerDataSource(JStrukturalContainer);
		jStruktural.setItemCaptionPropertyId("namaJabatan");
		jStruktural.setFilteringMode(FilteringMode.CONTAINS);
		jStruktural.setImmediate(true);
		jStruktural.setWidth("300px");
		fieldGroup.bind(jStruktural, "jbtnStruktural");
		fly.addComponent(jStruktural);
		ComboBox jFungsional = new ComboBox("Jabatan Fungsional");
		BeanItemContainer<JabatanYayasan> JFungsionalContainer = 
				new BeanItemContainer<>(JabatanYayasan.class, j);
		jFungsional.setContainerDataSource(JFungsionalContainer);
		jFungsional.setItemCaptionPropertyId("namaJabatan");
		jFungsional.setFilteringMode(FilteringMode.CONTAINS);
		jFungsional.setImmediate(true);
		fieldGroup.bind(jFungsional, "jbtnFungsional");
		fly.addComponent(jFungsional);
		jFungsional.setWidth("300px");
		HorizontalLayout hl2=new HorizontalLayout();
		hl2.setSpacing(true);
		ComboBox stsKepegawaian = new ComboBox("Jenis Jabatan");
		stsKepegawaian.setWidth("163px");
		stsKepegawaian.addItem("PTY");
		stsKepegawaian.addItem("PTT");
		stsKepegawaian.addItem("PNS");
		stsKepegawaian.setFilteringMode(FilteringMode.CONTAINS);
		stsKepegawaian.setImmediate(true);
		fieldGroup.bind(stsKepegawaian, "statusKepegawaian");
		hl2.addComponent(stsKepegawaian);
		hl2.addComponent(fieldGroup.buildAndBind("Tahun Masuk", "thnMasuk"));
		fly.addComponent(hl2);
		HorizontalLayout hl3=new HorizontalLayout();
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
		fieldGroup.bind( pendidikan, "jenjangPendTerakhir");
		hl3.addComponent(pendidikan);
		hl3.setSpacing(true);
		TextField txtProdi=new TextField("Prodi Pendidikan Terakhir");
		txtProdi.setWidth("163px");
		fieldGroup.bind(txtProdi, "prodiPendTerakhir");
		hl3.addComponent(txtProdi);
		fly.addComponent(hl3);
		TextField txtInstitusi=new TextField("Institusi Pendidikan Terakhir");
		txtInstitusi.setWidth("300px");
		fieldGroup.bind(txtInstitusi, "institusiPendTerakhir");
		fly.addComponent(txtInstitusi);		
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
