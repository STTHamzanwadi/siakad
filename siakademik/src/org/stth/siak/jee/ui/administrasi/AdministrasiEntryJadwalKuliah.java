package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.JadwalKuliahPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Hari;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.enumtype.JenisKelamin;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

public class AdministrasiEntryJadwalKuliah extends CustomComponent {
	private Semester semester;
	private String ta;
	private VerticalLayout root = new VerticalLayout();
	private ComboBox cbDosen = new ComboBox("Dosen Pengampu");
	private ComboBox cbMataKuliah = new ComboBox("Mata Kuliah");
	private ComboBox cbDosenPengganti = new ComboBox("Dosen Pengganti");
	private BeanItemContainer<DosenKaryawan> beanDosen = new BeanItemContainer<>(DosenKaryawan.class);
	private BeanItemContainer<KelasPerkuliahan> beanKelasPerkuliahan = new BeanItemContainer<>(KelasPerkuliahan.class);
	private DosenKaryawan dosen;
	private KelasPerkuliahan kelasPerkuliahan;
	private BeanItem<JadwalKuliah> item ;
	private FieldGroup fieldGroup;
	private TextField textProdi= new TextField("Program Studi");
	private TextField kodeKelas= new TextField("Kelas");
	private Window parent;
	private DosenKaryawan user;
	private TextField tfMulai;
	private TextField tfBerakhir;
	
	
	public AdministrasiEntryJadwalKuliah() {
		JadwalKuliah log = new JadwalKuliah();
		prepare(log);
		
	}
	public AdministrasiEntryJadwalKuliah( JadwalKuliah jk) {
		prepare(jk);
	}
	public void setParent(final Window parent){
		this.parent = parent;
	}
	
	private void prepare(JadwalKuliah log){
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		item = new BeanItem<JadwalKuliah>(log);
		cbDosenPengganti.setEnabled(false);
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		//cbDosenPengganti.set
		siapkanDaftarDosen();
		siapkanFormIsian();
		siapkanTombolAksi();
	}
	
	private void siapkanTombolAksi(){
		Button ok,batal;
		ok = new Button("OK");
		ok.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.commit();
					BeanItem<?> bi = (BeanItem<?>) fieldGroup.getItemDataSource();
					JadwalKuliah jadwal = (JadwalKuliah) bi.getBean();
					saveLog(jadwal);
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Notification.show("Failed in saving records "+ e.getMessage());
				}
			
				
			}
		});
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(ok);
		root.addComponent(hl);		
	}
	
	private void siapkanDaftarDosen(){
		beanDosen.addAll(DosenKaryawanPersistence.getDosen());
		cbDosen.setContainerDataSource(beanDosen);
		cbDosenPengganti.setContainerDataSource(beanDosen);
		cbDosen.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				dosen = (DosenKaryawan) event.getProperty().getValue();
				cbDosenPengganti.select(dosen);
				siapkanDaftarKelas();
				
			}
		});
	}
	
	private void siapkanDaftarKelas() {
		beanKelasPerkuliahan.removeAllItems();
		List<KelasPerkuliahan> lKelas = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		beanKelasPerkuliahan.addAll(lKelas);
		cbMataKuliah.setContainerDataSource(beanKelasPerkuliahan);		
		cbMataKuliah.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				kelasPerkuliahan = (KelasPerkuliahan) event.getProperty().getValue();
				textProdi.setValue(kelasPerkuliahan.getProdi().toString());
				textProdi.setEnabled(false);
				kodeKelas.setValue(kelasPerkuliahan.getKodeKelas());
				kodeKelas.setEnabled(false);
			}
		});
	}
	
	private void siapkanFormIsian(){
		FormLayout fl = new FormLayout();
		fieldGroup = new FieldGroup(item);
		String dw = "300px";
		
		cbDosen.setWidth(dw);
		cbMataKuliah.setWidth(dw);
		textProdi.setWidth(dw);
		kodeKelas.setWidth(dw);
		
		fieldGroup.bind(cbMataKuliah, "kelasPerkuliahan");
		fl.addComponent(cbDosen);
		fl.addComponent(cbMataKuliah);
		fl.addComponent(textProdi);
		fl.addComponent(kodeKelas);
		
		Container container = GeneralUtilities.createContainerFromEnumClass(Hari.class);
		ComboBox cbHari = new ComboBox("Hari", container);
		cbHari.addValidator(new NullValidator("Hari tidak boleh kosong", false));
		fl.addComponent(cbHari);
		fieldGroup.bind(cbHari, "hari");
		
		Property<Integer> integerProperty = (Property<Integer>) item
		        .getItemProperty("waktuMulai");
		tfMulai = new TextField("Mulai", integerProperty);
		tfMulai.addValidator(new NullValidator("Waktu mulai tidak boleh kosong", false));
		fl.addComponent(tfMulai);
		fieldGroup.bind(tfMulai, "waktuMulai");
		
		Property<Integer> integerProperty2 = (Property<Integer>) item
		        .getItemProperty("waktuBerakhir");
		tfBerakhir = new TextField("Berakhir", integerProperty);
		tfBerakhir.addValidator(new NullValidator("Waktu berakhir tidak boleh kosong", false));
		fl.addComponent(tfBerakhir);
		fieldGroup.bind(tfBerakhir, "waktuBerakhir");
		
		
		
		TextField lokasi = new TextField("Ruangan");
		lokasi.addValidator(new NullValidator("Ruangan tidak boleh kosong", false));
		lokasi.setImmediate(true);
		fieldGroup.bind(lokasi, "ruangan");
		lokasi.setWidth(dw);
		lokasi.setNullRepresentation("Isi dengan ruangan/kelas");
		fl.addComponent(lokasi);
		
		
		root.setMargin(true);
		root.setSizeUndefined();
		root.addComponent(fl);
		setCompositionRoot(root);
		
	}
	protected void siapkanPilihanDosenPengganti() {
		cbDosenPengganti.setEnabled(true);
		
		
	}
	private void saveLog(JadwalKuliah jadwalKuliah) {
		List<JadwalKuliah> existedLogs = JadwalKuliahPersistence.getLogSimilarOnDate(jadwalKuliah);
		if (existedLogs.size()==0){
			jadwalKuliah.setEntryOleh(user);
			GenericPersistence.merge(jadwalKuliah);
			parent.close();
		} else {
			Notification.show("Data perkuliahan di hari yang sama untuk kelas bersangkutan sudah ada", Notification.Type.ERROR_MESSAGE);
		}
		
	}

}
