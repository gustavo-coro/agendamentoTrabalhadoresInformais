package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AgendamentoTrabalho extends AppCompatActivity {

    private DatePickerDialog calendarioUsuario;
    private TimePickerDialog horaUsuario;
    private Calendar calendarioTemp;

    private TextView trabalhadorTxt;
    private Spinner tipoServicoSpnr;
    private TextView dataTxt;
    private TextView horaTxt;
    private Button confirmarBtn;
    private Button cancelarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_trabalho);

        trabalhadorTxt = (TextView) findViewById(R.id.nomeTrabalhadorTxt);
        tipoServicoSpnr = (Spinner) findViewById(R.id.selecionaServicoSpnr);
        dataTxt = (TextView) findViewById(R.id.dataTxt);
        horaTxt = (TextView) findViewById(R.id.horaInicioTxt);
        confirmarBtn = (Button) findViewById(R.id.confirmarBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);

        //responsavel por chamar todos os eventos dos botoes
        agendamentoEventos();

    }

    private void agendamentoEventos(){

        //variaveis a serem usadas na selecao de data de cada agendamento
        calendarioTemp = Calendar.getInstance();
        calendarioUsuario = new DatePickerDialog(AgendamentoTrabalho.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                calendarioTemp.set(ano, mes, dia);
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

                String pattern = "HH:mm";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);

                horaTxt.setText(formatador.format(calendarioTemp.getTime()));
            }
        }, calendarioTemp.get(Calendar.HOUR), calendarioTemp.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(AgendamentoTrabalho.this));

        confirmarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(AgendamentoTrabalho.this, MenuControle.class);

                startActivity(trocaAct);
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(AgendamentoTrabalho.this, MenuControle.class);

                startActivity(trocaAct);
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

}