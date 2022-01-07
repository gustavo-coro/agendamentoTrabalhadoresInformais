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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Servico;
import modelo.TiposServicos;
import modelo.Usuario;

public class ListarTrabalhadores extends AppCompatActivity {

    private ListView listaTrabalhadoresList;
    private Button cancelaBtn;

    //Lista usada para no ListView
    private ArrayList<String[]> resultadoLista;
    private ItemListaUsuario adaptar;

    private int idServico = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_trabalhadores);

        getSupportActionBar().setTitle("Lista de Trabalhadores");

        listaTrabalhadoresList = (ListView) findViewById(R.id.listaTrabalhadoresList);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);

        Intent intencao = getIntent();
        idServico = intencao.getIntExtra("id", -1);

        //responsavel por chamar todos os eventos dos botoes
        listarTrabalhadoresEventos();

        carregaEventosLista();
    }

    private void listarTrabalhadoresEventos() {

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void carregaEventosLista(){
        resultadoLista = new ArrayList<>();


        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "trabalhadorofereceservicos";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        JSONArray usuariosJSON = resposta.getJSONArray("informacao");

                        for (int i = 0; i < usuariosJSON.length(); i++) {
                            JSONArray obj = usuariosJSON.getJSONArray(i);

                            String temp[] = new String[4];

                            /*temp[0] = tuplas.getString("nome_usuario");
                            temp[1] = tuplas.getInt("idusuario")+"";
                            temp[2] = tuplas.getString("nome_servico");
                            temp[3] = tuplas.getFloat("preco")+"";*/

                            temp[0] = (String) obj.getString(0);
                            temp[1] = (String) obj.getString(1);
                            temp[2] = (String) obj.getString(2);
                            temp[3] = (String) obj.getString(3);
                            resultadoLista.add(temp);
                        }

                        adaptar = new ItemListaUsuario(getApplication(), resultadoLista);
                        listaTrabalhadoresList.setAdapter(adaptar);

                        listaTrabalhadoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent trocaAct = new Intent(ListarTrabalhadores.this,
                                        AgendamentoTrabalho.class);

                                String extra[] = resultadoLista.get(position);
                                try {
                                    trocaAct.putExtra("acao", "0");
                                    trocaAct.putExtra("idTrabalhador", extra[1]);
                                    trocaAct.putExtra("idServico", idServico+"");
                                    trocaAct.putExtra("nomeTrabalhador", extra[0]);
                                    trocaAct.putExtra("nomeServico", extra[2]);

                                    startActivity(trocaAct);
                                } catch (NumberFormatException ex) {
                                    Toast.makeText(ListarTrabalhadores.this,
                                            "Erro no formato do id", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(ListarTrabalhadores.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ListarTrabalhadores.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarTrabalhadores.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultainner");
                parametros.put("idServico", idServico + "");
                parametros.put("idUsuario", GlobalVar.idUsuario + "");

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

}