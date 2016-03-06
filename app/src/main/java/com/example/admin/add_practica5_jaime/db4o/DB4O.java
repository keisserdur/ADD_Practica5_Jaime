package com.example.admin.add_practica5_jaime.db4o;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Admin on 05/03/2016.
 */
public class DB4O {
    private ObjectContainer bd;

    public DB4O(Context c) throws IOException {
        Log.v("LOGV","abieta");
        //bd=Db4oEmbedded.openFile(dbConfig(),/*c.getExternalFilesDir(null) +*/ "/ADD_Practica5.db4o");
        bd=Db4oEmbedded.openFile(c.getExternalFilesDir(null) +"/ADD_Practica5.db4o");

    }

    public void insert(Posicion p){
        Log.v("LOGV","insertar");
        bd.store(p);
        bd.commit();
    }

    public List<Posicion> getPosicionList(){
        Log.v("LOGV","getpos");
        Query consulta = bd.query();
        consulta.constrain(new Posicion());
        Log.v("LOGV", "getpos");
        return bd.queryByExample(new Posicion());
    }

    public void cerrar(){
        Log.v("LOGV","cerrar");
        bd.close();
    }

    private EmbeddedConfiguration dbConfig() throws IOException {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new AndroidSupport());
        configuration.common().activationDepth(25);
        configuration.common().objectClass(GregorianCalendar.class).storeTransientFields(true);
        configuration.common().objectClass(GregorianCalendar.class).callConstructor(true);
        configuration.common().exceptionsOnNotStorable(false);
        configuration.common().objectClass(Posicion.class).objectField("date").indexed(true);
        return configuration;
    }

}
