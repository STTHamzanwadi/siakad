package org.stth.siak.jee.ui.dosen;


import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.jee.genericview.KurikulumView;
import org.stth.siak.jee.genericview.ViewFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DosenDetailKurikulum extends VerticalLayout implements View{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1653775031486924211L;
	private Kurikulum kr;
	private ProgramStudi prodi;
	private VerticalLayout dashboardPanels;
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private BeanItemContainer<Kurikulum> beanKur = new BeanItemContainer<>(Kurikulum.class);
	private BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<Integer, KelasPerkuliahan>(KelasPerkuliahan.class);
	private DosenKaryawan dosen;
	private FormLayout fl;
	private VerticalLayout content = new VerticalLayout();
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	private ComboBox cbKur = new ComboBox("Pilih Kurikulum");

	public DosenDetailKurikulum() {
		//System.out.println("numpang lewat");
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		dosen = (DosenKaryawan) VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
		setMargin(true);
		Responsive.makeResponsive(this);
		addComponent(ViewFactory.header("Detail Kurikulum"));
		addComponent(siapkanComboProdi());
		addComponent(content);
		//addComponent(new KurikulumView(kr));
		addComponent(ViewFactory.footer());

	}

	

	private Component siapkanComboProdi() {
		fl = new FormLayout();
		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		cbProdi.setContainerDataSource(beanProdi); 
		cbProdi.setNullSelectionAllowed(false);
		cbProdi.setTextInputAllowed(false);
		cbProdi.addValueChangeListener(new ValueChangeListener() {
			

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Notification.show(event.getProperty().getValue()+" selected");
				prodi = (ProgramStudi) event.getProperty().getValue();
				siapkanComboKurikulum();
			}
		});
		fl.addComponent(cbProdi);
		fl.addComponent(cbKur);
		return fl;
	}



	protected void siapkanComboKurikulum() {
		beanKur.removeAllItems();
		beanKur.addAll(KurikulumPersistence.getListByProdi(prodi));
		cbKur.setContainerDataSource(beanKur);
		cbKur.setNullSelectionAllowed(false);
		cbKur.setTextInputAllowed(false);
		cbKur.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Notification.show(event.getProperty().getValue()+" selected");
				kr = (Kurikulum) event.getProperty().getValue();
				content.removeAllComponents();
				KurikulumView kv = new KurikulumView(kr);
				content.addComponent(kv);
			}
		});
		
		
	}



	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	

		

}
