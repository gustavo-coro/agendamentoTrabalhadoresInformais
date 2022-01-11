package com.example.appagendamentotrabalhadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListaConcluido extends ArrayAdapter<String[]> {

    private Context contextoPai;
    ArrayList<String[]> concluidos;

    private static class ViewHolder {
        private TextView nomeUsuarioTxt;
        private TextView nomeServicoTxt;
        private TextView dataInicioTxt;
        private TextView horaInicioTxt;
        private TextView dataFimTxt;
        private TextView horaFimTxt;
        private TextView notaSelecioadaTxt;
    }

    public ItemListaConcluido(Context contexto, ArrayList<String[]> dados) {
        super(contexto, R.layout.item_lista_concluidos, dados);

        this.contextoPai = contexto;
        this.concluidos = dados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String[] servicoAtual = concluidos.get(position);
        ViewHolder novaView;
        final View resultado;

        //primeira vez em que a lista é criada
        if (convertView == null) {
            novaView = new ItemListaConcluido.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_concluidos, parent, false);

            novaView.nomeUsuarioTxt = (TextView) convertView.findViewById(R.id.nomeUsuarioTxt);
            novaView.nomeServicoTxt = (TextView) convertView.findViewById(R.id.nomeServicoTxt);
            novaView.dataInicioTxt = (TextView) convertView.findViewById(R.id.dataInicioTxt);
            novaView.horaInicioTxt = (TextView) convertView.findViewById(R.id.horaInicioTxt);
            novaView.dataFimTxt = (TextView) convertView.findViewById(R.id.dataFimTxt);
            novaView.horaFimTxt = (TextView) convertView.findViewById(R.id.horaFimTxt);

            resultado = convertView;
            convertView.setTag(novaView);
        } else {
            //item modificado
            novaView = (ItemListaConcluido.ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando os valores
        novaView.nomeUsuarioTxt.setText(servicoAtual[0]);
        novaView.nomeServicoTxt.setText(servicoAtual[1]);
        novaView.dataInicioTxt.setText(servicoAtual[2]);
        novaView.horaInicioTxt.setText(servicoAtual[3]);
        novaView.dataFimTxt.setText(servicoAtual[4]);
        novaView.horaFimTxt.setText(servicoAtual[5]);

        return resultado;
    }

}
