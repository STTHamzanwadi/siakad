package org.stth.siak.ui.util;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.ui.generalview.MahasiswaProfilView;
import org.stth.siak.jee.ui.mahasiswa.IPKView;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class GeneralPopups {
	public static void showProfilMahasiswa(Mahasiswa m){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new MahasiswaProfilView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static void showIpkMahasiswa(Mahasiswa m){
		final Window win = new Window("Transkrip Nilai Mahasiswa");
		Component c = new IPKView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}


}
