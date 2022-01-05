package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ControlarPedidoTrabalho extends AppCompatActivity {

    private TextView usuarioTxt;
    private TextView trabalhadorTxt;
    private TextView tipoServicoTxt;
    private TextView precoServicoTxt;
    private TextView enderecoServicoTxt;
    private TextView dataTxt;
    private TextView horaTxt;
    private TextView statusTxt;
    private Button recusarBtn;
    private Button aceitarBtn;
    private Button cancelarBtn;
    private Button editarBtn;
    private LinearLayout ctrlBtsLL;
    private LinearLayout statusLL;

    private int idContratacao;
    private int statusContratacao;
    private int tipoBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlar_pedido_trabalho);

        getSupportActionBar().hide();

        usuarioTxt = (TextView) findViewById(R.id.nomeUsuarioTxt);
        trabalhadorTxt = (TextView) findViewById(R.id.nomeTrabalhadorTxt);
        tipoServicoTxt = (TextView) findViewById(R.id.tipoServicoTxt);
        precoServicoTxt = (TextView) findViewById(R.id.precoServicoTxt);
        enderecoServicoTxt = (TextView) findViewById(R.id.enderecoServicoTxt);
        dataTxt = (TextView) findViewById(R.id.dataTxt);
        horaTxt = (TextView) findViewById(R.id.horaInicioTxt);
        statusTxt = (TextView) findViewById(R.id.statusServicoTxt);
        recusarBtn = (Button) findViewById(R.id.recusarBtn);
        aceitarBtn = (Button) findViewById(R.id.aceitarBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);
        editarBtn = (Button) findViewById(R.id.editarBtn);
        ctrlBtsLL = (LinearLayout) findViewById(R.id.ctrlBtsLL);
        statusLL = (LinearLayout) findViewById(R.id.statusLL);

        Intent intencao = getIntent();
        try {
            idContratacao = Integer.parseInt(intencao.getStringExtra("id"));
            statusContratacao = Integer.parseInt(intencao.getStringExtra("aceito"));
            tipoBusca = Integer.parseInt(intencao.getStringExtra("tipoBusca"));

            //0-Enviadas, 1-Recebidas
            if (tipoBusca == 0) {
                ctrlBtsLL.setVisibility(View.GONE);
                statusLL.setVisibility(View.VISIBLE);
                editarBtn.setVisibility(View.VISIBLE);
                //0-pendente, 1-aceita, 2-recusada, 3-editada pelo trabalhador
                if (statusContratacao == 0) {
                    statusTxt.setText("Aguardando resposta");
                } else if (statusContratacao == 2){
                    statusTxt.setText("Solicitação recusada");
                } else if (statusContratacao == 3) {
                    statusTxt.setText("Solicitação modificada pelo trabalhador");
                    ctrlBtsLL.setVisibility(View.VISIBLE);
                    recusarBtn.setVisibility(View.GONE);
                }
            } else {
                if (statusContratacao == 3) {
                    ctrlBtsLL.setVisibility(View.GONE);
                    statusLL.setVisibility(View.VISIBLE);
                    editarBtn.setVisibility(View.VISIBLE);
                    statusTxt.setText("Solicitação foi alterada. Aguardando retorno do contratante");
                } else {
                    ctrlBtsLL.setVisibility(View.VISIBLE);
                    statusLL.setVisibility(View.GONE);
                    editarBtn.setVisibility(View.GONE);
                }
            }

            buscaSolicitacao();

            //responsavel por chamar todos os eventos dos botoes
            controlaPedidoEvento();
        } catch (NumberFormatException ex) {
            Toast.makeText(ControlarPedidoTrabalho.this, "Erro no formato do id",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void controlaPedidoEvento() {

        recusarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceitaRecusaSolicitacao(0);
            }
        });

        aceitarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aceitaRecusaSolicitacao(1);
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(ControlarPedidoTrabalho.this,
                        ListarSolicitacoesTrabalho.class);
                startActivity(trocaAct);
                finish();
            }
        });

        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(ControlarPedidoTrabalho.this,
                        AgendamentoTrabalho.class);
                trocaAct.putExtra("acao", "1");
                trocaAct.putExtra("idContratacao", idContratacao+"");
                if (statusContratacao == 3 && tipoBusca == 1) {
                    trocaAct.putExtra("tipoUsuario", 1+"");
                } else {
                    trocaAct.putExtra("tipoUsuario", 0+"");
                }
                startActivity(trocaAct);
                finish();
            }
        });
    }

    private void  buscaSolicitacao () {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        JSONObject obj = resposta.getJSONObject("informacao");

                        Date dataServico = new Date(obj.getLong("data_hora_inicio"));
                        SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat formatadorHora = new SimpleDateFormat("HH:mm");

                        usuarioTxt.setText(obj.getString("nome_usuario"));
                        trabalhadorTxt.setText(obj.getString("nome_trabalhador"));
                        tipoServicoTxt.setText(obj.getString("nome_servico"));
                        precoServicoTxt.setText(String.format("%.2f",
                                obj.getDouble("preco")).replace(".", ","));
                        enderecoServicoTxt.setText(obj.getString("endereco_servico"));
                        dataTxt.setText(formatadorData.format(dataServico));
                        horaTxt.setText(formatadorHora.format(dataServico));

                    } else {
                        Toast.makeText(ControlarPedidoTrabalho.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ControlarPedidoTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ControlarPedidoTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultasolicitacaoid");
                parametros.put("id", idContratacao + "");

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void aceitaRecusaSolicitacao (int aceitaRecusa) {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        if (aceitaRecusa == 0) {
                            Toast.makeText(ControlarPedidoTrabalho.this,
                                    "Solicitação recusada",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ControlarPedidoTrabalho.this,
                                    "Solicitação aceita",
                                    Toast.LENGTH_LONG).show();
                        }

                        Intent trocaAct = new Intent(ControlarPedidoTrabalho.this,
                                ListarSolicitacoesTrabalho.class);
                        startActivity(trocaAct);
                        finish();

                    } else {
                        Toast.makeText(ControlarPedidoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ControlarPedidoTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ControlarPedidoTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "atualiza");
                parametros.put("id", idContratacao + "");
                if (aceitaRecusa == 0) {
                    parametros.put("aceito", 2 + "");
                } else {
                    parametros.put("aceito", 1 + "");
                }

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

}