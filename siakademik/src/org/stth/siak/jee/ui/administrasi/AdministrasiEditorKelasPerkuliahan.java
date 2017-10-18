package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.ui.util.StringPropertyGenerated;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiEditorKelasPerkuliahan extends Window{
	private static final long serialVersionUID = 1L;
	private List<PesertaKuliah> lPK;
	private KelasPerkuliahan kp;
	private FieldGroup fgKP;
	private BeanItem<KelasPerkuliahan> itemKP;
	private Panel panelPeserta = new Panel("Peserta Kuliah");
	private ComboBox cbSemester;
	private TextField tahunAjaran;
	private KonfigurasiPersistence k;
	private AutocompleteTextField tfMK;
	private Map<String, MataKuliah> mapmk;
	private ComboBox cbProdi;

	public AdministrasiEditorKelasPerkuliahan(KelasPerkuliahan kp) {
		setCaption("Edit Kelas Perkuliahan");
		k = new KonfigurasiPersistence();
		if(kp==null) {
			kp =new KelasPerkuliahan();
			kp.setSemester(k.getCurrentSemester());
			kp.setTahunAjaran(k.getCurrentTa());
		}

		this.kp=kp;
		itemKP=new BeanItem<KelasPerkuliahan>(this.kp);
		VerticalLayout vl = new VerticalLayout();
		vl.addComponent(buildContentIdentitas());
		panelPeserta.setContent(buildContentPesertaKuliah());
		vl.addComponent(panelPeserta);
		Panel root = new Panel();
		root.setContent(vl);
		setContent(root);
		setModal(true);
		center();
		setWidth("700px");
	}

	private Component buildContentIdentitas(){	
		fgKP = new FieldGroup(itemKP);
		Panel panelidentitas = new Panel("Identitas Kelas");
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();

		List<MataKuliah> lMatKul = GenericPersistence.findList(MataKuliah.class);
		mapmk = new HashMap<>();
		for (MataKuliah mk : lMatKul) {
			mapmk.put(mk.toString(), mk);
		}
		tfMK = new AutocompleteTextField("Mata Kuliah");
		tfMK.setInputPrompt("Inputkan kode atau nama matakuliah");
		tfMK.setWidth("250px");

		AutocompleteSuggestionProvider acspMK = new CollectionSuggestionProvider(mapmk.keySet(), MatchMode.CONTAINS, true);
		tfMK.setSuggestionProvider(acspMK);
		tfMK.setCache(true);
		tfMK.setMinChars(3);
		tfMK.setConverter(new Converter<String, MataKuliah>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MataKuliah convertToModel(String value, Class<? extends MataKuliah> targetType, Locale locale)
					throws ConversionException {
				if (!value.isEmpty()) {
					return mapmk.get(value);
				}
				return null;
			}

			@Override
			public String convertToPresentation(MataKuliah value, Class<? extends String> targetType, Locale locale)
					throws ConversionException {
				if (value!=null) {
					return value.toString();
				}
				return "";
			}

			@Override
			public Class<MataKuliah> getModelType() {
				return MataKuliah.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}
		});

		List<DosenKaryawan> lDosen= DosenKaryawanPersistence.getDosen(); 
		Map<String, DosenKaryawan> mapDosen= new HashMap<>();
		for (DosenKaryawan d : lDosen) {
			mapDosen.put(d.toString(), d);
		}
		AutocompleteSuggestionProvider acspDosen = new CollectionSuggestionProvider(mapDosen.keySet(),MatchMode.CONTAINS, true);
		AutocompleteTextField tfDosen = new AutocompleteTextField("Dosen");
		tfDosen.setInputPrompt("Inputkan nama dosen");
		tfDosen.setWidth("250px");

		tfDosen.setSuggestionProvider(acspDosen);
		tfDosen.setCache(true);
		tfDosen.setMinChars(3);
		tfDosen.setConverter(new Converter<String, DosenKaryawan>() {
			private static final long serialVersionUID = 1L;

			@Override
			public DosenKaryawan convertToModel(String value, Class<? extends DosenKaryawan> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (!value.isEmpty()) {
					return mapDosen.get(value);
				}
				return null;
			}

			@Override
			public String convertToPresentation(DosenKaryawan value, Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {
				if (value!=null) {
					return value.toString();
				}
				return "";
			}

			@Override
			public Class<DosenKaryawan> getModelType() {
				return DosenKaryawan.class;
			}

			@Override
			public Class<String> getPresentationType() {
				return String.class;
			}
		});

		TextField kodeKelas = new TextField("Kode Kelas");
		kodeKelas.setWidth("250px");

		cbSemester = new ComboBox("Semester", Arrays.asList(Semester.values()));
		cbSemester.setWidth("250px");

		tahunAjaran = new TextField("Tahun Ajaran");
		tahunAjaran.setWidth("250px");

		cbProdi = new ComboBox("Prodi", GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setWidth("250px");

		fgKP.bind(tfMK, "mataKuliah");
		fgKP.bind(tfDosen, "dosenPengampu");
		fgKP.bind(kodeKelas, "kodeKelas");
		fgKP.bind(cbSemester, "semester");
		fgKP.bind(tahunAjaran, "tahunAjaran");
		fgKP.bind(cbProdi, "prodi");

		Button simpanKelas = new Button("Simpan Kelas");
		simpanKelas.addClickListener(e->{
			simpanKelas();
		});

		flKiri.addComponents(tfMK, tfDosen, kodeKelas);
		flKanan.addComponents(cbSemester, tahunAjaran, cbProdi, simpanKelas);

		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(flKiri, flKanan);
		hl.setSpacing(true);
		hl.setMargin(true);

		panelidentitas.setContent(hl);
		return panelidentitas;
	}

	private void simpanKelas() {
		MataKuliah mk = (MataKuliah) tfMK.getConvertedValue();
		if (mk!=null) {
			try {
				fgKP.commit();
				kp = itemKP.getBean();
				if (kp.getId()>0) {
					GenericPersistence.merge(kp);
				}else{
					GenericPersistence.saveAndFlush(kp);;
				}

				Notification.show("Data Berhasil disimpan", Type.HUMANIZED_MESSAGE);
			} catch (CommitException e1) {
				e1.printStackTrace();
			}
		}else{
			Notification.show("Matakuliah Harus diisi", Type.ERROR_MESSAGE);
		}
		
	}

	private Component buildContentPesertaKuliah(){
		Map<Mahasiswa, PesertaKuliah> mapPK = new HashMap<>();
		BeanItemContainer<PesertaKuliah	> beans = new BeanItemContainer<>(PesertaKuliah.class);
		if (kp.getId()>0) {
			lPK = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
			if (lPK.size()>0) {
				for (PesertaKuliah pesertaKuliah : lPK) {
					mapPK.put(pesertaKuliah.getMahasiswa(), pesertaKuliah);
				}
				Collections.sort(lPK);
				beans.addAll(lPK);
			}
		}

		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(beans);
		//gpc.addGeneratedProperty("NO", new NoPropertyGenerated());
		gpc.addGeneratedProperty("NAMA", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = -1399849573795709540L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				Mahasiswa m = beans.getItem(itemId).getBean().getMahasiswa();
				if (m!=null) {
					return m.getNama();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				// TODO Auto-generated method stub
				return String.class;
			}
		});
		gpc.addGeneratedProperty("NIM", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = -6378746065624211332L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				Mahasiswa m = beans.getItem(itemId).getBean().getMahasiswa();
				if (m!=null) {
					return m.getNpm();
				}
				return "";
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		gpc.addGeneratedProperty("AKSI", new StringPropertyGenerated("Hapus Peserta"));
		Grid g = new Grid(gpc);
		g.removeAllColumns();
		g.setSizeFull();
		g.setEditorEnabled(true);
		g.setCaption(beans.size()+" mahasiswa");
		//g.addColumn("NO");
		g.addColumn("NIM");
		g.addColumn("NAMA");
		TextField tfNilai = new TextField("");
		TextField tfNilaiAngka = new TextField("");
		tfNilaiAngka.addValueChangeListener(e->{
			String nilai=tfNilaiAngka.getValue();
			String nilaiHuruf ="";
			if(nilai!=null){
				if(nilai!=null){
					if (!nilai.isEmpty()) {
						Double n = Double.parseDouble(nilai);
						if(n>=85){
							nilaiHuruf="A";
						}else if(n>=70){
							nilaiHuruf="B";
						}else if(n>=55){
							nilaiHuruf="C";
						}else if(n>=40){
							nilaiHuruf="D";
						}else{ 
							nilaiHuruf="E";
						}
					}
				}

			}
			tfNilai.setValue(nilaiHuruf);

		});

		g.addColumn("nilaiAngka").setHeaderCaption("NILAI ANGKA").setEditorField(tfNilaiAngka);
		g.addColumn("nilai").setHeaderCaption("NILAI").setEditorField(tfNilai).setEditable(false);
		g.addColumn("AKSI").setRenderer(new ButtonRenderer(e->{
			PesertaKuliah peserta = beans.getItem(e.getItemId()).getBean();
			ConfirmDialog.show(getUI(), "", "Hapus Peserta \n"+peserta.getMahasiswa()+" ?", "Ya", "Tidak", c -> {
				if (c.isConfirmed()) {
					GenericPersistence.delete(peserta);
					panelPeserta.setContent(buildContentPesertaKuliah());
				}
			});

		}));

		Mahasiswa example = new Mahasiswa();
		List<Mahasiswa> lm = MahasiswaPersistence.getListByExample(example);
		Map<String , Mahasiswa> mapMHS = new HashMap<>();
		for (Mahasiswa m : lm) {
			mapMHS.put(m.toString(), m);
		}

		AutocompleteTextField tfPeserta = new AutocompleteTextField();
		tfPeserta.setWidth("300px");
		AutocompleteSuggestionProvider acspMHS = new CollectionSuggestionProvider(mapMHS.keySet(), MatchMode.CONTAINS, true);
		tfPeserta.setInputPrompt("input nama atau nim mahasiswa");
		tfPeserta.setSuggestionProvider(acspMHS);
		tfPeserta.setMinChars(3);

		HorizontalLayout hlAddPeserta = new HorizontalLayout();
		Button addPeserta = new Button("");
		addPeserta.setIcon(FontAwesome.PLUS);
		hlAddPeserta.setSpacing(true);
		hlAddPeserta.addComponents(tfPeserta,addPeserta);
		addPeserta.addClickListener(e->{
			if (kp.getId()>0) {
				simpanPeserta(mapPK, mapMHS, tfPeserta);
			}else{
				Notification.show("Simpan kelas terlebih dahulu", Type.ERROR_MESSAGE);
			}
			
		});
		Button addPesertaKRS = new Button("Tambah peserta kolektif");
		addPesertaKRS.addClickListener(e->{

			if (kp.getId()>0) {
				if (kp.getMataKuliah()!=null) {
					DaftarPesertaPerMatkulKRS pmKRS = new DaftarPesertaPerMatkulKRS(kp);
					Window w =GeneralPopups.showGenericWindowReturn(pmKRS, "Daftar pengambilan matakuliah "
							+" "+k.getKRSTa()+"/"+k.getKRSSemester() +" yang disetujui");
					w.addCloseListener(c->{
						panelPeserta.setContent(buildContentPesertaKuliah());
					});

				}else{
					Notification.show("Matakuliah wajib diisi", Type.ERROR_MESSAGE);
				}

			}else Notification.show("Simpan kelas terlebih dahulu", Type.ERROR_MESSAGE);

		});
		hlAddPeserta.addComponent(addPesertaKRS);

		HorizontalLayout hlButton = new HorizontalLayout();
		Button simpanPeserta = new Button("Simpan Nilai");
		simpanPeserta.addClickListener(e->{
			Collection<?> col = g.getContainerDataSource().getItemIds();
			for(Object o  : col){
				PesertaKuliah peserta = (PesertaKuliah) o;
				GenericPersistence.merge(peserta);
				Notification.show("Nilai Berhasil Disimpan", Notification.Type.HUMANIZED_MESSAGE);
				System.out.println(peserta.getMahasiswa().getNama()+" "+peserta.getNilai());
			}
		});

		hlButton.addComponent(simpanPeserta);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.addComponents(hlAddPeserta, g, hlButton);

		return vl;

	}

	private void simpanPeserta(Map<Mahasiswa, PesertaKuliah> mapPK, Map<String, Mahasiswa> mapMHS,
			AutocompleteTextField tfPeserta) {
		if (!tfPeserta.getValue().isEmpty()) {
			Mahasiswa mahasiswaTerpilih= mapMHS.get(tfPeserta.getValue());
			PesertaKuliah pkTerpilih = mapPK.get(mahasiswaTerpilih);
			if(pkTerpilih!=null){
				Notification.show("Mahasiswa ini sudah ada", Notification.Type.HUMANIZED_MESSAGE);
			}else{
				PesertaKuliah pk = new PesertaKuliah(mahasiswaTerpilih, kp);
				GenericPersistence.merge(pk);
				panelPeserta.setContent(buildContentPesertaKuliah());
			}

		}else{
			Notification.show("Inputkan nim atau nama mahasiswa pada field yang tersedia", Notification.Type.HUMANIZED_MESSAGE);
		}
	}

}
