package com.hadean777.zpcalc;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationView();
        initButton();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(navigationView);
            }
        });
    }

    private void initNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        useMenu();
    }

    private void useMenu() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.drawer_menu);
        navigationView.bringToFront();


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        onMenuItemSelected(menuItem);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private boolean onMenuItemSelected(MenuItem item) {
        return true;
    }

    private void initButton() {
        final Button button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final TextView dirtyView = (TextView) findViewById(R.id.textview_dirty);
                final TextView cleanView = (TextView) findViewById(R.id.textview_clean);
                final EditText tariffView = (EditText) findViewById(R.id.input_tariff);
                Editable ed = tariffView.getText();
                String tariffStr = "";
                if (ed != null) {
                    tariffStr = ed.toString();
                }
                final EditText totalView = (EditText) findViewById(R.id.input_total);
                ed = totalView.getText();
                String totalStr = "";
                if (ed != null) {
                    totalStr = ed.toString();
                }
                final EditText overtimeView = (EditText) findViewById(R.id.input_overtime);
                ed = overtimeView.getText();
                String overtimeStr = "";
                if (ed != null) {
                    overtimeStr = ed.toString();
                }
                final EditText nightView = (EditText) findViewById(R.id.input_night);
                ed = nightView.getText();
                String nightStr = "";
                if (ed != null) {
                    nightStr = ed.toString();
                }

                try {

                    double tariff = 0;
                    double total = 0;
                    double overtime = 0;
                    double night = 0;

                    if (!"".equalsIgnoreCase(tariffStr)) {
                        tariff = Double.parseDouble(tariffStr);
                    }
                    if (!"".equalsIgnoreCase(totalStr)) {
                        total = Double.parseDouble(totalStr);
                    }
                    if (!"".equalsIgnoreCase(overtimeStr)) {
                        overtime = Double.parseDouble(overtimeStr);
                    }
                    if (!"".equalsIgnoreCase(nightStr)) {
                        night = Double.parseDouble(nightStr);
                    }


                    Double dirty = total * tariff * 0.3 + overtime * tariff + night * tariff * 0.4 + total * tariff;

                    Double clean = dirty * 0.805;


                    dirtyView.setText("Грязные: " + dirty.toString());
                    cleanView.setText("Чистые: " + clean.toString());

                } catch (NumberFormatException e) {
                    dirtyView.setText("Ошибка ввода данных");
                }
            }
        });
    }
}
