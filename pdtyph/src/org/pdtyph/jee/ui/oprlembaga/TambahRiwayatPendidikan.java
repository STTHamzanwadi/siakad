package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.RiwayatPendidikan;
import org.pdtyph.entity.UserOprInstansi;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.persistence.PegawaiPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class TambahRiwayatPendidikan extends  CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private VerticalLayout root;
	private BeanItem<RiwayatPendidikan> item;
	private FieldGroup fieldGroup;
	private ComboBox  pendidikan = new ComboBox ("Jenjang Pendidikan");
	@SuppressWarnings("serial")
	public TambahRiwayatPendidikan(RiwayatPendidikan rp,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
//		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setCompositionRoot(root);
		Responsive.makeResponsive(root);
		item=new BeanItem<RiwayatPendidikan>(rp);
		root.addComponent(buildForm());
		Button simpan=new Button("Simpan",FontAwesome.SAVE);
		root.addComponent(simpan);
		simpan.addClickListener(new ClickListener() {
			@Override public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.commit();
					BeanItem<?> bi = (BeanItem<?>) fieldGroup.getItemDataSource();
					GenericPersistence.merge(bi.getBean());
					parent.close();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//GenericPersistence.merge(item.getBean());
			}

		});
	}
	private Component buildForm() {	
		UserOprInstansi user = (UserOprInstansi) VaadinSession.getCurrent().getAttribute(
				UserOprInstansi.class);
		List<Instansi> i = GenericPersistence.findList(Instansi.class);
		List<Pegawai> p = PegawaiPersistence.getByLembaga(user.getInstansi());
		VerticalLayout pnl = new VerticalLayout();
		FormLayout fly = new FormLayout();
		setSizeFull();
		fieldGroup = new FieldGroup(item);
		ComboBox cmbPegawai = new ComboBox("Nama Pegawai");
		BeanItemContainer<Pegawai> pegawaiContainer = 
		new BeanItemContainer<>(Pegawai.class, p);
		cmbPegawai.setContainerDataSource(pegawaiContainer);
		cmbPegawai.setItemCaptionPropertyId("namaPegawai");
//		cmbPegawai.setFilteringMode(FilteringMode.CONTAINS);
		cmbPegawai.setImmediate(true);
		fieldGroup.bind(cmbPegawai, "nmaPegawai");		
		fly.addComponent(cmbPegawai);
		ComboBox instansi = new ComboBox("Instansi");
		BeanItemContainer<Instansi> instansiContainer = 
				new BeanItemContainer<>(Instansi.class, i);
		instansi.setContainerDataSource(instansiContainer);
		instansi.setItemCaptionPropertyId("nama");
		instansi.setFilteringMode(FilteringMode.CONTAINS);
		instansi.setImmediate(true);
		fieldGroup.bind(instansi, "nmInstansi");		
		fly.addComponent(instansi);
		 pendidikan.addItem("SD");
		 pendidikan.addItem("SMP");
		 pendidikan.addItem("SMA");
		 pendidikan.addItem("Profesi");
		 pendidikan.addItem("D1");
		 pendidikan.addItem("D2");
		 pendidikan.addItem("D3");
		 pendidikan.addItem("MDQH");
		 pendidikan.addItem("S1");
		 pendidikan.addItem("S2");
		 pendidikan.addItem("S3");
		 fieldGroup.bind(pendidikan, "jenjangPendidikan");
		fly.addComponent(pendidikan);
		fly.addComponent(fieldGroup.buildAndBind("Institusi", "namaInstansi"));
		fly.addComponent(fieldGroup.buildAndBind("Tahun Lulus", "tahunLulus"));
		fly.addComponent(fieldGroup.buildAndBind("Prodi", "jurusan"));
		fly.addComponent(fieldGroup.buildAndBind("Gelar Akademis", "gelarAkademis"));		
		pnl.addComponent(fly);
		return pnl;
	}
	
	

}
