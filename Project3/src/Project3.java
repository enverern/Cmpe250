import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

public class Project3{
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 3) {
            System.out.println("Usage: java Main <song-file> <test-case-file> <output-file>");
            return;
        }

        String song_file = args[0];
        String test_case_file = args[1];
        String output_file = args[2];

        int min_play_count = 99999;
        int max_play_count = 0;
        Blend epicblend = new Blend();
        try (Scanner scanner = new Scanner(new File(song_file))) {
            int number_of_songs = scanner.nextInt();
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                int song_id = Integer.parseInt(tokens[0]);
                String song_name = tokens[1];
                int play_count = Integer.parseInt(tokens[2]);
                if(play_count > max_play_count){
                    max_play_count = play_count;
                }
                if(play_count < min_play_count){
                    min_play_count = play_count;
                }
                int heartache_score = Integer.parseInt(tokens[3]);
                int roadtrip_score = Integer.parseInt(tokens[4]);
                int blissful_score = Integer.parseInt(tokens[5]);
                Song new_song = new Song(song_id, song_name, play_count, heartache_score, roadtrip_score, blissful_score);
                epicblend.addSonginitially(new_song);
            }
        }catch(FileNotFoundException e) {
            System.out.println("File not found: " + song_file);
            return;
        }
        epicblend.setPlaycountlimits(min_play_count, max_play_count);

        try (Scanner scanner = new Scanner(new File(test_case_file))) {
            // get the input from the first line of the input file
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            int playlist_category_limit = Integer.parseInt(tokens[0]);
            int epicblend_heartache_limit = Integer.parseInt(tokens[1]);
            int epicblend_roadtrip_limit = Integer.parseInt(tokens[2]);
            int epicblend_blissful_limit = Integer.parseInt(tokens[3]);
            epicblend.setLimits(playlist_category_limit, epicblend_heartache_limit, epicblend_roadtrip_limit, epicblend_blissful_limit);
            String num_of_playlists_str = scanner.nextLine();
            int num_of_playlists = Integer.parseInt(num_of_playlists_str);
            java.io.PrintWriter writer = new java.io.PrintWriter(output_file);
            for (int i = 0; i < num_of_playlists; i++) {
                String info_line = scanner.nextLine();
                String[] infos = info_line.split(" ");
                int playlist_id = Integer.parseInt(infos[0]);
                int playlist_song_count = Integer.parseInt(infos[1]);
                Playlist new_playlist = new Playlist(playlist_id, playlist_category_limit);
                String songs_line = scanner.nextLine();
                String[] songs = songs_line.split(" ");
                for (int j = 0; j < playlist_song_count; j++) {
                    int song_id = Integer.parseInt(songs[j]);
                    Song song = epicblend.getSongById(song_id);
                    song.setPlaylistId(playlist_id);
                    epicblend.secondaddSong(song);
                }
                epicblend.addPlaylist(new_playlist, playlist_id);
            }
            epicblend.creation_of_first_playlists();
            String num_of_events_str = scanner.nextLine();
            int num_of_events = Integer.parseInt(num_of_events_str);
            for (int i = 0; i < num_of_events; i++) {
                String event = scanner.nextLine();
                String[] event_tokens = event.split(" ");
                if (event_tokens[0].equals("ADD")) {
                    int song_id = Integer.parseInt(event_tokens[1]);
                    int playlist_id = Integer.parseInt(event_tokens[2]);
                    Song song = epicblend.getSongById(song_id);
                    song.setPlaylistId(playlist_id);
                    epicblend.addSong(song);
                    writer.println(epicblend.getLog());
                } else if (event_tokens[0].equals("REM")) {
                    int song_id = Integer.parseInt(event_tokens[1]);
                    int playlist_id = Integer.parseInt(event_tokens[2]);
                    Song song = epicblend.getSongById(song_id);
                    epicblend.removeSong(song);
                    writer.println(epicblend.getLog());
                } else if (event_tokens[0].equals("ASK")) {
                    String srt_lst = String.valueOf(epicblend.ask());
                    writer.println(srt_lst);
                }
            }
            writer.close();
        }catch(FileNotFoundException e) {
            System.out.println("File not found: " + test_case_file);
        }
    }
}