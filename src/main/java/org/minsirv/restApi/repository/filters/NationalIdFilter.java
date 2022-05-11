package org.minsirv.restApi.repository.filters;

import java.util.Date;

public class NationalIdFilter {
    private Date from;
    private Date to;
    private Boolean hasErrors;

    public NationalIdFilter() {
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Boolean getHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(Boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
