package ua.gabz.dm.auth;

import ua.gabz.dm.entities.enums.CriptoAlgoritm;
import ua.gabz.dm.threads.client.ClientThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by destr on 15.03.2020.
 */
public interface Auth {

    boolean openStream(DataInputStream dis, DataOutputStream dos, ClientThread client);

    boolean openCriptoStream(DataInputStream dis, DataOutputStream dos, ClientThread client);


    boolean cripto(DataInputStream dis, DataOutputStream dos, ClientThread client);

    boolean initClient();

    boolean getClientParametrs(DataInputStream dis, DataOutputStream dos, ClientThread client);

    CriptoAlgoritm getAlgoritm();

    boolean toAutorize(DataInputStream dis, DataOutputStream dos, ClientThread client);


}
