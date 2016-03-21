package org.stth.siak.jee.ui.generalview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.jee.ui.administrasi.AdministrasiEditorDataDosen;
import org.stth.siak.ui.util.GeneralPopups;
import org.stth.siak.util.GeneralUtilities;

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

public class DaftarDosenView extends CustomComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001325208244016531L;
	private List<DosenKaryawan> daftarDosen;
	private Table table;
	public static final int NAMA = 1;
	public static final int PRODI = 2;
	public static final int TGL_LAHIR = 4;
	public static final int TEMPAT_LAHIR = 8;
	public static final int ALAMAT = 16;
	public static final int NIDN = 32;
	public static final int TELP = 64;
	public static final int EMAIL = 128;
	public static final int ALIAS = 256;
	
	public static final int EDIT = 1;
	public static final int HAPUS = 2;

	private int visibleColumnsBit = 7;
	private int allowedActionsBit = 3;
	private int[] visColumnsArrays;
	private String[] columnNames;
	private List<String> visibleColumnNames = new ArrayList<String>();

	public DaftarDosenView(List<DosenKaryawan> kr){
		this.daftarDosen = kr;
		prepareContent();
	}
	public DaftarDosenView(List<DosenKaryawan> kr,int visibleColumns, int allowedActions){
		this.daftarDosen = kr;
		this.visibleColumnsBit = visibleColumns;
		this.allowedActionsBit = allowedActions;
		prepareContent();
	}

	private void prepareContent() {
		visColumnsArrays = new int[] {NAMA,PRODI,TGL_LAHIR,TEMPAT_LAHIR,ALAMAT,NIDN,TELP,EMAIL,ALIAS};
		columnNames = new String[] {"nama","prodi","tanggalLahir","tempatLahir","alamatRumah","nidn","nomorTelepon","email","alias","aksi"};
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(getTable());
		setCompositionRoot(layout);
	}

	@SuppressWarnings("serial")
	public Component getTable(){
		table = new Table("Ditemukan "+ daftarDosen.size()+ " dosen");
		BeanContainer<Integer, DosenKaryawan> beans = new BeanContainer<Integer, DosenKaryawan>(DosenKaryawan.class);
		Collections.sort(daftarDosen);
		beans.setBeanIdProperty("id");
		beans.removeAllItems();
		if (daftarDosen!=null){
			beans.addAll(daftarDosen);
		} else {
			beans.addBean(new DosenKaryawan());
		}
		table.setContainerDataSource(beans);
		table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
		table.addGeneratedColumn("aksi", new ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.setSpacing(true);
				BeanItem<?> i = (BeanItem<?>) source.getContainerDataSource().getItem(itemId);
				final DosenKaryawan d = (DosenKaryawan) i.getBean();
				
				Button buttonEdit = new Button("Edit Data");
				buttonEdit.addClickListener(new ClickListener() {
					@Override public void buttonClick(ClickEvent event) {
						AdministrasiEditorDataDosen ae = new AdministrasiEditorDataDosen(d);
						GeneralPopups.showGenericWindow(ae, "Edit Data Dosen");
					}
				});
	
				for (int j = 0; j < visColumnsArrays.length; j++) {
					int bitVal = GeneralUtilities.getBit(allowedActionsBit, j);
					if (bitVal==1 && j==0){
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
		table.setColumnHeader("alias", "ALIAS");
		table.setColumnHeader("nama", "NAMA");
		table.setColumnHeader("tanggalLahir", "TGL LAHIR");
		table.setColumnHeader("tempatLahir", "TEMPAT LAHIR");
		table.setColumnHeader("alamatRumah", "ALAMAT");
		table.setColumnHeader("prodi", "PROGRAM STUDI");
		table.setColumnHeader("nomorTelepon", "TELP");
		table.setColumnHeader("aksi", "AKSI");
		table.setColumnHeader("nidn", "NIDN");
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
	

}
