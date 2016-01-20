package org.stth.siak.jee.ui.dosen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.stth.siak.jee.ui.eis.ControlledAccessMenuItems;
import org.stth.siak.ui.util.AppMenuItems;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class DosenNavigator extends Navigator {

    
    private final static DosenMenuItems ERROR_VIEW = DosenMenuItems.DASHBOARD;
    private ViewProvider errorViewProvider;

    public DosenNavigator(ComponentContainer container) {
        super(UI.getCurrent(), container);

        initViewChangeListener();
        initViewProviders();

    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                for (DosenMenuItems view : DosenMenuItems.values()) {
                    if (view.getViewName().equals(event.getViewName())) {
                      
                        break;
                    }
                }

               
            }
        });
    }

   

    private void initViewProviders() {
        //List<AppMenuItems> ls = new ArrayList<>();
    	AppMenuItems[] y = DosenMenuItems.values(), z = ControlledAccessMenuItems.values();
    	List<AppMenuItems> l = new ArrayList<>();
    	l.addAll(Arrays.asList(y));
    	l.addAll(Arrays.asList(z));
    	//AppMenuItems[]  x = ArrayUtils.addAll(y,z);
    	for (final AppMenuItems view : l) {
            ViewProvider viewProvider = new ClassBasedViewProvider(
                    view.getViewName(), view.getViewClass()) {
                private View cachedInstance;
    
                @Override
                public View getView(String viewName) {
                    View result = null;
                    if (view.getViewName().equals(viewName)) {
                        if (view.isStateful()) {
                            // Stateful views get lazily instantiated
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(view
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            // Non-stateful views get instantiated every time
                            // they're navigated to
                            result = super.getView(view.getViewName());
                        }
                    }
                    return result;
                }
            };
    
            if (view == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }
    
            addProvider(viewProvider);
        }
        
    
        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }
    
            @Override
            public View getView(String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
