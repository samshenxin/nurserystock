package jdy.zsf.nurserystock.webappAPIPlugin;

import kd.bos.bill.*;
import java.util.*;
import kd.bos.entity.api.*;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.*;
import kd.bos.dataentity.entity.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.service.webapi.api.*;

import org.json.JSONObject;
import org.slf4j.*;

import jdy.zsf.nurserystock.model.ReplaceLabel;

public class ReplaceLabelApi implements IBillWebApiPlugin {
    private static final Logger logger;

    public ApiResult doCustomService(final Map<String, Object> params) {
        JSONObject json = new JSONObject(params);
        ReplaceLabelApi.logger.info("ReplaceLabelApi");
        try {
            if ("query".equals(params.get("action"))) {
                QFilter rfidQFilter = null;
                QFilter qrcodeQFilter = null;
                if (json.get("rfid") != null && !"".equals(json.get("rfid"))) {
                    rfidQFilter = new QFilter("zsf_quondam_rfid", "=", json.get("rfid"));
                }
                if (json.get("qrcode") != null && !"".equals(json.get("qrcode"))) {
                    qrcodeQFilter = new QFilter("zsf_quondam_qrcode", "=", json.get("qrcode"));
                }
                final QFilter[] filters = { rfidQFilter, qrcodeQFilter };
                final DynamicObject[] dynas = BusinessDataServiceHelper.load("zsf_replace_label",
                        "id, billno, billstatus, creator, createtime, zsf_name, zsf_type, zsf_quondam_qrcode, zsf_quondam_rfid, zsf_replace_qrcode, zsf_replace_rfid, zsf_remark",
                        filters);
                final List<ReplaceLabel> repls = new ArrayList<ReplaceLabel>();
                if (null != dynas && dynas.length > 0) {
                    for (final DynamicObject dyna : dynas) {
                        final ReplaceLabel rep = new ReplaceLabel(dyna.getString("id"), dyna.getString("billno"),
                                dyna.getString("billstatus"), dyna.getString("creator"), dyna.getString("createtime"),
                                dyna.getString("zsf_name"), dyna.getString("zsf_type"),
                                dyna.getString("zsf_quondam_qrcode"), dyna.getString("zsf_quondam_rfid"),
                                dyna.getString("zsf_replace_qrcode"), dyna.getString("zsf_replace_rfid"),
                                dyna.getString("zsf_remark"));
                        repls.add(rep);
                    }
                }
                return ApiResult.success((Object) repls);

            }
            if ("save".equals(json.get("action"))) {
                final DynamicObject dynobj = BusinessDataServiceHelper.newDynamicObject("zsf_replace_label");
                dynobj.set("zsf_quondam_qrcode", json.get("quondamQrcode"));
                dynobj.set("zsf_quondam_rfid", json.get("quondamRfid"));
                dynobj.set("zsf_replace_qrcode", json.get("replaceQrcode"));
                dynobj.set("zsf_replace_rfid", json.get("replaceRfid"));
                dynobj.set("zsf_remark", json.get("note"));
                dynobj.set("billstatus", "A");
                dynobj.set("creator", json.get("creator"));
                dynobj.set("createtime", json.get("createtime"));
                final Object[] objResult = SaveServiceHelper.save(new DynamicObject[] { dynobj });
                return ApiResult.success((Object) objResult);
            }
            return null;
        } catch (Exception e) {
            ReplaceLabelApi.logger.error(e.getMessage());
            return ApiException.handleException((Throwable) e);
        }
    }

    static {
        logger = LoggerFactory.getLogger((Class) ReplaceLabelApi.class);
    }
}
