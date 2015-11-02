package com.aseupc.flattitude.utility_REST;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MetzoDell on 20-10-15.
 */
public class ResultContainer<T> {
    private List<String> returnDescriptions;
    private T template;
    private boolean success;

    public ResultContainer(){
        returnDescriptions = new ArrayList<>();
    }
    public List<String> getReturnDescriptions() {
        return returnDescriptions;
    }

    public void addReason(String reason)
    {
        returnDescriptions.add(reason);
    }
    public T getTemplate() {
        return template;
    }
    public boolean getSucces()
    {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setReturnDescriptions(List<String> returnDescriptions) {
        this.returnDescriptions = returnDescriptions;
    }

    public void setTemplate(T template) {
        this.template = template;
    }
}
