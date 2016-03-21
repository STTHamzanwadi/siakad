package org.stth.siak.jee.ui.administrasi;

import java.util.Arrays;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.JenisKelamin;
import org.stth.siak.enumtype.StatusDosen;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class AdministrasiEditorDataDosen extends CustomComponent{
	
	private VerticalLayout vl = new VerticalLayout();
	private DosenKaryawan dosen;
	private BeanItem<DosenKaryawan> item;
	private TextArea taAlamat;
	private ComboBox cbAgama, cbJenisKelamin, cbJenjangPendidikan, cbStatus;
	private FieldGroup fg;
	private DosenKaryawan user;
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	/**
	 * Open editor for Mahasiswa instance
	 * @param dosen if creating new record, use new Mahasiswa()
	 */
	
	public AdministrasiEditorDataDosen(DosenKaryawan dosen) {
		this.dosen = dosen;
		user = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setCompositionRoot(vl);
		vl.setMargin(true);
		Responsive.makeResponsive(this);
		item = new BeanItem<DosenKaryawan>(this.dosen);
		siapkanPilihanProdi();
		vl.addComponent(buildForm());
	}
	
	private void siapkanPilihanProdi(){
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setContainerDataSource(beanProdi); 
	}
	
	private Component buildForm() {
		//VerticalLayout root = new VerticalLayout();
		Panel pnl = new Panel("Data Dosen");
		Panel pnlW = new Panel("Data Pendidikan Terakhir");
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		HorizontalLayout hl = new HorizontalLayout();
		HorizontalLayout hlW = new HorizontalLayout();
		hlW.setSpacing(true);
		hlW.setMargin(true);
		hl.setSpacing(true);
		FormLayout fl1 = new FormLayout(),fl2 = new FormLayout();
		hl.addComponents(fl1,fl2);
		vl.addComponent(hl);
		vl.addComponent(pnlW);
		pnl.setContent(vl);
		pnlW.setContent(hlW);
		
		fg = new FieldGroup(item);
		
		fl1.addComponent(fg.buildAndBind("Nama", "nama"));
		fl2.addComponent(fg.buildAndBind("Alias", "alias"));
		
		fl1.addComponent(fg.buildAndBind("NIDN", "nidn"));
		fl2.addComponent(fg.buildAndBind("NIS", "nis"));
		
		fl1.addComponent(fg.buildAndBind("Tanggal Lahir", "tanggalLahir"));
		fl2.addComponent(fg.buildAndBind("Tempat Lahir", "tempatLahir"));
		
		Container container = GeneralUtilities.createContainerFromEnumClass(JenisKelamin.class);
		cbJenisKelamin = new ComboBox("Jenis Kelamin", container);
		fl1.addComponent(cbJenisKelamin);
		fg.bind(cbJenisKelamin, "jenisKelamin");
		
		cbAgama = new ComboBox("Agama", Arrays.asList(GeneralUtilities.AGAMA));
		fl2.addComponent(cbAgama);
		fg.bind(cbAgama, "agama");
				
		fl1.addComponent(cbProdi);
		fg.bind(cbProdi, "prodi");
		
		fl1.addComponent(fg.buildAndBind("Nomor Telepon", "nomorTelepon"));
		fl2.addComponent(fg.buildAndBind("Alamat Email", "email"));
		
		Property<Integer> integerProperty = (Property<Integer>) item
		        .getItemProperty("thnMasuk");
		TextField tfTahunMasuk = new TextField("Tahun Masuk", integerProperty);
		fl2.addComponent(tfTahunMasuk);
		fg.bind(tfTahunMasuk, "thnMasuk");
		
		fl1.addComponent(fg.buildAndBind("Nomor KTP", "nomorKtp"));
		
		Container cStatusDosen = GeneralUtilities.createContainerFromEnumClass(StatusDosen.class);
		cbStatus = new ComboBox("Status Dosen",Arrays.asList(new String[] {"TETAP","TIDAK TETAP"}));
		fl2.addComponent(cbStatus);
		fg.bind(cbStatus, "status");
		
		taAlamat = new TextArea("Alamat");
		fg.bind(taAlamat, "alamatRumah");
		fl1.addComponent(taAlamat);
		
		cbJenjangPendidikan = new ComboBox("Jenjang", Arrays.asList(new String[]{"S1","S2","S3"}));
		hlW.addComponent(cbJenjangPendidikan);
		fg.bind(cbJenjangPendidikan, "jenjangPendTerakhir");
		
		
		
		hlW.addComponent(fg.buildAndBind("Program Studi", "prodiPendTerakhir"));
		hlW.addComponent(fg.buildAndBind("Institusi Studi", "institusiPendTerakhir"));
		
		
		
		Button simpan = new Button("Simpan");
		simpan.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fg.commit();
					DosenKaryawan m =  item.getBean();
					m.setUpdateOleh(user);
					GenericPersistence.merge(item.getBean());
					Notification.show("Perubahan data berhasil dilakukan", Notification.Type.HUMANIZED_MESSAGE);
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		vl.addComponent(simpan);
		return pnl;
	}
	
	

}
