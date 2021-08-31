package jdy.zsf.nurserystock.webappAPIPlugin;

import kd.bos.bill.*;
import kd.bos.entity.api.*;
import kd.bos.servicehelper.*;
import kd.bos.orm.query.*;
import jdy.zsf.nurserystock.model.*;
import kd.bos.service.webapi.api.*;
import kd.bos.dataentity.entity.*;
import java.util.*;
import org.slf4j.*;

public class OutstorageorderdetailApi implements IBillWebApiPlugin {
    private static final Logger logger;

    public ApiResult doCustomService(final Map<String, Object> params) {
        try {
            OutstorageorderdetailApi.logger.info("OutstorageorderdetailApi");
            final DynamicObject[] dynas = BusinessDataServiceHelper.load("zsf_outstorageorderdetail",
                    "id, billno, billstatus, creator, createtime,  zsf_name.name.zh_CN, zsf_type.name.zh_CN, zsf_qrcode, zsf_rfid, zsf_qty, zsf_dbh, zsf_ground_diameter, zsf_coronal_diameter, zsf_meter_diameter, zsf_height, zsf_storestatus, zsf_estimate_price, zsf_buy_price, zsf_sale_price, zsf_remark, zsf_picture,zsf_outsid",
                    (QFilter[]) null);
            final List<NurseryStock> nurseryStocks = new ArrayList<NurseryStock>();
            if (null != dynas && dynas.length > 0) {
                for (final DynamicObject dyna : dynas) {
                    final NurseryStock nurs = new NurseryStock(dyna.getString("id"), dyna.getString("billno"),
                            dyna.getString("creator"), dyna.getString("createtime"), null,
                            dyna.get("zsf_name.name.zh_CN"), dyna.get("zsf_type.name.zh_CN"),
                            dyna.getString("zsf_qrcode"), dyna.getString("zsf_rfid"), dyna.get("zsf_qty"),
                            dyna.get("zsf_dbh"), dyna.get("zsf_ground_diameter"),
                            dyna.getString("zsf_coronal_diameter"), dyna.getString("zsf_meter_diameter"),
                            dyna.getString("zsf_height"), dyna.getString("zsf_storestatus"),
                            dyna.getString("zsf_estimate_price"), dyna.getString("zsf_buy_price"),
                            dyna.getString("zsf_sale_price"), dyna.getString("zsf_remark"),
                            dyna.getString("zsf_picture"), null, null, null, null, dyna.getString("zsf_outsid"));
                    nurseryStocks.add(nurs);
                }
            }
            return ApiResult.success((Object) nurseryStocks);
        } catch (Exception e) {
            OutstorageorderdetailApi.logger.error(e.getMessage());
            return ApiException.handleException((Throwable) e);
        }
    }

    static {
        logger = LoggerFactory.getLogger((Class) OutstorageorderdetailApi.class);
    }
}
