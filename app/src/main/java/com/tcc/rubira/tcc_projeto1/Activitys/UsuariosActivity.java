package com.tcc.rubira.tcc_projeto1.Activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tcc.rubira.tcc_projeto1.Entidades.Usuarios;
import com.tcc.rubira.tcc_projeto1.R;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    ListView usersList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Usuarios userSelecionado;

    private List<Usuarios> listUsuarios = new ArrayList<Usuarios>();
    private ArrayAdapter<Usuarios> arrayAdapterUsuarios;


    //ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users, menu);
        getSupportActionBar().setTitle("   Usuários");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        usersList = (ListView) findViewById(R.id.usersList);

        inicializarFirebase();
        eventoDatabase();
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                userSelecionado = (Usuarios) parent.getItemAtPosition(position);
                Intent intentUsers = new Intent(UsuariosActivity.this, consultar_excluir_usuarioActivity.class);
                atualizarDados(userSelecionado);
            }
        });

    }

    private void atualizarDados(Usuarios userSelecionado) {

        Intent intentUsers = new Intent(UsuariosActivity.this, consultar_excluir_usuarioActivity.class);
        intentUsers.putExtra("users-id", userSelecionado.getId());
        intentUsers.putExtra("users-nome", userSelecionado.getNome());
        intentUsers.putExtra("users-sobrenome", userSelecionado.getSobrenome());
        intentUsers.putExtra("users-cpf", userSelecionado.getCpf());
        intentUsers.putExtra("users-email", userSelecionado.getEmail());
        intentUsers.putExtra("users-senha", userSelecionado.getSenha());
        intentUsers.putExtra("users-funcao", userSelecionado.getFuncao());
        startActivity(intentUsers);
    }

    private void eventoDatabase() {
        //Chama os nós filhos da entidade "Usuários" e toda a alteração feita na ramificaççao "usuarios" atualizara os dados na lista
        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            //Este método tras todos os elementos existentes nas ramificações da entidade "Usuarios"
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Limpa a lista usuarios sempre q houver atualização dos dados
                listUsuarios.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    //A cada laço trara em ordem do database todos os objetos usuarios e salvara na "listUsuarios"
                    Usuarios u = objSnapshot.getValue(Usuarios.class);
                    listUsuarios.add(u);

                }
                arrayAdapterUsuarios = new ArrayAdapter<Usuarios>(UsuariosActivity.this, android.R.layout.simple_list_item_1, listUsuarios);
                usersList.setAdapter(arrayAdapterUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarFirebase() {
        if (firebaseDatabase == null) {
            FirebaseApp.initializeApp(UsuariosActivity.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }
    }
    }
