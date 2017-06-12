package org.stth.siak.jee.ui.administrasi;

import org.stth.siak.util.GeneralUtilities;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

public class AdministrasiPopupTanggalLulus extends Window {
	private static final long serialVersionUID = 2319062893834904563L;
	private String[] l = new String[2];
	private DateField tanggalLulus = new DateField("Tanggal Lulus");
	private DateField tanggalTranskrip = new DateField("Tanggal Transkrip");

	public AdministrasiPopupTanggalLulus() {
		FormLayout fl= new FormLayout();
		Button ok = new Button("OK");
		ok.addClickListener(e-> {
			String lulus ="31 Januari 2017";
			String trans ="1 April 2017";
			if (tanggalLulus!=null) {
				lulus=GeneralUtilities.getLongFormattedDate(tanggalLulus.getValue());
			}
			if (tanggalTranskrip!=null) {
				trans=GeneralUtilities.getLongFormattedDate(tanggalTranskrip.getValue());
			}
			l[0]=(lulus);
			l[1]=(trans);
			setData(l);
			close();
		});
		fl.addComponents(tanggalLulus, tanggalTranskrip, ok);
		fl.setMargin(true);
		setWidth("480px");
		setCaption("Tanggal Lulus dan Tanggal Transkrip");
		center();
		setContent(fl);

	}
}
