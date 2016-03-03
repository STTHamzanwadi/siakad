package org.pdtyph.jee.ui.oprlembaga;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.UserOprYayasan;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.util.PasswordEncryptionService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarGambar extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("UPLOAD PHOTO PEGAWAI YPH");
	private VerticalLayout dashboardPanels;
	private Label titleLabel;
	private VerticalLayout root;
	private FieldGroup binder;
	private BeanContainer<Integer, Jabatan> beans = new BeanContainer<Integer, Jabatan>(Jabatan.class);

	@SuppressWarnings("serial")
	public DaftarGambar() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(buildHeader());
		
		root.addComponent(new UploadExample());

	}
	private Component buildHeader() {
		VerticalLayout header = new VerticalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		titleLabel = new Label("Daftar Photo");
		titleLabel.setSizeUndefined();
		titleLabel.addStyleName(ValoTheme.LABEL_H1);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(titleLabel);
		return header;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
