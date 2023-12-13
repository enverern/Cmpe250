import java.util.Comparator;
public class Playlist{
    private final int playlist_id;
    public MinHeap<Song> heartache_heap_current_epicblend;
    public MinHeap<Song> roadtrip_heap_current_epicblend;
    public MinHeap<Song> blissful_heap_current_epicblend;
    public MaxHeap<Song> heartache_heap_epicblend_candidate;
    public MaxHeap<Song> roadtrip_heap_epicblend_candidate;
    public MaxHeap<Song> blissful_heap_epicblend_candidate;
    public int current_heartache_contribution;
    public int current_roadtrip_contribution;
    public int current_blissful_contribution;
    public int contribution_limit;
    private Song current_max_heartache_candidate;
    private Song current_max_roadtrip_candidate;
    private Song current_max_blissful_candidate;


    public Playlist(int playlist_id, int contribution_limit) {
        this.playlist_id = playlist_id;
        this.contribution_limit = contribution_limit;
        this.current_heartache_contribution = 0;
        this.current_roadtrip_contribution = 0;
        this.current_blissful_contribution = 0;
        Comparator<Song> heartacheComparator = Comparator.comparingLong(Song::getHeartacheScore).reversed();
        Comparator<Song> nameComparator = Comparator.comparing(Song::getSongName);
        Comparator<Song> roadtripComparator = Comparator.comparingLong(Song::getRoadtripScore).reversed();
        Comparator<Song> blissfulComparator = Comparator.comparingLong(Song::getBlissfulScore).reversed();
        this.heartache_heap_current_epicblend = new MinHeap<>(heartacheComparator, nameComparator);
        this.roadtrip_heap_current_epicblend = new MinHeap<>(roadtripComparator, nameComparator);
        this.blissful_heap_current_epicblend = new MinHeap<>(blissfulComparator, nameComparator);
        this.heartache_heap_epicblend_candidate = new MaxHeap<>(heartacheComparator, nameComparator);
        this.roadtrip_heap_epicblend_candidate = new MaxHeap<>(roadtripComparator, nameComparator);
        this.blissful_heap_epicblend_candidate = new MaxHeap<>(blissfulComparator, nameComparator);
        this.current_max_heartache_candidate = null;
        this.current_max_roadtrip_candidate = null;
        this.current_max_blissful_candidate = null;
    }

    public int getId() {
        return playlist_id;
    }

    // when the song is added to the epicblend, it is also stored in the playlist's current epicblend heap
    public void epicblend_heartache_added(Song song){
        heartache_heap_current_epicblend.insert(song);
        current_heartache_contribution++;
    }

    public void epicblend_roadtrip_added(Song song){
        roadtrip_heap_current_epicblend.insert(song);
        current_roadtrip_contribution++;
    }
    public void epicblend_blissful_added(Song song){
        blissful_heap_current_epicblend.insert(song);
        current_blissful_contribution++;
    }
    // when the song is removed from the epicblend, it is also removed from the playlist's current epicblend heap
    public void epicblend_heartache_removed(Song song){
        heartache_heap_current_epicblend.remove(song);
        current_heartache_contribution--;
    }
    public void epicblend_roadtrip_removed(Song song){
        roadtrip_heap_current_epicblend.remove(song);
        current_roadtrip_contribution--;
    }
    public void epicblend_blissful_removed(Song song){
        blissful_heap_current_epicblend.remove(song);
        current_blissful_contribution--;
    }

    // returns the worst(min) candidate song for the epicblend from the playlist's current epicblend heap
    public Song getMinCurrentHeartache(){
        return heartache_heap_current_epicblend.getMin();
    }
    public Song getMinCurrentRoadtrip(){
        return roadtrip_heap_current_epicblend.getMin();
    }
    public Song getMinCurrentBlissful(){
        return blissful_heap_current_epicblend.getMin();
    }

    // extracts the worst(min) candidate song for the epicblend from the playlist's current epicblend heap
    public Song extractMinCurrentHeartache(){
        current_heartache_contribution--;
        return heartache_heap_current_epicblend.extractMin();
    }
    public Song extractMinCurrentRoadtrip(){
        current_roadtrip_contribution--;
        return roadtrip_heap_current_epicblend.extractMin();
    }
    public Song extractMinCurrentBlissful(){
        current_blissful_contribution--;
        return blissful_heap_current_epicblend.extractMin();
    }
    // when a song from playlist is added to the candidates, it is also stored in the playlist's candidate heap
    // current_max_candidate is updated if the new added candidate song is better than the current max candidate
    public void candidate_heartache_added(Song song){
        heartache_heap_epicblend_candidate.insert(song);
        if (current_max_heartache_candidate == null ||(current_max_heartache_candidate.getHeartacheScore() < song.getHeartacheScore() || (current_max_heartache_candidate.getHeartacheScore() == song.getHeartacheScore() && current_max_heartache_candidate.getSongName().compareTo(song.getSongName()) > 0))){
            current_max_heartache_candidate = song;
        }
    }
    public void candidate_roadtrip_added(Song song){
        roadtrip_heap_epicblend_candidate.insert(song);
        if (current_max_roadtrip_candidate == null ||(current_max_roadtrip_candidate.getRoadtripScore() < song.getRoadtripScore() || (current_max_roadtrip_candidate.getRoadtripScore() == song.getRoadtripScore() && current_max_roadtrip_candidate.getSongName().compareTo(song.getSongName()) > 0))){
            current_max_roadtrip_candidate = song;
        }
    }
    public void candidate_blissful_added(Song song){
        blissful_heap_epicblend_candidate.insert(song);
        if (current_max_blissful_candidate == null || (current_max_blissful_candidate.getBlissfulScore() < song.getBlissfulScore() || (current_max_blissful_candidate.getBlissfulScore() == song.getBlissfulScore() && current_max_blissful_candidate.getSongName().compareTo(song.getSongName()) > 0))){
            current_max_blissful_candidate = song;
        }
    }
    // when a song from playlist is removed from the candidates, it is also removed from the playlist's candidate heap
    // current_max_candidate is updated if the removed candidate song is the current max candidate
    public void candidate_heartache_removed(Song song){
        heartache_heap_epicblend_candidate.remove(song);
        if (song == current_max_heartache_candidate){
            current_max_heartache_candidate = null;
            current_max_heartache_candidate = heartache_heap_epicblend_candidate.getMax();
        }
    }
    public void candidate_roadtrip_removed(Song song){
        roadtrip_heap_epicblend_candidate.remove(song);
        if (song == current_max_roadtrip_candidate){
            current_max_roadtrip_candidate = null;
            current_max_roadtrip_candidate = roadtrip_heap_epicblend_candidate.getMax();
        }
    }
    public void candidate_blissful_removed(Song song){
        blissful_heap_epicblend_candidate.remove(song);
        if (song == current_max_blissful_candidate){
            current_max_blissful_candidate = null;
            current_max_blissful_candidate = blissful_heap_epicblend_candidate.getMax();
        }
    }
    // returns the best(max) candidate song for the epicblend from the playlist's candidate heap
    public Song getMaxCandidateHeartache(){
        return current_max_heartache_candidate;
    }
    public Song getMaxCandidateRoadtrip(){
        return current_max_roadtrip_candidate;
    }
    public Song getMaxCandidateBlissful(){
        return current_max_blissful_candidate;
    }
}