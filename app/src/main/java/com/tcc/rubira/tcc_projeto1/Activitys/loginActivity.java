package com.tcc.rubira.tcc_projeto1.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tcc.rubira.tcc_projeto1.DAO.config_firebase;
import com.tcc.rubira.tcc_projeto1.Entidades.Usuarios;
import com.tcc.rubira.tcc_projeto1.R;

public class loginActivity extends Activity {

    private EditText email,senha;
    private Button btnEntrar;
    private FirebaseAuth autentication;
    private Usuarios usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifica se os campos foram preenchidos
                if(!email.getText().toString().equals("")&& !senha.getText().toString().equals("")){
                    // Pega o email e a senha e manda para o metodo de validação
                    usuarios = new Usuarios();
                    usuarios.setEmail(email.getText().toString());
                    usuarios.setSenha(senha.getText().toString());

                    ValidarLogin();
                }else{
                    //Se os campos não forem preenchidos o metodo TOAST apresenta uma msg na tela
                    Toast.makeText(loginActivity.this, "Preenche os campos de email e senha", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void ValidarLogin(){

        autentication = config_firebase.getFirebaseAutentication();
        autentication.createUserWithEmailAndPassword(usuarios.getEmail(),usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirMain();
                    Toast.makeText(loginActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void abrirMain(){
      Intent intentabrirMain = new Intent(loginActivity.this, MainActivity.class);
      startActivity(intentabrirMain);

    }
}
