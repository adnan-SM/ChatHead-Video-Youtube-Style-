package com.example.chathead;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class ChatHeadService extends Service implements OnPreparedListener,SurfaceHolder.Callback{

	  private WindowManager windowManager;
	 
	  WindowManager.LayoutParams params;
	  WindowManager.LayoutParams params1;
	  public MediaPlayer mp ;
	  SurfaceView sView;
	  SurfaceHolder holder;

	  private static final int SWIPE_MIN_DISTANCE = 120;
	  private static final int SWIPE_MAX_OFF_PATH = 250;
	  private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	  private GestureDetector gestureDetector;
	  View.OnTouchListener gestureListener;
	  Animation swipeRight;

	  class MyGestureDetector extends SimpleOnGestureListener implements OnTouchListener{
		  
	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	        	
	            try {
	                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
	                    return false;
	                // right to left swipe
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                    Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
	                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	                    Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
	                    swipeRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.swipe_right);
	                    sView.startAnimation(swipeRight);
	                    stopSelf();
	                    mp.stop();

	                }
	            } catch (Exception e) {
	                // nothing
	            }
	            return false;
	        }

	            public boolean onDown(MotionEvent e) {
	              return true;
	        }

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}

				       
	    }
	
	  
	  @Override public IBinder onBind(Intent intent) {
	    // Not used
	    return null;
	  }
	  
	  @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String url = "rtsp://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp";
		
		String tempUrl = intent.getStringExtra("url");
		if(tempUrl!= null && tempUrl.trim().length()>0){
			url = tempUrl;
			Toast.makeText(getApplicationContext(), "got intent/", 500).show();
		}
		
		Log.d("URL", url);
		mp = new MediaPlayer();
		
		
		try {
			mp.setDataSource(url);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mp.setOnPreparedListener(this);
		try {
			mp.prepareAsync();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	  @Override 
	  public void onCreate() {
	    super.onCreate();

	    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	    

	   params = new WindowManager.LayoutParams(
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.WRAP_CONTENT,
	        WindowManager.LayoutParams.TYPE_PHONE,
	        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
	        PixelFormat.TRANSLUCENT);

	    params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
	    params.x = 15;
	    params.y = 100;
	    
	    params1 = new WindowManager.LayoutParams(
		        WindowManager.LayoutParams.WRAP_CONTENT,
		        WindowManager.LayoutParams.WRAP_CONTENT,
		        WindowManager.LayoutParams.TYPE_PHONE,
		        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		        PixelFormat.TRANSLUCENT);

		    params1.gravity = Gravity.TOP | Gravity.RIGHT;
		    params1.x = 0;
		    params1.y = 100;

		   gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        gestureListener = new View.OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	                return gestureDetector.onTouchEvent(event);
	            }
	        };
	        
	   
	    if (sView != null) {
	    	sView.setKeepScreenOn(true);
			sView.setOnTouchListener(gestureListener);
	    	windowManager.addView(sView, params);
	    	Log.d("Video","came in Video view");
	    	Toast.makeText(getApplicationContext(), "Not null", 500).show();
	    } else {
	    	sView = new SurfaceView(getApplicationContext());
	    	sView.setKeepScreenOn(true);
	        sView.setOnTouchListener(gestureListener);
			holder = sView.getHolder();
			holder.addCallback(this);
			holder.setFixedSize(500, 400);
			windowManager.addView(sView, params);
			
	    }
	    
	    
	    
	  }
	  
	  /*  
	    sView.setOnTouchListener(new View.OnTouchListener() {
	    	  private int initialX;
	    	  private int initialY;
	    	  private float initialTouchX;
	    	  private float initialTouchY;

	    	  @Override 
	    	  public boolean onTouch(View v, MotionEvent event) {
	    	    switch (event.getAction()) {
	    	      case MotionEvent.ACTION_DOWN:
	    	        initialX = params.x;
	    	        initialY = params.y;
	    	        initialTouchX = event.getRawX();
	    	        initialTouchY = event.getRawY();
	    	        
	    	        windowManager.addView(chatHead, params);
	    	        return true;
	    	      case MotionEvent.ACTION_UP:
	    	    	  
	    	        return true;
	    	      case MotionEvent.ACTION_MOVE:
	    	        params.x = initialX + (int) (event.getRawX() - initialTouchX);
	    	        params.y = initialY + (int) (event.getRawY() - initialTouchY);
	    	      //  windowManager.updateViewLayout(sView, params);
	    	        return true;
	    	    }
	    	    return false;
	    	  }
	    	});
		
	  }*/

	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	    stopSelf();
	    
	    windowManager.removeView(sView);
	    mp.stop();
	    mp.release();
	    
	  }
	  
	  

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mp.setDisplay(holder);
	   
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
