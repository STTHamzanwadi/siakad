package org.stth.siak.jee.ui.generalview;

import org.stth.siak.entity.Mahasiswa;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class MahasiswaProfilView extends CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	TextField npm = new TextField("Nomor Induk Mahasiswa");
	TextField nama = new TextField("Nama Mahasiswa");
	TextField txtProdi = new TextField("Program Studi");
	TextField txtJenisKelamin = new TextField("Jenis Kelamin");
	TextField tempatLahir = new TextField("Tempat Lahir");
	TextField txtPembimbingAkademik = new TextField("Pembimbing Akademik");
	DateField tanggalLahir = new DateField("Tanggal Lahir");
	public MahasiswaProfilView(Mahasiswa mhs){
		FormLayout layout = new FormLayout();
        layout.addComponent(npm);
        layout.addComponent(nama);
        layout.addComponent(txtProdi);
        layout.addComponent(txtJenisKelamin);
        layout.addComponent(tanggalLahir);
        layout.addComponent(tempatLahir);
        layout.addComponent(txtPembimbingAkademik);
        BeanItem<Mahasiswa> item = new BeanItem<Mahasiswa>(mhs);
        FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        txtProdi.setValue(mhs.getProdi().toString());
        txtJenisKelamin.setValue(mhs.getJenisKelamin().toString());
        txtPembimbingAkademik.setValue(mhs.getPembimbingAkademik().toString());
        setCompositionRoot(layout);
	}

}
