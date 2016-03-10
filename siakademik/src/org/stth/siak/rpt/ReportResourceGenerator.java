package org.stth.siak.rpt;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.stth.siak.entity.Mahasiswa;
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

}
