package com.example.pawrescue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawrescue.Model.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnimalListPage extends Fragment {

    private RecyclerView recyclerView;
    private AnimalListAdapter adapter;
    private List<Animal> animalList = new ArrayList<>();
    private Switch switchAnimalType;

    public AnimalListPage() {
        // Required empty public constructor
    }

    public static AnimalListPage newInstance() {
        return new AnimalListPage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_list_page, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AnimalListAdapter(getContext(), animalList);
        adapter.setClickListener(new AnimalListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                goToPetProfilePage(animalList.get(position));
            }
        });

        recyclerView.setAdapter(adapter);

        /*switchAnimalType = view.findViewById(R.id.switchAnimal);
        switchAnimalType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fetchAnimalsFromFirebase("Pet");
                } else {
                    fetchAnimalsFromFirebase("Stray");
                }
            }
        });*/

        // Fetch stray animals by default
        //fetchAnimalsFromFirebase("Stray");
        fetchAnimalsFromFirebase();
        //fetchAnimalsFromFirebase("Stray");

        return view;
    }

    private void fetchAnimalsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("animals");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalList.clear();
                for (DataSnapshot animalSnapshot : dataSnapshot.getChildren()) {
                    Animal animal = animalSnapshot.getValue(Animal.class);
                    if (animal != null) {
                        animal.setId(animalSnapshot.getKey()); // Store the Firebase key in the Animal object
                    }
                    animalList.add(animal);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors, e.g., show a toast or log
            }
        });
    }

    /*private void fetchAnimalsFromFirebase(String category) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("animals");
        databaseReference.orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalList.clear();
                for (DataSnapshot animalSnapshot : dataSnapshot.getChildren()) {
                    Animal animal = animalSnapshot.getValue(Animal.class);
                    if (animal != null) {
                        animal.setId(animalSnapshot.getKey()); // Store the Firebase key in the Animal object
                    }
                    animalList.add(animal);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors, e.g., show a toast or log
            }
        });
    }*/

    private void goToPetProfilePage(Animal animal) {
        Fragment animalProfileFragment = AnimalProfilePage.newInstance(animal);
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, animalProfileFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
