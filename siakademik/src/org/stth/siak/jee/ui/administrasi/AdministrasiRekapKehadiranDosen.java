// 
// Decompiled by Procyon v0.5.30
// 

package org.stth.siak.jee.ui.administrasi;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.data.Container;
import com.vaadin.data.Item;

import java.util.Collection;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

import org.stth.siak.helper.MonevKehadiranDosen;
import org.stth.siak.helper.MonevKehadiranDosen.RekapKehadiranDosen;
import org.stth.siak.util.GeneralUtilities;
import com.vaadin.ui.Button;
import java.util.Date;
import java.util.Calendar;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import com.vaadin.server.VaadinSession;
import org.stth.jee.persistence.KonfigurasiPersistence;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import org.stth.siak.entity.UserAccessRightsAdministrasi;
import java.util.List;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.DosenKaryawan;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.DateField;
import org.stth.siak.enumtype.Semester;
import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AdministrasiRekapKehadiranDosen extends VerticalLayout implements View
{
	private Semester semester;
	private String ta;
	private DateField periodStart;
	private DateField periodEnd;
	private BeanItemContainer<DosenKaryawan> beanDosen;
	private BeanItemContainer<KelasPerkuliahan> beanKelasPerkuliahan;
	private VerticalLayout content;
	protected DosenKaryawan dosen;
	protected KelasPerkuliahan kelasPerkuliahan;
	private List<UserAccessRightsAdministrasi> lacl;

	public AdministrasiRekapKehadiranDosen() {
		this.periodStart = new DateField("Periode Mulai");
		this.periodEnd = new DateField("Periode End");
		this.beanDosen = new BeanItemContainer((Class)DosenKaryawan.class);
		this.beanKelasPerkuliahan = new BeanItemContainer((Class)KelasPerkuliahan.class);
		this.content = new VerticalLayout();
		this.setMargin(true);
		this.setSpacing(true);
		Responsive.makeResponsive(new Component[] { this });
		KonfigurasiPersistence k = new KonfigurasiPersistence();
		this.semester = k.getKRSSemester();
		this.ta = k.getKRSTa();
		this.lacl = (List)VaadinSession.getCurrent().getAttribute("admrights");
		this.addComponent(ViewFactory.header("Catatan Perkuliahan Semester " + this.semester + " t.a " + this.ta));
		this.addComponent(this.createFilterComponent());
		this.addComponent((Component)this.content);
		this.addComponent(ViewFactory.footer());
	}

	private Component createFilterComponent() {
		Panel pnl = new Panel("Filter Data");
		VerticalLayout vl = new VerticalLayout();
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(5, -1);
		//this.periodStart.setValue((Object)c.getTime());
		this.periodStart.setValue(c.getTime());
		c.add(5, 2);
		this.periodEnd.setValue(c.getTime());
		Button saring = new Button("Saring");
		saring.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				saringData();

			}
		});
		hl.addComponents(new Component[] { this.periodStart, this.periodEnd });
		vl.addComponents(new Component[] { hl, saring });
		vl.setSpacing(true);
		vl.setMargin(true);
		pnl.setContent((Component)vl);
		return (Component)pnl;
	}

	protected void saringData() {
		Date dt1 = GeneralUtilities.truncateDate((Date)this.periodStart.getValue());
		Date dt2 = GeneralUtilities.truncateNextDay((Date)this.periodEnd.getValue());
		MonevKehadiranDosen mnv = new MonevKehadiranDosen(dt1, dt2);
		List<MonevKehadiranDosen.RekapKehadiranDosen> l = mnv.getRekap();
		this.content.removeAllComponents();
		Table t = new Table();
		BeanContainer<Integer, MonevKehadiranDosen.RekapKehadiranDosen> beans = (BeanContainer<Integer, 
				MonevKehadiranDosen.RekapKehadiranDosen>)new BeanContainer((Class)MonevKehadiranDosen.RekapKehadiranDosen.class);
		beans.setBeanIdProperty((Object)"dosen");
		if (l.size() > 0) {
			beans.addAll((Collection)l);
		}
		else {
			beans.addBean(mnv.new RekapKehadiranDosen());
		}
		t.setContainerDataSource((Container)beans);
		t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		t.setColumnHeader("dosen", "DOSEN");
		t.addGeneratedColumn("total", new ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem item = (BeanItem) source.getItem(itemId);
				MonevKehadiranDosen.RekapKehadiranDosen b = (RekapKehadiranDosen) item.getBean();
				//MonevKehadiranDosen.RekapKehadiranDosen b = (RekapKehadiranDosen) item.getItemProperty(columnId).getValue();
				if (!b.getLogHadir().isEmpty()){
					return new Label(String.valueOf(b.getLogHadir().size()));
				}
				return null;
				
			}
		});
		t.setVisibleColumns(new Object[] { "dosen", "total"});
		t.setSizeUndefined();
		Panel pnl = new Panel("Daftar Hasil Penyaringan");
		VerticalLayout pnlroot = new VerticalLayout();
		pnlroot.setSizeUndefined();
		pnlroot.setSpacing(true);
		pnlroot.addComponent((Component)t);
		pnlroot.setMargin(true);
		pnl.setContent((Component)pnlroot);
		this.content.addComponent((Component)pnl);
	}

	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}
}
