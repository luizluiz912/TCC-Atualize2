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

public class consultar_excluir_clientesActivity extends AppCompatActivity {

    private EditText edtnome, edtsobrenome, edtcpf, edtemail, edttel, edtcel, edtdatnasc;
    private Button btnEditar;
    String id;

    private Clientes clientes;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private AlertDialog alerta;

    //Action bar
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_users2, menu);
        getSupportActionBar().setTitle("   Cliente");
        getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent mIntent = new Intent(consultar_excluir_clientesActivity.this, ClientesActivity.class);
                startActivity(mIntent);
                finish();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Exclusão");
                builder.setMessage("Deseja excluir este cliente");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        inicializarFirebase();
                        Clientes u = new Clientes();
                        u.setId(id);
                        databaseReference.child("Clientes").child(u.getId()).removeValue();
                        terminarExclusão();
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        edtnome.setFocusable(true);
                        edtnome.requestFocus();
                    }
                });
                alerta = builder.create();
                alerta.show();
            default:break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_excluir_clientes);

        edtnome = (EditText) findViewById(R.id.nome);
        edtsobrenome = (EditText) findViewById(R.id.sobrenome);
        edtcpf = (EditText) findViewById(R.id.cpf);
        edtemail = (EditText) findViewById(R.id.email);
        edttel = (EditText) findViewById(R.id.fixo);
        edtcel = (EditText) findViewById(R.id.celular);
        edtdatnasc = (EditText) findViewById(R.id.datnasc);
        btnEditar = (Button) findViewById(R.id.btnEditar) ;

        Intent in = getIntent();
        id = in.getStringExtra("client-id");

        edtnome.setText(in.getStringExtra("client-nome"));
        edtsobrenome.setText(in.getStringExtra("client-sobrenome"));
        edtcpf.setText(in.getStringExtra("client-cpf"));
        edtemail.setText(in.getStringExtra("client-email"));
        edtcel.setText(in.getStringExtra("client-cel"));
        edttel.setText(in.getStringExtra("client-tel"));
        edtdatnasc.setText(in.getStringExtra("client-datnasc"));
        edtcpf.setEnabled(false);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               validarCampos();
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(consultar_excluir_clientesActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void validarCampos(){
        String nome = edtnome.getText().toString();
        String sobrenome = edtsobrenome.getText().toString();
        String email = edtemail.getText().toString();
        String cel = edtcel.getText().toString();
        String tel = edttel.getText().toString();
        String datnasc = edtdatnasc.getText().toString();

        if(!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(sobrenome) && !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(cel) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(datnasc)) {
            if(isEmailValido(email) == false) {
                edtemail.setFocusable(true);
                edtemail.requestFocus();
                Toast.makeText(consultar_excluir_clientesActivity.this, "Preencha com um email valido", Toast.LENGTH_SHORT).show();
            }else {
                inserirDados();
            }
        }else{
            //Se os campos não forem preenchidos o metodo TOAST apresenta uma msg na tela
            Toast.makeText(consultar_excluir_clientesActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
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
        clientes.setId(id);
        clientes.setNome(edtnome.getText().toString().trim());
        clientes.setSobrenome(edtsobrenome.getText().toString().trim());
        clientes.setCpf(edtcpf.getText().toString().trim());
        clientes.setEmail(edtemail.getText().toString().trim());
        clientes.setCelular(edtcel.getText().toString().trim());
        clientes.setTelefone(edttel.getText().toString().trim());
        clientes.setData_nasc(edtdatnasc.getText().toString().trim());
        databaseReference.child("Clientes").child(clientes.getId()).setValue(clientes);
        terminarAtualização();
    }

    private void terminarAtualização() {
        Toast.makeText(consultar_excluir_clientesActivity.this, "Atualização efetuada com sucesso", Toast.LENGTH_SHORT);
        Intent intentabrirMain = new Intent(consultar_excluir_clientesActivity.this, ClientesActivity.class);
        startActivity(intentabrirMain);
    }

    private void terminarExclusão() {
        Toast.makeText(consultar_excluir_clientesActivity.this, "Exclusão efetuada com sucesso", Toast.LENGTH_SHORT);
        Intent intentabrirMain = new Intent(consultar_excluir_clientesActivity.this, ClientesActivity.class);
        startActivity(intentabrirMain);
    }
}
