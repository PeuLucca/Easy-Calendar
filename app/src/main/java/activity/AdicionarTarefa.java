package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapter.AdapterCliente;
import adapter.AdapterListaCliente;
import adapter.RecycleViewItemClickListener;
import helper.DaoCliente;
import helper.DaoTarefa;
import model.Cliente;
import model.Tarefa;


public class AdicionarTarefa extends AppCompatActivity {

    private TextView txtClienteSelecionado,txtMessage;
    private EditText hra,min;
    private CalendarView calendar;
    private FloatingActionButton fab;
    private RecyclerView recyclerCliente;
    private List<Cliente> listaCliente = new ArrayList<>();
    private Cliente cliente;
    private Long idCliente;
    private int day,month,year,contador=0;
    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        txtClienteSelecionado = findViewById(R.id.txtClienteSelecionado);
        txtMessage = findViewById(R.id.txtMensage);
        hra = findViewById(R.id.editHora);
        min = findViewById(R.id.editMinuto);
        calendar = findViewById(R.id.calendarViewTarefa);
        recyclerCliente = findViewById(R.id.recyclerClientes);
        fab = findViewById(R.id.fabSalvar);

        listadeClientes();

        tarefa = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        if(tarefa!=null){ // para atualizar

            String retornoHorario[] = tarefa.getHorario().split( ":" );
            String hra2 = retornoHorario[0];
            String min2 = retornoHorario[1];

            hra.setText( hra2 );
            min.setText( min2 );

            /*DaoTarefa d2 = new DaoTarefa(getApplicationContext());
            String nome = d2.getNome(tarefa.getIdCliente());
            txtClienteSelecionado.setText("Cliente selecionado: " + nome );*/


            int day2 = tarefa.getDataDia();
            int month2 = tarefa.getDataMes() - 1;
            int year2 = tarefa.getDataAno();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year2);
            cal.set(Calendar.MONTH, month2);
            cal.set(Calendar.DAY_OF_MONTH, day2);

            long milliTime = cal.getTimeInMillis();

            calendar.setDate(milliTime,true,true);

        }

        recyclerCliente.addOnItemTouchListener(new RecycleViewItemClickListener(getApplicationContext(), recyclerCliente, new RecycleViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Cliente clienteSelected = listaCliente.get( position );
                if(!clienteSelected.getNome().equals("")){

                    DaoTarefa daoTarefa = new DaoTarefa(getApplicationContext());
                     idCliente =  daoTarefa.getHorario(clienteSelected.getNome());
                }
                txtClienteSelecionado.setText( "Cliente selecionado: " + clienteSelected.getNome() );
            }
            @Override
            public void onLongItemClick(View view, int position) {}

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
        }));

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int ano, int mes, int diaDoMes) {
                year = ano;
                month = mes+1;
                day = diaDoMes;
                contador++;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( validarDados() ){


                    if(tarefa == null){ // salvar

                        Tarefa t = new Tarefa();
                        t.setIdCliente( idCliente );
                        t.setDataDia( day );
                        t.setDataMes( month );
                        t.setDataAno( year );
                        t.setHorario( hra.getText().toString() + ":" + min.getText().toString() );
                        t.setStatus(0);

                        DaoTarefa dao = new DaoTarefa(getApplicationContext());
                        dao.salvar( t );
                        Toast.makeText(getApplicationContext(),"Tarefa salva com sucesso",Toast.LENGTH_SHORT).show();
                        finish();

                        hra.setText("");
                        min.setText("");
                        txtClienteSelecionado.setText( "Nenhum cliente selecionado" );

                    }else{ // atualizar
                        Tarefa tar = new Tarefa();
                        tar.setIdCliente( tarefa.getIdCliente() );
                        tar.setIdTarefa(tarefa.getIdTarefa());
                        tar.setDataDia( day );
                        tar.setDataMes( month );
                        tar.setDataAno( year );
                        tar.setHorario( hra.getText().toString() + ":" + min.getText().toString() );

                        DaoTarefa d = new DaoTarefa(getApplicationContext());
                        if(d.atualizar( tar )){
                            Toast.makeText(getApplicationContext(),"Tarefa atualizada com sucesso",Toast.LENGTH_SHORT).show();
                            finish();

                            hra.setText("");
                            min.setText("");
                            txtClienteSelecionado.setText( "Nenhum cliente selecionado" );

                        }else {
                            Toast.makeText(getApplicationContext(),"Erro ao atualizar tarefa",Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {
                    Snackbar.make(
                            view,
                            "Selecione novamente a data e o nome do cliente",
                            Snackbar.LENGTH_LONG
                    ).setAction("Ok,entendi", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    }).setActionTextColor(getResources().getColor(R.color.purple_1000)).show();
                }
            }
        });

    }

    public boolean validarDados() {

        if( hra.getText().toString().equals("") || min.getText().toString().equals("")){
            return false;
        }

        Long hora, minuto;
        hora = Long.parseLong(hra.getText().toString());
        minuto = Long.parseLong(min.getText().toString());

        if(idCliente == null || idCliente == 0){
            return false;
        }

        if(contador==0){
            return false;
        }

        if (hora >= 0 && hora <= 24) {
            if (minuto >= 0 && minuto < 60) {
                if (contador > 0) {
                    return true;
                }
            }
        }
            return false;

    }

    public void listadeClientes(){

        DaoCliente daoCliente = new DaoCliente(getApplicationContext());
        listaCliente = daoCliente.listar();

        if(!listaCliente.isEmpty()){

            AdapterListaCliente adapter = new AdapterListaCliente(listaCliente,getApplicationContext(),recyclerCliente);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerCliente.setLayoutManager(layoutManager);
            recyclerCliente.setHasFixedSize(true);
            recyclerCliente.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            recyclerCliente.setAdapter(adapter);
        }else {
            txtMessage.setVisibility(View.VISIBLE);
        }

    }

}
