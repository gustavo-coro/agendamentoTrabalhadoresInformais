package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_trabalho);

        getSupportActionBar().hide();

        trabalhadorTxt = (TextView) findViewById(R.id.nomeTrabalhadorTxt);
        nomeServicoTxt = (TextView) findViewById(R.id.nomeServicoTxt);
        enderecoTxt = (EditText) findViewById(R.id.enderecoTxt);
        dataTxt = (TextView) findViewById(R.id.dataTxt);
        horaTxt = (TextView) findViewById(R.id.horaInicioTxt);
        confirmarBtn = (Button) findViewById(R.id.confirmarBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);

        Intent intencao = getIntent();
        try {
            idServico = Integer.parseInt(intencao.getStringExtra("idServico"));
            idTrabalhador = Integer.parseInt(intencao.getStringExtra("idTrabalhador"));
            nomeServico = intencao.getStringExtra("nomeServico");
            nomeTrabalhador = intencao.getStringExtra("nomeTrabalhador");

            //pega a data atual do celular do usuario
            dataAtual = Calendar.getInstance();
            calendarioTemp = Calendar.getInstance();
            mostraDataAtual();

            //preenche a activity com as informações do serviço consultado
            preencheInformações();

            //responsavel por chamar todos os eventos dos botoes
            agendamentoEventos();

        } catch (NumberFormatException ex) {
            Toast.makeText(AgendamentoTrabalho.this,
                    "Erro no formato dos ids", Toast.LENGTH_LONG).show();
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
                finish();
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

    private void preencheInformações() {
        nomeServicoTxt.setText(nomeServico);
        trabalhadorTxt.setText(nomeTrabalhador);
        enderecoTxt.setText(GlobalVar.usuarioLogin.getEndereco());

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
            agendaServico(new Date(calendarioTemp.getTime().getTime()));
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

}