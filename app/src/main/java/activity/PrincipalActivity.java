package activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.R;
import com.example.noteapp.databinding.ActivityPrincipalBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.AdapterCliente;
import adapter.AdapterTarefa;
import helper.DaoCliente;
import helper.DaoProp;
import helper.DaoTarefa;
import model.Proprietario;
import model.Tarefa;

public class PrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPrincipalBinding binding;
    private CalendarView calendarView;
    private RecyclerView recyclerTarefas;
    private List<Tarefa> listaTarefa = new ArrayList<>();
    private TextView txtSemTarefas,txtNomeProp;
    private Boolean resposta;
    private int d,m,a;
    private String nome="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        calendarView = findViewById(R.id.calendarView);
        recyclerTarefas = findViewById(R.id.recyclerTarefas);
        txtSemTarefas = findViewById(R.id.txtSemtarefasHj);
        txtNomeProp = findViewById(R.id.txtNomeProprietario);

        nome = (String) getIntent().getSerializableExtra("nomeProprietario");
        if(nome!=null){
            txtNomeProp.setText( "Olá " + nome );
        }else{
            DaoProp dao = new DaoProp(getApplicationContext());
            nome = dao.verficarUsuario();
            txtNomeProp.setText("Olá " + nome);
        }

        carregar();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void carregar(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int ano, int mes, int diaDoMes) {

                d = diaDoMes;
                m = mes+1;
                a = ano;

                DaoTarefa d = new DaoTarefa(getApplicationContext());
                listaTarefa = d.listarTarefasCalendar(diaDoMes,mes+1,ano);

                AdapterTarefa adapterTarefa = new AdapterTarefa(listaTarefa,getApplicationContext(),recyclerTarefas,diaDoMes,mes+1,ano);
                resposta = adapterTarefa.isEmpty();

                if(!listaTarefa.isEmpty()){

                    txtSemTarefas.setVisibility( View.INVISIBLE );
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerTarefas.setLayoutManager(layoutManager);
                    recyclerTarefas.setHasFixedSize(true);
                    recyclerTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerTarefas.setAdapter(adapterTarefa);

                }else {
                    List<Tarefa> listaVazia = new ArrayList<>();
                    AdapterTarefa adapterCliente = new AdapterTarefa(listaVazia,getApplicationContext(),recyclerTarefas,diaDoMes,mes+1,ano);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerTarefas.setLayoutManager(layoutManager);
                    recyclerTarefas.setHasFixedSize(true);
                    recyclerTarefas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerTarefas.setAdapter(adapterCliente);

                    txtSemTarefas.setVisibility( View.VISIBLE );
                }
            }
        });
    }

    public void irAddCliente(View view){
        startActivity( new Intent(this,AdicionarCliente.class));
    }

    public void irAddTarefa(View view){
        startActivity( new Intent(this,AdicionarTarefa.class));
    }

    public void irListaCliente(View view){
        startActivity( new Intent(this,ListaCliente.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();*/
        return true;
    }
}