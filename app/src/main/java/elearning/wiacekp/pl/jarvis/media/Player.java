package elearning.wiacekp.pl.jarvis.media;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

public class Player
{
	private Activity act;
	private Context ctx;
	private MediaPlayer mp=null;
	private int position=0;

	private File lastFile;

	private boolean played=false;
	
	public PlayerListener mCallback;
	
	public Player(PlayerListener mCallback, Activity act, Context ctx){
		this.act=act;
		this.ctx=ctx;
		this.mCallback=mCallback;
	}
	public interface PlayerListener{
		public void finishSong();
	}
	
	public void startMusic(File file){
		lastFile=file;
		try{
			mp.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
			mp=MediaPlayer.create(ctx, Uri.parse( file.getAbsolutePath()));
			mp.start();
			played=true;
		
		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer p1) {
				played = false;
				mCallback.finishSong();
			}
		});
	}

	public File getLastFile(){
		return lastFile;
	}

	public void pause(){
		if(mp!=null) {
			mp.pause();
			position = mp.getCurrentPosition();
			played = false;
		}
	}
	
	public void resume(){
		if(mp!=null) {
			mp.start();
			mp.seekTo(position);
			played = true;
		}
	}
	public void stop(){
		if(mp!=null) {
			mp.stop();
			played = false;
		}
	}
	
	public boolean getPlayed(){
		return played;
	}
}
