package it.edu.fermimn.example.simulazioneinvestimenti;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.slider.Slider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final DecimalFormat decimalFormatter = new DecimalFormat("##.##");
    private int capitaleValue;
    private int tempoValue;
    private float tassoInteresseValue;
    private boolean isSyncEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSlidersListener();
        setupEditTextsListener();

        TextView textViewLabelCapitale = findViewById(R.id.textViewLabelCapitale);
        TextView textViewLabelUtile = findViewById(R.id.textViewLabelUtile);
        TextView textViewLabelRendimentoTotale = findViewById(R.id.textViewLabelRendimentoTotale);

        TextView textViewCapitale = findViewById(R.id.textViewCapitaleInvestito);
        TextView textViewUtile = findViewById(R.id.textViewUtile);
        TextView textViewRendimentoTotale = findViewById(R.id.textViewRendimentoTotale);

        LineChart lineChart = findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entries, "Capitale");

        Button btnCalcola = findViewById(R.id.btnCalcola);
        btnCalcola.setOnClickListener(view -> {
            double rendimentoTotale = capitaleValue * Math.pow(1 + tassoInteresseValue, tempoValue);

            textViewCapitale.setText(String.valueOf(capitaleValue));
            textViewCapitale.setVisibility(View.VISIBLE);
            textViewLabelCapitale.setVisibility(View.VISIBLE);

            textViewUtile.setText(decimalFormatter.format(rendimentoTotale - capitaleValue));
            textViewUtile.setVisibility(View.VISIBLE);
            textViewLabelUtile.setVisibility(View.VISIBLE);

            textViewRendimentoTotale.setText(decimalFormatter.format(rendimentoTotale));
            textViewRendimentoTotale.setVisibility(View.VISIBLE);
            textViewLabelRendimentoTotale.setVisibility(View.VISIBLE);

            entries.clear();

            for (int i = 0; i <= tempoValue; i++) {
                entries.add(new Entry(i, (float) (capitaleValue * Math.pow(1 + tassoInteresseValue, i))));
            }

            lineChart.setData(new LineData(dataSet));
            lineChart.invalidate();

            lineChart.setVisibility(View.VISIBLE);
        });

    }

    private void setupSlidersListener() {
        ((Slider) findViewById(R.id.sliderCapitale)).addOnChangeListener((slider, value, fromUser) -> {
            if (isSyncEdit) {
                isSyncEdit = false;
                return;
            }

            capitaleValue = (int) value;

            isSyncEdit = true;
            ((EditText) findViewById(R.id.editTextCapitale)).setText(String.valueOf(capitaleValue));
        });

        ((Slider) findViewById(R.id.sliderTempo)).addOnChangeListener((slider, value, fromUser) -> {
            if (isSyncEdit) {
                isSyncEdit = false;
                return;
            }

            tempoValue = (int) value;

            isSyncEdit = true;
            ((EditText) findViewById(R.id.editTextTempo)).setText(String.valueOf(tempoValue));
        });

        ((Slider) findViewById(R.id.sliderTassoInteresse)).addOnChangeListener((slider, value, fromUser) -> {
            if (isSyncEdit) {
                isSyncEdit = false;
                return;
            }

            tassoInteresseValue = value / 100;

            isSyncEdit = true;
            ((EditText) findViewById(R.id.editTextTassoInteresse)).setText(decimalFormatter.format(value));
        });
    }

    private void setupEditTextsListener() {
        ((EditText) findViewById(R.id.editTextCapitale)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) return;

                if (isSyncEdit) {
                    isSyncEdit = false;
                    return;
                }

                capitaleValue = Integer.parseInt(s.toString());

                isSyncEdit = true;
                ((Slider) findViewById(R.id.sliderCapitale)).setValue((capitaleValue >= 1 && capitaleValue <= 1000) ? capitaleValue : 1000);
            }
        });

        ((EditText) findViewById(R.id.editTextTempo)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) return;

                if (isSyncEdit) {
                    isSyncEdit = false;
                    return;
                }

                tempoValue = Integer.parseInt(s.toString());

                isSyncEdit = true;
                ((Slider) findViewById(R.id.sliderTempo)).setValue((tempoValue >= 1 && tempoValue <= 100) ? tempoValue : 100);
            }
        });


        ((EditText) findViewById(R.id.editTextTassoInteresse)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) return;

                if (isSyncEdit) {
                    isSyncEdit = false;
                    return;
                }

                tassoInteresseValue = Float.parseFloat(s.toString()) / 100;

                isSyncEdit = true;
                float temp = tassoInteresseValue * 100;
                ((Slider) findViewById(R.id.sliderTassoInteresse)).setValue((temp >= 1 && temp <= 10) ? temp : 10);
            }
        });
    }
}