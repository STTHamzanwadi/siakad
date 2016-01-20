package org.stth.siak.jee.ui.dosen;


import java.util.Collections;
import java.util.List;

import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.jee.ui.generalview.PesertaKelasPerkuliahanView;
import org.stth.siak.jee.ui.generalview.ViewFactory;

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

public class DosenKelasDiampu extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1653775031486924211L;
	private Table tabel = new Table("ANDA ADALAH PENGAMPU UNTUK KELAS BERIKUT INI");
	private VerticalLayout dashboardPanels;
	private BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<Integer, KelasPerkuliahan>(KelasPerkuliahan.class);
	private DosenKaryawan dosen;

	public DosenKelasDiampu() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Riwayat Mengajar"));
		addComponent(getTable());
		addComponent(ViewFactory.footer());

	}

	
	@SuppressWarnings("serial")
	private Component getTable(){
		List<KelasPerkuliahan> lm = KelasPerkuliahanPersistence.getKelasPerkuliahanByDosen(dosen);
		Collections.sort(lm);
		dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new KelasPerkuliahan());
		}
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.addGeneratedColumn("matakuliah", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getMataKuliah().toString();
			}
		});
		tabel.addGeneratedColumn("prodi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getProdi().getNama();
			}
		});
		
		tabel.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button button = new Button("Peserta");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				button.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						showPesertaKuliah(o);
					}

				});
				hl.addComponent(button);
				return hl;
			}
		});
		tabel.setColumnHeader("matakuliah", "MATA KULIAH");
		tabel.setColumnHeader("semester", "SEMESTER");
		tabel.setColumnHeader("prodi", "PRODI");
		tabel.setColumnHeader("tahunAjaran", "T.A");
		tabel.setColumnHeader("aksi", "LIHAT");
		tabel.setVisibleColumns("tahunAjaran","semester","matakuliah","prodi","aksi");
		dashboardPanels.addComponent(tabel);
		return dashboardPanels;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	

	private void showPesertaKuliah(KelasPerkuliahan o) {
		final Window win = new Window("Daftar Peserta Kuliah");
		Component c = new PesertaKelasPerkuliahanView(o);
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
