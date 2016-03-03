package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.RiwayatJabatan;
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

public class TambahRiwayatJabatan extends  CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private VerticalLayout root;
	private BeanItem<RiwayatJabatan> item;
	private FieldGroup fieldGroup;
	@SuppressWarnings("serial")
	public TambahRiwayatJabatan(RiwayatJabatan rj,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
//		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setCompositionRoot(root);
		Responsive.makeResponsive(root);
		item=new BeanItem<RiwayatJabatan>(rj);
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
		List<Instansi> i =PegawaiPersistence.getInstansi(user.getInstansi());
		List<Pegawai> p = PegawaiPersistence.getByLembaga(user.getInstansi());
		List<Jabatan> j = PegawaiPersistence.getJabatanByLembaga(user.getInstansi());
		VerticalLayout pnl = new VerticalLayout();
		FormLayout fly = new FormLayout();
		setSizeFull();
		fieldGroup = new FieldGroup(item);
		
		fly.addComponent(fieldGroup.buildAndBind("Nama Pegawai", "nmPegawai"));
		ComboBox instansi = new ComboBox("Instansi");
		BeanItemContainer<Instansi> instansiContainer = 
				new BeanItemContainer<>(Instansi.class, i);
		instansi.setContainerDataSource(instansiContainer);
		instansi.setItemCaptionPropertyId("nama");
		instansi.setFilteringMode(FilteringMode.CONTAINS);
		instansi.setImmediate(true);
		fieldGroup.bind(instansi, "nmInstansi");		
		fly.addComponent(instansi);
		
		ComboBox cmbjabatan = new ComboBox("Jabatan");
		BeanItemContainer<Jabatan> jabatanContainer = 
		new BeanItemContainer<>(Jabatan.class, j);
		cmbjabatan .setContainerDataSource(jabatanContainer);
		cmbjabatan .setItemCaptionPropertyId("namaJabatan");
		cmbjabatan .setFilteringMode(FilteringMode.CONTAINS);
		cmbjabatan .setImmediate(true);
		fieldGroup.bind(cmbjabatan, "nmJabatan");
		fly.addComponent(cmbjabatan);
		
		fly.addComponent(fieldGroup.buildAndBind("Mulai", "mulai"));
		fly.addComponent(fieldGroup.buildAndBind("Selesai", "selesai"));
		fly.addComponent(fieldGroup.buildAndBind("No SK", "noSK"));		
		pnl.addComponent(fly);
		return pnl;
	}
	
	
	
	
}
