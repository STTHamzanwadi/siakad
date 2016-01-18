package org.stth.siak.jee.ui.mahasiswa;

import java.util.List;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.jee.genericview.ViewFactory;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class IPKView extends VerticalLayout implements View {
    private Table tableIPK = new Table("DAFTAR MATA KULIAH YANG SUDAH DITEMPUH");
	private Mahasiswa mhs;
	private IndeksPrestasiHelper iph;
	private BeanContainer<Integer, PesertaKuliah> beansPK = new BeanContainer<Integer, PesertaKuliah>(PesertaKuliah.class);
	private VerticalLayout dashboardPanels;

    
	public IPKView(Mahasiswa mhs) {
		this.mhs = mhs;
    	prepareView();
    }
	public IPKView(){
    	mhs = (Mahasiswa) VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
    	prepareView();
    }

	private void prepareView() {
		iph = new IndeksPrestasiHelper(mhs);
        setMargin(true);
        Responsive.makeResponsive(this);
        addComponent(ViewFactory.header("Indeks Prestasi Mahasiswa"));
        Component content = buildContent2();
        addComponent(content);
        addComponent(ViewFactory.footer());
	}
	
    private Component buildContent2() {
        dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        List<PesertaKuliah> ls = iph.getNilaiClean();
        beansPK.setBeanIdProperty("id");
		beansPK.removeAllItems();
		beansPK.addAll(ls);
		tableIPK.setContainerDataSource(beansPK);
		tableIPK.setColumnHeader("copiedKodeMatkul", "KODE");
		tableIPK.setColumnHeader("copiedNamaMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("copiedSKSMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("nilai", "NILAI");
		tableIPK.setVisibleColumns("copiedKodeMatkul","copiedNamaMatkul","copiedSKSMatkul","nilai");
		dashboardPanels.addComponent(tableIPK);
		Label ipk = new Label("Indeks Prestasi Kumulatif : "+iph.getIpk());
		Label totSks = new Label("Jumlah SKS yang sudah ditempuh : "+iph.getSKStotal());
		Label nilaiD = new Label("Jumlah SKS nilai D : "+ iph.getSKSD());
		dashboardPanels.addComponents(ipk,totSks,nilaiD);
        return dashboardPanels;
    }

    
    
        @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
