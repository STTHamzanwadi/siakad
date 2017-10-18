package org.stth.siak.jee.ui.administrasi;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.RiwayatPekerjaan;
import org.stth.siak.enumtype.StatusMahasiswa;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.NoPropertyGenerated;
import org.stth.siak.ui.util.StringPropertyGenerated;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;


import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;


public class AdministrasiAlumniMahasiswa extends VerticalLayout implements View{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Panel content = new Panel("Daftar Alumni Mahasiswa");
	private BeanItemContainer<ProgramStudi> beanProdi = new BeanItemContainer<>(ProgramStudi.class);
	private ComboBox cbProdi = new ComboBox("Pilih prodi");
	protected ProgramStudi prodi;
	private TextField nama = new TextField("Nama");
	private TextField nim = new TextField("NIM");
	private TextField angkatan = new TextField("Angkatan");
	private Button btnCari = new Button("Cari");
	private Mahasiswa m;
	private AutocompleteTextField atf;
	
	private List<Mahasiswa> l;
	private Map<String, Mahasiswa> map ;
	private List<String> col;

	public AdministrasiAlumniMahasiswa() {
		setMargin(true);
		setSpacing(true);
		HorizontalLayout hl = tambahAlumni();
		addComponent(ViewFactory.header("Administrasi Data Alumni"));
		addComponent(hl);
		addComponent(filterLulusan());
		addComponent(content);
		btnCari.addClickListener(click->cari());

	}
	private HorizontalLayout tambahAlumni() {
		atf = new AutocompleteTextField();
		atf.setWidth("400px");
		atf.setInputPrompt("inputkan nim atau nama mahasiswa yang masih aktif");
		addSuggesttoTextField();
		Button b = new Button("Tambah Alumni");
		b.addClickListener(klik->{
			Mahasiswa mSelected = map.get(atf.getValue());
			if (mSelected!=null) {
				AdministrasiEditorAlumni aa = new AdministrasiEditorAlumni(mSelected);
				getUI().addWindow(aa);
				aa.addCloseListener(close->{
					atf.clear();
					addSuggesttoTextField();
				});
			}else{
				Notification.show("inputkan mahasiswa atau nim di textfield", Notification.Type.WARNING_MESSAGE);
			}
			
		});
		
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponents(atf, b);
		return hl;
	}
	private void addSuggesttoTextField() {

		Mahasiswa example = new Mahasiswa();
		example.setStatus(StatusMahasiswa.AKTIF);
		l = MahasiswaPersistence.getListByExample(example);
		map = new HashMap<>();
		col = new ArrayList<>();
		for (Mahasiswa m : l) {
			col.add(m.toString());
			map.put(m.toString(), m);
		}
		System.out.println(l.size()+" "+col.size()+" "+map.size());
		AutocompleteSuggestionProvider sP = new CollectionSuggestionProvider(map.keySet(), MatchMode.CONTAINS, true);
		atf.setCache(true);
		atf.setDelay(500);
		atf.setMinChars(3);
		atf.setSuggestionProvider(sP);

	}
	private Component filterLulusan() {
		Panel pnl = new Panel("Filter");
		HorizontalLayout hl = new HorizontalLayout();
		hl.setMargin(true);
		hl.setSpacing(true);
		FormLayout flKiri = new FormLayout();
		FormLayout flKanan = new FormLayout();
		FormLayout flButton = new FormLayout();

		beanProdi.addAll(GenericPersistence.findList(ProgramStudi.class));
		cbProdi = new ComboBox("Program Studi", beanProdi);
		cbProdi.addValueChangeListener(e->{
			prodi = (ProgramStudi) e.getProperty().getValue();
		});
		flButton.addComponent(btnCari);
		flKiri.addComponents(nama, nim);
		flKanan.addComponents(angkatan, cbProdi);
		hl.addComponents(flKiri, flKanan, flButton);
		pnl.setContent(hl);
		return pnl;
	}
	protected void cari() {
		VerticalLayout vl = new VerticalLayout();

		Mahasiswa example = new Mahasiswa();
		example.setNama(nama.getValue());
		example.setProdi(prodi);
		example.setNpm(nim.getValue());
		example.setStatus(StatusMahasiswa.LULUS);
		if (!angkatan.getValue().isEmpty()) {
			example.setAngkatan(Integer.parseInt(angkatan.getValue()));
		}
		List<Mahasiswa> l = MahasiswaPersistence.getListByExample(example);
		BeanItemContainer<Mahasiswa> beans = new BeanItemContainer<Mahasiswa>(Mahasiswa.class);
		
		if (!l.isEmpty()) {
			beans.addAll(l);
		}
		GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(beans);
		gpc.addGeneratedProperty("AKSI", new StringPropertyGenerated("Edit"));
		gpc.addGeneratedProperty("Bekerja", new PropertyValueGenerator<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				String kerja = "Belum";
				BeanItem<?> i = (BeanItem<?>) item;
				Mahasiswa m = (Mahasiswa) i.getBean();
				List<Criterion> cr = new ArrayList<>();
				cr.add(Restrictions.eq("mahasiswa", m));
				List<RiwayatPekerjaan> lrp = GenericPersistence.findList(RiwayatPekerjaan.class, cr );
				if(lrp.size()>0){
					kerja="Sudah";
				}
				return kerja;
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		gpc.addGeneratedProperty("NO", new NoPropertyGenerated(beans)); 
		Grid g = new Grid(gpc);
		g.setWidth("800px");
		g.setCaption("Ditemukan "+ l.size()+ " mahasiswa");
		g.removeAllColumns();
		g.addColumn("NO");
		g.addColumn("nama").setHeaderCaption("NAMA");
		g.addColumn("prodi").setHeaderCaption("PRODI");
		g.addColumn("tempatLahir").setHeaderCaption("TEMPAT LAHIR");
		g.addColumn("tanggalLahir").setHeaderCaption("TGL LAHIR").setConverter(new StringToDateConverter(){
			private static final long serialVersionUID = -722166920049925960L;
			@Override
			public DateFormat getFormat(Locale locale){
				return new SimpleDateFormat("dd-MMM-yyyy");

			}
		});
		//g.addColumn("alamat").setHeaderCaption("ALAMAT");
		g.addColumn("Bekerja").setHeaderCaption("BEKERJA");
		g.addColumn("AKSI").setRenderer(new ButtonRenderer(click->{
			m= beans.getItem(click.getItemId()).getBean();
			AdministrasiEditorAlumni aa = new AdministrasiEditorAlumni(m);
			getUI().addWindow(aa);
			aa.addCloseListener(close->{
				addSuggesttoTextField();
			});
		}));
		vl.setMargin(true);
		vl.setSpacing(true);
		vl.addComponent(g);
		content.setContent(vl);

	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
