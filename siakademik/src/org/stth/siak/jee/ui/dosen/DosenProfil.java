package org.stth.siak.jee.ui.dosen;


import java.util.Arrays;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.enumtype.StatusDosen;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenProfil extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	TextField txtNama,txtNIDN,txtNIS,txtTanggalLahir,txtEmail,txtAlamat,txtKtp;
	TextField txtJenjangPendidikanTerakhir,txtProdiPendidikanTerakhir,txtInstitusiPendidikanTerakhir;
	private ComboBox status;
	private BeanItem<DosenKaryawan> item;
	private Button simpan = new Button("Simpan");

	public DosenProfil() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		DosenKaryawan d = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		item = new BeanItem<DosenKaryawan>(d);
		addComponent(ViewFactory.header("Profil Anda"));
			addComponent(buildForm());
		addComponent(ViewFactory.footer());
	}
	private Component buildForm() {
		VerticalLayout pnl = new VerticalLayout();
		FormLayout flkiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		FieldGroup binder = new FieldGroup(item);
		flkiri.addComponent(binder.buildAndBind("Nama", "nama"));
		flkiri.addComponent(binder.buildAndBind("Alias", "alias"));
		flkiri.addComponent(binder.buildAndBind("NIS", "nis"));
		flkiri.addComponent(binder.buildAndBind("NIDN", "nidn"));
		flkiri.addComponent(binder.buildAndBind("No. KTP", "nomorKtp"));
		flkiri.addComponent(binder.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		flkiri.addComponent(binder.buildAndBind("Tempat Lahir", "tempatLahir"));
		
		flKanan.addComponent(binder.buildAndBind("Telepon", "nomorTelepon"));
		flKanan.addComponent(binder.buildAndBind("e-mail", "email"));
		flKanan.addComponent(binder.buildAndBind("Tahun masuk", "thnMasuk"));
		flKanan.addComponent(binder.buildAndBind("Pendidikan Terakhir", "jenjangPendTerakhir"));
		flKanan.addComponent(binder.buildAndBind("Program Studi", "prodiPendTerakhir"));
		flKanan.addComponent(binder.buildAndBind("Institusi", "institusiPendTerakhir"));
		status = new ComboBox("Status", Arrays.asList(StatusDosen.values()));
		binder.bind(status, "status");
		flKanan.addComponent(status);;
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponents(flkiri, flKanan);
		
		simpan.addClickListener(e->{
			simpanDK(binder);
		});
		pnl.addComponents(hl, simpan);
		return pnl;
	}
	
	private void simpanDK(FieldGroup binder) {
		try {
			binder.commit();
			DosenKaryawan dk= item.getBean();
			dk.setUpdateOleh(dk);
			GenericPersistence.merge(dk);
			Notification.show("Data berhasil disimpan", Notification.Type.HUMANIZED_MESSAGE);
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
