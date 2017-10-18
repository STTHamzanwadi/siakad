package org.stth.siak.ui.util;

import java.util.List;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.ui.generalview.DaftarMahasiswaView;
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
	public static void showDaftarMahasiswa(List<Mahasiswa> lm ){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new DaftarMahasiswaView(lm);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static void showGenericWindow(Component c, String title){
		final Window win = new Window(title);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	public static Window showGenericWindowReturn(Component c, String title){
		final Window win = new Window(title);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
		return win;
	}
	
	public static <T extends Component & HasParent> void showWindowRefreshParentOnClose(T child, String title){
		final Window win = new Window(title);
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setMargin(true);
		vl.addComponent(child);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}

}
