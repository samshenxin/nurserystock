package jdy.zsf.nurserystock.webappAPIPlugin;

import kd.bos.bill.*;
import kd.bos.entity.api.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import jdy.zsf.nurserystock.model.*;
import kd.bos.service.webapi.api.*;
import kd.bos.dataentity.entity.*;
import java.util.*;
import org.slf4j.*;

public class GetNurseryStockListApiPlugin implements IBillWebApiPlugin {
    private static final Logger logger;

    public ApiResult doCustomService(final Map<String, Object> params) {
        try {
            GetNurseryStockListApiPlugin.logger.info("GetNurseryStockListApiPlugin");
            QFilter rfidQFilter = null;
            QFilter qrcodeQFilter = null;
            final QFilter statusQFilter = new QFilter("zsf_storestatus", "=", (Object) "0");
            if (params.get("rfid") != null && !"".equals(params.get("rfid"))) {
                rfidQFilter = new QFilter("zsf_rfid", "=", params.get("rfid"));
            }
            if (params.get("qrcode") != null && !"".equals(params.get("qrcode"))) {
                qrcodeQFilter = new QFilter("zsf_qrcode", "=", params.get("qrcode"));
            }
            final QFilter[] filters = { statusQFilter, rfidQFilter, qrcodeQFilter };
            final DynamicObject[] dynas = BusinessDataServiceHelper.load("zsf_instorageorderdetail",
                    "id, billno, billstatus, creator.bos_user.name, createtime, zsf_site.name.zh_CN, zsf_name, zsf_type, zsf_qrcode, zsf_rfid, zsf_qty, zsf_dbh, zsf_ground_diameter, zsf_coronal_diameter, zsf_meter_diameter, zsf_height, zsf_storestatus, zsf_estimate_price, zsf_buy_price, zsf_sale_price, zsf_remark, zsf_picture, zsf_checkexpensive, zsf_checklabel, zsf_labelstatus,zsf_insid",
                    filters);
            final List<NurseryStock> nurseryStocks = new ArrayList<NurseryStock>();
            if (null != dynas && dynas.length > 0) {
                for (final DynamicObject dyna : dynas) {
                    final NurseryStock nurs = new NurseryStock(dyna.getString("id"), dyna.getString("billno"),
                            dyna.getString("creator"), dyna.getString("createtime"), dyna.get("zsf_site.name.zh_CN"),
                            dyna.get("zsf_name"), dyna.getString("zsf_type"),
                            dyna.getString("zsf_qrcode"), dyna.getString("zsf_rfid"), dyna.get("zsf_qty"),
                            dyna.get("zsf_dbh"), dyna.get("zsf_ground_diameter"),
                            dyna.getString("zsf_coronal_diameter"), dyna.getString("zsf_meter_diameter"),
                            dyna.getString("zsf_height"), dyna.getString("zsf_storestatus"),
                            dyna.getString("zsf_estimate_price"), dyna.getString("zsf_buy_price"),
                            dyna.getString("zsf_sale_price"), dyna.getString("zsf_remark"),
                            dyna.getString("zsf_picture"), dyna.getString("zsf_checkexpensive"),
                            dyna.getString("zsf_checklabel"), dyna.getString("zsf_labelstatus"),
                            dyna.getString("zsf_insid"), null);
                    nurseryStocks.add(nurs);
                }
            }
            return ApiResult.success((Object) nurseryStocks);
        } catch (Exception e) {
            GetNurseryStockListApiPlugin.logger.error(e.getMessage());
            return ApiException.handleException((Throwable) e);
        }
    }

    static {
        logger = LoggerFactory.getLogger((Class) GetNurseryStockListApiPlugin.class);
    }
}
