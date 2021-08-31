package jdy.zsf.nurserystock.model;

public class ReplaceLabel
{
    private String repl_id;
    private String repl_billno;
    private String repl_billstatus;
    private String repl_creator;
    private String repl_createtime;
    private String repl_name;
    private String repl_type;
    private String repl_qd_qrcode;
    private String repl_qd_rfid;
    private String repl_rl_qrcode;
    private String repl_rl_rfid;
    private String repl_remark;
    
    public ReplaceLabel(final String repl_id, final String repl_billno, final String repl_billstatus, final String repl_creator, final String repl_createtime, final String repl_name, final String repl_type, final String repl_qd_qrcode, final String repl_qd_rfid, final String repl_rl_qrcode, final String repl_rl_rfid, final String repl_remark) {
        this.repl_id = "id";
        this.repl_billno = "billno";
        this.repl_billstatus = "billstatus";
        this.repl_creator = "creator";
        this.repl_createtime = "createtime";
        this.repl_name = "zsf_name";
        this.repl_type = "zsf_type";
        this.repl_qd_qrcode = "zsf_quondam_qrcode";
        this.repl_qd_rfid = "zsf_quondam_rfid";
        this.repl_rl_qrcode = "zsf_replace_qrcode";
        this.repl_rl_rfid = "zsf_replace_qrcode";
        this.repl_remark = "zsf_remark";

    }
    
    public String getRepl_BILLNO() {
        return this.repl_billno;
    }
    
    public void setRepl_BILLNO(final String repl_BILLNO) {
        this.repl_billno = repl_BILLNO;
    }
    
    public String getRepl_ID() {
        return this.repl_id;
    }
    
    public void setRepl_ID(final String repl_ID) {
        this.repl_id = repl_ID;
    }
    
    public String getRepl_BILLSTATUS() {
        return this.repl_billstatus;
    }
    
    public void setRepl_BILLSTATUS(final String repl_BILLSTATUS) {
        this.repl_billstatus = repl_BILLSTATUS;
    }
    
    public String getRepl_CREATOR() {
        return this.repl_creator;
    }
    
    public void setRepl_CREATOR(final String repl_CREATOR) {
        this.repl_creator = repl_CREATOR;
    }
    
    public String getRepl_CREATETIME() {
        return this.repl_createtime;
    }
    
    public void setRepl_CREATETIME(final String repl_CREATETIME) {
        this.repl_createtime = repl_CREATETIME;
    }
    
    public String getRepl_Name() {
        return this.repl_name;
    }
    
    public void setRepl_Name(final String repl_Name) {
        this.repl_name = repl_Name;
    }
    
    public String getRepl_type() {
        return this.repl_type;
    }
    
    public void setRepl_type(final String repl_type) {
        this.repl_type = repl_type;
    }
    
    public String getRepl_qd_qrcode() {
        return this.repl_qd_qrcode;
    }
    
    public void setRepl_qd_qrcode(final String repl_qd_qrcode) {
        this.repl_qd_qrcode = repl_qd_qrcode;
    }
    
    public String getRepl_qd_rfid() {
        return this.repl_qd_rfid;
    }
    
    public void setRepl_qd_rfid(final String repl_qd_rfid) {
        this.repl_qd_rfid = repl_qd_rfid;
    }
    
    public String getRepl_rl_qrcode() {
        return this.repl_rl_qrcode;
    }
    
    public void setRepl_rl_qrcode(final String repl_rl_qrcode) {
        this.repl_rl_qrcode = repl_rl_qrcode;
    }
    
    public String getRepl_rl_rfid() {
        return this.repl_rl_rfid;
    }
    
    public void setRepl_rl_rfid(final String repl_rl_rfid) {
        this.repl_rl_rfid = repl_rl_rfid;
    }
    
    public String getRepl_remark() {
        return this.repl_remark;
    }
    
    public void setRepl_remark(final String repl_remark) {
        this.repl_remark = repl_remark;
    }
    
    public char charAt(final int index) {
        return this.repl_type.charAt(index);
    }
    
    public String getRepl_name() {
        return this.repl_name;
    }
    
    public void setRepl_name(final String repl_name) {
        this.repl_name = repl_name;
    }
}
