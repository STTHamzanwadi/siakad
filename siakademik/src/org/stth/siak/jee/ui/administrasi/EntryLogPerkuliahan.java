package org.stth.siak.jee.ui.administrasi;

import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.enumtype.Semester;

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

public class EntryLogPerkuliahan extends CustomComponent {
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
	private BeanItem<LogPerkuliahan> item ;
	private FieldGroup fieldGroup;
	private TextField textProdi= new TextField("Program Studi");
	private TextField kodeKelas= new TextField("Kelas");
	private Window parent;
	private DosenKaryawan user;
	
	public EntryLogPerkuliahan() {
		LogPerkuliahan log = new LogPerkuliahan();
		prepare(log);
		
	}
	public EntryLogPerkuliahan( LogPerkuliahan log) {
		prepare(log);
	}
	public void setParent(final Window parent){
		this.parent = parent;
	}
	
	private void prepare(LogPerkuliahan log){
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		item = new BeanItem<LogPerkuliahan>(log);
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
					LogPerkuliahan log = (LogPerkuliahan) bi.getBean();
					saveLog(log);
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
		
		DateField waktu = new DateField("Waktu Pertemuan");
		waktu.setResolution(Resolution.MINUTE);
		waktu.addValidator(new NullValidator("Waktu pertemuan tidak boleh kosong", false));
		waktu.setImmediate(true);
		fieldGroup.bind(waktu, "tanggalPertemuan");
		fl.addComponent(waktu);
		
		TextField lokasi = new TextField("Ruangan");
		lokasi.addValidator(new NullValidator("Ruangan tidak boleh kosong", false));
		lokasi.setImmediate(true);
		fieldGroup.bind(lokasi, "ruangPertemuan");
		lokasi.setWidth(dw);
		lokasi.setNullRepresentation("Isi dengan ruangan/kelas");
		fl.addComponent(lokasi);
		
		CheckBox isOlehDosen = new CheckBox("Diisi oleh dosen bersangkutan");
		isOlehDosen.setValue(true);
		isOlehDosen.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean b = (boolean) event.getProperty().getValue();
				if (!b){
					siapkanPilihanDosenPengganti();
				}
				
			}
		});
		fieldGroup.bind(isOlehDosen, "olehDosen");
		fl.addComponent(isOlehDosen);
		
		cbDosenPengganti.setEnabled(false);
		cbDosenPengganti.setWidth(dw);
		fl.addComponent(cbDosenPengganti);
		fieldGroup.bind(cbDosenPengganti, "diisiOleh");
		
		TextArea textMateri = new TextArea("Materi");
		textMateri.addValidator(new NullValidator("Materi harus diisi",false));
		textMateri.setImmediate(true);
		fl.addComponent(textMateri);
		textMateri.setWidth(dw);
		textMateri.setNullRepresentation("Isi dengan materi kuliah");
		fieldGroup.bind(textMateri,"materiPertemuan");
		
		
		root.setMargin(true);
		root.setSizeUndefined();
		root.addComponent(fl);
		setCompositionRoot(root);
		
	}
	protected void siapkanPilihanDosenPengganti() {
		cbDosenPengganti.setEnabled(true);
		
		
	}
	private void saveLog(LogPerkuliahan log) {
		List<LogPerkuliahan> existedLogs = LogPerkuliahanPersistence.getLogSimilarOnDate(log);
		if (existedLogs.size()==0){
			log.setEntryOleh(user);
			GenericPersistence.merge(log);
			parent.close();
		} else {
			Notification.show("Data perkuliahan di hari yang sama untuk kelas bersangkutan sudah ada", Notification.Type.ERROR_MESSAGE);
		}
		
	}

}
