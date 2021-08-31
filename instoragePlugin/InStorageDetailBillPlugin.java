package jdy.zsf.nurserystock.instoragePlugin;

import kd.bos.form.plugin.*;
import kd.bos.cache.*;
import kd.bos.form.control.*;
import kd.bos.form.events.*;
import kd.bos.form.control.events.*;
import kd.bos.dataentity.utils.*;
import java.math.*;
import kd.bos.servicehelper.coderule.*;
import kd.bos.servicehelper.coderule.CodeRuleServiceHelper;
import kd.bos.servicehelper.*;
import jdy.zsf.nurserystock.utils.*;
import kd.bos.dataentity.entity.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.entity.datamodel.events.*;
import kd.bos.orm.query.*;
import kd.bos.db.*;
import kd.bos.form.*;
import java.util.*;
import kd.bos.algo.*;
import kd.bos.logging.*;

public class InStorageDetailBillPlugin extends AbstractFormPlugin
{
    private static final Log logger;
    DistributeSessionlessCache cache;
    final String insidParam;
    final String qtyParam;
    final String siteParam;
    final String formId = "zsf_instorageorderdetail";
    
    public InStorageDetailBillPlugin() {
        this.cache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("customRegion");
        this.insidParam = (String)this.cache.get("insidParam");
        this.qtyParam = (String)this.cache.get("qtyParam");
        this.siteParam = (String)this.cache.get("siteParam");
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
            final BigDecimal qtyOb = (BigDecimal)this.getModel().getValue("zsf_qty");
            final double value = qtyOb.doubleValue();
            final boolean checkBox = (boolean)this.getModel().getValue("zsf_checkbox");
            final boolean checkLabel = (boolean)this.getModel().getValue("zsf_checklabel");
            final int qty = (int)Math.floor(value);
            final DynamicObject dataInfo = this.getModel().getDataEntity();
            if (value != 0.0 && checkBox) {
                final String[] numbers = CodeRuleServiceHelper.getBatchNumber(this.getView().getEntityId().toString(), dataInfo, (String)null, qty);
                Object[] objResult = null;
                for (int i = 0; i < qty; ++i) {
                    if (i > 0) {
                        final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_instorageorderdetail");
                        dynobj.set("zsf_name", dataInfo.get("zsf_name"));
                        dynobj.set("zsf_type", dataInfo.get("zsf_type"));
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
                        dynobj.set("zsf_insid", dataInfo.get("zsf_insid"));
                        dynobj.set("billno", (Object)numbers[i]);
                        dynobj.set("zsf_picture", dataInfo.get("zsf_picture"));
                        dynobj.set("zsf_checkexpensive", dataInfo.get("zsf_checkexpensive"));
                        dynobj.set("zsf_labelstatus", dataInfo.get("zsf_labelstatus"));
                        dynobj.set("zsf_checklabel", dataInfo.get("zsf_checklabel"));
                        dynobj.set("zsf_estimate_price", dataInfo.get("zsf_estimate_price"));
                        dynobj.set("zsf_qrcode", (Object)IdUtils.fastSimpleUUID());
                        if (checkLabel) {
                            dynobj.set("zsf_rfid", (Object)IdUtils.getGUID());
                        }
                        dynobj.set("zsf_buy_price", dataInfo.get("zsf_buy_price"));
                        dynobj.set("zsf_qty", (Object)1);
                        dynobj.set("zsf_site", dataInfo.get("zsf_site"));
                        objResult = SaveServiceHelper.save(new DynamicObject[] { dynobj });
                    }
                    else {
                        dataInfo.set("zsf_qty", (Object)1);
                        objResult = SaveServiceHelper.save(new DynamicObject[] { dataInfo });
                    }
                    if (objResult == null) {
                        final boolean recycleNum = CodeRuleServiceHelper.recycleNumber("zsf_instorageorderdetail", dataInfo, (String)null, numbers[i]);
                        InStorageDetailBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum);
                    }
                    InStorageDetailBillPlugin.logger.info("\u5165\u5e93\u660e\u7ec6\u6279\u91cf\u4fdd\u5b58\uff1a" + objResult);
                }
            }
            else {
                final Object[] objResult2 = SaveServiceHelper.save(new DynamicObject[] { dataInfo });
                InStorageDetailBillPlugin.logger.info("\u5165\u5e93\u660e\u7ec6\u4fdd\u5b58\uff1a" + objResult2);
                if (objResult2 == null) {
                    CodeRuleServiceHelper.autoRecycleNumber("zsf_instorageorderdetail");
                    final boolean recycleNum2 = CodeRuleServiceHelper.recycleNumber("zsf_instorageorderdetail", dataInfo, (String)null, dataInfo.getString("billno"));
                    InStorageDetailBillPlugin.logger.info("\u4fdd\u5b58\u5931\u8d25\u56de\u6536\u7f16\u7801\uff1a" + recycleNum2);
                }
            }
        }
    }
    
    public void afterCreateNewData(final EventObject e) {
        if (this.insidParam != null) {
            this.getModel().setValue("zsf_insid", (Object)this.insidParam);
            this.getModel().setValue("zsf_site", (Object)this.siteParam);
        }
        final boolean checkLabel = (boolean)this.getModel().getValue("zsf_checklabel");
        if (checkLabel) {
            this.getModel().setValue("zsf_rfid", (Object)IdUtils.getGUID());
        }
        this.getModel().setValue("zsf_qrcode", (Object)IdUtils.fastSimpleUUID());
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
            int columnCount = 0;
            final String algoKey = this.getClass().getName() + ".query_resume";
            final String sql = "select sum(fk_zsf_qty) as qty from tk_zsf_instorage_detail  where fk_zsf_insid ='" + this.insidParam + "'";
            try (final DataSet ds = DB.queryDataSet(algoKey, DBRoute.of("fa"), sql, (Object[])null)) {
                while (ds.hasNext()) {
                    final Row row = ds.next();
                    if (row.getInteger(0) != null) {
                        columnCount = row.getInteger("qty");
                    }
                }
            }
            final BigDecimal qtyOb = (BigDecimal)this.getModel().getValue("zsf_qty");
            final double realQty = qtyOb.doubleValue() + columnCount;
            final double count = Double.parseDouble(this.qtyParam);
            final FieldTip qtyFieldTip = new FieldTip(FieldTip.FieldTipsLevel.Info, FieldTip.FieldTipsTypes.others, "zsf_qty", "\u672c\u8ba2\u5355\u5df2\u6709\u5e93\u5b58\u6570\u91cf" + columnCount + ",\u8f93\u5165\u6570\u91cf\u4e0d\u80fd\u5927\u4e8e\u5165\u5e93\u603b\u6570" + count);
            if (realQty > count) {
                this.getView().showFieldTip(qtyFieldTip);
            }
            else {
                qtyFieldTip.setSuccess(true);
                this.getView().showFieldTip(qtyFieldTip);
            }
        }
        if (propName.equals("zsf_checklabel")) {
            final boolean checkLabel = (boolean)this.getModel().getValue("zsf_checklabel");
            if (checkLabel) {
                this.getModel().setValue("zsf_labelstatus", (Object)"0");
                this.getModel().setValue("zsf_rfid", (Object)IdUtils.getGUID());
            }
            else {
                this.getModel().setValue("zsf_labelstatus", (Object)"2");
            }
        }
    }
    
    static {
        logger = LogFactory.getLog((Class)InStorageBillPlugin.class);
    }
}

