package org.stth.siak.jee.ui.administrasi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.DaftarKelasPerkuliahanView;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.rpt.ReportContentFactory;
import org.stth.siak.rpt.ReportOutputGenerator;
import org.stth.siak.rpt.ReportRawMaterials;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class AdministrasiCetakAbsensi extends VerticalLayout implements View{
	private static final long serialVersionUID = -1425418749387686688L;
	private VerticalLayout root = new VerticalLayout();
	private Panel content = new Panel("Daftar Kelas");
	private ComboBox comboProdi;
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private Semester semester;
	private String ta;
	private ProgramStudi prodi;
	
	public AdministrasiCetakAbsensi(){
		setMargin(true);
		setSpacing(true);
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		addComponent(ViewFactory.header("Daftar Kelas Perkuliahan Semester "+semester+" t.a "+ ta));
		addComponent(createFilterComponent());
		addComponent(content);
		addComponent(ViewFactory.footer());
		setMargin(true);
	}

	private Component createFilterComponent() {
		Panel pnl = new Panel("Filter");
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		comboProdi = new ComboBox("Program Studi", beanProdi);
		comboProdi.addValueChangeListener(new ValueChangeListener() {
			
			

			@Override
			public void valueChange(ValueChangeEvent event) {
				prodi = (ProgramStudi) event.getProperty().getValue();
				
			}
		});
		vl.addComponent(comboProdi);
		Button saring = new Button("Saring");
		saring.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saringData();
				
			}
		});
		vl.addComponent(saring);
		pnl.setContent(vl);
		return pnl;
	}

	protected void saringData() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);
		vl.removeAllComponents();
		List<KelasPerkuliahan> l = 
				KelasPerkuliahanPersistence.getKelasPerkuliahanByProdiSemester(prodi, semester, ta);
		final Table t = new Table();
		BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<>(KelasPerkuliahan.class);
		beans.setBeanIdProperty("id");
		if (l.size()>0){
			beans.addAll(l);
		} else {
		//	beans.addBean(new KelasPerkuliahan());
		}
		t.setSelectable(true);
		t.setMultiSelect(true);
		t.setMultiSelectMode(MultiSelectMode.SIMPLE);
		t.setContainerDataSource(beans);
		t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		t.setColumnHeader("prodi", "PROGRAM STUDI");
		t.setColumnHeader("dosenPengampu", "DOSEN");
		t.setColumnHeader("mataKuliah", "MATA KULIAH");
		t.setColumnHeader("kodeKelas", "KELAS");
		t.setVisibleColumns("dosenPengampu","mataKuliah","prodi","kodeKelas");
		vl.addComponent(t);
		Label label = new Label("Pilih kelas sebelum mencetak");
		Button cetak = new Button("Cetak Absensi");
		cetak.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Set<Integer> x = (Set<Integer>) t.getValue();
				List<KelasPerkuliahan> lkp = new ArrayList<>();
				if (x.size()>0){
					Iterator<Integer> it = x.iterator();
					while (it.hasNext()){
						BeanItem<KelasPerkuliahan> item = (BeanItem<KelasPerkuliahan>) t.getItem(it.next());
						KelasPerkuliahan kp = item.getBean();
						lkp.add(kp);
						//Notification.show(item.getBean().toString());
					}
					try {
						cetakAbsensi(lkp);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Notification.show("Create report failed",Notification.Type.ERROR_MESSAGE);
					}
				} else {
					Notification.show("Anda belum memilih kelas", Notification.Type.ERROR_MESSAGE);
				}
				
			}
		});
		vl.addComponents(label,cetak);
		content.setContent(vl);
	}

	protected void cetakAbsensi(List<KelasPerkuliahan> lkp) throws JRException {
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportAbsensiHarian(lkp);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Berita Acara Perkuliahan"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		getUI().getPage().open(resource, "_blank", false);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
