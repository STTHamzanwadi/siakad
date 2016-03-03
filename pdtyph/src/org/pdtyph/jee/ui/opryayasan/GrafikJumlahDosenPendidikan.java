package org.pdtyph.jee.ui.opryayasan;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.yph.jee.persistence.GenericPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class GrafikJumlahDosenPendidikan extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Label titleLabel;
	private VerticalLayout root;

	private BeanItem<Instansi> item;
	private FieldGroup fieldGroup;
	private ComboBox instansi = new ComboBox("Instansi");
	private ComboBox  pendidikan = new ComboBox ("Profesi");
	public GrafikJumlahDosenPendidikan() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(buildHeader());
		root.addComponent(buildFields());

	}
	private Component buildHeader() {
		VerticalLayout header = new VerticalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		titleLabel = new Label("Grafik Jumlah Pegawai Aktif Berdasarkan Jenjang pendidikan");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H4);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(titleLabel);
		return header;
	}
	
	private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");
        List<Instansi> i = GenericPersistence.findList(Instansi.class);
		fieldGroup = new FieldGroup(item);
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
		 
		 
		instansi = new ComboBox("Instansi");
		BeanItemContainer<Instansi> instansiContainer = 
		new BeanItemContainer<>(Instansi.class, i);
		instansi.setContainerDataSource(instansiContainer);
		instansi.setItemCaptionPropertyId("nama");
		instansi.setFilteringMode(FilteringMode.CONTAINS);
		instansi.setImmediate(true);
        instansi.setIcon(FontAwesome.UNIVERSITY);
        instansi.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        pendidikan.setIcon(FontAwesome.GRADUATION_CAP);
        pendidikan.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Cari");
        signin.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(instansi, pendidikan, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            	
            }
        });
        return fields;
    }
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
