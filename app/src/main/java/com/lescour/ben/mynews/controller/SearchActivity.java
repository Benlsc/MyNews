package com.lescour.ben.mynews.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lescour.ben.mynews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by benja on 04/02/2019.
 */
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_toolbar) Toolbar toolbar;
    @BindView(R.id.search_query_term) EditText editText;
    @BindView(R.id.select_begin_date) TextView selectBeginDate;
    @BindView(R.id.select_end_date) TextView selectEndDate;
    @BindView(R.id.checkbox_arts) CheckBox checkBoxArts;
    @BindView(R.id.checkbox_business) CheckBox checkBoxBusiness;
    @BindView(R.id.checkbox_entrepreneurs) CheckBox checkBoxEntrepreneurs;
    @BindView(R.id.checkbox_politics) CheckBox checkBoxPolitics;
    @BindView(R.id.checkbox_sports) CheckBox checkBoxSports;
    @BindView(R.id.checkbox_travel) CheckBox checkBoxTravel;
    @BindView(R.id.launch_search_button) Button launchSearchButton;
    private DatePickerDialog.OnDateSetListener beginDateSetListener, endDateSetListener;
    private Calendar calendar;
    private SimpleDateFormat visualFormat, urlFormat;
    private int year, month, day;
    private DatePickerDialog datePickerDialog;
    private String beginDateToShow, endDateToShow;
    private String beginDateForUrl, endDateForUrl;
    private String arts, business, entrepreneurs, politics, sports, travel;
    private StringBuilder compactCategoriesBuilder;
    private String filter_query;
    public static final String BUNDLE_EXTRA_QUERY = "BUNDLE_EXTRA_QUERY";
    public static final String BUNDLE_EXTRA_BEGIN_DATE = "BUNDLE_EXTRA_BEGIN_DATE";
    public static final String BUNDLE_EXTRA_END_DATE = "BUNDLE_EXTRA_END_DATE";
    public static final String BUNDLE_EXTRA_FILTER_QUERY = "BUNDLE_EXTRA_FILTER_QUERY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        this.configureToolbar();
        this.initCalendar();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    launchPersonaliseSearch();
                    return true;
                }
                return false;
            }
        });

        beginDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                beginDateToShow = visualFormat.format(calendar.getTime());
                checkIfBeginDateIsCorrect(beginDateToShow);
            }
        };

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                endDateToShow = visualFormat.format(calendar.getTime());
                checkIfEndDateIsCorrect(endDateToShow);
            }
        };
    }

    private void initCalendar() {
        calendar = Calendar.getInstance();
        urlFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        visualFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        visualFormat.setCalendar(calendar);
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);
    }

//////////    Toolbar   //////////
    private void configureToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//////////    BeginDate   //////////

    @OnClick(R.id.select_begin_date)
    public void beginDate() {
        initCalendar();
        initBeginDatePickerDialog();
    }

    private void initBeginDatePickerDialog() {
        datePickerDialog = new DatePickerDialog(
                SearchActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                beginDateSetListener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void checkIfBeginDateIsCorrect(String beginDateToShow){
        if (endDateToShow != null) {
            try {
                Date beginDate = visualFormat.parse(beginDateToShow);
                Date endDate = visualFormat.parse(endDateToShow);
                if (beginDate.before(endDate) || beginDate.equals(endDate)) {
                    showAndSaveBeginDateInUrlFormat(beginDateToShow);
                } else {
                    Toast.makeText(this, "Begin date can't be after end date.", Toast.LENGTH_LONG).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            showAndSaveBeginDateInUrlFormat(beginDateToShow);
        }
    }

    private void showAndSaveBeginDateInUrlFormat(String beginDateToShow) {
        selectBeginDate.setText(beginDateToShow);
        urlFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        urlFormat.setCalendar(calendar);
        beginDateForUrl = urlFormat.format(calendar.getTime());
    }

//////////    EndDate   //////////

    @OnClick(R.id.select_end_date)
    public void endDate() {
        initCalendar();
        initEndDatePickerDialog();
    }

    private void initEndDatePickerDialog() {
        datePickerDialog = new DatePickerDialog(
                SearchActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                endDateSetListener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void checkIfEndDateIsCorrect(String endDateToShow) {
        if (beginDateToShow != null) {
            try {
                Date beginDate = visualFormat.parse(beginDateToShow);
                Date endDate = visualFormat.parse(endDateToShow);
                if (beginDate.before(endDate) || beginDate.equals(endDate)) {
                    showAndSaveEndDateInUrlFormat(endDateToShow);
                } else {
                    Toast.makeText(this, "End date can't be before begin date.", Toast.LENGTH_LONG).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            showAndSaveEndDateInUrlFormat(endDateToShow);
        }
    }

    private void showAndSaveEndDateInUrlFormat(String endDateToShow) {
        selectEndDate.setText(endDateToShow);
        urlFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        urlFormat.setCalendar(calendar);
        endDateForUrl = urlFormat.format(calendar.getTime());
    }

//////////    CheckBox   //////////

    public void checkCheckBox(View checkBox) {
        switch (checkBox.getId()){
            case R.id.checkbox_arts :
                if (checkBoxArts.isChecked()) {
                    arts = "\"arts\"";
                } else {
                    arts = null;
                }
                break;
            case R.id.checkbox_business :
                if (checkBoxBusiness.isChecked()) {
                    business = "\"business\"";
                } else {
                    business = null;
                }
                break;
            case R.id.checkbox_entrepreneurs :
                if (checkBoxEntrepreneurs.isChecked()) {
                    entrepreneurs = "\"entrepreneurs\"";
                } else {
                    entrepreneurs = null;
                }
                break;
            case R.id.checkbox_politics :
                if (checkBoxPolitics.isChecked()) {
                    politics = "\"politics\"";
                } else {
                    politics = null;
                }
                break;
            case R.id.checkbox_sports :
                if (checkBoxSports.isChecked()) {
                    sports = "\"sports\"";
                } else {
                    sports = null;
                }
                break;
            case R.id.checkbox_travel :
                if (checkBoxTravel.isChecked()) {
                    travel = "\"travel\"";
                } else {
                    travel = null;
                }
                break;
        }
    }

//////////    Search   //////////

    @OnClick(R.id.launch_search_button)
    public void launchPersonaliseSearch() {
        String query = editText.getText().toString();
        this.buildCompactCategoriesBuilder();
        if (!query.equals("") && compactCategoriesBuilder.length() > 0) {
            filter_query = "news_desk:(" + compactCategoriesBuilder + ")";
            Intent customActivity = new Intent(this, CustomActivity.class);
            customActivity.putExtra(BUNDLE_EXTRA_BEGIN_DATE, beginDateForUrl);
            customActivity.putExtra(BUNDLE_EXTRA_END_DATE, endDateForUrl);
            customActivity.putExtra(BUNDLE_EXTRA_QUERY, query);
            customActivity.putExtra(BUNDLE_EXTRA_FILTER_QUERY, filter_query);
            this.startActivity(customActivity);
        }
        else if (query.equals("") &&  compactCategoriesBuilder.length() > 0) {
            Toast.makeText(this, "Please enter a query.", Toast.LENGTH_LONG).show();
        }
        else if (!query.equals("") && compactCategoriesBuilder.length() == 0) {
            Toast.makeText(this, "Please choose a categories.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please enter a query and choose a categories.", Toast.LENGTH_LONG).show();
        }
    }

    private void buildCompactCategoriesBuilder() {
        compactCategoriesBuilder = new StringBuilder();
        if (arts != null || business != null || entrepreneurs != null || politics != null || sports != null || travel != null) {
            if (arts != null) {
                compactCategoriesBuilder.append(arts);
            }
            if (business != null) {
                compactCategoriesBuilder.append(business);
            }
            if (entrepreneurs != null) {
                compactCategoriesBuilder.append(entrepreneurs);
            }
            if (politics != null) {
                compactCategoriesBuilder.append(politics);
            }
            if (sports != null) {
                compactCategoriesBuilder.append(sports);
            }
            if (travel != null) {
                compactCategoriesBuilder.append(travel);
            }
        }
    }
}
