package com.mhandharbeni.e_parking.apis.responses.data;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataMe implements Serializable, Parcelable {

    @SerializedName("jenis")
    @Expose
    private String jenis;
    @SerializedName("golongan")
    @Expose
    private String golongan;
    @SerializedName("thn_register")
    @Expose
    private String thnRegister;
    @SerializedName("kecamatan")
    @Expose
    private String kecamatan;
    @SerializedName("npwrd")
    @Expose
    private String npwrd;
    @SerializedName("nik")
    @Expose
    private String nik;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("tempat_lahir")
    @Expose
    private String tempatLahir;
    @SerializedName("tgl_lahir")
    @Expose
    private String tglLahir;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("nm_kelurahan")
    @Expose
    private String nmKelurahan;
    @SerializedName("nm_kecamatan")
    @Expose
    private String nmKecamatan;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("lokasi_parkir")
    @Expose
    private String lokasiParkir;
    @SerializedName("nmr_kawasan")
    @Expose
    private Integer nmrKawasan;
    @SerializedName("no_rekening")
    @Expose
    private String noRekening;
    @SerializedName("created")
    @Expose
    private String created;
    public final static Parcelable.Creator<DataMe> CREATOR = new Creator<DataMe>() {

        public DataMe createFromParcel(android.os.Parcel in) {
            return new DataMe(in);
        }

        public DataMe[] newArray(int size) {
            return (new DataMe[size]);
        }

    };

    protected DataMe(android.os.Parcel in) {
        this.jenis = ((String) in.readValue((String.class.getClassLoader())));
        this.golongan = ((String) in.readValue((String.class.getClassLoader())));
        this.thnRegister = ((String) in.readValue((String.class.getClassLoader())));
        this.kecamatan = ((String) in.readValue((String.class.getClassLoader())));
        this.npwrd = ((String) in.readValue((String.class.getClassLoader())));
        this.nik = ((String) in.readValue((String.class.getClassLoader())));
        this.nama = ((String) in.readValue((String.class.getClassLoader())));
        this.namaLengkap = ((String) in.readValue((String.class.getClassLoader())));
        this.tempatLahir = ((String) in.readValue((String.class.getClassLoader())));
        this.tglLahir = ((String) in.readValue((String.class.getClassLoader())));
        this.alamat = ((String) in.readValue((String.class.getClassLoader())));
        this.nmKelurahan = ((String) in.readValue((String.class.getClassLoader())));
        this.nmKecamatan = ((String) in.readValue((String.class.getClassLoader())));
        this.noHp = ((String) in.readValue((String.class.getClassLoader())));
        this.lokasiParkir = ((String) in.readValue((String.class.getClassLoader())));
        this.nmrKawasan = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.noRekening = ((String) in.readValue((String.class.getClassLoader())));
        this.created = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DataMe() {
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getThnRegister() {
        return thnRegister;
    }

    public void setThnRegister(String thnRegister) {
        this.thnRegister = thnRegister;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getNpwrd() {
        return npwrd;
    }

    public void setNpwrd(String npwrd) {
        this.npwrd = npwrd;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNmKelurahan() {
        return nmKelurahan;
    }

    public void setNmKelurahan(String nmKelurahan) {
        this.nmKelurahan = nmKelurahan;
    }

    public String getNmKecamatan() {
        return nmKecamatan;
    }

    public void setNmKecamatan(String nmKecamatan) {
        this.nmKecamatan = nmKecamatan;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getLokasiParkir() {
        return lokasiParkir;
    }

    public void setLokasiParkir(String lokasiParkir) {
        this.lokasiParkir = lokasiParkir;
    }

    public Integer getNmrKawasan() {
        return nmrKawasan;
    }

    public void setNmrKawasan(Integer nmrKawasan) {
        this.nmrKawasan = nmrKawasan;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(jenis);
        dest.writeValue(golongan);
        dest.writeValue(thnRegister);
        dest.writeValue(kecamatan);
        dest.writeValue(npwrd);
        dest.writeValue(nik);
        dest.writeValue(nama);
        dest.writeValue(namaLengkap);
        dest.writeValue(tempatLahir);
        dest.writeValue(tglLahir);
        dest.writeValue(alamat);
        dest.writeValue(nmKelurahan);
        dest.writeValue(nmKecamatan);
        dest.writeValue(noHp);
        dest.writeValue(lokasiParkir);
        dest.writeValue(nmrKawasan);
        dest.writeValue(noRekening);
        dest.writeValue(created);
    }

    public int describeContents() {
        return 0;
    }

}