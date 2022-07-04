package com.mhandharbeni.e_parking.utils;

import java.util.ArrayList;
import java.util.List;

public class UtilList<T> {

    @SuppressWarnings("unchecked")
    public List<T> convertObjectToList(Object obj) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List) {
            result.addAll(((List<T>) obj));
        }
        return result;
    }
}
