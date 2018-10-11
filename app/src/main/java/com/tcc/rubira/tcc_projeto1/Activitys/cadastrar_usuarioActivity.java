package com.tcc.rubira.tcc_projeto1.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
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
import com.tcc.rubira.tcc_projeto1.Entidades.Usuarios;
import com.tcc.rubira.tcc_projeto1.R;
import java.util.UUID;

public class cadastrar_usuarioActivity extends AppCompatActivity {

    private EditText edtnome, edtsobrenome, edtcpf, edtemail, edtsenha;
    private Button btncadastrar,funcao;
    private String func;
    private Usuarios usuarios;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    //Action bar
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportActionBar().setTitle("   Cadsastrar Usuário");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        edtnome = (EditText) findViewById(R.id.nome);
        edtsobrenome = (EditText) findViewById(R.id.sobrenome);
        edtcpf = (EditText) findViewById(R.id.cpf);
        SimpleMaskFormatter maskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf1 = new MaskTextWatcher(edtcpf,maskcpf);
        edtcpf.addTextChangedListener(maskcpf1);

        edtemail = (EditText) findViewById(R.id.email);
        edtsenha = (EditText) findViewById(R.id.senha);

        // AlertDialog com as opções de funçoes
        funcao = (Button) findViewById(R.id.funcao);
        funcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"Gerente","Vendedor","Mestre de obras"};
                AlertDialog.Builder builder = new AlertDialog.Builder(cadastrar_usuarioActivity.this);
                builder.setTitle("Função:");
                AlertDialog.Builder builder1 = builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selecionado) {
                        Toast.makeText(cadastrar_usuarioActivity.this, "Item selecionado: " + items[selecionado],
                                Toast.LENGTH_SHORT).show();
                        func = items[selecionado];
                    }
                });
                builder.create().show();
            }
        });

        funcao.setText(func);

        //Quando for pressionado o botão de cadastrar ->
        btncadastrar = (Button) findViewById(R.id.btncadastrar);
        btncadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                inicializarFirebase();
                String cpf = edtcpf.getText().toString();
                Query query1 = databaseReference.child("Usuarios").orderByChild("cpf").equalTo(cpf);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            edtcpf.setFocusable(true);
                            edtcpf.requestFocus();
                            Toast.makeText(cadastrar_usuarioActivity.this, "Usuário ja cadastrado", Toast.LENGTH_SHORT).show();;
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
        String senha = edtsenha.getText().toString();

        if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(sobrenome) && !TextUtils.isEmpty(cpf)&& (cpf.length() == 14) && !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(senha)) {
            if(isEmailValido(email) == false) {
                edtemail.setFocusable(true);
                edtemail.requestFocus();
                Toast.makeText(cadastrar_usuarioActivity.this, "Preencha com um email valido", Toast.LENGTH_SHORT).show();
            }else {
                inserirDados();
            }
        }else{
            //Se os campos não forem preenchidos o metodo TOAST apresenta uma msg na tela
            Toast.makeText(cadastrar_usuarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValido(String email){
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private void inserirDados(){
        usuarios = new Usuarios();
        //Atribui uma ID aleatória
        usuarios.setId(UUID.randomUUID().toString());
        usuarios.setNome(edtnome.getText().toString());
        usuarios.setSobrenome(edtsobrenome.getText().toString());
        usuarios.setCpf(edtcpf.getText().toString());
        usuarios.setEmail(edtemail.getText().toString());
        usuarios.setSenha(edtsenha.getText().toString());
        usuarios.setFuncao(func);
        databaseReference.child("Usuarios").child(usuarios.getId()).setValue(usuarios);
        terminarCadastro();
    }

    private void inicializarFirebase() {
        if(firebaseDatabase == null) {
            FirebaseApp.initializeApp(cadastrar_usuarioActivity.this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }
    }

    private void terminarCadastro() {
        Intent intentabrirMain = new Intent(cadastrar_usuarioActivity.this, UsuariosActivity.class);
        Toast.makeText(cadastrar_usuarioActivity.this, "Cadastro efetuado com sucesso", Toast.LENGTH_LONG);
        startActivity(intentabrirMain);
    }
}