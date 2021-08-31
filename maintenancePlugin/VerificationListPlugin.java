package jdy.zsf.nurserystock.maintenancePlugin;

import kd.bos.list.plugin.*;
import kd.bos.form.control.*;
import kd.bos.form.control.events.*;
import kd.bos.list.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.db.*;
import com.alibaba.druid.util.*;
import kd.bos.form.*;
import kd.bos.form.plugin.*;
import kd.bos.entity.datamodel.*;
import java.util.*;
import kd.bos.dataentity.entity.*;
import kd.bos.form.events.*;
import kd.bos.logging.*;

public class VerificationListPlugin extends AbstractListPlugin
{
    private static final Log logger;
    final String formId = "zsf_verification";
    
    public void registerListener(final EventObject e) {
        super.registerListener(e);
        final Toolbar toolBar = (Toolbar)this.getView().getControl("toolbarap");
        toolBar.addItemClickListener((ItemClickListener)this);
    }
    
    public void itemClick(final ItemClickEvent evt) {
        super.itemClick(evt);
        final String keyName = evt.getItemKey();
        if ("tbldel".equals(keyName)) {
            final BillList list = (BillList)this.getControl("billlistap");
            final ListSelectedRowCollection rowIndexs = list.getSelectedRows();
            System.err.println(rowIndexs);
            for (final ListSelectedRow row : rowIndexs) {
                final QFilter[] filters = { new QFilter("id", "=", row.getPrimaryKeyValue()) };
                final DynamicObject[] dob = BusinessDataServiceHelper.load("zsf_verification", "id,billno,zsf_formerstatus,zsf_number", filters);
                if (dob != null && dob.length > 0) {
                    final String formerstatus = dob[0].getString("zsf_formerstatus");
                    final Object[] param = { dob[0].getString("billno") };
                    final String updateSql = "update tk_zsf_instorage_detail set fk_zsf_store_status ='" + formerstatus + "' where fbillno =? ";
                    final boolean updateFlag = DB.execute(DBRoute.basedata, updateSql, param);
                    final String deleteSql = "delete  from tk_zsf_verification where fid =?";
                    VerificationListPlugin.logger.info("\u6838\u9500\u5220\u9664\u5e76\u66f4\u65b0\u5165\u5e93\u5e93\u5b58\u72b6\u6001\uff1a" + updateFlag);
                    final Object[] param2 = { row.getPrimaryKeyValue() };
                    final boolean delFlag = DB.execute(DBRoute.basedata, deleteSql, param2);
                    VerificationListPlugin.logger.info("\u6838\u9500\u6570\u636e\u5220\u9664\uff1a" + delFlag);
                    if (delFlag) {
                        this.getView().showSuccessNotification(row.getBillNo() + ",\u5220\u9664\u6210\u529f");
                    }
                    else {
                        this.getView().showErrorNotification(row.getBillNo() + ",\u5220\u9664\u6210\u529f");
                    }
                    this.getView().invokeOperation("refresh");
                }
                else {
                    this.getView().showErrorNotification("\u5e93\u5b58\u72b6\u6001\u5f02\u5e38\uff0c\u5220\u9664\u5931\u8d25");
                }
            }
        }
        if (StringUtils.equals("zsf_loaddata", keyName)) {
            final FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("zsf_stocklist_select");
            formShowParameter.setCaption("\u5e93\u5b58\u6570\u636e");
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            formShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "zsf_loaddata"));
            this.getView().showForm(formShowParameter);
        }
    }
    
    public void closedCallBack(final ClosedCallBackEvent e) {
        super.closedCallBack(e);
        this.getView().updateView();
    }
    
    static {
        logger = LogFactory.getLog((Class)VerificationListPlugin.class);
    }
}
