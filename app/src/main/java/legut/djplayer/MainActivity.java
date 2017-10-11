package legut.djplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Song> songsList = new ArrayList<>();
    RecyclerView recyclerView;
    SongsAdapter songsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        songsAdapter = new SongsAdapter(songsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(songsAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getBaseContext(), PlayerActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        prepareSongsData();
    }

    void prepareSongsData() {
        Song song = new Song(getResources().getIdentifier("the_serpent_and_the_sphere", "drawable", this.getPackageName()), "Plateau of the Ages", "Agalloch", getResources().getIdentifier("plateau_of_the_ages", "raw", this.getPackageName()));
        songsList.add(song);

        song = new Song(getResources().getIdentifier("deliverance", "drawable", this.getPackageName()), "A Fair Judgement", "Opeth", getResources().getIdentifier("a_fair_judgement", "raw", this.getPackageName()));
        songsList.add(song);

        song = new Song(getResources().getIdentifier("winters_gate", "drawable", this.getPackageName()), "Winter's Gate Part 6", "Insomnium", getResources().getIdentifier("winters_gate_part_6", "raw", this.getPackageName()));
        songsList.add(song);

        songsAdapter.notifyDataSetChanged();
    }
}