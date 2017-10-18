package org.stth.siak.jee.ui.administrasi;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.UserAccessRightsAdministrasi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class AdministrasiDaftarLogPerkuliahan extends VerticalLayout implements View {

	private static final long serialVersionUID = -8249051415291086482L;
	private Semester semester;
	private String ta;
	private ComboBox cbDosen = new ComboBox("Dosen Pengampu");
	private ComboBox cbMataKuliah = new ComboBox("Mata Kuliah");
	private DateField periodStart = new DateField("Periode Mulai");
	private DateField periodEnd = new DateField("Periode End");
	private BeanItemContainer<DosenKaryawan> beanDosen = new BeanItemContainer<>(DosenKaryawan.class);
	private BeanItemContainer<KelasPerkuliahan> beanKelasPerkuliahan = new BeanItemContainer<>(KelasPerkuliahan.class);
	private VerticalLayout content = new VerticalLayout();
	protected DosenKaryawan dosen;
	protected KelasPerkuliahan kelasPerkuliahan;
	private List<UserAccessRightsAdministrasi> lacl;

	public AdministrasiDaftarLogPerkuliahan() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		lacl = (List<UserAccessRightsAdministrasi>) VaadinSession.getCurrent().getAttribute("admrights");
		
		addComponent(ViewFactory.header("Catatan Perkuliahan Semester "+semester+" t.a "+ ta));
		addComponent(createActionButton());
		addComponent(createFilterComponent());
		addComponent(content);
		addComponent(ViewFactory.footer());
	}
	
	private void siapkanDaftarDosen(){
		beanDosen.addAll(DosenKaryawanPersistence.getDosen());
		cbDosen.setContainerDataSource(beanDosen);
		
		cbDosen.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				dosen = (DosenKaryawan) event.getProperty().getValue();
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
			
			}
		});
	}
	
	private Component createActionButton() {
		HorizontalLayout hl = new HorizontalLayout();
		Button tambah;
		tambah = new Button("Tambah");
		tambah.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				AdministrasiEntryLogPerkuliahan entryLog = new AdministrasiEntryLogPerkuliahan();
				siapkanLogPerkuliahan(entryLog);
			}
		});
		
		
		
		hl.addComponents(tambah);
		
		
		hl.setSpacing(true);
		return hl;
	}
	
	private Component createFilterComponent(){
		Panel pnl = new Panel("Filter Data");
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		periodStart.setValue(c.getTime());
		c.add(Calendar.DATE, +2);
		periodEnd.setValue(c.getTime());
		Button saring = new Button("Saring");
		
		saring.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				saringData();
			}
		});
		hl.addComponents(cbDosen,cbMataKuliah,periodStart,periodEnd);
		vl.addComponents(hl,saring);
		vl.setSpacing(true);
		vl.setMargin(true);
		siapkanDaftarDosen();
		pnl.setContent(vl);
		return pnl;
	}
	
	protected void saringData() {
		Date dt1 = GeneralUtilities.truncateDate(periodStart.getValue());
		Date dt2 = GeneralUtilities.truncateNextDay(periodEnd.getValue());
		List<LogPerkuliahan> l;
		if (kelasPerkuliahan!=null){
			l = LogPerkuliahanPersistence.getByKelasOnPeriod(kelasPerkuliahan, dt1, dt2);
		} else if (dosen!=null){
			l = LogPerkuliahanPersistence.getByDosenOnPeriod(dosen,  dt1, dt2);
		} else {
			l = LogPerkuliahanPersistence.getLogOnPeriod( dt1, dt2);
		}
		Collections.sort(l);
		content.removeAllComponents();
		Table t = new Table();
		BeanContainer<Integer, LogPerkuliahan> beans = new BeanContainer<>(LogPerkuliahan.class);
		beans.setBeanIdProperty("id");
		if (l.size()>0){
			beans.addAll(l);
		} else {
			beans.addBean(new LogPerkuliahan());
		}		
		t.setContainerDataSource(beans);
		t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		t.setColumnHeader("aksi", "AKSI");
		t.setColumnHeader("diisiOleh", "DOSEN");
		t.setColumnHeader("kelasPerkuliahan", "KELAS");
		t.setColumnHeader("tanggalPertemuan", "WAKTU");
		t.setColumnHeader("ruangPertemuan", "RUANGAN");
		t.setColumnHeader("materiPertemuan", "MATERI");
		t.setColumnHeader("entryOleh", "PETUGAS ENTRI");
		t.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.setSpacing(true);
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final LogPerkuliahan log = (LogPerkuliahan) i.getBean();
				Button daftarHadir = new Button("Daftar Hadir");
				daftarHadir.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						siapkanIsianDaftarHadir(log);
					}

				});
				hl.addComponent(daftarHadir);
				if (ACLAdministrasiEnum.isEligibleTo(lacl, ACLAdministrasiEnum.LOG_PERKULIAHAN_EDIT)){
					Button edit = new Button("Ubah");
					edit.setEnabled(true);
					edit.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							AdministrasiEntryLogPerkuliahan entryLog = new AdministrasiEntryLogPerkuliahan(log);
							siapkanLogPerkuliahan(entryLog);
							
						}
					});
					hl.addComponent(edit);
				}
				//allow hapus
				//if (ACLAdministrasiEnum.isEligibleTo(lacl, ACLAdministrasiEnum.LOG_PERKULIAHAN_DELETE)){
				if (true){
					Button hapus;
					hapus = new Button("Hapus");
					hapus.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							deleteLog(log);
							Notification.show("Data berhasil dihapus");
							saringData();
						}

					});
					hl.addComponent(hapus);
				}
				
				return hl;
			}
		});
		t.setVisibleColumns("aksi","diisiOleh","kelasPerkuliahan","tanggalPertemuan","ruangPertemuan","materiPertemuan","entryOleh");
		t.setSizeUndefined();
		Panel pnl = new Panel("Daftar Hasil Penyaringan");
		VerticalLayout pnlroot = new VerticalLayout();
		pnlroot.setSizeUndefined();
		pnlroot.setSpacing(true);
		pnlroot.addComponent(t);
		pnlroot.setMargin(true);
		pnl.setContent(pnlroot);
		content.addComponent(pnl);
	}
	
	private void siapkanIsianDaftarHadir(LogPerkuliahan log) {
		final Window win = new Window("Daftar Hadir Mahasiswa");
		AdministrasiIsianDaftarHadirMahasiswa dhm = new AdministrasiIsianDaftarHadirMahasiswa(log, win);
		win.setContent(dhm);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	
	private void siapkanLogPerkuliahan(AdministrasiEntryLogPerkuliahan entryLog){
		final Window win = new Window("Entry Log Perkuliahan");
		entryLog.setParent(win);
		win.addCloseListener(new CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				saringData();
			}
		});
		win.setContent(entryLog);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	

	private void deleteLog(LogPerkuliahan log) {
		List<LogKehadiranPesertaKuliah> l = LogKehadiranPerkuliahanPersistence.getByLogPerkuliahan(log);
		for (LogKehadiranPesertaKuliah logKehadiranPesertaKuliah : l) {
			GenericPersistence.delete(logKehadiranPesertaKuliah);
		}
		GenericPersistence.delete(log);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
