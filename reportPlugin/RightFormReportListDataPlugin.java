package jdy.zsf.nurserystock.reportPlugin;

import java.util.*;
import kd.bos.entity.report.*;
import kd.bos.dataentity.entity.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.algo.*;

public class RightFormReportListDataPlugin extends AbstractReportListDataPlugin
{
    public List<AbstractReportColumn> getColumns(final List<AbstractReportColumn> columns) throws Throwable {
        columns.add((AbstractReportColumn)this.createReportColumn("zsf_site", "basedata", "\u82d7\u573a"));
        columns.add((AbstractReportColumn)this.createReportColumn("zsf_name", "basedata", "\u54c1\u540d"));
        columns.add((AbstractReportColumn)this.createReportColumn("zsf_type", "basedata", "\u7c7b\u578b"));
        columns.add((AbstractReportColumn)this.createReportColumn("zsf_qty", "qty", "\u6570\u91cf"));
        return (List<AbstractReportColumn>)super.getColumns((List)columns);
    }
    
    public ReportColumn createReportColumn(final String fieldKey, final String fieldType, final String caption) {
        final ReportColumn column = new ReportColumn();
        column.setFieldKey(fieldKey);
        column.setFieldType(fieldType);
        column.setCaption(new LocaleString(caption));
        return column;
    }
    
    public DataSet query(final ReportQueryParam reportQueryParam, final Object o) throws Throwable {
        final DynamicObject site = (DynamicObject)((DynamicObject)o).get("zsf_site");
        final QFilter siteQFilter = new QFilter("zsf_site", "=", site.get("id"));
        final DataSet dataSet = QueryServiceHelper.queryDataSet(this.getClass().getName(), "zsf_instorageorderdetail", "id, billno, zsf_insid, zsf_type, zsf_name, zsf_qty, zsf_site", (QFilter[])((siteQFilter == null) ? null : siteQFilter.toArray()), (String)null);
        final DataSet result = dataSet.select(new String[] { "zsf_type", "zsf_name", "zsf_qty", "zsf_site" });
        GroupbyDataSet groupby = result.groupBy(new String[] { "zsf_type", "zsf_name", "zsf_site" });
        groupby = groupby.sum("zsf_qty");
        final DataSet dataSet2 = groupby.finish();
        return dataSet2;
    }
}
