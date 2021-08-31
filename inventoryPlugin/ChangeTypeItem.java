package jdy.zsf.nurserystock.inventoryPlugin;

import java.io.*;
import java.util.*;

class ChangeTypeItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String itemKey;
    protected String itemName;
    protected String formMeta;
    protected List<String> mapFields;
}
