package no.poltilbud.feed;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProductList extends ListActivity {
	
	private List<Product> m_productsFromXML = null;
	private ArrayList<Product> m_products = null;
	private ProgressDialog m_ProgressDialog;
	private ProductAdapter m_adapter;
	private final String offerXMLFilename = "offersFromPoltilbud.xml";
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);  
        //loadFeed(false);
        if (isOnline() && !offerFileExists()) {
        	loadFeed(false);
        }    	
        else if (offerFileExists()) {
        	loadFeed(true);
		}
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, 0, 
				0, R.string.android_sax);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		ArrayAdapter<String> adapter =
			(ArrayAdapter<String>) this.getListAdapter();
		if (adapter.getCount() > 0){
			adapter.clear();
		}
		this.loadFeed(false);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent viewMessage = new Intent(Intent.ACTION_VIEW, 
				Uri.parse(m_productsFromXML.get(position).getLink().toExternalForm()));
		this.startActivity(viewMessage);
	}

	private void loadFeed(boolean useLocalXMLFile){
    	try{
	    	final FeedParser parser;
	    	if (useLocalXMLFile){
	    		parser = FeedParserFactory.getParser(getLocalInputStream());
	    	} else
	    	{
	    		parser = FeedParserFactory.getParser();
	    	}
	    	
	    	m_products = new ArrayList<Product>();
	        this.m_adapter = new ProductAdapter(this, R.layout.row, m_products);
	                setListAdapter(this.m_adapter);
	    	
	    	Runnable viewProducts = new Runnable(){
	            public void run() {
	            	long start = System.currentTimeMillis();
	            	m_productsFromXML = parser.parse();	 
	            	m_products = new ArrayList<Product>(m_productsFromXML.size());
	    	    	for (Product msg : m_productsFromXML){
	    	    		m_products.add(msg);
	    	    	}
	    	    	long duration = System.currentTimeMillis() - start;
	    	    	Log.i("Poltilbud", "Parser duration=" + duration);
	    	    	String xml = writeXml();
	    	    	Log.i("Poltilbud", xml);
	    	    	saveFileToFilesystem(xml);
	    	    	Log.i("Poltilbud", "File written to filesystem");
	            	runOnUiThread(returnRes);	            	
	            }
	        };
		    Thread thread =  new Thread(null, viewProducts, "MagentoBackground");
		        thread.start();
		        
		    String messageToUser = "";
		    if (!useLocalXMLFile){
		    	messageToUser = "Henter tilbud";
		    } else
		    {
		    	messageToUser = "Henter tilbud fra lokalt buffer";
		    }
	        m_ProgressDialog = ProgressDialog.show(ProductList.this,    
	              "Vennligst vent...", messageToUser, true);
	
	    	this.setListAdapter(m_adapter);
	    	
    	} catch (Throwable t){
    		Log.e("Poltilbud",t.getMessage(),t);
    	}
    }
	
	private Runnable returnRes = new Runnable() {

        public void run() {
            if(m_products != null && m_products.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_products.size();i++)
                	m_adapter.add(m_products.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
	private String writeXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "rss");
			serializer.startTag("", "channel");
			
			for (Product msg: m_productsFromXML){
				serializer.startTag("", "item");
				
				serializer.startTag("", "title");
				serializer.text(msg.getTitle());
				serializer.endTag("", "title");
				
				serializer.startTag("", "link");
				serializer.text(msg.getLink().toExternalForm());
				serializer.endTag("", "link");
				
				serializer.startTag("", "description");
				serializer.text(msg.getDescription());
				serializer.endTag("", "description");	
				
				serializer.startTag("", "pubDate");
				serializer.text(msg.getDate());
				serializer.endTag("", "pubDate");	

				serializer.endTag("", "item");
			}
			serializer.endTag("", "channel");
			serializer.endTag("", "rss");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	private boolean isOnline() {
   	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
   	 if (cm.getActiveNetworkInfo() == null)
   		 return false;
   	 return cm.getActiveNetworkInfo().isConnectedOrConnecting();

   	}
	
	private void saveFileToFilesystem(String file){
		
		try { // catches IOException below	           
	           
	           // ##### Write a file to the disk #####
	           /* We have to use the openFileOutput()-method
	           * the ActivityContext provides, to
	           * protect your file from others and
	           * This is done for security-reasons.
	           * We chose MODE_WORLD_READABLE, because
	           * we have nothing to hide in our file */
	           FileOutputStream fOut = openFileOutput(PoltilbudEnums.FILENAME,
	           MODE_WORLD_READABLE);
	           OutputStreamWriter osw = new OutputStreamWriter(fOut);
	           
	           // Write the string to the file
	           osw.write(file);
	           /* ensure that everything is
	           * really written out and close */
	           osw.flush();
	           osw.close();
	           // ##### Read the file back in #####
	           
	           
	           // WOHOO lets Celebrate =)
	           Log.i("Poltilbud", "File written ok");
	           
	       } catch (IOException ioe) {
	           ioe.printStackTrace();
	       }
		
	}
	
	private InputStream getLocalInputStream(){
		
		readXMLFile();
		try {
		    return openFileInput(PoltilbudEnums.FILENAME);		    
		} catch (java.io.FileNotFoundException e) {
			Log.i("Poltilbud", "File do not exists!");
			e.printStackTrace();
			return null;
		}
	}
	
	private String readXMLFile(){
		String offerXMLFile = new String();

		try {
		    // open the file for reading
		    InputStream in = openFileInput(offerXMLFilename);
		 
		    // if file the available for reading
		    if (in != null) {
		      // prepare the file for reading
		      InputStreamReader input = new InputStreamReader(in);
		      BufferedReader buffreader = new BufferedReader(input);
		      String str;
		      StringBuffer stringBuffer = new StringBuffer();
		      // read every line of the file into the line-variable, on line at the time
		      while (( str = buffreader.readLine()) != null) {
		        stringBuffer.append(str + "\n");
		      }
		      in.close();
		      offerXMLFile = stringBuffer.toString();
		      Log.i("Poltilbud", "XML file from local: " + offerXMLFile);
		    }
		    
		    Log.i("Poltilbud", "File read");
		    
		} catch (java.io.FileNotFoundException e) {
		  Log.i("Poltilbud", "File read");
		  e.printStackTrace();		 
	  	} catch (IOException e) {
		  Log.i("Poltilbud", "File read");
		  e.printStackTrace();		  
		}

		return offerXMLFile;
	}
	
	private boolean offerFileExists(){
		try {
		    openFileInput(offerXMLFilename);
		} catch (java.io.FileNotFoundException e) {
		  return false;
		}
	  	return true;
	}
	
}