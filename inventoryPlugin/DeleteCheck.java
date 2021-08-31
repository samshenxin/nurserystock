package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.form.plugin.*;
import java.util.*;
import kd.bos.form.control.events.*;
import kd.bos.entity.plugin.args.*;
import kd.bos.form.control.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.resource.*;
import kd.bos.entity.datamodel.*;
import kd.bos.dataentity.entity.*;
import kd.bos.form.events.*;
import kd.bos.form.operate.*;
import kd.bos.dataentity.utils.*;

public class DeleteCheck extends AbstractFormPlugin
{
    final String formId = "zsf_inventory";
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("toolbarap");
        toolBar.addItemClickListener((ItemClickListener)this);
    }
    
    public void beginOperationTransaction(final BeginOperationTransactionArgs e) {
        final String operateName = e.getOperationKey();
        if ("tbldel".equals(operateName)) {
            final IDataModel model = this.getModel();
            final int index = model.getEntryCurrentRowIndex("zsf_inventory");
            final EntryGrid entryGrid = (EntryGrid)this.getView().getControl("zsf_inventory");
            final int[] rows = entryGrid.getSelectRows();
            if (null != rows && rows.length > 0) {
                for (int i = 0; i < rows.length; ++i) {
                    final DynamicObject entity = (DynamicObject)model.getEntryEntity("zsf_inventory").get(rows[i]);
                    final String taskname = entity.get("zsf_name").toString();
                    final QFilter nameFilter = new QFilter("zsf_ordername", "=", (Object)taskname);
                    final QFilter[] filters = { nameFilter };
                    final String fields = "id,zsf_site,zsf_name,zsf_qrcode, zsf_rfid,zsf_qty,zsf_storestatus,zsf_labelstatus";
                    final DynamicObjectCollection inventorys = QueryServiceHelper.query("zsf_inventory_detail", fields, filters);
                    if (inventorys.size() > 0) {
                        this.getView().showTipNotification(ResManager.loadKDString("\u8be5\u5355\u636e\u5b58\u5728\u4e0b\u6e38\u5355\u636e\u4e0d\u80fd\u5220\u9664", "DeleteCheck_0", "jdy.zsf.nurserystock.inventoryPlugin", new Object[0]));
                        return;
                    }
                    model.deleteEntryRow("zsf_inventory", rows[i]);
                }
            }
        }
    }
    
    public void beforeDoOperation(final BeforeDoOperationEventArgs args) {
        super.beforeDoOperation(args);
        final FormOperate formOperate = (FormOperate)args.getSource();
        if (StringUtils.equals((CharSequence)"delete", (CharSequence)formOperate.getOperateKey())) {
            final IDataModel ob = formOperate.getView().getModel();
            System.err.println(ob);
            final EntryGrid entryGrid = (EntryGrid)this.getView().getControl("zsf_inventory");
            final int[] rows = entryGrid.getSelectRows();
            if (null != rows && rows.length > 0) {
                for (int i = 0; i < rows.length; ++i) {
                    final DynamicObject entity = (DynamicObject)ob.getEntryEntity("zsf_inventory").get(rows[i]);
                    final String taskname = entity.get("zsf_name").toString();
                    final QFilter nameFilter = new QFilter("zsf_ordername", "=", (Object)taskname);
                    final QFilter[] filters = { nameFilter };
                    final String fields = "id,zsf_site,zsf_name,zsf_qrcode, zsf_rfid,zsf_qty,zsf_storestatus,zsf_labelstatus";
                    final DynamicObjectCollection inventorys = QueryServiceHelper.query("zsf_inventory_detail", fields, filters);
                    if (inventorys.size() > 0) {
                        this.getView().showTipNotification(ResManager.loadKDString("\u8be5\u5355\u636e\u5b58\u5728\u4e0b\u6e38\u5355\u636e\u4e0d\u80fd\u5220\u9664", "DeleteCheck_0", "jdy.zsf.nurserystock.inventoryPlugin", new Object[0]));
                        return;
                    }
                    ob.deleteEntryRow("zsf_inventory", rows[i]);
                }
            }
        }
    }
}
