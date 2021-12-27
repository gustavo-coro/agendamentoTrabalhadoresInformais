package com.example.appagendamentotrabalhadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListaSolicitacao extends ArrayAdapter<String[]> {

    private Context contextoPai;
    ArrayList<String[]> solicitacoes;

    private static class ViewHolder {
        private TextView nomeUsuarioTxt;
        private TextView nomeServicoTxt;
        private TextView dataServicoTxt;
        private TextView horaServicoTxt;
    }

    public ItemListaSolicitacao(Context contexto, ArrayList<String[]> dados) {
        super(contexto, R.layout.item_lista_solicitacoes, dados);

        this.contextoPai = contexto;
        this.solicitacoes = dados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String[] solicitacaoAtual = solicitacoes.get(position);
        ItemListaSolicitacao.ViewHolder novaView;
        final View resultado;

        //primeira vez em que a lista é criada
        if (convertView == null) {
            novaView = new ItemListaSolicitacao.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_solicitacoes, parent, false);

            novaView.nomeUsuarioTxt = (TextView) convertView.findViewById(R.id.nomeUsuarioTxt);
            novaView.nomeServicoTxt = (TextView) convertView.findViewById(R.id.nomeServicoTxt);
            novaView.dataServicoTxt = (TextView) convertView.findViewById(R.id.dataServicoTxt);
            novaView.horaServicoTxt = (TextView) convertView.findViewById(R.id.horaServicoTxt);

            resultado = convertView;
            convertView.setTag(novaView);
        } else {
            //item modificado
            novaView = (ItemListaSolicitacao.ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando os valores
        novaView.nomeUsuarioTxt.setText(solicitacaoAtual[4]);
        novaView.nomeServicoTxt.setText(solicitacaoAtual[5]);
        novaView.dataServicoTxt.setText(solicitacaoAtual[2]);
        novaView.horaServicoTxt.setText(solicitacaoAtual[3]);

        return resultado;
    }

}
