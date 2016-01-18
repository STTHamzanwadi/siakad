package org.stth.siak.jee.ui.mahasiswa;


import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.genericview.MahasiswaProfilView;
import org.stth.siak.jee.genericview.ViewFactory;

import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MahasiswaProfil extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2694783715823501660L;
	TextField txtNama,txtNIDN,txtNIS,txtTanggalLahir,txtEmail,txtAlamat,txtKtp;
	TextField txtJenjangPendidikanTerakhir,txtProdiPendidikanTerakhir,txtInstitusiPendidikanTerakhir;
	private BeanItem<Mahasiswa> item;

	public MahasiswaProfil() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		Mahasiswa m = (Mahasiswa) VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		item = new BeanItem<Mahasiswa>(m);
		addComponent(ViewFactory.header("Profil Mahasiswa"));
		addComponent(new MahasiswaProfilView(m));
		addComponent(ViewFactory.footer());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
