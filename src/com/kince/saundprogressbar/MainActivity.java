package com.kince.saundprogressbar;

import com.kince.saundprogressbar.widget.SaundProgressBar;
import com.kince.saundprogressbar.widget.SideHpProgressBar;
import com.kince.saundprogressbar.widget.SideHpProgressBar.Formatter;

import android.view.View;
import android.widget.ProgressBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {

	private SaundProgressBar mPbar;
	private SideHpProgressBar mHpbar;
	
	ProgressBar pb;
    private int progress=0;
    private Message message;
    private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int p=msg.what;
			mPbar.setProgress(p);
			mHpbar.setProgress(p);
			pb.setProgress(p);
		}
    	
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Drawable indicator = getResources().getDrawable(R.color.transparent);
//				R.drawable.white_progress_indicator);
		Rect bounds = new Rect(0, 0, indicator.getIntrinsicWidth() + 65,
				indicator.getIntrinsicHeight()+70);
		indicator.setBounds(bounds);

		mPbar = (SaundProgressBar) this.findViewById(R.id.regularprogressbar);
		initProgressBar(mPbar);
		mPbar.setProgressIndicator(indicator);
		
		mHpbar = (SideHpProgressBar) this.findViewById(R.id.hprprogressbar);
		initProgressBar(mHpbar);
		mHpbar.setProgressIndicator(indicator);
		mHpbar.setBarHeight(150);
		mHpbar.setTextColor(Color.BLACK);
		
		Formatter textFormatter = new Formatter() {
			
			@Override
			public String getText(int progress) {
				
				return progress + " / 100"  ;
			}
		};
		mHpbar.setTextFormatter(textFormatter);
		
		
		pb = (ProgressBar)findViewById(R.id.pb);
		initProgressBar(pb);
		new Thread(runnable).start();
	}
	
	
	protected void initProgressBar(ProgressBar bar){
		bar.setMax(100);
		


		
		bar.setProgress(0);
		bar.setVisibility(View.VISIBLE);
	}

	Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			message=handler.obtainMessage();
			// TODO Auto-generated method stub
			try {
				for (int i = 1; i <= 101; i++) {
					int x=progress++;
					message.what=x;
					handler.sendEmptyMessage(message.what);
					Thread.sleep(1000);
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
