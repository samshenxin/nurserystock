package jdy.zsf.nurserystock.maintenancePlugin;

import kd.bos.form.plugin.*;
import kd.bos.form.control.*;
import kd.bos.form.control.events.*;
import kd.bos.dataentity.utils.*;
import java.math.*;
import json.*;
import kd.bos.db.*;
import kd.bos.servicehelper.coderule.*;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.algo.*;
import kd.bos.entity.operate.result.*;
import kd.bos.entity.datamodel.events.*;
import kd.bos.orm.query.*;
import kd.bos.form.field.*;
import kd.bos.dataentity.entity.*;
import java.util.*;
import java.util.List;
import kd.bos.logging.*;

public class VerificationBillPlugin extends AbstractFormPlugin
{
    private static final Log logger;
    final String formId = "zsf_verification";
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("tbmain");
        toolBar.addItemClickListener((ItemClickListener)this);
        this.addClickListeners(new String[] { "bar_save" });
    }
    
    public void itemClick(final ItemClickEvent evt) {
        final String keyname = evt.getItemKey();
        if (StringUtils.equalsIgnoreCase((CharSequence)keyname, (CharSequence)"bar_save")) {
            final DynamicObject dataInfo = this.getModel().getDataEntity();
            final BigDecimal qtyOb = (BigDecimal)dataInfo.get("zsf_qty");
            final boolean checkBox = (boolean)dataInfo.get("zsf_checkbox");
            final DynamicObject siteOb = (DynamicObject)dataInfo.get("zsf_site");
            final DynamicObject nameOb = (DynamicObject)dataInfo.get("zsf_name");
            final double realQty = qtyOb.doubleValue();
            final String site = (siteOb != null) ? siteOb.get("id").toString() : "";
            final String name = (nameOb != null) ? nameOb.get("id").toString() : "";
            final int qty = (int)Math.floor(realQty);
            final String algoKey = this.getClass().getName() + ".query_resume";
            final String sql = "select fid,fbillno,fk_zsf_name,fk_zsf_type,fk_zsf_qrcode,fk_zsf_rfid,fk_zsf_qty,fk_zsf_sale_price from tk_zsf_instorage_detail where fk_zsf_site =" + site + "  and fk_zsf_name =" + name + " and fk_zsf_store_status != '2'";
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
            OperationResult operationResult = null;
            if (qty != 0 && checkBox) {
                final String[] numbers = CodeRuleServiceHelper.getBatchNumber(this.getView().getEntityId().toString(), dataInfo, (String)null, qty);
                for (int i = 0; i < qty; ++i) {
                    if (list == null || list.size() <= 0) {
                        final boolean recycleNum = CodeRuleServiceHelper.recycleNumber("zsf_verification", dataInfo, (String)null, numbers[i]);
                        VerificationBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum);
                        this.getView().showTipNotification("\u5e93\u5b58\u6570\u91cf\u4e0d\u8db3");
                        break;
                    }
                    if (i > 0) {
                        final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_verification");
                        dynobj.set("billno", (Object)numbers[i]);
                        dynobj.set("zsf_site", (Object)site);
                        dynobj.set("zsf_name", list.get(i).get((Object)"fk_zsf_name"));
                        dynobj.set("zsf_type", list.get(i).get((Object)"fk_zsf_type"));
                        dynobj.set("zsf_ground_diameter", dataInfo.get("zsf_ground_diameter"));
                        dynobj.set("zsf_dbh", dataInfo.get("zsf_dbh"));
                        dynobj.set("zsf_coronal_diameter", dataInfo.get("zsf_coronal_diameter"));
                        dynobj.set("zsf_meter_diameter", dataInfo.get("zsf_meter_diameter"));
                        dynobj.set("zsf_height", dataInfo.get("zsf_height"));
                        dynobj.set("zsf_storestatus", dataInfo.get("zsf_storestatus"));
                        dynobj.set("zsf_remark", dataInfo.get("zsf_remark"));
                        dynobj.set("createtime", dataInfo.get("createtime"));
                        dynobj.set("billstatus", dataInfo.get("billstatus"));
                        dynobj.set("creator", dataInfo.get("creator"));
                        dynobj.set("modifier", dataInfo.get("modifier"));
                        dynobj.set("modifytime", dataInfo.get("modifytime"));
                        dynobj.set("zsf_picture", dataInfo.get("zsf_picture"));
                        dynobj.set("zsf_formerstatus", list.get(i).get((Object)"fk_zsf_store_status"));
                        dynobj.set("zsf_number", list.get(i).get((Object)"fid"));
                        dynobj.set("zsf_qrcode", list.get(i).get((Object)"fk_zsf_qrcode"));
                        dynobj.set("zsf_rfid", list.get(i).get((Object)"fk_zsf_rfid"));
                        dynobj.set("zsf_qty", (Object)1);
                        operationResult = SaveServiceHelper.saveOperate("zsf_verification", new DynamicObject[] { dynobj }, OperateOption.create());
                    }
                    else {
                        dataInfo.set("zsf_formerstatus", list.get(i).get((Object)"fk_zsf_store_status"));
                        dataInfo.set("zsf_number", list.get(i).get((Object)"fid"));
                        dataInfo.set("zsf_qrcode", list.get(i).get((Object)"fk_zsf_qrcode"));
                        dataInfo.set("zsf_rfid", list.get(i).get((Object)"fk_zsf_rfid"));
                        dataInfo.set("zsf_qty", (Object)1);
                        operationResult = SaveServiceHelper.saveOperate("zsf_verification", new DynamicObject[] { dataInfo }, OperateOption.create());
                    }
                    VerificationBillPlugin.logger.info("\u6838\u9500\u6279\u91cf\u4fdd\u5b58\uff1a" + objResult);
                    if (operationResult.isSuccess()) {
                        final Object[] param = { list.get(i).get((Object)"fid") };
                        final String updateSql = "update tk_zsf_instorage_detail set fk_zsf_store_status = '2' where fid =? ";
                        final boolean updateFlag = DB.execute(DBRoute.basedata, updateSql, param);
                        VerificationBillPlugin.logger.info("\u6838\u9500\u4fdd\u5b58\u6210\u529f\u5e76\u66f4\u65b0\u5165\u5e93\u5e93\u5b58\u72b6\u6001\uff1a" + updateFlag);
                    }
                    else {
                        final boolean recycleNum = CodeRuleServiceHelper.recycleNumber("zsf_verification", dataInfo, (String)null, numbers[i]);
                        VerificationBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum);
                    }
                }
            }
            else if (list.size() > 0) {
                dataInfo.set("zsf_formerstatus", list.get(0).get((Object)"fk_zsf_store_status"));
                dataInfo.set("zsf_number", list.get(0).get((Object)"fid"));
                dataInfo.set("zsf_qrcode", list.get(0).get((Object)"fk_zsf_qrcode"));
                dataInfo.set("zsf_rfid", list.get(0).get((Object)"fk_zsf_rfid"));
                operationResult = SaveServiceHelper.saveOperate("zsf_verification", new DynamicObject[] { dataInfo }, OperateOption.create());
                VerificationBillPlugin.logger.info("\u6838\u9500\u660e\u7ec6\u4fdd\u5b58\uff1a" + objResult);
                if (operationResult.isSuccess()) {
                    final Object[] param2 = { list.get(0).get((Object)"fid") };
                    final String updateSql2 = "update tk_zsf_instorage_detail set fk_zsf_store_status = '2' where fid =? ";
                    final boolean updateFlag2 = DB.execute(DBRoute.basedata, updateSql2, param2);
                    VerificationBillPlugin.logger.info("\u4fdd\u5b58\u6210\u529f\u5e76\u66f4\u65b0\u5165\u5e93\u5e93\u5b58\u72b6\u6001\uff1a" + updateFlag2);
                    this.getView().showTipNotification(operationResult.getMessage());
                }
                else {
                    final boolean recycleNum2 = CodeRuleServiceHelper.recycleNumber("zsf_verification", dataInfo, (String)null, dataInfo.getString("billno"));
                    VerificationBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum2);
                }
            }
            else {
                final boolean recycleNum2 = CodeRuleServiceHelper.recycleNumber("zsf_verification", dataInfo, (String)null, dataInfo.getString("billno"));
                VerificationBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum2);
                this.getView().showTipNotification("\u5e93\u5b58\u6570\u91cf\u4e0d\u8db3");
            }
        }
    }
    
    public void afterCreateNewData(final EventObject e) {
    }
    
    public void propertyChanged(final PropertyChangedArgs e) {
        final String propName = e.getProperty().getName();
        if (propName.equals("zsf_name")) {
            final ComboEdit comboEdit = (ComboEdit)this.getView().getControl("zsf_qrcode");
            final DynamicObject site = (DynamicObject)this.getModel().getValue("zsf_site");
            final DynamicObject pictureOb = (DynamicObject)this.getModel().getValue("zsf_name");
            final Long id = (Long)pictureOb.get("id");
            final QFilter[] filters = { (id != null) ? new QFilter("id", "=", (Object)id) : null };
            final Map<Object, DynamicObject> map = (Map<Object, DynamicObject>)BusinessDataServiceHelper.loadFromCache("zsf_nurseryname", "id,name,number,zsf_picture,zsf_nurserytype,zsf_qrcode,zsf_rfid", filters);
            String picture = null;
            DynamicObject typeObj = null;
            final List<ComboItem> data = new ArrayList<ComboItem>();
            for (final Map.Entry entry : map.entrySet()) {
                final DynamicObject object = (DynamicObject) entry.getValue();
                picture = object.getString("zsf_picture");
                typeObj = object.getDynamicObject("zsf_nurserytype");
            }
            this.getModel().setValue("zsf_picture", (Object)picture);
            this.getModel().setValue("zsf_type", (Object)typeObj);
            final QFilter[] filters2 = { new QFilter("zsf_site", "=", site.get("id")), new QFilter("zsf_name", "=", (Object)id) };
            final DynamicObject[] load;
            final DynamicObject[] dobjs = load = BusinessDataServiceHelper.load("zsf_instorageorderdetail", "id,zsf_qrcode,zsf_rfid", filters2);
            for (final DynamicObject dob : load) {
                final String qrcode = dob.getString("zsf_qrcode");
                data.add(new ComboItem((LocaleString)null, qrcode));
            }
            comboEdit.setComboItems((List)data);
        }
        if (propName.equals("zsf_qrcode")) {
            final String qrcode2 = (String)this.getModel().getValue("zsf_qrcode");
            final QFilter[] filters3 = { (qrcode2 != null) ? new QFilter("zsf_qrcode", "=", (Object)qrcode2) : null };
            final DynamicObject[] dobj = BusinessDataServiceHelper.load("zsf_instorageorderdetail", "id,zsf_nurserytype,zsf_qrcode,zsf_rfid", filters3);
            if (dobj != null && dobj.length > 0) {
                final String rfid = dobj[0].getString("zsf_rfid");
                this.getModel().setValue("zsf_rfid", (Object)rfid);
            }
        }
    }
    
    static {
        logger = LogFactory.getLog((Class)VerificationBillPlugin.class);
    }
}
