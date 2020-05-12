package com.bantoo.babooo.Model;

public class TanggalPesanan {
    private String tanggal;
    private String bulan;

    public TanggalPesanan(String tanggal, String bulan) {
        this.tanggal = tanggal;
        this.bulan = bulan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }
}
