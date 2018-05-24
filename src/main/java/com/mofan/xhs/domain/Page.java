package com.mofan.xhs.domain;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
    private Integer limit;
    private Integer offset;
    private Integer total;

    private T condition;
    private List<T> results;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
