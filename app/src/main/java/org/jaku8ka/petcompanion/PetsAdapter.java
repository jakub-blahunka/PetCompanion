package org.jaku8ka.petcompanion;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PetsAdapter extends ArrayAdapter<Pet> {

    private Context context;
    private List<Pet> pets;

    public PetsAdapter(Context context, List<Pet> list) {

        super(context, R.layout.row_list_layout, list);
        this.context = context;
        this.pets = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.row_list_layout, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvName);
        ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);

        tvName.setText(pets.get(position).getName());

        switch (pets.get(position).getType()) {
            case 0:
                ivAvatar.setBackground(ContextCompat.getDrawable(context, R.drawable.cat));
                break;

            case 1:
                ivAvatar.setBackground(ContextCompat.getDrawable(context, R.drawable.dog));
                break;

            case 2:
                ivAvatar.setBackground(ContextCompat.getDrawable(context, R.drawable.rabbit));
                break;

            default:
                break;
        }

        return convertView;
    }

}
