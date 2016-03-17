package org.stth.siak.jee.ui.mahasiswa;

import java.util.Collections;
import java.util.List;

import org.stth.jee.persistence.JadwalKuliahPersistence;
import org.stth.jee.persistence.KelasPerkuliahanPersistence;
import org.stth.jee.persistence.KonfigurasiPersistence;
import org.stth.jee.persistence.LogKehadiranPerkuliahanPersistence;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import org.stth.jee.persistence.PesertaKuliahPersistence;
import org.stth.siak.entity.JadwalKuliah;
import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.PesertaKuliah;
import org.stth.siak.enumtype.Semester;
import org.stth.siak.jee.ui.generalview.DaftarLogPerkuliahan;
import org.stth.siak.jee.ui.generalview.ViewFactory;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.ColumnGenerator;

public class MahasiswaKelasDiikuti extends VerticalLayout implements View{
	
	
	
	private Mahasiswa mhs;
	private Semester semester;
	private String ta;
	private List<KelasPerkuliahan> lkp;
	private BeanContainer<Integer, KelasPerkuliahan> beans = new BeanContainer<Integer, KelasPerkuliahan>(KelasPerkuliahan.class);

	public MahasiswaKelasDiikuti(){
		mhs = (Mahasiswa) VaadinSession.getCurrent().getAttribute(Mahasiswa.class);
    	KonfigurasiPersistence k = new KonfigurasiPersistence();
		semester = k.getCurrentSemester();
		ta = k.getCurrentTa();
    	setMargin(true);
        Responsive.makeResponsive(this);
        addComponent(ViewFactory.header("Kelas Perkuliahan Anda Semester "+semester+ " T.A "+ta));
        Component content = buildContent2();
        addComponent(content);
        addComponent(ViewFactory.footer());
		
	}
	
	private Component buildContent2(){
		Panel pnl = new Panel("Daftar Mata Kuliah");
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSpacing(true);
		lkp = KelasPerkuliahanPersistence.getKelasPerkuliahanMahasiswaSemester(mhs, semester, ta);
		Table tabel = new Table();
		Collections.sort(lkp);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (lkp!=null){
			beans.addAll(lkp);
		} else {
			beans.addBean(new KelasPerkuliahan());
		}
		tabel.setContainerDataSource(beans);
		tabel.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		tabel.addGeneratedColumn("matakuliah", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getMataKuliah().toString();
			}
		});
		tabel.addGeneratedColumn("sks", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				return o.getMataKuliah().getSks();
			}
		});
		tabel.addGeneratedColumn("jadwal", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				List<JadwalKuliah> l = JadwalKuliahPersistence.getJadwalByKelasPerkuliahan(o);
				if (l.size()>0){
					String s="";
					for (JadwalKuliah j : l) {
						s = s + j.getHariJam() + " ";
					}
				}
				return "-";
			}
		});
		tabel.addGeneratedColumn("kehadiran", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				PesertaKuliah pk = PesertaKuliahPersistence.getPesertaKuliahByKelasPerkuliahanMahasiswa(o, mhs);
				int pertemuan = LogPerkuliahanPersistence.getByKelas(o).size();
				int kehadiran = LogKehadiranPerkuliahanPersistence.getByPesertaKuliah(pk).size();
				String ratio = kehadiran +" dari " +pertemuan;
				return ratio;
			}
		});
		tabel.addGeneratedColumn("log", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button log = new Button("Lihat");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final KelasPerkuliahan o = (KelasPerkuliahan) i.getBean();
				log.addClickListener(new ClickListener() {
				@Override
					public void buttonClick(ClickEvent event) {
						showLogPerkuliahan(o);
					}
				});
				return log;
			}
		});
		vl.addComponent(tabel);
		pnl.setContent(vl);
		tabel.setVisibleColumns("matakuliah","sks","dosenPengampu","jadwal","kehadiran","log");
		return pnl;
	}
	
	private void showLogPerkuliahan(KelasPerkuliahan kp) {
		DaftarLogPerkuliahan dlp = new DaftarLogPerkuliahan(kp);
		GeneralPopups.showGenericWindow(dlp, "Log Perkuliahan");
		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
		
	}
	
	
}
