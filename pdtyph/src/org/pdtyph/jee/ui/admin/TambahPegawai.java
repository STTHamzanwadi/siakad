package org.pdtyph.jee.ui.admin;


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
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
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
		HorizontalLayout hl=new HorizontalLayout();
		VerticalLayout fly=new VerticalLayout();
		VerticalLayout fly1=new VerticalLayout();
		VerticalLayout fly2=new VerticalLayout();
		
		hl.addComponents(fly,fly1,fly2);
		form.addComponent(hl);
		hl.setMargin(true);
		hl.setSpacing(true);
		setSizeFull();
		fieldGroup = new FieldGroup(item);
		fly.addComponent(fieldGroup.buildAndBind("NIY", "niy"));
		fly.addComponent(fieldGroup.buildAndBind("NIS", "nis"));
		fly.addComponent(fieldGroup.buildAndBind("Nama", "namaPegawai"));		
		fly.addComponent(fieldGroup.buildAndBind("Nama Alias", "alias"));
		fly.addComponent(fieldGroup.buildAndBind("Tempat Lahir", "tempatLahir"));
		fly.addComponent(fieldGroup.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		ComboBox jnsKelamin = new ComboBox("Jenis Kelamin");
		jnsKelamin.addItem("Pria");
		jnsKelamin.addItem("Wanita");
		jnsKelamin.setFilteringMode(FilteringMode.CONTAINS);
		jnsKelamin.setImmediate(true);
		fieldGroup.bind(jnsKelamin, "kelamin");
		fly1.addComponent(jnsKelamin);
		ComboBox agama = new ComboBox("Agama");
		agama.addItem("Islam");
		agama.addItem("Kristen");
		agama.addItem("Budha");
		agama.addItem("Hindu");
		agama.addItem("Katolik");
		agama.addItem("Konghucu");
		agama.setFilteringMode(FilteringMode.CONTAINS);
		agama.setImmediate(true);
		fieldGroup.bind(agama, "agama");
		fly1.addComponent(agama);
		fly1.addComponent(fieldGroup.buildAndBind("e-Mail", "email"));
		fieldGroup.getField("email").addValidator(new ValidatorContent());
		fly1.addComponent(fieldGroup.buildAndBind("Alamat Rumah", "alamatRumah"));
		fly1.addComponent(fieldGroup.buildAndBind("No Telpon", "nomorTelepon"));
		fly1.addComponent(fieldGroup.buildAndBind("Nomor KTP", "nomorKtp"));
		ComboBox jStruktural = new ComboBox("Jabatan Struktural");
		BeanItemContainer<JabatanYayasan> JStrukturalContainer = 
				new BeanItemContainer<>(JabatanYayasan.class, j);
		jStruktural.setContainerDataSource(JStrukturalContainer);
		jStruktural.setItemCaptionPropertyId("namaJabatan");
		jStruktural.setFilteringMode(FilteringMode.CONTAINS);
		jStruktural.setImmediate(true);
		fieldGroup.bind(jStruktural, "jbtnStruktural");
		fly2.addComponent(jStruktural);
		ComboBox jFungsional = new ComboBox("Jabatan Fungsional");
		BeanItemContainer<JabatanYayasan> JFungsionalContainer = 
				new BeanItemContainer<>(JabatanYayasan.class, j);
		jFungsional.setContainerDataSource(JFungsionalContainer);
		jFungsional.setItemCaptionPropertyId("namaJabatan");
		jFungsional.setFilteringMode(FilteringMode.CONTAINS);
		jFungsional.setImmediate(true);
		fieldGroup.bind(jFungsional, "jbtnFungsional");
		fly2.addComponent(jFungsional);
		ComboBox stsKepegawaian = new ComboBox("Jenis Jabatan");
		stsKepegawaian.addItem("PTY");
		stsKepegawaian.addItem("PTT");
		stsKepegawaian.addItem("PNS");
		stsKepegawaian.setFilteringMode(FilteringMode.CONTAINS);
		stsKepegawaian.setImmediate(true);
		fieldGroup.bind(stsKepegawaian, "statusKepegawaian");
		fly2.addComponent(stsKepegawaian);
		fly2.addComponent(fieldGroup.buildAndBind("Tahun Masuk", "thnMasuk"));
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
		fly2.addComponent(pendidikan);
		
		fly2.addComponent(fieldGroup.buildAndBind("Prodi Pendidikan Terakhir", "prodiPendTerakhir"));
		fly2.addComponent(fieldGroup.buildAndBind("Institusi Pendidikan Terakhir", "institusiPendTerakhir"));
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
