package org.pdtyph.jee.ui.oprlembaga;

import java.util.List;

import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.UserOprInstansi;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.persistence.PegawaiPersistence;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class DaftarPegawai extends Panel implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Table tabel = new Table("DAFTAR PEGAWAI YPH PPD NW PANCOR");
	private VerticalLayout dashboardPanels;
	private FieldGroup fg;
	private VerticalLayout root;
	private BeanContainer<Integer, Pegawai> beans= new BeanContainer<Integer, Pegawai>(Pegawai.class);
	private BeanItem<Pegawai> bimp;

	
	public DaftarPegawai() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		bimp = new BeanItem<Pegawai>(new Pegawai());
		root = new VerticalLayout();
		//root.setSizeFull();
		root.setMargin(true);
		root.setSpacing(true);
		root.addStyleName("dashboard-view");
		setContent(root);
		Responsive.makeResponsive(root);
		root.addComponents(getTable());
		final Button tambah=new Button("Tambah",	FontAwesome.PLUS_CIRCLE);
		tambah.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		root.addComponents(tambah);
		root.setComponentAlignment(tambah, Alignment.BOTTOM_LEFT);		
		tambah.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				tambahPegawaiBaru(new Pegawai());
			}
		});
		
		
	}

	@SuppressWarnings({ "serial"})
	private Component getTable(){
		UserOprInstansi user = (UserOprInstansi) VaadinSession.getCurrent().getAttribute(
				UserOprInstansi.class);
		List<Pegawai> pr =PegawaiPersistence.getByLembaga(user.getInstansi());

		dashboardPanels = new VerticalLayout();
		dashboardPanels.addStyleName("dashboard-panels");
		Responsive.makeResponsive(dashboardPanels);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (pr!=null){
			beans.addAll(pr);
		} else {
			beans.addBean(new Pegawai());
		}
		tabel.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button edit = new Button(FontAwesome.EDIT);
				Button hapus = new Button(FontAwesome.TRASH_O);
				edit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				edit.addStyleName(ValoTheme.BUTTON_SMALL);
				hapus.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				hapus.addStyleName(ValoTheme.BUTTON_SMALL);
				BeanItem<?> p = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Pegawai o = (Pegawai) p.getBean();
				edit.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						tambahPegawaiBaru(o);
					}

				});
				hapus.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						try {
							
							BeanItem<?> p = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
							GenericPersistence.delete(p.getBean());
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
		tabel.addGeneratedColumn("riwayat", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button pendidikan = new Button(FontAwesome.EDIT);
				Button jabatan = new Button(FontAwesome.TRASH_O);
				pendidikan.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				pendidikan.addStyleName(ValoTheme.BUTTON_SMALL);
				jabatan.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				jabatan.addStyleName(ValoTheme.BUTTON_SMALL);
				BeanItem<?> p = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				p.getBean();
				pendidikan.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						
					}

				});
				jabatan.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						
						
					}

				});

				hl.addComponent(pendidikan);
				hl.addComponent(jabatan);
				return hl;
			}
		});
		tabel.addGeneratedColumn("detail", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button detail = new Button(FontAwesome.TRASH_O);
								detail.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				detail.addStyleName(ValoTheme.BUTTON_SMALL);
				BeanItem<?> p = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				p.getBean();
				
				detail.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						
						
					}

				});
				hl.addComponent(detail);
				return hl;
			}
		});
		tabel.setSizeFull();
		tabel.setImmediate(true);
		tabel.setSelectable(true);
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.setColumnHeader("instansi", "NAMA LEMBAGA");
		tabel.setColumnHeader("namaPegawai","NAMA PEGAWAI");
		tabel.setColumnHeader("jbtnStruktural","JABATAN STRUKTURAL");
		tabel.setColumnHeader("jbtnFungsional","JABATAN FUNGSIONAL");
		tabel.setColumnHeader( "alias","NAMA ALIAS");
		tabel.setColumnHeader("niy","NIY");
		tabel.setColumnHeader("nidn","NIDN/NUPN");
		tabel.setColumnHeader("nis","NIP/NUPTK");
		tabel.setColumnHeader("tempatLahir","TEMPAT LAHIR");
		tabel.setColumnHeader("tanggalLahir","TANGGAL LAHIR");
		tabel.setColumnHeader( "kelamin","JENIS KELAMIN");
		tabel.setColumnHeader("agama","AGAMA");
		tabel.setColumnHeader("email","E-MAIL");
		tabel.setColumnHeader("alamatRumah","ALAMAT");
		tabel.setColumnHeader("nomorTelepon","TELPON");
		tabel.setColumnHeader("nomorKtp","NIK");
		tabel.setColumnHeader("namaKepegawaian","JENIS KEPEGAWAIAN");
		tabel.setColumnHeader("statusKepegawaian","SETATUS KEPEGAWAIAN");
		tabel.setColumnHeader("golongan","GOLONGAN");
		tabel.setColumnHeader("thnMasuk","TAHUN MASUK");
		tabel.setColumnHeader("jenjangPendTerakhir","PENDIDIKAN TERAKHIR");
		tabel.setColumnHeader("prodiPendTerakhir","PRODI TERAKHIR");
		tabel.setColumnHeader("institusiPendTerakhir","INSTITUSI TERAKHIR");
		tabel.setColumnHeader("riwayat", "RIWAYAT");
		tabel.setColumnHeader("aksi", "AKSI");
		tabel.setColumnHeader("detail", "DETAIL");
		tabel.setVisibleColumns("aksi","instansi","namaPegawai","jbtnStruktural","jbtnFungsional","alias","niy","nidn","nis","tempatLahir","tanggalLahir","agama","email","alamatRumah","nomorTelepon","nomorKtp","namaKepegawaian","statusKepegawaian","golongan","thnMasuk","jenjangPendTerakhir","prodiPendTerakhir","institusiPendTerakhir","riwayat","detail");
	
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}
	private void tambahPegawaiBaru(Pegawai p) {
		final Window win = new Window("Data Pegawai");
		Component c = new TambahPegawai(p,win);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("1000px");
		win.center();
		UI.getCurrent().addWindow(win);

	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}


}
