package org.stth.siak.jee.ui.mahasiswa;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.jee.genericview.ViewFactory;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class KelasPerkuliahanView extends VerticalLayout implements View {


    private VerticalLayout dashboardPanels;
	private Mahasiswa mhs;
	private BeanContainer<Integer, PesertaKuliah> beansPK = new BeanContainer<Integer, PesertaKuliah>(PesertaKuliah.class);

    public KelasPerkuliahanView() {
    	mhs = (Mahasiswa) VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
    	setMargin(true);
        Responsive.makeResponsive(this);
        addComponent(ViewFactory.header("Kelas Perkuliahan"));
        Component content = buildContent2();
        addComponent(content);
        addComponent(ViewFactory.footer());

    }
    private Component buildContent2() {
        dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        String textNoAccessRights= "<center><h3>Data untuk semester ini belum tersedia. "
				+ "Data kelas tersedia setelah registrasi ulang ditutup dan pembagian tugas dosen selesai .<br>"
				+ "Terima Kasih</h3></center>";
		Label noKelasPerkuliahanYet = new Label(textNoAccessRights, ContentMode.HTML);
		noKelasPerkuliahanYet.setStyleName(ValoTheme.LABEL_FAILURE);
		dashboardPanels.addComponent(noKelasPerkuliahanYet);
        return dashboardPanels;
    }

    
        @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
