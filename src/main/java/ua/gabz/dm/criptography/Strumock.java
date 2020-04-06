package ua.gabz.dm.criptography;

/**
 * Created by destr on 01.04.2020.
 */
public class Strumock implements Cripography {

    ///TODO(1) add Struock native implementation for cripted Stream
    ///TODO(2) add RSA implementation for saving to DB

    @Override
    public byte[] encript(byte[] data) {
        return data;
    }

    @Override
    public byte[] decript(byte[] data) {
        return data;
    }
}
