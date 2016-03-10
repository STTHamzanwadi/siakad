package org.stth.siak.jee.ui.generalview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.jee.ui.administrasi.AdministrasiEditorDataMahasiswa;
import org.stth.siak.rpt.ReportResourceGenerator;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.GeneralUtilities;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
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
	private boolean showCetakTranskrip = false;
	public static final int NIM = 1;
	public static final int NAMA = 2;
	public static final int PRODI = 4;
	public static final int TGL_LAHIR = 8;
	public static final int TEMPAT_LAHIR = 16;
	public static final int ALAMAT = 32;
	public static final int DOSEN_PA = 64;

	public static final int LIHAT_PROFIL = 1;
	public static final int LIHAT_IPK = 2;
	public static final int CETAK_TRANSKRIP = 4;
	public static final int EDIT = 8;
	public static final int HAPUS = 16;

	private int visibleColumnsBit = 7;
	private int allowedActionsBit = 3;
	private int[] visColumnsArrays;
	private String[] columnNames;
	private List<String> visibleColumnNames = new ArrayList<String>();

	public DaftarMahasiswaView(List<Mahasiswa> kr){
		this.daftarMahasiswa = kr;
		prepareContent();
	}
	public DaftarMahasiswaView(List<Mahasiswa> kr,int visibleColumns, int allowedActions){
		this.daftarMahasiswa = kr;
		this.visibleColumnsBit = visibleColumns;
		this.allowedActionsBit = allowedActions;
		prepareContent();
	}

	private void prepareContent() {
		visColumnsArrays = new int[] {NIM,NAMA,PRODI,TGL_LAHIR,TEMPAT_LAHIR,ALAMAT,DOSEN_PA};
		columnNames = new String[] {"npm","nama","prodi","tanggalLahir","tempatLahir","alamat","pembimbingAkademik","aksi"};
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
				hl.setSpacing(true);
				
				Button buttonLihatProfil = new Button("Profil");
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final Mahasiswa o = (Mahasiswa) i.getBean();
				buttonLihatProfil.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						GeneralPopups.showProfilMahasiswa(o);
					}
				});
				
				Button buttonIPK = new Button("Indeks Prestasi");
				buttonIPK.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						GeneralPopups.showIpkMahasiswa(o);
					}
				});
				
				Button buttonTranskrip = new Button("Cetak Transkrip");
				buttonTranskrip.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						try {
							StreamResource resource = ReportResourceGenerator.cetakTranskripMahasiswa(o);
							getUI().getPage().open(resource, "_blank", false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				Button buttonEdit = new Button("Edit Mahasiswa");
				buttonEdit.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						AdministrasiEditorDataMahasiswa ae = new AdministrasiEditorDataMahasiswa(o);
						GeneralPopups.showGenericWindow(ae, "Edit Data Mahasiswa");
					}
				});
				
				for (int j = 0; j < visColumnsArrays.length; j++) {
					int bitVal = GeneralUtilities.getBit(allowedActionsBit, j);
					if (bitVal==1 && j==0){
						hl.addComponent(buttonLihatProfil);
					}
					if (bitVal==1 && j==1){
						hl.addComponent(buttonIPK);
					}
					if (bitVal==1 && j==2){
						hl.addComponent(buttonTranskrip);
					}
					if (bitVal==1 && j==3){
						hl.addComponent(buttonEdit);
					}
				}
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
		table.setColumnHeader("aksi", "AKSI");
		table.setColumnHeader("pembimbingAkademik", "DOSEN P.A");
		for (int j = 0; j < visColumnsArrays.length; j++) {
			int bitVal = GeneralUtilities.getBit(visibleColumnsBit, j);
			if (bitVal==1){
				visibleColumnNames.add(columnNames[j]);
			}
		}
		if(allowedActionsBit > 1){
			visibleColumnNames.add("aksi");
		}
		table.setVisibleColumns(visibleColumnNames.toArray());
		return table;
	}
	public void showTranskripButton(){
		showCetakTranskrip = true;
	}

}
