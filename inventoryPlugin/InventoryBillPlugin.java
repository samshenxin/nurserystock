package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.list.plugin.*;
import kd.bos.entity.datamodel.events.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.entity.*;
import kd.bos.form.*;
import kd.bos.dataentity.utils.*;
import java.util.*;

public class InventoryBillPlugin extends AbstractListPlugin
{
    public void propertyChanged(final PropertyChangedArgs e) {
        final String propName = e.getProperty().getName();
        if (propName.equals("zsf_name")) {
            final String ordername = (String)this.getModel().getValue("zsf_name");
            final QFilter[] filters = { (ordername != null) ? new QFilter("zsf_name", "=", (Object)ordername) : null };
            final Map<Object, DynamicObject> map = (Map<Object, DynamicObject>)BusinessDataServiceHelper.loadFromCache("zsf_inventory", "id,billno,zsf_name", filters);
            String sourcename = "";
            for (final Map.Entry entry : map.entrySet()) {
                final DynamicObject object = (DynamicObject) entry.getValue();
                sourcename = object.getString("zsf_name");
            }
            final FieldTip nameFieldTip = new FieldTip(FieldTip.FieldTipsLevel.Info, FieldTip.FieldTipsTypes.others, "zsf_name", "\u5df2\u5b58\u5728\u76f8\u540c\u7684\u76d8\u70b9\u5355\u540d\u79f0");
            if (StringUtils.equals((CharSequence)sourcename, (CharSequence)ordername)) {
                this.getView().showFieldTip(nameFieldTip);
            }
            else {
                nameFieldTip.setSuccess(true);
                this.getView().showFieldTip(nameFieldTip);
            }
        }
    }
}
