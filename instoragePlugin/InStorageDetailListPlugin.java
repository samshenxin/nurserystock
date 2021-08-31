package jdy.zsf.nurserystock.instoragePlugin;

import java.util.EventObject;
import java.util.Map;
import java.util.List;

import kd.bos.cache.CacheFactory;
import kd.bos.cache.DistributeSessionlessCache;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.events.SetFilterEvent;
import kd.bos.list.plugin.AbstractListPlugin;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.basedata.BaseDataServiceHelper;

public class InStorageDetailListPlugin extends AbstractListPlugin
{
    DistributeSessionlessCache cache;
    private long orgId;
    
    public InStorageDetailListPlugin() {
        this.cache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("customRegion");
        this.orgId = 0L;
    }
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("toolbarap");
        toolBar.addItemClickListener((ItemClickListener)this);
    }
    
    public void itemClick(final ItemClickEvent evt) {
        final String keyname = evt.getItemKey();
        if (StringUtils.equalsIgnoreCase((CharSequence)keyname, (CharSequence)"tblclose")) {
            this.cache.remove("insidParam");
            this.cache.remove("qtyParam");
        }
    }
    
    public void afterCreateNewData(final EventObject e) {
        final Map<?, ?> mapParam = (Map<?, ?>)this.getView().getFormShowParameter().getCustomParams();
        final String insidParam = (mapParam.get("insidParam") == null) ? "" : mapParam.get("insidParam").toString();
        final String statusParam = (mapParam.get("statusParam") == null) ? "" : mapParam.get("statusParam").toString();
        final String qtyParam = (mapParam.get("qtyParam") == null) ? "" : mapParam.get("qtyParam").toString();
        final String siteParam = (mapParam.get("siteParam") == null) ? "" : mapParam.get("siteParam").toString();
        this.cache.put("insidParam", (String)insidParam);
        this.cache.put("qtyParam", (String)qtyParam);
        this.cache.put("siteParam", (String)siteParam);
        if (!"A".equals(statusParam)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[] { "tblnew" });
        }
    }
    
    public void setFilter(final SetFilterEvent e) {
        this.orgId = RequestContext.get().getOrgId();
        if (this.orgId == 0L) {
            return;
        }
        final BaseDataServiceHelper helper = new BaseDataServiceHelper();
        final QFilter qfilter = BaseDataServiceHelper.getBaseDataFilter("zsf_instorageorderdetail", Long.valueOf(this.orgId));
        final List<QFilter> filters = (List<QFilter>)e.getQFilters();
        final QFilter filterInsid = new QFilter("zsf_insid", "=", this.cache.get("insidParam"));
        filters.add(qfilter);
        filters.add(filterInsid);
        e.setQFilters((List)filters);
        super.setFilter(e);
    }
}
