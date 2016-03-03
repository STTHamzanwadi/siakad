package org.pdtyph.jee.ui.admin;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.JabatanYayasan;
import org.yph.jee.persistence.GenericPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
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

public class TambahJabatan extends  CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private VerticalLayout root;
	private BeanItem<JabatanYayasan> item;
	private FieldGroup fieldGroup;
	@SuppressWarnings("serial")
	public TambahJabatan(JabatanYayasan j,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
//		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setCompositionRoot(root);
		Responsive.makeResponsive(root);
		item=new BeanItem<JabatanYayasan>(j);
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
		VerticalLayout pnl = new VerticalLayout();
		FormLayout fly = new FormLayout();
		setSizeFull();
		fieldGroup = new FieldGroup(item);
		ComboBox jenisJabatan = new ComboBox("Jenis Jabatan");
		jenisJabatan.addItem("Struktural");
		jenisJabatan.addItem("Fungsional");
		jenisJabatan.setFilteringMode(FilteringMode.CONTAINS);
		jenisJabatan.setImmediate(true);
		fieldGroup.bind(jenisJabatan, "jnsJabatan");	
		TextField txtNamaJabatan=new TextField("Nama Jabatan");
		txtNamaJabatan.setWidth("300px");
		fieldGroup.bind(txtNamaJabatan, "namaJabatan");
		fly.addComponent(txtNamaJabatan);
		fly.addComponent(jenisJabatan);
		TextField txtUrut=new TextField("Urut Jabatan");
		txtUrut.setWidth("300px");
		fieldGroup.bind(txtUrut, "urut");
		fly.addComponent(txtUrut);
		pnl.addComponent(fly);
		return pnl;
	}
	
	

}
