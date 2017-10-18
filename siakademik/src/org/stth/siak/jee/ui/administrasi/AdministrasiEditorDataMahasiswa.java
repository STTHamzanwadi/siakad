package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisKelamin;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusMasuk;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;



public class AdministrasiEditorDataMahasiswa extends CustomComponent{


	private static final long serialVersionUID = 1744423645758750586L;
	private VerticalLayout vl = new VerticalLayout();
	private BeanItem<Mahasiswa> item;
	private TextField tfNim,tfAngkatan, nama ;
	private TextArea taAlamat,taAlamatWali;
	private ComboBox cbAgama, cbJenisKelamin, cbProdi, cbStatus,cbDosenPa;
	private FieldGroup fg;
	private DosenKaryawan user;
	private Mahasiswa m;
	/**
	 * Open editor for Mahasiswa instance
	 * @param mahasiswa if creating new record, use new Mahasiswa()
	 */

	public AdministrasiEditorDataMahasiswa(Mahasiswa mahasiswa) {
		if (mahasiswa==null) {
			mahasiswa=new Mahasiswa();
			mahasiswa.setStatus(StatusMahasiswa.AKTIF);
			mahasiswa.setAngkatan(GeneralUtilities.getCurrentYearLocal());
		}
		m=mahasiswa;
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setCompositionRoot(vl);
		vl.setMargin(true);
		vl.setSpacing(true);
		Responsive.makeResponsive(this);
		item = new BeanItem<Mahasiswa>(m);
		vl.addComponent(buildForm());
	}

	private Component buildForm() {
		//VerticalLayout root = new VerticalLayout();
		Panel pnl = new Panel("Data Mahasiswa");
		Panel pnlW = new Panel("Data Wali");
		VerticalLayout vl = new VerticalLayout();
		//vl.setMargin(true);
		vl.setSpacing(true);
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hlW = new HorizontalLayout();
		hlW.setSpacing(true);
		hlW.setMargin(true);
		//hl.setSpacing(true);
		FormLayout fl1 = new FormLayout(),fl2 = new FormLayout();
		hl.addComponents(fl1,fl2);
		vl.addComponent(hl);
		vl.addComponent(pnlW);
		pnl.setContent(vl);
		pnlW.setContent(hlW);

		fg = new FieldGroup(item);

		tfNim = new TextField("Nomor Induk");
		tfNim.setReadOnly(true);
		tfNim.addValidator(new StringLengthValidator("minimal 7 karakter", 7, null, false));
		fl1.addComponent(tfNim);
		fg.bind(tfNim, "npm");

		nama= new TextField("Nama");
		fg.bind(nama, "nama");
		fl2.addComponent(nama);
		nama.addValidator(new StringLengthValidator("Nama mininimal 3 karakter", 3, null, false));
		fl1.addComponent(fg.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		fl2.addComponent(fg.buildAndBind("Tempat Lahir", "tempatLahir"));

		Container container = GeneralUtilities.createContainerFromEnumClass(JenisKelamin.class);
		cbJenisKelamin = new ComboBox("Jenis Kelamin", container);
		fl1.addComponent(cbJenisKelamin);
		fg.bind(cbJenisKelamin, "jenisKelamin");

		cbAgama = new ComboBox("Agama", Arrays.asList(GeneralUtilities.AGAMA));
		fl2.addComponent(cbAgama);
		fg.bind(cbAgama, "agama");


		cbProdi = new ComboBox("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setRequired(true);
		cbProdi.setRequiredError("Pilih Program Studi");
		//cbProdi.setReadOnly(true);
		fl1.addComponent(cbProdi);
		fg.bind(cbProdi, "prodi");

		Property<Integer> integerProperty = item
				.getItemProperty("angkatan");
		tfAngkatan = new TextField("angkatan", integerProperty);
		tfAngkatan.setRequired(true);
		tfAngkatan.setRequiredError("Angkatan harus diisi");
		fl2.addComponent(tfAngkatan);
		fg.bind(tfAngkatan, "angkatan");

		cbStatus = new ComboBox("Status", Arrays.asList(StatusMahasiswa.values()));
		cbStatus.setRequired(true);
		cbStatus.setRequiredError("Pilih status mahasiswa");

		//tfStatus.setReadOnly(true);
		fl1.addComponent(cbStatus);
		fg.bind(cbStatus, "status");

		cbDosenPa = new ComboBox("Pembimbing Akademik", DosenKaryawanPersistence.getDosen());
		fg.bind(cbDosenPa, "pembimbingAkademik");		
		fl2.addComponent(cbDosenPa);


		fl1.addComponent(fg.buildAndBind("Asal Sekolah", "asalSekolah"));


		fl2.addComponent(fg.buildAndBind("Nomor Telepon", "nomorHP"));
		ComboBox cbStatusMasuk = new ComboBox("Status Masuk", Arrays.asList(StatusMasuk.values()));
		cbStatusMasuk.setRequired(true);
		cbStatusMasuk.setRequiredError("Pilih status masuk");
		fg.bind(cbStatusMasuk, "statusMasuk");
		fl2.addComponent(cbStatusMasuk);
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
		simpan.addClickListener(e-> {
			simpan();

		});
		vl.addComponent(simpan);
		return pnl;
	}

	private void simpan() {
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("npm", tfNim.getValue()));
		List<Mahasiswa> l = GenericPersistence.findList(Mahasiswa.class,lc );
		System.out.println(l.size());
		if (l.size()>1) {
			batalSimpan();
		}else if (l.size()>0) {
			if (m.getId()>0) {
				if (m.getId()==l.get(0).getId()) {
					save();
				}else{
					batalSimpan();
				}
			}else{
				batalSimpan();
			}
			
		}else{
			save();
		}

	}

	private void batalSimpan() {
		Notification.show("Gunakan NIM yang lain \n"
				+ "NIM ini sudah dipakai", Notification.Type.ERROR_MESSAGE);
	}

	private void save() {
		try {
			fg.commit();
			Mahasiswa m =  item.getBean();
			m.setUpdateOleh(user);
			GenericPersistence.merge(m);
			Notification.show("Data berhasil Simpan", Notification.Type.HUMANIZED_MESSAGE);
		} catch (CommitException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

}
