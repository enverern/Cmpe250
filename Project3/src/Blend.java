import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

public class Blend {
    private final HashMap<Integer, Playlist> playlists;
    private final ArrayList<Integer> playlist_ids = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> sorted_list = new ArrayList<>();
    private final HashMap<Integer, Song> songs;
    private int playlist_category_limit;
    private int heartache_category_limit;
    private int roadtrip_category_limit;
    private int blissful_category_limit;
    private final Comparator<Song> HeartacheComparator = Comparator.comparingInt(Song::getHeartacheScore);
    private final Comparator<Song> RoadtripComparator = Comparator.comparingInt(Song::getRoadtripScore);
    private final Comparator<Song> BlissfulComparator = Comparator.comparingInt(Song::getBlissfulScore);
    private final Comparator<Song> nameComparator = Comparator.comparing(Song::getSongName);
    private final MaxHeap<Song> epicblend_candidate_heartache_songs = new MaxHeap<>(HeartacheComparator.reversed(), nameComparator);
    private final MaxHeap<Song> epicblend_candidate_roadtrip_songs = new MaxHeap<>(RoadtripComparator.reversed(), nameComparator);
    private final MaxHeap<Song> epicblend_candidate_blissful_songs = new MaxHeap<>(BlissfulComparator.reversed(), nameComparator);
    private final MaxHeap<Song> epicblend_initial_candidate_heartache_songs = new MaxHeap<>(HeartacheComparator.reversed(), nameComparator);
    private final MaxHeap<Song> epicblend_initial_candidate_roadtrip_songs = new MaxHeap<>(RoadtripComparator.reversed(), nameComparator);
    private final MaxHeap<Song> epicblend_initial_candidate_blissful_songs = new MaxHeap<>(BlissfulComparator.reversed(), nameComparator);
    private final MinHeap<Song> epicblend_heartache_songs = new MinHeap<>(HeartacheComparator.reversed(), nameComparator);
    private final MinHeap<Song> epicblend_roadtrip_songs = new MinHeap<>(RoadtripComparator.reversed(), nameComparator);
    private final MinHeap<Song> epicblend_blissful_songs = new MinHeap<>(BlissfulComparator.reversed(), nameComparator);
    private final MaxHeap<Song> playlists_max_heartache_candidates = new MaxHeap<>(HeartacheComparator.reversed(), nameComparator);
    private final MaxHeap<Song> playlists_max_roadtrip_candidates = new MaxHeap<>(RoadtripComparator.reversed(), nameComparator);
    private final MaxHeap<Song> playlists_max_blissful_candidates = new MaxHeap<>(BlissfulComparator.reversed(), nameComparator);
    private int current_num_of_heartache_songs;
    private int current_num_of_roadtrip_songs;
    private int current_num_of_blissful_songs;
    public ArrayList<Integer> heartache_log = new ArrayList<>();
    public ArrayList<Integer> roadtrip_log = new ArrayList<>();
    public ArrayList<Integer> blissful_log = new ArrayList<>();
    private int max_playcount;
    private int min_playcount;
    private boolean first_ask;

    public Blend() {
        playlists = new HashMap<>();
        songs = new HashMap<>();
        this.playlist_category_limit = 0;
        this.heartache_category_limit = 0;
        this.roadtrip_category_limit = 0;
        this.blissful_category_limit = 0;
        this.current_num_of_heartache_songs = 0;
        this.current_num_of_roadtrip_songs = 0;
        this.current_num_of_blissful_songs = 0;
        this.first_ask = true;
    }

    // to assign parameters given in the second input file
    public void setLimits(int playlist_category_limit, int heartache_category_limit, int roadtrip_category_limit, int blissful_category_limit) {
        this.playlist_category_limit = playlist_category_limit;
        this.heartache_category_limit = heartache_category_limit;
        this.roadtrip_category_limit = roadtrip_category_limit;
        this.blissful_category_limit = blissful_category_limit;
    }

    // playlist objects are stored in hashtable and their ids are stored in an arraylist
    public void addPlaylist(Playlist playlist, int playlist_id) {
        playlist_ids.add(playlist_id);
        playlists.put(playlist_id, playlist);
    }

    // to assign initial available candidades to epicblend
    private void addSongToMaxCandidate(){
        for (Integer playlist_Id : playlist_ids) {
            if (playlists.get(playlist_Id).current_heartache_contribution < playlist_category_limit) {
                if (playlists.get(playlist_Id).getMaxCandidateHeartache() != null) {
                    playlists_max_heartache_candidates.insert(playlists.get(playlist_Id).getMaxCandidateHeartache());

                }
            }
            if (playlists.get(playlist_Id).current_roadtrip_contribution < playlist_category_limit) {
                if (playlists.get(playlist_Id).getMaxCandidateRoadtrip() != null) {
                    playlists_max_roadtrip_candidates.insert(playlists.get(playlist_Id).getMaxCandidateRoadtrip());
                }
            }
            if (playlists.get(playlist_Id).current_blissful_contribution < playlist_category_limit) {
                if (playlists.get(playlist_Id).getMaxCandidateBlissful() != null) {
                    playlists_max_blissful_candidates.insert(playlists.get(playlist_Id).getMaxCandidateBlissful());
                }
            }
        }
    }

    // whole songs (in the first input file) are stored in a hashtable
    public void addSonginitially(Song song) {
        songs.put(song.getId(), song);
    }

    // songs in the playlist initially are added to maxHeap
    public void secondaddSong(Song song) {
        epicblend_initial_candidate_heartache_songs.insert(song);
        epicblend_initial_candidate_roadtrip_songs.insert(song);
        epicblend_initial_candidate_blissful_songs.insert(song);
    }

    // initial cration of epicblend, and candidates of epicblend are added to maxHeap
    public void creation_of_first_playlists(){
        while(!epicblend_initial_candidate_heartache_songs.isEmpty()){
            Song candidate = epicblend_initial_candidate_heartache_songs.extractMax();
            if (current_num_of_heartache_songs < heartache_category_limit && playlists.get(candidate.getPlaylistId()).current_heartache_contribution < playlist_category_limit){
                epicblend_heartache_songs.insert(candidate);
                playlists.get(candidate.getPlaylistId()).epicblend_heartache_added(candidate);
                current_num_of_heartache_songs++;
                candidate.makeHeartache(true);
            }else{
                epicblend_candidate_heartache_songs.insert(candidate);
                playlists.get(candidate.getPlaylistId()).candidate_heartache_added(candidate);
            }
        }
        while(!epicblend_initial_candidate_roadtrip_songs.isEmpty()){
            Song candidate = epicblend_initial_candidate_roadtrip_songs.extractMax();
            if (current_num_of_roadtrip_songs < roadtrip_category_limit && playlists.get(candidate.getPlaylistId()).current_roadtrip_contribution < playlist_category_limit){
                epicblend_roadtrip_songs.insert(candidate);
                playlists.get(candidate.getPlaylistId()).epicblend_roadtrip_added(candidate);
                current_num_of_roadtrip_songs++;
                candidate.makeRoadtrip(true);
            }else{
                epicblend_candidate_roadtrip_songs.insert(candidate);
                playlists.get(candidate.getPlaylistId()).candidate_roadtrip_added(candidate);
            }
        }
        while(!epicblend_initial_candidate_blissful_songs.isEmpty()){
            Song candidate = epicblend_initial_candidate_blissful_songs.extractMax();
            if (current_num_of_blissful_songs < blissful_category_limit && playlists.get(candidate.getPlaylistId()).current_blissful_contribution < playlist_category_limit){
                epicblend_blissful_songs.insert(candidate);
                playlists.get(candidate.getPlaylistId()).epicblend_blissful_added(candidate);
                current_num_of_blissful_songs++;
                candidate.makeBlissful(true);
            }else{
                epicblend_candidate_blissful_songs.insert(candidate);
                playlists.get(candidate.getPlaylistId()).candidate_blissful_added(candidate);
            }
        }
        this.addSongToMaxCandidate();
    }

    // ADD Event: depending on the cases (blend limits and playlist contribution limits), songs are added to epicblend and candidates are updated
    public void addSong(Song song){
        if (current_num_of_heartache_songs < heartache_category_limit && playlists.get(song.getPlaylistId()).current_heartache_contribution < playlist_category_limit){
            song.makeHeartache(true);
            epicblend_heartache_songs.insert(song);
            playlists.get(song.getPlaylistId()).epicblend_heartache_added(song);
            if (playlists.get(song.getPlaylistId()).current_heartache_contribution == playlist_category_limit && playlists.get(song.getPlaylistId()).getMaxCandidateHeartache() != null){
                playlists_max_heartache_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
            }
            current_num_of_heartache_songs++;
            heartache_log.add(song.getId());
            heartache_log.add(0);
        }
        else if (current_num_of_heartache_songs == heartache_category_limit && playlists.get(song.getPlaylistId()).current_heartache_contribution < playlist_category_limit){
            if ((song.getHeartacheScore() > epicblend_heartache_songs.getMin().getHeartacheScore()) || (song.getHeartacheScore() == epicblend_heartache_songs.getMin().getHeartacheScore() && song.getSongName().compareTo(epicblend_heartache_songs.getMin().getSongName()) < 0)){
                Song removed = epicblend_heartache_songs.extractMin();
                playlists.get(removed.getPlaylistId()).extractMinCurrentHeartache();
                song.makeHeartache(true);
                removed.makeHeartache(false);
                epicblend_heartache_songs.insert(song);
                playlists.get(song.getPlaylistId()).epicblend_heartache_added(song);
                if (playlists.get(removed.getPlaylistId()).getMaxCandidateHeartache() != null && !playlists_max_heartache_candidates.isEmpty()){
                    playlists_max_heartache_candidates.remove(playlists.get(removed.getPlaylistId()).getMaxCandidateHeartache());
                }
                playlists.get(removed.getPlaylistId()).candidate_heartache_added(removed);
                if (playlists.get(removed.getPlaylistId()).getMaxCandidateHeartache() != null){
                    playlists_max_heartache_candidates.insert(playlists.get(removed.getPlaylistId()).getMaxCandidateHeartache());
                }
                if ((playlists.get(song.getPlaylistId()).current_heartache_contribution == playlist_category_limit) && (playlists.get(song.getPlaylistId()).getMaxCandidateHeartache() != null)){
                    playlists_max_heartache_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
                }
                heartache_log.add(song.getId());
                heartache_log.add(removed.getId());
            }else{
                if (playlists.get(song.getPlaylistId()).getMaxCandidateHeartache() != null){
                    if (song.getHeartacheScore() > playlists.get(song.getPlaylistId()).getMaxCandidateHeartache().getHeartacheScore() || (song.getHeartacheScore() == playlists.get(song.getPlaylistId()).getMaxCandidateHeartache().getHeartacheScore() && song.getSongName().compareTo(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache().getSongName()) < 0)){
                        playlists_max_heartache_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
                        playlists.get(song.getPlaylistId()).candidate_heartache_added(song);
                        playlists_max_heartache_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
                    }else{
                        playlists.get(song.getPlaylistId()).candidate_heartache_added(song);
                    }
                }else {
                    playlists.get(song.getPlaylistId()).candidate_heartache_added(song);
                    if (playlists.get(song.getPlaylistId()).current_heartache_contribution < playlist_category_limit){
                        playlists_max_heartache_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
                    }
                }
            }
        }else{
            if((playlists.get(song.getPlaylistId()).getMinCurrentHeartache().getHeartacheScore() < song.getHeartacheScore()) || ((playlists.get(song.getPlaylistId()).getMinCurrentHeartache().getHeartacheScore() == song.getHeartacheScore()) && song.getSongName().compareTo(playlists.get(song.getPlaylistId()).getMinCurrentHeartache().getSongName()) < 0)){
                Song removed = playlists.get(song.getPlaylistId()).extractMinCurrentHeartache();
                epicblend_heartache_songs.remove(removed);
                playlists.get(song.getPlaylistId()).candidate_heartache_added(removed);
                epicblend_heartache_songs.insert(song);
                playlists.get(song.getPlaylistId()).epicblend_heartache_added(song);
                song.makeHeartache(true);
                removed.makeHeartache(false);
                heartache_log.add(song.getId());
                heartache_log.add(removed.getId());
            }else {
                playlists.get(song.getPlaylistId()).candidate_heartache_added(song);
            }
        }
        if (current_num_of_roadtrip_songs < roadtrip_category_limit && playlists.get(song.getPlaylistId()).current_roadtrip_contribution < playlist_category_limit){
            song.makeRoadtrip(true);
            epicblend_roadtrip_songs.insert(song);
            playlists.get(song.getPlaylistId()).epicblend_roadtrip_added(song);
            if (playlists.get(song.getPlaylistId()).current_roadtrip_contribution == playlist_category_limit && playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                playlists_max_roadtrip_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
            }
            current_num_of_roadtrip_songs++;
            roadtrip_log.add(song.getId());
            roadtrip_log.add(0);
        }
        else if (current_num_of_roadtrip_songs == roadtrip_category_limit && playlists.get(song.getPlaylistId()).current_roadtrip_contribution < playlist_category_limit){
            if ((song.getRoadtripScore() > epicblend_roadtrip_songs.getMin().getRoadtripScore()) || (song.getRoadtripScore() == epicblend_roadtrip_songs.getMin().getRoadtripScore() && song.getSongName().compareTo(epicblend_roadtrip_songs.getMin().getSongName()) < 0)){
                Song removed = epicblend_roadtrip_songs.extractMin();
                playlists.get(removed.getPlaylistId()).extractMinCurrentRoadtrip();
                song.makeRoadtrip(true);
                removed.makeRoadtrip(false);
                epicblend_roadtrip_songs.insert(song);
                playlists.get(song.getPlaylistId()).epicblend_roadtrip_added(song);
                if (playlists.get(removed.getPlaylistId()).getMaxCandidateRoadtrip() != null && !playlists_max_roadtrip_candidates.isEmpty()){
                    playlists_max_roadtrip_candidates.remove(playlists.get(removed.getPlaylistId()).getMaxCandidateRoadtrip());
                }
                playlists.get(removed.getPlaylistId()).candidate_roadtrip_added(removed);
                if (playlists.get(removed.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                    playlists_max_roadtrip_candidates.insert(playlists.get(removed.getPlaylistId()).getMaxCandidateRoadtrip());
                }
                if (playlists.get(song.getPlaylistId()).current_roadtrip_contribution == playlist_category_limit && playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                    playlists_max_roadtrip_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
                }
                roadtrip_log.add(song.getId());
                roadtrip_log.add(removed.getId());
            }else{
                if (playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                    if (song.getRoadtripScore() > playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip().getRoadtripScore() || (song.getRoadtripScore() == playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip().getRoadtripScore() && song.getSongName().compareTo(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip().getSongName()) < 0)){
                        playlists_max_roadtrip_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
                        playlists.get(song.getPlaylistId()).candidate_roadtrip_added(song);
                        playlists_max_roadtrip_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
                    }else{
                        playlists.get(song.getPlaylistId()).candidate_roadtrip_added(song);
                    }
                }else {
                    playlists.get(song.getPlaylistId()).candidate_roadtrip_added(song);
                    if (playlists.get(song.getPlaylistId()).current_roadtrip_contribution < playlist_category_limit){
                        playlists_max_roadtrip_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
                    }
                }
            }
        }else{
            if((playlists.get(song.getPlaylistId()).getMinCurrentRoadtrip().getRoadtripScore() < song.getRoadtripScore()) || ((playlists.get(song.getPlaylistId()).getMinCurrentRoadtrip().getRoadtripScore() == song.getRoadtripScore()) && song.getSongName().compareTo(playlists.get(song.getPlaylistId()).getMinCurrentRoadtrip().getSongName()) < 0)){
                Song removed = playlists.get(song.getPlaylistId()).extractMinCurrentRoadtrip();
                epicblend_roadtrip_songs.remove(removed);
                playlists.get(song.getPlaylistId()).candidate_roadtrip_added(removed);
                epicblend_roadtrip_songs.insert(song);
                playlists.get(song.getPlaylistId()).epicblend_roadtrip_added(song);
                song.makeRoadtrip(true);
                removed.makeRoadtrip(false);
                roadtrip_log.add(song.getId());
                roadtrip_log.add(removed.getId());
            }else {
                playlists.get(song.getPlaylistId()).candidate_roadtrip_added(song);
            }
        }
        if (current_num_of_blissful_songs < blissful_category_limit && playlists.get(song.getPlaylistId()).current_blissful_contribution < playlist_category_limit){
            song.makeBlissful(true);
            epicblend_blissful_songs.insert(song);
            playlists.get(song.getPlaylistId()).epicblend_blissful_added(song);
            if (playlists.get(song.getPlaylistId()).current_blissful_contribution == playlist_category_limit && playlists.get(song.getPlaylistId()).getMaxCandidateBlissful() != null){
                playlists_max_blissful_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
            }
            current_num_of_blissful_songs++;
            blissful_log.add(song.getId());
            blissful_log.add(0);
        }
        else if (current_num_of_blissful_songs == blissful_category_limit && playlists.get(song.getPlaylistId()).current_blissful_contribution < playlist_category_limit){
            if ((song.getBlissfulScore() > epicblend_blissful_songs.getMin().getBlissfulScore()) || (song.getBlissfulScore() == epicblend_blissful_songs.getMin().getBlissfulScore() && song.getSongName().compareTo(epicblend_blissful_songs.getMin().getSongName()) < 0)){
                Song removed = epicblend_blissful_songs.extractMin();
                playlists.get(removed.getPlaylistId()).extractMinCurrentBlissful();
                song.makeBlissful(true);
                removed.makeBlissful(false);
                epicblend_blissful_songs.insert(song);
                playlists.get(song.getPlaylistId()).epicblend_blissful_added(song);
                if (playlists.get(removed.getPlaylistId()).getMaxCandidateBlissful() != null && !playlists_max_blissful_candidates.isEmpty()){
                    playlists_max_blissful_candidates.remove(playlists.get(removed.getPlaylistId()).getMaxCandidateBlissful());
                }
                playlists.get(removed.getPlaylistId()).candidate_blissful_added(removed);
                if (playlists.get(removed.getPlaylistId()).getMaxCandidateBlissful() != null){
                    playlists_max_blissful_candidates.insert(playlists.get(removed.getPlaylistId()).getMaxCandidateBlissful());
                }
                if (playlists.get(song.getPlaylistId()).current_blissful_contribution == playlist_category_limit && playlists.get(song.getPlaylistId()).getMaxCandidateBlissful() != null){
                    playlists_max_blissful_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
                }
                blissful_log.add(song.getId());
                blissful_log.add(removed.getId());
            }else{
                if (playlists.get(song.getPlaylistId()).getMaxCandidateBlissful() != null){
                    if (song.getBlissfulScore() > playlists.get(song.getPlaylistId()).getMaxCandidateBlissful().getBlissfulScore() || (song.getBlissfulScore() == playlists.get(song.getPlaylistId()).getMaxCandidateBlissful().getBlissfulScore() && song.getSongName().compareTo(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful().getSongName()) < 0)){
                        playlists_max_blissful_candidates.remove(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
                        playlists.get(song.getPlaylistId()).candidate_blissful_added(song);
                        playlists_max_blissful_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
                    }else{
                        playlists.get(song.getPlaylistId()).candidate_blissful_added(song);
                    }
                }else {
                    playlists.get(song.getPlaylistId()).candidate_blissful_added(song);
                    if (playlists.get(song.getPlaylistId()).current_blissful_contribution < playlist_category_limit){
                        playlists_max_blissful_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
                    }
                }
            }
        }else{
            if((playlists.get(song.getPlaylistId()).getMinCurrentBlissful().getBlissfulScore() < song.getBlissfulScore()) || ((playlists.get(song.getPlaylistId()).getMinCurrentBlissful().getBlissfulScore() == song.getBlissfulScore()) && song.getSongName().compareTo(playlists.get(song.getPlaylistId()).getMinCurrentBlissful().getSongName()) < 0)){
                Song removed = playlists.get(song.getPlaylistId()).extractMinCurrentBlissful();
                epicblend_blissful_songs.remove(removed);
                playlists.get(song.getPlaylistId()).candidate_blissful_added(removed);
                epicblend_blissful_songs.insert(song);
                playlists.get(song.getPlaylistId()).epicblend_blissful_added(song);
                song.makeBlissful(true);
                removed.makeBlissful(false);
                blissful_log.add(song.getId());
                blissful_log.add(removed.getId());
            }else {
                playlists.get(song.getPlaylistId()).candidate_blissful_added(song);
            }
        }

    }

    // log function to write the output
    public String getLog() {
        String addition_string = "";
        String removal_string = "";
        if (heartache_log.isEmpty()){
            heartache_log.add(0);
            heartache_log.add(0);
        }
        if (roadtrip_log.isEmpty()){
            roadtrip_log.add(0);
            roadtrip_log.add(0);
        }
        if (blissful_log.isEmpty()){
            blissful_log.add(0);
            blissful_log.add(0);
        }
        addition_string += String.valueOf(heartache_log.get(0));
        addition_string += " ";
        addition_string += String.valueOf(roadtrip_log.get(0));
        addition_string += " ";
        addition_string += String.valueOf(blissful_log.get(0));
        removal_string += String.valueOf(heartache_log.get(1));
        removal_string += " ";
        removal_string += String.valueOf(roadtrip_log.get(1));
        removal_string += " ";
        removal_string += String.valueOf(blissful_log.get(1));

        String result = addition_string + "\n" + removal_string;
        heartache_log.clear();
        roadtrip_log.clear();
        blissful_log.clear();
        return result;
    }

    // to get a song by its id from the hashtable of songs (in the first input file)
    public Song getSongById(int songId) {
        return songs.get(songId);
    }

    // REM Event: depending on the cases (blend limits and playlist contribution limits and current status of the song),
    // songs are removed from epicblend and candidates are updated
    public void removeSong(Song song){
        if (song.isheartache) {
            epicblend_heartache_songs.remove(song);
            playlists.get(song.getPlaylistId()).epicblend_heartache_removed(song);
            if (playlists.get(song.getPlaylistId()).current_heartache_contribution == (playlist_category_limit - 1) && playlists.get(song.getPlaylistId()).getMaxCandidateHeartache() != null){
                playlists_max_heartache_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
            }
            song.makeHeartache(false);
            current_num_of_heartache_songs--;
            if (!playlists_max_heartache_candidates.isEmpty()){
                Song current_candidate = playlists_max_heartache_candidates.extractMax();
                playlists.get(current_candidate.getPlaylistId()).candidate_heartache_removed(current_candidate);
                current_candidate.makeHeartache(true);
                epicblend_heartache_songs.insert(current_candidate);
                playlists.get(current_candidate.getPlaylistId()).epicblend_heartache_added(current_candidate);
                current_num_of_heartache_songs++;
                heartache_log.add(current_candidate.getId());
                heartache_log.add(song.getId());
                if (playlists.get(current_candidate.getPlaylistId()).current_heartache_contribution < playlist_category_limit && playlists.get(current_candidate.getPlaylistId()).getMaxCandidateHeartache() != null){
                    playlists_max_heartache_candidates.insert(playlists.get(current_candidate.getPlaylistId()).getMaxCandidateHeartache());
                }
            }
            else{
                heartache_log.add(0);
                heartache_log.add(song.getId());
            }
        }else {
            if(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache() == song && playlists.get(song.getPlaylistId()).current_heartache_contribution < playlist_category_limit){
                playlists_max_heartache_candidates.remove(song);
                playlists.get(song.getPlaylistId()).candidate_heartache_removed(song);
                if (playlists.get(song.getPlaylistId()).getMaxCandidateHeartache() != null){
                    playlists_max_heartache_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateHeartache());
                }
            }else{
            playlists.get(song.getPlaylistId()).candidate_heartache_removed(song);
            }
        }
        if (song.isroadtrip) {
            epicblend_roadtrip_songs.remove(song);
            playlists.get(song.getPlaylistId()).epicblend_roadtrip_removed(song);
            if (playlists.get(song.getPlaylistId()).current_roadtrip_contribution == (playlist_category_limit - 1) && playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                playlists_max_roadtrip_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
            }
            song.makeRoadtrip(false);
            current_num_of_roadtrip_songs--;
            if (!playlists_max_roadtrip_candidates.isEmpty()){
                Song current_candidate = playlists_max_roadtrip_candidates.extractMax();
                playlists.get(current_candidate.getPlaylistId()).candidate_roadtrip_removed(current_candidate);
                current_candidate.makeRoadtrip(true);
                epicblend_roadtrip_songs.insert(current_candidate);
                playlists.get(current_candidate.getPlaylistId()).epicblend_roadtrip_added(current_candidate);
                current_num_of_roadtrip_songs++;
                roadtrip_log.add(current_candidate.getId());
                roadtrip_log.add(song.getId());
                if (playlists.get(current_candidate.getPlaylistId()).current_roadtrip_contribution < playlist_category_limit && playlists.get(current_candidate.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                    playlists_max_roadtrip_candidates.insert(playlists.get(current_candidate.getPlaylistId()).getMaxCandidateRoadtrip());
                }
            }
            else{
                roadtrip_log.add(0);
                roadtrip_log.add(song.getId());
            }
        }else {
            if(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip() == song && playlists.get(song.getPlaylistId()).current_roadtrip_contribution < playlist_category_limit){
                playlists_max_roadtrip_candidates.remove(song);
                playlists.get(song.getPlaylistId()).candidate_roadtrip_removed(song);
                if (playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip() != null){
                    playlists_max_roadtrip_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateRoadtrip());
                }
            }else{
            playlists.get(song.getPlaylistId()).candidate_roadtrip_removed(song);
            }
        }
        if (song.isblissful) {
            epicblend_blissful_songs.remove(song);
            playlists.get(song.getPlaylistId()).epicblend_blissful_removed(song);
            if (playlists.get(song.getPlaylistId()).current_blissful_contribution == (playlist_category_limit - 1) && playlists.get(song.getPlaylistId()).getMaxCandidateBlissful() != null){
                playlists_max_blissful_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
            }
            song.makeBlissful(false);
            current_num_of_blissful_songs--;
            if (!playlists_max_blissful_candidates.isEmpty()){
                Song current_candidate = playlists_max_blissful_candidates.extractMax();
                playlists.get(current_candidate.getPlaylistId()).candidate_blissful_removed(current_candidate);
                current_candidate.makeBlissful(true);
                epicblend_blissful_songs.insert(current_candidate);
                playlists.get(current_candidate.getPlaylistId()).epicblend_blissful_added(current_candidate);
                current_num_of_blissful_songs++;
                blissful_log.add(current_candidate.getId());
                blissful_log.add(song.getId());
                if (playlists.get(current_candidate.getPlaylistId()).current_blissful_contribution < playlist_category_limit && playlists.get(current_candidate.getPlaylistId()).getMaxCandidateBlissful() != null){
                    playlists_max_blissful_candidates.insert(playlists.get(current_candidate.getPlaylistId()).getMaxCandidateBlissful());
                }
            }
            else{
                blissful_log.add(0);
                blissful_log.add(song.getId());
            }
        }else {
            if(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful() == song && playlists.get(song.getPlaylistId()).current_blissful_contribution < playlist_category_limit){
                playlists_max_blissful_candidates.remove(song);
                playlists.get(song.getPlaylistId()).candidate_blissful_removed(song);
                if (playlists.get(song.getPlaylistId()).getMaxCandidateBlissful() != null){
                    playlists_max_blissful_candidates.insert(playlists.get(song.getPlaylistId()).getMaxCandidateBlissful());
                }
            }else{
            playlists.get(song.getPlaylistId()).candidate_blissful_removed(song);
            }
        }
    }

    // by using max and min playcount values, an arraylist of arraylists is created to store songs in sorted order
    public void setPlaycountlimits(int min_play_count, int max_play_count) {
        max_playcount = max_play_count;
        min_playcount = min_play_count;
        for (int i = 0; i <= max_play_count - min_play_count + 1; i++) {
            sorted_list.add(new ArrayList<>());
        }
    }
    // sorting songs by playcount and song name by using the playcount limits and playcount as an index
    public String ask() {
        if (!first_ask){
            for (int i = 0; i <= max_playcount - min_playcount + 1; i++) {
                sorted_list.get(i).clear();
            }
        }
        first_ask = false;
        ArrayList<Integer> heartache_IDs = epicblend_heartache_songs.getIds();
        ArrayList<Integer> roadtrip_IDs = epicblend_roadtrip_songs.getIds();
        ArrayList<Integer> blissful_IDs = epicblend_blissful_songs.getIds();
        for (Integer current_id : heartache_IDs) {
            if (sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).isEmpty()) {
                sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(current_id);
            } else {
                for (int j = 0; j < sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).size(); j++) {
                    if (songs.get(sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).get(j)).getSongName().compareTo(songs.get(current_id).getSongName()) > 0) {
                        sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(j, current_id);
                        break;
                    } else if (songs.get(sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).get(j)).getSongName().compareTo(songs.get(current_id).getSongName()) == 0) {
                        break;
                    } else if (j == sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).size() - 1) {
                        sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(current_id);
                        break;
                    }
                }
            }
        }
        for (Integer current_id : roadtrip_IDs) {
            if (sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).isEmpty()) {
                sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(current_id);
            } else {
                for (int j = 0; j < sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).size(); j++) {
                    if (songs.get(sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).get(j)).getSongName().compareTo(songs.get(current_id).getSongName()) > 0) {
                        sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(j, current_id);
                        break;
                    } else if (songs.get(sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).get(j)).getSongName().compareTo(songs.get(current_id).getSongName()) == 0) {
                        break;
                    }else if (j == sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).size() - 1){
                        sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(current_id);
                        break;
                    }
                }
            }
        }
        for (Integer current_id : blissful_IDs) {
            if (sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).isEmpty()) {
                sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(current_id);
            } else {
                for (int j = 0; j < sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).size(); j++) {
                    if (songs.get(sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).get(j)).getSongName().compareTo(songs.get(current_id).getSongName()) > 0) {
                        sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(j, current_id);
                        break;
                    } else if (songs.get(sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).get(j)).getSongName().compareTo(songs.get(current_id).getSongName()) == 0) {
                        break;
                    }else if (j == sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).size() - 1){
                        sorted_list.get(songs.get(current_id).getPlayCount() - min_playcount).add(current_id);
                        break;
                    }
                }
            }
        }
        StringBuilder answer = new StringBuilder();
        for (int i = sorted_list.size() - 1; i >= 0; i--) {
            //normal order
            if (!sorted_list.get(i).isEmpty()) {
                for (int j = 0; j < sorted_list.get(i).size(); j++) {
                    answer.append(sorted_list.get(i).get(j));
                    answer.append(" ");
                }
            }
        }

        answer.deleteCharAt(answer.length() - 1);
        return answer.toString();
    }
}