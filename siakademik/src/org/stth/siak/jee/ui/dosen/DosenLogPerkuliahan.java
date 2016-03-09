package org.stth.siak.jee.ui.dosen;

import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class DosenLogPerkuliahan extends VerticalLayout implements View{

	private DosenKaryawan dosen;
	private Semester semester;
	private String ta;
	private KelasPerkuliahan kelas;
	private ComboBox cbKelas = new ComboBox("Pilih kelas");
	private VerticalLayout content = new VerticalLayout();
	private BeanItemContainer<KelasPerkuliahan> beanKelas = new BeanItemContainer<>(KelasPerkuliahan.class);	
	private BeanItemContainer<LogPerkuliahan> beanLog = new BeanItemContainer<>(LogPerkuliahan.class);

	public DosenLogPerkuliahan() {
		setMargin(true);
		setSpacing(true);
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		Responsive.makeResponsive(this);
		siapkanPilihanKelas();
		addComponent(ViewFactory.header("Log Perkuliahan Semester "+semester+" t.a "+ta ));
		addComponent(createFilterComponent());
		addComponent(content);
		addComponent(ViewFactory.footer());
	}
	
	
	private Component createFilterComponent() {
		Panel pnl = new Panel("Pilih kelas");
		VerticalLayout hl = new VerticalLayout();
		hl.setSpacing(true);
		hl.setMargin(true);
		pnl.setContent(hl);
		hl.addComponent(cbKelas);
		Button saring = new Button("Saring");
		saring.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				prepareContent();
			}
		});
		hl.addComponent(saring);
		return pnl;
	}
	
	protected void prepareContent() {
		content.removeAllComponents();
		if (kelas==null){
			Notification.show("Pilih kelas terlebih dahulu", Notification.Type.ERROR_MESSAGE);
			return;
		}
		beanLog.removeAllItems();
		List<LogPerkuliahan> l = LogPerkuliahanPersistence.getByKelas(kelas);
		beanLog.addAll(l);
		Panel p = new Panel("Log Perkuliahan");
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeUndefined();
		vl.setSpacing(true);
		vl.setMargin(true);
		p.setContent(vl);
		Table t = new Table();
		String[] visIds = {"tanggalPertemuan","ruangPertemuan","materiPertemuan","daftarHadir"};
		
		t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		t.addGeneratedColumn("daftarHadir", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final LogPerkuliahan log = (LogPerkuliahan) i.getBean();
				Button lihat = new Button("Lihat");
				lihat.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						siapkanDaftarHadir(log);
					}
				});
				return lihat; 
			}
		});
		t.setContainerDataSource(beanLog, Arrays.asList(visIds));
		vl.addComponent(t);
		content.addComponent(p);
		content.setImmediate(true);
	}


	private void siapkanPilihanKelas(){
		List<KelasPerkuliahan> l = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		beanKelas.addAll(l);
		cbKelas.setContainerDataSource(beanKelas);
		cbKelas.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				kelas = (KelasPerkuliahan) event.getProperty().getValue();
			}
		});
		
	}
	
	private void siapkanDaftarHadir(LogPerkuliahan log){
		DosenDaftarHadirMahasiswa dhm = new DosenDaftarHadirMahasiswa(log);
		GeneralPopups.showGenericWindow(dhm, "Daftar Hadir Mahasiswa");
	}


	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}

}
