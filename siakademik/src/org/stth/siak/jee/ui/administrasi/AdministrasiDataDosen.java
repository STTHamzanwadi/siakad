package org.stth.siak.jee.ui.administrasi;


import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.ui.generalview.DaftarDosenView;
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

public class AdministrasiDataDosen extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	protected ProgramStudi prodi;
	private VerticalLayout content = new VerticalLayout();
	private TextField tfNama;
	

	public AdministrasiDataDosen() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Administrasi Data Mahasiswa"));
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		addComponent(content);
		addComponent(ViewFactory.footer());

	}
	private void siapkanPilihanProdi(){
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setContainerDataSource(beanProdi); 
		cbProdi.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				prodi = (ProgramStudi) event.getProperty().getValue();
				
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
		flKiri.addComponents(tfNama,buttonFilter);
		flKanan.addComponents(cbProdi);
		gl.addComponent(flKiri, 0, 0);
		gl.addComponent(flKanan, 1, 0);
		gl.setSpacing(true);
		pnl.setContent(gl);
		return pnl;
	}
	
	
	protected void filter() {
		DosenKaryawan example = new DosenKaryawan();
		example.setNama(tfNama.getValue());
		example.setProdi(prodi);
		content.removeAllComponents();
		DosenKaryawanPersistence.getDosenByExample(example);
		List<DosenKaryawan> kr = DosenKaryawanPersistence.getDosenByExample(example);
		int visColumns = DaftarMahasiswaView.NIM 
				| DaftarMahasiswaView.NAMA 
				| DaftarMahasiswaView.PRODI 
				| DaftarMahasiswaView.TGL_LAHIR
				| DaftarMahasiswaView.TEMPAT_LAHIR
				| DaftarMahasiswaView.DOSEN_PA
				;
		int allowedActions = DaftarMahasiswaView.LIHAT_PROFIL
				| DaftarMahasiswaView.EDIT;
		DaftarDosenView dafv = new DaftarDosenView(kr,visColumns 
				,allowedActions);
		Panel panel = new Panel("Hasil Pencarian");
		panel.setContent(dafv);
		panel.getContent().setSizeUndefined();
		content.addComponent(panel);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		

	}
	
	

}
