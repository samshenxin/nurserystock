package jdy.zsf.nurserystock.instoragePlugin;

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

public class InStorageListPlugin extends AbstractListPlugin
{
    private static final String KEY_ORDERNAME = "zsf_ordername";
    
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
        final Map<Object, DynamicObject> map = (Map<Object, DynamicObject>)BusinessDataServiceHelper.loadFromCache("zsf_instorageorder", "id,name,billno,billstatus,zsf_ordername,zsf_qty,zsf_site", filters);
        String billstatus = "";
        String qty = "";
        String ordername = "";
        DynamicObject siteObj = null;
        for (final Map.Entry entry : map.entrySet()) {
            final DynamicObject object = (DynamicObject) entry.getValue();
            billstatus = object.getString("billstatus");
            qty = object.getString("zsf_qty");
            ordername = object.getString("zsf_ordername");
            siteObj = (DynamicObject)object.get("zsf_site");
        }
        if (StringUtils.equals((CharSequence)"zsf_ordername", (CharSequence)args.getHyperLinkClickEvent().getFieldName())) {
            args.setCancel(true);
            final ListShowParameter showParameter = new ListShowParameter();
            showParameter.setFormId("bos_list");
            showParameter.setStatus(OperationStatus.ADDNEW);
            final Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("insidParam", ordername);
            mapParam.put("statusParam", billstatus);
            mapParam.put("qtyParam", qty);
            mapParam.put("siteParam", (siteObj == null) ? "" : siteObj.get("id"));
            showParameter.setCustomParams((Map)mapParam);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setBillFormId("zsf_instorageorderdetail");
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }
}
