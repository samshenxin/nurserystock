package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.bill.*;
import kd.bos.cache.*;
import kd.bos.form.control.events.*;
import com.alibaba.druid.util.*;
import kd.bos.form.container.*;
import kd.bos.orm.query.*;
import kd.bos.entity.datamodel.*;
import kd.bos.form.field.*;
import kd.bos.form.*;
import kd.bos.dataentity.entity.*;
import kd.bos.form.control.*;

import java.util.EventObject;
import java.util.List;
import java.util.HashMap;
import kd.bos.servicehelper.*;
import kd.bos.servicehelper.operation.*;

public class InventoryListSelect extends AbstractBillPlugIn
{
    private static final String KEY_FIELDSETPANELAP = "zsf_search";
    private static final String KEY_TOOLBAR_IMPORTDATA = "zsf_toolbarap_invdata";
    private static final String KEY_ENTRYENTITY_SEARCH = "zsf_import_inv_entity";
    private static final String KEY_ITEM_SEARCH = "zsf_search_invdata";
    private static final String KEY_OK = "btnok";
    private static final String KEY_CANCEL = "btncancel";
    final String formId = "zsf_inventory_detail";
    DistributeSessionlessCache cache;
    
    public InventoryListSelect() {
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
            final FormShowParameter showParameter = this.getView().getFormShowParameter();
            final String insidParam = (String)showParameter.getCustomParam("insidParam");
            String fields = "id,zsf_site,zsf_type,zsf_name";
            final Container myFldPanel = (Container)this.getView().getControl("zsf_search");
            final Object qrcode = myFldPanel.getModel().getValue("zsf_search_qrcode");
            final Object rfid = myFldPanel.getModel().getValue("zsf_search_rfid");
            final QFilter qrcodeFilter = new QFilter("zsf_qrcode", "=", qrcode);
            final QFilter rfidFilter = new QFilter("zsf_rfid", "=", rfid);
            final DynamicObject nameObj = (DynamicObject)this.getModel().getValue("zsf_search_name");
            Long nameId = null;
            if (nameObj != null) {
                nameId = (Long)nameObj.get("id");
            }
            final QFilter nameFilter = new QFilter("zsf_name", "=", (Object)nameId);
            final QFilter[] filters = { (qrcode != null && !qrcode.equals("")) ? qrcodeFilter : null, (rfid != null && !rfid.equals("")) ? rfidFilter : null, (nameId != null) ? nameFilter : null };
            fields = "billno,zsf_rfid,zsf_site,zsf_qrcode,zsf_name,zsf_qty,zsf_storestatus,zsf_checklabel,zsf_labelstatus";
            final DynamicObjectCollection struCol = QueryServiceHelper.query("zsf_instorageorderdetail", fields, filters);
            this.getModel().deleteEntryData("zsf_import_inv_entity");
            final AbstractFormDataModel model = (AbstractFormDataModel)this.getModel();
            model.clearDirty();
            model.beginInit();
            final EntryGrid entryGrid = (EntryGrid)this.getControl("zsf_import_inv_entity");
            final List<FieldEdit> fieldList = (List<FieldEdit>)entryGrid.getFieldEdits();
            final TableValueSetter setter = new TableValueSetter(new String[0]);
            for (int i = 0; i < fieldList.size(); ++i) {
                setter.addField(fieldList.get(i).getKey(), new Object[0]);
            }
            for (int i = 0; i < struCol.size(); ++i) {
                setter.addRow(new Object[] { insidParam, ((DynamicObject)struCol.get(i)).get("billno"), ((DynamicObject)struCol.get(i)).getString("zsf_qrcode"), ((DynamicObject)struCol.get(i)).getString("zsf_rfid"), ((DynamicObject)struCol.get(i)).get("zsf_site"), ((DynamicObject)struCol.get(i)).get("zsf_name"), ((DynamicObject)struCol.get(i)).get("zsf_qty"), ((DynamicObject)struCol.get(i)).get("zsf_storestatus"), ((DynamicObject)struCol.get(i)).get("zsf_checklabel"), ((DynamicObject)struCol.get(i)).get("zsf_labelstatus") });
            }
            model.batchCreateNewEntryRow("zsf_import_inv_entity", setter);
            model.endInit();
            this.getView().updateView("zsf_import_inv_entity");
        }
    }
    
    public void click(final EventObject evt) {
        super.click(evt);
        final Control source = (Control)evt.getSource();
        if (StringUtils.equals(source.getKey(), "btnok")) {
            final HashMap<String, DynamicObjectCollection> hashMap = new HashMap<String, DynamicObjectCollection>();
            final EntryGrid entryGrid = (EntryGrid)this.getView().getControl("zsf_import_inv_entity");
            final int[] rows = entryGrid.getSelectRows();
            if (null != rows && rows.length > 0) {
                for (int i = 0; i < rows.length; ++i) {
                    final DynamicObject entity = (DynamicObject)this.getModel().getEntryEntity("zsf_import_inv_entity").get(rows[i]);
                    final Long siteid = (entity.getDynamicObject("zsf_site") != null) ? entity.getDynamicObject("zsf_site").getLong("id") : 0L;
                    final Long nameid = (entity.getDynamicObject("zsf_name") != null) ? entity.getDynamicObject("zsf_name").getLong("id") : 0L;
                    final QFilter qrcodeFilter = new QFilter("zsf_qrcode", "=", entity.get("zsf_qrcode"));
                    final QFilter rfidFilter = new QFilter("zsf_rfid", "=", entity.get("zsf_rfid"));
                    final QFilter stateFilter = new QFilter("zsf_invstatus", "=", (Object)"A");
                    final QFilter[] filters = { qrcodeFilter, rfidFilter, stateFilter };
                    final String fields = "id,zsf_site,zsf_name,zsf_qrcode, zsf_rfid,zsf_qty,zsf_storestatus,zsf_labelstatus";
                    final DynamicObjectCollection assets = QueryServiceHelper.query("zsf_inventory_detail", fields, filters);
                    if (assets.size() <= 0) {
                        final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_inventory_detail");
                        dynobj.set("billstatus", (Object)"A");
                        dynobj.set("zsf_ordername", this.cache.get("insidParam"));
                        dynobj.set("zsf_qrcode", (Object)entity.getString("zsf_qrcode"));
                        dynobj.set("zsf_rfid", (Object)entity.getString("zsf_rfid"));
                        dynobj.set("zsf_site", (Object)siteid);
                        dynobj.set("zsf_name", (Object)nameid);
                        dynobj.set("zsf_qty", entity.get("zsf_qty"));
                        dynobj.set("zsf_invstatus", this.cache.get("invstatusParam"));
                        dynobj.set("creator", this.cache.get("checkerParam"));
                        dynobj.set("zsf_storestatus", (Object)entity.getString("zsf_storestatus"));
                        dynobj.set("zsf_labelstatus", (Object)entity.getString("zsf_labelstatus"));
                        SaveServiceHelper.save(new DynamicObject[] { dynobj });
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
}
