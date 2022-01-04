package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AgendamentoTrabalho extends AppCompatActivity {

    //Variáveis usadas no trabalgo com as datas
    private DatePickerDialog calendarioUsuario;
    private TimePickerDialog horaUsuario;
    private Calendar calendarioTemp;
    private Calendar dataAtual;

    private TextView tituloTxt;
    private TextView trabalhadorTxt;
    private TextView nomeServicoTxt;
    private TextView dataTxt;
    private TextView horaTxt;
    private EditText enderecoTxt;
    private Button confirmarBtn;
    private Button cancelarBtn;

    private int idTrabalhador;
    private int idServico;
    private String nomeServico;
    private String nomeTrabalhador;

    //0-cadastro, 1-editar
    private int acao = -1;
    private int idContratacao;
    private int tipoUsuario = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_trabalho);

        getSupportActionBar().hide();

        tituloTxt = (TextView) findViewById(R.id.tituloTxt);
        trabalhadorTxt = (TextView) findViewById(R.id.nomeTrabalhadorTxt);
        nomeServicoTxt = (TextView) findViewById(R.id.nomeServicoTxt);
        enderecoTxt = (EditText) findViewById(R.id.enderecoTxt);
        dataTxt = (TextView) findViewById(R.id.dataTxt);
        horaTxt = (TextView) findViewById(R.id.horaInicioTxt);
        confirmarBtn = (Button) findViewById(R.id.confirmarBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);

        Intent intencao = getIntent();
        try {
            //pega a data atual do celular do usuario
            dataAtual = Calendar.getInstance();
            calendarioTemp = Calendar.getInstance();

            acao = Integer.parseInt(intencao.getStringExtra("acao"));
            if (acao == 0) {
                idServico = Integer.parseInt(intencao.getStringExtra("idServico"));
                idTrabalhador = Integer.parseInt(intencao.getStringExtra("idTrabalhador"));
                nomeServico = intencao.getStringExtra("nomeServico");
                nomeTrabalhador = intencao.getStringExtra("nomeTrabalhador");

                mostraDataAtual();
            } else if (acao == 1) {
                idContratacao = Integer.parseInt(intencao.getStringExtra("idContratacao"));
                tipoUsuario = Integer.parseInt(intencao.getStringExtra("tipoUsuario"));
            }
            //preenche a activity com as informações do serviço consultado
            preencheInformacoes();

            //responsavel por chamar todos os eventos dos botoes
            agendamentoEventos();

        } catch (NumberFormatException ex) {
            Toast.makeText(AgendamentoTrabalho.this,
                    "Erro no formato dos ids", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void mostraDataAtual() {

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat formatador = new SimpleDateFormat(pattern);
        dataTxt.setText(formatador.format(calendarioTemp.getTime()));

        pattern = "HH:mm";
        formatador = new SimpleDateFormat(pattern);
        horaTxt.setText(formatador.format(calendarioTemp.getTime()));

    }

    private void agendamentoEventos() {

        //metodo usado na data de cada agendamento
        calendarioUsuario = new DatePickerDialog(AgendamentoTrabalho.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                calendarioTemp.set(ano, mes, dia);

                if (calendarioTemp.before(dataAtual)) {
                    calendarioTemp = Calendar.getInstance();
                    Toast toast = Toast.makeText(AgendamentoTrabalho.this, "Você não pode selecionar uma data no passado.", Toast.LENGTH_LONG);
                    toast.show();
                    mostraDataAtual();
                }
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);
                dataTxt.setText(formatador.format(calendarioTemp.getTime()));
            }
        }, calendarioTemp.get(Calendar.YEAR), calendarioTemp.get(Calendar.MONTH), calendarioTemp.get(Calendar.DAY_OF_MONTH));

        //variaveis a serem usadas na selecao de hora de cada agendamento
        horaUsuario = new TimePickerDialog(AgendamentoTrabalho.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int min) {
                calendarioTemp.set(calendarioTemp.get(Calendar.YEAR), calendarioTemp.get(Calendar.MONTH), calendarioTemp.get(Calendar.DAY_OF_MONTH), hora, min);

                if (calendarioTemp.before(dataAtual)) {
                    calendarioTemp = Calendar.getInstance();
                    Toast toast = Toast.makeText(AgendamentoTrabalho.this, "Você não pode selecionar um horário no passado.", Toast.LENGTH_LONG);
                    toast.show();
                    mostraDataAtual();
                }
                String pattern = "HH:mm";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);

                horaTxt.setText(formatador.format(calendarioTemp.getTime()));
            }
        }, calendarioTemp.get(Calendar.HOUR), calendarioTemp.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(AgendamentoTrabalho.this));

        confirmarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaPreenchimento();
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acao == 0) {
                    finish();
                } else {
                    AlertDialog.Builder confirmarExclusao =
                            new AlertDialog.Builder(AgendamentoTrabalho.this);
                    confirmarExclusao.setTitle("Atencão!");
                    confirmarExclusao.setMessage("Tem certeza que deseja excluir o registro?");
                    confirmarExclusao.setCancelable(false);
                    confirmarExclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            excluirSolicitacao();
                        }
                    });
                    confirmarExclusao.setNegativeButton("Não", null);
                    confirmarExclusao.setIcon(android.R.drawable.ic_dialog_alert);
                    confirmarExclusao.show();
                }
            }
        });

        dataTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarioUsuario.show();
            }
        });

        horaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horaUsuario.show();
            }
        });
    }

    private void preencheInformacoes() {
        if (acao==0) {
            nomeServicoTxt.setText(nomeServico);
            trabalhadorTxt.setText(nomeTrabalhador);
            enderecoTxt.setText(GlobalVar.usuarioLogin.getEndereco());
        } else if (acao==1) {

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
                            calendarioTemp.setTime(dataServico);

                            trabalhadorTxt.setText(obj.getString("nome_trabalhador"));
                            nomeServicoTxt.setText(obj.getString("nome_servico"));
                            enderecoTxt.setText(obj.getString("endereco_servico"));
                            dataTxt.setText(formatadorData.format(dataServico));
                            horaTxt.setText(formatadorHora.format(dataServico));
                            tituloTxt.setText("Editar solicitação");
                            cancelarBtn.setText("Excluir");

                        } else {
                            Toast.makeText(AgendamentoTrabalho.this, resposta.getString("informacao"),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException ex) {
                        Toast.makeText(AgendamentoTrabalho.this,
                                "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AgendamentoTrabalho.this,
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

        } else {
            Toast.makeText(AgendamentoTrabalho.this,
                    "Erro na identificação da ação", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void verificaPreenchimento() {
        if (enderecoTxt.getText().toString().isEmpty()) {
            Toast.makeText(AgendamentoTrabalho.this,
                    "Você deve preencher o endereço!", Toast.LENGTH_LONG).show();
        } else if (calendarioTemp.before(dataAtual)){
            Toast.makeText(AgendamentoTrabalho.this,
                    "Selecione uma data válida!", Toast.LENGTH_LONG).show();
        } else {
            calendarioTemp.set(Calendar.SECOND, 0);
            calendarioTemp.set(Calendar.MILLISECOND, 0);
            if (acao == 0) {
                agendaServico(new Date(calendarioTemp.getTime().getTime()));
            } else {
                atualizaSolicitacao(new Date(calendarioTemp.getTime().getTime()));
            }
        }
    }

    private void agendaServico(Date dataCadastro) {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(AgendamentoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();

                        Intent trocaAct = new Intent(AgendamentoTrabalho.this, MenuControle.class);
                        startActivity(trocaAct);
                        finish();

                    } else {
                        Toast.makeText(AgendamentoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(AgendamentoTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgendamentoTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("servico", "cadastro");
                parametros.put("idUsuario", GlobalVar.idUsuario+"");
                parametros.put("idServico", idServico+"");
                SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                parametros.put("horaInicio", formatador.format(dataCadastro));
                parametros.put("endereco", enderecoTxt.getText().toString());
                parametros.put("idTrabalhador", idTrabalhador+"");

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

    private void atualizaSolicitacao (Date dataServico) {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(AgendamentoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();

                        Intent trocaAct = new Intent(AgendamentoTrabalho.this, MenuControle.class);
                        startActivity(trocaAct);
                        finish();

                    } else {
                        Toast.makeText(AgendamentoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(AgendamentoTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgendamentoTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("servico", "atualiza");
                parametros.put("id", idContratacao+"");
                if (tipoUsuario == 0) {
                    parametros.put("aceito", 0+"");
                } else if (tipoUsuario == 1) {
                    parametros.put("aceito", "3");
                }
                SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                parametros.put("horaInicio", formatador.format(dataServico));
                parametros.put("endereco", enderecoTxt.getText().toString());

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

    private void excluirSolicitacao() {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(AgendamentoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();

                        Intent trocaAct = new Intent(AgendamentoTrabalho.this, MenuControle.class);
                        startActivity(trocaAct);
                        finish();

                    } else {
                        Toast.makeText(AgendamentoTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(AgendamentoTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgendamentoTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("servico", "deleta");
                parametros.put("id", idContratacao+"");

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

}