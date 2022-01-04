package com.example.appagendamentotrabalhadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelo.Servico;

public class ItemListaServico extends ArrayAdapter<Servico> {

    private Context contextoPai;
    ArrayList<Servico> servicos;

    private static class ViewHolder {
        private TextView nomeServico;
        private TextView precoServico;
    }

    public ItemListaServico(Context contexto, ArrayList<Servico> dados) {
        super(contexto, R.layout.item_lista_servicos, dados);

        this.contextoPai = contexto;
        this.servicos = dados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Servico servicoAtual = servicos.get(position);
        ViewHolder novaView;
        final View resultado;

        //primeira vez em que a lista é criada
        if (convertView == null) {
            novaView = new ItemListaServico.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_servicos, parent, false);

            novaView.nomeServico = (TextView) convertView.findViewById(R.id.nomeServicoTxt);
            novaView.precoServico = (TextView) convertView.findViewById(R.id.precoServicoTxt);
            resultado = convertView;
            convertView.setTag(novaView);
        } else {
            //item modificado
            novaView = (ItemListaServico.ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando os valores
        novaView.nomeServico.setText(servicoAtual.getNome());
        novaView.precoServico.setText(String.format("%.2f",
                servicoAtual.getPreco()).replace(".",","));

        return resultado;
    }

}
