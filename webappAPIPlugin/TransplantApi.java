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

public class TransplantApi implements IBillWebApiPlugin {
    private static final Logger logger;

    public ApiResult doCustomService(final Map<String, Object> params) {
        try {
            TransplantApi.logger.info("TransplantApi");
            JSONObject json = new JSONObject(params);
            if ("query".equals(params.get("action"))) {
                QFilter rfidQFilter = null;
                QFilter qrcodeQFilter = null;
                if (json.get("rfid") != null && !"".equals(json.get("rfid"))) {
                    rfidQFilter = new QFilter("zsf_rfid", "=", json.get("rfid"));
                }
                if (json.get("qrcode") != null && !"".equals(json.get("qrcode"))) {
                    qrcodeQFilter = new QFilter("zsf_qrcode", "=", json.get("qrcode"));
                }
                final QFilter[] filters = { rfidQFilter, qrcodeQFilter };
                final DynamicObject[] dynas = BusinessDataServiceHelper.load("zsf_transplant",
                        "id, billno, billstatus, creator, createtime,  zsf_site, zsf_name, zsf_type, zsf_qrcode, zsf_rfid, zsf_before_gps, zsf_after_gps",
                        filters);
                final List<Transplant> repls = new ArrayList<Transplant>();
                if (null != dynas && dynas.length > 0) {
                    for (final DynamicObject dyna : dynas) {
                        final Transplant rep = new Transplant(dyna.getString("id"), dyna.getString("billno"),
                                dyna.getString("billstatus"), dyna.getString("creator"), dyna.getString("createtime"),
                                dyna.getString("zsf_site"), dyna.getString("zsf_name"),
                                dyna.getString("zsf_type"), dyna.getString("zsf_qrcode"),
                                dyna.getString("zsf_rfid"), dyna.getString("zsf_before_gps"),
                                dyna.getString("zsf_after_gps"));
                        repls.add(rep);
                    }
                }
                return ApiResult.success((Object) repls);
            }
            if ("save".equals(json.get("action"))) {
                final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_transplant");
                dynobj.set("zsf_rfid", json.get("nurs_RFID"));
                dynobj.set("zsf_qrcode", json.get("nurs_QRCODE"));
                dynobj.set("zsf_name", json.get("nurs_NAME"));
                dynobj.set("zsf_site", json.get("nurs_SITE"));
                dynobj.set("zsf_type", json.get("nurs_TYPE"));
                dynobj.set("zsf_gps", json.get("nurs_Gps"));
                dynobj.set("zsf_after_gps", json.get("afterGps"));
                dynobj.set("creator", json.get("creator"));
                dynobj.set("createtime", json.get("createtime"));
                final Object[] objResult = SaveServiceHelper.save(new DynamicObject[] { dynobj });
                return ApiResult.success((Object) objResult);
            }
            return null;
        } catch (Exception e) {
            TransplantApi.logger.error(e.getMessage());
            return ApiException.handleException((Throwable) e);
        }
    }

    static {
        logger = LoggerFactory.getLogger((Class) TransplantApi.class);
    }
}
