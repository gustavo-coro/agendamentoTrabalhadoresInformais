package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class TrabalhosConcluidos extends AppCompatActivity {

    private Button voltarBtn;
    private ListView trabalhosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabalhos_concluidos);

        getSupportActionBar().hide();

        trabalhosList = (ListView) findViewById(R.id.trabalhosList);
        voltarBtn = (Button) findViewById(R.id.voltarBtn);

        //responsavel por chamar todos os eventos dos botoes
        menuEvento();
    }

    private void menuEvento() {

        voltarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(TrabalhosConcluidos.this, AgendaMenu.class);

                startActivity(trocaAct);
            }
        });
    }
}