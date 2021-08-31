package jdy.zsf.nurserystock.outstoragePlugin;

import kd.bos.list.plugin.*;
import kd.bos.form.control.*;
import kd.bos.form.control.events.*;
import kd.bos.dataentity.utils.*;
import kd.bos.form.events.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.entity.*;
import java.math.*;
import kd.bos.list.*;
import kd.bos.bill.*;
import kd.bos.form.*;
import kd.bos.entity.datamodel.*;
import java.util.*;
import kd.bos.logging.*;

public class OutStorageListPlugin extends AbstractListPlugin
{
    private static final Log log;
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
        final Map<Object, DynamicObject> map = (Map<Object, DynamicObject>)BusinessDataServiceHelper.loadFromCache("zsf_outstorageorder", "id,name,number,zsf_ordername,zsf_qty,zsf_site,billstatus", filters);
        String billstatus = "";
        BigDecimal qty = null;
        Long site = 0L;
        String ordername = "";
        for (final Map.Entry entry : map.entrySet()) {
            final DynamicObject object = (DynamicObject) entry.getValue();
            billstatus = object.getString("billstatus");
            qty = (BigDecimal)object.get("zsf_qty");
            ordername = object.getString("zsf_ordername");
            final DynamicObject zsf_site = (DynamicObject)object.get("zsf_site");
            site = (Long)zsf_site.get("id");
        }
        if (StringUtils.equals((CharSequence)"zsf_ordername", (CharSequence)args.getHyperLinkClickEvent().getFieldName())) {
            args.setCancel(true);
            final ListShowParameter showParameter = new ListShowParameter();
            showParameter.setFormId("bos_list");
            showParameter.setStatus(OperationStatus.ADDNEW);
            showParameter.setCustomParam("zsf_outsid", (Object)ordername);
            showParameter.setCustomParam("qty", (Object)qty.toString());
            showParameter.setCustomParam("site", (Object)site);
            showParameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
            showParameter.setBillFormId("zsf_outstorageorderdetail");
            this.getView().showForm((FormShowParameter)showParameter);
        }
    }
    
    static {
        log = LogFactory.getLog((Class)OutStorageListPlugin.class);
    }
}
