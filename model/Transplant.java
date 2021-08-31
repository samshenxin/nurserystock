package jdy.zsf.nurserystock.model;

public class Transplant
{
    private String tran_id;
    private String tran_billno;
    private String tran_billstatus;
    private String tran_creator;
    private String tran_createtime;
    private String tran_site;
    private String tran_name;
    private String tran_type;
    private String tran_qrcode;
    private String tran_rfid;
    private String tran_bf_gps;
    private String tran_af_gps;
    
    public Transplant(final String tran_id, final String tran_billno, final String tran_billstatus, final String tran_creator, final String tran_createtime, final String tran_site, final String tran_name, final String tran_type, final String tran_qrcode, final String tran_rfid, final String tran_bf_gps, final String tran_af_gps) {
        this.tran_id = "id";
        this.tran_billno = "billno";
        this.tran_billstatus = "billstatus";
        this.tran_creator = "creator";
        this.tran_createtime = "createtime";
        this.tran_site = "zsf_site";
        this.tran_name = "zsf_name";
        this.tran_type = "zsf_type";
        this.tran_qrcode = "zsf_qrcode";
        this.tran_rfid = "zsf_rfid";
        this.tran_bf_gps = "zsf_before_gps";
        this.tran_af_gps = "zsf_after_gps";
    }
    
    public String getTran_id() {
        return this.tran_id;
    }
    
    public void setTran_id(final String tran_id) {
        this.tran_id = tran_id;
    }
    
    public String getTran_billno() {
        return this.tran_billno;
    }
    
    public void setTran_billno(final String tran_billno) {
        this.tran_billno = tran_billno;
    }
    
    public String getTran_billstatus() {
        return this.tran_billstatus;
    }
    
    public void setTran_billstatus(final String tran_billstatus) {
        this.tran_billstatus = tran_billstatus;
    }
    
    public String getTran_creator() {
        return this.tran_creator;
    }
    
    public void setTran_creator(final String tran_creator) {
        this.tran_creator = tran_creator;
    }
    
    public String getTran_createtime() {
        return this.tran_createtime;
    }
    
    public void setTran_createtime(final String tran_createtime) {
        this.tran_createtime = tran_createtime;
    }
    
    public String getTran_site() {
        return this.tran_site;
    }
    
    public void setTran_site(final String tran_site) {
        this.tran_site = tran_site;
    }
    
    public String getTran_name() {
        return this.tran_name;
    }
    
    public void setTran_name(final String tran_name) {
        this.tran_name = tran_name;
    }
    
    public String getTran_type() {
        return this.tran_type;
    }
    
    public void setTran_type(final String tran_type) {
        this.tran_type = tran_type;
    }
    
    public String getTran_qrcode() {
        return this.tran_qrcode;
    }
    
    public void setTran_qrcode(final String tran_qrcode) {
        this.tran_qrcode = tran_qrcode;
    }
    
    public String getTran_rfid() {
        return this.tran_rfid;
    }
    
    public void setTran_rfid(final String tran_rfid) {
        this.tran_rfid = tran_rfid;
    }
    
    public String getTran_bf_gps() {
        return this.tran_bf_gps;
    }
    
    public void setTran_bf_gps(final String tran_bf_gps) {
        this.tran_bf_gps = tran_bf_gps;
    }
    
    public String getTran_af_gps() {
        return this.tran_af_gps;
    }
    
    public void setTran_af_gps(final String tran_af_gps) {
        this.tran_af_gps = tran_af_gps;
    }
}
