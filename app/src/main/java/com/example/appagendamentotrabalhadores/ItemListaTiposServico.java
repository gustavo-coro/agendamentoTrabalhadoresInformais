package com.example.appagendamentotrabalhadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelo.TiposServicos;

public class ItemListaTiposServico extends ArrayAdapter<TiposServicos> {

    private Context contextoPai;
    ArrayList<TiposServicos> tiposServicos;

    private static class ViewHolder {
        private TextView nomeTipoServico;
    }

    public ItemListaTiposServico(Context contexto, ArrayList<TiposServicos> dados) {
        super(contexto, R.layout.item_lista_tipos_servicos, dados);

        this.contextoPai = contexto;
        this.tiposServicos = dados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TiposServicos servicoAtual = tiposServicos.get(position);
        ViewHolder novaView;
        final View resultado;

        //primeira vez em que a lista é criada
        if (convertView == null) {
            novaView = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_tipos_servicos, parent, false);

            novaView.nomeTipoServico = (TextView) convertView.findViewById(R.id.nomeTipoTrabalhoTxt);
            resultado = convertView;
            convertView.setTag(novaView);
        } else {
            //item modificado
            novaView = (ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando os valores
        novaView.nomeTipoServico.setText(servicoAtual.getNomeTipoServico());

        return resultado;

    }
}
