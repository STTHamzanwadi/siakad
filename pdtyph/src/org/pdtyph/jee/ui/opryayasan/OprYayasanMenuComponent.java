package org.pdtyph.jee.ui.opryayasan;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.pdtyph.entity.Pegawai;
import org.pdtyph.entity.UserOprYayasan;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.util.PasswordEncryptionService;

import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class OprYayasanMenuComponent extends CustomComponent {

	private static final String STYLE_VISIBLE = "valo-menu-visible";
	public static final String NAME = "";
	private Label notificationsBadge;
	private UserOprYayasan opr;
	
	public OprYayasanMenuComponent() {
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
	private Component buildUserMenu() {
		UserOprYayasan user=(UserOprYayasan)VaadinSession.getCurrent().getAttribute(UserOprYayasan.class);
		 final MenuBar settings = new MenuBar();
	        settings.addStyleName("user-menu");
	        settings.addStyleName(ValoTheme.MENUBAR_SMALL);
			opr = VaadinSession.getCurrent().getAttribute(UserOprYayasan.class);
			System.out.println(opr);
	        String basepath = VaadinService.getCurrent()
					.getBaseDirectory().getAbsolutePath();
			FileResource resource = new FileResource(new File(basepath +"/WEB-INF/img/yayasan.jpg"));
	        final MenuItem settingsItem = settings.addItem(user.getRealName(), resource,null);
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
	                VaadinSession.getCurrent().setAttribute(UserOprYayasan.class, null);
	                MainUI  ui = (MainUI) getUI();
	                ui.updateContent();
	            }
	        });
	        return settings;
	}
	private Component buildTitle() {
		Label logo = new Label("<strong>PDT YPH PPD NW PANCOR</strong>",
				ContentMode.HTML);
		logo.setSizeUndefined();
		
		HorizontalLayout logoWrapper = new HorizontalLayout(logo);
		logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_RIGHT);
		logoWrapper.addStyleName(ValoTheme.LABEL_BOLD);
		logoWrapper.addStyleName(ValoTheme.LABEL_H2);
		logoWrapper.addStyleName(ValoTheme.LABEL_SPINNER);
		logoWrapper.addStyleName("valo-menu-title");
		return logoWrapper;
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
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		return valoMenuToggleButton;
	}



	private Component buildBadgeWrapper(Component menuItemButton,
			Component badgeLabel) {
		CssLayout dashboardWrapper = new CssLayout(menuItemButton);
		dashboardWrapper.setSizeFull();
		dashboardWrapper.addStyleName("badgewrapper");
		dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
		dashboardWrapper.setWidth(100.0f, Unit.PERCENTAGE);
		badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
		badgeLabel.setWidthUndefined();
		badgeLabel.setVisible(false);
		dashboardWrapper.addComponent(badgeLabel);
		return dashboardWrapper;
	}

	@Override
	public void attach() {
		super.attach();
		//updateNotificationsCount(null);
	}
	private Component buildMenuItems() {
		CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");
		menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);

		for (final OprYayasanMenuItems view : OprYayasanMenuItems.values()) {
			Component menuItemComponent = new ValoMenuItemButton(view);


			// if (view == MenuItems.DASHBOARD) {
			notificationsBadge = new Label();
			menuItemComponent = buildBadgeWrapper(menuItemComponent,
					notificationsBadge);
			//}

			menuItemsLayout.addComponent(menuItemComponent);
		}
		return menuItemsLayout;

	}
	

	public class ValoMenuItemButton extends Button {

		public ValoMenuItemButton(final OprYayasanMenuItems view) {
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

	 protected void spawnPasswordResetDialog() {
		final Window gantiPassword = new Window("Ganti password");
		FormLayout fly = new FormLayout();
		final PasswordField usr1 = new PasswordField("Username baru");
		final PasswordField usr2 = new PasswordField("Ketik ulang Username");
		final PasswordField tpass1 = new PasswordField("Password baru");
		final PasswordField tpass2 = new PasswordField("Ketik ulang password");
		Button b = new Button("Simpan", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (tpass1.getValue().equals(tpass2.getValue()) && usr1.getValue().equals(usr2.getValue())){
					try {
						byte[] salt = PasswordEncryptionService.generateSalt();
						opr.setSalt(salt);
						opr.setPassword(PasswordEncryptionService.getEncryptedPassword(tpass1.getValue(), salt));
						GenericPersistence.merge(opr);
						Notification.show("Penggantian Username password berhasil",Notification.Type.HUMANIZED_MESSAGE);
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

}
