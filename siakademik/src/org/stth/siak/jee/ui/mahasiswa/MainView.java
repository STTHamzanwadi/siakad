package org.stth.siak.jee.ui.mahasiswa;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
    	setSizeFull();
        addStyleName("mainview");

        addComponent(new MahasiswaMenuComponent());
        
        ComponentContainer content = new CssLayout();
        content.setPrimaryStyleName("valo-content");
        content.addStyleName("v-scrollable");
        content.setSizeFull();
       
        addComponent(content);
        setExpandRatio(content, 1.0f);
        
        new MahasiswaNavigator(content);
    }
}
