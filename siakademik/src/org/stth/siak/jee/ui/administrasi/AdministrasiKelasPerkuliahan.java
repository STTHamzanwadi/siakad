package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.ui.util.NoPropertyGenerated;
import org.stth.siak.ui.util.StringPropertyGenerated;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
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
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import net.sf.jasperreports.engine.JRException;

public class AdministrasiKelasPerkuliahan extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	private Panel content = new Panel("Daftar Kelas Perkuliahan");
	private List<KelasPerkuliahan> lKelasPerkuliahan;
	private KelasPerkuliahan kelasExample;
	private Button tambah;
	private List<KelasPerkuliahan> l;
	private Mahasiswa m;
	private KonfigurasiPersistence k;
	
	public AdministrasiKelasPerkuliahan() {
		k=new KonfigurasiPersistence();
		setMargin(true);
		setSpacing(true);
		addComponent(ViewFactory.header("Administrasi Kelas Perkuliahan"));
		tambah = new Button("Tambah");
		tambah.addClickListener(klik->{
			Window w = new AdministrasiEditorKelasPerkuliahan(null);
			getUI().addWindow(w);
		});
		addComponent(tambah);
		addComponent(buildFilter());
		addComponent(content);
	}
	
	private Component buildFilter(){
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		FormLayout flLeft = new FormLayout();
		FormLayout flMiddle = new FormLayout();
		FormLayout flRight = new FormLayout();
		TextField ta = new TextField("T.A");
		ta.setValue(k.getCurrentTa());
		
		AutocompleteTextField	mk = new AutocompleteTextField("Mata Kuliah");
		mk.setWidth("250px");
		mk.setInputPrompt("Inputkan kode atau nama matakuliah");
		List<MataKuliah> lkp = GenericPersistence.findList(MataKuliah.class);
		Map<String, MataKuliah> mapmk = new HashMap<>();
		for (MataKuliah mk2 : lkp) {
			mapmk.put(mk2.toString(), mk2);
		}
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		mk.setSuggestionProvider(acsp);
		mk.setCache(true);
		mk.setMinChars(3);
		ComboBox semester = new ComboBox("Semester", Arrays.asList(Semester.values()));
		semester.setValue(k.getCurrentSemester());
		
		BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		ComboBox prodi = new ComboBox("Prodi", beanProdi);
		
		BeanItemContainer<DosenKaryawan> beansDosen = new BeanItemContainer<>(DosenKaryawan.class);
		beansDosen.addAll(DosenKaryawanPersistence.getDosen());
		ComboBox dosen = new ComboBox("Dosen", beansDosen);
		
		AutocompleteTextField mhs = new AutocompleteTextField("Mahasiswa");
		mhs.setWidth("250px");
		mhs.setInputPrompt("Inputkan nim atau nama mahasiswa");
		List<Mahasiswa> lm= MahasiswaPersistence.getListByExample(new Mahasiswa());
		System.out.println(lm.size());
		Map<String, Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa m : lm) {
			mapMHS.put(m.toString(), m);
		}
		AutocompleteSuggestionProvider acspm = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		mhs.setSuggestionProvider(acspm);
		mhs.setCache(true);
		mhs.setMinChars(3);
		
		Button cari = new Button("Cari");
		cari.addClickListener(klik->{
			m=mapMHS.get(mhs.getValue());
			MataKuliah matKul = mapmk.get(mk.getValue());
			kelasExample = new KelasPerkuliahan();
			kelasExample.setTahunAjaran(ta.getValue());
			kelasExample.setProdi((ProgramStudi) prodi.getValue());
			kelasExample.setSemester((Semester) semester.getValue());
			kelasExample.setDosenPengampu((DosenKaryawan) dosen.getValue());
			kelasExample.setMataKuliah(matKul);
			if (m!=null) {
				lKelasPerkuliahan=new ArrayList<>();
				PesertaKuliah peserta= new PesertaKuliah();
				peserta.setMahasiswa(m);
				peserta.setKelasPerkuliahan(kelasExample);
				peserta.setCopiedKodeMatkul("");
				if (matKul!=null) {
					peserta.setCopiedKodeMatkul(matKul.getKode());
				}

				List<PesertaKuliah> l =PesertaKuliahPersistence.getPesertaKuliahByExample(peserta);
				for (PesertaKuliah pk : l) {
					lKelasPerkuliahan.add(pk.getKelasPerkuliahan());
				}
			}else{
				
				lKelasPerkuliahan=KelasPerkuliahanPersistence.getKelasPerkuliahanByExample(kelasExample);
			}
			buildContent();
			m=null;
		});
		flLeft.addComponents(dosen, prodi);
		flMiddle.addComponents(semester, ta);
		flRight.addComponents(mk, mhs);
		FormLayout flB = new FormLayout();
		flB.addComponent(cari);
		hl.addComponents(flLeft, flMiddle, flRight, flB);
		Panel panelFilter = new Panel("Filter");
		panelFilter.setContent(hl);
		return panelFilter;
	}
	
	private void buildContent(){
		VerticalLayout vl = new VerticalLayout();
		
		BeanItemContainer<KelasPerkuliahan> beans = new BeanItemContainer<>(KelasPerkuliahan.class);
		if(lKelasPerkuliahan.size()>0){
			beans.addAll(lKelasPerkuliahan);
		}
		System.out.println(beans.size());
		
		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(beans);
		gpc.addGeneratedProperty("NO", new NoPropertyGenerated(beans));
		gpc.addGeneratedProperty("AKSI", new StringPropertyGenerated("EDIT"));
		
		Grid g = new  Grid("Ditemukan "+ lKelasPerkuliahan.size()+" kelas",gpc);
		g.removeAllColumns();
		g.setSelectionMode(SelectionMode.MULTI);
		g.setSizeFull();
		g.addColumn("NO");
		g.addColumn("dosenPengampu");
		g.addColumn("kodeKelas");
		g.addColumn("mataKuliah");
		g.addColumn("semester");
		g.addColumn("prodi");
		g.addColumn("tahunAjaran");
		g.addColumn("AKSI").setRenderer(new ButtonRenderer(e->{
			KelasPerkuliahan kp  = beans.getItem(e.getItemId()).getBean();
			Window w = new AdministrasiEditorKelasPerkuliahan(kp);
			getUI().addWindow(w);
		}));
		
		HorizontalLayout hlButton = new HorizontalLayout();
		Button cetakSampulUAS = new Button("Cetak Sampul Soal UAS");
		cetakSampulUAS.addClickListener(klik->{
			cetakSampulSoal(beans, g, JenisUjian.UAS);
		});
		Button cetakSampulUTS = new Button("Cetak Sampul Soal UTS");
		cetakSampulUTS.addClickListener(klik->{
			cetakSampulSoal(beans, g, JenisUjian.UTS);
		});
		Button cetakAbsenUTS = new Button("Cetak Absen UTS");
		cetakAbsenUTS.addClickListener(klik->{
			cetakAbsenUjian(beans, g, JenisUjian.UTS);
		});
		
		Button cetakAbsenUAS = new Button("Cetak Absen UAS");
		cetakAbsenUAS.addClickListener(klik->{
			 cetakAbsenUjian(beans, g, JenisUjian.UAS);
		});
		hlButton.setSpacing(true);
		hlButton.addComponents(cetakSampulUTS ,cetakSampulUAS, cetakAbsenUTS, cetakAbsenUAS);
		
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.addComponents(g, hlButton);
		content.setContent(vl);
	}

	private void cetakAbsenUjian(BeanItemContainer<KelasPerkuliahan> beans,Grid g, JenisUjian jenis) {
		l=new ArrayList<>();
		Collection<?> col = g.getSelectedRows();
		for(Object o : col){
			KelasPerkuliahan kp = beans.getItem(o).getBean();
			l.add(kp);
		}
		try {
			StreamResource source = ReportResourceGenerator.cetakAbsensiUjian(l, jenis);
			getUI().getPage().open(source, "_blank", false);
		} catch (JRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void cetakSampulSoal(BeanItemContainer<KelasPerkuliahan> beans, Grid g, JenisUjian jenis) {
		l=new ArrayList<>();
		Collection<?> col = g.getSelectedRows();
		for (Object o : col) {
			KelasPerkuliahan kp = beans.getItem(o).getBean();
			l.add(kp);
		}
		StreamResource resource;
		try {
			resource = ReportResourceGenerator.cetakSampulUjian(l, jenis);
			getUI().getPage().open(resource, "_blank", false);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
