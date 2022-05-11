package org.minsirv.restApi.controller.response;

import org.minsirv.restApi.entity.NationalId;
import org.minsirv.restApi.enumerators.Messages;

import java.util.ArrayList;
import java.util.List;

public class ValidationSummary {
    private final List<NationalId> validNationalIdList;
    private final List<NationalId> invalidNationalIdList;

    public ValidationSummary() {
        validNationalIdList = new ArrayList<>();
        invalidNationalIdList = new ArrayList<>();
    }

    public String getSummary() {
        if (!validNationalIdList.isEmpty() && !invalidNationalIdList.isEmpty())
            return Messages.SOME_INVALID.getValue();
        if (!validNationalIdList.isEmpty())
            return Messages.ALL_VALID.getValue();
        return Messages.ALL_INVALID.getValue();
    }

    public List<NationalId> getValidNationalIdList() {
        return validNationalIdList;
    }

    public List<NationalId> getInvalidNationalIdList() {
        return invalidNationalIdList;
    }

}
