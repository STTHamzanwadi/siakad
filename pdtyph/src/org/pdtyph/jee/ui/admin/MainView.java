package org.pdtyph.jee.ui.admin;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
    	
        setSizeFull();
        addStyleName("mainview");

        addComponent(new AdminMenuComponent());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new AdminNavigator(content);
    }
}
