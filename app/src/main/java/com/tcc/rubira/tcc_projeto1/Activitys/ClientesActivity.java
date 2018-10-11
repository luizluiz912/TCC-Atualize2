package com.tcc.rubira.tcc_projeto1.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.tcc.rubira.tcc_projeto1.Entidades.Clientes;
import com.tcc.rubira.tcc_projeto1.Entidades.Usuarios;
import com.tcc.rubira.tcc_projeto1.R;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {
    ListView clienteList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Clientes clientSelecionado;

    private List<Clientes> listClientes = new ArrayList<Clientes>();
    private ArrayAdapter<Clientes> arrayAdapterClientes;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users, menu);
        getSupportActionBar().setTitle("   Clientes");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent mIntent = new Intent(ClientesActivity.this, cadastro_clienteActivity.class);
                startActivity(mIntent);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        clienteList = (ListView) findViewById(R.id.clientList);

        inicializarFirebase();
        eventoDatabase();
        clienteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                clientSelecionado = (Clientes) parent.getItemAtPosition(position);
                Intent intentClient = new Intent(ClientesActivity.this, consultar_excluir_clientesActivity.class);
                atualizarDados(clientSelecionado);
            }
        });

    }

    private void eventoDatabase() {
        //Chama os nós filhos da entidade "Clientes" e toda a alteração feita na ramificaççao "clientes" atualizara os dados na lista
        databaseReference.child("Clientes").addValueEventListener(new ValueEventListener() {
            //Este método tras todos os elementos existentes nas ramificações da entidade "Clientes"
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Limpa a lista de clientes sempre q houver atualização dos dados
                listClientes.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    //A cada laço trara em ordem do database todos os objetos cliente e salvara na "listClients"
                    Clientes u = objSnapshot.getValue(Clientes.class);
                    listClientes.add(u);
                }
                arrayAdapterClientes = new ArrayAdapter<Clientes>(ClientesActivity.this, android.R.layout.simple_list_item_1, listClientes);
                clienteList.setAdapter(arrayAdapterClientes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void atualizarDados(Clientes clientSelecionado) {

        Intent intentClients = new Intent(ClientesActivity.this, consultar_excluir_clientesActivity.class);
        intentClients.putExtra("client-id", clientSelecionado.getId());
        intentClients.putExtra("client-nome", clientSelecionado.getNome());
        intentClients.putExtra("client-sobrenome", clientSelecionado.getSobrenome());
        intentClients.putExtra("client-cpf", clientSelecionado.getCpf());
        intentClients.putExtra("client-email", clientSelecionado.getEmail());
        intentClients.putExtra("client-datnasc", clientSelecionado.getData_nasc());
        intentClients.putExtra("client-tel", clientSelecionado.getTelefone());
        intentClients.putExtra("client-cel", clientSelecionado.getCelular());
        startActivity(intentClients);
    }

    private void inicializarFirebase() {
        if (firebaseDatabase == null) {
            FirebaseApp.initializeApp(ClientesActivity.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }
    }
}
