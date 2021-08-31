package jdy.zsf.nurserystock.serviceHelper;

import kd.bos.dataentity.entity.*;
import kd.bos.dataentity.*;
import kd.bos.dataentity.Consumer;
import kd.bos.entity.operate.result.*;
import kd.bos.servicehelper.operation.*;
import kd.bos.mvc.*;
import java.util.function.*;
import kd.bos.dlock.*;
import kd.bos.db.tx.*;
import kd.bos.base.*;
import kd.bos.bill.*;
import kd.bos.form.*;
import kd.bos.entity.*;
import kd.bos.orm.query.*;
import kd.bos.servicehelper.*;
import jdy.zsf.nurserystock.utils.*;

public class ABillServiceHelper
{
    public static OperationResult executeOperate(final String operationKey, final String formId, final DynamicObject[] dataEntities, final OperateOption option) {
        return OperationServiceHelper.executeOperate(operationKey, formId, dataEntities, option);
    }
    
    public static OperationResult executeOperate(final String operationKey, final String formId, final Object[] ids, final OperateOption option) {
        return OperationServiceHelper.executeOperate(operationKey, formId, ids, option);
    }
    
    public static IFormView createAddView(final String formId) {
        final FormShowParameter parameter = getShowParameter(formId);
        return createViewByShowParameter(parameter);
    }
    
    public static IFormView createViewByShowParameter(final FormShowParameter parameter) {
        invokeFormServiceMethod(parameter);
        return SessionManager.getCurrent().getView(parameter.getPageId());
    }
    
    public static IFormView createModifyView(final String formId, final String id) {
        final FormShowParameter parameter = getModifyParameter(formId, id);
        return createViewByShowParameter(parameter);
    }
    
    public static OperationResult modifyFormWithLock(final String formId, final String id, final Consumer<IFormView> view, final Function<IFormView, OperationResult> action) {
        final DLock lock = DLock.create(formId + id, "createModifyView" + formId + id);
        lock.lock();
        OperationResult result;
        try {
            final IFormView modifyView = createModifyView(formId, id);
            view.accept(modifyView);
            result = action.apply(modifyView);
            exitView(modifyView);
        }
        finally {
            lock.unlock();
        }
        return result;
    }
    
    public static OperationResult saveOperateWithNoTx(final IFormView view) {
        try (final TXHandle ignored = TX.notSupported()) {
            return saveOperate(view);
        }
    }
    
    public static OperationResult saveOperateWithNoTx(final IFormView view, final boolean autoAudit) {
        try (final TXHandle ignored = TX.notSupported()) {
            return saveOperate(view, autoAudit);
        }
    }
    
    public static OperationResult saveOperate(final IFormView view) {
        OperationResult result;
        try {
            result = view.invokeOperation("save");
        }
        finally {
            exitView(view);
        }
        return result;
    }
    
    public static OperationResult saveOperate(final IFormView view, final boolean autoAudit) {
        OperationResult result = saveOperate(view);
        if (autoAudit && result.isSuccess()) {
            final IFormView modifyView = createModifyView(view.getEntityId(), result.getSuccessPkIds().get(0).toString());
            try {
                result = modifyView.invokeOperation("submit");
                if (result.isSuccess()) {
                    result = modifyView.invokeOperation("audit");
                }
            }
            finally {
                exitView(modifyView);
            }
        }
        return result;
    }
    
    public static FormShowParameter getShowParameter(final String formId) {
        final MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType(formId);
        FormShowParameter parameter;
        if (mainEntityType.getClass().equals(BasedataEntityType.class)) {
            parameter = (FormShowParameter)new BaseShowParameter();
        }
        else if (mainEntityType.getClass().equals(BillEntityType.class)) {
            parameter = (FormShowParameter)new BillShowParameter();
        }
        else {
            parameter = new FormShowParameter();
        }
        parameter.setStatus(OperationStatus.ADDNEW);
        parameter.getOpenStyle().setShowType(ShowType.MainNewTabPage);
        parameter.getOpenStyle().setTargetKey("tabap");
        parameter.setFormId(formId);
        return parameter;
    }
    
    public static FormShowParameter getModifyParameter(final String formId, final String id) {
        final FormShowParameter parameter = getShowParameter(formId);
        if (parameter.getClass().equals(BaseShowParameter.class)) {
            final BaseShowParameter baseShowParameter = (BaseShowParameter)parameter;
            try {
                final MainEntityType mainEntityType = EntityMetadataCache.getDataEntityType(formId);
                final String org = mainEntityType.getMainOrg();
                final String pkName = mainEntityType.getPrimaryKey().getName();
                final DynamicObject object = QueryServiceHelper.queryOne(formId, org, new QFilter[] { new QFilter(pkName, "=", (Object)id) });
                final String orgId = object.getString(org);
                baseShowParameter.setCustomParam("useorgId", (Object)orgId);
            }
            catch (Exception ex) {}
            baseShowParameter.setPkId((Object)id);
            baseShowParameter.setStatus(OperationStatus.EDIT);
        }
        else {
            final BillShowParameter billShowParameter = (BillShowParameter)parameter;
            billShowParameter.setPkId((Object)id);
            billShowParameter.setStatus(OperationStatus.EDIT);
        }
        return parameter;
    }
    
    private static void invokeFormServiceMethod(final FormShowParameter parameter) {
        ReflectUtils.invokeCosmicMethod("kd.bos.service.ServiceFactory", "FormService", "createConfig", parameter);
        ReflectUtils.invokeCosmicMethod("kd.bos.service.ServiceFactory", "FormService", "batchInvokeAction", parameter.getPageId(), "[{\"key\":\"\",\"methodName\":\"loadData\",\"args\":[],\"postData\":[]}]");
    }
    
    public static void exitView(final IFormView view) {
        view.getModel().setDataChanged(false);
        view.close();
    }
}
