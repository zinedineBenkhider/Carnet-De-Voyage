package com.example.yahiatene.maquette;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final CardView button_add = findViewById(R.id.add_button);
        button_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addTripIntent = new Intent(MainActivity.this, AddingTrip.class);
                startActivity(addTripIntent);
            }
        });

        final CardView button_view = findViewById(R.id.view_button);
        button_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent viewTripIntent = new Intent(MainActivity.this, ViewTrips.class);
                startActivity(viewTripIntent);
            }
        });

        final CardView button_impexp = findViewById(R.id.impextrip);
        button_impexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Import/Export");
                builder.setCancelable(true);
                builder.setPositiveButton("Exporter des Voyages", new OkOnClickListener());
                builder.setNegativeButton("Importer des Voyages", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final CardView button_edit = findViewById(R.id.edittrip);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(MainActivity.this, EditTrip.class);
                startActivity(editIntent);
            }
        });

        final CardView button_option = findViewById(R.id.options);
        button_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent optionIntent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(optionIntent);
            }
        });

    }

    private final class CancelOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getApplicationContext(), "Importation de données",
                    Toast.LENGTH_LONG).show();
        }
    }

    private final class OkOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getApplicationContext(), "Exportation de données",
                    Toast.LENGTH_LONG).show();
        }
    }

}
