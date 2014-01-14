package de.quaddyservices.extintor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

	private Runnable runnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(getClass().getName(), "onCreate...");

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_fullscreen);

		// final View controlsView =
		// findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (gedruecktesAuto != null) {
					gedruecktesAuto.setBackgroundColor(schwarz);
					gedruecktesAuto = null;
				}
			}
		});

		onCreateNightLight();

		for (final View tempFeuer : feuer) {

			tempFeuer.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					if (gedruecktesAuto != null) {
						// feuer gelöscht
						gedruecktesAuto.setBackgroundColor(schwarz);
						gedruecktesAuto = null;
						if (brennt.contains(tempFeuer)) {
							tempFeuer.setBackgroundColor(schwarz);
							brennt.remove(tempFeuer);
						}
					}
				}
			});
		}
		feuerAnzuenden();
		final View tempAuto = findViewById(R.id.auto);

		tempAuto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tempAuto.setBackgroundColor(blau);
				gedruecktesAuto = tempAuto;
			}
		});
	}

	private void feuerAnzuenden() {
		int tempRandom = (int) Math.round(Math.random() * feuer.size());
		if (tempRandom < feuer.size()) {
			View tempFeuer = feuer.get(tempRandom);
			if (!brennt.contains(tempFeuer)) {
				tempFeuer.setBackgroundColor(rot);
				brennt.add(tempFeuer);
				Toast.makeText(getActivity(), "Es brennt...", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	private View gedruecktesAuto = null;
	private int schwarz = 0xff000000;
	private int gruen = 0xff00ff00;
	private int blau = 0xff0000ff;
	private int rot = 0xffff0000;

	private int naechstesFeuer = 3000;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(getClass().getName(), "touch " + event);
		doFinish();
		return super.onTouchEvent(event);
	}

	private void doFinish() {
		naechstesFeuer = 3000;
		runnable = null;
		Log.i(getClass().getName(), "doFinish:" + this);
	}

	@Override
	public void onBackPressed() {
		Log.i(getClass().getName(), "onBackPressed");
		super.onBackPressed();
		doFinish();
	}

	@Override
	protected void onPause() {
		Log.i(getClass().getName(), "onBackPressed");
		super.onPause();
		doFinish();
	}

	private void onCreateNightLight() {
		if (feuer.size() == 0) {
			feuer.add(findViewById(R.id.feuer1));
			feuer.add(findViewById(R.id.feuer2));
			feuer.add(findViewById(R.id.feuer3));
			feuer.add(findViewById(R.id.feuer4));
			feuer.add(findViewById(R.id.feuer5));
			feuer.add(findViewById(R.id.feuer6));
			feuer.add(findViewById(R.id.feuer7));
			feuer.add(findViewById(R.id.feuer8));
		}
		Log.i(getClass().getName(), "onCreateNightLight:" + this);
	}

	@Override
	protected void onStart() {
		Log.i(getClass().getName(), "onStart:" + this);
		super.onStart();
		if (runnable == null) {
			runnable = new Runnable() {
				public void run() {
					Log.d(getClass().getName(), "timer");
					startTimer();
				}
			};
		}
		startTimer();
	}

	@Override
	protected void onStop() {
		Log.i(getClass().getName(), "onStop:" + this);
		super.onStop();
		runnable = null;
	}

	int offsetX = 1;
	int countX = 0;
	private List<View> feuer = new ArrayList<View>();
	private Set<View> brennt = new HashSet<View>();

	int countColor = 0;
	int offsetColor = 1;

	private void startTimer() {

		final View tempAuto = findViewById(R.id.auto);

		FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) tempAuto
				.getLayoutParams();

		layoutParams.leftMargin = layoutParams.leftMargin + 1;

		final View contentView = findViewById(R.id.fullscreen_content);
		if (layoutParams.leftMargin > contentView.getWidth() - 30) {
			layoutParams.leftMargin = 10;
		}

		tempAuto.setLayoutParams(layoutParams);

		if (runnable != null) {
			final Handler handler = new Handler();
			handler.postDelayed(runnable, naechstesFeuer);
		}

		if (naechstesFeuer > 400) {
			naechstesFeuer = naechstesFeuer - 200;
		}

		feuerAnzuenden();
	}

	private Context getActivity() {
		return this;
	}

	@Override
	public void onDetachedFromWindow() {
		doFinish();
		Log.i(getClass().getName(), "onDetachedFromWindow");
		super.onDetachedFromWindow();
	}

	protected void onDestroy() {
		runnable = null;
		Log.i(getClass().getName(), "onDestroy");
		super.onDestroy();
	};

}
