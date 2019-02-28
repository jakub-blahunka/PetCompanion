package org.jaku8ka.petcompanion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class PetsAdapter extends ArrayAdapter<Pet> {

    private Context context;
    private List<Pet> pets;
    private ArrayList<Long> mDate = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();

    public PetsAdapter(Context context, List<Pet> list) {

        super(context, R.layout.row_list_layout, list);
        this.context = context;
        this.pets = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_list_layout, parent, false);

            holder = new ViewHolder();

            holder.name = view.findViewById(R.id.tvName);
            holder.pet = view.findViewById(R.id.ivAvatar);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(pets.get(position).getName());

        switch (pets.get(position).getType()) {
            case 0:
                holder.pet.setBackground(ContextCompat.getDrawable(context, R.drawable.cat));
                break;

            case 1:
                holder.pet.setBackground(ContextCompat.getDrawable(context, R.drawable.dog));
                break;

            case 2:
                holder.pet.setBackground(ContextCompat.getDrawable(context, R.drawable.rabbit));
                break;

            default:
                break;
        }

        if (pets.get(position).getSelected()) {
            view.setBackgroundResource(R.drawable.selected);
        } else {
            view.setBackgroundResource(R.drawable.pressed);
        }

        Calendar calendar = Calendar.getInstance();
        long todayDate = calendar.getTimeInMillis();

        try {
            if (todayDate <= ApplicationClass.pets.get(position).getNextParasites().getTime()) {
                mDate.add(ApplicationClass.pets.get(position).getNextParasites().getTime());
                mName.add(ApplicationClass.pets.get(position).getName());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (todayDate <= ApplicationClass.pets.get(position).getNextVaccination().getTime()) {
                mDate.add(ApplicationClass.pets.get(position).getNextVaccination().getTime());
                mName.add(ApplicationClass.pets.get(position).getName());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            int minIndex = mDate.indexOf(Collections.min(mDate));
            long finalDate = mDate.get(minIndex);
            String name = mName.get(minIndex);

            if (todayDate <= finalDate) {
                NotificationScheduler.scheduleNotification(context, finalDate + 3600 * 8000, name);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return view;
    }

    private static class ViewHolder {
        public ImageView pet;
        public TextView name;
    }

}
