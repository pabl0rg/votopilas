package com.crealoya.votopilas.util;

import com.crealoya.votopilas.model.Politician;

/**
 * Created by JOSUE on 01-Aug-15.
 */
public class StringManager {

    public static String getFullName(Politician politician) {
        return getFullName(politician.name, politician.lastname);
    }

    public static String getFullName(String name, String lastname) {
        StringBuilder nameBld = new StringBuilder();
        nameBld.append(name);
        nameBld.append(" ");
        nameBld.append(lastname);
        return nameBld.toString();
    }
}
