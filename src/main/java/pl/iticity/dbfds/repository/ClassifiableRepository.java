package pl.iticity.dbfds.repository;

import pl.iticity.dbfds.model.common.Classification;

import java.util.List;

public interface ClassifiableRepository<MODEL> {

    public List<MODEL> findByClassification(Classification classification);

}
