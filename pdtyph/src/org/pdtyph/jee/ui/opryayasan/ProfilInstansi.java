package org.pdtyph.jee.ui.opryayasan;

import org.pdtyph.cummon.ValidatorContent;
import org.pdtyph.cummon.ValidatorFields;
import org.pdtyph.cummon.ValidatorInteger;
import org.pdtyph.entity.Instansi;
import org.yph.jee.persistence.GenericPersistence;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ProfilInstansi extends  CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Label titleLabel;
	private VerticalLayout root;
	private BeanItem<Instansi> item;
	private FieldGroup fieldGroup;
	private Window parent;

	@SuppressWarnings("serial")
	public ProfilInstansi(Instansi i,final Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		this.parent = parent;
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setCompositionRoot(root);
		Responsive.makeResponsive(root);
		item=new BeanItem<Instansi>(i);
		root.addComponent(buildForm());
		Button simpan=new Button("Simpan",FontAwesome.SAVE);
		root.addComponent(simpan);
		simpan.addClickListener(new ClickListener() {
			@Override public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.commit();
					BeanItem bi = (BeanItem) fieldGroup.getItemDataSource();
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
		fieldGroup = new FieldGroup(item);
		ComboBox jenisLembaga = new ComboBox("Jenis Lembaga");
		jenisLembaga.addItem("SD/MI");
		jenisLembaga.addItem("SMP/MTs");
		jenisLembaga.addItem("SMA/MA/SMK");
		jenisLembaga.addItem("Perguruan Tinggi");
		jenisLembaga.addItem("Non Pendidikan");
		fly.addComponent(fieldGroup.buildAndBind("Nama", "nama"));
		fieldGroup.getField("nama").setRequired(true);
		fieldGroup.getField("nama").addValidator(new ValidatorFields());
		fly.addComponent(fieldGroup.buildAndBind("Kode", "kode"));
		fly.addComponent(fieldGroup.buildAndBind("Alamat", "alamat"));
		fieldGroup.bind(jenisLembaga, "jenisLembaga");
		fly.addComponent(jenisLembaga);
		fly.addComponent(fieldGroup.buildAndBind("No Telpon", "noTelp"));
		fly.addComponent(fieldGroup.buildAndBind("e-Mail", "email"));
		fieldGroup.getField("email").setRequired(true);
		fieldGroup.getField("email").addValidator(new ValidatorContent());
		fly.addComponent(fieldGroup.buildAndBind("Tahun Berdiri", "thnBerdiri"));
		fieldGroup.getField("thnBerdiri").setRequired(true);		
		fieldGroup.getField("thnBerdiri").addValidator(new ValidatorInteger("invalid another integer type"));
		
		fly.addComponent(fieldGroup.buildAndBind("Status Akreditasi", "stsAkreditasi"));
		fieldGroup.getField("stsAkreditasi").setRequired(true);
		fieldGroup.getField("stsAkreditasi").addValidator(new ValidatorFields());
		fly.addComponent(fieldGroup.buildAndBind("Pimpinan", "pimpinan"));
		fieldGroup.getField("pimpinan").setRequired(true);
		fieldGroup.getField("pimpinan").addValidator(new ValidatorFields());
		pnl.addComponent(fly);
		return pnl;
	}
	
	

}
