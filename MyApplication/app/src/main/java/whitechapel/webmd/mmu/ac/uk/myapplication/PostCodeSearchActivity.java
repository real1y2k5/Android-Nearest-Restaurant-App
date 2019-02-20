package whitechapel.webmd.mmu.ac.uk.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PostCodeSearchActivity extends ActionBarActivity {

    TableRow tr[] = new TableRow[10];
    TextView tv[] = new TextView[10];
    ImageView iv[] = new ImageView[10];
    TableLayout tlayout;
    String postCode;
    String restName;
    String final_url;
    int tcounter = 0;
    ArrayList<String> listLat = new ArrayList<String>();
    ArrayList<String> listLong = new ArrayList<String>();
    ArrayList<String> listRating = new ArrayList<String>();
    ArrayList<String> listName = new ArrayList<String>();
    ArrayList<String> listAddress1 = new ArrayList<String>();
    ArrayList<String> listAddress2 = new ArrayList<String>();
    ArrayList<String> listAddress3 = new ArrayList<String>();

    // set image margins
    int leftMargin = 0;
    int topMargin = 55;
    int rightMargin = 0;
    int bottomMargin = 0;


    // set table row layout parameters for image
    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_code_search);

        StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        // get postcode and name from main activity
        postCode = getIntent().getStringExtra("postCode");
        restName = getIntent().getStringExtra("restName");
        //longi = getIntent().getDoubleExtra("long", 10);

        // get table layout and set up tablerows for data
        tlayout =(TableLayout)findViewById(R.id.table);

        // apply margins to table row parameters
        tableRowParams.setMargins(leftMargin, topMargin, rightMargin,
                bottomMargin);

        callConnection();
    }

    private void callConnection(){
        // make server connection
        try {
           if (postCode != null) {
               postCode = postCode.replace(" ","");
               final_url = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_postcode&postcode=" + postCode;
           }
            else {
              // if (restName != null) {
                   restName = restName.replace(" ", "%20");
                   final_url = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_name&name=" + restName;
               //}
           }
                URL url = new URL(final_url);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //get and store server response
                InputStreamReader ins = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader in = new BufferedReader(ins);

            //read input stream
            String line = "";
            while((line = in.readLine()) != null) {

                JSONArray jArray = new JSONArray(line);
                for (int i=0; i<jArray.length(); i++) {
                    tr[tcounter] = new TableRow(this);
                    tv[tcounter] = new TextView(this);
                    iv[tcounter] = new ImageView(this);
                    iv[tcounter].setLayoutParams(tableRowParams);

                    JSONObject jObject = (JSONObject) jArray.get(i);
                    // get latitude
                    listLat.add(jObject.getString("Latitude"));
                    //get longitude
                    listLong.add(jObject.getString("Longitude"));
                    //get rating
                    listRating.add(jObject.getString("RatingValue"));
                    listName.add(jObject.getString("BusinessName"));
                    listAddress1.add(jObject.getString("AddressLine1"));
                    listAddress2.add(jObject.getString("AddressLine2"));
                    listAddress3.add(jObject.getString("AddressLine3"));
                    // if statemnet to check rating value
                    if ((jObject.get("RatingValue")).equals("-1")) {
                        tv[tcounter].setText(Html.fromHtml("<br>" + "<b>" + (jObject.getString("BusinessName") + "</b>" + "<br>" + jObject.getString("AddressLine1")
                                + "<br>" + jObject.getString("AddressLine2") + "<br>" + jObject.getString("AddressLine3") + "<br>"
                                + jObject.getString("PostCode") + "<br>" + "Exempt" )));
                    }

                    else {
                        tv[tcounter].setText(Html.fromHtml("<br>" + "<b>" + (jObject.getString("BusinessName") + "</b>" + "<br>" + jObject.getString("AddressLine1")
                                + "<br>" + jObject.getString("AddressLine2") + "<br>" + jObject.getString("AddressLine3") + "<br>"
                                + jObject.getString("PostCode") + "<br>" + "Rating Value: "+jObject.getString("RatingValue"))));
                    }

                    // switch statment to select image
                    switch (jArray.getJSONObject(i).getString("RatingValue")){

                        case "0":
                            Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rating0);
                            Bitmap bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        case "1":
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rating1);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        case "2":
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rating2);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        case "3":
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rrating3);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        case "4":
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rating4);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        case "5":
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rating5);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        case "-1":
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.ratinex);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;
                        default:
                            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.rating_1);
                            bitmapSize = Bitmap.createScaledBitmap(bmp, 350, 200, true);
                            iv[tcounter].setImageBitmap(bitmapSize);
                            break;


                    }


                    // add textview to table row
                    tr[tcounter].addView(tv[tcounter]);
                    //add image view to table row
                    tr[tcounter].addView(iv[tcounter]);
                    //add table row to table layout
                    tlayout.addView(tr[tcounter]);
                    // increment counter
                    tcounter++;
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_code_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void showOnMapBtnOnClick(View view) {

        Intent intent = new Intent(PostCodeSearchActivity.this, MainActivity.class);
        intent.putStringArrayListExtra("latArray",listLat);
        intent.putStringArrayListExtra("longArray",listLong);
        intent.putStringArrayListExtra("ratingArray",listRating);
        intent.putStringArrayListExtra("nameArray",listName);
        intent.putStringArrayListExtra("add1Array",listAddress1);
        intent.putStringArrayListExtra("add2Array",listAddress2);
        intent.putStringArrayListExtra("add3Array",listAddress3);
        startActivity(intent);
        finish();
        listLat.clear();
        listLong.clear();
        listRating.clear();
        listName.clear();
        listAddress1.clear();
        listAddress2.clear();
        listAddress3.clear();

    }

    public void backToHomeBtnOnClick(View view) {
        listLat.clear();
        listLong.clear();
        listRating.clear();
        listName.clear();
        listAddress1.clear();
        listAddress2.clear();
        listAddress3.clear();
        finish();
    }
}
