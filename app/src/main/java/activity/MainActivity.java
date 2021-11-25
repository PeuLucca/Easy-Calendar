package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;

import com.example.noteapp.R;
import com.google.android.material.snackbar.Snackbar;

import helper.DaoProp;
import helper.DbHelper;
import model.Proprietario;

public class MainActivity extends AppCompatActivity {

    private EditText editNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNomeProprietario);

        verificarLogin();
    }

    @Override
    protected void onStart() {
        verificarLogin();
        super.onStart();
    }

    public void verificarLogin(){

        DaoProp dao = new DaoProp(getApplicationContext());
        String nome = dao.verficarUsuario();

        if(nome != null){
            Intent intent = new Intent( this, PrincipalActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("nomeProprietario", nome);
            startActivity(intent);
            finish();
        }
    }

    public void irPrincipal(View view){

        if(!editNome.getText().toString().equals("")){

            DaoProp daoProp = new DaoProp(getApplicationContext());

            Proprietario p = new Proprietario();
            p.setNome( editNome.getText().toString() );
            if(daoProp.salvar(p)){

                Toast.makeText(getApplicationContext(),"Usuário criado",Toast.LENGTH_SHORT).show();
                startActivity( new Intent( this, PrincipalActivity.class ) );

            }else {
                Toast.makeText(getApplicationContext(),"Erro ao criar usuário",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Campo de nome vazio",Toast.LENGTH_SHORT).show();
        }
    }
}