package org.stth.siak.jee.ui.generalview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.PesertaKuliah;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

public class PesertaKelasPerkuliahanView extends CustomComponent{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4001325208244016531L;
	private KelasPerkuliahan kp;
	private Table table;
	private VerticalLayout content = new VerticalLayout();
	private boolean showRekapKehadiran=false;
	private boolean showNilai=false;

	public PesertaKelasPerkuliahanView(KelasPerkuliahan kp){
		this.kp = kp;
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(content);
		content.setSizeUndefined();
		content.addComponent(getTable());
		DosenKaryawan d = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		if (d!=null){
			layout.addComponent(getExcelExporter());
		}
		setCompositionRoot(layout);
	}
	@SuppressWarnings("serial")
	private Component getExcelExporter() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Button buttonExport = new Button("Export ke Excel");
		buttonExport.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ExcelExport exp = new ExcelExport(table);
				exp.export();
			}
		});
		//hl.addComponent(buttonExport);
		return hl;
	}
	@SuppressWarnings("serial")
	public Component getTable(){
		table = new Table();
		BeanContainer<Integer, PesertaKuliah> beans = new BeanContainer<Integer, PesertaKuliah>(PesertaKuliah.class);
		List<PesertaKuliah> lm = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kp);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new PesertaKuliah());
		}
		table.setContainerDataSource(beans);
		table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		table.addGeneratedColumn("nim", new ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				PesertaKuliah o = (PesertaKuliah) i.getBean();
				return o.getMahasiswa().getNpm();
			}
		});
			
		table.addGeneratedColumn("nama", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				PesertaKuliah o = (PesertaKuliah) i.getBean();
				return o.getMahasiswa().getNama();
			}
		});
		table.addGeneratedColumn("pa", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				PesertaKuliah o = (PesertaKuliah) i.getBean();
				return o.getMahasiswa().getPembimbingAkademik().getNama();
			}
		});
		table.addGeneratedColumn("rekapHadir", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				PesertaKuliah o = (PesertaKuliah) i.getBean();
				List<LogKehadiranPesertaKuliah> l = LogKehadiranPerkuliahanPersistence.getByPesertaKuliah(o);
				return l.size();
			}
		});
		String[] s = {"nim","nama","pa"};
		List<String> visColumns= new ArrayList<>(Arrays.asList(s));
		table.setColumnHeader("nama", "NAMA");
		table.setColumnHeader("nim", "NIM");
		table.setColumnHeader("nilai", "NILAI");
		table.setColumnHeader("rekapHadir", "TOTAL HADIR");
		table.setColumnHeader("pa", "PEMBIMBING AKADEMIK");
		table.setColumnHeader("nim", "NIM");
		if (showNilai){
			visColumns.add("nilai");
		}
		if (showRekapKehadiran){
			visColumns.add("rekapHadir");
		}
		table.setVisibleColumns(visColumns.toArray());
		return table;
	}
	
	public void refreshContent(){
		content.removeAllComponents();
		content.addComponent(getTable());
	}
	
	public void setNilaiVisible(){
		showNilai = true;
	}
	public void setRekapKehadiranVisible(){
		showRekapKehadiran = true;
	}
	
}
