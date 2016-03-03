package org.pdtyph.jee.ui.opryayasan;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.pdtyph.cummon.ValidatorFields;
import org.pdtyph.entity.Instansi;
import org.pdtyph.entity.UserOprYayasan;
import org.yph.jee.persistence.GenericPersistence;
import org.yph.jee.util.PasswordEncryptionService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class TambahUserYayasan extends  CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8265667895917038873L;
	private Label titleLabel;
	private VerticalLayout root;
	private BeanItem<UserOprYayasan> item;
	private FieldGroup fieldGroup;
	private Window parent;
	private UserOprYayasan opr;
	private PasswordField tpass1;
	private PasswordField tpass2;
	@SuppressWarnings("serial")
	public TambahUserYayasan(UserOprYayasan i, Window parent) {
		addStyleName(ValoTheme.PANEL_BORDERLESS);
		this.parent = parent;
		root = new VerticalLayout();
		root.setSizeFull();
		root.setMargin(true);
		root.addStyleName("dashboard-view");
		setCompositionRoot(root);
		Responsive.makeResponsive(root);
		item=new BeanItem<UserOprYayasan>(i);
		root.addComponent(buildForm());
		Button simpan=new Button("Simpan",FontAwesome.SAVE);
		root.addComponent(simpan);
		simpan.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		simpan.addClickListener(new ClickListener() {
			@Override public void buttonClick(ClickEvent event) {
				simpanUser();
			}

		});
	}
	private Component buildForm() {
		VerticalLayout pnl = new VerticalLayout();

		FormLayout fly = new FormLayout();
		fieldGroup = new FieldGroup(item);
		TextField txtUser=new TextField("Nama Pengguna");
		txtUser.setWidth("300px");
		fieldGroup.bind(txtUser, "userName");
		fly.addComponent(txtUser);
		
		TextField txtAlias=new TextField("Nama Pengguna");
		txtAlias.setWidth("300px");
		fieldGroup.bind(txtAlias, "nama");
		fly.addComponent(txtAlias);
		TextField txtNama=new TextField("Nama Lengkap");
		txtNama.setWidth("300px");
		fieldGroup.bind(txtNama, "realName");
		fly.addComponent(txtNama);
		fly.addComponent(fieldGroup.buildAndBind("Tanggal Registrasi", "registerDate"));
		TextField txtEmail=new TextField("e-Mail");
		txtEmail.setWidth("300px");
		fieldGroup.bind(txtEmail, "email");
		fly.addComponent(txtEmail);
		fieldGroup.getField("email").setRequired(true);
		fieldGroup.getField("email").addValidator(new EmailValidator("e-mail invalid"));
		tpass1 = new PasswordField("Password");
		tpass2 = new PasswordField("Ketik ulang password");
		fly.addComponent(tpass1);
		fly.addComponent(tpass2);
		pnl.addComponent(fly);
		return pnl;
	}
	private void simpanUser() {
		try {
			fieldGroup.commit();
			BeanItem bi = (BeanItem) fieldGroup.getItemDataSource();
			UserOprYayasan u =  (UserOprYayasan) bi.getBean();
			if (!tpass1.getValue().isEmpty()) {
				if (tpass1.getValue().equals(tpass2.getValue())) {
					try {
						byte[] salt = PasswordEncryptionService.generateSalt();
						u.setSalt(salt);
						u.setPassword(PasswordEncryptionService
								.getEncryptedPassword(tpass1.getValue(), salt));

					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidKeySpecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Notification.show("Password tidak match",
							Notification.Type.ERROR_MESSAGE);
					return;
				}
			}
			GenericPersistence.merge(u);
			
		} catch (CommitException e) {
		
			e.printStackTrace();
		}
	}



}
