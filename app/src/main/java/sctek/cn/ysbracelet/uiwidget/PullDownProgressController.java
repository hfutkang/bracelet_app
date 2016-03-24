package sctek.cn.ysbracelet.uiwidget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import sctek.cn.ysbracelet.R;

/**
 * Created by kang on 16-3-4.
 */
public class PullDownProgressController {

    private final static String TAG = PullDownProgressController.class.getSimpleName();

    private ProgressBar left;
    private ProgressBar right;
    private LinearLayout progressBarsLo;

    private boolean setup = false;
    private int accumulatedDeltaY = 0;

    ObjectAnimator leftFade, rightFade;

    public PullDownProgressController(LinearLayout barsLo) {
        progressBarsLo = barsLo;
        left = (ProgressBar)progressBarsLo.findViewById(R.id.left_pb);
        right = (ProgressBar)progressBarsLo.findViewById(R.id.right_pb);

        LayerDrawable layerDrawable = (LayerDrawable) left.getProgressDrawable();
        layerDrawable.setDrawableByLayerId(android.R.id.background, new ColorDrawable(Color.TRANSPARENT));

        layerDrawable = (LayerDrawable) right.getProgressDrawable();
        layerDrawable.setDrawableByLayerId(android.R.id.background, new ColorDrawable(Color.TRANSPARENT));
    }

    public void updateProgress(int deltaY) {

        showSwipeDown();

        accumulatedDeltaY += -deltaY;
        left.setProgress(accumulatedDeltaY);
        right.setProgress(accumulatedDeltaY);
        if (accumulatedDeltaY == 0)
            hideSwipeDown();

    }

    public void showSwipeDown()
    {
        if (!setup) {
            if (leftFade != null) {
                leftFade.cancel();
                leftFade = null;
            }
            if (rightFade != null) {
                rightFade.cancel();
                rightFade = null;
            }
            progressBarsLo.setVisibility(View.VISIBLE);
            accumulatedDeltaY = 0;
            setup = true;
        }
    }

    public void hideSwipeDown()
    {
        leftFade = ObjectAnimator.ofInt (left, "progress", left.getProgress(), 0);
        leftFade.setDuration (250);
        leftFade.start ();
        rightFade = ObjectAnimator.ofInt (right, "progress", right.getProgress(), 0);
        rightFade.setDuration (250);
        rightFade.start ();
        rightFade.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                progressBarsLo.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
        setup = false;
    }
}
