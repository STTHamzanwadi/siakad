package org.stth.siak.jee.ui.administrasi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.util.PasswordEncryptionService;
import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.entity.BerkasFotoDosen;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.UserAccessRightsAdministrasi;
import org.stth.siak.ui.util.MultiPurposeImageUploaderWindow;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
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
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class AdministrasiMenuComponent extends CustomComponent {

	private static final String STYLE_VISIBLE = "valo-menu-visible";
	public static final String NAME = "";
	//private Label notificationsBadge;
	private DosenKaryawan admUser;


	public AdministrasiMenuComponent() {
		addStyleName("valo-menu");
		setSizeUndefined();
		admUser = VaadinSession.getCurrent().getAttribute(DosenKaryawan.class);
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
		
		//System.out.println(dosen);
		final BerkasFotoDosen bfd = DosenKaryawanPersistence.getFotoDosen(admUser);
		Resource resource;
		if (bfd!=null) {
			StreamResource.StreamSource imageSource = new StreamResource.StreamSource() {
				@Override
				public InputStream getStream() {
					return new ByteArrayInputStream(bfd.getFile());
				}
			};
			resource = new StreamResource(imageSource, admUser.toString().trim()+".jpg");
		} else {
			resource = new ThemeResource(
					"img/usermale.png");
		}
		final MenuItem settingsItem = settings.addItem(admUser.getNama(), resource, null);
		settingsItem.addItem("Ganti Password", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				spawnPasswordResetDialog();

			}
		});
		settingsItem.addSeparator();
		settingsItem.addItem("Ganti Foto Profil", new Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				try {
					changeProfilePicture();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		settingsItem.addItem("Sign Out", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				VaadinSession.getCurrent().setAttribute(DosenKaryawan.class, null);
				AdministrasiUI ui = (AdministrasiUI) getUI();
				ui.updateContent();
			}
		});
		return settings;
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
		for (final AdministrasiMenuItems view : AdministrasiMenuItems.values()){
			Component menuItemComponent = new ValoMenuItemButton(view);
			menuItemsLayout.addComponent(menuItemComponent);
		}
		try {
			List<UserAccessRightsAdministrasi> lacl = (List<UserAccessRightsAdministrasi>) VaadinSession.getCurrent().getAttribute("admrights");
			for (final AdministrasiControlledAccessMenuItems view : AdministrasiControlledAccessMenuItems.values()){
				if(ACLAdministrasiEnum.isEligibleTo(lacl, view.getAccessControlList())){
					Component menuItemComponent = new ValoMenuItemButton(view);
					menuItemsLayout.addComponent(menuItemComponent);
				}
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		

		return menuItemsLayout;

	}


	@Override
	public void attach() {
		super.attach();
		//updateNotificationsCount(null);
	}


	public class ValoMenuItemButton extends Button {

		public ValoMenuItemButton(final AdministrasiMenuItems view) {
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
		public ValoMenuItemButton(final AdministrasiControlledAccessMenuItems view) {
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
		final PasswordField tpass1 = new PasswordField("Password baru");
		final PasswordField tpass2 = new PasswordField("Ketik ulang password");
		Button b = new Button("Simpan", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (tpass1.getValue().equals(tpass2.getValue())){
					try {
						byte[] salt = PasswordEncryptionService.generateSalt();
						admUser.setSalt(salt);
						admUser.setPassword(PasswordEncryptionService.getEncryptedPassword(tpass1.getValue(), salt));
						GenericPersistence.merge(admUser);
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
	
	protected void changeProfilePicture() throws IOException{
		final MultiPurposeImageUploaderWindow gantiFotoProfil = new MultiPurposeImageUploaderWindow(admUser);
		gantiFotoProfil.center();
		UI.getCurrent().addWindow(gantiFotoProfil);
		gantiFotoProfil.addCloseListener(new CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				try {
					if (gantiFotoProfil.getImage()!=null&&gantiFotoProfil.isGambarOK()){
						DosenKaryawanPersistence.updatePicture(admUser, gantiFotoProfil.getImage());
						UI.getCurrent().getPage().reload();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
	}

}
