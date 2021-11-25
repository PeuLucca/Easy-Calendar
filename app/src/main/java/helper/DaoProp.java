package helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Cliente;
import model.Proprietario;

public class DaoProp {
    private SQLiteDatabase escreve,le;
    private Cursor cursor;

    public DaoProp(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    public boolean salvar(Proprietario proprietario) {

        ContentValues cv = new ContentValues();
        cv.put("nome",proprietario.getNome());

        try{
            escreve.insert( DbHelper.TABELA_PROPRIETARIO , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deletar(Proprietario proprietario) {

        String[] args = { proprietario.getId().toString() };

        try {
            escreve.delete(DbHelper.TABELA_PROPRIETARIO,"id=?",args);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String verficarUsuario(){

        String nome = "";
        String sql = "SELECT nome FROM " + DbHelper.TABELA_PROPRIETARIO + ";";
        cursor = le.rawQuery( sql, null );

        while(cursor.moveToNext()){

            @SuppressLint("Range") String name = cursor.getString( cursor.getColumnIndex("nome") );
            nome = name;
        }

        if(nome.equals("")){
            return null;
        }else{
            return nome;
        }
    }

}
