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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.TiposServicos;

public class SelecionarTrabalho extends AppCompatActivity {

    private ListView listaTrabalhosList;
    private Button confirmaBtn;
    private Button cancelaBtn;

    private ArrayList<TiposServicos> tiposServicos;
    private ItemListaTiposServico adaptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_trabalho);

        getSupportActionBar().hide();

        listaTrabalhosList = (ListView) findViewById(R.id.selectTrabalhosList);
        confirmaBtn = (Button) findViewById(R.id.confirmaBtn);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);

        //responsavel por chamar todos os eventos dos botoes
        eventos();

        carregaEventosLista();
    }

    private void eventos() {

        confirmaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct;
                //verifica se o usuário fez login
                if(GlobalVar.idUsuario != -1){
                    trocaAct = new Intent(SelecionarTrabalho.this, MenuControle.class);
                    startActivity(trocaAct);
                    finish();
                }else{
                    trocaAct = new Intent(SelecionarTrabalho.this, MainActivity.class);
                    startActivity(trocaAct);
                    finish();
                }
            }
        });

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct;
                //verifica se o usuário fez login
                if(GlobalVar.idUsuario != -1){
                    finish();
                }else{
                    trocaAct = new Intent(SelecionarTrabalho.this, MainActivity.class);
                    startActivity(trocaAct);
                    finish();
                }
            }
        });

    }

    private void carregaEventosLista(){
        tiposServicos = new ArrayList<>();


        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "tiposservicos";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        JSONArray servicosJSON = resposta.getJSONArray("informacao");

                        for (int i = 0; i < servicosJSON.length(); i++) {
                            JSONObject obj = servicosJSON.getJSONObject(i);
                            TiposServicos temp = new TiposServicos(obj.getInt("id"),
                                    obj.getInt("idAdm"), obj.getString("nome"));
                            tiposServicos.add(temp);
                        }

                        adaptar = new ItemListaTiposServico(getApplication(), tiposServicos);
                        listaTrabalhosList.setAdapter(adaptar);

                        listaTrabalhosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent novoFluxo = new Intent(SelecionarTrabalho.this,
                                        SelecionarServico.class);
                                novoFluxo.putExtra("acao", 0);
                                novoFluxo.putExtra("id", tiposServicos.get(position).getId());
                                novoFluxo.putExtra("nome", tiposServicos.get(position).getNomeTipoServico());

                                startActivity(novoFluxo);
                            }
                        });

                    } else {
                        Toast.makeText(SelecionarTrabalho.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(SelecionarTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelecionarTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consulta");

                return parametros;
            }
        };
        pilha.add(requisicao);
    }


}