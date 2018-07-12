package galapagos.calculatorx1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


public class ServiceFloating extends Service {

	private WindowManager mWindowManager;
	private ImageView mChatHead;
	private ImageView mBackButton;
	private CalculatorView mCalculatorView;
	private View mPopupView;

	private long lastPressTime;
	private boolean _enable = true;
	private boolean mHasDoubleClicked = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		mChatHead = new ImageView(this);

		mChatHead.setImageResource(R.drawable.ic_touch_cal);

		 if(prefs.getString("ICON", "floating2").equals("floating5")){
			 mChatHead.setImageResource(R.drawable.ic_touch_cal);
		}

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		mWindowManager.addView(mChatHead, params);

		initiatePopupWindow();

		try {
			mChatHead.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;

				@Override
                public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// Get current time in nano seconds.
						long pressTime = System.currentTimeMillis();

						// If double click...
						if (pressTime - lastPressTime <= 300) {
							ServiceFloating.this.stopSelf();
							mHasDoubleClicked = true;
						}
						else {     // If not double click....
							mHasDoubleClicked = false;
						}
						lastPressTime = pressTime; 
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_MOVE:
						paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
						mWindowManager.updateViewLayout(mChatHead, paramsF);
						break;
					}
					return false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

		mChatHead.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View arg0) {

				if (_enable) {
					mPopupView.setVisibility(View.VISIBLE);
				} else {
					mPopupView.setVisibility(View.GONE);
				}
				_enable = !_enable;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mChatHead != null) mWindowManager.removeView(mChatHead);
	}

	/**
	 * カルキュレータービューのボタンをクリックする
	 */
	public void onClickButton(View view) {
		mCalculatorView.onClickButton(view);
	}

	/**
	 * 戻りのイベントを実行すると、アプリの画面に戻るということだ
	 */
	public void onBackButton(){
		SharePreferenceObject.getInstance(this).saveDataFomula(mCalculatorView.mtxtFomula.getText().toString());
		SharePreferenceObject.getInstance(this).saveDataResult(mCalculatorView.mtxtResult.getText().toString());
		ServiceFloating.this.stopSelf();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		getApplicationContext().startActivity(intent);
	}

	/**
	 * ポップアップを初期する
	 */
	private void initiatePopupWindow() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				(int) (display.getWidth()/(1.7)),
				(int) (display.getHeight()/(1.7)),
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				,
				PixelFormat.RGB_888);
		final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mPopupView = inflater.inflate(R.layout.popup_view, null);
		mCalculatorView = (CalculatorView)mPopupView.findViewById(R.id.contain_layout);
		mBackButton = (ImageView) mPopupView.findViewById(R.id.btn_back);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				wm.removeView(mPopupView);
				onBackButton();
			}
		});
		wm.addView(mPopupView, params);
		mPopupView.setVisibility(View.GONE);
	}

}