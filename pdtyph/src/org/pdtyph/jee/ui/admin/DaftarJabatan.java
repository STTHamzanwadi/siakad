package org.pdtyph.jee.ui.admin;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import org.pdtyph.entity.Admin;
import org.pdtyph.entity.Jabatan;
import org.pdtyph.entity.UserOprInstansi;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.persistence.PegawaiPersistence;
import org.yph.jee.util.PasswordEncryptionService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarJabatan extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("DAFTAR JABATAN LEMBAGA YPHPPD NW PANCOR");
	private VerticalLayout dashboardPanels;
	private VerticalLayout root;
	private FieldGroup binder;
	private BeanContainer<Integer, Jabatan> beans = new BeanContainer<Integer, Jabatan>(Jabatan.class);

	public DaftarJabatan() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSpacing(true);
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponent(getTable());
		Button tes=new Button("tes");
		tes.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					dor();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		root.addComponent(tes);
	
	}
	
	protected void dor() throws NoSuchAlgorithmException, InvalidKeySpecException {
		Admin  u = new Admin();
		u.setEmail("operator@hamzanwadi.org");
		u.setNama("harianto");
		u.setUserName("admin");
		u.setRealName("Operator 1");
		u.setRegisterDate(new Date());
		byte[] salt = PasswordEncryptionService.generateSalt();
		byte[] pass = PasswordEncryptionService.getEncryptedPassword("admin", salt);
		
		u.setSalt(salt);
		u.setPassword(pass);
		u.setLastSuccessfulLogin(new Date());
		GenericPersistence.merge(u);
		
	}
	@SuppressWarnings("serial")
	private Component getTable(){
		List<Jabatan> pr =GenericPersistence.findList(Jabatan.class);

		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (pr!=null){
			beans.addAll(pr);
		} else {
			beans.addBean(new Jabatan());
		}

		tabel.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button hapus = new Button(FontAwesome.TRASH_O);
				hapus.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				hapus.addStyleName(ValoTheme.BUTTON_SMALL);
				BeanItem<?> p = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Jabatan o = (Jabatan) p.getBean();
				
				hapus.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						try {
							binder.commit();
							BeanItem<?> bi = (BeanItem<?>) binder.getItemDataSource();
							GenericPersistence.delete(bi.getBean());
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}

				});

				hl.addComponent(hapus);
				return hl;
			}
		});
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("instansi", "NAMA LEMBAGA");
		tabel.setColumnHeader("namaJabatan", "JABATAN");
		
		tabel.setColumnHeader("jnsJabatan", "JENIS JABATAN");
		tabel.setColumnHeader("urut", "URUT JABATAN");
		tabel.setColumnHeader("aksi", "AKSI");
		tabel.setVisibleColumns("aksi","instansi","namaJabatan","jnsJabatan","urut");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
