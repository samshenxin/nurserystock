package jdy.zsf.nurserystock.webappAPIPlugin;

import kd.bos.bill.*;
import kd.bos.entity.api.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import jdy.zsf.nurserystock.model.*;
import kd.bos.dataentity.entity.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.service.webapi.api.*;
import java.util.*;

import org.json.JSONObject;
import org.slf4j.*;

public class TransplantApi implements IBillWebApiPlugin
{
    private static final Logger logger;
    
    public ApiResult doCustomService(final Map<String, Object> params) {
        try {
            TransplantApi.logger.info("TransplantApi");
            JSONObject json = new JSONObject(params);
            if ("query".equals(params.get("action"))) {
                QFilter rfidQFilter = null;
                QFilter qrcodeQFilter = null;
                if (params.get("rfid") != null && !"".equals(params.get("rfid"))) {
                    rfidQFilter = new QFilter("zsf_quondam_rfid", "=", params.get("rfid"));
                }
                if (params.get("qrcode") != null && !"".equals(params.get("qrcode"))) {
                    qrcodeQFilter = new QFilter("zsf_quondam_qrcode", "=", params.get("qrcode"));
                }
                final QFilter[] filters = { rfidQFilter, qrcodeQFilter };
                final DynamicObject[] dynas = BusinessDataServiceHelper.load("zsf_transplant", "id, billno, billstatus, creator, createtime,  zsf_site.name.zh_CN, zsf_name.name.zh_CN, zsf_type.name.zh_CN, zsf_qrcode, zsf_rfid, zsf_before_gps, zsf_after_gps", filters);
                final List<Transplant> repls = new ArrayList<Transplant>();
                if (null != dynas && dynas.length > 0) {
                    for (final DynamicObject dyna : dynas) {
                        final Transplant rep = new Transplant(dyna.getString("id"), dyna.getString("billno"), dyna.getString("billstatus"), dyna.getString("creator"), dyna.getString("createtime"), dyna.getString("zsf_site.name.zh_CN"), dyna.getString("zsf_name.name.zh_CN"), dyna.getString("zsf_type.name.zh_CN"), dyna.getString("zsf_qrcode"), dyna.getString("zsf_rfid"), dyna.getString("zsf_before_gps"), dyna.getString("zsf_after_gps"));
                        repls.add(rep);
                    }
                }
                return ApiResult.success((Object)repls);
            }
            if ("save".equals(params.get("action"))) {
                final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_transplant");
                dynobj.set("zsf_before_gps", params.get("beforeGps"));
                dynobj.set("zsf_after_gps", params.get("afterGps"));
                final Object[] objResult = SaveServiceHelper.save(new DynamicObject[] { dynobj });
                return ApiResult.success((Object)objResult);
            }
            return null;
        }
        catch (Exception e) {
            TransplantApi.logger.error(e.getMessage());
            return ApiException.handleException((Throwable)e);
        }
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)TransplantApi.class);
    }
}
