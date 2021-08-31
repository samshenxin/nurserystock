package jdy.zsf.nurserystock.maintenancePlugin;

import kd.bos.list.plugin.*;
import kd.bos.filter.*;
import kd.bos.form.events.*;
import kd.bos.context.*;
import kd.bos.servicehelper.basedata.*;
import kd.bos.orm.query.*;
import java.util.*;

public class RareWoodBillPlugin extends AbstractListPlugin
{
    final String formId = "zsf_boslist_rarewood";
    private long orgId;
    
    public RareWoodBillPlugin() {
        this.orgId = 0L;
    }
    
    public void afterCreateNewData(final EventObject e) {
        final FilterContainer container = (FilterContainer)this.getControl("filtercontainerap");
        container.setBillFormId("zsf_boslist_rarewood");
        super.afterCreateNewData(e);
    }
    
    public void setFilter(final SetFilterEvent e) {
        this.orgId = RequestContext.get().getOrgId();
        if (this.orgId == 0L) {
            return;
        }
        final QFilter qfilter = BaseDataServiceHelper.getBaseDataFilter("zsf_instorageorderdetail", Long.valueOf(this.orgId));
        final List<QFilter> filters = (List<QFilter>)e.getQFilters();
        final QFilter filtercheckexpensive = new QFilter("zsf_checkexpensive", "=", (Object)"1");
        filters.add(qfilter);
        filters.add(filtercheckexpensive);
        e.setQFilters((List<QFilter>)filters);
        super.setFilter(e);
    }
}
