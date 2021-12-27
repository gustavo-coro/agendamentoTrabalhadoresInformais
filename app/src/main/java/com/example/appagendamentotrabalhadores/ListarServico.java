package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import modelo.Servico;
import modelo.TiposServicos;

public class ListarServico extends AppCompatActivity {

    private Spinner trabalhoSpnr;
    private ListView listaServicosList;
    private Button cancelaBtn;
    private TextView listaVaziaTxt;

    //Lista usada para no ListView
    private ArrayList<Servico> servicos;
    private ItemListaServico adaptar;

    //Lista usada para no Spinner
    private List<TiposServicos> tiposServicosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_servico);

        getSupportActionBar().hide();

        trabalhoSpnr = (Spinner) findViewById(R.id.trabalhoSpnr);
        listaServicosList = (ListView) findViewById(R.id.listaServicosList);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);
        View vazia = findViewById(R.id.listaVaziaTxt);
        listaServicosList.setEmptyView(vazia);

        //responsavel por chamar todos os eventos dos botoes
        listarServicoEventos();

        configuraSpinner();
    }

    private void listarServicoEventos() {
        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void  configuraSpinner() {
        tiposServicosList = new ArrayList<>();
        tiposServicosList.add(new TiposServicos(-1,-1,""));

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
                            tiposServicosList.add(temp);
                        }

                        ArrayAdapter<TiposServicos> adapterSpinner = new ArrayAdapter<TiposServicos>(
                                ListarServico.this, android.R.layout.simple_spinner_item,
                                tiposServicosList);
                        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        trabalhoSpnr.setAdapter(adapterSpinner);

                        trabalhoSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                TiposServicos tipoServicoSelecionado = (TiposServicos)
                                        parent.getSelectedItem();

                                carregaEventosLista(tipoServicoSelecionado.getId());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {
                        Toast.makeText(ListarServico.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ListarServico.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarServico.this,
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

    private void carregaEventosLista(int idTipoServico) {

        servicos = new ArrayList<>();


        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "servicos";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        JSONArray usuariosJSON = resposta.getJSONArray("informacao");
                        listaServicosList.getEmptyView().setActivated(false);

                        for (int i = 0; i < usuariosJSON.length(); i++) {
                            JSONObject obj = usuariosJSON.getJSONObject(i);
                            Servico temp = new Servico(obj.getInt("id"),
                                    obj.getString("nome"), (float) obj.getDouble("preco"),
                                    idTipoServico, obj.getInt("idAdm"));
                            servicos.add(temp);
                        }

                        adaptar = new ItemListaServico(getApplication(), servicos);
                        listaServicosList.setAdapter(adaptar);

                        listaServicosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent trocaAct = new Intent(ListarServico.this,
                                        ListarTrabalhadores.class);

                                trocaAct.putExtra("id", servicos.get(position).getId());

                                startActivity(trocaAct);
                            }
                        });

                    } else if (resposta.getInt("cod") == 204) {

                        adaptar = new ItemListaServico(getApplication(), servicos);
                        listaServicosList.setAdapter(adaptar);

                        Toast.makeText(ListarServico.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                        listaServicosList.getEmptyView().setActivated(true);

                    }else {
                        adaptar = new ItemListaServico(getApplication(), servicos);
                        listaServicosList.setAdapter(adaptar);

                        Toast.makeText(ListarServico.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                        listaServicosList.getEmptyView().setActivated(true);
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ListarServico.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarServico.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultatipo");
                if (idTipoServico == -1) {
                    parametros.put("idTipoServico", "null");
                } else {
                    parametros.put("idTipoServico", idTipoServico+"");
                }

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

}