package org.stth.siak.jee.ui.generalview;

import java.util.Collections;
import java.util.List;

import org.stth.jee.persistence.MataKuliahKurikulumPersistence;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.MataKuliahKurikulum;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

public class KurikulumView extends CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	private Kurikulum kr;
	private Table table;

	public KurikulumView(Kurikulum kr){
		this.kr = kr;
		VerticalLayout layout = new VerticalLayout();
		//layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(getTable());
		setCompositionRoot(layout);
	}

	@SuppressWarnings("serial")
	public Component getTable(){
		table = new Table("Daftar Mata Kuliah "+ kr.getNama());
		BeanContainer<Integer, MataKuliahKurikulum> beans = new BeanContainer<Integer, MataKuliahKurikulum>(MataKuliahKurikulum.class);
		List<MataKuliahKurikulum> lm = MataKuliahKurikulumPersistence.getByKurikulum(kr);
		Collections.sort(lm);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lm!=null){
			beans.addAll(lm);
		} else {
			beans.addBean(new MataKuliahKurikulum());
		}
		table.setContainerDataSource(beans);
		table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		table.addGeneratedColumn("kode", new ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				MataKuliahKurikulum o = (MataKuliahKurikulum) i.getBean();
				return o.getMataKuliah().getKode();
			}
		});
			
		table.addGeneratedColumn("nama", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				MataKuliahKurikulum o = (MataKuliahKurikulum) i.getBean();
				return o.getMataKuliah().getNama();
			}
		});
		table.addGeneratedColumn("sks", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				MataKuliahKurikulum o = (MataKuliahKurikulum) i.getBean();
				return o.getMataKuliah().getSks();
			}
		});
		table.setColumnHeader("semesterBuka", "SEM");
		table.setColumnHeader("kode", "KODE");
		table.setColumnHeader("nama", "NAMA");
		table.setColumnHeader("sks", "SKS");
		table.setColumnHeader("jenis", "JENIS");
		table.setVisibleColumns("semesterBuka","kode","nama","sks","jenis");
		return table;
	}

}
