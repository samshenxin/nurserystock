package jdy.zsf.nurserystock.reportPlugin;

import kd.bos.dataentity.entity.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.entity.report.*;
import kd.bos.algo.*;

public class LeftTreeReportListDataPlugin extends AbstractReportListDataPlugin
{
    public DataSet query(final ReportQueryParam reportQueryParam, final Object arg1) throws Throwable {
        final FilterInfo filterInfo = reportQueryParam.getFilter();
        final FilterItemInfo itemSite = filterInfo.getFilterItem("zsf_search_basedata_site");
        Long siteId = null;
        QFilter siteQFilter = null;
        if (itemSite.getValue() instanceof DynamicObject) {
            final DynamicObject dynSite = (DynamicObject)itemSite.getValue();
            siteId = (Long)dynSite.get("id");
            siteQFilter = new QFilter("zsf_site", "=", (Object)siteId);
        }
        final DataSet dataSet = QueryServiceHelper.queryDataSet(this.getClass().getName(), "zsf_instorageorder", "id, zsf_site", (QFilter[])((siteQFilter == null) ? null : siteQFilter.toArray()), (String)null);
        GroupbyDataSet groupby = dataSet.groupBy(new String[] { "zsf_site" });
        groupby = groupby.count("zsf_qty");
        final DataSet dataSet2 = groupby.finish();
        return dataSet2;
    }
}
