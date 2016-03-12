package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.JenisKelamin;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AdministrasiEditorDataMahasiswa extends CustomComponent{
	
	private VerticalLayout vl = new VerticalLayout();
	private Mahasiswa mahasiswa;
	private BeanItem<Mahasiswa> item;
	private TextField tfNama,tfNim,tfTempatLahir,tfProdi,tfAngkatan,tfAsalSekolah,tfTahunLulus,tfTelp,tfEmail,
			tfWali,tfTelpWali,tfDosenPa,tfStatus;
	private TextArea taAlamat,taAlamatWali;
	private ComboBox cbAgama, cbJenisKelamin;
	private DateField dtTglLahir;
	private FieldGroup fg;
	private DosenKaryawan user;
	/**
	 * Open editor for Mahasiswa instance
	 * @param mahasiswa if creating new record, use new Mahasiswa()
	 */
	
	public AdministrasiEditorDataMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa =mahasiswa;
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setCompositionRoot(vl);
		vl.setMargin(true);
		Responsive.makeResponsive(this);
		item = new BeanItem<Mahasiswa>(mahasiswa);
		vl.addComponent(buildForm());
	}
	
	private Component buildForm() {
		//VerticalLayout root = new VerticalLayout();
		Panel pnl = new Panel("Data Mahasiswa");
		Panel pnlW = new Panel("Data Wali");
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hlW = new HorizontalLayout();
		hlW.setSpacing(true);
		hlW.setMargin(true);
		hl.setSpacing(true);
		FormLayout fl1 = new FormLayout(),fl2 = new FormLayout();
		hl.addComponents(fl1,fl2);
		vl.addComponent(hl);
		vl.addComponent(pnlW);
		pnl.setContent(vl);
		pnlW.setContent(hlW);
		
		fg = new FieldGroup(item);
		
		tfNim = new TextField("Nomor Induk");
		tfNim.setReadOnly(true);
		fl1.addComponent(tfNim);
		fg.bind(tfNim, "npm");
		
		fl2.addComponent(fg.buildAndBind("Nama", "nama"));
		fl1.addComponent(fg.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		fl2.addComponent(fg.buildAndBind("Tempat Lahir", "tempatLahir"));
		
		Container container = GeneralUtilities.createContainerFromEnumClass(JenisKelamin.class);
		cbJenisKelamin = new ComboBox("Jenis Kelamin", container);
		fl1.addComponent(cbJenisKelamin);
		fg.bind(cbJenisKelamin, "jenisKelamin");
		
		cbAgama = new ComboBox("Agama", Arrays.asList(GeneralUtilities.AGAMA));
		fl2.addComponent(cbAgama);
		fg.bind(cbAgama, "agama");
		
		
		
		tfProdi = new TextField("Program Studi");
		tfProdi.setValue(mahasiswa.getProdi().toString());
		tfProdi.setReadOnly(true);
		fl1.addComponent(tfProdi);
		//fg.bind(tfProdi, "prodi");
		
		Property<Integer> integerProperty = (Property<Integer>) item
		        .getItemProperty("angkatan");
		tfAngkatan = new TextField("angkatan", integerProperty);
		fl2.addComponent(tfAngkatan);
		fg.bind(tfAngkatan, "angkatan");
		
		tfStatus = new TextField("Status");
		tfStatus.setValue(mahasiswa.getStatus().toString());
		tfStatus.setReadOnly(true);
		fl1.addComponent(tfStatus);
		
		tfDosenPa = new TextField("Pembimbing Akademik");
		tfDosenPa.setValue(mahasiswa.getPembimbingAkademik().getNama());
		tfDosenPa.setReadOnly(true);
		fl2.addComponent(tfDosenPa);
		
		fl1.addComponent(fg.buildAndBind("Asal Sekolah", "asalSekolah"));
		
		Property<Integer> integerProperty2 = (Property<Integer>) item
		        .getItemProperty("tahunLulus");
		tfTahunLulus = new TextField("tahunLulus", integerProperty2);
		fl2.addComponent(tfTahunLulus);
		fg.bind(tfTahunLulus, "tahunLulus");
		
		fl1.addComponent(fg.buildAndBind("Nomor Telepon", "nomorHP"));
		fl2.addComponent(fg.buildAndBind("Alamat Email", "email"));
		
		taAlamat = new TextArea("Alamat");
		fg.bind(taAlamat, "alamat");
		fl1.addComponent(taAlamat);
		
		fl2.addComponent(new Label());
		
		hlW.addComponent(fg.buildAndBind("Nama Wali", "wali"));
		hlW.addComponent(fg.buildAndBind("Nomor Telepon Wali", "nomorHPWali"));
		
		taAlamatWali = new TextArea("Alamat Wali");
		fg.bind(taAlamatWali, "alamatWali");
		hlW.addComponent(taAlamatWali);
		
		Button simpan = new Button("Simpan");
		simpan.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fg.commit();
					Mahasiswa m =  item.getBean();
					m.setUpdateOleh(user);
					GenericPersistence.merge(item.getBean());
					Notification.show("Perubahan data berhasil dilakukan", Notification.Type.HUMANIZED_MESSAGE);
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		vl.addComponent(simpan);
		return pnl;
	}
	
	

}
