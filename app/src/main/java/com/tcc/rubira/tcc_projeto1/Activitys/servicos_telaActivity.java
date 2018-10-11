package com.tcc.rubira.tcc_projeto1.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class servicos_telaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_servicos_tela);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_secondary, menu);
        getSupportActionBar().setTitle("   Serviços");
        //getSupportActionBar().setIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);

        return true;
    }
}