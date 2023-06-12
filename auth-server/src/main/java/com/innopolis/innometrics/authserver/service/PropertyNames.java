package com.innopolis.innometrics.authserver.service;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class PropertyNames {
    private PropertyNames() {
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
            if (src.getPropertyValue(descriptor.getName()) == null) {
                emptyNames.add(descriptor.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
