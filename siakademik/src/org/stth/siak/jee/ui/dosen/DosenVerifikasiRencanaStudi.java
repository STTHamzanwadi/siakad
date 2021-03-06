package org.stth.siak.jee.ui.dosen;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.jee.persistence.RencanaStudiPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.jee.ui.generalview.MahasiswaProfilView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.jee.ui.mahasiswa.IPKView;

import com.ibm.icu.text.DecimalFormat;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class DosenVerifikasiRencanaStudi extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private Table tableMhs ;
	private VerticalLayout dashboardPanels;
	private BeanContainer<Integer, Mahasiswa> beansMhs = new BeanContainer<Integer, Mahasiswa>(Mahasiswa.class);
	private DosenKaryawan dosen;
	private Semester semester;
	private String ta;
	private boolean isKRSOpen;
	private Panel p ;
	private AbstractField<Object> cbStatus;
	//private int limitPengambilanSKS;

	public DosenVerifikasiRencanaStudi() {
		setMargin(true);
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		isKRSOpen = k.isKRSOpen();
		//limitPengambilanSKS = k.getKRSMaxSKS();
		Responsive.makeResponsive(this);
		p=new Panel();
		addComponent(ViewFactory.header("Verifikasi Rencana Studi Semester "+semester+" t.a "+ta ));
		if (isKRSOpen) {
			addComponent(filter());
			p.setContent(getTable());
			addComponent(p);
		} else {
			addComponent(new Label("Masa pengambilan mata kuliah telah ditutup"));
		}
		addComponent(ViewFactory.footer());

	}

	private Component filter(){
		FormLayout fl = new FormLayout();
		cbStatus = new ComboBox("Status", Arrays.asList(StatusRencanaStudi.values()));
		cbStatus.setValue(StatusRencanaStudi.DIAJUKAN);
		cbStatus.addValueChangeListener(e->{
			p.setContent(getTable());
		});
		fl.addComponent(cbStatus);
		return fl;
	}
	
	@SuppressWarnings("serial")
	private Component getTable(){
		tableMhs = new Table("DAFTAR STATUS PENGAJUAN RENCANA STUDI OLEH MAHASISWA BIMBINGAN AKADEMIK");
		List<Mahasiswa> lm = new ArrayList<>();
		StatusRencanaStudi sRS = (StatusRencanaStudi) cbStatus.getValue();
		if (sRS!=null) {
			RencanaStudiMahasiswa rsm = new RencanaStudiMahasiswa();
			rsm.setStatus(sRS);
			rsm.setSemester(semester);
			rsm.setTahunAjaran(ta);
			List<RencanaStudiMahasiswa> lrs = RencanaStudiPersistence.getList(rsm );
			for (RencanaStudiMahasiswa rs : lrs) {
				if (dosen.getId()==rs.getMahasiswa().getPembimbingAkademik().getId()) {
					lm.add(rs.getMahasiswa());
				}
			}
		}else{
			lm = MahasiswaPersistence.getListByPembimbingAkademik(dosen);
		}
		
		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beansMhs.setBeanIdProperty("id");
		beansMhs.removeAllItems();
		if (lm!=null){
			beansMhs.addAll(lm);
		} else {
			beansMhs.addBean(new Mahasiswa());
		}
		tableMhs.setContainerDataSource(beansMhs);
		tableMhs.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tableMhs.addGeneratedColumn("prodi", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {

				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				Mahasiswa o = (Mahasiswa) i.getBean();

				return o.getProdi().getNama();
			}
		});
		tableMhs.addGeneratedColumn("ipk", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				Mahasiswa o = (Mahasiswa) i.getBean();
				IndeksPrestasiHelper iph = new IndeksPrestasiHelper(o);
				DecimalFormat df = new DecimalFormat("#.00");
				return df.format(iph.getIpk());
			}
		});
		tableMhs.addGeneratedColumn("rs", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Mahasiswa o = (Mahasiswa) i.getBean();
				final RencanaStudiMahasiswa rsm = RencanaStudiPersistence.getByMhsSemTa(o, semester, ta);
				Button buttonVerifikasi = new Button("Belum Mengisi KRS");
				buttonVerifikasi.setStyleName(Reindeer.LAYOUT_BLACK);
				buttonVerifikasi.setEnabled(false);
				if (rsm!=null){
					buttonVerifikasi.setCaption(rsm.getStatus()+" | Lihat");
					if (rsm.getStatus()==StatusRencanaStudi.DISETUJUI) {
						buttonVerifikasi.setStyleName(Reindeer.LAYOUT_BLUE);
					}
					buttonVerifikasi.setEnabled(true);
					buttonVerifikasi.addClickListener(new ClickListener() {
						public void buttonClick(ClickEvent event) {
							showRencanaStudiMahasiswa(rsm);
						}
					});
				}
				hl.addComponents(buttonVerifikasi);
				return hl;
			}
		});
		tableMhs.addGeneratedColumn("prof", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.setSpacing(true);
				Button buttonProf = new Button("Profil");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Mahasiswa o = (Mahasiswa) i.getBean();
				buttonProf.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						showProfilMahasiswa(o);
					}
				});
				Button buttonTranskrip = new Button("Transkrip");
				buttonTranskrip.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						showIpkMahasiswa(o);
					}
				});
				hl.addComponents(buttonTranskrip,buttonProf);
				return hl;
			}
		});

		tableMhs.setColumnHeader("npm", "NIM");
		tableMhs.setColumnHeader("nama", "NAMA");
		tableMhs.setColumnHeader("prodi", "PRODI");
		tableMhs.setColumnHeader("ipk", "IPK");
		tableMhs.setColumnHeader("rs", "STATUS RENCANA STUDI");
		tableMhs.setColumnHeader("prof", "PROFIL");
		tableMhs.setVisibleColumns("npm","nama","prodi","ipk","rs","prof");
		dashboardPanels.addComponent(tableMhs);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {


	}

	private void showProfilMahasiswa(Mahasiswa m){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new MahasiswaProfilView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	private void showIpkMahasiswa(Mahasiswa m){
		final Window win = new Window("Transkrip Nilai Mahasiswa");
		Component c = new IPKView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}


	private void showRencanaStudiMahasiswa(RencanaStudiMahasiswa rsm) {
		final Window win = new Window("Pengajuan Rencana Studi Mahasiswa");
		final RencanaStudiDiajukanView c = new RencanaStudiDiajukanView(rsm, win);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
		win.addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				if (c.needRefresh){
					UI.getCurrent().getPage().reload();
				}
			}
		});
	}

}
