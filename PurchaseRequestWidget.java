package edu.mtu.wmtu;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.google.ras.api.core.plugin.BasicWidget;
import com.google.ras.api.core.ui.resources.PackageResources;
import com.google.ras.api.core.ui.resources.Resources;

public class PurchaseRequestWidget extends BasicWidget implements ActionListener {
	private static final Resources R = 
			new PackageResources( PurchaseRequestWidget.class );
	private static final Insets WEST_INSETS = new Insets( 5, 10, 5, 10 );
	private static final Insets EAST_INSETS = new Insets( 5, 10, 5, 10 );
	private static final String SONG_STR = "Song Name";
	private static final String ALBUM_STR = "Album Name";
	private static final String SONG_REQUEST_QUERY = 
			"INSERT INTO music_requests (artist, title) VALUES (?, ?)";
	private static final String ALBUM_REQUEST_QUERY = 
			"INSERT INTO music_requests (artist, album) VALUES (?, ?)";
	
	private JLabel messageLabel;
	private JLabel songArtistLabel;
	private JTextField songArtist;
	private JLabel titleLabel;
	private JTextField title;
	private JLabel buttonLabel;
	private JRadioButton buttonSong;
	private JRadioButton buttonAlbum;
	
	private String errorMessage;
	
	public PurchaseRequestWidget() {
		super( "Purchase", R.getImage("purchase.png") );
	}
	
	@Override
	protected JComponent buildContentPanel() {
		getToolbar().setTitle( "Music Purchase Request" );
		
		// Set up container JPanel
		JPanel panel = new JPanel();
		panel.setBackground( Color.BLACK );
		panel.setLayout( new GridBagLayout() );
		
		// Initialize object variables
		GridBagConstraints gbc;
		Font newLabelFont;
		
		// Add message label
		gbc = createGbc( 0, 0 );
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets( 10, 10, 10, 10 );
		gbc.weightx = 1.0;
		messageLabel = new JLabel( " ", JLabel.CENTER );
		messageLabel.setForeground( Color.WHITE );
		panel.add( messageLabel, gbc );
		
		// Add purchase type boxes and field
		gbc = createGbc( 0, 1 );
		buttonLabel = new JLabel( "What type of music purchase?", 
				JLabel.RIGHT );
		buttonLabel.setForeground( new Color( 65, 227, 195 ) );
		newLabelFont = new Font( buttonLabel.getFont().getName(), 
				Font.BOLD + Font.ITALIC, buttonLabel.getFont().getSize() );
		buttonLabel.setFont(newLabelFont);
		panel.add( buttonLabel, gbc );
		ButtonGroup buttons = new ButtonGroup();
		gbc = createGbc( 1, 1 );
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		buttonSong = new JRadioButton( "Single Song" );
		buttonSong.setForeground( Color.WHITE );
		buttonSong.setSelected( true );
		buttonSong.setActionCommand( "song" );
		buttonSong.addActionListener( this );
		buttons.add( buttonSong );
		panel.add( buttonSong, gbc );
		gbc = createGbc( 2, 1 );
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 1;
		gbc.weightx = 0.8;
		buttonAlbum = new JRadioButton( "Full Album" );
		buttonAlbum.setForeground( Color.WHITE );
		buttonAlbum.setActionCommand( "album" );
		buttonAlbum.addActionListener( this );
		buttons.add( buttonAlbum );
		panel.add( buttonAlbum, gbc );
		
		// Add artist label and field
		gbc = createGbc( 0, 2 );
		songArtistLabel = new JLabel( "Artist or Group", JLabel.RIGHT );
		songArtistLabel.setForeground( new Color( 235, 124, 43 ) );
		newLabelFont = new Font( songArtistLabel.getFont().getName(), 
				Font.BOLD + Font.ITALIC, songArtistLabel.getFont().getSize() );
		songArtistLabel.setFont(newLabelFont);
		panel.add( songArtistLabel, gbc );
		gbc = createGbc( 1, 2 );
		songArtist = new JTextField( 27 );
		panel.add( songArtist, gbc );
		
		// Add song title/album label and field
		gbc = createGbc( 0, 3 );
		titleLabel = new JLabel( SONG_STR, JLabel.RIGHT );
		titleLabel.setForeground( new Color( 219, 45, 235 ) );
		newLabelFont = new Font( titleLabel.getFont().getName(), 
				Font.BOLD + Font.ITALIC, titleLabel.getFont().getSize() );
		titleLabel.setFont(newLabelFont);
		panel.add( titleLabel, gbc );
		gbc = createGbc( 1, 3 );
		title = new JTextField( 27 );
		panel.add( title, gbc );
		
		// Add buttons
		gbc = createGbc( 0, 4 );
		gbc.fill = GridBagConstraints.NONE;
		JButton addLog = new JButton( "Submit" );
		addLog.setActionCommand( "submit" );
		addLog.addActionListener( this );
		panel.add( addLog, gbc );
		gbc = createGbc( 1, 4 );
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		JButton clearForm = new JButton( "Clear Form" );
		clearForm.setActionCommand( "clear" );
		clearForm.addActionListener( this );
		panel.add( clearForm, gbc );
		
		return panel;
	}
	
	private GridBagConstraints createGbc( int x, int y ) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = (x == 0) ? 1 : 2;
		gbc.gridheight = 1;

		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = (x == 0) ? GridBagConstraints.BOTH
				: GridBagConstraints.HORIZONTAL;

		gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
		gbc.weightx = (x == 0) ? 0.1 : 1.0;
		gbc.weighty = 1.0;
		return gbc;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if ( event.getActionCommand().equals( "song" ) ) {
			
			titleLabel.setText( SONG_STR );
			
		} else if ( event.getActionCommand().equals( "album" ) ) {
			
			titleLabel.setText( ALBUM_STR );
			
		} else if ( event.getActionCommand().equals( "submit" ) ) {
			
			if ( !( buttonSong.isSelected() || buttonAlbum.isSelected() ) ) {
				messageLabel.setText( "Error: 'What type of music purchase?' "
						+ "must be specified" );
				messageLabel.setForeground( Color.ORANGE );
				return;
			}
			boolean songRequest = titleLabel.getText().equals( SONG_STR );
			boolean albumRequest = titleLabel.getText().equals( ALBUM_STR );
			if ( songRequest == albumRequest ) {
				messageLabel.setText( "MOTHER FUCK" );
				messageLabel.setForeground( Color.ORANGE );
				return;
			}
			if ( songRequest && songArtist.getText().trim().equals( "" ) ) {
				messageLabel.setText( "Error: 'Artist or Group' cannot be "
						+ "blank" );
				messageLabel.setForeground( Color.ORANGE );
				return;
			}
			if ( title.getText().trim().equals( "" ) ) {
				if ( songRequest ) {
					messageLabel.setText( "Error: 'Song Name' cannot be blank" );
				} else {
					messageLabel.setText( "Error: 'Album Name' cannot be blank" );
				}
				
				messageLabel.setForeground( Color.ORANGE );
				return;
			}
			
			String regex = "(<([^>]+)>)";
			String artist = songArtist.getText().replaceAll( regex, "" );
			String name = title.getText().replaceAll( regex, "" );
			artist = WordUtils.capitalizeFully( artist );
			name = WordUtils.capitalizeFully( name );
			
			boolean success;
			if ( songRequest ) {
				success = insertReq( SONG_REQUEST_QUERY, artist, name );
			} else {
				success = insertReq( ALBUM_REQUEST_QUERY, artist, name );
			}
			
			if ( success ) {
				messageLabel.setText( "Thank you!" );
				messageLabel.setForeground( Color.GREEN );
				
				// Clear form fields
				buttonSong.setSelected( true );
				songArtist.setText( "" );
				titleLabel.setText( SONG_STR );
				title.setText( "" );
			} else {
				messageLabel.setText( "Error: " + errorMessage );
				messageLabel.setForeground( Color.RED );
			}
		} else if ( event.getActionCommand().equals( "clear" ) ) {
			messageLabel.setText( " " );
			buttonSong.setSelected( true );
			songArtist.setText( "" );
			titleLabel.setText( SONG_STR );
			title.setText( "" );
		}
	}
	
	private boolean insertReq( String query, String artist, 
			String name ) {
		String dbUrl = "jdbc:mysql://10.0.1.6:3306/wmtucdrequest"
				+ "?useUnicode=true&characterEncoding=utf-8";
		String userName = "USERNAME";
		String password = "PASSWORD";
		
		try {
			Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
			Connection conn = DriverManager.getConnection( dbUrl, userName, 
					password );
			
			PreparedStatement statement = conn.prepareStatement( query );
			statement.setString( 1, artist );
			statement.setString( 2, name );
			
			statement.execute();
			
			conn.close();
			
			return true;
		} catch ( Exception e ) {
			errorMessage = ExceptionUtils.getStackTrace( e );
			e.printStackTrace();
		}
		
		return false;
	}
	
}
