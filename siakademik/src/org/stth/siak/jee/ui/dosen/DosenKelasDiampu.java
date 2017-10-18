package org.stth.siak.jee.ui.dosen;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.stth.jee.persistence.JadwalKuliahPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.DaftarLogPerkuliahan;
import org.stth.siak.jee.ui.generalview.PesertaKelasPerkuliahanView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenKelasDiampu extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1653775031486924211L;
	private Table tabel;
	private VerticalLayout dashboardPanels;
	private BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<Integer, KelasPerkuliahan>(KelasPerkuliahan.class);
	private DosenKaryawan dosen;
	private Semester semester;
	private String ta;
	private Panel filter;
	private Panel daftarTugasMengajar;

	public DosenKelasDiampu() {
		setMargin(true);
		setSpacing(true);
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Tugas Mengajar Semester "));
		filter = new Panel("Filter");
		filter.setContent(buildFilter());
		daftarTugasMengajar = new Panel("Daftar Tugas Mengajar");
		daftarTugasMengajar.setContent(getTable());
		addComponents(filter, daftarTugasMengajar);
		addComponent(ViewFactory.footer());

	}

	private Component buildFilter(){
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flMid = new FormLayout();
		FormLayout flKanan = new FormLayout();
		TextField tfta = new TextField("T.A");
		tfta.setValue(ta);
		flKiri.addComponent(tfta);
		ComboBox cbSemester = new ComboBox("Semester", Arrays.asList(Semester.values()));
		cbSemester.setValue(semester);
		flMid.addComponent(cbSemester);
		Button cari = new Button("Cari");
		cari.addClickListener(e->{
			ta=tfta.getValue();
			semester=(Semester) cbSemester.getValue();
			daftarTugasMengajar.setContent(getTable());
		});
		flKanan.addComponent(cari);
		hl.addComponents(flKiri, flMid, flKanan);
		return hl;
	}

	@SuppressWarnings("serial")
	private Component getTable(){


		List<KelasPerkuliahan> lm = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosenSemesterTa(dosen, semester, ta);
		Collections.sort(lm);
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new KelasPerkuliahan());
		}
		tabel = new Table("");
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.addGeneratedColumn("sks", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getMataKuliah().getSks();
			}
		});

		tabel.addGeneratedColumn("matakuliah", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getMataKuliah().toString();
			}
		});

		tabel.addGeneratedColumn("prodi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getProdi().getNama();
			}
		});

		tabel.addGeneratedColumn("jadwal", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				List<JadwalKuliah> l = JadwalKuliahPersistence.getJadwalByKelasPerkuliahan(o);
				if (l.size()>0){
					String s="";
					for (JadwalKuliah j : l) {
						s = s + j.getHariJam() + " ";
					}
					return s;
				}
				return "-";
			}
		});

		tabel.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.setSpacing(true);
				Button button = new Button("Peserta Kuliah");
				Button buttonLog = new Button("Log Mengajar");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				button.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						showPesertaKuliah(o);
					}

				});
				buttonLog.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						showLogPerkuliahan(o);
					}

				});
				hl.addComponents(button,buttonLog);
				return hl;
			}
		});
		tabel.setColumnHeader("matakuliah", "MATA KULIAH");
		tabel.setColumnHeader("prodi", "PRODI");
		tabel.setColumnHeader("kodeKelas", "KELAS");
		tabel.setColumnHeader("sks", "SKS");
		tabel.setColumnHeader("aksi", "LIHAT");
		tabel.setColumnHeader("jadwal", "JADWAL");
		tabel.setVisibleColumns("matakuliah","sks","kodeKelas","jadwal","prodi","aksi");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


	private void showPesertaKuliah(KelasPerkuliahan o) {
		PesertaKelasPerkuliahanView c = new PesertaKelasPerkuliahanView(o);
		c.setRekapKehadiranVisible();
		c.refreshContent();
		GeneralPopups.showGenericWindow(c,"Daftar Peserta Kuliah");
	}


	private void showLogPerkuliahan(KelasPerkuliahan kp) {
		DaftarLogPerkuliahan dlp = new DaftarLogPerkuliahan(kp);
		GeneralPopups.showGenericWindow(dlp, "Log Perkuliahan");

	}


}
