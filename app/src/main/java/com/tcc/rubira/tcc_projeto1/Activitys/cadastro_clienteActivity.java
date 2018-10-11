package com.tcc.rubira.tcc_projeto1.Activitys;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc.rubira.tcc_projeto1.Entidades.Clientes;
import com.tcc.rubira.tcc_projeto1.R;

import java.util.UUID;


public class cadastro_clienteActivity extends AppCompatActivity {


    private EditText edtnome, edtsobrenome, edtcpf, edtemail, edtdatnasc, edtcel,edtfixo;
    private Button btncadastrar;
    private Clientes clientes;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    //Action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("   Cadastrar Cliente");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.drawable.ic_arrow_back_black_24dp:
                Intent mIntent = new Intent(cadastro_clienteActivity.this, ClientesActivity.class);
                startActivity(mIntent);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_cadastro_cliente);

        edtnome = (EditText) findViewById(R.id.nome);
        edtsobrenome = (EditText) findViewById(R.id.sobrenome);

        edtcpf = (EditText) findViewById(R.id.cpf);
        SimpleMaskFormatter maskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf1 = new MaskTextWatcher(edtcpf,maskcpf);
        edtcpf.addTextChangedListener(maskcpf1);

        edtemail = (EditText) findViewById(R.id.email);

        edtcel = (EditText) findViewById(R.id.celular);
        SimpleMaskFormatter maskcel = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher maskcel1 = new MaskTextWatcher(edtcel,maskcel);
        edtcel.addTextChangedListener(maskcel1);

        edtfixo = (EditText) findViewById(R.id.fixo);
        SimpleMaskFormatter maskfixo = new SimpleMaskFormatter("(NN)NNNN-NNNN");
        MaskTextWatcher maskfixo1 = new MaskTextWatcher(edtfixo,maskfixo);
        edtfixo.addTextChangedListener(maskfixo1);

        edtdatnasc = (EditText) findViewById(R.id.datnasc);
        SimpleMaskFormatter masknasc = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher masknasc1 = new MaskTextWatcher(edtdatnasc,masknasc);
        edtdatnasc.addTextChangedListener(masknasc1);

        //Quando for pressionado o botão de cadastrar ->
        btncadastrar = (Button) findViewById(R.id.btncadastrar);
        btncadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                    //Inicia o banco de dados
                    inicializarFirebase();
                String cpf = edtcpf.getText().toString();
                //if(!TextUtils.isEmpty(edtnome.getText().toString()) && !TextUtils.isEmpty(edtsobrenome.getText().toString()) && !TextUtils.isEmpty(edtcpf.getText().toString())&& (edtcpf.getText().toString().length() == 11) && !TextUtils.isEmpty(edtemail.getText().toString())&& !TextUtils.isEmpty(edtsenha.getText().toString())) {
                Query query1 = databaseReference.child("Clientes").orderByChild("cpf").equalTo(cpf);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            edtcpf.setFocusable(true);
                            edtcpf.requestFocus();
                            Toast.makeText(cadastro_clienteActivity.this, "Usuário ja cadastrado", Toast.LENGTH_SHORT).show();;
                        }else{
                            validarCampos();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });
    }

    private void validarCampos(){
        String nome = edtnome.getText().toString();
        String sobrenome = edtsobrenome.getText().toString();
        String cpf = edtcpf.getText().toString();
        String email = edtemail.getText().toString();
        String cel = edtcel.getText().toString();
        String nasc = edtdatnasc.getText().toString();

        if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(sobrenome) && !TextUtils.isEmpty(cpf) && !TextUtils.isEmpty(cpf)&& (cpf.length() == 14) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(cel) && !TextUtils.isEmpty(nasc)) {
            if(isEmailValido(email) == false) {
                edtemail.setFocusable(true);
                edtemail.requestFocus();
                Toast.makeText(cadastro_clienteActivity.this, "Preencha com um email valido", Toast.LENGTH_SHORT).show();
            }else {
                inserirDados();
            }
        }else{
            //Se os campos não forem preenchidos o metodo TOAST apresenta uma msg na tela
            Toast.makeText(cadastro_clienteActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValido(String email){
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private void inserirDados(){
        inicializarFirebase();
        clientes = new Clientes();
        //Atribui uma ID aleatória
        clientes.setId(UUID.randomUUID().toString());
        clientes.setNome(edtnome.getText().toString());
        clientes.setSobrenome(edtsobrenome.getText().toString());
        clientes.setCpf(edtcpf.getText().toString());
        clientes.setEmail(edtemail.getText().toString());
        clientes.setData_nasc(edtdatnasc.getText().toString());
        clientes.setCelular(edtcel.getText().toString());
        clientes.setTelefone(edtfixo.getText().toString());
        databaseReference.child("Clientes").child(clientes.getId()).setValue(clientes);
        terminarCadastro();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(cadastro_clienteActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void terminarCadastro() {
        Toast.makeText(cadastro_clienteActivity.this, "Cadastro efetuado com sucesso", Toast.LENGTH_SHORT);
        Intent intentabrirMain = new Intent(cadastro_clienteActivity.this, ClientesActivity.class);
        startActivity(intentabrirMain);
    }
}
