package org.stth.siak.jee.ui.dosen;

import java.util.Collection;
import java.util.List;

import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.LogKehadiranPesertaKuliah;
import org.stth.siak.entity.LogPerkuliahan;
import org.stth.siak.entity.PesertaKuliah;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DosenDaftarHadirMahasiswa extends CustomComponent{
	private KelasPerkuliahan kelasPerkuliahan;
	private LogPerkuliahan logPerkuliahan;
	private List<LogKehadiranPesertaKuliah> l;
	private Table t;

	public DosenDaftarHadirMahasiswa(LogPerkuliahan log) {
		this.logPerkuliahan = log;
		this.kelasPerkuliahan = log.getKelasPerkuliahan();
		loadDaftarHadir();
		siapkanForm();
	}

	private void siapkanForm() {
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		Label identitas = new Label();
		identitas.setValue(logPerkuliahan.getLongDesc());
		vl.addComponent(identitas);
		Panel pnl = new Panel("Daftar Hadir");
		pnl.setContent(getTable());
		vl.addComponent(pnl);
		setCompositionRoot(vl);
	}

	private Component getTable() {
		t = new Table();
		BeanContainer<Integer, LogKehadiranPesertaKuliah> beans = new BeanContainer<>(LogKehadiranPesertaKuliah.class);
		beans.setBeanIdProperty("mahasiswa");
		if (l.size()>0){
			beans.addAll(l);
		} else {
			beans.addBean(new LogKehadiranPesertaKuliah());
		}		
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
				Label hadir = new Label();
				if (lpk.isHadir()){
					hadir.setValue("Hadir");
				}
				return hadir;
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
		if (l.size()==0){
			List<PesertaKuliah> lpk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahan(kelasPerkuliahan);
			for (PesertaKuliah pesertaKuliah : lpk) {
				LogKehadiranPesertaKuliah lkpk = new LogKehadiranPesertaKuliah(logPerkuliahan, pesertaKuliah);
				l.add(lkpk);
			}
		}

	}
}
