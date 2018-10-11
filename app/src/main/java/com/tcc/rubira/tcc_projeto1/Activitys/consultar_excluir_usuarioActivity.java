package com.tcc.rubira.tcc_projeto1.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tcc.rubira.tcc_projeto1.Entidades.Clientes;
import com.tcc.rubira.tcc_projeto1.Entidades.Usuarios;
import com.tcc.rubira.tcc_projeto1.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class consultar_excluir_usuarioActivity extends AppCompatActivity {

    private EditText nome, sobrenome, cpf, email, senha, func;
    private Button btnEditar;
    String id;

    private Usuarios usuarios;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_excluir_usuario);

        nome = (EditText) findViewById(R.id.nome);
        sobrenome = (EditText) findViewById(R.id.sobrenome);
        cpf = (EditText) findViewById(R.id.cpf);
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        func = (EditText) findViewById(R.id.func);
        btnEditar = (Button) findViewById(R.id.btnEditar) ;

        Intent in = getIntent();
        id = in.getStringExtra("users-id");

        nome.setText(in.getStringExtra("users-nome"));
        sobrenome.setText(in.getStringExtra("users-sobrenome"));
        cpf.setText(in.getStringExtra("users-cpf"));
        email.setText(in.getStringExtra("users-email"));
        senha.setText(in.getStringExtra("users-senha"));
        func.setText(in.getStringExtra("users-funcao"));
        cpf.setEnabled(false);
        func.setEnabled(false);
        inicializarFirebase();

        btnEditar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(!TextUtils.isEmpty(nome.getText()) && !TextUtils.isEmpty(sobrenome.getText()) && !TextUtils.isEmpty(cpf.getText()) && !TextUtils.isEmpty(email.getText())&& !TextUtils.isEmpty(senha.getText())) {


                    usuarios = new Usuarios();

                    usuarios.setId(id);
                    usuarios.setNome(nome.getText().toString().trim());
                    usuarios.setSobrenome(sobrenome.getText().toString().trim());
                    usuarios.setCpf(cpf.getText().toString().trim());
                    usuarios.setEmail(email.getText().toString().trim());
                    usuarios.setSenha(senha.getText().toString().trim());

                    databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios);
                    terminarAtualização();
                } else{
                    //Se os campos não forem preenchidos o metodo TOAST apresenta uma msg na tela
                    Toast.makeText(consultar_excluir_usuarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT);
                }
            }



        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_users2, menu);
        getSupportActionBar().setTitle("   Usuário");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent mIntent = new Intent(consultar_excluir_usuarioActivity.this, UsuariosActivity.class);
                startActivity(mIntent);
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exclusão");
                builder.setMessage("Deseja excluir este usuário");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Usuarios u = new Usuarios();
                        u.setId(id);
                        databaseReference.child("Usuarios").child(u.getId()).removeValue();
                        terminarExclusão();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        nome.setFocusable(true);
                        nome.requestFocus();
                    }
                });
                alerta = builder.create();
                alerta.show();
            default:break;
        }
        return true;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(consultar_excluir_usuarioActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void terminarAtualização() {
        Toast.makeText(consultar_excluir_usuarioActivity.this, "Atualização efetuada com sucesso", Toast.LENGTH_SHORT);
        Intent intentabrirMain = new Intent(consultar_excluir_usuarioActivity.this, UsuariosActivity.class);
        startActivity(intentabrirMain);
    }

    private void terminarExclusão() {
        Toast.makeText(consultar_excluir_usuarioActivity.this, "Exclusão efetuada com sucesso", Toast.LENGTH_SHORT);
        Intent intentabrirMain = new Intent(consultar_excluir_usuarioActivity.this, UsuariosActivity.class);
        startActivity(intentabrirMain);
    }
}