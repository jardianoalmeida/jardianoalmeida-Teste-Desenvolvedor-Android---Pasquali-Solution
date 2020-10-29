package com.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.squareup.picasso.Picasso;
import com.teste.config.CircleTransform;
import com.teste.model.Player;
import com.teste.service.HTTPService;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private TextView name;
    private TextView country;
    private TextView pos;
    private TextView percentual;
    private TextView cpDisputada;
    private TextView cpVencida;
    private ImageView imgPerson;
    private NumberProgressBar numberProgressBar1;
    private NumberProgressBar numberProgressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        cpVencida = findViewById(R.id.copas_vencidas_pos);
        cpDisputada = findViewById(R.id.copas_disputadas_pos);
        country = findViewById(R.id.country);
        pos = findViewById(R.id.pos);
        percentual = findViewById(R.id.percentual);
        imgPerson = findViewById(R.id.img_person);
        numberProgressBar1 = findViewById(R.id.number_progress_bar1);
        numberProgressBar2 = findViewById(R.id.number_progress_bar2);

        try {
            isOnline(getApplicationContext());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void isOnline(Context context) throws ExecutionException, InterruptedException {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()){
            Player player = new HTTPService().execute().get();
            Picasso.with(context).load(player.getImg()).transform(new CircleTransform()).fit().into(this.imgPerson);

            Double percentual = Double.parseDouble(player.getPercentual());
            String resultado = String.format("%.3f", percentual);

            this.numberProgressBar1.setMax(Integer.parseInt(player.getBarra().getCopaMundoDisputada().getMax()));
            this.numberProgressBar2.setMax(Integer.parseInt(player.getBarra().getCopaMundoVencida().getMax()));
            this.numberProgressBar1.setProgress(Integer.parseInt(player.getBarra().getCopaMundoDisputada().getPla()));
            this.numberProgressBar2.setProgress(Integer.parseInt(player.getBarra().getCopaMundoVencida().getPla()));
            this.name.setText(player.getName());
            this.country.setText(player.getCountry());
            this.pos.setText(player.getPos());
            this.percentual.setText(resultado);
            cpDisputada.setText(player.getBarra().getCopaMundoDisputada().getPos()+"º");
            cpVencida.setText(player.getBarra().getCopaMundoVencida().getPos()+"º");
        }else {
            Toast.makeText(context, "No momento você sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }
}
