package org.stth.siak.jee.ui.mahasiswa;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
    	setSizeFull();
    	//setHeight("100%");
        addStyleName("mainview");
        Component menu = new MahasiswaMenuComponent();
        addComponent(menu);
        menu.setHeight("100%");
        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1f);
        new MahasiswaNavigator(content);
    }
}
