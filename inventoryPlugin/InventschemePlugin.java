package jdy.zsf.nurserystock.inventoryPlugin;

import kd.bos.bill.*;
import kd.bos.dataentity.entity.*;
import com.jdy.ek.develop.plugin.util.*;
import java.math.*;
import kd.bos.form.control.events.*;
import kd.bos.form.plugin.*;
import kd.bos.form.*;
import kd.bos.list.*;
import kd.bos.orm.query.*;
import kd.bos.form.events.*;
import kd.bos.dataentity.utils.*;
import kd.bos.entity.datamodel.*;
import kd.bos.servicehelper.*;
import java.util.*;
import kd.bos.logging.*;

public class InventschemePlugin extends AbstractFormPlugin implements IBillPlugin
{
    private static final Log log;
    private static String[] realFieldArr;
    private static final String KEY_MAIN_ADD_ROW = "main_add_row";
    
    public void registerListener(final EventObject evn) {
        super.registerListener(evn);
        this.addItemClickListeners(new String[] { "tbmain", "main_change_toolbarap" });
    }
    
    public void initialize() {
        super.initialize();
    }
    
    private void setOprationCardCache() {
        final IDataModel model = this.getModel();
        final Set<Object> realCardIds = new HashSet<Object>();
        if (model.getEntryRowCount("main_changebillentry") > 0 || model.getEntryRowCount("other_changebillentry") > 0) {
            for (final DynamicObject dy : model.getEntryEntity("main_changebillentry")) {
                final DynamicObject realCard = dy.getDynamicObject("m_realcard");
                final DynamicObject depreuse = dy.getDynamicObject("m_depreuse");
                if (realCard != null) {
                    final Object depruseId = (depreuse == null) ? Long.valueOf(0L) : depreuse.getPkValue();
                    realCardIds.add(String.format("%s_%s", realCard.getPkValue(), depruseId));
                }
            }
            for (final DynamicObject dy : model.getEntryEntity("other_changebillentry")) {
                final DynamicObject realCard = dy.getDynamicObject("o_realcard");
                final DynamicObject depreuse = dy.getDynamicObject("o_depreuse");
                if (realCard != null) {
                    final Object depruseId = (depreuse == null) ? Long.valueOf(0L) : depreuse.getPkValue();
                    realCardIds.add(String.format("%s_%s", realCard.getPkValue(), depruseId));
                }
            }
        }
        else {
            for (final DynamicObject dy : model.getEntryEntity("fieldentry")) {
                final DynamicObject realCard = dy.getDynamicObject("realcard1");
                final DynamicObject depreuse = dy.getDynamicObject("depreuse1");
                if (realCard != null) {
                    final Object depruseId = (depreuse == null) ? Long.valueOf(0L) : depreuse.getPkValue();
                    realCardIds.add(String.format("%s_%s", realCard.getPkValue(), depruseId));
                }
            }
        }
        if (realCardIds.size() > 0) {
            final String realCardsStr = SerializationUtils.serializeToBase64((Object)realCardIds);
            this.getPageCache().put("cacherealcardopration", realCardsStr);
        }
    }
    
    private void setAllChangeFieldDetail() {
        this.setChangeFieldDetail("main_changebillentry");
        this.setRealCardDetailByEntry();
    }
    
    private void setChangeFieldDetail(final String entityName) {
        final IDataModel model = this.getModel();
        final int rowCount = model.getEntryRowCount(entityName);
        if (rowCount == 0) {
            return;
        }
    }
    
    Boolean checkChangeStatus(final Object befValue, final Object aftValue) {
        Boolean result = true;
        if (befValue instanceof Integer || befValue instanceof Long) {
            final Long bef = (Long)befValue;
            final Long aft = (Long)aftValue;
            if (aft.equals(0L) || bef.equals(aft)) {
                result = false;
            }
        }
        else if (befValue instanceof BigDecimal && aftValue instanceof BigDecimal) {
            final BigDecimal bef2 = new BigDecimal(befValue.toString());
            final BigDecimal aft2 = new BigDecimal(aftValue.toString());
            if (bef2.compareTo(aft2) == 0) {
                result = false;
            }
        }
        else {
            final String bef3 = befValue.toString();
            final String aft3 = (aftValue != null) ? aftValue.toString() : null;
            if (aft3 == null) {
                result = false;
            }
            else if (bef3.equals(aft3) || aft3.isEmpty()) {
                result = false;
            }
        }
        return result;
    }
    
    private void setRealCardDetailByEntry() {
        final IDataModel model = this.getModel();
        model.beginInit();
        final Set<Object> realCardIds = this.getReadCardIDByEntity("main_changebillentry");
        for (final Object realCardId : realCardIds) {
            final int rRowIndex = model.createNewEntryRow("realentry");
            model.setEntryCurrentRowIndex("realentry", rRowIndex);
            model.setValue("realcard", realCardId, rRowIndex);
        }
        model.endInit();
    }
    
    Set<Object> getReadCardIDByEntity(final String entityName) {
        final IDataModel model = this.getModel();
        final Set<Object> realCardIds = new HashSet<Object>();
        for (int rowcount = model.getEntryRowCount(entityName), i = 0; i < rowcount; ++i) {
            final DynamicObject realCard = (DynamicObject)model.getValue("m_insid", i);
            if (realCard != null) {
                realCardIds.add(realCard.getPkValue());
            }
        }
        return realCardIds;
    }
    
    public void itemClick(final ItemClickEvent evt) {
        if ("main_add_row".equals(evt.getItemKey())) {
            this.showNurserystock();
        }
    }
    
    public void showNurserystock() {
        final ListShowParameter para = ShowFormHelper.createShowListForm("zsf_instorage_order_base", true);
        para.setCloseCallBack(new CloseCallBack((IFormPlugin)this, "callbackrealcardform"));
        final List<QFilter> filterList = this.getSelectFilter();
        if (filterList != null && filterList.size() > 0) {
            para.setListFilterParameter(new ListFilterParameter((List)filterList, (String)null));
        }
        para.setHasRight(true);
        para.getOpenStyle().setShowType(ShowType.Modal);
        this.getView().showForm((FormShowParameter)para);
    }
    
    private List<QFilter> getSelectFilter() {
        final IDataModel model = this.getModel();
        final List<QFilter> rangeFilterList = new ArrayList<QFilter>();
        final Set<Object> nurserystockIds = new HashSet<Object>();
        final Set<Object> mNutdrtystockIds = this.getNurserystockIDByEntity("main_changebillentry");
        nurserystockIds.addAll(mNutdrtystockIds);
        if (nurserystockIds.size() > 0) {
            rangeFilterList.add(new QFilter("id", "not in", (Object)nurserystockIds));
        }
        rangeFilterList.add(new QFilter("zsf_labelstatus", "=", (Object)"0"));
        return rangeFilterList;
    }
    
    Set<Object> getNurserystockIDByEntity(final String entityName) {
        final IDataModel model = this.getModel();
        final Set<Object> nurserystockIds = new HashSet<Object>();
        for (int rowcount = model.getEntryRowCount(entityName), i = 0; i < rowcount; ++i) {
            final DynamicObject nurserystock = (DynamicObject)model.getValue("m_insid", i);
            if (nurserystock != null) {
                nurserystockIds.add(nurserystock.getPkValue());
            }
        }
        return nurserystockIds;
    }
    
    public void closedCallBack(final ClosedCallBackEvent evt) {
        super.closedCallBack(evt);
        final Object returnData = evt.getReturnData();
        if (returnData == null) {
            return;
        }
        if (StringUtils.equalsIgnoreCase((CharSequence)evt.getActionId(), (CharSequence)"callbackrealcardf7") || StringUtils.equalsIgnoreCase((CharSequence)evt.getActionId(), (CharSequence)"callbackrealcardform")) {
            if (returnData instanceof ListSelectedRowCollection) {
                final ListSelectedRowCollection returnDataList = (ListSelectedRowCollection)returnData;
                this.getPageCache().put("cacherealcardf7flag", "1");
                if (returnDataList.size() > 0) {
                    final Set<Object> nurserystockPKSet = this.getReturnCardPKSet(returnDataList);
                    this.insertSelectRow(nurserystockPKSet, null);
                }
                this.getPageCache().remove("cacherealcardf7flag");
            }
        }
        else {
            InventschemePlugin.log.info("else\u65b9\u6cd5");
        }
    }
    
    private Set<Object> getReturnCardPKSet(final ListSelectedRowCollection selectlist) {
        final Set<Object> nurserystockIds = new HashSet<Object>();
        for (final ListSelectedRow row : selectlist) {
            nurserystockIds.add(row.getPrimaryKeyValue());
        }
        return nurserystockIds;
    }
    
    public void insertSelectRow(final Set<Object> nurserystockPKSet, final Set<Object> cardAndDepeuses) {
        if (nurserystockPKSet.size() > 0) {
            final String curRowIdStr = this.getPageCache().get("cahcerealcardf7curvalue");
            final long oldNurserystockId = (curRowIdStr == null || curRowIdStr.isEmpty()) ? 0L : Long.parseLong(curRowIdStr);
            final Map<String, DynamicObject> nurserystockMap = this.queryRealCards(nurserystockPKSet);
            this.insertSelectRowByReal(nurserystockPKSet, oldNurserystockId, "main_changebillentry", nurserystockMap);
        }
    }
    
    public void insertSelectRowByReal(final Set<Object> nurserystockPKSet, final long oldNurserystockId, final String entityName, final Map<String, DynamicObject> nurserystockMap) {
        if (nurserystockPKSet.size() == 0) {
            return;
        }
        final IDataModel model = this.getModel();
        final int curIndex = this.getCurRealIndex(entityName, oldNurserystockId, Boolean.TRUE);
        int startIndex = curIndex + 1;
        final List<ChangeTypeItem> changeTypeItems = this.getChangeTypeItemsByCache(entityName);
        final Boolean hasChangeItem = this.checkChangeItem(entityName, changeTypeItems);
        if (!hasChangeItem) {
            return;
        }
        model.beginInit();
        for (final Object nurserystockId : nurserystockPKSet) {
            final DynamicObject nurserystock = nurserystockMap.get(nurserystockId.toString());
            if (oldNurserystockId == 0L && "other_changebillentry".equals(entityName)) {
                startIndex = this.getCurRealIndex(entityName, Long.parseLong(nurserystockId.toString()), true) + 1;
            }
            startIndex = model.insertEntryRow(entityName, startIndex);
            model.setValue("zsf_qrcode", nurserystock.get("zsf_qrcode"), startIndex);
            model.setValue("zsf_rfid", nurserystock.get("zsf_rfid"), startIndex);
            model.setValue("zsf_site", nurserystock.get("zsf_site"), startIndex);
            model.setValue("zsf_name", nurserystock.get("zsf_name"), startIndex);
            model.setValue("zsf_qty", nurserystock.get("zsf_qty"), startIndex);
            model.setValue("zsf_labelstatus", nurserystock.get("zsf_labelstatus"), startIndex);
            ++startIndex;
        }
        final Set<String> idStrs = new HashSet<String>();
        idStrs.add(Long.toString(oldNurserystockId));
        model.endInit();
        this.getView().updateView(entityName);
    }
    
    private int getCurRealIndex(final String entityName, final long value, final Boolean containsNull) {
        final IDataModel model = this.getModel();
        int curIndex = 0;
        final int entryRows = model.getEntryRowCount(entityName);
        for (int i = 0; i < entryRows; ++i) {
            final DynamicObject nurserystock = (DynamicObject)model.getValue("m_insid", i);
            if (containsNull || nurserystock != null) {
                if (nurserystock != null && nurserystock.getLong("id") == value) {
                    break;
                }
                if (nurserystock == null && value == 0L) {
                    break;
                }
                ++curIndex;
            }
        }
        if (entryRows - 1 < curIndex) {
            curIndex = entryRows - 1;
        }
        return curIndex;
    }
    
    public int removeOldRows(final String entryName, final String fieldName, final Set<String> values, final Boolean initFalg) {
        int result = 0;
        if (values == null || values.size() == 0) {
            return result;
        }
        final IDataModel model = this.getModel();
        for (int rowcount = model.getEntryRowCount(entryName), i = 0; i < rowcount; ++i) {
            final Object pk = this.getPK(fieldName, i);
            if (pk == null || values.contains(pk.toString())) {
                if (initFalg) {
                    model.beginInit();
                    model.deleteEntryRow(entryName, i);
                    model.endInit();
                }
                else {
                    model.deleteEntryRow(entryName, i);
                }
                --i;
                --rowcount;
                ++result;
            }
        }
        this.getView().updateView(entryName);
        return result;
    }
    
    public Object getPK(final String fieldName, final int index) {
        final IDataModel model = this.getModel();
        Object pk = null;
        final Object value = model.getValue(fieldName, index);
        if (value instanceof DynamicObject) {
            pk = ((DynamicObject)value).getPkValue();
        }
        else if (value instanceof Long) {
            pk = value;
        }
        return pk;
    }
    
    private List<ChangeTypeItem> getChangeTypeItemsByCache(final String entityname) {
        List<ChangeTypeItem> changeItems = new ArrayList<ChangeTypeItem>();
        if (this.getPageCache().get(entityname) == null) {
            return changeItems;
        }
        changeItems = (List<ChangeTypeItem>)SerializationUtils.deSerializeFromBase64(this.getPageCache().get(entityname));
        return changeItems;
    }
    
    public Map<String, DynamicObject> queryRealCards(final Set<Object> nurserystockPKSet) {
        final QFilter[] filters = { new QFilter("id", "in", (Object)nurserystockPKSet) };
        final Set<String> realFieldSet = new HashSet<String>();
        realFieldSet.addAll(Arrays.asList(InventschemePlugin.realFieldArr));
        final Map<String, Set<String>> changeItems = this.getAllChangeTypeItems();
        if (changeItems != null && changeItems.get("zsf_instorageorderdetail") != null) {
            realFieldSet.addAll(changeItems.get("zsf_instorageorderdetail"));
        }
        final String selector = String.join(",", realFieldSet);
        final DynamicObject[] realCards = BusinessDataServiceHelper.load("zsf_instorageorderdetail", selector, filters);
        final Map<String, DynamicObject> realCardMap = new HashMap<String, DynamicObject>(realCards.length);
        for (final DynamicObject nurserystock : realCards) {
            final String nurserystockId = nurserystock.getPkValue().toString();
            realCardMap.put(nurserystockId, nurserystock);
        }
        return realCardMap;
    }
    
    Boolean checkChangeItem(final String entityName, final List<ChangeTypeItem> changeTypeItems) {
        Boolean result = Boolean.FALSE;
        if ("other_changebillentry".equals(entityName)) {
            for (final ChangeTypeItem item : changeTypeItems) {
                if (item.formMeta.equals("fa_card_fin")) {
                    result = Boolean.TRUE;
                    break;
                }
            }
        }
        else {
            result = Boolean.TRUE;
        }
        return result;
    }
    
    private Map<String, Set<String>> getAllChangeTypeItems() {
        final Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        final List<ChangeTypeItem> mainChangeItems = this.getChangeTypeItemsByCache("main_changebillentry");
        final List<ChangeTypeItem> otherChangeItems = this.getChangeTypeItemsByCache("other_changebillentry");
        for (final ChangeTypeItem changeItem : mainChangeItems) {
            if (result.containsKey(changeItem.formMeta)) {
                result.get(changeItem.formMeta).add(changeItem.itemKey);
            }
            else {
                final Set<String> items = new HashSet<String>();
                items.add(changeItem.itemKey);
                result.put(changeItem.formMeta, items);
            }
        }
        for (final ChangeTypeItem changeItem : otherChangeItems) {
            if (result.containsKey(changeItem.formMeta)) {
                result.get(changeItem.formMeta).add(changeItem.itemKey);
            }
            else {
                final Set<String> items = new HashSet<String>();
                items.add(changeItem.itemKey);
                result.put(changeItem.formMeta, items);
            }
        }
        return result;
    }
    
    static {
        log = LogFactory.getLog((Class)InventschemePlugin.class);
        InventschemePlugin.realFieldArr = new String[] { "id", "zsf_qrcode", "zsf_rfid", "zsf_site", "zsf_name", "zsf_type", "zsf_qty", "zsf_labelstatus" };
    }
}
