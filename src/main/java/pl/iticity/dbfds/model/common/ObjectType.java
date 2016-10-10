package pl.iticity.dbfds.model.common;

import com.google.common.collect.Lists;

import java.util.List;

public enum ObjectType {

    DOCUMENT, PRODUCT, PROJECT, QUOTATION;

    public static List<ObjectType> getAll() {
        return Lists.newArrayList(values());
    }

    public static List<ObjectType> getNonDic() {
        return Lists.newArrayList(PRODUCT,PROJECT,QUOTATION);
    }
}
