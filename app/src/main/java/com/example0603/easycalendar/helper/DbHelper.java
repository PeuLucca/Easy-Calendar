package com.example0603.easycalendar.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static int VERSION = 1; // versão do app
    private static String NOME_DB = "db_NoteApp"; // nome do banco de dados
    public static String TABELA_CLIENTE = "cliente";
    public static String TABELA_TAREFA = "tarefa";
    public static String TABELA_PROPRIETARIO = "proprietario";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
        String sqlCliente = "CREATE TABLE IF NOT EXISTS " + TABELA_CLIENTE +
                "(idCliente INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "telefone TEXT," +
                "celular TEXT," +
                "email TEXT);";

        String sqlTarefa = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFA +
                "(idTarefa INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idCliente int NOT NULL," +
                "dataMes int NOT NULL," +
                "dataDia int NOT NULL," +
                "dataAno int NOT NULL," +
                "horario TEXT NOT NULL," +
                "status BOOLEAN NOT NULL);";

            String sqlProprietario = "CREATE TABLE IF NOT EXISTS " + TABELA_PROPRIETARIO +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL)";

            db.execSQL(sqlCliente);
            db.execSQL(sqlTarefa);
            db.execSQL(sqlProprietario);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
