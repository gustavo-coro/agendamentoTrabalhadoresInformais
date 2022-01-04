package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.Servico;

public class SelecionarServico extends AppCompatActivity {

    private ListView listaServicosList;
    private Button confirmaBtn;
    private Button cancelaBtn;

    private ArrayList<Servico> servicos;
    private ItemListaServico adaptar;
    private int idTipoServico = -1;

    //Em caso do usuário ter acabado de se cadastrar
    int idUsuario = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_servico);

        getSupportActionBar().hide();

        listaServicosList = (ListView) findViewById(R.id.selectServicoList);
        confirmaBtn = (Button) findViewById(R.id.confirmaBtn);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);

        Intent intencao = getIntent();
        idTipoServico = intencao.getIntExtra("id", -1);
        if (GlobalVar.idUsuario == -1) {
            idUsuario = intencao.getIntExtra("idUsuario", -1);
        }

        //responsavel por chamar todos os eventos dos botoes
        eventos();

        carregaEventosLista();
    }

    private void eventos() {
        confirmaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct;
                if(GlobalVar.idUsuario == -1) {
                    trocaAct = new Intent(SelecionarServico.this, MainActivity.class);
                } else {
                    trocaAct = new Intent(SelecionarServico.this, MenuControle.class);
                }
                startActivity(trocaAct);
                finish();
            }
        });

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void carregaEventosLista() {
        servicos = new ArrayList<>();

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "servicos";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        JSONArray servicosJSON = resposta.getJSONArray("informacao");

                        for (int i = 0; i < servicosJSON.length(); i++) {
                            JSONObject obj = servicosJSON.getJSONObject(i);
                            Servico temp = new Servico(obj.getInt("id"), obj.getString("nome"),
                                     (float) obj.getDouble("preco"), obj.getInt("idTiposServicos"),
                                     obj.getInt("idAdm"));
                            servicos.add(temp);
                        }

                        adaptar = new ItemListaServico(getApplication(), servicos);
                        listaServicosList.setAdapter(adaptar);

                        listaServicosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                consultaExistenciaServico(servicos.get(position).getId());
                            }
                        });

                    } else {
                        Toast.makeText(SelecionarServico.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(SelecionarServico.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelecionarServico.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultatipo");
                parametros.put("idTipoServico", idTipoServico+"");

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void consultaExistenciaServico(int idServico) {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "trabalhadorofereceservicos";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        cadastraServico(idServico);

                    } else {
                        Toast.makeText(SelecionarServico.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(SelecionarServico.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelecionarServico.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultaexistenciaservico");
                parametros.put("idServico", idServico+"");
                if (GlobalVar.idUsuario != -1) {
                    parametros.put("idUsuario", GlobalVar.idUsuario+"");
                } else {
                    parametros.put("idUsuario", idUsuario+"");
                }


                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void cadastraServico(int idServico) {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "trabalhadorofereceservicos";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        if (GlobalVar.usuarioIsTrabalhador != 1) {
                            GlobalVar.usuarioIsTrabalhador = 1;
                        }

                        Toast.makeText(SelecionarServico.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(SelecionarServico.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(SelecionarServico.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelecionarServico.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "cadastro");
                parametros.put("idServico", idServico+"");
                if (GlobalVar.idUsuario != -1) {
                    parametros.put("idUsuario", GlobalVar.idUsuario+"");
                } else {
                    parametros.put("idUsuario", idUsuario+"");
                }

                return parametros;
            }
        };
        pilha.add(requisicao);

    }
}