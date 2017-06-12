// 
// Decompiled by Procyon v0.5.30
// 

package org.stth.siak.helper;

import java.util.ArrayList;
import org.stth.siak.entity.DosenKaryawan;
import java.util.Iterator;
import org.stth.jee.persistence.LogPerkuliahanPersistence;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import org.stth.siak.entity.LogPerkuliahan;
import java.util.List;

public class MonevKehadiranDosen
{
    private List<LogPerkuliahan> logs;
    private Map<Integer, RekapKehadiranDosen> rekapMap;
    
    public MonevKehadiranDosen(final Date start, final Date end) {
        this.rekapMap = new HashMap();
        this.logs = LogPerkuliahanPersistence.getLogOnPeriod(start, end);
        this.constructRekap();
    }
    
    private void constructRekap() {
        for (final LogPerkuliahan log : this.logs) {
            final DosenKaryawan dosen = log.getKelasPerkuliahan().getDosenPengampu();
            final int idDosen = dosen.getId();
            RekapKehadiranDosen rkh;
            if (this.rekapMap.containsKey(idDosen)) {
                rkh = this.rekapMap.get(idDosen);
            }
            else {
                rkh = new RekapKehadiranDosen();
                rkh.setDosen(dosen);
                this.rekapMap.put(idDosen, rkh);
            }
            rkh.addLog(log);
        }
    }
    
    public List<RekapKehadiranDosen> getRekap() {
        final ArrayList<RekapKehadiranDosen> l = new ArrayList<RekapKehadiranDosen>(this.rekapMap.values());
        return l;
    }
    
    public class RekapKehadiranDosen
    {
        private DosenKaryawan dosen;
        private List<LogPerkuliahan> logHadir;
        
        public RekapKehadiranDosen() {
            this.logHadir = new ArrayList();
        }
        
        public DosenKaryawan getDosen() {
            return this.dosen;
        }
        
        public void setDosen(final DosenKaryawan dosen) {
            this.dosen = dosen;
        }
        
        public List<LogPerkuliahan> getLogHadir() {
            return this.logHadir;
        }
        
        public void setLogHadir(List<LogPerkuliahan> logHadir) {
            this.logHadir = logHadir;
        }
        
        public void addLog(LogPerkuliahan log) {
            this.logHadir.add(log);
        }
    }
}
