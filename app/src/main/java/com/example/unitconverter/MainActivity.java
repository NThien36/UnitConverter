package com.example.unitconverter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText inputEditText, outputEditText;
    private Spinner spinnerFrom, spinnerTo;
    private Button btnConvert, btnSwap, btnHistory;
    private List<String> historyList;

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

        // Initialize the views
        inputEditText = findViewById(R.id.input);
        outputEditText = findViewById(R.id.output);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        btnConvert = findViewById(R.id.btnConvert);
        btnSwap = findViewById(R.id.btnSwap);
        btnHistory = findViewById(R.id.btnHistory);

        // Initialize history list
        historyList = new ArrayList<>();

        // Set an OnClickListener for the "CONVERT" button
        btnConvert.setOnClickListener(v -> performConversion());
        
        // Set an OnClickListener for the "SWAP" button
        btnSwap.setOnClickListener(v -> performSwap());

        // Set an OnClickListener for the "HISTORY" button
        btnHistory.setOnClickListener(v -> showHistory());
    }

    private void showHistory() {
        // Create a dialog to show history
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_history, null);
        dialogBuilder.setView(dialogView);

        // Find the close button in the dialog layout and set an OnClickListener
        Button btnClose = dialogView.findViewById(R.id.btnCloseDiaglog);

        // Find the ListView in the dialog layout and set up an ArrayAdapter
        ListView listView = dialogView.findViewById(R.id.historyListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        listView.setAdapter(adapter);

        // Build and show the dialog
        AlertDialog historyDialog = dialogBuilder.create();

        // Set the dialog's background to transparent
        historyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        historyDialog.show();

        // Set an OnClickListener for the close button
        btnClose.setOnClickListener(v -> historyDialog.dismiss());
    }

    private void performSwap() {
        // Swap selected units in spinners
        int fromUnitPosition = spinnerFrom.getSelectedItemPosition();
        int toUnitPosition = spinnerTo.getSelectedItemPosition();

        spinnerFrom.setSelection(toUnitPosition);
        spinnerTo.setSelection(fromUnitPosition);

        // Swap values between input and output fields
        String inputText = inputEditText.getText().toString();
        String outputText = outputEditText.getText().toString();

        inputEditText.setText(outputText);
        outputEditText.setText(inputText);

        // Store the swapped conversion history
        if (inputText != null && !inputText.isEmpty()){
            storeConversionHistory(outputText,
                    spinnerFrom.getSelectedItem().toString(),
                    spinnerTo.getSelectedItem().toString(),
                    Double.parseDouble(inputText));
        }
    }

    private void performConversion() {
        // Get the input value
        String inputValueStr = inputEditText.getText().toString();

        // Check if the input value is empty
        if (inputValueStr.isEmpty()) {
            inputEditText.setError("Please enter a value");
            return;
        }

        double inputValue = Double.parseDouble(inputValueStr);
        String inputValueAsString = String.valueOf(inputValue);

        // Check if the input value is positive
        if (inputValue <= 0) {
            inputEditText.setError("Please enter a positive value");
            return;
        }

        // Set double value for input
        inputEditText.setText(inputValueAsString);

        // Get selected units from the spinners
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();

        // Return if fromUnit and toUnit are the same
        if (fromUnit.equals(toUnit)) {
            outputEditText.setText(inputValueAsString);
            return;
        }

        try {
            // Use ConversionHelper to convert the value
            double result = ConversionHelper.convert(fromUnit, toUnit, inputValue);
            outputEditText.setText(String.valueOf(result));

            // Store the conversion history
            storeConversionHistory(inputValueAsString, fromUnit, toUnit, result);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Conversion not supported for the selected units", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeConversionHistory(String inputValue, String fromUnit, String toUnit, double result) {
        // Add the conversion result to history as a formatted string
        String historyEntry = inputValue + " " + fromUnit + " = " + result + " " + toUnit;
        // Add the conversion result to history only if it does not already exist
        if (!historyList.contains(historyEntry)) {
            historyList.add(historyEntry);
        }
    }

}