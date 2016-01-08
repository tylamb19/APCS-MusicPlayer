/**
 * GUI for playing MP3 files
 * @author Tyler Lamb
 * @version 1.0
 */
package com.tylamb.musicplayer;
//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import entagged.audioformats.exceptions.CannotReadException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.event.ActionEvent;

/**
 * The Class MusicPlayerApp.
 */
public class MusicPlayerApp {
	
	/** The organizer. */
	MusicOrganizer organizer;
	
	/** The player. */
	MusicPlayer player;
	
	/** The shuffle boolean. */
	private boolean isShuffle;
	
	/** The previous song. */
	private int previousSong;
	
	/** The playback runnable. */
	private Runnable playbackThread;
	
	/** The first thread */
	private Thread t1;
	
	/** The current track. */
	private Track currentTrack;
	
	/** The song list. */
	private JComboBox<String> songList;
	
	/** The shuffle checkbox. */
	private JCheckBox shuffle;
	
	/** The play button. */
	private JButton playButton;
	
	/** The ffwd button. */
	private JButton ffwdButton;
	
	/** The rwnd button. */
	private JButton rwndButton;
	
	/** The artist button. */
	private JButton artistButton;
	
	/** The genre button. */
	private JButton genreButton;
	
	/** The reset button. */
	private JButton resetButton;
	
	/** The stop button. */
	private JButton stopButton;
	
	/** The add button. */
	private JButton addButton;
	
	/** The delete button. */
	private JButton deleteButton;
	
	/** The change button. */
	private JButton changeButton;
	
	/** The rename button. */
	private JButton renameButton;
	
	/** The details button. */
	private JButton detailsButton;
	
	/** The track name. */
	private JLabel trackName;
	
	/** The artist. */
	private JLabel artist;
	
	/** The album. */
	private JLabel album;
	
	/** The genre. */
	private JLabel genre;
	
	/** The current time. */
	private JLabel currentTime;
	
	/** The duration. */
	private JLabel duration;
	
	/** The play label. */
	private JLabel playLabel;
	
	/** The stop label. */
	private JLabel stopLabel;
	
	/** The fast forward label. */
	private JLabel fastForwardLabel;
	
	/** The rewind label. */
	private JLabel rewindLabel;
	
	/** The file chooser. */
	private JFileChooser fileChooser;
	
	/** The slider. */
	private JSlider slider;
	
	/** The play icon. */
	private ImageIcon playIcon;
	
	/** The stop icon. */
	private ImageIcon stopIcon;
	
	/** The ffwd icon. */
	private ImageIcon ffwdIcon;
	
	/** The rwnd icon. */
	private ImageIcon rwndIcon;
	
	/** The genres. */
	private ArrayList<String> genres;
		
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) 
    {
        new MusicPlayerApp();
    }
    
    /**
     * Instantiates a new music player app.
     */
    public MusicPlayerApp()
    {
    	player = new MusicPlayer();
    	organizer = new MusicOrganizer("./music/");
    	isShuffle = false;
    	initializeGenres();
        JFrame guiFrame = new JFrame();
        //close when the close button is pressed.
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //we need a title
        guiFrame.setTitle("Music Player");
        //we also need a size
        guiFrame.setSize(440,270);
        //just center in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        //All primary variables assigned below
        playIcon = new ImageIcon("./images/Play.gif");
        stopIcon = new ImageIcon("./images/Stop.gif");
        ffwdIcon = new ImageIcon("./images/fastfwd.png");
        rwndIcon = new ImageIcon("./images/rwnd.png");
        songList = new JComboBox<String>();
        addAllToSongList();
        shuffle = new JCheckBox("Shuffle");
        playButton = new JButton();
        artistButton = new JButton("Sort Tracks by Artist");
        genreButton = new JButton("Sort Tracks by Genre");
        resetButton = new JButton("Reset Selections");
        playLabel = new JLabel("     Play");
        stopLabel = new JLabel("     Stop");
        fastForwardLabel = new JLabel("     Skip");
        rewindLabel = new JLabel("     Rewind");
        stopButton = new JButton();
        ffwdButton = new JButton();
        rwndButton = new JButton();
        addButton = new JButton("Add Track to Library");
        deleteButton = new JButton("Delete Selected Track");
        renameButton = new JButton("Rename Selected Track");
        detailsButton = new JButton("Track Details");
        changeButton = new JButton("Change Library Location");
        trackName = new JLabel("Track Name:");
        artist = new JLabel("Artist:");
        album = new JLabel("Album:");
        genre = new JLabel("Genre:");
        currentTime = new JLabel("0:00");
        duration = new JLabel("0:00");
		fileChooser = new JFileChooser();
		slider = new JSlider();
		t1 = new Thread(playbackThread);
		//create list panel
        JPanel listPanel = new JPanel();
        listPanel.add(shuffle);
        listPanel.add(songList);
        //create details panel
        JPanel details = new JPanel();
        details.add(trackName);
        details.add(artist);
        details.add(album);
        details.add(genre);
        //create button panel
        JPanel buttons = new JPanel();
        buttons.add(rewindLabel);
        buttons.add(rwndButton);
        rwndButton.setIcon(rwndIcon);
        buttons.add(playLabel);
        buttons.add(playButton);
        playButton.setIcon(playIcon);
        buttons.add(stopLabel);
        buttons.add(stopButton);
        stopButton.setIcon(stopIcon);
        buttons.add(fastForwardLabel);
        buttons.add(ffwdButton);
        ffwdButton.setIcon(ffwdIcon);
        buttons.add(genreButton);
        buttons.add(artistButton);
        buttons.add(resetButton);
        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(changeButton);
        buttons.add(renameButton);
        buttons.add(detailsButton);
        //create lower panel
        JPanel lowerPanel = new JPanel();
        lowerPanel.add(currentTime);
        lowerPanel.add(slider);
        slider.setEnabled(false);
        lowerPanel.add(duration);
        playButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	currentTrack = new Track(organizer.getLocation() + "/" + organizer.getLibrary().get(organizer.getLibrary().indexOf(songList.getSelectedObjects()[0] + ".mp3")));
				String songArtist = currentTrack.getArtist();
				String songAlbum = currentTrack.getAlbum();
				String songGenre = currentTrack.getGenre();
				if(songAlbum.equals("FAILED") || songArtist.equals("FAILED") || songGenre.equals("FAILED")) JOptionPane.showMessageDialog(guiFrame,"<html>Failed to get one or more ID3 tags from file.<br>This file may be corrupted or incorrectly created.<br>You <b>may</b> still be able to play this file.</html>","Failed to get ID3 tags.",JOptionPane.WARNING_MESSAGE);
            	player.startPlaying(organizer.getLocation() + "/" + organizer.getLibrary().get(organizer.getLibrary().indexOf(songList.getSelectedObjects()[0] + ".mp3")));
            	if(t1.isAlive())
            	{
            		t1.interrupt();
            	}
            	
            	t1 = new Thread(playbackThread);
            	t1.start();
            }
        });
        detailsButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
				Track selectedTrack = new Track(organizer.getLocation() + "/" + organizer.getLibrary().get(organizer.getLibrary().indexOf(songList.getSelectedObjects()[0] + ".mp3")));
				String songArtist = selectedTrack.getArtist();
				String songAlbum = selectedTrack.getAlbum();
				String songGenre = selectedTrack.getGenre();
				if(songAlbum.equals("FAILED") || songArtist.equals("FAILED") || songGenre.equals("FAILED")) JOptionPane.showMessageDialog(guiFrame,"<html>Failed to get one or more ID3 tags from file.<br>This file may be corrupted or incorrectly created.<br>You <b>may</b> still be able to play this file.</html>","Failed to get ID3 tags.",JOptionPane.WARNING_MESSAGE);
				JOptionPane.showMessageDialog(guiFrame, "<html><b>Track Name:</b><br>" + songList.getSelectedObjects()[0] + ".mp3" + "<br><b>Artist:</b><br>" + songArtist + "<br><b>Album:</b><br>" + songAlbum + "<br><b>Genre:</b><br>" + songGenre + "</html>","Track Details",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        stopButton.addActionListener(new ActionListener()
        {
            @Override
           
            public void actionPerformed(ActionEvent event)
            {
				previousSong = songList.getSelectedIndex();
            	player.stop();
        		t1.interrupt();
           }
        });
        ffwdButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	if(isShuffle)
				{
					Random r1 = new Random();
					songList.setSelectedIndex(r1.nextInt(organizer.getLibrary().size()));
					playButton.doClick();
				}
				else
				{
					songList.setSelectedIndex(songList.getSelectedIndex()+1);
					playButton.doClick();
				}
           }
        });
        rwndButton.addActionListener(new ActionListener()
        {
        	@Override
        	
        	public void actionPerformed(ActionEvent event)
            {
        		previousSong = songList.getSelectedIndex() - 1;
            	player.stop();
        		t1.interrupt();
        		songList.setSelectedIndex(previousSong);
        		playButton.doClick();
            }
        });
        artistButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	String artist = JOptionPane.showInputDialog("Enter an Artist:");
            	ArrayList<String> tracksByArtist = organizer.getTracksByArtist(artist);
            	if(artist.equals(JOptionPane.CANCEL_OPTION))
            	{
                	resetButton.doClick();
            	}
            	else if(artist.equals(JOptionPane.OK_OPTION) && tracksByArtist.isEmpty())
            	{
					JOptionPane.showMessageDialog(guiFrame,"No matches in library.","No Matches",JOptionPane.ERROR_MESSAGE);
            	}
            	else
            	{
            		songList.removeAllItems();
            		int x = 0;
            		while(x<tracksByArtist.size())
            		{
            			if(tracksByArtist.indexOf(organizer.getLibrary().get(x)) == -1)
            			{
            				songList.addItem(tracksByArtist.get(x).replace(".mp3", ""));
            			}
            			x++;
            		}
            	songList.setSelectedItem(0);
           }
           }
        });
        genreButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	String genre = JOptionPane.showInputDialog("Enter a genre:");
            	int genreInt = genres.indexOf(genre);
            	ArrayList<String> tracksOfGenre = organizer.getTracksByGenre(Integer.toString(genreInt));
            	if(artist.equals(JOptionPane.CANCEL_OPTION))
            	{
                	resetButton.doClick();
            	}
            	else if(genre.equals(JOptionPane.OK_OPTION) && tracksOfGenre.isEmpty())
            	{
					JOptionPane.showMessageDialog(guiFrame,"No matches in library.","No Matches",JOptionPane.ERROR_MESSAGE);
            	}
            	songList.removeAllItems();
            	int x = 0;
            	while(x<tracksOfGenre.size())
            	{
            		if(tracksOfGenre.indexOf(organizer.getLibrary().get(x)) != -1)
            		{
            			songList.addItem(tracksOfGenre.get(x).replace(".mp3", ""));
            		}
            		x++;
            	}
            	songList.setSelectedItem(0);
           }
        });
        resetButton.addActionListener(new ActionListener()
        {
            @Override
           
            public void actionPerformed(ActionEvent event)
            {
            	songList.removeAllItems();
            	addAllToSongList();
            }
        });
        addButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	int userChoice = fileChooser.showOpenDialog(fileChooser);
            	if (userChoice == JFileChooser.APPROVE_OPTION) 
            	{
        			try 
        			{
        				File sourceFile = fileChooser.getSelectedFile();
        				File destFile = new File(organizer.getLocation().toString() + "/" + fileChooser.getSelectedFile().getName());
        				InputStream is = null;
        			    OutputStream os = null;
        			    try 
        			    {
        			        is = new FileInputStream(sourceFile);
        			        os = new FileOutputStream(destFile);
        			        byte[] buffer = new byte[1024];
        			        int length;
        			        while ((length = is.read(buffer)) > 0) 
        			        {
        			            os.write(buffer, 0, length);
        			        }
        			    } 
        			    finally 
        			    {
        			        is.close();
        			        os.close();
        			    }					
        			} 
        			catch (IOException e) 
        			{
 					}
            	}
            	songList.removeAllItems();
				organizer = new MusicOrganizer("./music/");
            	resetButton.doClick();
            }
        });
        renameButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	String newName = JOptionPane.showInputDialog("Enter the new name:");
            	if ((newName != null) && (newName.length() > 0))
            	{
        			try 
        			{
        				File sourceFile = new File(organizer.getLocation() + "/" + songList.getSelectedItem() + ".mp3");
        				File destFile = new File(organizer.getLocation().toString() + "/" + newName +".mp3");
        				InputStream is = null;
        			    OutputStream os = null;
        			    try {
        			        is = new FileInputStream(sourceFile);
        			        os = new FileOutputStream(destFile);
        			        byte[] buffer = new byte[1024];
        			        int length;
        			        while ((length = is.read(buffer)) > 0) 
        			        {
        			            os.write(buffer, 0, length);
        			        }
        			    } 
        			    finally 
        			    {
        			        is.close();
        			        os.close();
        			        Files.delete(sourceFile.toPath());
        			    }					
        			} 
        			catch (IOException e) 
        			{
        				JOptionPane.showMessageDialog(guiFrame,"File could not be opened or created.\nMake sure that there are no duplicate files and try again.","File Operation Error",JOptionPane.ERROR_MESSAGE);
					}
        			songList.removeAllItems();
					organizer = new MusicOrganizer("./music/");
	            	resetButton.doClick();
            	}
            }
        });
        deleteButton.addActionListener(new ActionListener()
        {
            @Override
            
            public void actionPerformed(ActionEvent event)
            {
            	int yesNo = JOptionPane.showConfirmDialog(guiFrame, "Delete the file \"" + songList.getSelectedItem() + ".mp3\"?");
            	if (yesNo == 0)
            	{
        			try 
        			{
        				File toDeleteFile = new File(organizer.getLocation() + "/" + songList.getSelectedItem() + ".mp3");
        				{
        			        Files.delete(toDeleteFile.toPath());
        			    }					
        			} 
        			catch (IOException e) 
        			{
        				JOptionPane.showMessageDialog(guiFrame,"File could not be deleted.\nMake sure that you have read and write privileges and try again.","File Operation Error",JOptionPane.ERROR_MESSAGE);
					}
        			songList.removeAllItems();
					organizer = new MusicOrganizer("./music/");
	            	resetButton.doClick();
            	}
            }
        });
        changeButton.addActionListener(new ActionListener()
        {
            @Override
           
            public void actionPerformed(ActionEvent event)
            {
            	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            	int userChoice = fileChooser.showOpenDialog(fileChooser);
            	if (userChoice == JFileChooser.APPROVE_OPTION) 
            	{
        			File newFolder = fileChooser.getSelectedFile();
        			songList.removeAllItems();
        			organizer = new MusicOrganizer(newFolder.toString());
        			resetButton.doClick();
            	}
            }
        });
        playbackThread = new Runnable() 
        {
    		@Override
    		/**
    	     * The thread started when called.
    	     *
    	     */
    		public void run() 
    		{
    			int secondsPassed = 0;
    			int trackTime;
    			try
    			{
    				trackTime = currentTrack.getTime();
    				try 
    				{
    					slider.setMaximum(trackTime);
    					slider.setMinimum(0);
    					slider.setValue(0);					
    					while(secondsPassed<trackTime)
    						{
    								long millisPassed = secondsPassed*1000;
    								int secondsPassed2 = (int) (millisPassed / 1000) % 60 ;
    								int minutesPassed = (int) ((millisPassed / (1000*60)) % 60);
    								int secondsRemaining = (int) (((trackTime - secondsPassed)*1000) / 1000) % 60 ;
    								int minutesRemaining = (int) ((trackTime-secondsPassed) / 60 % 60 );
    								secondsPassed++;
    								Thread.sleep(1000);
    								slider.setValue(secondsPassed);
    								if(secondsPassed2<10)
    								{
    									currentTime.setText(Integer.toString(minutesPassed) + ":0" + Integer.toString(secondsPassed2));
    								}
    								else
    								{
    									currentTime.setText(Integer.toString(minutesPassed) + ":" + Integer.toString(secondsPassed2));
    								}
    								if(secondsRemaining<10)
    								{
    									duration.setText(Integer.toString(minutesRemaining) + ":0" + Integer.toString(secondsRemaining));
    								}
    								else
    								{
    									duration.setText(Integer.toString(minutesRemaining) + ":" + Integer.toString(secondsRemaining));
    								}
    								//if the thread is canned
    								if(Thread.currentThread().isInterrupted())
    								{
    									previousSong = songList.getSelectedIndex();
    									secondsPassed = 0;
    			    					trackTime = 0;
    			    					slider.setValue(0);
    			    					currentTime.setText("0:00");
    			    					duration.setText("0:00");
    									break;
    								}	
    						}
    					Thread.sleep(1000);
    					duration.setText("0:00");
    					Thread.sleep(1000);
						previousSong = songList.getSelectedIndex();
    					if(shuffle.isSelected())
    					{
    						int currentSelected = songList.getSelectedIndex();
    						Random r1 = new Random();
    						int randInt = r1.nextInt(organizer.getLibrary().size()-1);
    						if (currentSelected != randInt)
    						{
    							songList.setSelectedIndex(randInt);
    							playButton.doClick();
    						}
    						else 
    						{
    							randInt = r1.nextInt(organizer.getLibrary().size()-1);
    							songList.setSelectedIndex(randInt);
    							playButton.doClick();
    						}
    					}
    					else
    					{
    						int currentSelected = songList.getSelectedIndex();
    						if(currentSelected >= organizer.getLibrary().size())
    						{
    							songList.setSelectedIndex(0);
    						}
    						else
    						{
    							songList.setSelectedIndex(songList.getSelectedIndex()+1);
    						}
        					playButton.doClick();
    					}
    				} 
    				//if the previous method breaks
    				catch (InterruptedException e1) 
    				{
    					secondsPassed = 0;
    					trackTime = 0;
    					slider.setValue(0);
    					currentTime.setText("0:00");
    					duration.setText("0:00");
    				}
    			} 
    			catch (CannotReadException e) 
    			{
    				JOptionPane.showMessageDialog(guiFrame,"File could not be opened.\nMake sure that you have read and write privileges and try again.","File Error",JOptionPane.ERROR_MESSAGE);
    			}
    		}
    	};
    	//completely "render" the frame
        guiFrame.add(buttons, BorderLayout.CENTER);
        guiFrame.add(listPanel, BorderLayout.NORTH);
        guiFrame.add(lowerPanel, BorderLayout.SOUTH);
		//zero the timer
        slider.setValue(0);	
        //set frame to be visible
        guiFrame.setVisible(true);
    }
    
    /**
     * Adds all files to song list.
     */
    private void addAllToSongList()
    {
    	int x = 0;
    	while(x<organizer.getLibrary().size())
    	{
    		songList.addItem(organizer.getLibrary().get(x).replace(".mp3", ""));
    		x++;
    	}
    }
    
    /**
     * Initializes genres using ID3v2 tags.
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
		genres = new ArrayList<String>();
		genres.addAll(Arrays.asList(genreStrings));
	}
}