package jdy.zsf.nurserystock.outstoragePlugin;

import kd.bos.form.plugin.*;
import java.util.*;
import kd.bos.servicehelper.coderule.*;
import kd.bos.dataentity.entity.*;
import kd.bos.entity.datamodel.events.*;
import kd.bos.db.*;
import java.math.*;
import kd.bos.form.*;
import kd.bos.algo.*;
import kd.bos.logging.*;

public class OutStorageBillPlugin extends AbstractFormPlugin
{
    private static final Log logger;
    
    public void afterCreateNewData(final EventObject e) {
        final DynamicObject dataInfo = this.getModel().getDataEntity();
        final String number = CodeRuleServiceHelper.getNumber(this.getView().getEntityId().toString(), dataInfo, (String)null);
        final String ordername = this.getOrdername(number);
        this.getModel().setValue("zsf_ordername", (Object)ordername);
    }
    
    public String getOrdername(String billno) {
        final int last = billno.lastIndexOf("-");
        billno = billno.substring(last + 1);
        final String date = billno.substring(0, 8);
        final String number = billno.substring(8, billno.length());
        final Integer num = Integer.valueOf(number) + 1;
        final int length = billno.length() - 8 - String.valueOf(num).length();
        String ordername = "";
        for (int i = 0; i < length; ++i) {
            ordername += "0";
        }
        OutStorageBillPlugin.logger.info("\u51fa\u5e93\u5355\u7f16\u53f7\uff1a" + date + ordername + num);
        return date + ordername + num;
    }
    
    public void propertyChanged(final PropertyChangedArgs e) {
        final String propName = e.getProperty().getName();
        if (propName.equals("zsf_qty")) {
            final DynamicObject siteOb = (DynamicObject)this.getModel().getValue("zsf_site");
            final String site = (siteOb != null) ? siteOb.get("id").toString() : "";
            int columnCount = 0;
            final String algoKey = this.getClass().getName() + ".query_resume";
            final String sql = "select sum(fk_zsf_qty) as qty from tk_zsf_instorage_detail where fk_zsf_site =" + site + "  and fk_zsf_store_status='0'";
            try (final DataSet ds = DB.queryDataSet(algoKey, DBRoute.of("fa"), sql, (Object[])null)) {
                while (ds.hasNext()) {
                    final Row row = ds.next();
                    if (row.getInteger(0) != null) {
                        columnCount = row.getInteger("qty");
                    }
                }
            }
            final BigDecimal qtyOb = (BigDecimal)this.getModel().getValue("zsf_qty");
            final double realQty = qtyOb.doubleValue();
            final FieldTip qtyFieldTip = new FieldTip(FieldTip.FieldTipsLevel.Info, FieldTip.FieldTipsTypes.others, "zsf_qty", "\u4e0d\u80fd\u5927\u4e8e\u5e93\u5b58\u6570\u91cf" + columnCount);
            if (realQty > columnCount) {
                this.getView().showFieldTip(qtyFieldTip);
            }
            else {
                qtyFieldTip.setSuccess(true);
                this.getView().showFieldTip(qtyFieldTip);
            }
        }
    }
    
    static {
        logger = LogFactory.getLog((Class)OutStorageBillPlugin.class);
    }
}
