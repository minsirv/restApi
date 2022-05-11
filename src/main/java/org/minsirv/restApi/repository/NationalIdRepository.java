package org.minsirv.restApi.repository;

import org.minsirv.restApi.entity.NationalId;
import org.minsirv.restApi.repository.filters.NationalIdFilter;

import java.util.List;

public interface NationalIdRepository {

    NationalId getById(String id);

    NationalId save(NationalId nationalId);

    List<NationalId> search(NationalIdFilter filter);

    List<NationalId> findAll();

    void remove(NationalId nationalId);
}
