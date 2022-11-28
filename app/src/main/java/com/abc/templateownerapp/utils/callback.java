package com.abc.templateownerapp.utils;

public interface callback<T1> {
    public void onSucess(T1 t1);
    public void onError(Exception e);
}
