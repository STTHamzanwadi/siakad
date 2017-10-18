package org.stth.siak.jee.ui.generalview;


import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ProfilUmumDosenKaryawan extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	TextField txtNama,txtNIDN,txtNIS,txtTanggalLahir,txtEmail,txtAlamat,txtKtp;
	TextField txtJenjangPendidikanTerakhir,txtProdiPendidikanTerakhir,txtInstitusiPendidikanTerakhir;
	private BeanItem<DosenKaryawan> item;
	private DosenKaryawan dk;
	private VerticalLayout pnl;
	private Button simpan = new Button("Simpan");

	public ProfilUmumDosenKaryawan() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dk = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		item = new BeanItem<DosenKaryawan>(dk);
		addComponent(ViewFactory.header("Profil Anda"));
		addComponent(buildForm());
		addComponent(ViewFactory.footer());
	}
	private Component buildForm() {
		pnl = new VerticalLayout();
		if (dk.isDosen()){
			buildForDosen();
		} else {
			buildForKaryawan();
		}
		return pnl;
	}
	private void buildForKaryawan() {
		FormLayout fly = new FormLayout();
		FieldGroup binder = new FieldGroup(item);
		fly.addComponent(binder.buildAndBind("Nama", "nama"));
		fly.addComponent(binder.buildAndBind("Alias", "alias"));
		fly.addComponent(binder.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		fly.addComponent(binder.buildAndBind("Tempat Lahir", "tempatLahir"));
		fly.addComponent(binder.buildAndBind("No. KTP", "nomorKtp"));
		fly.addComponent(binder.buildAndBind("Telepon", "nomorTelepon"));
		fly.addComponent(binder.buildAndBind("e-mail", "email"));
		fly.addComponent(binder.buildAndBind("Alamat Rumah", "alamatRumah"));
		fly.addComponent(binder.buildAndBind("Tahun masuk", "thnMasuk"));
		fly.addComponent(simpan);
		simpan.addClickListener(e->{
			simpanDK(binder);
		});
		pnl.addComponent(fly);
		
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
	private void buildForDosen() {
		FormLayout fly = new FormLayout();
		FieldGroup binder = new FieldGroup(item);
		fly.addComponent(binder.buildAndBind("Nama", "nama"));
		fly.addComponent(binder.buildAndBind("Alias", "alias"));
		fly.addComponent(binder.buildAndBind("NIS", "nis"));
		fly.addComponent(binder.buildAndBind("NIDN", "nidn"));
		fly.addComponent(binder.buildAndBind("No. KTP", "nomorKtp"));
		fly.addComponent(binder.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		fly.addComponent(binder.buildAndBind("Tempat Lahir", "tempatLahir"));
		fly.addComponent(binder.buildAndBind("Telepon", "nomorTelepon"));
		fly.addComponent(binder.buildAndBind("e-mail", "email"));
		fly.addComponent(binder.buildAndBind("Tahun masuk", "thnMasuk"));
		fly.addComponent(binder.buildAndBind("Pendidikan Terakhir", "jenjangPendTerakhir"));
		fly.addComponent(binder.buildAndBind("Program Studi", "prodiPendTerakhir"));
		fly.addComponent(binder.buildAndBind("Institusi", "institusiPendTerakhir"));
		fly.addComponent(binder.buildAndBind("Status", "status"));
		fly.addComponent(simpan);
		simpan.addClickListener(e->{
			simpanDK(binder);
		});
		pnl.addComponent(fly);
	}
	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
