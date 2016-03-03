package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.RiwayatJabatan;
import org.pdtyph.entity.RiwayatPendidikan;
import org.pdtyph.entity.UserOprInstansi;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.persistence.PegawaiPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarRiwayatJabatan extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("DAFTAR RIWAYAT JABATAN PEGAWAI YPHPPD NW PANCOR");
	private VerticalLayout dashboardPanels;
	private Label titleLabel;
	private VerticalLayout root;
	private FieldGroup fieldGroup;
	private BeanItem<DaftarRiwayatJabatan> item;
	private BeanContainer<Integer, RiwayatJabatan> beans = new BeanContainer<Integer, RiwayatJabatan>(RiwayatJabatan.class);

	
	public DaftarRiwayatJabatan() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);	
		root.addComponent(getTable());

	}
	

	@SuppressWarnings("serial")
	private Component getTable(){
		UserOprInstansi user = (UserOprInstansi) VaadinSession.getCurrent().getAttribute(
				UserOprInstansi.class);
		List<RiwayatJabatan> rj =PegawaiPersistence.getRiwayatJabatanByLembaga(user.getInstansi());

		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (rj!=null){
			beans.addAll(rj);
		} else {
			beans.addBean(new RiwayatJabatan());
		}

		tabel.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button edit = new Button(FontAwesome.EDIT);
				Button hapus = new Button(FontAwesome.TRASH_O);
				edit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				edit.addStyleName(ValoTheme.BUTTON_SMALL);
				hapus.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				hapus.addStyleName(ValoTheme.BUTTON_SMALL);
				BeanItem<?> p = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final RiwayatJabatan o = (RiwayatJabatan) p.getBean();
				edit.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						tambahRiwayatJabatanbaru(o);
					}

				});
				hapus.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						try {
							fieldGroup.commit();
							BeanItem<?> bi = (BeanItem<?>) fieldGroup.getItemDataSource();
							GenericPersistence.delete(bi.getBean());
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}

				});

				hl.addComponent(edit);
				hl.addComponent(hapus);
				return hl;
			}
		});
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("nmPegawai", "NAMA PEGAWAI");
		tabel.setColumnHeader("nmInstansi", "NAMA INSTANSI");
		tabel.setColumnHeader("nmJabatan", "JABATAN");
		tabel.setColumnHeader("mulai", "MULAI");
		tabel.setColumnHeader("selesai", "SELESAI");
		tabel.setColumnHeader("noSK", "NO SK");
		tabel.setColumnHeader("aksi", "AKSI");
		tabel.setVisibleColumns("aksi","nmPegawai","nmInstansi","nmJabatan","mulai","selesai","noSK");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	private void tambahRiwayatJabatanbaru(RiwayatJabatan rj) {
		final Window win = new Window("Riwayat Jabatan");
		Component c = new TambahRiwayatJabatan(rj,win);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
