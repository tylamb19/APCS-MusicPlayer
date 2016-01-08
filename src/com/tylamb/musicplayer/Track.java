/**
 * Fetches underlying details of MP3 files
 * @author Tyler Lamb
 * @version 1.0
 */
package com.tylamb.musicplayer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;

/**
 * The Class Track.
 */
public class Track {
	
	/** The location. */
	private File location;
	
	/** The audio file. */
	private AudioFile audioFile;
	
	/** The genres. */
	static ArrayList<String> genres = new ArrayList<String>();
	
	/**
	 * Instantiates a new track.
	 *
	 * @param file the file
	 */
	Track(String file)
	{
		initializeGenres();
		location = new File(file);
		try {
			audioFile = AudioFileIO.read(location);
		} catch (CannotReadException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize genres.
	 */
	private void initializeGenres()
	{
		String[] genreStrings = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop","Jazz", "Metal", "New Age", 
				"Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock","Techno", "Industrial", "Alternative", 
				"Ska", "Death Metal", "Pranks", "Soundtrack","Euro-Techno", "Ambient", "Trip-Hop", "Vocal", 
				"Jazz+Funk", "Fusion", "Trance","Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", 
				"Gospel", "Noise","Alt. Rock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop",
				"Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial","Electronic", "Pop-Folk", 
				"Eurodance", "Dream", "Southern Rock", "Comedy", "Cult","Gangsta Rap", "Top 40", "Christian Rap", 
				"Pop/Funk", "Jungle", "Native American","Cabaret", "New Wave", "Psychedelic", "Rave", "Showtunes", 
				"Trailer", "Lo-Fi", "Tribal","Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", 
				"Hard Rock","Folk", "Folk/Rock", "National Folk", "Swing", "Fast-Fusion", "Bebob", "Latin", 
				"Revival","Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock",
				"Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour","Speech", 
				"Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus","Porn Groove", 
				"Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore","Ballad", "Power Ballad", "Rhythmic Soul", 
				"Freestyle", "Duet", "Punk Rock", "Drum Solo","A Cappella", "Euro-House", "Dance Hall", "Goa", 
				"Drum & Bass", "Club-House","Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", 
				"Beat","Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian",
				"Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop",};
		genres.addAll(Arrays.asList(genreStrings));
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle()
	{
		getTrack();
		return audioFile.getTag().getTitle().toString().replace("[", "").replace("]", "");
	}
	
	/**
	 * Gets the time.
	 *
	 * @return the time
	 * @throws CannotReadException the cannot read exception
	 */
	public int getTime() throws CannotReadException
	{
		audioFile = AudioFileIO.read(location);
		return audioFile.getLength();
	}
	
	/**
	 * Gets the artist.
	 *
	 * @return the artist
	 */
	public String getArtist()
	{
		getTrack();
		String artistName = audioFile.getTag().getArtist().toString().replace("[", "").replace("]", "");
		if(artistName.isEmpty()) return "FAILED";
		return artistName;
	}
	
	/**
	 * Gets the album.
	 *
	 * @return the album
	 */
	public String getAlbum()
	{
		getTrack();
		String albumName = audioFile.getTag().getAlbum().toString().replace("[", "").replace("]", "");
		if(albumName.isEmpty()) return "FAILED";
		return albumName;
	}
	
	/**
	 * Gets the genre.
	 *
	 * @return the genre
	 */
	public String getGenre()
	{
		getTrack();
		String genreList = audioFile.getTag().getGenre().toString();
		genreList = genreList.replaceAll("[^0-9]","");
		if(genreList.isEmpty()) return "FAILED";
		int g = Integer.parseInt(genreList);
		return matchGenre(g);
	}
	
	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName()
	{
		getTrack();
		return location.toString().replace("[", "").replace("]", "");
	}
	
	/**
	 * Find genre number.
	 *
	 * @param genre the genre
	 * @return the int
	 */
	public int findGenreNumber(String genre)
	{
		return genres.indexOf(genre);
	}
	
	/**
	 * Match genre.
	 *
	 * @param genre the genre
	 * @return the string
	 */
	private String matchGenre(int genre)
	{
		return genres.get(genre);
	}
	
	/**
	 * Gets the track.
	 *
	 * @return the track
	 */
	private void getTrack()
	{
		try {
			audioFile = AudioFileIO.read(location);
		} catch (CannotReadException e) {
			e.printStackTrace();
		}
	}
}