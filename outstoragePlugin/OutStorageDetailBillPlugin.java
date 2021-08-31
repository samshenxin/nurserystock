package jdy.zsf.nurserystock.outstoragePlugin;

import kd.bos.form.plugin.*;
import kd.bos.cache.*;
import kd.bos.form.control.*;
import kd.bos.form.events.*;
import kd.bos.form.control.events.*;
import kd.bos.dataentity.utils.*;
import java.math.*;
import kd.bos.dataentity.entity.*;
import json.*;
import kd.bos.db.*;
import kd.bos.servicehelper.coderule.*;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.bos.algo.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.entity.datamodel.events.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.form.*;
import java.util.*;
import java.util.List;
import kd.bos.logging.*;

public class OutStorageDetailBillPlugin extends AbstractFormPlugin
{
    private static final Log logger;
    DistributeSessionlessCache cache;
    final String site;
    final String qty;
    final String outsid;
    final String formId = "zsf_outstorageorderdetail";
    
    public OutStorageDetailBillPlugin() {
        this.cache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("customRegion");
        this.site = (String)this.cache.get("site");
        this.qty = (String)((this.cache.get("qty") != null) ? this.cache.get("qty") : "");
        this.outsid = (String)this.cache.get("outsid");
    }
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("tbmain");
        toolBar.addItemClickListener((ItemClickListener)this);
        this.addClickListeners(new String[] { "bar_save" });
    }
    
    public void beforeDoOperation(final BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
    }
    
    public void itemClick(final ItemClickEvent evt) {
        final String keyname = evt.getItemKey();
        if (StringUtils.equalsIgnoreCase((CharSequence)keyname, (CharSequence)"bar_save")) {
            final DynamicObject dataInfo = this.getModel().getDataEntity();
            final BigDecimal qtyOb = (BigDecimal)dataInfo.get("zsf_qty");
            final boolean checkBox = (boolean)dataInfo.get("zsf_checkbox");
            final DynamicObject nameOb = (DynamicObject)dataInfo.get("zsf_name");
            final double realQty = qtyOb.doubleValue();
            final String name = (nameOb != null) ? nameOb.get("id").toString() : "";
            final int qty = (int)Math.floor(realQty);
            final String algoKey = this.getClass().getName() + ".query_resume";
            final String sql = "select fid,fbillno,fk_zsf_name,fk_zsf_type,fk_zsf_qrcode,fk_zsf_rfid,fk_zsf_qty,fk_zsf_sale_price from  tk_zsf_instorage_detail  where fk_zsf_site =" + this.site + "  and fk_zsf_name =" + name + " and fk_zsf_store_status='0'";
            final List<JSONObject> list = new ArrayList<JSONObject>();
            try (final DataSet ds = DB.queryDataSet(algoKey, DBRoute.of("fa"), sql, (Object[])null)) {
                final RowMeta md = ds.getRowMeta();
                final int columnCount = md.getFieldCount();
                while (ds.hasNext()) {
                    final Row row = ds.next();
                    final Map<String, Object> rowData = new HashMap<String, Object>();
                    for (int k = 0; k < columnCount; ++k) {
                        rowData.put(md.getField(k).toString(), row.get(k));
                    }
                    final JSONObject json = new JSONObject((Map)rowData);
                    list.add(json);
                }
            }
            final Object[] objResult = null;
            if (qty != 0 && checkBox) {
                final String[] numbers = CodeRuleServiceHelper.getBatchNumber(this.getView().getEntityId().toString(), dataInfo, (String)null, qty);
                for (int i = 0; i < qty; ++i) {
                    if (list.size() <= 0) {
                        final boolean recycleNum = CodeRuleServiceHelper.recycleNumber("zsf_outstorageorderdetail", dataInfo, (String)null, numbers[i]);
                        OutStorageDetailBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum);
                        this.getView().showTipNotification("\u5e93\u5b58\u6570\u91cf\u4e0d\u8db3");
                        break;
                    }
                    this.saveData(list.get(i), dataInfo, i, numbers);
                }
            }
            else if (list.size() > 0) {
                this.saveData(list.get(0), dataInfo, 0, null);
            }
            else {
                final boolean recycleNum2 = CodeRuleServiceHelper.recycleNumber("zsf_outstorageorderdetail", dataInfo, (String)null, dataInfo.getString("billno"));
                OutStorageDetailBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum2);
                this.getView().showTipNotification("\u5e93\u5b58\u6570\u91cf\u4e0d\u8db3");
            }
        }
    }
    
    public void saveData(final JSONObject josn, final DynamicObject dataInfo, final int i, final String[] numbers) {
        Object[] objResult = null;
        dataInfo.set("zsf_qrcode", josn.get((Object)"fk_zsf_qrcode"));
        dataInfo.set("zsf_rfid", josn.get((Object)"fk_zsf_rfid"));
        dataInfo.set("zsf_qty", (Object)1);
        dataInfo.set("zsf_insid", josn.get((Object)"fbillno"));
        dataInfo.set("zsf_name", josn.get((Object)"fk_zsf_name"));
        dataInfo.set("zsf_type", josn.get((Object)"fk_zsf_type"));
        dataInfo.set("zsf_ground_diameter", josn.get((Object)"zsf_ground_diameter"));
        dataInfo.set("zsf_dbh", josn.get((Object)"zsf_dbh"));
        dataInfo.set("zsf_coronal_diameter", josn.get((Object)"zsf_coronal_diameter"));
        dataInfo.set("zsf_meter_diameter", josn.get((Object)"zsf_meter_diameter"));
        dataInfo.set("zsf_height", josn.get((Object)"zsf_height"));
        dataInfo.set("zsf_storestatus", josn.get((Object)"zsf_storestatus"));
        dataInfo.set("zsf_remark", josn.get((Object)"zsf_remark"));
        dataInfo.set("createtime", josn.get((Object)"createtime"));
        dataInfo.set("billstatus", josn.get((Object)"billstatus"));
        dataInfo.set("creator", josn.get((Object)"creator"));
        dataInfo.set("modifier", josn.get((Object)"modifier"));
        dataInfo.set("modifytime", josn.get((Object)"modifytime"));
        dataInfo.set("zsf_outsid", josn.get((Object)"zsf_outsid"));
        if (i > 0) {
            dataInfo.set("billno", (Object)numbers[i]);
        }
        dataInfo.set("zsf_picture", josn.get((Object)"zsf_picture"));
        dataInfo.set("zsf_sale_price", josn.get((Object)"zsf_sale_price"));
        dataInfo.set("zsf_buy_price", josn.get((Object)"zsf_buy_price"));
        dataInfo.set("zsf_estimate_price", josn.get((Object)"zsf_estimate_price"));
        objResult = SaveServiceHelper.save(new DynamicObject[] { dataInfo });
        OutStorageDetailBillPlugin.logger.info("\u51fa\u5e93\u660e\u7ec6\u4fdd\u5b58\uff1a" + objResult);
        if (objResult.length > 0) {
            final Object[] param = { josn.get((Object)"fbillno") };
            final String updateSql = "update tk_zsf_instorage_detail set fk_zsf_store_status = '1' where fbillno =? ";
            final boolean updateFlag = DB.execute(DBRoute.basedata, updateSql, param);
            OutStorageDetailBillPlugin.logger.info("\u51fa\u5e93\u4fdd\u5b58\u6210\u529f\u5e76\u66f4\u65b0\u5165\u5e93\u5e93\u5b58\u72b6\u6001\uff1a" + updateFlag);
        }
        else {
            final boolean recycleNum = CodeRuleServiceHelper.recycleNumber("zsf_outstorageorderdetail", dataInfo, (String)null, dataInfo.getString("billno"));
            OutStorageDetailBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum);
        }
    }
    
    public void afterCreateNewData(final EventObject e) {
        if (this.outsid != null) {
            this.getModel().setValue("zsf_outsid", (Object)this.outsid);
        }
    }
    
    public void propertyChanged(final PropertyChangedArgs e) {
        final String propName = e.getProperty().getName();
        if (propName.equals("zsf_name")) {
            final DynamicObject pictureOb = (DynamicObject)this.getModel().getValue("zsf_name");
            final Long id = (Long)pictureOb.get("id");
            final QFilter[] filters = { (id != null) ? new QFilter("id", "=", (Object)id) : null };
            final Map<Object, DynamicObject> map = (Map<Object, DynamicObject>)BusinessDataServiceHelper.loadFromCache("zsf_nurseryname", "id,name,billno,zsf_picture,zsf_nurserytype", filters);
            String picture = null;
            DynamicObject typeObj = null;
            for (final Map.Entry entry : map.entrySet()) {
                final DynamicObject object = (DynamicObject) entry.getValue();
                picture = object.getString("zsf_picture");
                typeObj = object.getDynamicObject("zsf_nurserytype");
            }
            this.getModel().setValue("zsf_picture", (Object)picture);
            this.getModel().setValue("zsf_type", (Object)typeObj);
            final String obj = typeObj.getString("name");
            if ("\u704c\u6728".equals(obj)) {
                this.getModel().setValue("zsf_checkbox", (Object)false);
            }
        }
        if (propName.equals("zsf_qty")) {
            final DynamicObject nameOb = (DynamicObject)this.getModel().getValue("zsf_name");
            final String name = (nameOb != null) ? nameOb.get("id").toString() : "";
            int columnCount = 0;
            final String algoKey = this.getClass().getName() + ".query_resume";
            final String sql = "select sum(fk_zsf_qty) as qty from tk_zsf_instorage_detail where fk_zsf_site =" + this.site + "  and fk_zsf_name =" + name + " and fk_zsf_store_status='0'";
            try (final DataSet ds = DB.queryDataSet(algoKey, DBRoute.of("fa"), sql, (Object[])null)) {
                while (ds.hasNext()) {
                    final Row row = ds.next();
                    if (row.getInteger(0) != null) {
                        columnCount = row.getInteger("qty");
                    }
                }
            }
            final BigDecimal qtyOb = (BigDecimal)this.getModel().getValue("zsf_qty");
            final double realQty = qtyOb.doubleValue();
            final FieldTip qtyFieldTip = new FieldTip(FieldTip.FieldTipsLevel.Info, FieldTip.FieldTipsTypes.others, "zsf_qty", "\u4e0d\u80fd\u5927\u4e8e\u5e93\u5b58\u6570\u91cf" + columnCount);
            if (realQty > columnCount) {
                this.getView().showFieldTip(qtyFieldTip);
            }
            else {
                qtyFieldTip.setSuccess(true);
                this.getView().showFieldTip(qtyFieldTip);
            }
        }
    }
    
    static {
        logger = LogFactory.getLog((Class)OutStorageDetailBillPlugin.class);
    }
}
