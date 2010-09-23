package no.poltilbud.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity{
	
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);  
        
        Button beerButton = (Button)this.findViewById(R.id.beerButton);
        beerButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_BEER);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button redwineButton = (Button)this.findViewById(R.id.redwineButton);
        redwineButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_REDWINE);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button whitewhineButton = (Button)this.findViewById(R.id.whitewhineButton);
        whitewhineButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_WHITEWINE);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button rosewineButton = (Button)this.findViewById(R.id.rosewineButton);
        rosewineButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_ROSE);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button champButton = (Button)this.findViewById(R.id.champButton);
        champButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_CHAMP);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button strongwineButton = (Button)this.findViewById(R.id.strongwineButton);
        strongwineButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_STRONGWINE);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button spiritButton = (Button)this.findViewById(R.id.spiritButton);
        spiritButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_SPRIRT);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button nonalchButton = (Button)this.findViewById(R.id.nonalchButton);
        nonalchButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_NONALCH);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button fruitwineButton = (Button)this.findViewById(R.id.fruitwineButton);
        fruitwineButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_FRUITWINE);
              startActivityForResult(myIntent, 0);
          }
        });
        
        Button fetchOffersButton = (Button)this.findViewById(R.id.fetchOffersButton);
        fetchOffersButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
        	  Intent myIntent = new Intent(v.getContext(), ProductList.class);
        	  myIntent.putExtra(PoltilbudEnums.BUTTONEXTR, PoltilbudEnums.BUTTON_FETCHOFFERS);
              startActivityForResult(myIntent, 0);
          }
        });
        
        TextView dateFetchedText = (TextView)this.findViewById(R.id.TextFetchedDate);
        dateFetchedText.append(offerDateFromFile());
        
    }
	
	private String offerDateFromFile(){
		String offerDate = new String();

		try {
		    InputStream in = openFileInput(PoltilbudEnums.offerFetchedDate);

		    if (in != null) {
		      InputStreamReader input = new InputStreamReader(in);
		      BufferedReader buffreader = new BufferedReader(input);
		      String str;
		      StringBuffer stringBuffer = new StringBuffer();
		      while (( str = buffreader.readLine()) != null) {
		        stringBuffer.append(str + "\n");
		      }
		      in.close();
		      offerDate = stringBuffer.toString();
		      Log.i("Poltilbud", "XML file from local: " + offerDate);
		    }
		    
		    Log.i("Poltilbud", "File read");
		    
		} catch (java.io.FileNotFoundException e) {
		  Log.i("Poltilbud", "File read");
		  e.printStackTrace();		 
	  	} catch (IOException e) {
		  Log.i("Poltilbud", "File read");
		  e.printStackTrace();		  
		}

		return offerDate;
	}
    
	

}
