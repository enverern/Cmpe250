public class Song {
    public int playlist_id;
    public int song_id;
    private final String song_name;
    private final int play_count;
    private final int heartache_score;
    private final int roadtrip_score;
    private final int blissful_score;
    boolean isheartache;
    boolean isroadtrip;
    boolean isblissful;

    public Song(int song_id, String song_name, int play_count, int heartache_score, int roadtrip_score, int blissful_score) {
        this.playlist_id = 0;
        this.song_id = song_id;
        this.song_name = song_name;
        this.play_count = play_count;
        this.heartache_score = heartache_score;
        this.roadtrip_score = roadtrip_score;
        this.blissful_score = blissful_score;
        this.isheartache = false;
        this.isroadtrip = false;
        this.isblissful = false;
    }

    public int getId() {
        return this.song_id;
    }
    // setters to set the status (is it in the epicblend or not) of the song
    public void makeHeartache(boolean bool) {
        this.isheartache = bool;
    }

    public void makeRoadtrip(boolean bool) {
        this.isroadtrip = bool;
    }

    public void makeBlissful(boolean bool) {
        this.isblissful = bool;
    }
    // setter and getter for the playlist id of the song
    public void setPlaylistId(int playlist_idd) {
        this.playlist_id = playlist_idd;
    }

    public int getPlaylistId() {
        return this.playlist_id;
    }
    //getters for the scores and the name of the song

    public String getSongName() {
        return this.song_name;
    }

    public int getPlayCount() {
        return this.play_count;
    }

    public int getHeartacheScore() {
        return this.heartache_score;
    }

    public int getRoadtripScore() {
        return this.roadtrip_score;
    }

    public int getBlissfulScore() {
        return this.blissful_score;
    }

}