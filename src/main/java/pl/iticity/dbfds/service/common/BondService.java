package pl.iticity.dbfds.service.common;

import pl.iticity.dbfds.model.BaseModel;
import pl.iticity.dbfds.model.common.Bond;
import pl.iticity.dbfds.model.common.BondType;
import pl.iticity.dbfds.model.common.ObjectType;
import pl.iticity.dbfds.service.ScopedService;
import pl.iticity.dbfds.service.Service;

import java.util.List;

public interface BondService extends ScopedService<Bond> {

    public Bond createBond(String aId, Class<? extends BaseModel> aClass, String aRevision, String bId, Class<? extends BaseModel> bClass, String bRevision, BondType bondType);

    public void deleteBond(String id);

    public List<Bond> findBondsForObject(String oId, Class oClass, List<ObjectType> includes);

    public void deleteBondsOfObject(String oId, Class oClass);

    public BaseModel findObjectForLink(String linkId,boolean first);

}
