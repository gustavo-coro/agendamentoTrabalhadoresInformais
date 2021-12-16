package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuControle extends AppCompatActivity {

    private TextView nomeTxt;
    private TextView descricaoTxt;
    private Button agendamentoBtn;
    private Button pedidosBtn;
    private Button logoutBtn;
    private Button editarABtn;
    private Button agendaMenuBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_controle);

        getSupportActionBar().hide();

        nomeTxt = (TextView) findViewById(R.id.nomeUsuarioTxt);
        descricaoTxt = (TextView) findViewById(R.id.descricaoUsuarioTxt);
        agendamentoBtn = (Button) findViewById(R.id.agendamentoBtn);
        pedidosBtn = (Button) findViewById(R.id.pedidosBtn);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        editarABtn = (Button) findViewById(R.id.editarAgendaMenuBtn);
        agendaMenuBtn = (Button) findViewById(R.id.agendaMenuBtn);


        //responsavel por chamar todos os eventos dos botoes
        menuEvento();


    }

    private void menuEvento() {

        nomeTxt.setText(GlobalVar.nomeUsuarioLogin);
        descricaoTxt.setText(GlobalVar.descricaoUsuarioLogin);

        nomeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MenuControle.this, Cadastro.class);
                trocaAct.putExtra("acao", 1);
                startActivity(trocaAct);
            }
        });

        agendamentoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(MenuControle.this, ListarTrabalhadores.class);
                startActivity(trocaAct);

            }
        });

        pedidosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(MenuControle.this, ControlarPedidoTrabalho.class);
                startActivity(trocaAct);

            }
        });

        editarABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(MenuControle.this, EditAgenda.class);

                startActivity(trocaAct);

            }
        });

        agendaMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(MenuControle.this, AgendaMenu.class);

                startActivity(trocaAct);

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(MenuControle.this, MainActivity.class);

                GlobalVar.nomeUsuarioLogin = "";
                GlobalVar.descricaoUsuarioLogin = "";
                GlobalVar.idUsuario = -1;

                startActivity(trocaAct);
                finish();
            }
        });

    }


}