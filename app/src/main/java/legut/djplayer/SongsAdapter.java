package legut.djplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    private List<Song> songsList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView songTitle;
        TextView band;

        MyViewHolder(View view) {
            super(view);
            albumImage = (ImageView) view.findViewById(R.id.album_image);
            songTitle = (TextView) view.findViewById(R.id.song_title);
            band = (TextView) view.findViewById(R.id.band);
        }
    }

    SongsAdapter(List<Song> songsList) {
        this.songsList = songsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = songsList.get(position);
        holder.albumImage.setImageResource(song.getAlbumImage());
        holder.songTitle.setText(song.getSongTitle());
        holder.band.setText(song.getBand());
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }
}