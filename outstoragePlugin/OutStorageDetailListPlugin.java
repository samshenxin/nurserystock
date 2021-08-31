package jdy.zsf.nurserystock.outstoragePlugin;

import kd.bos.list.plugin.*;
import kd.bos.cache.*;
import kd.bos.form.control.*;
import kd.bos.form.control.events.*;
import kd.bos.dataentity.utils.*;
import kd.bos.list.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.db.*;
import kd.bos.entity.datamodel.*;
import java.util.*;
import kd.bos.dataentity.entity.*;
import kd.bos.form.events.*;
import kd.bos.context.*;
import kd.bos.logging.*;

public class OutStorageDetailListPlugin extends AbstractListPlugin
{
    private static final Log logger;
    DistributeSessionlessCache cache;
    final String formId = "zsf_outstorageorderdetail";
    private long orgId;
    
    public OutStorageDetailListPlugin() {
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
            this.cache.remove("outsid");
        }
        if ("tbldel".equals(keyname)) {
            final BillList list = (BillList)this.getControl("billlistap");
            final ListSelectedRowCollection rowIndexs = list.getSelectedRows();
            System.err.println(rowIndexs);
            for (final ListSelectedRow row : rowIndexs) {
                final QFilter[] filters = { new QFilter("id", "=", row.getPrimaryKeyValue()) };
                final DynamicObject[] dob = BusinessDataServiceHelper.load("zsf_outstorageorderdetail", "id,billno,zsf_insid,zsf_number", filters);
                if (dob != null && dob.length > 0) {
                    final Object[] param = { dob[0].getString("zsf_insid") };
                    final String updateSql = "update tk_zsf_instorage_detail set fk_zsf_store_status = '0' where fbillno =? ";
                    final boolean updateFlag = DB.execute(DBRoute.basedata, updateSql, param);
                    final String deleteSql = "delete  from tk_zsf_outstorage_detail where fid =?";
                    OutStorageDetailListPlugin.logger.info("\u51fa\u5e93\u5220\u9664\u5e76\u66f4\u65b0\u5165\u5e93\u5e93\u5b58\u72b6\u6001\uff1a" + updateFlag);
                    final Object[] param2 = { row.getPrimaryKeyValue() };
                    final boolean delFlag = DB.execute(DBRoute.basedata, deleteSql, param2);
                    OutStorageDetailListPlugin.logger.info("\u51fa\u5e93\u6570\u636e\u5220\u9664\uff1a" + delFlag);
                    if (delFlag) {
                        this.getView().showSuccessNotification(row.getBillNo() + ",\u5220\u9664\u6210\u529f");
                    }
                    else {
                        this.getView().showErrorNotification(row.getBillNo() + ",\u5220\u9664\u6210\u529f");
                    }
                    this.getView().invokeOperation("refresh");
                }
                else {
                    this.getView().showTipNotification("\u5e93\u5b58\u72b6\u6001\u5f02\u5e38\uff0c\u5220\u9664\u5931\u8d25");
                }
            }
        }
    }
    
    public void afterCreateNewData(final EventObject e) {
        final String outsid = (String)this.getView().getFormShowParameter().getCustomParam("zsf_outsid");
        final String status = (String)this.getView().getFormShowParameter().getCustomParam("status");
        final String qty = (String)this.getView().getFormShowParameter().getCustomParam("qty");
        final Long site = (Long)this.getView().getFormShowParameter().getCustomParam("site");
        if (outsid != null) {
            this.cache.put("outsid", (String)outsid);
        }
        if (status != null && !"A".equals(status)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[] { "tblnew" });
        }
        if (qty != null) {
            this.cache.put("qty", (String)qty);
        }
        if (site != null) {
            this.cache.put("site", (String)site.toString());
        }
    }
    
    public void setFilter(final SetFilterEvent e) {
        this.orgId = RequestContext.get().getOrgId();
        if (this.orgId == 0L) {
            return;
        }
    }
    
    static {
        logger = LogFactory.getLog((Class)OutStorageDetailListPlugin.class);
    }
}
