package org.jaku8ka.petcompanion;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewPet extends AppCompatActivity {

    private TextView tvDateBirth, tvDateParasites, tvDateVaccination;
    private EditText etName, etSpecies, etColor;
    private Spinner sPet, sSex, sParasites, sVaccination;

    private int sexSpinner, petSpinner, vacSpinner, parSpinner;
    private Button btnDateOfBirth, btnDateOfPar, btnDateOfVac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);

        initViews();
        populateUI();
    }

    private void populateUI() {
        if (MainActivity.POSITION != 9999) {
            etName.setText(ApplicationClass.pets.get(MainActivity.POSITION).getName());
            sPet.setSelection(ApplicationClass.pets.get(MainActivity.POSITION).getType());
            tvDateBirth.setText(ApplicationClass.pets.get(MainActivity.POSITION).getDateOfBirth());
            sSex.setSelection(ApplicationClass.pets.get(MainActivity.POSITION).getSex());
            etSpecies.setText(ApplicationClass.pets.get(MainActivity.POSITION).getSpecies());
            etColor.setText(ApplicationClass.pets.get(MainActivity.POSITION).getColor());
            tvDateParasites.setText(ApplicationClass.pets.get(MainActivity.POSITION).getParasites());
            sParasites.setSelection(ApplicationClass.pets.get(MainActivity.POSITION).getNextPar());
            tvDateVaccination.setText(ApplicationClass.pets.get(MainActivity.POSITION).getVaccination());
            sVaccination.setSelection(ApplicationClass.pets.get(MainActivity.POSITION).getNextVac());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_pet, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_ok) {

            if (etName.getText().toString().isEmpty()) {
                Toast.makeText(NewPet.this, "Please enter name!", Toast.LENGTH_SHORT).show();
            } else {
                saveNewPet();
            }

            return true;

        } else
            return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        tvDateBirth = findViewById(R.id.tvDateBirth);
        tvDateParasites = findViewById(R.id.tvDateParasites);
        tvDateVaccination = findViewById(R.id.tvDateVaccination);
        etName = findViewById(R.id.etName);
        etSpecies = findViewById(R.id.etSpecies);
        etColor = findViewById(R.id.etColor);
        sPet = findViewById(R.id.sPet);
        ArrayAdapter<CharSequence> adapterPet = ArrayAdapter.createFromResource(this, R.array.pet_types, R.layout.spinner_item);
        adapterPet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPet.setAdapter(adapterPet);
        sPet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                petSpinner = adapterView.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sSex = findViewById(R.id.sSex);
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this, R.array.sex_types, R.layout.spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSex.setAdapter(adapterSex);
        sSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexSpinner = adapterView.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sParasites = findViewById(R.id.sParasites);
        ArrayAdapter<CharSequence> adapterOdc = ArrayAdapter.createFromResource(this, R.array.par_loop, R.layout.spinner_item);
        adapterOdc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sParasites.setAdapter(adapterOdc);
        sParasites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                parSpinner = adapterView.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sVaccination = findViewById(R.id.sVaccination);
        ArrayAdapter<CharSequence> adapterVac = ArrayAdapter.createFromResource(this, R.array.vac_loop, R.layout.spinner_item);
        adapterVac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sVaccination.setAdapter(adapterVac);
        sVaccination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vacSpinner = adapterView.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnDateOfBirth = findViewById(R.id.add_pet_birth);
        btnDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();

                dialogFragment.show(getSupportFragmentManager(), "Date Picker Birth");
            }
        });

        btnDateOfPar = findViewById(R.id.add_pet_par);
        btnDateOfPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();

                dialogFragment.show(getSupportFragmentManager(), "Date Picker Par");
            }
        });

        btnDateOfVac = findViewById(R.id.add_pet_vac);
        btnDateOfVac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();

                dialogFragment.show(getSupportFragmentManager(), "Date Picker Vac");
            }
        });

    }

    private void saveNewPet() {

        String name = etName.getText().toString().trim();
        int petType = petSpinner;
        String birth = tvDateBirth.getText().toString().trim();
        int sex = sexSpinner;
        String species = etSpecies.getText().toString().trim();
        String color = etColor.getText().toString().trim();
        String parDate = tvDateParasites.getText().toString().trim();
        int parLoop = parSpinner;
        String vacDate = tvDateVaccination.getText().toString().trim();
        int vacLoop = vacSpinner;

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateParString = null;
        Date datePar = null;
        try {
            datePar = dateFormat.parse(parDate);

            int myMonthValue;
            switch (parLoop) {
                case 0:
                    myMonthValue = 1;
                    break;
                case 1:
                    myMonthValue = 3;
                    break;
                case 2:
                    myMonthValue = 6;
                    break;
                default:
                    myMonthValue = 0;
            }
            datePar.setMonth(datePar.getMonth() + myMonthValue);
            dateParString = dateFormat.format(datePar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateVacString = null;
        Date dateVac = null;
        try {
            dateVac = dateFormat.parse(vacDate);

            int myMonthValue;
            switch (vacLoop) {
                case 0:
                    myMonthValue = 6;
                    break;
                case 1:
                    myMonthValue = 12;
                    break;
                case 2:
                    myMonthValue = 24;
                    break;
                default:
                    myMonthValue = 0;
            }
            dateVac.setMonth(dateVac.getMonth() + myMonthValue);
            dateVacString = dateFormat.format(dateVac);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (MainActivity.POSITION == 9999) {
            Pet pet = new Pet();

            pet.setName(name);
            pet.setType(petType);
            pet.setDateOfBirth(birth);
            pet.setSex(sex);
            pet.setSpecies(species);
            pet.setColor(color);
            pet.setParasites(parDate);
            pet.setNextPar(parLoop);
            pet.setVaccination(vacDate);
            pet.setNextVac(vacLoop);
            pet.setUserEmail(ApplicationClass.user.getEmail());
            pet.setNextParasites(datePar);
            pet.setNextVaccination(dateVac);
            pet.setNextParasitesString(dateParString);
            pet.setNextVaccinationString(dateVacString);
            pet.setSelected(false);

            Backendless.Persistence.save(pet, new AsyncCallback<Pet>() {
                @Override
                public void handleResponse(Pet response) {

                    Toast.makeText(NewPet.this, "Pet saved successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPet.this, MainActivity.class));
                    NewPet.this.finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    Toast.makeText(NewPet.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            ApplicationClass.pets.get(MainActivity.POSITION).setName(etName.getText().toString().trim());
            ApplicationClass.pets.get(MainActivity.POSITION).setType(petType);
            ApplicationClass.pets.get(MainActivity.POSITION).setDateOfBirth(tvDateBirth.getText().toString().trim());
            ApplicationClass.pets.get(MainActivity.POSITION).setSex(sex);
            ApplicationClass.pets.get(MainActivity.POSITION).setSpecies(etSpecies.getText().toString().trim());
            ApplicationClass.pets.get(MainActivity.POSITION).setColor(etColor.getText().toString().trim());
            ApplicationClass.pets.get(MainActivity.POSITION).setParasites(tvDateParasites.getText().toString().trim());
            ApplicationClass.pets.get(MainActivity.POSITION).setNextPar(parLoop);
            ApplicationClass.pets.get(MainActivity.POSITION).setVaccination(tvDateVaccination.getText().toString().trim());
            ApplicationClass.pets.get(MainActivity.POSITION).setNextVac(vacLoop);
            ApplicationClass.pets.get(MainActivity.POSITION).setUserEmail(ApplicationClass.user.getEmail());
            ApplicationClass.pets.get(MainActivity.POSITION).setNextParasites(datePar);
            ApplicationClass.pets.get(MainActivity.POSITION).setNextVaccination(dateVac);
            ApplicationClass.pets.get(MainActivity.POSITION).setNextParasitesString(dateParString);
            ApplicationClass.pets.get(MainActivity.POSITION).setNextVaccinationString(dateVacString);
            ApplicationClass.pets.get(MainActivity.POSITION).setSelected(false);

            Backendless.Persistence.save(ApplicationClass.pets.get(MainActivity.POSITION), new AsyncCallback<Pet>() {
                @Override
                public void handleResponse(Pet response) {

                    Toast.makeText(NewPet.this, "Pet updated successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPet.this, MainActivity.class));
                    NewPet.this.finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    Toast.makeText(NewPet.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String petName = etName.getText().toString();
        int petType = petSpinner;
        String petDate = tvDateBirth.getText().toString();
        int petSex = sexSpinner;
        String petSpecies = etSpecies.getText().toString();
        String petColor = etColor.getText().toString();
        String odcDate = tvDateParasites.getText().toString();
        int odcLoop = parSpinner;
        String vacDate = tvDateVaccination.getText().toString();
        int vacLoop = vacSpinner;

        outState.putString("savedName", petName);
        outState.putInt("savedType", petType);
        outState.putString("savedDate", petDate);
        outState.putInt("savedSex", petSex);
        outState.putString("savedSpecies", petSpecies);
        outState.putString("savedColor", petColor);
        outState.putString("savedDateOdc", odcDate);
        outState.putInt("savedLoopOdc", odcLoop);
        outState.putString("savedDateVac", vacDate);
        outState.putInt("savedLoopVac", vacLoop);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String petName = savedInstanceState.getString("savedName");
        int petType = savedInstanceState.getInt("savedType");
        String petDate = savedInstanceState.getString("savedDate");
        int petSex = savedInstanceState.getInt("savedSex");
        String petSpecies = savedInstanceState.getString("savedSpecies");
        String petColor = savedInstanceState.getString("savedColor");
        String odcDate = savedInstanceState.getString("savedDateOdc");
        int odcLoop = savedInstanceState.getInt("savedLoopOdc");
        String vacDate = savedInstanceState.getString("savedDateVac");
        int vacLoop = savedInstanceState.getInt("savedLoopVac");

        etName.setText(petName);
        sPet.setSelection(petType);
        tvDateBirth.setText(petDate);
        sSex.setSelection(petSex);
        etSpecies.setText(petSpecies);
        etColor.setText(petColor);
        tvDateParasites.setText(odcDate);
        sParasites.setSelection(odcLoop);
        tvDateVaccination.setText(vacDate);
        sVaccination.setSelection(vacLoop);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        NewPet.this.finish();
    }
}
