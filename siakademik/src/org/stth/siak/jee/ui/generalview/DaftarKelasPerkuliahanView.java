package org.stth.siak.jee.ui.generalview;

import java.util.Collections;
import java.util.List;

import org.stth.siak.entity.KelasPerkuliahan;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DaftarKelasPerkuliahanView extends CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	private Table tabel;
	private List<KelasPerkuliahan> lkp;
	private BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<Integer, KelasPerkuliahan>(KelasPerkuliahan.class);


	public DaftarKelasPerkuliahanView(List<KelasPerkuliahan> lkp){
		this.lkp = lkp;
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(getTable());
		setCompositionRoot(layout);
	}

	@SuppressWarnings("serial")
	public Component getTable(){
		tabel = new Table("Daftar Kelas Perkuliahan ");
		Collections.sort(lkp);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lkp!=null){
			beans.addAll(lkp);
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
		return tabel;
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
