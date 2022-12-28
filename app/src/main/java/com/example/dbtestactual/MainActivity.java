package com.example.dbtestactual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private FragmentManager manager;
    private MusicDatabase instance;
    private RecyclerView trackRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            manager = getSupportFragmentManager();

            manager.beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container, MusicFragment.class, null).commit();
        }


        instance = MusicDatabase.Instance.DB_INSTANCE.getDatabase(getApplicationContext());


        MusicDao dao = instance.getDao();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem add, remove, search;
        add = menu.findItem(R.id.add_item);
        remove = menu.findItem(R.id.remove_item);
        search = menu.findItem(R.id.search_item);
        CheckBox filter = findViewById(R.id.filter);
        Toast universalToast = new Toast(getApplicationContext());
        universalToast.setDuration(Toast.LENGTH_SHORT);
        AtomicBoolean filtered = new AtomicBoolean(false);


        TrackAdapter musicAdapter = (TrackAdapter) trackRecycler.getAdapter();

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);

        add.setOnMenuItemClickListener(e -> {


            View view = getLayoutInflater().inflate(R.layout.add_track_layout, null);

            EditText titleField = view.findViewById(R.id.title_field);

            EditText artistField = view.findViewById(R.id.artist_field);


            builder.setView(view);
            builder.setTitle("Add a new Track");
            builder.setMessage("Add the title and artist of the track you would like to add");
            builder.setPositiveButton("add track", (dialogInterface, i) -> MusicDatabase.service.execute(() -> {

                instance.getDao().insert(new Track(titleField.getText().toString(), artistField.getText().toString()));
            }));


            builder.create().show();

            return true;
        });
        search.setOnMenuItemClickListener(e -> {

            View view = getLayoutInflater().inflate(R.layout.search_layout, null);

            LinearLayout layout = view.findViewById(R.id.search_type);
            CheckBox title = layout.findViewById(R.id.title_search);
            CheckBox artist = layout.findViewById(R.id.artist_search);
            EditText searchValue = view.findViewById(R.id.search_value);

            View.OnClickListener checkBoxListener = c -> {
                CheckBox checkBox = (CheckBox) c;

                if (checkBox.isChecked()) switch (c.getId()) {
                    case R.id.title_search:
                        artist.setChecked(false);
                        searchValue.setHint("title");

                        break;
                    case R.id.artist_search:
                        title.setChecked(false);
                        searchValue.setHint("artist");

                        break;
                    default:

                        break;
                }
                else searchValue.setHint("");
            };

            title.setOnClickListener(checkBoxListener);
            artist.setOnClickListener(checkBoxListener);

            builder.setView(view);
            builder.setTitle("Search for a Track");
            builder.setMessage("Please specify your search");
            builder.setPositiveButton("set filter", (i, c) -> {
                String text = searchValue.getText().toString();

                if (artist.isChecked()) {

                    filter.setOnCheckedChangeListener((compoundButton, checked) -> {
                        if (checked)
                            instance.getDao().getAllTrackByArtist(text).observe(this, tracks -> {
                                musicAdapter.getTrackList().clear();
                                musicAdapter.getTrackList().addAll(tracks);
                                musicAdapter.notifyDataSetChanged();

                            });
                        else instance.getDao().getAllTracksSorted().observe(this, tracks -> {
                            musicAdapter.getTrackList().clear();
                            musicAdapter.getTrackList().addAll(tracks);
                            musicAdapter.notifyDataSetChanged();
                        });

                    });
                    filter.setChecked(false);
                    filter.setChecked(true);
                } else if (title.isChecked()) {

                    filter.setOnCheckedChangeListener((compoundButton, checked) -> {
                        if (checked)
                            instance.getDao().getAllTrackByTitle(text).observe(this, tracks -> {
                                musicAdapter.getTrackList().clear();
                                musicAdapter.getTrackList().addAll(tracks);
                                musicAdapter.notifyDataSetChanged();

                            });
                        else instance.getDao().getAllTracksSorted().observe(this, tracks -> {
                            musicAdapter.getTrackList().clear();
                            musicAdapter.getTrackList().addAll(tracks);
                            musicAdapter.notifyDataSetChanged();
                        });

                    });
                    filter.setChecked(false);
                    filter.setChecked(true);
                } else {

                    universalToast.setText("please select a title or artist to filter");
                    universalToast.show();
                }

            });
            builder.create().show();
            return true;
        });

        remove.setOnMenuItemClickListener(e -> {
            if (musicAdapter.getHighlighted() != null)

                MusicDatabase.service.execute(() -> instance.getDao().delete(musicAdapter.getHighlighted().getTrack()));
            else {

                universalToast.setText("no track highlighted (press track to highlight)");

                universalToast.show();


            }

            return false;
        });

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        trackRecycler = findViewById(R.id.track_recycler);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        TrackAdapter adapter = new TrackAdapter();

        trackRecycler.setLayoutManager(layoutManager);
        trackRecycler.setAdapter(adapter);

        instance.getDao().getAllTracksSorted().observe(this, tracks -> {
            adapter.getTrackList().clear();
            adapter.getTrackList().addAll(tracks);
            adapter.notifyDataSetChanged();
        });

    }
}