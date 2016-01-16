package org.stth.siak.jee.ui.mahasiswa;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.MataKuliahRencanaStudi;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.helper.IndeksPrestasiHelper;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class IPKView extends Panel implements View {

    private Label titleLabel;
    private VerticalLayout dashboardPanels;
    private VerticalLayout root;
    private Table tableIPK = new Table("DAFTAR MATA KULIAH YANG SUDAH DITEMPUH");
	private Mahasiswa mhs;
	private IndeksPrestasiHelper iph;
	private BeanContainer<Integer, PesertaKuliah> beansPK = new BeanContainer<Integer, PesertaKuliah>(PesertaKuliah.class);

    
	public IPKView(Mahasiswa mhs) {
		this.mhs = mhs;
    	prepareView();
    }
	public IPKView(){
    	mhs = (Mahasiswa) VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
    	prepareView();
    }

	private void prepareView() {
		iph = new IndeksPrestasiHelper(mhs);
    	addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();


        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());
        

        Component content = buildContent2();
        root.addComponent(content);
        root.setExpandRatio(content, 1);
	}
	
	

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Indeks Prestasi Kumulatif");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    
    
    /*private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        List<PesertaKuliah> ls = iph.getNilaiClean();
        beansPK.setBeanIdProperty("id");
		beansPK.removeAllItems();
		beansPK.addAll(ls);
		tableIPK.setContainerDataSource(beansPK);
		tableIPK.setColumnHeader("copiedKodeMatkul", "KODE");
		tableIPK.setColumnHeader("copiedNamaMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("copiedSKSMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("nilai", "NILAI");
		tableIPK.setVisibleColumns("copiedKodeMatkul","copiedNamaMatkul","copiedSKSMatkul","nilai");
		dashboardPanels.addComponent(createContentWrapper(tableIPK));
        return dashboardPanels;
    }*/
    private Component buildContent2() {
        dashboardPanels = new VerticalLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        List<PesertaKuliah> ls = iph.getNilaiClean();
        beansPK.setBeanIdProperty("id");
		beansPK.removeAllItems();
		beansPK.addAll(ls);
		tableIPK.setContainerDataSource(beansPK);
		tableIPK.setColumnHeader("copiedKodeMatkul", "KODE");
		tableIPK.setColumnHeader("copiedNamaMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("copiedSKSMatkul", "MATA KULIAH");
		tableIPK.setColumnHeader("nilai", "NILAI");
		tableIPK.setVisibleColumns("copiedKodeMatkul","copiedNamaMatkul","copiedSKSMatkul","nilai");
		dashboardPanels.addComponent(tableIPK);
		Label ipk = new Label("Indeks Prestasi Kumulatif : "+iph.getIpk());
		Label totSks = new Label("Jumlah SKS yang sudah ditempuh : "+iph.getSKStotal());
		Label nilaiD = new Label("Jumlah SKS nilai D : "+ iph.getSKSD());
		dashboardPanels.addComponents(ipk,totSks,nilaiD);
        return dashboardPanels;
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {

            @Override
            public void menuSelected(final MenuItem selectedItem) {
                if (!slot.getStyleName().contains("max")) {
                    selectedItem.setIcon(FontAwesome.COMPRESS);
                    toggleMaximized(slot, true);
                } else {
                    slot.removeStyleName("max");
                    selectedItem.setIcon(FontAwesome.EXPAND);
                    toggleMaximized(slot, false);
                }
            }
        });
        max.setStyleName("icon-only");
        MenuItem root = tools.addItem("", FontAwesome.COG, null);
        root.addItem("Configure", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });
        root.addSeparator();
        root.addItem("Close", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return slot;
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

    
        @Override
    public void enter(ViewChangeEvent event) {
        
    }

}
