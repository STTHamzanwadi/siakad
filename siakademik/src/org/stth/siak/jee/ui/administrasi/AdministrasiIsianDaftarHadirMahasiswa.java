package org.stth.siak.jee.ui.administrasi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;

public class AdministrasiIsianDaftarHadirMahasiswa extends CustomComponent{

	private static final long serialVersionUID = -6821145374499236035L;
	private KelasPerkuliahan kelasPerkuliahan;
	private LogPerkuliahan logPerkuliahan;
	private List<LogKehadiranPesertaKuliah> l;
	private Window parent;
	private Table t;
	private List<PesertaKuliah> lpk;
	private Map<Mahasiswa, Mahasiswa> mapPesertaAbsen;

	public AdministrasiIsianDaftarHadirMahasiswa(LogPerkuliahan log, Window parent) {
		this.parent = parent;
		this.logPerkuliahan = log;
		this.kelasPerkuliahan = log.getKelasPerkuliahan();
		lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kelasPerkuliahan);
		loadDaftarHadir();
		siapkanForm();
	}

	private void siapkanForm() {
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		Panel pnl = new Panel("Daftar Hadir");
		Label identitas = new Label();
		identitas.setValue(logPerkuliahan.getLongDesc());
		vl.addComponent(identitas);
		Map<String, PesertaKuliah> MapMhs = new HashMap<>();
		for (PesertaKuliah pk : lpk) {
			MapMhs.put(pk.getMahasiswa().toString(), pk);
		}
		
		AutocompleteSuggestionProvider acsp = new CollectionSuggestionProvider(MapMhs.keySet(), MatchMode.CONTAINS, true);
		HorizontalLayout hlAdd=new HorizontalLayout();
		AutocompleteTextField atfMhsKelas = new AutocompleteTextField();
		atfMhsKelas.setSuggestionProvider(acsp);
		atfMhsKelas.setInputPrompt("Inputkan nama atau nim Mahasiswa");
		atfMhsKelas.setWidth("250px");
		atfMhsKelas.setMinChars(3);
		Button bAdd = new Button();
		bAdd.setIcon(FontAwesome.PLUS);
		bAdd.addClickListener(e->{
			PesertaKuliah ps = MapMhs.get(atfMhsKelas.getValue());
			if (ps!=null) {
				Mahasiswa mPeserta = ps.getMahasiswa();
				Mahasiswa mExist = mapPesertaAbsen.get(mPeserta);
				if (mExist!=null) {
					Notification.show("mahasiswa ini sudah ada", Type.ERROR_MESSAGE);
				}else{
					LogKehadiranPesertaKuliah lhpk = new LogKehadiranPesertaKuliah(logPerkuliahan, ps);
					l.add(lhpk);
					listMhsAbsen();
					//loadDaftarHadir();
					pnl.setContent(getTable());
				}
			}
			
		});
		hlAdd.setSpacing(true);
		hlAdd.addComponents(atfMhsKelas, bAdd);
		
		pnl.setContent(getTable());
		vl.addComponent(hlAdd);
		vl.addComponent(pnl);
		Button simpan = new Button("Simpan");
		simpan.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Collection<?> ids = t.getContainerDataSource().getItemIds();
				for (Object component : ids) {
					BeanItem<?> item = (BeanItem<?>) t.getContainerDataSource().getItem(component);
					LogKehadiranPesertaKuliah lkp = (LogKehadiranPesertaKuliah) item.getBean();
					//Notification.show(lkp.toString());
					GenericPersistence.merge(lkp);
					
				}
				parent.close();
			}
		});
		vl.addComponent(simpan);
		setCompositionRoot(vl);
	}

	private Component getTable() {
		t = new Table();
		t.setSizeFull();
		BeanContainer<Integer, LogKehadiranPesertaKuliah> beans = new BeanContainer<>(LogKehadiranPesertaKuliah.class);
		beans.setBeanIdProperty("mahasiswa");
		if (l.size()>0){
			beans.addAll(l);
		} 
//		else {
//			beans.addBean(new LogKehadiranPesertaKuliah());
//		}		
		t.setContainerDataSource(beans);
		t.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		t.addGeneratedColumn("nim", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				LogKehadiranPesertaKuliah lpk = (LogKehadiranPesertaKuliah) i.getBean();
				Label l = new Label();
				l.setValue(lpk.getMahasiswa().getNpm());
				return l;
			}
		});
		t.addGeneratedColumn("nama", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				LogKehadiranPesertaKuliah lpk = (LogKehadiranPesertaKuliah) i.getBean();
				Label l = new Label();
				l.setValue(lpk.getMahasiswa().getNama());
				return l;
			}
		});

		t.addGeneratedColumn("hadir", new ColumnGenerator() {

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final LogKehadiranPesertaKuliah lpk = (LogKehadiranPesertaKuliah) i.getBean();
				CheckBox isHadir= new CheckBox();
				isHadir.setValue(lpk.isHadir());
				isHadir.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						boolean b = (boolean) event.getProperty().getValue();
						lpk.setHadir(b);
					}
				});
				return isHadir;
			}
		});
		
		t.setColumnHeader("nim", "NIM");
		t.setColumnHeader("nama", "NAMA");
		t.setColumnHeader("hadir", "HADIR");
		t.setVisibleColumns("nim","nama","hadir");
		return t;
	}

	private void loadDaftarHadir() {
		l = LogKehadiranPerkuliahanPersistence.getByLogPerkuliahan(logPerkuliahan);
		System.out.println(l);
		if (l.size()==0){
			//List<PesertaKuliah> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kelasPerkuliahan);
			int i=1;
			for (PesertaKuliah pesertaKuliah : lpk) {
				System.out.println(i);
				LogKehadiranPesertaKuliah lkpk = new LogKehadiranPesertaKuliah(logPerkuliahan, pesertaKuliah);
				l.add(lkpk);i++;
			}
			System.out.println(lpk.size());
		}
		listMhsAbsen();

	}

	private void listMhsAbsen() {
		mapPesertaAbsen = new HashMap<>();
		for (LogKehadiranPesertaKuliah lhpk : l) {
			mapPesertaAbsen.put(lhpk.getMahasiswa(), lhpk.getMahasiswa());
		}
	}
}
