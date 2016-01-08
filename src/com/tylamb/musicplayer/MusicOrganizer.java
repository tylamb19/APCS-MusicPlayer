/**
 * Base for organizing and sorting MP3 files
 * @author Tyler Lamb
 * @version 1.0
 */
package com.tylamb.musicplayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;

public class MusicOrganizer {
	File location;
	ArrayList<String> library;
	MusicOrganizer(String libLocation)
	{
		location = new File(libLocation);
		library = new ArrayList<String>(Arrays.asList(location.list()));
	}
	/**
	 * Lists All Tracks (should not be used in GUIs)
	 */
	public void listAllTracks()
	{
		int x = 0;
		while(x<library.size())
		{
			System.out.println(x+1 + ") " + library.get(x));
			x++;
		}
	}
	/**
	 * Adds tracks from directory
	 * @param directory the directory containing the files
	 */
	public void addAllTracks(String directory)
	{
		Path srcDir = Paths.get(directory);
		Path destDir = location.toPath();
		try {
			Files.copy(srcDir, destDir, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e){
		}
		refreshMusic();
	}
	/**
	 * Gets track list from array list
	 * @return ArrayList<String> all tracks
	*/
	public ArrayList<String> getAllTracks()
	{
		return library;
	}
	/**
	 * Sorts tracks by genre
	 * @param genre 	A string that defines the genre
	 * @return 			tracks only of a certain genre
	 */
	public ArrayList<String> getTracksByGenre(String genre)
	{
		int x = 0;
		ArrayList<String> genreTracks = new ArrayList <String>();
		while(x<library.size())
		{
			try {
				AudioFile audioFile = AudioFileIO.read(new File(location + "/" + library.get(x)));
				String songGenre = audioFile.getTag().getGenre().toString();
				if(songGenre.equals("[(" + genre + ")]"))
				{
					genreTracks.add(library.get(x));
				}
			} catch (CannotReadException e) {
				e.printStackTrace();
				System.out.println("I need to debug this...");
			}
			x++;
		}
		return genreTracks;
	}
	/**
	 * Sorts tracks by artist
	 * @param artist A string that defines the artist
	 * @return tracks by only a certain artist
	 */
	public ArrayList<String> getTracksByArtist(String artist)
	{
		int x = 0;
		ArrayList<String> artistTracks = new ArrayList <String>();
		while(x<library.size())
		{
			try {
				AudioFile audioFile = AudioFileIO.read(new File(location + "/" + library.get(x)));
				String songArtist=audioFile.getTag().getArtist().toString();
				if(songArtist.equals("[" + artist + "]"))
				{
					artistTracks.add(library.get(x));
				}
			} catch (CannotReadException e) {
				e.printStackTrace();
			}
			x++;
		}
		return artistTracks;
	}
	/**
	 * Refreshes music list
	 */
	private void refreshMusic(){
		library = new ArrayList<String>(Arrays.asList(location.list()));
	}
	/**
	 * Gets library of songs
	 * @return the library
	 */
	public ArrayList<String> getLibrary()
	{
		return library;
	}
	/**
	 * Gets location of songs
	 * @return location
	 */
	public File getLocation()
	{
		return location;
	}
}
