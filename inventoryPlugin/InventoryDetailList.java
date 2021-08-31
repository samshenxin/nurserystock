package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.list.plugin.*;
import kd.bos.cache.*;
import java.util.*;
import kd.bos.form.control.events.*;
import com.alibaba.druid.util.*;
import kd.bos.form.*;
import kd.bos.form.plugin.*;
import kd.bos.form.events.*;

public class InventoryDetailList extends AbstractListPlugin
{
    private static final String KEY_ITEM_LOAD = "zsf_loaddata";
    DistributeSessionlessCache cache;
    
    public InventoryDetailList() {
        this.cache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("customRegion");
    }
    
    public void beforeItemClick(final BeforeItemClickEvent evt) {
    }
    
    public void afterCreateNewData(final EventObject e) {
        final Map<?, ?> mapParam = (Map<?, ?>)this.getView().getFormShowParameter().getCustomParams();
        final String insidParam = (mapParam.get("insidParam") == null) ? "" : mapParam.get("insidParam").toString();
        final String statusParam = (mapParam.get("statusParam") == null) ? "" : mapParam.get("statusParam").toString();
        final String checkerParam = (mapParam.get("checkerParam") == null) ? "" : mapParam.get("checkerParam").toString();
        final String invstatusParam = (mapParam.get("invstatusParam") == null) ? "" : mapParam.get("invstatusParam").toString();
        this.cache.put("insidParam", (String)insidParam);
        this.cache.put("checkerParam", (String)checkerParam);
        this.cache.put("invstatusParam", (String)invstatusParam);
        if (!"A".equals(statusParam)) {
            this.getView().setVisible(Boolean.valueOf(false), new String[] { "tblnew" });
        }
    }
    
    public void itemClick(final ItemClickEvent evt) {
        super.itemClick(evt);
        if (StringUtils.equals("zsf_loaddata", evt.getItemKey())) {
            final FormShowParameter showParameter = this.getView().getFormShowParameter();
            final Long inventorytaskid = (Long)showParameter.getCustomParam("inventorytaskid");
            final FormShowParameter formShowParameter = new FormShowParameter();
            formShowParameter.setFormId("zsf_inventorylist_select");
            formShowParameter.setCaption("\u8d44\u4ea7\u76d8\u70b9\u6570\u636e");
            formShowParameter.setCustomParam("insidParam", this.cache.get("insidParam"));
            formShowParameter.getOpenStyle().setShowType(ShowType.Modal);
            formShowParameter.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "zsf_loaddata"));
            this.getView().showForm(formShowParameter);
        }
    }
    
    public void closedCallBack(final ClosedCallBackEvent e) {
        super.closedCallBack(e);
        this.getView().updateView();
    }
}
