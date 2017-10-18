package org.stth.siak.jee.ui.mahasiswa;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import net.sf.jasperreports.engine.JRException;

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
	public IPKView(Mahasiswa mhs, String a) {
		tableIPK = new Table(a);
		this.mhs = mhs;
    	prepareViewKHS();
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
	private void prepareViewKHS(){
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		Semester sms;
		String ta;
		if (k.getCurrentSemester()==Semester.GANJIL) {
			sms=Semester.GENAP;
			int a = Integer.valueOf(k.getCurrentTa().substring(0, 3))-1 ;
			int b = Integer.valueOf(k.getCurrentTa().substring(4))-1 ;
			ta=String.valueOf(a)+"-"+String.valueOf(b);
		}else{
			ta=k.getCurrentTa();
			sms=Semester.GANJIL;
		}
		
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("mahasiswa", mhs));
		lc.add(Restrictions.eq("kelasPerkuliahan.semester", sms));
		lc.add(Restrictions.eq("kelasPerkuliahan.tahunAjaran", ta));
		String[] alias = {"kelasPerkuliahan"};
		List<PesertaKuliah> lpk = GenericPersistence.findList(PesertaKuliah.class,lc,alias);
		iph = new IndeksPrestasiHelper(lpk, mhs);
		setMargin(true);
        Responsive.makeResponsive(this);
        addComponent(ViewFactory.header("IP " +sms.toString()+" T.A "+ta));
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
		Button btTranskrip = new Button("Cetak Transkrip");
		btTranskrip.addClickListener(e->{
			StreamResource resource;
			try {
				resource = ReportResourceGenerator.cetakTranskripMahasiswa(mhs);
				getUI().getPage().open(resource, "_blank", false);
			} catch (JRException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		});
		tableIPK.setContainerDataSource(beansPK);
		tableIPK.setColumnHeader("copiedKodeMatkul", "KODE");
		tableIPK.setColumnHeader("copiedNamaMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("copiedSKSMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("nilai", "NILAI");
		tableIPK.setVisibleColumns("copiedKodeMatkul","copiedNamaMatkul","copiedSKSMatkul","nilai");
		dashboardPanels.addComponent(btTranskrip);
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
