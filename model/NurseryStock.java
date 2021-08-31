package jdy.zsf.nurserystock.model;

public class NurseryStock
{
    private String NURS_ID;
    private String NURS_BILLNO;
    private String NURS_BILLSTATUS;
    private String NURS_CREATOR;
    private String NURS_CREATETIME;
    private Object NURS_SITE;
    private Object NURS_NAME;
    private Object NURS_TYPE;
    private String NURS_QRCODE;
    private String NURS_RFID;
    private Object NURS_QTY;
    private Object NURS_DBH;
    private Object NURS_GROUND_DIAMETER;
    private String NURS_CORONAL_DIAMETER;
    private String NURS_METER_DIAMETER;
    private String NURS_HEIGHT;
    private String NURS_STORESTATUS;
    private String NURS_ESTIMATE_PRICE;
    private String NURS_BUY_PRICE;
    private String NURS_SALE_PRICE;
    private String NURS_REMARK;
    private String NURS_PICTURE;
    private String NURS_CHECKEXPENSIVE;
    private String NURS_CHECKLABEL;
    private String NURS_LABELSTATUS;
    private String NURS_INSID;
    private String NURS_OUTSID;
    
    public NurseryStock(final String id, final String billno, final String creator, final String createtime, final Object zsf_site, final Object zsf_name, final Object zsf_type, final String zsf_qrcode, final String zsf_rfid, final Object zsf_qty, final Object zsf_dbh, final Object zsf_ground_diameter, final String zsf_coronal_diameter, final String zsf_meter_diameter, final String zsf_height, final String zsf_storestatus, final String zsf_estimate_price, final String zsf_buy_price, final String zsf_sale_price, final String zsf_remark, final String zsf_picture, final String zsf_checkexpensive, final String zsf_checklabel, final String zsf_labelstatus, final String zsf_insid, final String zsf_outsid) {
        this.NURS_ID = "id";
        this.NURS_BILLNO = "billno";
        this.NURS_BILLSTATUS = "billstatus";
        this.NURS_CREATOR = "creator";
        this.NURS_CREATETIME = "createtime";
        this.NURS_SITE = "zsf_site";
        this.NURS_NAME = "zsf_name";
        this.NURS_TYPE = "zsf_type";
        this.NURS_QRCODE = "zsf_qrcode";
        this.NURS_RFID = "zsf_rfid";
        this.NURS_QTY = "zsf_qty";
        this.NURS_DBH = "zsf_dbh";
        this.NURS_GROUND_DIAMETER = "zsf_ground_diameter";
        this.NURS_CORONAL_DIAMETER = "zsf_coronal_diameter";
        this.NURS_METER_DIAMETER = "zsf_meter_diameter";
        this.NURS_HEIGHT = "zsf_height";
        this.NURS_STORESTATUS = "zsf_storestatus";
        this.NURS_ESTIMATE_PRICE = "zsf_estimate_price";
        this.NURS_BUY_PRICE = "zsf_buy_price";
        this.NURS_SALE_PRICE = "zsf_sale_price";
        this.NURS_REMARK = "zsf_remark";
        this.NURS_PICTURE = "zsf_picture";
        this.NURS_CHECKEXPENSIVE = "zsf_checkexpensive";
        this.NURS_CHECKLABEL = "zsf_checklabel";
        this.NURS_LABELSTATUS = "zsf_labelstatus";
        this.NURS_INSID = "zsf_insid";
        this.NURS_OUTSID = "zsf_outsid";
    }
    
    public String getNURS_ID() {
        return this.NURS_ID;
    }
    
    public void setNURS_ID(final String nURS_ID) {
        this.NURS_ID = nURS_ID;
    }
    
    public String getNURS_BILLSTATUS() {
        return this.NURS_BILLSTATUS;
    }
    
    public void setNURS_BILLSTATUS(final String nURS_BILLSTATUS) {
        this.NURS_BILLSTATUS = nURS_BILLSTATUS;
    }
    
    public String getNURS_BILLNO() {
        return this.NURS_BILLNO;
    }
    
    public void setNURS_BILLNO(final String nURS_BILLNO) {
        this.NURS_BILLNO = nURS_BILLNO;
    }
    
    public String getNURS_CREATOR() {
        return this.NURS_CREATOR;
    }
    
    public void setNURS_CREATOR(final String nURS_CREATOR) {
        this.NURS_CREATOR = nURS_CREATOR;
    }
    
    public String getNURS_CREATETIME() {
        return this.NURS_CREATETIME;
    }
    
    public void setNURS_CREATETIME(final String nURS_CREATETIME) {
        this.NURS_CREATETIME = nURS_CREATETIME;
    }
    
    public Object getNURS_SITE() {
        return this.NURS_SITE;
    }
    
    public void setNURS_SITE(final Object nURS_SITE) {
        this.NURS_SITE = nURS_SITE;
    }
    
    public Object getNURS_NAME() {
        return this.NURS_NAME;
    }
    
    public void setNURS_NAME(final Object nURS_NAME) {
        this.NURS_NAME = nURS_NAME;
    }
    
    public Object getNURS_TYPE() {
        return this.NURS_TYPE;
    }
    
    public void setNURS_TYPE(final Object nURS_TYPE) {
        this.NURS_TYPE = nURS_TYPE;
    }
    
    public String getNURS_QRCODE() {
        return this.NURS_QRCODE;
    }
    
    public void setNURS_QRCODE(final String nURS_QRCODE) {
        this.NURS_QRCODE = nURS_QRCODE;
    }
    
    public String getNURS_RFID() {
        return this.NURS_RFID;
    }
    
    public void setNURS_RFID(final String nURS_RFID) {
        this.NURS_RFID = nURS_RFID;
    }
    
    public Object getNURS_QTY() {
        return this.NURS_QTY;
    }
    
    public void setNURS_QTY(final Object nURS_QTY) {
        this.NURS_QTY = nURS_QTY;
    }
    
    public Object getNURS_DBH() {
        return this.NURS_DBH;
    }
    
    public void setNURS_DBH(final Object nURS_DBH) {
        this.NURS_DBH = nURS_DBH;
    }
    
    public Object getNURS_GROUND_DIAMETER() {
        return this.NURS_GROUND_DIAMETER;
    }
    
    public void setNURS_GROUND_DIAMETER(final Object nURS_GROUND_DIAMETER) {
        this.NURS_GROUND_DIAMETER = nURS_GROUND_DIAMETER;
    }
    
    public String getNURS_CORONAL_DIAMETER() {
        return this.NURS_CORONAL_DIAMETER;
    }
    
    public void setNURS_CORONAL_DIAMETER(final String nURS_CORONAL_DIAMETER) {
        this.NURS_CORONAL_DIAMETER = nURS_CORONAL_DIAMETER;
    }
    
    public String getNURS_METER_DIAMETER() {
        return this.NURS_METER_DIAMETER;
    }
    
    public void setNURS_METER_DIAMETER(final String nURS_METER_DIAMETER) {
        this.NURS_METER_DIAMETER = nURS_METER_DIAMETER;
    }
    
    public String getNURS_HEIGHT() {
        return this.NURS_HEIGHT;
    }
    
    public void setNURS_HEIGHT(final String nURS_HEIGHT) {
        this.NURS_HEIGHT = nURS_HEIGHT;
    }
    
    public String getNURS_STORESTATUS() {
        return this.NURS_STORESTATUS;
    }
    
    public void setNURS_STORESTATUS(final String nURS_STORESTATUS) {
        this.NURS_STORESTATUS = nURS_STORESTATUS;
    }
    
    public String getNURS_ESTIMATE_PRICE() {
        return this.NURS_ESTIMATE_PRICE;
    }
    
    public void setNURS_ESTIMATE_PRICE(final String nURS_ESTIMATE_PRICE) {
        this.NURS_ESTIMATE_PRICE = nURS_ESTIMATE_PRICE;
    }
    
    public String getNURS_BUY_PRICE() {
        return this.NURS_BUY_PRICE;
    }
    
    public void setNURS_BUY_PRICE(final String nURS_BUY_PRICE) {
        this.NURS_BUY_PRICE = nURS_BUY_PRICE;
    }
    
    public String getNURS_SALE_PRICE() {
        return this.NURS_SALE_PRICE;
    }
    
    public void setNURS_SALE_PRICE(final String nURS_SALE_PRICE) {
        this.NURS_SALE_PRICE = nURS_SALE_PRICE;
    }
    
    public String getNURS_REMARK() {
        return this.NURS_REMARK;
    }
    
    public void setNURS_REMARK(final String nURS_REMARK) {
        this.NURS_REMARK = nURS_REMARK;
    }
    
    public String getNURS_PICTURE() {
        return this.NURS_PICTURE;
    }
    
    public void setNURS_PICTURE(final String nURS_PICTURE) {
        this.NURS_PICTURE = nURS_PICTURE;
    }
    
    public String getNURS_CHECKEXPENSIVE() {
        return this.NURS_CHECKEXPENSIVE;
    }
    
    public void setNURS_CHECKEXPENSIVE(final String nURS_CHECKEXPENSIVE) {
        this.NURS_CHECKEXPENSIVE = nURS_CHECKEXPENSIVE;
    }
    
    public String getNURS_CHECKLABEL() {
        return this.NURS_CHECKLABEL;
    }
    
    public void setNURS_CHECKLABEL(final String nURS_CHECKLABEL) {
        this.NURS_CHECKLABEL = nURS_CHECKLABEL;
    }
    
    public String getNURS_LABELSTATUS() {
        return this.NURS_LABELSTATUS;
    }
    
    public void setNURS_LABELSTATUS(final String nURS_LABELSTATUS) {
        this.NURS_LABELSTATUS = nURS_LABELSTATUS;
    }
    
    public String getNURS_INSID() {
        return this.NURS_INSID;
    }
    
    public void setNURS_INSID(final String nURS_INSID) {
        this.NURS_INSID = nURS_INSID;
    }
    
    public String getNURS_OUTSID() {
        return this.NURS_OUTSID;
    }
    
    public void setNURS_OUTSID(final String nURS_OUTSID) {
        this.NURS_OUTSID = nURS_OUTSID;
    }
}
