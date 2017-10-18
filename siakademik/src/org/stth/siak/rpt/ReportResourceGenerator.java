package org.stth.siak.rpt;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.stth.siak.entity.KelasPerkuliahan;
import org.stth.siak.entity.KelasPerkuliahanMahasiswaPerSemester2;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.enumtype.JenisUjian;
import org.stth.siak.helper.IndeksPrestasiHelper;

import com.vaadin.server.StreamResource;

public class ReportResourceGenerator {
	public static StreamResource cetakTranskripMahasiswa(Mahasiswa m) throws JRException{
		IndeksPrestasiHelper iph = new IndeksPrestasiHelper(m);
		ReportRawMaterials rrm = ReportContentFactory.siapkanReportTranskripMahasiswa(iph);
		List<ReportRawMaterials> rrms = new ArrayList<ReportRawMaterials>();
		rrms.add(rrm);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Transkrip Mahasiswa"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	public static StreamResource cetakTranskripWisudaMahasiswa(Mahasiswa m,String[] lTanggal) throws JRException{
		IndeksPrestasiHelper iph = new IndeksPrestasiHelper(m);
		ReportRawMaterials rrm = ReportContentFactory.siapkanReportTranskripWisudaMahasiswa(iph,lTanggal[0],lTanggal[1]);
		List<ReportRawMaterials> rrms = new ArrayList<ReportRawMaterials>();
		rrms.add(rrm);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Transkrip Mahasiswa"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	public static StreamResource cetakTranskripWisudaMahasiswa(List<Mahasiswa> l,String[] lTanggal) throws JRException{
		List<ReportRawMaterials> rrms = new ArrayList<ReportRawMaterials>();
		for (Mahasiswa m : l) {
			IndeksPrestasiHelper iph = new IndeksPrestasiHelper(m);
			ReportRawMaterials rrm = ReportContentFactory.siapkanReportTranskripWisudaMahasiswa(iph,lTanggal[0],lTanggal[1]);
			rrms.add(rrm);
		}
		
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Transkrip Mahasiswa"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	
	public static StreamResource cetakKHS(List<KelasPerkuliahanMahasiswaPerSemester2> lkpmp) throws JRException{
		List<ReportRawMaterials> rrms  = ReportContentFactory.siapkanKartuHasilStdui(lkpmp);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms,"Kartu Hasil Studi"+String.valueOf(rrms.hashCode()));
		StreamResource resource = rog.exportToPdf();
 		return resource;
	}
	
	public static StreamResource cetakKartuUjian(List<KelasPerkuliahanMahasiswaPerSemester2> selected, JenisUjian tipe) throws JRException{
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportKartuUjian(selected, tipe);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Kartu "+tipe.toString()+String.valueOf(rrms.hashCode()));
		StreamResource source = rog.exportToPdf();
		return source;
	}
	public static StreamResource cetakSampulUjian(List<KelasPerkuliahan> l, JenisUjian jenis) throws JRException{
		ReportRawMaterials rrm = ReportContentFactory.siapkanReportSampulUjian(l, jenis);
		List<ReportRawMaterials> rrms = new ArrayList<>();
		rrms.add(rrm);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Kartu "+jenis.toString()+String.valueOf(rrms.hashCode()));
		StreamResource source = rog.exportToPdf();
		return source;
	}
	public static StreamResource cetakAbsensiUjian(List<KelasPerkuliahan> l, JenisUjian jenis) throws JRException{
		List<ReportRawMaterials> rrms = ReportContentFactory.siapkanReportAbsenPerkuliahan(l, jenis);
		ReportOutputGenerator rog = new ReportOutputGenerator(rrms, "Kartu "+jenis.toString()+String.valueOf(rrms.hashCode()));
		StreamResource source = rog.exportToPdf();
		return source;
	}
	
}
