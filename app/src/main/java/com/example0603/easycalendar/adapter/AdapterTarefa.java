package com.example0603.easycalendar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example0603.easycalendar.R;

import java.util.ArrayList;
import java.util.List;

import com.example0603.easycalendar.activity.AdicionarTarefa;
import com.example0603.easycalendar.activity.PrincipalActivity;
import com.example0603.easycalendar.helper.DaoTarefa;
import com.example0603.easycalendar.model.Tarefa;

public class AdapterTarefa extends RecyclerView.Adapter<AdapterTarefa.MyViewHolder>{

    private List<Tarefa> listaTarefa = new ArrayList<>();
    private Context context;
    private RecyclerView recycler;
    private int dia,mes,ano,contador=0;

    public AdapterTarefa(List<Tarefa>list, Context c, RecyclerView r,int d,int m, int a) {
        this.listaTarefa = list;
        this.context = c;
        this.recycler = r;
        this.dia = d;
        this.mes = m;
        this.ano = a;
    }

    public boolean isEmpty(){
        if(listaTarefa.isEmpty()){
            return true; // vazia
        }else {
            return false; // não vazia
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_tarefas,parent,false);

        return new AdapterTarefa.MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa tarefa = listaTarefa.get( position );

        if(listaTarefa.isEmpty()){

            List<Tarefa> listaVazia = new ArrayList<>();
            AdapterTarefa adapterCliente = new AdapterTarefa(listaVazia,context.getApplicationContext(),recycler,dia,mes+1,ano);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
            recycler.setLayoutManager(layoutManager);
            recycler.setHasFixedSize(true);
            recycler.addItemDecoration(new DividerItemDecoration(context.getApplicationContext(), LinearLayoutManager.VERTICAL));
            recycler.setAdapter(adapterCliente);

            holder.nome.setVisibility(View.INVISIBLE);
            holder.imgcel.setVisibility(View.INVISIBLE);
            holder.imgtel.setVisibility(View.INVISIBLE);
            holder.imgDeletar.setVisibility(View.INVISIBLE);
            holder.celular.setVisibility(View.INVISIBLE);
            holder.telefone.setVisibility(View.INVISIBLE);
            holder.horario.setVisibility(View.INVISIBLE);
            holder.data.setVisibility(View.INVISIBLE);
            holder.remarcar.setVisibility(View.INVISIBLE);

        }
        else {

            DaoTarefa dao = new DaoTarefa(context.getApplicationContext());

            String nome = dao.getNome( tarefa.getIdCliente() );
            holder.nome.setText(nome);

            String telefone = dao.getTelefone( tarefa.getIdCliente() );
            if( telefone.equals("...") ){
                holder.imgtel.setVisibility( View.INVISIBLE );
                holder.telefone.setVisibility( View.INVISIBLE );
            }else {
                holder.telefone.setText( telefone );

                holder.telefone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tel = "tel:" + telefone;
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse( tel ));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

                holder.imgtel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tel = "tel:" + telefone;
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse( tel ));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }

            String celular = dao.getCelular( tarefa.getIdCliente() );
            if( celular.equals("...") ){
                holder.imgcel.setVisibility( View.INVISIBLE );
                holder.celular.setVisibility( View.INVISIBLE );
            }else {
                holder.celular.setText( celular );

                holder.celular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent ();
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendIntent.setAction (Intent.ACTION_SEND);
                        sendIntent.putExtra (Intent.EXTRA_TEXT, "Olá " + nome + "!");
                        sendIntent.putExtra ("jid", celular + "@s.whatsapp.net");
                        sendIntent.setPackage ("com.whatsapp");
                        sendIntent.setType ("text / plain");
                        context.startActivity (sendIntent);
                    }
                });

                holder.imgcel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent ();
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendIntent.setAction (Intent.ACTION_SEND);
                        sendIntent.putExtra (Intent.EXTRA_TEXT, "Olá " + nome + "!");
                        sendIntent.putExtra ("jid", celular + "@s.whatsapp.net");
                        sendIntent.setPackage ("com.whatsapp");
                        sendIntent.setType ("text / plain");
                        context.startActivity (sendIntent);
                    }
                });

            }

            holder.data.setText( tarefa.getDataDia() + "/" + tarefa.getDataMes() + "/" + tarefa.getDataAno() );
            holder.horario.setText( "Horário: " + tarefa.getHorario() );

            holder.imgDeletar.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                    alertDialog.setTitle( "CONFIRMAR EXCLUSÃO\n\n" );
                    alertDialog.setMessage( "Deseja excluir esta tarefa?");

                    alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(dao.deletar( tarefa )){

                                if(!listaTarefa.isEmpty()){

                                    Toast.makeText(context.getApplicationContext(),"Tarefa excluida com sucesso",
                                            Toast.LENGTH_SHORT).show();

                                    listaTarefa = dao.listarTarefasCalendar(dia,mes,ano);

                                    AdapterTarefa adapterCliente = new AdapterTarefa(listaTarefa,context.getApplicationContext(),recycler,dia,mes+1,ano);

                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
                                    recycler.setLayoutManager(layoutManager);
                                    recycler.setHasFixedSize(true);
                                    recycler.addItemDecoration(new DividerItemDecoration(context.getApplicationContext(), LinearLayoutManager.VERTICAL));
                                    recycler.setAdapter(adapterCliente);
                                }
                            }

                        }
                    });

                    alertDialog.setNegativeButton("Não", null);
                    alertDialog.create().show();
                }
            });

            holder.remarcar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!celular.equals("...")){

                        Intent sendIntent = new Intent ();
                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendIntent.setAction (Intent.ACTION_SEND);
                        sendIntent.putExtra (Intent.EXTRA_TEXT, "Olá " + nome + ", tudo bem?\n\nEstou enviando esta mensagem para analisar sua disponibilidade de " +
                                "reagendar para dia:\n xx do xx às xx:xx.\n\nDesde já agradecemos pela atenção!");
                        sendIntent.putExtra ("jid", celular + "@s.whatsapp.net");
                        sendIntent.setPackage ("com.whatsapp");
                        sendIntent.setType ("text / plain");
                        context.startActivity (sendIntent);

                    }else {

                        Toast.makeText(context.getApplicationContext(), "Não existe número de celular cadastrado",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });

            holder.imgAtualizarTarefa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent( context.getApplicationContext(), AdicionarTarefa.class );
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tarefaSelecionada", tarefa);
                    context.startActivity(intent);

                    listaTarefa = dao.listarTarefasCalendar(dia,mes,ano);
                    AdapterTarefa adapterCliente = new AdapterTarefa(listaTarefa,context.getApplicationContext(),recycler,dia,mes+1,ano);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
                    recycler.setLayoutManager(layoutManager);
                    recycler.setHasFixedSize(true);
                    recycler.addItemDecoration(new DividerItemDecoration(context.getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recycler.setAdapter(adapterCliente);

                }
            });

            if(tarefa.getStatus() == 0){
                holder.sStatus.setChecked(false);
                holder.status.setText("Não feita");
            }else if( tarefa.getStatus() == 1 ){
                holder.sStatus.setChecked(true);
                holder.status.setText("Feita");
            }

            holder.sStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.sStatus.isChecked()){

                        tarefa.setStatus(1);
                        dao.atualizarStatus( tarefa );
                        holder.status.setText("Feita");

                    }else {

                        tarefa.setStatus(0);
                        dao.atualizarStatus( tarefa );
                        holder.status.setText("Não feita");

                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return listaTarefa.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nome,data,horario,telefone,celular,remarcar,status;
        private ImageView imgDeletar,imgtel,imgcel,imgAtualizarTarefa;
        private Switch sStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeCompleto);
            data = itemView.findViewById(R.id.txtDataCompleta);
            horario = itemView.findViewById(R.id.txtHorarioCompleto);
            telefone = itemView.findViewById(R.id.txtTelefoneCompleto);
            celular = itemView.findViewById(R.id.txtCelularCompleto);
            remarcar = itemView.findViewById(R.id.txtRemarcar_Confirmar);
            status = itemView.findViewById(R.id.txtStatus);

            imgDeletar = itemView.findViewById(R.id.imgApagarTarefa);
            imgtel = itemView.findViewById(R.id.imgCallTelefone);
            imgcel = itemView.findViewById(R.id.imgCallCelular);
            imgAtualizarTarefa = itemView.findViewById(R.id.imgAtualizarTarefa);

            sStatus = itemView.findViewById(R.id.switchStatus);
        }
    }
}
