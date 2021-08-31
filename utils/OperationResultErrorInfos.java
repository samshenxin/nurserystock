package jdy.zsf.nurserystock.utils;

import kd.bos.entity.operate.result.*;
import java.util.*;

public class OperationResultErrorInfos
{
    private String getOperationResultErrorInfos(final OperationResult operationResult) {
        if (operationResult.isSuccess()) {
            return "";
        }
        final List<IOperateInfo> errorInfos = (List<IOperateInfo>)operationResult.getAllErrorOrValidateInfo();
        final int size = errorInfos.size() + operationResult.getSuccessPkIds().size();
        if (size > 1) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0, len = errorInfos.size(); i < 5 && i < len; ++i) {
                stringBuilder.append(errorInfos.get(i).getMessage());
            }
            return stringBuilder.toString();
        }
        if (!errorInfos.isEmpty()) {
            final OperateErrorInfo errorInfo = (OperateErrorInfo)errorInfos.get(0);
            final String msg = (errorInfo.getMessage() == null) ? "" : errorInfo.getMessage();
            return msg;
        }
        final String msg2 = (operationResult.getMessage() == null) ? "" : operationResult.getMessage();
        return msg2;
    }
}
