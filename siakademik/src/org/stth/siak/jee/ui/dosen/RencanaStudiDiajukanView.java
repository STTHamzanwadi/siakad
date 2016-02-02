package org.stth.siak.jee.ui.dosen;

import java.util.ArrayList;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.RencanaStudiPilihanMataKuliahPersistence;
import org.stth.siak.entity.MataKuliah;
import org.stth.siak.entity.RencanaStudiMahasiswa;
import org.stth.siak.entity.RencanaStudiPilihanMataKuliah;
import org.stth.siak.enumtype.StatusRencanaStudi;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class RencanaStudiDiajukanView extends Panel implements View {

    private Label titleLabel;
    private VerticalLayout dashboardPanels;
    private VerticalLayout root;
    private Table tableMatkul = new Table("DAFTAR MATA KULIAH YANG DIAMBIL");
	private BeanContainer<Integer, RencanaStudiPilihanMataKuliah> beansPK = new BeanContainer<Integer, RencanaStudiPilihanMataKuliah>(RencanaStudiPilihanMataKuliah.class);
	private RencanaStudiMahasiswa rsm;
	private Window window;
	public boolean needRefresh=false;

    
	public RencanaStudiDiajukanView(RencanaStudiMahasiswa rsm, Window win) {
		this.rsm = rsm;
		this.window = win;
    	prepareView();
    }
	
	private void prepareView() {
		
    	addStyleName(ValoTheme.PANEL_BORDERLESS);
        root = new VerticalLayout();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());
        Component content = populateMatkul();
        root.addComponent(content);
        Component aksi = createActionButton();
        root.addComponent(aksi);
        root.setExpandRatio(content, 1);
	}
	
	

    private Component createActionButton() {
    	HorizontalLayout hl = new HorizontalLayout();
    	hl.setSpacing(true);
    	hl.addComponent(new Label("Status Rencana Studi: "+rsm.getStatus().toString()));
		if (rsm.getStatus().equals(StatusRencanaStudi.DIAJUKAN)){
			Button buttonSetujui = new Button("SETUJUI");
			buttonSetujui.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					rsm.setStatus(StatusRencanaStudi.DISETUJUI);
					GenericPersistence.merge(rsm);
					needRefresh = true;
					window.close();
				}
			});
			Button buttonTolak = new Button("TOLAK");
			buttonTolak.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					rsm.setStatus(StatusRencanaStudi.DITOLAK);
					GenericPersistence.merge(rsm);
					needRefresh = true;
					window.close();
					
				}
			});
			hl.addComponents(buttonSetujui,buttonTolak);
		}
		return hl;
	}

	private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Verifikasi Rencana Studi Oleh Wali");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    
    
  
    private Component populateMatkul() {
        dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        int totSks=0;
        List<MataKuliah> ls = new ArrayList<>();
        List<RencanaStudiPilihanMataKuliah> lsR = RencanaStudiPilihanMataKuliahPersistence.getByRencanaStudi(rsm);
        beansPK.setBeanIdProperty("id");
		beansPK.removeAllItems();
        if (lsR!=null) {
        	beansPK.addAll(lsR);
        	for (RencanaStudiPilihanMataKuliah rencanaStudiPilihanMataKuliah : lsR) {
				MataKuliah mk = rencanaStudiPilihanMataKuliah.getMataKuliah();
				ls.add(mk);
				totSks += mk.getSks();
			}
		}
		beansPK.addBean(new RencanaStudiPilihanMataKuliah());
		
		tableMatkul.setContainerDataSource(beansPK);
		
		tableMatkul.addGeneratedColumn("kode", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				RencanaStudiPilihanMataKuliah o = (RencanaStudiPilihanMataKuliah) i.getBean();
				if (o.getMataKuliah()!=null){
					return o.getMataKuliah().getKode();
				}
				return '-';
			}
		});
		tableMatkul.addGeneratedColumn("nama", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				RencanaStudiPilihanMataKuliah o = (RencanaStudiPilihanMataKuliah) i.getBean();
				if (o.getMataKuliah()!=null){
					return o.getMataKuliah().getNama();
				}
				return '-';
			}
		});
		tableMatkul.addGeneratedColumn("sks", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				RencanaStudiPilihanMataKuliah o = (RencanaStudiPilihanMataKuliah) i.getBean();
				if (o.getMataKuliah()!=null){
					return o.getMataKuliah().getSks();
				}
				return '-';
			}
		});
		tableMatkul.setColumnHeader("keterangan", "KETERANGAN");
		
		tableMatkul.setVisibleColumns("kode","nama","sks","keterangan");
		dashboardPanels.addComponent(tableMatkul);
		Label ipk = new Label("Indeks Prestasi Kumulatif : "+rsm.getIpk());
		Label totSksL = new Label("Jumlah SKS yang diambil : "+totSks);
		Label nilaiD = new Label("Keterangan : "+ rsm.getRemarks());
		dashboardPanels.addComponents(ipk,totSksL,nilaiD);
        return dashboardPanels;
    }

   

    
        @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
