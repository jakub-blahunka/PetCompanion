package org.jaku8ka.petcompanion;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetListDetailFragment extends Fragment {

    ImageView ivPet;
    TextView tv_name, age_normal, age_pet, tv_par, tv_next_par, tv_vac, tv_next_vac, tv_par_count, tv_vac_count;
    ProgressBar pb_vac, pb_par;
    View view;

    public PetListDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pet_list_detail, container, false);

        initViews();
        ((Information) getActivity()).setFragmentRefreshListener(new Information.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                populateUI();
            }
        });

        return view;
    }

    private void populateUI() {

        if (MainActivity.POSITION != 9999) {

            switch (ApplicationClass.pets.get(MainActivity.POSITION).getType()) {
                case 0:
                    ivPet.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.cat));
                    break;

                case 1:
                    ivPet.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dog));
                    break;

                case 2:
                    ivPet.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rabbit));
                    break;
            }

            tv_name.setText(ApplicationClass.pets.get(MainActivity.POSITION).getName());
            String birth = ApplicationClass.pets.get(MainActivity.POSITION).getDateOfBirth();
            try {
                if (birth.isEmpty()) {
                    age_normal.setText("...");
                    age_pet.setText("(...)");
                } else {
                    StringTokenizer tokens = new StringTokenizer(birth, ".");

                    int spinnerValue = ApplicationClass.pets.get(MainActivity.POSITION).getType();

                    int day = Integer.parseInt(tokens.nextToken().trim());
                    int month = Integer.parseInt(tokens.nextToken().trim());
                    int year = Integer.parseInt(tokens.nextToken().trim());

                    String birthNormalAge = getAge(year, month, day);
                    String birthPetAge = getAgePet(Integer.parseInt(birthNormalAge), spinnerValue);

                    age_normal.setText(birthNormalAge + "y");
                    age_pet.setText("(" + birthPetAge + "y)");
                }
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
                progressDrawablePar.setColorFilter(getContext().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_IN);
            } else if (typePar < 66.0) {
                progressDrawablePar.setColorFilter(getContext().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
            } else
                progressDrawablePar.setColorFilter(getContext().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
        } else {
            tv_par_count.setText("");
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
                progressDrawableVac.setColorFilter(getContext().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_IN);
            } else if (typeVac < 66.0) {
                progressDrawableVac.setColorFilter(getContext().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
            } else
                progressDrawableVac.setColorFilter(getContext().getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN);
        } else {
            tv_vac_count.setText("");
            pb_vac.setProgress(0);
        }
    }

}

    private void initViews() {

        ivPet = view.findViewById(R.id.ivPet);
        tv_name = view.findViewById(R.id.tv_name);
        age_normal = view.findViewById(R.id.age_normal);
        age_pet = view.findViewById(R.id.age_pet);
        tv_par = view.findViewById(R.id.tv_date_par);
        tv_next_par = view.findViewById(R.id.tv_date_next_par);
        tv_vac = view.findViewById(R.id.tv_date_vac);
        tv_next_vac = view.findViewById(R.id.tv_date_next_vac);
        tv_par_count = view.findViewById(R.id.tv_par_count);
        tv_vac_count = view.findViewById(R.id.tv_vac_count);
        pb_vac = view.findViewById(R.id.pb_vac);
        pb_par = view.findViewById(R.id.pb_par);
    }

    /**
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * getMenuInflater().inflate(R.menu.menu_new_pet, menu);
     * <p>
     * ActionBar actionBar = getSupportActionBar();
     * actionBar.setDisplayHomeAsUpEnabled(true);
     * <p>
     * MenuItem menuItem = menu.findItem(R.id.action_ok);
     * menuItem.setVisible(false);
     * <p>
     * return true;
     * }
     */


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
        String petYears = "...";

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

    private long getNumberOfDays(String date1, String date2, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Date Date1 = null, Date2 = null;
        try {
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000);
    }

    private float getPbPercent(int currentDays, long daysToEnd) {

        return (currentDays * 100 / daysToEnd);
    }
}
