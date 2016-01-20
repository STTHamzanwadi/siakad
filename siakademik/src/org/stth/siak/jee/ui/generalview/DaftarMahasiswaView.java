package org.stth.siak.jee.ui.generalview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.ui.util.GeneralPopups;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

public class DaftarMahasiswaView extends CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	private List<Mahasiswa> daftarMahasiswa;
	private Table table;

	public DaftarMahasiswaView(List<Mahasiswa> kr){
		this.daftarMahasiswa = kr;
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(getTable());
		setCompositionRoot(layout);
	}

	@SuppressWarnings("serial")
	public Component getTable(){
		table = new Table("Ditemukan "+ daftarMahasiswa.size()+ " mahasiswa");
		BeanContainer<Integer, Mahasiswa> beans = new BeanContainer<Integer, Mahasiswa>(Mahasiswa.class);
		Collections.sort(daftarMahasiswa);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (daftarMahasiswa!=null){
			beans.addAll(daftarMahasiswa);
		} else {
			beans.addBean(new Mahasiswa());
		}
		table.setContainerDataSource(beans);
		table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		table.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				Button buttonTranskrip = new Button("Profil");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Mahasiswa o = (Mahasiswa) i.getBean();
				buttonTranskrip.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						GeneralPopups.showProfilMahasiswa(o);
					}
				});
				Button buttonProfil = new Button("Transkrip");
				buttonProfil.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						GeneralPopups.showIpkMahasiswa(o);
					}
				});
				hl.addComponents(buttonProfil,buttonTranskrip);
				return hl;
			}
		});
		table.setConverter("tanggalLahir", new StringToDateConverter(){

			@Override

			public DateFormat getFormat(Locale locale){

			return new SimpleDateFormat("dd-MMM-yyyy");

			}

			});
		table.setColumnHeader("npm", "NIM");
		table.setColumnHeader("nama", "NAMA");
		table.setColumnHeader("tanggalLahir", "TGL LAHIR");
		table.setColumnHeader("tempatLahir", "TEMPAT LAHIR");
		table.setColumnHeader("alamat", "ALAMAT");
		table.setColumnHeader("prodi", "PROGRAM STUDI");
		table.setColumnHeader("pembimbingAkademik", "DOSEN P.A");
		table.setVisibleColumns("npm","nama","tanggalLahir","tempatLahir","alamat","pembimbingAkademik","aksi");
		return table;
	}

}
