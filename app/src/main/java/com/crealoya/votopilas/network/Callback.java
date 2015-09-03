package com.crealoya.votopilas.network;

/**
 * Created by @pablopantaleon on 7/29/15.
 */
public interface Callback<T> {

    void done(boolean success, T data);

}
