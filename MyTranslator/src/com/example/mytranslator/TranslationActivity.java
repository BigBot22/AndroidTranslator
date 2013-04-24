
package com.example.mytranslator;

//import com.example.mynetworking.UrlSearcher;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.example.mytranslator.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TranslationActivity extends Activity {

	
	boolean downloadingDone;
	ImageView img;
	Bitmap[] bitMapArr ;
	URL[] imageURL;

	Integer[] imageIDs = { 
			R.drawable.ic_gallery,
			R.drawable.ic_gallery,
			R.drawable.ic_gallery,
			R.drawable.ic_gallery
	};
	
	

	///////////////////////////////////////////////////  Gallery //////////////////////////////
	public class ImageAdapter extends BaseAdapter{
		Context context;
		int itemBackground;
		
		public ImageAdapter(Context c){
			context = c;
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			itemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}
		
		public int getCount(){
			return bitMapArr.length;
		}
		
		public Object getItem(int position){
			return position;
		}
		
		public long getItemId(int position){
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			ImageView imageView;
			if (convertView == null){
				imageView = new ImageView(context);
				//imageView.setImageResource(imageIDs[position]);
				imageView.setImageBitmap(bitMapArr[position]);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
			}
			else{
				imageView = (ImageView) convertView;
			}
			imageView.setBackgroundColor(itemBackground);
			return imageView;
		}
	}
	
	////////////////////////////////////////////////// Networking /////////////////////////////
	
	
	Bitmap bitMapTemp;
	
	public InputStream OpenHttpConnection (URL urlString) throws IOException
	{
		InputStream in = null;
		int response = -1;
		
		URL url = urlString;
		URLConnection conn = url.openConnection();
		
		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");
		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK){
				in = httpConn.getInputStream();
			}
		}
		catch (Exception ex)
		{
			Log.d("Networking",ex.getLocalizedMessage());
			throw new IOException("Error connecting");
		}
		return in;
	} 
	
	private Bitmap DownloadImage(URL Url)
	{
		Bitmap bitmap = null;
		InputStream in = null;
		try{
			in = OpenHttpConnection(Url);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
			
		} catch (IOException e1) {
			Log.d("NetworkingActivity", e1.getLocalizedMessage());
		}
		return bitmap;
	}
	
	private class DownloadImageTask extends AsyncTask<URL, Void, Bitmap[]>{
		
		protected Bitmap[] doInBackground(URL... urls){
			Log.d("MyApp","background begin");
			
			for(int i = 0; i < urls.length; i++){
			    bitMapArr[i] = DownloadImage(urls[i]);
			    Log.d("MyApp","background " + i);
			}
			downloadingDone = true;
			Log.d("MyApp","background end");
			
			return bitMapArr;
		}
		
		protected void onPostExecute(Bitmap[] result){
			Log.d("MyApp","post");
		}
		protected void onProgressUpdate(Bitmap... Bitmap){
			
		}
	}
	
	
	
	public void onClickBack(View v)
	{
	
		   startActivity(new Intent("com.example.mynetworking.MyNetworkingActivity"));     
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translating);
	    
	    TextView txtInfo = (TextView)findViewById(R.id.checkedTextView1);
	    String word = getIntent().getExtras().getString("wordToTranslate");
	
	    txtInfo.setText(Translater.translate(word));
	    
	    Log.d("MyApp", "Begine to finde URLs:");
		
	    imageURL = new URL[10];
		bitMapArr = new Bitmap[imageURL.length];
		
	    for(int i = 0; i < imageURL.length; ++i){
	    	bitMapArr[i] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_gallery);
	    }
        
        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new OnItemClickListener(){
        	
       	    public void onItemClick(AdapterView parent, View v, int position, long id){
       	    	
          	   //  Toast.makeText(getBaseContext(), "pic" + (position + 1) + "selected", Toast.LENGTH_SHORT).show();
          	     ImageView imageView = (ImageView) findViewById(R.id.img);
          	     imageView.setImageBitmap(bitMapArr[position]);
        	}
       });
         
        
		imageURL = UrlSearcher.getURLs(word);
       
        new DownloadImageTask().execute(imageURL);
      
        Log.d("MyApp","waiting " + downloadingDone);
	   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_second, menu);
        return true;
    }
}




