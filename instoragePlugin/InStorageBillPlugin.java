package jdy.zsf.nurserystock.instoragePlugin;

import kd.bos.form.plugin.*;
import java.util.*;
import kd.bos.logging.*;

public class InStorageBillPlugin extends AbstractFormPlugin
{
    private static final Log logger;
    
    public void afterCreateNewData(final EventObject e) {
    }
    
    public void afterBindData(final EventObject e) {
        super.afterBindData(e);
        String billno = (String)this.getModel().getValue("billno");
        final int last = billno.lastIndexOf("-");
        billno = billno.substring(last + 1);
        this.getModel().setValue("zsf_ordername", (Object)billno);
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
        InStorageBillPlugin.logger.info("\u5165\u5e93\u5355\u7f16\u53f7\uff1a" + date + ordername + num);
        return date + ordername + num;
    }
    
    static {
        logger = LogFactory.getLog((Class)InStorageBillPlugin.class);
    }
}