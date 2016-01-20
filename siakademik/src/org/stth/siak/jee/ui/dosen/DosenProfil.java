package org.stth.siak.jee.ui.dosen;


import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
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
	private BeanItem<DosenKaryawan> item;

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
		pnl.addComponent(fly);
		return pnl;
	}
	

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
