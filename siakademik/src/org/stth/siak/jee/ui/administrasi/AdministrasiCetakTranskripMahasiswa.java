package org.stth.siak.jee.ui.administrasi;


import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.enumtype.StatusMasuk;
import org.stth.siak.jee.ui.generalview.DaftarMahasiswaView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


import net.sf.jasperreports.engine.JRException;

public class AdministrasiCetakTranskripMahasiswa extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private DosenKaryawan dosenPA;
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private BeanItemContainer<DosenKaryawan> beanDosenPA = new BeanItemContainer<>(DosenKaryawan.class);
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	private ComboBox cbPA = new ComboBox("Pilih Dosen P.A");
	private ComboBox cbStatus = new ComboBox("Status", Arrays.asList(StatusMahasiswa.values()));
	protected ProgramStudi prodi;
	private List<DosenKaryawan> lpa;
	private VerticalLayout content = new VerticalLayout();
	private StatusMahasiswa statusM;
	private TextField tfNama;
	private TextField tfNimStart;
	private TextField tfAngkatan;
	List<Mahasiswa> kr;
	private ComboBox cbStatusMasuk;

	public AdministrasiCetakTranskripMahasiswa() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		//addComponent(ViewFactory.header("Cetak Transkrip Mahasiswa"));
		addComponent(ViewFactory.header("Administrasi Data Mahasiswa"));
		Button add = new Button("Tambah");
		add.addClickListener(klik->{
			AdministrasiEditorDataMahasiswa ae = new AdministrasiEditorDataMahasiswa(null);
			GeneralPopups.showGenericWindow(ae, "Edit Data Mahasiswa");
		});
		addComponent(add);
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		siapkanPilihanPA();
		addComponent(content);
		addComponent(ViewFactory.footer());

	}
	private void siapkanPilihanProdi(){
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setContainerDataSource(beanProdi); 
		//cbProdi.setNullSelectionAllowed(false);
		//cbProdi.setTextInputAllowed(false);
		cbProdi.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				prodi = (ProgramStudi) event.getProperty().getValue();

			}
		});
	}
	private void siapkanPilihanPA(){
		lpa = DosenKaryawanPersistence.getDosen();
		java.util.Collections.sort(lpa);
		beanDosenPA.addAll(lpa);
		cbPA.setContainerDataSource(beanDosenPA); 
		cbPA.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				dosenPA = (DosenKaryawan) event.getProperty().getValue();

			}
		});
	}


	private Component createFilterComponent() {
		Panel pnl = new Panel("Cari mahasiswa");
		GridLayout gl = new GridLayout(4, 2);
		gl.setMargin(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flMid = new FormLayout();
		FormLayout flKanan = new FormLayout();
		tfNama = new TextField("Nama");
		tfNimStart = new TextField("Nim mulai");
		tfAngkatan = new TextField("Angkatan");
		cbStatus.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				statusM =  (StatusMahasiswa) event.getProperty().getValue();
				System.out.println(statusM);

			}
		});
		flKiri.setSpacing(true);
		flKanan.setSpacing(true);

		Button buttonFilter = new Button("Cari");
		buttonFilter.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				filter();
			}

		});
		
		cbStatusMasuk = new ComboBox("Status Masuk", Arrays.asList(StatusMasuk.values()));
		flKiri.addComponents(tfNama,tfNimStart);
		flMid.addComponents(cbProdi, tfAngkatan);
		flKanan.addComponents(cbPA, cbStatus);
		FormLayout flButton  = new FormLayout();
		flButton.addComponents(cbStatusMasuk, buttonFilter);
		gl.addComponent(flKiri, 0, 0);
		gl.addComponent(flMid, 1, 0);
		gl.addComponent(flKanan, 2, 0);
		gl.addComponent(flButton, 3,0);
		gl.setSpacing(true);
		pnl.setContent(gl);
		return pnl;
	}


	protected void filter() {
		Mahasiswa example = new Mahasiswa();
		example.setNpm(tfNimStart.getValue());
		example.setNama(tfNama.getValue());
		if (!tfAngkatan.getValue().isEmpty()) {
			example.setAngkatan(Integer.parseInt(tfAngkatan.getValue()));
		}
		example.setProdi(prodi);
		example.setPembimbingAkademik(dosenPA);
		example.setStatus(statusM);
		example.setStatusMasuk((StatusMasuk) cbStatusMasuk.getValue());
		content.removeAllComponents();
		kr = MahasiswaPersistence.getListByExample(example);
		int visColumns = DaftarMahasiswaView.NIM 
				| DaftarMahasiswaView.NAMA 
				| DaftarMahasiswaView.PRODI 
				//| DaftarMahasiswaView.TGL_LAHIR
				//| DaftarMahasiswaView.TEMPAT_LAHIR
				| DaftarMahasiswaView.DOSEN_PA
				;
		int allowedActions = DaftarMahasiswaView.LIHAT_PROFIL | DaftarMahasiswaView.EDIT
				| DaftarMahasiswaView.CETAK_TRANSKRIP;
		DaftarMahasiswaView dafv = new DaftarMahasiswaView(kr, visColumns, allowedActions);
		dafv.showTranskripButton();
		Panel panel = new Panel("Hasil Pencarian");
		panel.setContent(dafv);
		panel.getContent().setSizeUndefined();
		content.addComponent(panel);
		if (statusM!=null) {
			if (statusM.equals(StatusMahasiswa.LULUS)) {
				Button printAll = new Button("Print All");
				printAll.addClickListener(event->{

					Window window = new AdministrasiPopupTanggalLulus();
					window.addCloseListener(e-> {
						StreamResource resource;
						try {
							Object o =  e.getWindow().getData();
							String[] l = (String[]) o;
							resource = ReportResourceGenerator.cetakTranskripWisudaMahasiswa(kr,l);
							getUI().getPage().open(resource, "_blank", false);
						} catch (JRException ej) {
							// TODO Auto-generated catch block
							ej.printStackTrace();
						}
					});
					getUI().addWindow(window);

				});
				content.addComponent(printAll);
			}
		}
	}
	@Override
	public void enter(ViewChangeEvent event) {

	}


}
