package org.stth.siak.jee.ui.eis;


import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.DaftarMahasiswaView;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
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

public class CariMahasiswa extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private DosenKaryawan dosenPA;
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private BeanItemContainer<DosenKaryawan> beanDosenPA = new BeanItemContainer<>(DosenKaryawan.class);
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	private ComboBox cbPA = new ComboBox("Pilih Dosen P.A");
	protected ProgramStudi prodi;
	private List<DosenKaryawan> lpa;
	private VerticalLayout content = new VerticalLayout();
	private TextField tfNama;
	private TextField tfNimStart;
	private TextField tfAngkatan;

	public CariMahasiswa() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Pencarian Data Mahasiswa"));
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
		GridLayout gl = new GridLayout(3, 2);
		gl.setMargin(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		tfNama = new TextField("Nama");
		tfNimStart = new TextField("Nim mulai");
		tfAngkatan = new TextField("Angkatan");
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
		flKiri.addComponents(tfNama,tfNimStart,cbProdi);
		flKanan.addComponents(tfAngkatan,cbPA,buttonFilter);
		gl.addComponent(flKiri, 0, 0);
		gl.addComponent(flKanan, 1, 0);
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
		content.removeAllComponents();
		List<Mahasiswa> kr = MahasiswaPersistence.getListByExample(example);
		DaftarMahasiswaView dafv = new DaftarMahasiswaView(kr);
		Panel panel = new Panel("Hasil Pencarian");
		panel.setContent(dafv);
		panel.getContent().setSizeUndefined();
		content.addComponent(panel);
	}
	@Override
	public void enter(ViewChangeEvent event) {


	}
	
	

}
