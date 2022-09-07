package com.example.testapplication.ui.home;

import static com.example.testapplication.data.network.network_response.Response.Type.SUCCESS;
import static com.example.testapplication.data.network.network_response.Response.Type.ERROR;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.LiveData;
import com.example.testapplication.R;
import com.example.testapplication.data.db.CityWeatherInfoDao;
import com.example.testapplication.data.db.models.CityWeatherInfo;
import com.example.testapplication.data.network.NetworkManager;
import com.example.testapplication.data.network.network_response.CityWeatherInfoResponse;
import com.example.testapplication.data.network.network_response.ErrorResponse;
import com.example.testapplication.data.network.network_response.Response;
import com.example.testapplication.repository.CityWeatherRepository;
import com.example.testapplication.ui.app_launcher.AppLauncherActivity;
import com.example.testapplication.ui.base.BaseActivity;
import com.example.testapplication.ui.base.BasePresenter;

public class HomeActivity extends  BaseActivity implements HomeView{


    View viewBatteryLevel;
    AppCompatTextView tvBatteryPercent;
    CardView cvCountryDetails;
    AppCompatButton btnAppLauncher;
    AppCompatImageButton btnCityWeatherCard;

    AppCompatTextView tvCountryName;
    AppCompatTextView tvCityName;
    AppCompatTextView tvDescription;
    AppCompatTextView tvTemperature;
    ContentLoadingProgressBar clbProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        initView();
        initListeners();
        setPresenter(new HomePresenter(context, this, new CityWeatherRepository(NetworkManager.getInstance(context), new CityWeatherInfoDao(context), context)));
        onViewLoaded();

    }

    @Override
    protected void setPresenter(BasePresenter _presenter){
        super.setPresenter(_presenter);
    }

    @Override
    protected void initView(){
        super.initView();
        viewBatteryLevel = findViewById(R.id.v_bl);
        tvBatteryPercent = findViewById(R.id.tv_battery_percent);
        cvCountryDetails = findViewById(R.id.cv_country_detail);
        cvCountryDetails.setBackgroundResource(R.drawable.bg_blue_gradient);
        btnAppLauncher = findViewById(R.id.btn_app_launcher);
        btnCityWeatherCard = findViewById(R.id.btn_city_weather_card);
        tvCountryName = findViewById(R.id.tv_country_name);
        tvCityName = findViewById(R.id.tv_city);
        tvDescription = findViewById(R.id.tv_desc);
        tvTemperature = findViewById(R.id.tv_temperature);
        clbProgressBar = findViewById(R.id.progress_bar);


    }

    @Override
    protected void initListeners(){
        btnAppLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomePresenter)presenter).AppLauncherButtonClicked();
            }
        });

        btnCityWeatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                ((HomePresenter)presenter).cityWeatherCardButtonClicked();
            }
        });
    }

    @Override
    public void onViewLoaded(){
        super.onViewLoaded();
        presenter.onViewLoaded();
    }

    @Override
    public void showBatteryLevel(int level) {
        ViewGroup.LayoutParams params = viewBatteryLevel.getLayoutParams();
        params.height = level;
        viewBatteryLevel.setLayoutParams(params);
        viewBatteryLevel.invalidate();
    }

    @Override
    public void observeLifeCycle(BasePresenter presenter) {
        getLifecycle().addObserver(presenter);
    }

    @Override
    public void setBatteryPercent(String text) {
        tvBatteryPercent.setText(text);
    }

    @Override
    public void moveToAppLauncherScreen() {
        startActivity(new Intent(context, AppLauncherActivity.class));
    }

    @Override
    public void updateCityWeatherCard(LiveData<Response> responseLiveData) {
        responseLiveData.observe(this, networkResponse -> {
            hideProgressBar();
            switch (networkResponse.getType()){
                case SUCCESS:
                    CityWeatherInfoResponse response = (CityWeatherInfoResponse)networkResponse;
                    startAnimation(tvCityName);
                    startAnimation(tvCountryName);
                    startAnimation(tvDescription);
                    startAnimation(tvTemperature);
                    tvCityName.setText(response.getCity());
                    tvCountryName.setText(response.getCountry());
                    tvDescription.setText(response.getDescription());
                    tvTemperature.setText(String.valueOf(response.getTemperature()));

                    break;

                case ERROR :
                    ErrorResponse errorResponse = (ErrorResponse) networkResponse;
                    Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }

        });
    }

    @Override
    public void setInfoOnCardView(CityWeatherInfo cityWeatherInfo) {
        tvCityName.setText(cityWeatherInfo.getCityName());
        tvCountryName.setText(cityWeatherInfo.getCountry());
        tvDescription.setText(cityWeatherInfo.getCurrentDescription());
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void startAnimation(View view){
        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.fade_out);
        animator.setTarget(view);
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    public void showProgressBar(){
        clbProgressBar.show();
    }

    @Override
    public void hideProgressBar(){
        clbProgressBar.hide();
    }


}
