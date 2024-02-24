package com.example.pawrescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pawrescue.Model.Animal;

import java.util.List;

public class AnimalListAdapter extends RecyclerView.Adapter<AnimalListAdapter.ViewHolder> {

    private List<Animal> animalList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    AnimalListAdapter(Context context, List<Animal> animalList) {
        this.inflater = LayoutInflater.from(context);
        this.animalList = animalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        String imageUrl = animal.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageAnimal); // Changed to imageAnimal
        } else {
            holder.imageAnimal.setImageResource(R.drawable.default_pet_image); // Use your default image
        }

        holder.textAnimalName.setText(animal.getName());
        //holder.textAnimalDescription.setText(animal.getDescription());
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageAnimal; // Changed to ImageView
        TextView textAnimalName;
        //TextView textAnimalDescription;

        ViewHolder(View itemView) {
            super(itemView);
            imageAnimal = itemView.findViewById(R.id.image_animal); // This is now an ImageView
            textAnimalName = itemView.findViewById(R.id.text_animal_name);
            //textAnimalDescription = itemView.findViewById(R.id.text_animal_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }


    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
