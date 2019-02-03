package org.jaku8ka.petcompanion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class PetInfo extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    ImageView ivPet;
    TextView tv_name, age_normal, age_pet, tv_par, tv_next_par, tv_vac, tv_next_vac, tv_par_count, tv_vac_count;
    ProgressBar pb_vac, pb_par;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);

        initViews();
        populateUI();
    }

    private void populateUI() {
        if(MainActivity.POSITION != 9999) {

            switch (ApplicationClass.pets.get(MainActivity.POSITION).getType()) {
                case 0:
                    ivPet.setBackground(ContextCompat.getDrawable(this, R.drawable.cat));
                    break;

                case 1:
                    ivPet.setBackground(ContextCompat.getDrawable(this, R.drawable.dog));
                    break;

                case 2:
                    ivPet.setBackground(ContextCompat.getDrawable(this, R.drawable.rabbit));
                    break;
            }

            tv_name.setText(ApplicationClass.pets.get(MainActivity.POSITION).getName());

            try {
                String birth = ApplicationClass.pets.get(MainActivity.POSITION).getDateOfBirth();
                StringTokenizer tokens = new StringTokenizer(birth, ".");
                int spinnerValue = ApplicationClass.pets.get(MainActivity.POSITION).getType();

                int day = Integer.parseInt(tokens.nextToken().trim());
                int month = Integer.parseInt(tokens.nextToken().trim());
                int year = Integer.parseInt(tokens.nextToken().trim());

                String birthNormalAge = getAge(year, month, day);
                String birthPetAge = getAgePet(Integer.parseInt(birthNormalAge), spinnerValue);

                age_normal.setText(birthNormalAge + "r.");
                age_pet.setText("(" + birthPetAge + "r.)");
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NoSuchElementException n) {
                n.printStackTrace();
            }

            tv_par.setText(ApplicationClass.pets.get(MainActivity.POSITION).getParasites());
            tv_next_par.setText(ApplicationClass.pets.get(MainActivity.POSITION).getNextParasitesString());
            tv_vac.setText(ApplicationClass.pets.get(MainActivity.POSITION).getVaccination());
            tv_next_vac.setText(ApplicationClass.pets.get(MainActivity.POSITION).getNextVaccinationString());

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            if (!(ApplicationClass.pets.get(MainActivity.POSITION).getParasites().isEmpty())) {
                long parCount = (getNumberOfDays(ApplicationClass.pets.get(MainActivity.POSITION).getParasites(), ApplicationClass.pets.get(MainActivity.POSITION).getNextParasitesString(), "dd.MM.yyyy"));
                long parDays = (getNumberOfDays(currentDate, ApplicationClass.pets.get(MainActivity.POSITION).getNextParasitesString(), "dd.MM.yyyy"));
                int parProgress = Math.toIntExact(parCount - parDays);
                pb_par.setMax(Math.toIntExact(parCount));
                String sParCount = String.valueOf(parDays);
                if (Integer.parseInt(sParCount) == 1) {
                    tv_par_count.setText(sParCount + " day");
                } else tv_par_count.setText(sParCount + " days");
                pb_par.setProgress(parProgress, true);

                LayerDrawable layerDrawablePar = (LayerDrawable) pb_par.getProgressDrawable();
                Drawable progressDrawablePar = layerDrawablePar.findDrawableByLayerId(android.R.id.progress);

                Float typePar = getPbPercent(parProgress, parCount);
                if (typePar < 33.0) {
                    progressDrawablePar.setColorFilter(this.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                } else if (typePar < 66.0) {
                    progressDrawablePar.setColorFilter(this.getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
                } else
                    progressDrawablePar.setColorFilter(this.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            } else {
                pb_par.setProgress(0);
            }

            if (!(ApplicationClass.pets.get(MainActivity.POSITION).getVaccination().isEmpty())) {
                long vacCount = (getNumberOfDays(ApplicationClass.pets.get(MainActivity.POSITION).getVaccination(), ApplicationClass.pets.get(MainActivity.POSITION).getNextVaccinationString(), "dd.MM.yyyy"));
                long vacDays = (getNumberOfDays(currentDate, ApplicationClass.pets.get(MainActivity.POSITION).getNextVaccinationString(), "dd.MM.yyyy"));
                int vacProgress = Math.toIntExact(vacCount - vacDays);
                pb_vac.setMax(Math.toIntExact(vacCount));
                String sVacCount = String.valueOf(vacDays);
                if (Integer.parseInt(sVacCount) == 1) {
                    tv_vac_count.setText(sVacCount + " day");
                } else tv_vac_count.setText(sVacCount + " days");
                pb_vac.setProgress(vacProgress, true);

                LayerDrawable layerDrawableVac = (LayerDrawable) pb_vac.getProgressDrawable();
                Drawable progressDrawableVac = layerDrawableVac.findDrawableByLayerId(android.R.id.progress);

                Float typeVac = getPbPercent(vacProgress, vacCount);
                if (typeVac < 33.0) {
                    progressDrawableVac.setColorFilter(this.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                } else if (typeVac < 66.0) {
                    progressDrawableVac.setColorFilter(this.getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
                } else
                    progressDrawableVac.setColorFilter(this.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            } else {
                pb_vac.setProgress(0);
            }
        }
    }

    private void initViews() {
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        ivPet = findViewById(R.id.ivPet);
        tv_name = findViewById(R.id.tv_name);
        age_normal = findViewById(R.id.age_normal);
        age_pet = findViewById(R.id.age_pet);
        tv_par = findViewById(R.id.tv_date_par);
        tv_next_par = findViewById(R.id.tv_date_next_par);
        tv_vac = findViewById(R.id.tv_date_vac);
        tv_next_vac = findViewById(R.id.tv_date_next_vac);
        tv_par_count = findViewById(R.id.tv_par_count);
        tv_vac_count = findViewById(R.id.tv_vac_count);
        pb_vac = findViewById(R.id.pb_vac);
        pb_par = findViewById(R.id.pb_par);
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
        tvLoad.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_pet, menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        MenuItem menuItem = menu.findItem(R.id.action_ok);
        menuItem.setVisible(false);

        return true;
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        Integer ageInt = new Integer(age);
        String fail = "0";
        if (ageInt < 0)
            return fail;

        String ageS = ageInt.toString();

        return ageS;
    }

    private String getAgePet(int years, int spinnerValue) {
        String petYears = "Cannot count.";

        switch (spinnerValue) {
            case 0: //macka
                switch (years) {
                    case 1:
                        petYears = "17";
                        break;
                    case 2:
                        petYears = "24";
                        break;
                    case 3:
                        petYears = "28";
                        break;
                    case 4:
                        petYears = "32";
                        break;
                    case 5:
                        petYears = "36";
                        break;
                    case 6:
                        petYears = "40";
                        break;
                    case 7:
                        petYears = "44";
                        break;
                    case 8:
                        petYears = "48";
                        break;
                    case 9:
                        petYears = "52";
                        break;
                    case 10:
                        petYears = "56";
                        break;
                    case 11:
                        petYears = "60";
                        break;
                    case 12:
                        petYears = "64";
                        break;
                    case 13:
                        petYears = "68";
                        break;
                    case 14:
                        petYears = "72";
                        break;
                    case 15:
                        petYears = "76";
                        break;
                    case 16:
                        petYears = "80";
                        break;
                    case 17:
                        petYears = "84";
                        break;
                    case 18:
                        petYears = "88";
                        break;
                    case 19:
                        petYears = "92";
                        break;
                    case 20:
                        petYears = "100";
                        break;
                    case 21:
                        petYears = "108";
                        break;
                    default:
                        break;
                }

                break;
            case 1: //pes
                switch (years) {
                    case 1:
                        petYears = "15.5";
                        break;
                    case 2:
                        petYears = "25";
                        break;
                    case 3:
                        petYears = "29";
                        break;
                    case 4:
                        petYears = "32";
                        break;
                    case 5:
                        petYears = "37";
                        break;
                    case 6:
                        petYears = "40";
                        break;
                    case 7:
                        petYears = "44";
                        break;
                    case 8:
                        petYears = "47";
                        break;
                    case 9:
                        petYears = "52";
                        break;
                    case 10:
                        petYears = "55";
                        break;
                    case 11:
                        petYears = "60";
                        break;
                    case 12:
                        petYears = "64";
                        break;
                    case 13:
                        petYears = "68";
                        break;
                    case 14:
                        petYears = "72";
                        break;
                    case 15:
                        petYears = "77";
                        break;
                    case 16:
                        petYears = "80";
                        break;
                    case 17:
                        petYears = "85";
                        break;
                    case 18:
                        petYears = "88";
                        break;
                    case 19:
                        petYears = "96";
                        break;
                    case 20:
                        petYears = "100";
                        break;
                    case 21:
                        petYears = "104";
                        break;
                    default:
                        break;
                }

                break;
            case 2: //zajac
                switch (years) {
                    case 1:
                        petYears = "20";
                        break;
                    case 2:
                        petYears = "28";
                        break;
                    case 3:
                        petYears = "36";
                        break;
                    case 4:
                        petYears = "44";
                        break;
                    case 5:
                        petYears = "52";
                        break;
                    case 6:
                        petYears = "60";
                        break;
                    case 7:
                        petYears = "68";
                        break;
                    case 8:
                        petYears = "76";
                        break;
                    case 9:
                        petYears = "84";
                        break;
                    case 10:
                        petYears = "92";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return petYears;
    }

    private long getNumberOfDays(String date1,String date2,String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date Date1 = null,Date2 = null;
        try{
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime())/(24*60*60*1000);
    }

    private float getPbPercent(int currentDays, long daysToEnd) {

        return (currentDays * 100 / daysToEnd);
    }
}
