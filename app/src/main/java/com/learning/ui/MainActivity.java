package com.learning.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.learning.DragBubble.DragBubbleActivity;
import com.learning.Path.PathActivity;
import com.learning.Path.PathMeasureActivity;
import com.learning.Path.SearchAnimActivity;
import com.learning.SplashView.SplashActivity;
import com.learning.XMode.GuaGuaLeActivity;
import com.learning.XMode.XModeView;
import com.learning.XMode.XfermodeEraserView;
import com.learning.XMode.XfermodesActivity;
import com.learning.canvas.CanvasTransformActivity;
import com.learning.colorFilter.ColorFilter;
import com.learning.colorFilter.ColorFilterActivity;
import com.learning.split.SplitActiviity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn_shader;
    private Button mBtn_xmode;
    private Button mBtn_guaguale;
    private Button mBtn_colorFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_shader = (Button) findViewById(R.id.btn_shader);
        mBtn_xmode = (Button) findViewById(R.id.btn_xmode);
        mBtn_guaguale = (Button) findViewById(R.id.btn_guaguale);
        mBtn_colorFilter = (Button) findViewById(R.id.btn_colorFilter);
//        setContentView(new GradientLayout(this));
//        setContentView(new XModeView(this));
//        setContentView(new XfermodeEraserView(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_shader:
                toActivity(ShaderActivity.class);
                break;
            case R.id.btn_xmode:
                toActivity(XfermodesActivity.class);
                break;
            case R.id.btn_guaguale:
                toActivity(GuaGuaLeActivity.class);
                break;
            case R.id.btn_colorFilter:
                toActivity(ColorFilterActivity.class);
                break;
            case R.id.btn_canvasTrans:
                toActivity(CanvasTransformActivity.class);
                break;
            case R.id.btn_canvasBalls:
                toActivity(SplitActiviity.class);
                break;
            case R.id.btn_splashView:
                toActivity(SplashActivity.class);
                break;
            case R.id.btn_drag_bubble:
                toActivity(DragBubbleActivity.class);
                break;
            case R.id.btn_path_view:
                toActivity(PathActivity.class);
                break;
            case R.id.btn_pathmeasure_fly:
                toActivity(PathMeasureActivity.class);
                break;
            case R.id.btn_pathmeasure_search:
                toActivity(SearchAnimActivity.class);
                break;
        }
    }

    public void toActivity(Class<?> cls){
        Intent intent = new Intent(MainActivity.this,cls);
        startActivity(intent);
    }
}