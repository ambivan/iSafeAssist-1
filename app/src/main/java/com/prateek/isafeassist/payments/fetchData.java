package com.prateek.isafeassist.payments;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void, Void,Void> {

    String transact;
    String data="";

    public fetchData(){

    }
    public fetchData(String transact){
        this.transact= transact;

    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url= new URL("https://rzp_test_38c3EtFqcXSfDQ:TgCEphGbwTH6PgsPKTVu00CU@api.razorpay.com/v1/payments/"+transact);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("rzp_test_sa6kn8NSqpmCef","rzp_test_38c3EtFqcXSfDQ");
            httpURLConnection.setRequestProperty("rzp_test_sa6kn8NSqpmCef","TgCEphGbwTH6PgsPKTVu00CU");
            InputStream inputStream= httpURLConnection.getInputStream();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while(line!=null){
                line= bufferedReader.readLine();
                data= data+line;
                System.out.println(data);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("return request message"+data);
    }
}
