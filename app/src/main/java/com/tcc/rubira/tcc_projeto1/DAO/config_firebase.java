package com.tcc.rubira.tcc_projeto1.DAO;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;


public class config_firebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autentication;

    public static DatabaseReference getFirebase(){
        if(referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutentication(){
        if(autentication == null){
            autentication = FirebaseAuth.getInstance();
        }
        return autentication;
    }



}
