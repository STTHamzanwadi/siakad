package org.stth.siak.jee.ui.mahasiswa;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.util.PasswordEncryptionService;
import org.stth.siak.entity.Mahasiswa;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MahasiswaMenuComponent extends CustomComponent {

    private static final String STYLE_VISIBLE = "valo-menu-visible";
    public static final String NAME = "";
    //private Label notificationsBadge;
	private Mahasiswa mhs;

    public MahasiswaMenuComponent() {
        
    	addStyleName("valo-menu");
    	setSizeUndefined();
        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("STT <strong>Hamzanwadi</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
		mhs = VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
		System.out.println(mhs);
        final MenuItem settingsItem = settings.addItem(mhs.getNama(), new ThemeResource(
                "img/profile-pic-300px.jpg"), null);
        settingsItem.addItem("Ganti Password", new Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                spawnPasswordResetDialog();
            }
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                VaadinSession.getCurrent().setAttribute(Mahasiswa.class, null);
                MahasiswaUI ui = (MahasiswaUI) getUI();
                ui.updateContent();
            }
        });
        return settings;
    }

    protected void spawnPasswordResetDialog() {
		final Window gantiPassword = new Window("Ganti password");
		FormLayout fly = new FormLayout();
		final PasswordField tpass1 = new PasswordField("Password baru");
		final PasswordField tpass2 = new PasswordField("Ketik ulang password");
		Button b = new Button("Simpan", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (tpass1.getValue().equals(tpass2.getValue())){
					try {
						byte[] salt = PasswordEncryptionService.generateSalt();
						mhs.setSalt(salt);
						mhs.setPassword(PasswordEncryptionService.getEncryptedPassword(tpass1.getValue(), salt));
						GenericPersistence.merge(mhs);
						Notification.show("Penggantian password berhasil",Notification.Type.HUMANIZED_MESSAGE);
						gantiPassword.close();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidKeySpecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Notification.show("Password tidak match",Notification.Type.ERROR_MESSAGE);
				}
			}
		});
		gantiPassword.setModal(true);
		gantiPassword.setWidth("375px");
		fly.setMargin(true);
		gantiPassword.setContent(fly);
		fly.addComponents(tpass1,tpass2,b);
		gantiPassword.center();
		UI.getCurrent().addWindow(gantiPassword);
	}

	private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);
        
        for (final MahasiswaMenuItems view : MahasiswaMenuItems.values()) {
            Component menuItemComponent = new ValoMenuItemButton(view);
            menuItemsLayout.addComponent(menuItemComponent);
        }
        return menuItemsLayout;

    }

  

    @Override
    public void attach() {
        super.attach();
        //updateNotificationsCount(null);
    }

    
    public class ValoMenuItemButton extends Button {

        //private static final String STYLE_SELECTED = "selected";

        //private final MahasiswaMenuItems view;

        public ValoMenuItemButton(final MahasiswaMenuItems view) {
            //view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(view.getViewName().substring(0, 1).toUpperCase()
                    + view.getViewName().substring(1));
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    System.out.println(view.getViewName());
                	UI.getCurrent().getNavigator()
                            .navigateTo(view.getViewName());
                    
                }
            });
        }

        }
}
