package org.stth.siak.jee.ui.dosen;


import java.util.List;

import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.helper.IndeksPrestasiHelper;
import org.stth.siak.jee.genericview.MahasiswaProfilView;
import org.stth.siak.jee.genericview.ViewFactory;
import org.stth.siak.jee.ui.mahasiswa.IPKView;

import com.ibm.icu.text.DecimalFormat;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class DosenStatusBimbinganAkademik extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private Table tableMhs = new Table("ANDA ADALAH PEMBIMBING AKADEMIK UNTUK MAHASISWA BERIKUT INI");
	private VerticalLayout dashboardPanels;
	private BeanContainer<Integer, Mahasiswa> beansMhs = new BeanContainer<Integer, Mahasiswa>(Mahasiswa.class);
	private DosenKaryawan dosen;

	public DosenStatusBimbinganAkademik() {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Bimbingan Akademik"));
		addComponent(getTable());
		addComponent(ViewFactory.footer());

	}
	

	
	@SuppressWarnings("serial")
	private Component getTable(){
		List<Mahasiswa> lm = MahasiswaPersistence.getListByPembimbingAkademik(dosen);
		dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        beansMhs.setBeanIdProperty("id");
		beansMhs.removeAllItems();
		if (lm!=null){
			beansMhs.addAll(lm);
		} else {
			beansMhs.addBean(new Mahasiswa());
		}
		tableMhs.setContainerDataSource(beansMhs);
		tableMhs.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tableMhs.addGeneratedColumn("prodi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				Mahasiswa o = (Mahasiswa) i.getBean();
				return o.getProdi().getNama();
			}
		});
		tableMhs.addGeneratedColumn("ipk", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				Mahasiswa o = (Mahasiswa) i.getBean();
				IndeksPrestasiHelper iph = new IndeksPrestasiHelper(o);
				DecimalFormat df = new DecimalFormat("#.00");
				return df.format(iph.getIpk());
			}
		});
		tableMhs.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button buttonTranskrip = new Button("Profil");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Mahasiswa o = (Mahasiswa) i.getBean();
				buttonTranskrip.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						showProfilMahasiswa(o);
					}
				});
				Button buttonProfil = new Button("Transkrip");
				buttonProfil.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						showIpkMahasiswa(o);
					}
				});
				hl.addComponents(buttonProfil,buttonTranskrip);
				return hl;
			}
		});
		tableMhs.setColumnHeader("npm", "NIM");
		tableMhs.setColumnHeader("nama", "NAMA");
		tableMhs.setColumnHeader("prodi", "PRODI");
		tableMhs.setColumnHeader("ipk", "IPK");
		tableMhs.setColumnHeader("aksi", "LIHAT");
		tableMhs.setVisibleColumns("npm","nama","prodi","ipk","aksi");
		dashboardPanels.addComponent(tableMhs);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {


	}
	
	private void showProfilMahasiswa(Mahasiswa m){
		final Window win = new Window("Profil Mahasiswa");
		Component c = new MahasiswaProfilView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}
	private void showIpkMahasiswa(Mahasiswa m){
		final Window win = new Window("Transkrip Nilai Mahasiswa");
		Component c = new IPKView(m);
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(c);
		win.setContent(vl);
		win.setModal(true);
		win.setWidth("600px");
		win.center();
		UI.getCurrent().addWindow(win);
	}

}
