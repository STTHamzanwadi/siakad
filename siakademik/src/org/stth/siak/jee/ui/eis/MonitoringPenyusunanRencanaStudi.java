package org.stth.siak.jee.ui.eis;


import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.helper.MonevKRS;
import org.stth.siak.helper.MonevKRS.RekapMonevKRS;
import org.stth.siak.jee.ui.generalview.ViewFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MonitoringPenyusunanRencanaStudi extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5660284900239030833L;
	private BeanItemContainer<RekapMonevKRS> beanRekap = new BeanItemContainer<>(RekapMonevKRS.class);
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);	
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	private VerticalLayout content = new VerticalLayout();
	private Semester semester;
	private String ta;
	protected ProgramStudi prodi;
	private List<RekapMonevKRS> rekap;
	private Table tabel;
	private TextField tfa;
	
	public MonitoringPenyusunanRencanaStudi() {
		setMargin(true);
		setSpacing(true);
		Responsive.makeResponsive(this);
		
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getKRSSemester();
		ta = k.getKRSTa();
		
		addComponent(ViewFactory.header("Monitoring Penyusunan Rencana Studi semester "+semester+" t.a "+ ta));
		addComponent(createFilterComponent());
		siapkanPilihanProdi();
		addComponent(content);
		addComponent(ViewFactory.footer());

	}
	
	private void siapkanPilihanProdi(){
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setContainerDataSource(beanProdi); 
		//cbProdi.setNullSelectionAllowed(false);
		//cbProdi.setTextInputAllowed(false);
		cbProdi.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				prodi = (ProgramStudi) event.getProperty().getValue();
				
			}
		});
	}
	
	private Component createFilterComponent(){
		Panel pnl = new Panel("Pilih program studi dan angkatan");
		GridLayout gl = new GridLayout(2, 1);
		FormLayout fl1 = new FormLayout();
		FormLayout fl2 = new FormLayout();
		fl1.addComponent(cbProdi);
		tfa = new TextField("angkatan");
		fl2.addComponent(tfa);
		Button buttonProses = new Button("Lakukan Kalkulasi");
		buttonProses.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				prepareContent();
				
			}
		});
		fl2.addComponent(buttonProses);
		gl.addComponent(fl1);
		gl.addComponent(fl2);
		gl.setSpacing(true);
		gl.setMargin(true);
		pnl.setContent(gl);
		return pnl;
		
	}
	
	public void prepareContent(){
		MonevKRS m = new MonevKRS(semester,ta,Integer.valueOf(tfa.getValue()),prodi);
		rekap = m.getRekap();
		beanRekap.removeAllItems();
		beanRekap.addAll(rekap);
		content.removeAllComponents();
		Panel p = new Panel("Rekapitulasi Status Penyusunan Rencana Studi");
		
		tabel = new Table("",beanRekap);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.addGeneratedColumn("prodi", new ColumnGenerator() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final RekapMonevKRS r = (RekapMonevKRS) i.getBean();
				return r.getProdi().getNama();
			}
		});
		tabel.setColumnHeader("prodi", "PROGRAM STUDI");
		tabel.setColumnHeader("belumSusun", "BELUM SUSUN");
		tabel.setColumnHeader("draft", "DRAFT");
		tabel.setColumnHeader("diajukan", "DIAJUKAN");
		tabel.setColumnHeader("disetujui", "DISETUJUI");
		tabel.setColumnHeader("ditolak", "DITOLAK");
		tabel.setColumnHeader("finaL", "FINAL");
		tabel.setColumnHeader("dosenPa", "DOSEN P.A");
		tabel.setVisibleColumns("prodi","dosenPa","belumSusun","draft","diajukan","disetujui","ditolak","finaL");
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.addComponent(tabel);
		p.setContent(vl);
		content.setSpacing(true);
		//content.setMargin(true);
		content.addComponent(p);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	
	

}
