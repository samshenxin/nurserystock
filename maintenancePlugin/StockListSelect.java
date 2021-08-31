package jdy.zsf.nurserystock.maintenancePlugin;

import kd.bos.bill.*;
import kd.bos.cache.*;
import kd.bos.form.control.events.*;
import com.alibaba.druid.util.*;
import kd.bos.form.container.*;
import kd.bos.orm.query.*;
import kd.bos.dataentity.entity.*;
import kd.bos.form.control.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.db.*;
import kd.bos.servicehelper.*;
import kd.bos.entity.datamodel.*;
import kd.bos.form.field.*;
import java.util.*;
import java.util.List;
import kd.bos.logging.*;

public class StockListSelect extends AbstractBillPlugIn
{
    private static final Log logger;
    private static final String KEY_FIELDSETPANELAP = "zsf_search";
    private static final String KEY_TOOLBAR_IMPORTDATA = "zsf_toolbarap_invdata";
    private static final String KEY_ENTRYENTITY_SEARCH = "zsf_import_stock_entity";
    private static final String KEY_ITEM_SEARCH = "zsf_search_invdata";
    private static final String KEY_OK = "btnok";
    private static final String KEY_CANCEL = "btncancel";
    final String formId = "zsf_verification";
    DistributeSessionlessCache cache;
    
    public StockListSelect() {
        this.cache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("customRegion");
    }
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolbar_select = (Toolbar)this.getView().getControl("zsf_toolbarap_invdata");
        toolbar_select.addItemClickListener((ItemClickListener)this);
        this.addClickListeners(new String[] { "btnok", "btncancel" });
    }
    
    public void itemClick(final ItemClickEvent evt) {
        super.itemClick(evt);
        if (StringUtils.equals("zsf_search_invdata", evt.getItemKey())) {
            final Container myFldPanel = (Container)this.getView().getControl("zsf_search");
            final Object qrcode = myFldPanel.getModel().getValue("zsf_search_qrcode");
            final Object rfid = myFldPanel.getModel().getValue("zsf_search_rfid");
            final QFilter qrcodeFilter = new QFilter("zsf_qrcode", "=", qrcode);
            final QFilter rfidFilter = new QFilter("zsf_rfid", "=", rfid);
            final DynamicObject nameObj = (DynamicObject)this.getModel().getValue("zsf_search_name");
            final DynamicObject siteObj = (DynamicObject)this.getModel().getValue("zsf_search_site");
            Long nameId = null;
            Long siteId = null;
            if (nameObj != null) {
                nameId = (Long)nameObj.get("id");
            }
            if (siteObj != null) {
                siteId = (Long)siteObj.get("id");
            }
            final QFilter nameFilter = new QFilter("zsf_name", "=", (Object)nameId);
            final QFilter siteFilter = new QFilter("zsf_site", "=", (Object)siteId);
            final QFilter stateFilter = new QFilter("zsf_storestatus", "!=", (Object)"2");
            final QFilter[] filters = { (qrcode != null && !qrcode.equals("")) ? qrcodeFilter : null, (rfid != null && !rfid.equals("")) ? rfidFilter : null, (nameId != null) ? nameFilter : null, (siteId != null) ? siteFilter : null, stateFilter };
            this.loadingdata(filters);
        }
    }
    
    public void click(final EventObject evt) {
        super.click(evt);
        final Control source = (Control)evt.getSource();
        if (StringUtils.equals(source.getKey(), "btnok")) {
            final HashMap<String, DynamicObjectCollection> hashMap = new HashMap<String, DynamicObjectCollection>();
            final EntryGrid entryGrid = (EntryGrid)this.getView().getControl("zsf_import_stock_entity");
            final int[] rows = entryGrid.getSelectRows();
            if (null != rows && rows.length > 0) {
                for (int i = 0; i < rows.length; ++i) {
                    final DynamicObject entity = (DynamicObject)this.getModel().getEntryEntity("zsf_import_stock_entity").get(rows[i]);
                    final Long siteid = (entity.getDynamicObject("zsf_site") != null) ? entity.getDynamicObject("zsf_site").getLong("id") : 0L;
                    final Long nameid = (entity.getDynamicObject("zsf_name") != null) ? entity.getDynamicObject("zsf_name").getLong("id") : 0L;
                    final Long typeid = (entity.getDynamicObject("zsf_type") != null) ? entity.getDynamicObject("zsf_type").getLong("id") : 0L;
                    final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_verification");
                    dynobj.set("billstatus", (Object)"A");
                    dynobj.set("billno", entity.get("zsf_billno"));
                    dynobj.set("zsf_qrcode", (Object)entity.getString("zsf_qrcode"));
                    dynobj.set("zsf_rfid", (Object)entity.getString("zsf_rfid"));
                    dynobj.set("zsf_site", (Object)siteid);
                    dynobj.set("zsf_name", (Object)nameid);
                    dynobj.set("zsf_type", (Object)typeid);
                    dynobj.set("zsf_qty", entity.get("zsf_qty"));
                    dynobj.set("zsf_storestatus", (Object)"2");
                    dynobj.set("zsf_formerstatus", (entity.get("zsf_storestatus") == "") ? "0" : entity.get("zsf_storestatus"));
                    dynobj.set("createtime", (Object)new Date());
                    final Object[] objResult = SaveServiceHelper.save(new DynamicObject[] { dynobj });
                    if (objResult.length > 0) {
                        final Object[] param = { entity.get("zsf_billno"), entity.getString("zsf_qrcode") };
                        final String updateSql = "update tk_zsf_instorage_detail set fk_zsf_store_status = '2' where fbillno =? and fk_zsf_qrcode =? ";
                        final boolean updateFlag = DB.execute(DBRoute.basedata, updateSql, param);
                        StockListSelect.logger.info("\u51fa\u5e93\u4fdd\u5b58\u6210\u529f\u5e76\u66f4\u65b0\u5165\u5e93\u5e93\u5b58\u72b6\u6001\uff1a" + updateFlag);
                    }
                }
            }
            this.getView().returnDataToParent((Object)hashMap);
            this.getView().close();
        }
        else if (StringUtils.equals(source.getKey(), "btncancel")) {
            this.getView().returnDataToParent((Object)null);
            this.getView().close();
        }
    }
    
    public void afterCreateNewData(final EventObject e) {
        final QFilter stateFilter = new QFilter("zsf_storestatus", "!=", (Object)"2");
        final QFilter[] filters = { stateFilter };
        this.loadingdata(filters);
    }
    
    public void loadingdata(final QFilter[] filters) {
        final String fields = "zsf_insid,billno,zsf_rfid,zsf_site,zsf_qrcode,zsf_name,zsf_type,zsf_ground_diameter,zsf_dbh,zsf_coronal_diameter,zsf_meter_diameter,zsf_height,zsf_remark,zsf_qty,zsf_storestatus,zsf_checklabel,zsf_labelstatus,zsf_picture,createtime";
        final DynamicObjectCollection struCol = QueryServiceHelper.query("zsf_instorageorderdetail", fields, filters);
        this.getModel().deleteEntryData("zsf_import_stock_entity");
        final AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
        model.clearDirty();
        model.beginInit();
        final EntryGrid entryGrid = (EntryGrid)this.getControl("zsf_import_stock_entity");
        final List<FieldEdit> fieldList = (List<FieldEdit>)entryGrid.getFieldEdits();
        final TableValueSetter setter = new TableValueSetter(new String[0]);
        for (int i = 0; i < fieldList.size(); ++i) {
            setter.addField(fieldList.get(i).getKey(), new Object[0]);
        }
        for (int i = 0; i < struCol.size(); ++i) {
            setter.addRow(new Object[] { ((DynamicObject)struCol.get(i)).get("zsf_insid"), ((DynamicObject)struCol.get(i)).get("billno"), ((DynamicObject)struCol.get(i)).getString("zsf_qrcode"), ((DynamicObject)struCol.get(i)).getString("zsf_rfid"), ((DynamicObject)struCol.get(i)).get("zsf_site"), ((DynamicObject)struCol.get(i)).get("zsf_name"), ((DynamicObject)struCol.get(i)).get("zsf_type"), ((DynamicObject)struCol.get(i)).get("zsf_qty"), ((DynamicObject)struCol.get(i)).get("zsf_storestatus"), ((DynamicObject)struCol.get(i)).get("zsf_checklabel"), ((DynamicObject)struCol.get(i)).get("zsf_labelstatus"), ((DynamicObject)struCol.get(i)).get("zsf_picture"), ((DynamicObject)struCol.get(i)).get("createtime"), ((DynamicObject)struCol.get(i)).get("zsf_ground_diameter"), ((DynamicObject)struCol.get(i)).get("zsf_dbh"), ((DynamicObject)struCol.get(i)).get("zsf_coronal_diameter"), ((DynamicObject)struCol.get(i)).get("zsf_meter_diameter"), ((DynamicObject)struCol.get(i)).get("zsf_height"), ((DynamicObject)struCol.get(i)).get("zsf_remark") });
        }
        model.batchCreateNewEntryRow("zsf_import_stock_entity", setter);
        model.endInit();
        this.getView().updateView("zsf_import_stock_entity");
    }
    
    static {
        logger = LogFactory.getLog((Class)StockListSelect.class);
    }
}
