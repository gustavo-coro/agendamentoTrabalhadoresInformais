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
    private Button aceitosBtn;
    private Button trabalhosConcluidosBtn;


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
        aceitosBtn = (Button) findViewById(R.id.trabalhosAceitosBtn);
        trabalhosConcluidosBtn = (Button) findViewById(R.id.trabalhosConcluidosBtn);

        //responsavel por chamar todos os eventos dos botoes
        menuEvento();
    }

    private void menuEvento() {

        nomeTxt.setText(GlobalVar.usuarioLogin.getNome());
        descricaoTxt.setText(GlobalVar.usuarioLogin.getDescricao());

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
                Intent trocaAct = new Intent(MenuControle.this, ListarServico.class);
                startActivity(trocaAct);
            }
        });

        pedidosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MenuControle.this,
                        ListarSolicitacoesTrabalho.class);
                startActivity(trocaAct);
            }
        });

        aceitosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(MenuControle.this,
                        ListarTrabalhosAceitos.class);
                startActivity(trocaAct);
            }
        });

        trabalhosConcluidosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MenuControle.this, TrabalhosConcluidos.class);

                startActivity(trocaAct);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MenuControle.this, MainActivity.class);

                GlobalVar.usuarioLogin = null;
                GlobalVar.idUsuario = -1;

                startActivity(trocaAct);
                finish();
            }
        });

    }


}