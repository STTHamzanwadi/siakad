package org.stth.siak.jee.ui.generalview;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.UserAccessRightsAdministrasi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.dosen.DosenDaftarHadirMahasiswa;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;

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
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class DaftarLogPerkuliahan extends CustomComponent{

	private Semester semester;
	private String ta;
	private VerticalLayout content = new VerticalLayout();
	protected DosenKaryawan dosen;
	protected KelasPerkuliahan kelasPerkuliahan;
	private List<UserAccessRightsAdministrasi> lacl;

	public DaftarLogPerkuliahan(KelasPerkuliahan kp) {
		this.kelasPerkuliahan = kp;
		content.setMargin(true);
		content.setSpacing(true);
		Responsive.makeResponsive(this);
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		content.setSizeUndefined();
		saringData();
		setCompositionRoot(content);
	}
	
	
	protected void saringData() {
		List<LogPerkuliahan> l;
		l = LogPerkuliahanPersistence.getByKelas(kelasPerkuliahan);
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
		t.setColumnHeader("aksi", "DAFTAR HADIR");
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
						siapkanDaftarHadir(log);
					}

				});
				hl.addComponent(daftarHadir);
				return hl;
			}
		});
		t.setVisibleColumns("tanggalPertemuan","ruangPertemuan","materiPertemuan","aksi");
		t.setSizeUndefined();
		content.addComponent(t);
	}
	
	private void siapkanDaftarHadir(LogPerkuliahan log){
		DosenDaftarHadirMahasiswa dhm = new DosenDaftarHadirMahasiswa(log);
		GeneralPopups.showGenericWindow(dhm,"Daftar Hadir Mahasiswa");
	}
	
	
	

}
