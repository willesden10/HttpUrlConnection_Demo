package gab.httpurlconnection_demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

//AsyncTask enables proper and easy use of the UI thread. This class allows to perform background operations
// and publish results on the UI thread without having to manipulate threads and/or handlers.
public class FetchData extends AsyncTask<String, Void, String> {
    private Activity mActivity;

    FetchData(Activity activity){
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        //An URLConnection for HTTP (RFC 2616) used to send and receive data over the web. Data may be of any type and length.
        //This class may be used to send and receive streaming data whose length is not known in advance.
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(params[0]);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            //Sets the request command which will be sent to the remote HTTP server. This method can only be called before the connection is made.
            httpURLConnection.setRequestMethod("GET");

            //Returns an InputStream for reading data from the resource pointed by this URLConnection.
            // It throws an UnknownServiceException by default. This method must be overridden by its subclasses.
            InputStream inputStream = httpURLConnection.getInputStream();

            //Wraps an existing Reader and buffers the input.
            //Expensive interaction with the underlying reader is minimized, since most (smaller) requests can be satisfied by accessing the buffer alone.
            // The drawback is that some extra space is required to hold the buffer and that copying takes place when filling that buffer,
            // but this is usually outweighed by the performance benefits.
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //A modifiable sequence of characters for use in creating strings.
            //This class is intended as a direct replacement of StringBuffer for non-concurrent use;
            //unlike StringBuffer this class is not synchronized.
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line+"\n");
            }

            if(stringBuilder.length() != 0) return stringBuilder.toString();
        }

        catch(MalformedURLException exc){
            exc.printStackTrace(System.err);
        }
        catch(ProtocolException exc){
            exc.printStackTrace(System.err);
        }
        catch(IOException exc){
            exc.printStackTrace(System.err);
        }
        finally {
            if(httpURLConnection != null)
                    httpURLConnection.disconnect();
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String weather) {
        super.onPostExecute(weather);

        TextView textView = (TextView) mActivity.findViewById(R.id.textview);

        if(weather == null) textView.setText("No data available.");
        else textView.setText(weather);
    }
}
