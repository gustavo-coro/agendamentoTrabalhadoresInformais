package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

public class EditAgenda extends AppCompatActivity {


    private Button cancelarTrabBtn;
    private ListView listaAgendaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agenda);

        getSupportActionBar().hide();

        listaAgendaList = (ListView) findViewById(R.id.listaAgendaList);
        cancelarTrabBtn = (Button) findViewById(R.id.cancelarTrabBtn);

        //responsavel por chamar todos os eventos dos botoes
        menuEvento();
    }

    private void menuEvento() {

        cancelarTrabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(EditAgenda.this, AgendaMenu.class);

                startActivity(trocaAct);
            }
        });
    }

}