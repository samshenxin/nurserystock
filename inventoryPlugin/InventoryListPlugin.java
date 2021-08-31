package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.list.plugin.*;
import kd.bos.form.control.*;
import kd.bos.form.control.events.*;
import kd.bos.dataentity.utils.*;
import kd.bos.form.events.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.entity.*;
import kd.bos.list.*;
import kd.bos.bill.*;
import kd.bos.form.*;
import kd.bos.entity.datamodel.*;
import java.util.*;

public class InventoryListPlugin extends AbstractListPlugin
{
    private static final String KEY_ORDERNAME = "zsf_name";
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("toolbarap");
        toolBar.addItemClickListener((ItemClickListener)this);
    }
    
    public void itemClick(final ItemClickEvent evt) {
        final String keyname = evt.getItemKey();
        if (StringUtils.equalsIgnoreCase((CharSequence)keyname, (CharSequence)"tblnew")) {}
    }
    
    public void billListHyperLinkClick(final HyperLinkClickArgs args) {
        super.billListHyperLinkClick(args);
        final int selectRow = args.getRowIndex();
        final BillList billList = (BillList)args.getHyperLinkClickEvent().getSource();
        final ListSelectedRowCollection lsrc = billList.getCurrentListAllRowCollection();
        final QFilter[] filters = { (lsrc.get(selectRow) != null) ? new QFilter("id", "=", (Object)lsrc.get(selectRow).toString()) : null };
        final Map<Object, DynamicObject> map = (Map<Object, DynamicObject>)BusinessDataServiceHelper.loadFromCache("zsf_inventory", "id,name,billno,billstatus,zsf_name,zsf_invstatus,zsf_checker", filters);
        String billstatus = "";
        Object checker = "";
        String ordername = "";
        String invstatus = null;
        for (final Map.Entry entry : map.entrySet()) {
            final DynamicObject object = (DynamicObject) entry.getValue();
            billstatus = object.getString("billstatus");
            ordername = object.getString("zsf_name");
            invstatus = object.getString("zsf_invstatus");
            checker = object.getString("zsf_checker");
        }
        if (StringUtils.equals((CharSequence)"zsf_name", (CharSequence)args.getHyperLinkClickEvent().getFieldName())) {
            args.setCancel(true);
            final ListShowParameter showParameter = new ListShowParameter();
            showParameter.setFormId("bos_list");
            showParameter.setStatus(OperationStatus.ADDNEW);
            final Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("insidParam", ordername);
            mapParam.put("statusParam", billstatus);
            mapParam.put("checkerParam", checker);
            mapParam.put("invstatusParam", invstatus);
            showParameter.setCustomParams((Map)mapParam);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setBillFormId("zsf_inventory_detail");
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }
}
