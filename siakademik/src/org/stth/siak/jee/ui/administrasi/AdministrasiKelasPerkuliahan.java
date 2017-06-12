package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.NoPropertyGenerated;
import org.stth.siak.ui.util.StringPropertyGenerated;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
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

public class AdministrasiKelasPerkuliahan extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	private Panel content = new Panel("Daftar Kelas Perkuliahan");
	private List<KelasPerkuliahan> lKelasPerkuliahan;
	private KelasPerkuliahan kelasExample;
	private Button tambah;
	
	public AdministrasiKelasPerkuliahan() {
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
		AutocompleteTextField	mk = new AutocompleteTextField("Mata Kuliah");
		mk.setWidth("300px");
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
		BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		ComboBox prodi = new ComboBox("Prodi", beanProdi);
		
		BeanItemContainer<DosenKaryawan> beansDosen = new BeanItemContainer<>(DosenKaryawan.class);
		beansDosen.addAll(DosenKaryawanPersistence.getDosen());
		ComboBox dosen = new ComboBox("Dosen", beansDosen);
		
		Button cari = new Button("Cari");
		cari.addClickListener(klik->{
			kelasExample = new KelasPerkuliahan();
			kelasExample.setTahunAjaran(ta.getValue());
			kelasExample.setProdi((ProgramStudi) prodi.getValue());
			kelasExample.setSemester((Semester) semester.getValue());
			kelasExample.setDosenPengampu((DosenKaryawan) dosen.getValue());
			kelasExample.setMataKuliah(mapmk.get(mk.getValue()));
			content.setContent(buildContent());
		});
		flLeft.addComponents(dosen, prodi);
		flMiddle.addComponents(semester, ta);
		flRight.addComponents(mk, cari);
		hl.addComponents(flLeft, flMiddle, flRight);
		Panel panelFilter = new Panel("Filter");
		panelFilter.setContent(hl);
		return panelFilter;
	}
	
	private Component buildContent(){	
		lKelasPerkuliahan=KelasPerkuliahanPersistence.getKelasPerkuliahanByExample(kelasExample);
		BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<>(KelasPerkuliahan.class);
		beans.setBeanIdProperty("id");
		if(lKelasPerkuliahan.size()>0){
			beans.addAll(lKelasPerkuliahan);
		}
		System.out.println(beans.size());
		
		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(beans);
		gpc.addGeneratedProperty("NO", new NoPropertyGenerated());
		gpc.addGeneratedProperty("AKSI", new StringPropertyGenerated("EDIT"));
		
		Grid g = new  Grid(gpc);
		g.removeAllColumns();
		g.setSelectionMode(SelectionMode.MULTI);
		g.setCaption("Ditemukan "+ lKelasPerkuliahan.size()+" kelas");
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
		return g;
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
