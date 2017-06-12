package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.RiwayatPekerjaan;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.util.CaseUtil;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AdministrasiEditorAlumni extends Window{
	private static final long serialVersionUID = 1L;
	private Mahasiswa m;
	private RiwayatPekerjaan rp;
	private FieldGroup fgMahasiswa ;
	private FieldGroup fgRiwayat;
	private BeanItem<Mahasiswa> beanMahasiswa;
	private BeanItem<RiwayatPekerjaan> beanRiwayatKerja;
	private Panel pRiwayat;
	private ComboBox status;
	private DosenKaryawan user;
	private TextArea judulSkripsi;
	private List<RiwayatPekerjaan> lRP;
	private AbstractField<Boolean> cekBekerja;
	
	
	public AdministrasiEditorAlumni(Mahasiswa m ) {
		setCaption("Data Alumni");
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		if (m!=null) {
			this.m=m;
		}
		
		setWidth("600px");
		setCaption("Edit Data Alumni");
		setContent(buildLayout());
		center();
	}
	private Component buildLayout(){
		beanMahasiswa = new BeanItem<Mahasiswa>(this.m);
		fgMahasiswa = new FieldGroup(beanMahasiswa);
		
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		Panel p = new Panel("Mahasiswa");
		FormLayout flkiri = new FormLayout();
		FormLayout flKanan= new FormLayout();
		TextField nama = new TextField("Nama");
		TextField nim = new TextField("NIM");
		TextField prodi = new TextField("prodi");
		prodi.setValue(m.getProdi().toString());
		prodi.setReadOnly(true);
		TextField jenjang = new TextField("Jenjang");
		TextArea alamat = new TextArea("Alamat Rumah");
		TextField noHP= new TextField("No Handphone");
		TextField email = new TextField("Email");
		TextField facebook = new TextField("Facebook");
		TextField angkatanLulus = new TextField("Angkatan Lulus");
		DateField tanggalLulus = new DateField("Lulus");
		status = new ComboBox("Status",  Arrays.asList(StatusMahasiswa.values()));
		status.setValue(m.getStatus());
		
		
		fgMahasiswa.bind(status, "status");
		
		fgMahasiswa.bind(nama, "nama");
		fgMahasiswa.bind(nim, "npm");
		fgMahasiswa.bind(alamat, "alamat");
		fgMahasiswa.bind(noHP, "nomorHP");
		fgMahasiswa.bind(angkatanLulus, "angkatanLulus");
		fgMahasiswa.bind(email, "email");
		fgMahasiswa.bind(facebook, "facebook");
		fgMahasiswa.bind(tanggalLulus, "tanggalLulus");
		
		flkiri.addComponents(nama, nim, prodi, jenjang, alamat);
		flKanan.addComponents(noHP, email, facebook, status, angkatanLulus, tanggalLulus);
		judulSkripsi = new TextArea("Judul Skripsi");
		Button toUpperCase = new Button("To UpperCase First Letter");
		toUpperCase.addClickListener(e-> {
			judulSkripsi.setValue(CaseUtil.upperfirsLetter(judulSkripsi.getValue()));
		});
		fgMahasiswa.bind(judulSkripsi, "judulSkripsi");
		
		judulSkripsi.setWidth("500px");
		
		VerticalLayout vlJudul = new VerticalLayout();
		vlJudul.addComponents(judulSkripsi, toUpperCase);
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(flkiri, flKanan);
		VerticalLayout vlMahasiswa = new VerticalLayout();
		vlMahasiswa.addComponents(hl, vlJudul);
		p.setContent(vlMahasiswa);
		
		vl.addComponent(p);
		HorizontalLayout hlCek = new HorizontalLayout();
		hlCek.setMargin(true);
		cekBekerja = new CheckBox("Sudah Bekerja");
		cekBekerja.addValueChangeListener(v->{
			if(cekBekerja.getValue()){
				pRiwayat.setEnabled(true);
			}else{
				pRiwayat.setEnabled(false);
			}
			
		});
		vl.addComponent(cekBekerja);
		vl.addComponent(buildLayoutPekerjaan());
		Button simpan = new Button("Simpan");
		simpan.addClickListener(klik->{
			try {
				fgMahasiswa.commit();
				m.setUpdateOleh(user);
				GenericPersistence.merge(m);
				fgRiwayat.commit();
				if (cekBekerja.getValue()) {
					GenericPersistence.merge(rp);
					System.out.println(cekBekerja.getValue());
				}else{
					if(lRP.size()>0){
						GenericPersistence.delete(rp);
					}	
				}
				Notification.show("Perubahan data berhasil dilakukan", Notification.Type.HUMANIZED_MESSAGE);
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		vl.addComponent(simpan);
		return vl;
		
	}
	private Component buildLayoutPekerjaan(){
		List<Criterion> c = new ArrayList<>();
		c.add(Restrictions.eq("mahasiswa", m));
		pRiwayat = new Panel("Riwayat Pekerjaan");
		lRP = GenericPersistence.findList(RiwayatPekerjaan.class, c);
		if(lRP.size()>0){
			rp = lRP.get(0);
			pRiwayat.setEnabled(true);
			cekBekerja.setValue(true);
		}else{
			pRiwayat.setEnabled(false);
			rp=new RiwayatPekerjaan();
			
		}
		rp.setMahasiswa(m);
		beanRiwayatKerja=new BeanItem<RiwayatPekerjaan>(rp);
		fgRiwayat= new FieldGroup(beanRiwayatKerja);
		
		FormLayout fl = new FormLayout();
		TextField instansi = new TextField("Instansi");
		TextArea alamatInstansi = new TextArea("Alamat Instansi");
		ComboBox cbLamaMenunggu = new ComboBox("Lama Menunggu Pekerjaan");
		cbLamaMenunggu.addItems("< 6 Bulan", "6-12 bulan", ">12 bulan");
		ComboBox sumberInformasi = new ComboBox("Sumber Informasi Pekerjaan");
		
		List<String> itemIds = new ArrayList<>();
		itemIds.add("Media Cetak");
		itemIds.add("Teman");
		itemIds.add("Media Elektronik");
		itemIds.add("Almamater/ Fakultas");
		itemIds.add("Orang tua/ Saudara");
		itemIds.add("Lainnya");
		sumberInformasi.addItems(itemIds);
		
		fgRiwayat.bind(instansi, "instansi");
		fgRiwayat.bind(alamatInstansi, "alamatInstansi");
		fgRiwayat.bind(cbLamaMenunggu, "lamaMenungguKerja");
		fgRiwayat.bind(sumberInformasi, "sumberPekerjaan");
		
		fl.addComponents(instansi, alamatInstansi, cbLamaMenunggu, sumberInformasi);
		pRiwayat.setContent(fl);
		
		return pRiwayat;
	}
}
