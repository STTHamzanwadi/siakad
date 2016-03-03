package org.pdtyph.jee.ui.opryayasan;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
    	
        setSizeFull();
        addStyleName("mainview");

        addComponent(new OprYayasanMenuComponent());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new OprYayasanNavigator(content);
    }
}
