package elearning.wiacekp.pl.jarvis.logic;

import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import elearning.wiacekp.pl.jarvis.R;
import elearning.wiacekp.pl.jarvis.books.EpubReader;
import elearning.wiacekp.pl.jarvis.commands.Commands;
import elearning.wiacekp.pl.jarvis.helpclasses.FilesList;
import elearning.wiacekp.pl.jarvis.helpclasses.ExtSdHelper;
import elearning.wiacekp.pl.jarvis.helpclasses.Helper;
import elearning.wiacekp.pl.jarvis.interfaces.RecordInterface;
import elearning.wiacekp.pl.jarvis.media.Jokes;
import elearning.wiacekp.pl.jarvis.media.Player;
import elearning.wiacekp.pl.jarvis.rssreader.MainRssReader;
import elearning.wiacekp.pl.jarvis.rssreader.SimpleWebView;
import elearning.wiacekp.pl.jarvis.social.MessagesAndPhone;
import elearning.wiacekp.pl.jarvis.social.MessagesList;
import elearning.wiacekp.pl.jarvis.social.MyPhoneListener;

public class MainLogic implements RecordInterface, Player.PlayerListener
{
	private Context ctx;
	private Activity act;

	private int lastFolderNumber=-1;

	private ArrayList<String> inputText;
	private MLogicListener mCallback;
	private ExtSdHelper esh;
	private Player player;
	private Commands commands;
	private EpubReader epubReader;
	private MessagesAndPhone messagesAndPhone;
	private Jokes jokes;


	private MainRssReader mrr;

	private Helper helper = new Helper(ctx);

	private boolean startRead=false;

	private MessagesList messageToSend = null;
	private boolean pendingMessage = false;

	public MainLogic(Context ctx, Activity act){
		player = new Player(this, act, ctx);
		this.ctx=ctx;
		this.act=act;
		epubReader = new EpubReader(ctx);
		esh= new ExtSdHelper(ctx);

		esh.createDirs(-1);

		commands = new Commands(ctx);
		mCallback=(MLogicListener)act;
////		mgr=(AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
////		maxVolume=mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
////		volume=mgr.getStreamVolume(AudioManager.STREAM_MUSIC);


		MyPhoneListener phoneListener = new MyPhoneListener(ctx, act);
		TelephonyManager telephonyManager =
				(TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		messagesAndPhone = new MessagesAndPhone(ctx, act);


		mrr = new MainRssReader(act, ctx);
		jokes = new Jokes(act, ctx);
	}
	
	public interface MLogicListener{
		public void tellText(String text, boolean book);
		public void addText(String text, boolean book);
	}
	
	public void setInput(ArrayList<String> inputText){
		this.inputText=inputText;
		for(int i=0; i<inputText.size(); i++){
			Log.d("InputCommand", inputText.get(i));
		}
		int command=checkCommand();
		if(command!=-1){
			commandNumberInterpreter(command);
		}else{
			if(pendingMessage){
				pendingMessage=false;
			}else {
				mCallback.tellText(ctx.getResources().getString(R.string.command_error), false);
			}
		}
	}

	public void booksIsDone(){
		if(startRead) {
			String line = Jsoup.parse(epubReader.nextPage()).text();
			if(line.equals("<><><><>")){
				startRead=false;
				mCallback.tellText(ctx.getResources().getString(R.string.end), false);
			}else {
				mCallback.addText(line, true);
			}
		}
	}

	private void commandNumberInterpreter(int comNumber){
		switch (comNumber){
			case 1:
				esh.createDirs(-2);
				tellFilesList(0);
				break;
			case 2:
				tellFilesList(0);
				break;
			case 3:
				tellFilesList(1);
				break;
			case 4:
				tellFilesList(2);
				break;
			case 5:
				esh.createDirs(commands.getLastNumber()-1);
				lastFolderNumber = commands.getLastNumber()-1;
				tellFilesList(0);
				break;
			case 6: //next disc
				esh.createDirs(-2);
				if(esh.getFilesList().size()>0&&lastFolderNumber+1<esh.getFilesList().size()&&esh.getFilesList().get(lastFolderNumber+1).isDirectory()) {
					lastFolderNumber += 1;
					esh.createDirs(lastFolderNumber);
					if (esh.getMusicList().size() > 0)
						player.startMusic(esh.getMusicList().get(0).getFile());
				}
				break;
			case 7:	//previous disc
			    esh.createDirs(-2);
				if(esh.getFilesList().size()>0&&lastFolderNumber-1>=0) {
					lastFolderNumber -= 1;
					esh.createDirs(lastFolderNumber);
					if (esh.getMusicList().size() > 0)
						player.startMusic(esh.getMusicList().get(0).getFile());
				}
				break;
			case 8:	//next track
				if(esh.getMusicList().size()>0) {
					int index = helper.getIdMusicFiles(esh.getMusicList(), player.getLastFile());
					if(index+1<esh.getMusicList().size())
						player.startMusic(esh.getMusicList().get(index+1).getFile());
				}
				break;
			case 9:	//previous track
				if(esh.getMusicList().size()>0) {
					int index = helper.getIdMusicFiles(esh.getMusicList(), player.getLastFile());
					if(index-1>=0)
						player.startMusic(esh.getMusicList().get(index-1).getFile());
				}
				break;
			case 10://play
				if(esh.getMusicList().size()>0)
					player.startMusic(esh.getMusicList().get(0).getFile());
				break;
			case 11://pause
				player.pause();
				break;
			case 12://continue
				player.resume();
				break;
			case 13:
				esh.setDirs(0);
				tellFilesList(0);
				break;

			case 14:		//start read ebook
				epubReader.loadEbooks(esh.getBooksList());
				if(commands.getLastNumber()-1<esh.getBooksList().size()&&commands.getLastNumber()>0) {
					epubReader.startRead(commands.getLastNumber() - 1);

					try {
						mCallback.tellText(Jsoup.parse(epubReader.nextPage()).text(), true);
					} catch (Exception e) {
						e.printStackTrace();
					}
					startRead = true;
				}else{
					mCallback.tellText(ctx.getResources().getString(R.string.book_not_exist), false);
				}
				break;
			case 15:		//continue read book
				epubReader.loadLastFile();
				mCallback.tellText(Jsoup.parse(epubReader.nextPage()).text(), true);
				startRead=true;
				break;
			case 16:		//stop read book
				try {
					epubReader.savePostion();
					startRead = false;
					mCallback.tellText("", false);
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			case 17:
				if(startRead||startReadIsSet){
					epubReader.setPageNumber(commands.getLastNumber());
					mCallback.tellText("", true);
				}
				break;
			case 18:
				esh.setDirs(1);
				tellFilesList(0);
				break;
			case 19:
				esh.saveDirsAs(0);
				break;
			case 20:
				esh.saveDirsAs(1);
				break;
			case 21:	//call
				messagesAndPhone.call(commands.getPhoneNumber());
				break;
			case 22:
				mCallback.tellText(ctx.getResources().getString(R.string.tell_contacts), false);
				for(int i=0; i<messagesAndPhone.getContactLists().size(); i++){
					mCallback.addText((i+1)+"."+messagesAndPhone.getContactLists().get(i).getfName(), false);
				}
				break;
			case 23:
				messagesAndPhone.call(messagesAndPhone.getContactLists().get(commands.getLastNumber()-1).getPhone());
				break;
			case 24:
				mCallback.tellText("linia to " + epubReader.getPageNumber(), false);
				break;
			case 25:
				messagesAndPhone.readSms();
				for(int i=0; i<messagesAndPhone.getDistinctMessagesLists().size(); i++){
					String name = messagesAndPhone.getDistinctMessagesLists().get(i).getPhone();
					for(int j=0; j<messagesAndPhone.getContactLists().size(); j++){
						if(messagesAndPhone.getDistinctMessagesLists().get(i).getPhone().replace("+48", "").trim().equals(messagesAndPhone.getContactLists().get(j).getPhone().trim())||
								("+48"+messagesAndPhone.getDistinctMessagesLists().get(i).getPhone()).trim().equals(messagesAndPhone.getContactLists().get(j).getPhone().trim())){
							name = messagesAndPhone.getContactLists().get(j).getfName();
						}
					}
					mCallback.addText((i+1)+ctx.getResources().getString(R.string.message_from)+name, false);
				}
				break;
			case 26:
				try {
					messagesAndPhone.genThreadMessages(messagesAndPhone.getDistinctMessagesLists().get(commands.getLastNumber() - 1).getThreadId());
					for (int i = 0; i < messagesAndPhone.getThreadMessagesLists().size(); i++) {
						String line = (messagesAndPhone.getThreadMessagesLists().get(i).isInbox()) ? ctx.getResources().getString(R.string.message_in) : ctx.getResources().getString(R.string.message_out);
						line += messagesAndPhone.getThreadMessagesLists().get(i).getMessage();
						mCallback.addText(line, false);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				break;
			case 27:
				if(messagesAndPhone.getThreadMessagesLists().size()>0) {
					messageToSend = new MessagesList(messagesAndPhone.getThreadMessagesLists().get(0).getPhone(), "", commands.getSecondPart(), false, messagesAndPhone.getThreadMessagesLists().get(0).getThreadId());
					pendingMessage = true;
					mCallback.tellText(ctx.getResources().getString(R.string.message_confirmation) + commands.getSecondPart(), false);
				}else{
					mCallback.tellText(ctx.getResources().getString(R.string.conversation_not_selected), false);
				}
				break;
			case 28: //tak
				if(pendingMessage){
					String smsStatus = (messagesAndPhone.sendSMSMessage(messageToSend))?ctx.getResources().getString(R.string.message_send):ctx.getResources().getString(R.string.message_send_error);
					mCallback.tellText(smsStatus, false);
				}
				break;
			case 29:
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm");
				Date date = new Date();
				String[] time = simpleDateFormat.format(date).split("-");
				String[] hours = ctx.getResources().getStringArray(R.array.hours);
				mCallback.tellText(ctx.getResources().getString(R.string.is_hour)+hours[Integer.parseInt(time[0])-1]+" "+Integer.parseInt(time[1]), false);
				break;

			case 30:
				for (int i = 0; i < mrr.getSubscriptionsTitle().size(); i++) {
					mCallback.addText((i+1)+ctx.getResources().getString(R.string.subscription)+mrr.getSubscriptionsTitle().get(i), false);
				}
				break;
			case 31:
				if(commands.getLastNumber()-1<mrr.getSubscriptions().size()-1&&commands.getLastNumber()-1>=0){
					for (int i = commands.getLastNumber()-1; i < mrr.getSubscriptionsTitle().size(); i++) {
						mCallback.addText((i+1)+ctx.getResources().getString(R.string.subscription)+mrr.getSubscriptionsTitle().get(i), false);
					}
				}else{
					mCallback.tellText(ctx.getResources().getString(R.string.number_of_bound), false);
				}
				break;
			case 32:
				if(commands.getLastNumber()-1>=0&&commands.getLastNumber()-1<mrr.getFeeds().size()-1){
					mrr.setSelectedSubscription(commands.getLastNumber()-1);
					mCallback.tellText("", false);
					for(int i=0; i<mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().size(); i++){
						mCallback.addText((i+1)+", "+mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().get(i).getTitle(), false);
					}
				}else{
					mCallback.tellText(ctx.getResources().getString(R.string.subscription_not_found), false);
				}
				break;
			case 33:
				if(mrr.getSelectedSubscription()>=0&&mrr.getSelectedSubscription()<mrr.getFeeds().size()-1){
					mCallback.tellText("", false);
					for(int i=0; i<mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().size(); i++){
						mCallback.addText((i+1)+", "+mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().get(i).getTitle(), false);
					}
				}else{
					mCallback.tellText(ctx.getResources().getString(R.string.subscription_not_found), false);
				}
				break;
			case 34:
				if(mrr.getSelectedSubscription()>=0&&mrr.getSelectedSubscription()<mrr.getFeeds().size()-1){
					if(commands.getLastNumber()-1<mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().size()-1&&commands.getLastNumber()-1>=0) {
						mCallback.tellText("", false);
						for (int i = commands.getLastNumber()-1; i < mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().size(); i++) {
							mCallback.addText((i + 1) + ", " + mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().get(i).getTitle(), false);
						}
					}else{
						mCallback.tellText(ctx.getResources().getString(R.string.element_not_found), false);
					}
				}else{
					mCallback.tellText(ctx.getResources().getString(R.string.subscription_not_found), false);
				}
				break;
			case 35:
				if(commands.getLastNumber()-1<mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().size()-1&&commands.getLastNumber()-1>=0) {
					mrr.setSelectedArticle(commands.getLastNumber()-1);
					mCallback.tellText(Jsoup.parse(
							mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().get(mrr.getSelectedArticle()).getDescription()
					).text(), false);
				}
				break;
			case 36:
				new SimpleWebView(){
					@Override
					protected void onPostExecute(String[] strings) {
						mCallback.tellText("", false);
						mCallback.addText(strings[0], false);
						mCallback.addText(strings[1], false);
					}
				}.execute(mrr.getFeeds().get(mrr.getSelectedSubscription()).getRssItems().get(mrr.getSelectedArticle()).getLink());
				break;
			case 37:
				mrr.updateList();
				mCallback.tellText(ctx.getResources().getString(R.string.update_rss), false);
				break;
			case 38:
				mCallback.tellText(jokes.nextJoke(), false);
				break;
		}
	}


	public void updateRSSFiles(){
		mrr.updateList();
	}

	/**
	 *
	 * @param typeList	typ odczytywanych plików
	 *                  0 - normalna lista plikow i folderow
	 *                  1 - lista plikow muszycznych
	 *                  2 - lista plikow tekstowych (ksiazek)
	 */
	private void tellFilesList(int typeList){
		String title="";
		String text="";
		String type="";

		ArrayList<FilesList> list = new ArrayList<>();
		switch (typeList){
			case 0:
				list = esh.getFilesList();
				title = ctx.getResources().getString(R.string.tell_files_normal);
				break;
			case 1:
				list = esh.getMusicList();
				title = ctx.getResources().getString(R.string.tell_files_music);
				break;
			case 2:
				list = esh.getBooksList();
				title = ctx.getResources().getString(R.string.tell_files_books);
				break;
		}

		mCallback.addText(title+" "+list.size(), false);
		for(int i=0; i<list.size(); i++){
			type=(list.get(i).isDirectory())? ctx.getResources().getString(R.string.folderName): ctx.getResources().getString(R.string.fileName);
			text=(i+1)+". "+type+",  "+list.get(i).getFile().getName()+",,";
			mCallback.addText(text, false);
		}
	}

	@Override
	public void finishSong()
	{
		commandNumberInterpreter(8);
	}

	private int checkCommand(){
		for(int i=0; i<inputText.size(); i++){
			int temp = commands.checkCommands(inputText.get(i).toLowerCase());
			if(temp != -1){
				return temp;
			}
		}
		return -1;
	}
	

	

	private boolean btnToPlay=false;
	private boolean startReadIsSet=false;

	@Override
	public void buttonIsClicked(boolean onClick)
	{
		if(startRead){
			startReadIsSet=true;
		}
		if(onClick&&(startRead||startReadIsSet)){
			startRead=false;
			epubReader.savePostion();
			mCallback.tellText("", false);
		}else{
			if(startReadIsSet){
				startRead=true;
				epubReader.setPageNumber(epubReader.getPageNumber() - 3);
				mCallback.tellText(Jsoup.parse(epubReader.nextPage()).text(), true);
			}
			startReadIsSet=false;
		}
		if(player.getPlayed()||btnToPlay){
			btnToPlay=onClick;
			if(onClick){
				player.pause();
			}else{
				player.resume();
			}
		}
		
	}
}
