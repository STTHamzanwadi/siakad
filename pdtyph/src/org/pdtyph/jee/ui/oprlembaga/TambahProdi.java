package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Prodi;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class TambahProdi extends  CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private VerticalLayout root;
	private BeanItem<Prodi> item;
	private FieldGroup fieldGroup;
	@SuppressWarnings("serial")
	public TambahProdi(Prodi p,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);
		root.addStyleName("dashboard-view");
		setCompositionRoot(root);
		Responsive.makeResponsive(root);
		item=new BeanItem<Prodi>(p);
		root.addComponent(buildForm());
		Button simpan=new Button("Simpan",FontAwesome.SAVE);
		root.addComponent(simpan);
		simpan.addStyleName(ValoTheme.BUTTON_FRIENDLY);
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
//		UserOprInstansi user = (UserOprInstansi) VaadinSession.getCurrent().getAttribute(
//				UserOprInstansi.class);
//		List<Instansi> i =PegawaiPersistence.getInstansi(user.getInstansi());
		List<Instansi> i = GenericPersistence.findList(Instansi.class);
		VerticalLayout pnl = new VerticalLayout();
		FormLayout fly = new FormLayout();
		setSizeFull();
		fieldGroup = new FieldGroup(item);
		ComboBox instansi = new ComboBox("Instansi");
		BeanItemContainer<Instansi> instansiContainer = 
				new BeanItemContainer<>(Instansi.class, i);
		instansi.setContainerDataSource(instansiContainer);
		instansi.setItemCaptionPropertyId("nama");
		instansi.setWidth("300px");
		instansi.setFilteringMode(FilteringMode.CONTAINS);
		instansi.setImmediate(true);
		fieldGroup.bind(instansi, "instansi");
		fly.addComponent(instansi);
		TextField txtNamaProdi=new TextField("Nama Prodi");
		txtNamaProdi.setWidth("300px");
		fieldGroup.bind(txtNamaProdi, "namaProdi");
		fly.addComponent(txtNamaProdi);		
		
		fly.addComponent(fieldGroup.buildAndBind("Kode", "kodeProdi"));
		fly.addComponent(fieldGroup.buildAndBind("Tahun Berdiri", "tahunBerdiri"));
		fly.addComponent(fieldGroup.buildAndBind("Status Akreditasi", "stsAkreditasi"));
		TextField txtProdi=new TextField("Nama Prodi");
		txtProdi.setWidth("300px");
		fieldGroup.bind(txtProdi, "kaprodi");
		fly.addComponent(txtProdi);
		
		pnl.addComponent(fly);
		return pnl;
	}
	
	

}
