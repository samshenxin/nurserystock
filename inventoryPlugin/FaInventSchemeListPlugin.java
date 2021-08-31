package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.list.plugin.*;
import kd.bos.context.*;
import kd.bos.servicehelper.license.*;
import kd.bos.license.api.*;
import java.util.*;
import kd.bos.form.control.*;
import kd.bos.dataentity.resource.*;
import kd.bos.form.*;
import kd.bos.form.events.*;
import kd.bos.servicehelper.inte.*;
import kd.bos.list.*;

public class FaInventSchemeListPlugin extends AbstractListPlugin
{
    public void preOpenForm(final PreOpenFormEventArgs e) {
        final String uid = RequestContext.getOrCreate().getUserId();
        final LicenseCheckResult result = LicenseServiceHelper.checkUserInGroup(Long.valueOf(Long.parseLong(uid)), Long.valueOf(4L));
        if (!result.getHasLicense()) {
            final String msg = result.getMsg();
            e.setCancel(true);
            e.setCancelMessage(msg);
        }
        else {
            super.preOpenForm(e);
        }
    }
    
    public void registerListener(final EventObject e) {
        this.addClickListeners(new String[] { "viewtask", "viewreport" });
    }
    
    public void click(final EventObject evt) {
        final Control c = (Control)evt.getSource();
        final String key = c.getKey();
        if ("viewtask".equalsIgnoreCase(key)) {
            this.viewTask(evt);
        }
        else if ("viewreport".equalsIgnoreCase(key)) {
            this.viewReport(evt);
        }
        super.click(evt);
    }
    
    private void viewTask(final EventObject evt) {
        final Object schemeId = ((IListView)this.getView()).getCurrentSelectedRowInfo().getPrimaryKeyValue();
        final ListShowParameter parameter = new ListShowParameter();
        parameter.setFormId("bos_list");
        parameter.setCaption(ResManager.loadKDString("\u76d8\u70b9\u4efb\u52a1", "FaInventSchemeListPlugin_0", "fi-fa-formplugin", new Object[0]));
        parameter.setCustomParam("schemeId", schemeId);
        parameter.setBillFormId("fa_inventory_task");
        parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        parameter.setHasRight(true);
        this.getView().showForm((FormShowParameter)parameter);
    }
    
    private void viewReport(final EventObject evt) {
        final Object schemeId = ((IListView)this.getView()).getCurrentSelectedRowInfo().getPrimaryKeyValue();
        final FormShowParameter parameter = new FormShowParameter();
        parameter.setFormId("fa_inventory_report");
        parameter.setCaption(ResManager.loadKDString("\u76d8\u70b9\u62a5\u544a", "FaInventSchemeListPlugin_1", "fi-fa-formplugin", new Object[0]));
        parameter.setCustomParam("schemeId", schemeId);
        parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        this.getView().showForm(parameter);
    }
    
    public void beforeCreateListColumns(final BeforeCreateListColumnsArgs args) {
        final String formatStr = InteServiceHelper.getDateFormat(Long.valueOf(Long.parseLong(RequestContext.get().getUserId())));
        final DateListColumn column = (DateListColumn)args.getListColumn("createtime");
        column.setDisplayFormatString(formatStr);
    }
}
