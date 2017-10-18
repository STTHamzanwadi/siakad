package org.stth.siak.jee.ui.administrasi;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KHSPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.KelasPerkuliahanMahasiswaPerSemester2;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.ui.util.NoPropertyGenerated;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import net.sf.jasperreports.engine.JRException;

public class AdministrasiKartudanHasilStudi extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	private List<KelasPerkuliahanMahasiswaPerSemester2> lKPMP ;
	private List<KelasPerkuliahanMahasiswaPerSemester2> lKPMPSelected ;
	private ComboBox cbSemester;
	private TextField tfTA;
	private TextField tfAngkatan;
	private ComboBox cbProdi;
	private TextField tfNama;
	private Panel p = new Panel("Daftar Mahasiswa");
	private KonfigurasiPersistence k;

	public AdministrasiKartudanHasilStudi() {
		setMargin(true);
		setSpacing(true);
		k = new KonfigurasiPersistence();
		addComponent(ViewFactory.header("Administrasi Daftar Mahasiswa Aktif"));
		addComponent(buildFilter());
		addComponent(p);
	}
	private Component buildFilter(){
		Panel p = new Panel("Filter");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		FormLayout flLeft = new FormLayout();
		FormLayout flMiddle = new FormLayout();
		FormLayout flRight = new FormLayout();
		tfNama = new TextField("Nama");
		cbSemester = new ComboBox("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(k.getCurrentSemester());
		tfTA= new TextField("Tahun Ajaran");
		tfTA.setValue(k.getCurrentTa());
		cbProdi = new ComboBox("Program Studi", GenericPersistence.findList(ProgramStudi.class));
		tfAngkatan = new TextField("Angkatan");
		Button cari = new Button("Cari");
		flLeft.addComponents(tfNama, cbProdi);
		flMiddle.addComponents(cbSemester, tfTA);
		flRight.addComponents(tfAngkatan, cari);
		hl.addComponents(flLeft, flMiddle, flRight);
		p.setContent(hl);
		cari.addClickListener(klik->{
			buildComponent();
		});

		return p;
	}
	private void buildComponent(){
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		String nama="";
		String prodi="";
		String ta="";
		int angkatan=0;
		if(!cbProdi.isEmpty()){
			prodi=((ProgramStudi)cbProdi.getValue()).getNama();
		}

		if(!tfNama.isEmpty()){
			nama=tfNama.getValue();
		}
		if(!tfAngkatan.isEmpty()){
			angkatan=Integer.parseInt(tfAngkatan.getValue());
		}
		if(!tfTA.getValue().isEmpty()){
			ta=tfTA.getValue();
		}

		lKPMP = KHSPersistence.khs(nama, (Semester)cbSemester.getValue(), prodi, ta,angkatan);
		BeanContainer<Integer, KelasPerkuliahanMahasiswaPerSemester2> beans = new BeanContainer<>(KelasPerkuliahanMahasiswaPerSemester2.class);

		beans.setBeanIdProperty("rownum");
		if(lKPMP.size()>0){
			beans.addAll(lKPMP);
		}
		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(beans);
		System.out.println(lKPMP.size());
		Grid g = new Grid();

		gpc.addGeneratedProperty("NIM", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				BeanItem<?> i = (BeanItem<?>) item;
				KelasPerkuliahanMahasiswaPerSemester2 kpmp = (KelasPerkuliahanMahasiswaPerSemester2) i.getBean();
				return kpmp.getMahasiswa().getNpm();
			}

			@Override
			public Class<String> getType() {
				// TODO Auto-generated method stub
				return String.class;
			}

		});
		gpc.addGeneratedProperty("NAMA", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				BeanItem<?> i = (BeanItem<?>) item;
				KelasPerkuliahanMahasiswaPerSemester2 kpmp = (KelasPerkuliahanMahasiswaPerSemester2) i.getBean();
				return kpmp.getMahasiswa().getNama();
			}

			@Override
			public Class<String> getType() {
				// TODO Auto-generated method stub
				return String.class;
			}
		});
		gpc.addGeneratedProperty("Kelas", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				BeanItem<?> i = (BeanItem<?>) item;
				KelasPerkuliahanMahasiswaPerSemester2 kpmp = (KelasPerkuliahanMahasiswaPerSemester2) i.getBean();
				return String.valueOf(kpmp.getJumlahMataKuliah());
			}

			@Override
			public Class<String> getType() {
				// TODO Auto-generated method stub
				return String.class;
			}

		});

		//gpc.addGeneratedProperty("NO", new NoPropertyGenerated()); 
		g.setContainerDataSource(gpc);
		g.setSizeFull();
		g.removeAllColumns();
		g.setWidth("800px");
		g.setCaption("Ditemukan "+beans.size()+" mahasiswa");
		//g.addColumn("NO");
		g.addColumn("semester").setHeaderCaption("SEMESTER");
		g.addColumn("tahunAjaran");
		g.addColumn("NIM");
		g.addColumn("NAMA");
		g.addColumn("Kelas");

		g.setSelectionMode(SelectionMode.MULTI);

		HorizontalLayout hlButton = new HorizontalLayout();
		Button cetakKHS = new Button("Cetak KHS");
		cetakKHS.addClickListener(khs->{
			lKPMPSelected = new ArrayList<>();
			for(Object o : g.getSelectedRows()){
				KelasPerkuliahanMahasiswaPerSemester2 kpm = beans.getItem(o).getBean();
				lKPMPSelected.add(kpm);
				System.out.println(kpm.getMahasiswa().getNama());
			}
			StreamResource resource;
			try {
				resource = ReportResourceGenerator.cetakKHS(lKPMPSelected);
				getUI().getPage().open(resource, "_blank", false);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
		Button cetakkartuUTS = new Button("Cetak Kartu UTS");
		cetakkartuUTS.addClickListener(uts->{
			cetakKartu(beans, g, JenisUjian.UTS);
		});
		Button cetakKartuUAS = new Button("Cetak Kartu UAS");
		cetakKartuUAS.addClickListener(uas->{
			cetakKartu(beans, g, JenisUjian.UAS);
		});
		hlButton.setSpacing(true);
		hlButton.addComponents(cetakkartuUTS, cetakKartuUAS, cetakKHS);
		vl.addComponents(g, hlButton);
		p.setContent(vl);

	}
	
	private void cetakKartu(BeanContainer<Integer, KelasPerkuliahanMahasiswaPerSemester2> beans, Grid g, JenisUjian jenis) {
		lKPMPSelected = new ArrayList<>();
		for(Object o : g.getSelectedRows()){
			KelasPerkuliahanMahasiswaPerSemester2 kpm = beans.getItem(o).getBean();
			lKPMPSelected.add(kpm);
			System.out.println(kpm.getMahasiswa().getNama());
		}
			StreamResource source;
			try {
				source = ReportResourceGenerator.cetakKartuUjian(lKPMPSelected, jenis);
				getUI().getPage().open(source, "_blank", false);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Override
	public void enter(ViewChangeEvent event) {


	}

}
