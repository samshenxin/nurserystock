package jdy.zsf.nurserystock.maintenancePlugin;

import kd.bos.list.plugin.*;
import kd.bos.filter.*;
import kd.bos.form.control.*;
import kd.bos.form.control.events.*;
import kd.bos.form.events.*;
import kd.bos.context.*;
import kd.bos.servicehelper.basedata.*;
import kd.bos.orm.query.*;

import java.util.EventObject;
import java.util.List;

public class CancelLabelListPlugin extends AbstractListPlugin
{
    final String formId = "zsf_cancel_label";
    private long orgId;
    
    public CancelLabelListPlugin() {
        this.orgId = 0L;
    }
    
    public void afterCreateNewData(final EventObject e) {
        final FilterContainer container = (FilterContainer)this.getControl("filtercontainerap");
        container.setBillFormId("zsf_cancel_label");
        super.afterCreateNewData(e);
    }
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("toolbarap");
        toolBar.addItemClickListener((ItemClickListener)this);
    }
    
    public void itemClick(final ItemClickEvent evt) {
    }
    
    public void setFilter(final SetFilterEvent e) {
        this.orgId = RequestContext.get().getOrgId();
        if (this.orgId == 0L) {
            return;
        }
        final BaseDataServiceHelper helper = new BaseDataServiceHelper();
        final QFilter qfilter = BaseDataServiceHelper.getBaseDataFilter("zsf_instorageorderdetail", Long.valueOf(this.orgId));
        final List<QFilter> filters = (List<QFilter>)e.getQFilters();
        final QFilter filterLabelStat = new QFilter("zsf_labelstatus", "!=", (Object)"2");
        final QFilter filterStatus = new QFilter("zsf_storestatus", "!=", (Object)'0');
        filters.add(qfilter);
        filters.add(filterLabelStat);
        filters.add(filterStatus);
        e.setQFilters((List)filters);
        super.setFilter(e);
    }
}
