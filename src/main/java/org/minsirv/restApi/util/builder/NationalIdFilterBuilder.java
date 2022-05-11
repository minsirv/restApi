package org.minsirv.restApi.util.builder;

import org.minsirv.restApi.repository.filters.NationalIdFilter;

import java.util.Date;

public class NationalIdFilterBuilder {

    private Date from;
    private Date to;
    private Boolean hasErrors;

    public NationalIdFilterBuilder() {
    }

    public NationalIdFilterBuilder to(Date to) {
        this.to = to;
        return this;
    }

    public NationalIdFilterBuilder from(Date from) {
        this.from = from;
        return this;
    }

    public NationalIdFilterBuilder hasErrors(Boolean hasErrors) {
        this.hasErrors = hasErrors;
        return this;
    }

    public NationalIdFilter build() {
        NationalIdFilter filter = new NationalIdFilter();
        filter.setFrom(from);
        filter.setTo(to);
        filter.setHasErrors(hasErrors);
        return filter;
    }
}
