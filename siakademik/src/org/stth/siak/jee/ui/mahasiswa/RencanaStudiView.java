package org.stth.siak.jee.ui.mahasiswa;


import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.RencanaStudiMatkulAdditionMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulKeterangan;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.enumtype.StatusRencanaStudi;
import org.stth.siak.helper.RencanaStudiManualHelper;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportContentFactory;
import org.stth.siak.rpt.ReportOutputGenerator;
import org.stth.siak.rpt.ReportRawMaterials;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class RencanaStudiView extends VerticalLayout implements View{


	private static final long serialVersionUID = 3747760953041344405L;
	private Button buttonAutoPick;
	private Semester semester = Semester.GENAP;
	private String ta = "2015-2016";
	private int limitPengambilanSKS = 22;
	private boolean isKRSOpen;
	private Table tableMkTersedia = new Table("DAFTAR MATA KULIAH TERSEDIA");
	private Table tableMKterpilih = new Table("DAFTAR MATA KULIAH YANG SUDAH DIPILIH");
	private RencanaStudiManualHelper rsmh;
	private Mahasiswa mhs;
	private HorizontalLayout tableHl = new HorizontalLayout();
	private VerticalLayout rightContainer = new VerticalLayout();
	private VerticalLayout leftContainer = new VerticalLayout();
	private int sksTotal;
	private Label labelSksTotal= new Label();
	private BeanContainer<Integer, MataKuliahRencanaStudi> beanMkrs = new BeanContainer<Integer, MataKuliahRencanaStudi>(MataKuliahRencanaStudi.class);
	private BeanContainer<Integer, RencanaStudiPilihanMataKuliah> beanRspmks = new BeanContainer<Integer, RencanaStudiPilihanMataKuliah>(RencanaStudiPilihanMataKuliah.class);
	private Button buttonPrint;
	private Button buttonSubmit;
	private HorizontalLayout actionButtonContainer;
	final Logger logger = LoggerFactory.getLogger(RencanaStudiView.class);

	public RencanaStudiView() {
		mhs = (Mahasiswa) VaadinSession.getCurrent().getAttribute(
				Mahasiswa.class);
		setMargin(true);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		isKRSOpen = k.isKRSOpen();
		limitPengambilanSKS = k.getKRSMaxSKS();
		logger.debug("Semester {} - tahun ajaran {}", semester,ta);
		logger.debug("Limit SKS: " + limitPengambilanSKS);
		if (isKRSOpen) {

			GenericPersistence.closeSession();
			rsmh = new RencanaStudiManualHelper(mhs, semester, ta, limitPengambilanSKS);
			
			if (rsmh.isEligibleForEntry()) {
				prepareMainUI();
			} else {
				String textNoAccessRights = "<center><h3>Anda belum memiliki hak akses untuk mengisi rencana studi pada semester ini. "
						+ "Mohon Lengkapi persyaratan di BAAK untuk mendapatkan hak akses.<br>"
						+ "Terima Kasih</h3></center>";
				Label noAccessRights = new Label(textNoAccessRights,
						ContentMode.HTML);
				noAccessRights.setStyleName(ValoTheme.LABEL_FAILURE);
				addComponent(new Label(" "));
				addComponent(noAccessRights);
			}
		} else {
			String textOutsidePeriod = "<center><h3>Masa pengisian rencana studi online telah ditutup. "
					+ "Jika anda memiliki pertanyaan silahkan menghubungi bagian akademik.<br>"
					+ "Terima Kasih</h3></center>";
			Label lblOutsidePeriod = new Label(textOutsidePeriod,
					ContentMode.HTML);
			lblOutsidePeriod.setStyleName(ValoTheme.LABEL_FAILURE);
			addComponent(new Label(" "));
			addComponent(lblOutsidePeriod);
		}


	}

	private void prepareMainUI() {
		removeAllComponents();
		addComponent(ViewFactory.header("Rencana Studi "+ semester+" "+ta));
		prepareActionButtons();
		populateMataKuliahTersediaTable();
		populateMataKuliahTerpilihTable();
		addComponent(actionButtonContainer);
		Label lbl = new Label("Status Pengajuan Rencana Studi: "+ rsmh.getRencanaStudi().getStatus());
		Label lblDesc = new Label("<i><small>Informasi urutan status rencana studi: Draft, Diajukan, Disetujui/Ditolak, Final</i></small><p></p>");
		lblDesc.setContentMode(ContentMode.HTML);
		addComponents(lbl);
		addComponents(lblDesc);
		addComponent(tableHl);
		rightContainer.addComponent(tableMKterpilih);
		prepareTableMataKulTerpilih();
		rightContainer.addComponent(labelSksTotal);
		leftContainer.addComponent(tableMkTersedia);
		tableHl.setSpacing(true);
		tableHl.addComponent(leftContainer);
		tableHl.addComponent(rightContainer);
		addComponent(ViewFactory.footer());
	}


	private void prepareActionButtons(){
		actionButtonContainer = new HorizontalLayout();
		actionButtonContainer.setSpacing(true);
		
		buttonAutoPick = new Button("Pilih Mata Kuliah Secara Otomatis", new ClickListener() {
			private static final long serialVersionUID = -6853145078651546796L;
			@Override
			public void buttonClick(ClickEvent event) {
				rsmh.ambilMataKuliahOtomatis();
				populateMataKuliahTerpilihTable();
			}
		});
		buttonPrint = new Button("Cetak", new ClickListener() {
			private static final long serialVersionUID = -6853145078651546796L;
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					printRencanaStudi();
				} catch (JRException e) {
					
					e.printStackTrace();
				}
			}
		});
		buttonSubmit = new Button("Ajukan Ke Wali", new ClickListener() {
			private static final long serialVersionUID = -6853145078651546796L;
			@Override
			public void buttonClick(ClickEvent event) {
				submitRencanaStudi();
				UI.getCurrent().getPage().reload();
			}
		});
		actionButtonContainer.addComponents(buttonAutoPick,buttonPrint,buttonSubmit);
		refreshActionButtonsState();
	}

	protected void submitRencanaStudi() {
		RencanaStudiMahasiswa rsm = rsmh.getRencanaStudi();
		rsm.setStatus(StatusRencanaStudi.DIAJUKAN);
		GenericPersistence.merge(rsm);
		rsmh = new RencanaStudiManualHelper(mhs, semester, ta, limitPengambilanSKS);
		//prepareMainUI();

	}

	private void refreshActionButtonsState(){
		StatusRencanaStudi state = rsmh.getRencanaStudi().getStatus();

		if (state.equals(StatusRencanaStudi.DRAFT)){
			buttonPrint.setEnabled(false);
			buttonSubmit.setEnabled(true);
			buttonAutoPick.setEnabled(true);
		}
		else if (state.equals(StatusRencanaStudi.DIAJUKAN)){
			buttonPrint.setEnabled(true);
			buttonSubmit.setEnabled(false);
			buttonAutoPick.setEnabled(false);
		}
		else if (state.equals(StatusRencanaStudi.DITOLAK)){
			buttonPrint.setEnabled(false);
			buttonSubmit.setEnabled(true);
			buttonAutoPick.setEnabled(true);
		}
		else if (state.equals(StatusRencanaStudi.DISETUJUI)){
			buttonPrint.setEnabled(true);
			buttonSubmit.setEnabled(false);
			buttonAutoPick.setEnabled(false);
		}
		else if (state.equals(StatusRencanaStudi.FINAL)){
			buttonPrint.setEnabled(true);
			buttonSubmit.setEnabled(false);
			buttonAutoPick.setEnabled(false);
		}

	}

	@SuppressWarnings("deprecation")
	protected void printRencanaStudi() throws JRException {
		List<RencanaStudiMahasiswa> rss = new ArrayList<>();
		rss.add(this.rsmh.getRencanaStudi());
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportRencanaStudi(rss);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Rencana Studi Mahasiswa");
		StreamResource resource = rog.exportToPdf();
 		getUI().getPage().open(resource, "_blank", false);
        
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	private void populateMataKuliahTersediaTable() {
		beanMkrs.setBeanIdProperty("id");
		List<MataKuliahRencanaStudi> ls = rsmh.getMatkulAvailable();
		beanMkrs.removeAllItems();
		beanMkrs.addAll(ls);
		tableMkTersedia.setContainerDataSource(beanMkrs);
		tableMkTersedia.addGeneratedColumn("SKS", new ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6116248434559742727L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				MataKuliahRencanaStudi mkrs = (MataKuliahRencanaStudi) i.getBean();
				return mkrs.getMataKuliah().getSks();
			}
		});
		tableMkTersedia.addGeneratedColumn("oldmark", new ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5553399701227634672L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				MataKuliahRencanaStudi mkrs = (MataKuliahRencanaStudi) i.getBean();
				String kodemk = mkrs.getMataKuliah().getKode();
				return rsmh.getNilaiLamaBilaAda(kodemk);
			}
		});

		tableMkTersedia.addGeneratedColumn("Aksi", new ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8859706558584315063L;

			@Override
			public Object generateCell(final Table source,
					final Object itemId, final Object columnId) {
				Button button = new Button("Ambil");
				button.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 6503445304990970016L;

					@Override
					public void buttonClick(ClickEvent event) {
						BeanItem<?> i = (BeanItem<?>) source
								.getContainerDataSource().getItem(itemId);
						MataKuliahRencanaStudi mkrs = (MataKuliahRencanaStudi) i
								.getBean();
						addPilihanMataKuliahRencanaStudi(mkrs);
						populateMataKuliahTerpilihTable();
					}
				});

				StatusRencanaStudi state = rsmh.getRencanaStudi().getStatus();
				if (state.equals(StatusRencanaStudi.DRAFT)||state.equals(StatusRencanaStudi.DITOLAK)) {
					return button;
				}
				return new Label(" ");
			}
		});

		tableMkTersedia.setColumnHeader("mataKuliah", "MATA KULIAH");
		tableMkTersedia.setColumnHeader("semesterBuka", "SEM");
		tableMkTersedia.setColumnHeader("oldmark", "GRADE");
		tableMkTersedia.setColumnHeader("Aksi", "AKSI");
		tableMkTersedia.setVisibleColumns("mataKuliah","SKS","semesterBuka","oldmark","Aksi");

	}

	private void deleteRencanaStudiPilihanMatakuliah(
			RencanaStudiPilihanMataKuliah o) {
		GenericPersistence.delete(o);
		populateMataKuliahTerpilihTable();
		Notification.show("Perhatian",
				"Mata kuliah " + o.getMataKuliah() +" sudah dihapus dari pilihan rencana studi",
				Notification.Type.HUMANIZED_MESSAGE);
	}
	private void addPilihanMataKuliahRencanaStudi(MataKuliahRencanaStudi mkrs) {
		int sksTambahan = mkrs.getMataKuliah().getSks();
		if ((sksTotal + sksTambahan)>limitPengambilanSKS){
			Notification.show("Perhatian",
					"SKS yang anda ambil melebihi ketentuan",
					Notification.Type.ERROR_MESSAGE);
			return;
		}
		String s = rsmh.getNilaiLamaBilaAda(mkrs.getMataKuliah().getKode());
		RencanaStudiMatkulKeterangan k = RencanaStudiMatkulKeterangan.REGULER;
		if (s!=null){
			k = RencanaStudiMatkulKeterangan.MENGULANG;
			Notification.show("Perhatian",
					"Anda pernah mengambil mata kuliah ini dan mendapat nilai " + s,
					Notification.Type.WARNING_MESSAGE);
		}
		RencanaStudiPilihanMataKuliah rspmk = new RencanaStudiPilihanMataKuliah(
				rsmh.getRencanaStudi(), mkrs.getMataKuliah());
		rspmk.setAddMethod(RencanaStudiMatkulAdditionMethod.MANUAL_MAHASISWA);
		rspmk.setSubmittedBy(mhs.getNama());
		rspmk.setKeterangan(k);
		GenericPersistence.merge(rspmk);
		populateMataKuliahTerpilihTable();

	}
	private void populateMataKuliahTerpilihTable() {
		beanRspmks.setBeanIdProperty("id");
		List<Criterion> lc = new ArrayList<>();
		lc.add(Restrictions.eq("rencanaStudi", rsmh.getRencanaStudi()));
		//System.out.println(rsm.getId());
		List<RencanaStudiPilihanMataKuliah> ls = GenericPersistence.findList(RencanaStudiPilihanMataKuliah.class, lc);
		System.out.println(ls.size());
		sksTotal = 0;
		beanRspmks.removeAllItems();
		for (RencanaStudiPilihanMataKuliah rencanaStudiPilihanMataKuliah : ls) {
			beanRspmks.addBean(rencanaStudiPilihanMataKuliah);
			sksTotal = sksTotal + rencanaStudiPilihanMataKuliah.getMataKuliah().getSks();
		}
		labelSksTotal.setValue("Total SKS : "+sksTotal);

	}

	private void prepareTableMataKulTerpilih() {
		tableMKterpilih.setContainerDataSource(beanRspmks);

		tableMKterpilih.addGeneratedColumn("SKS", new ColumnGenerator() {
			private static final long serialVersionUID = -4580812840907731347L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				RencanaStudiPilihanMataKuliah o = (RencanaStudiPilihanMataKuliah) i.getBean();
				return o.getMataKuliah().getSks();
			}
		});

		tableMKterpilih.addGeneratedColumn("Aksi", new ColumnGenerator() {
			private static final long serialVersionUID = -8171967164764811461L;

			@Override
			public Object generateCell(final Table source,
					final Object itemId, final Object columnId) {
				Button button = new Button("Hapus");
				button.addClickListener(new ClickListener() {
					private static final long serialVersionUID = -4478739536913982357L;

					@Override
					public void buttonClick(ClickEvent event) {
						BeanItem<?> i = (BeanItem<?>) source
								.getContainerDataSource().getItem(itemId);
						RencanaStudiPilihanMataKuliah o = (RencanaStudiPilihanMataKuliah) i
								.getBean();
						deleteRencanaStudiPilihanMatakuliah(o);
						populateMataKuliahTerpilihTable();
					}
				});
				StatusRencanaStudi state = rsmh.getRencanaStudi().getStatus();
				if (state.equals(StatusRencanaStudi.DRAFT)||state.equals(StatusRencanaStudi.DITOLAK)) {
					return button;
				}
				return new Label(" ");
			}
		});

		//tableMKterpilih.remo
		tableMKterpilih.setColumnHeader("mataKuliah", "MATA KULIAH");
		tableMKterpilih.setColumnHeader("keterangan", "KETERANGAN");
		tableMKterpilih.setColumnHeader("Aksi", "AKSI");
		tableMKterpilih.setVisibleColumns("mataKuliah","SKS","keterangan","Aksi");
	}

}
