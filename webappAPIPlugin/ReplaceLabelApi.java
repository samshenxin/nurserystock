package jdy.zsf.nurserystock.webappAPIPlugin;

import kd.bos.bill.*;
import java.util.*;
import kd.bos.entity.api.*;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.entity.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.service.webapi.api.*;
import org.slf4j.*;

public class ReplaceLabelApi implements IBillWebApiPlugin
{
    private static final Logger logger;
    
    public ApiResult doCustomService(final Map<String, Object> params) {
        try {
            ReplaceLabelApi.logger.info("ReplaceLabelApi");
            final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_replace_label");
            dynobj.set("zsf_quondam_qrcode", params.get("quondamQrcode"));
            dynobj.set("zsf_quondam_rfid", params.get("quondamRfid"));
            dynobj.set("zsf_replace_qrcode", params.get("replaceQrcode"));
            dynobj.set("zsf_replace_rfid", params.get("replaceRfid"));
            dynobj.set("zsf_remark", params.get("note"));
            dynobj.set("billstatus", (Object)"0");
            dynobj.set("creator", params.get("creator"));
            dynobj.set("createtime", params.get("createtime"));
            final Object[] objResult = SaveServiceHelper.save(new DynamicObject[] { dynobj });
            return ApiResult.success((Object)objResult);
        }
        catch (Exception e) {
            ReplaceLabelApi.logger.error(e.getMessage());
            return ApiException.handleException((Throwable)e);
        }
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)ReplaceLabelApi.class);
    }
}
