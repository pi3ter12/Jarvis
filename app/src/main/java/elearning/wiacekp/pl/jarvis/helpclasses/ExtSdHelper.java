package elearning.wiacekp.pl.jarvis.helpclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import elearning.wiacekp.pl.jarvis.helpclasses.FilesList;

public class ExtSdHelper
{
	private Context ctx;
	private File lastPatch;
	private ArrayList<FilesList> filesList= new ArrayList<>( );
	private ArrayList<FilesList> musicList= new ArrayList<>( );
	private ArrayList<FilesList> booksList= new ArrayList<>( );
	private SharedPreferences sharedPreferences;
	
	public ExtSdHelper(Context ctx){
		this.ctx=ctx;
		lastPatch=Environment.getExternalStorageDirectory();
		createDirs(-1);
		sharedPreferences = ctx.getSharedPreferences("extSD", Context.MODE_PRIVATE);
	}
	
	public String getFileExtension(String name){
		String out= name.split("\\.")[name.split("\\.").length-1];
		return out;
	}

	public ArrayList<FilesList> getFilesList(){
		return filesList;
	}

	public ArrayList<FilesList> getMusicList(){
		return musicList;
	}

	public ArrayList<FilesList> getBooksList(){
		return booksList;
	}
	
	public ArrayList<FilesList> createDirs(int id){
		if(id==-2){
			lastPatch=lastPatch.getParentFile();
		}else if(id==-1){
			lastPatch=Environment.getExternalStorageDirectory();
		}else{
			if(id>=filesList.size()||filesList.get(id).isDirectory()==false){
				return filesList;
			}
			lastPatch=filesList.get(id).getFile();
		}
		genFilesLits();
		return filesList;
	}

	private void genFilesLits(){
		filesList.clear();
		FileFilter folderF =new FileFilter(){
			@Override
			public boolean accept(File p1)
			{
				return p1.isDirectory();
			}
		};

		File[] folders = lastPatch.listFiles(folderF);

		for(int i=0; i<folders.length; i++){
			filesList.add(new FilesList( i, folders[i], true));
		}
		int filesStart=folders.length;

		FileFilter fileF = new FileFilter(){
			@Override
			public boolean accept(File p1)
			{
				return p1.isFile();
			}
		};

		File[] files = lastPatch.listFiles(fileF);

		for(int i=0, j=filesStart; i<files.length; i++, j++){
			filesList.add(new FilesList( j,files[i],false));
		}

		musicList.clear();
		for(int i=0; i<filesList.size(); i++){
			if(getFileExtension(filesList.get(i).getFile().getName()).equals("mp3")||
					getFileExtension(filesList.get(i).getFile().getName()).equals("m4a")){
				musicList.add(filesList.get(i));
			}
		}

		booksList.clear();
		for(int i=0; i<filesList.size(); i++){
			if(getFileExtension(filesList.get(i).getFile().getName()).equals("epub")){
				booksList.add(filesList.get(i));
			}
		}
	}

	/**
	 *
	 * @param type	typ folderu
	 *              0-muzyczny,
	 *              1-folder z ksiazkami
	 * @return	true jezeli operacja przebiegnie pomyslnie
	 */
	public boolean saveDirsAs(int type){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String name = (type==0)? "music":"books";
		editor.putString(name, lastPatch.getAbsolutePath());
		editor.commit();
		return true;
	}

	public void setDirs(int type){
		String name = (type==0)? "music":"books";
		File file = null;
		file = new File(sharedPreferences.getString(name, lastPatch.getAbsolutePath()));
		lastPatch = file;
		genFilesLits();
	}
	
}
