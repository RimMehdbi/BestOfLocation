package com.rim.asus.bestoflocation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chargement extends AppCompatActivity {
    Button b1 ;
    Button b2 ;
    ProgressBar br   ;
    TextView txt ;
    ListView lv ;
    Boolean b = false ;
    telechargement2 t = new telechargement2();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargement);

        b1= findViewById(R.id.st_bt) ;
        b2= findViewById(R.id.stp_qt) ;
        br = findViewById(R.id.progressBar3) ;
        txt = findViewById(R.id.txt_pr);
        lv = findViewById(R.id.lv) ;
        t.execute();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b=true ;

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            b=false ;
            }
        });




    }

    class Animation extends  Thread
    {

        public void run ()
        {
            while (true)
            {

                while(br.getProgress()<100 && b)
                {

                    br.incrementProgressBy(5);
                    txt.setText(br.getProgress()+"%");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (br.getProgress()>=100)
                        br.setProgress(0);
                    txt.setText(0+"%");


                }



            }




        }




    }

    class telechargement extends AsyncTask <Void , Void,Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            while (true)
            {

                while(br.getProgress()<100 && b)
                {

                    br.incrementProgressBy(5);
                    publishProgress();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (br.getProgress()>=100)
                        br.setProgress(0);
                       publishProgress();



                }



            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            b1.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            txt.setText(br.getProgress()+"%");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }



    class telechargement2 extends AsyncTask <Void , Void,Void>
    {    ProgressDialog dialog ;
         ArrayList data = new ArrayList() ;


        @Override
        protected Void doInBackground(Void... voids) {
            JSONParser json = new JSONParser();
            String ip="192.168.43.127" ;
            JSONObject resultat = json.makeRequest("http://"+ip+"/test_servicephp/get_all_Adresses.php");
            try {
                String s = resultat.getString("succes") ;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONArray tab = resultat.getJSONArray("adresse") ;
                for (int i = 0 ; i<tab.length() ; i++)
                {
                    JSONObject ligne = tab.getJSONObject(i);
                    String i1 = ligne.getString("IdAdresse") ;
                    String i2 = ligne.getString("Longitude") ;
                    String i3 = ligne.getString("Latitude") ;
                    String i4 = ligne.getString("Type") ;
                    data.add(i1+i2+i3+i4);





                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Chargement.this) ;
            dialog.setTitle("veuillez patientez");
            dialog.setMessage("téléchargement des données JSON ");
            dialog.show();
        }

       /* @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);


        }*/

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            lv.setAdapter(new ArrayAdapter<>(Chargement.this ,android.R.layout.simple_list_item_1,data));

            dialog.dismiss();
        }
    }


}
