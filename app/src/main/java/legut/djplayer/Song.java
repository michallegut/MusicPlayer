package legut.djplayer;

class Song {
    private int albumImage;
    private String songTitle;
    private String band;
    private int song;

    Song(int albumImage, String songTitle, String band, int song) {
        this.albumImage = albumImage;
        this.songTitle = songTitle;
        this.band = band;
        this.song = song;
    }

    int getAlbumImage() {
        return albumImage;
    }

    String getSongTitle() {
        return songTitle;
    }

    String getBand() {
        return band;
    }

    int getSong() {
        return song;
    }
}
