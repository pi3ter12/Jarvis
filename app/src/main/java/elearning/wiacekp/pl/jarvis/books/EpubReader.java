package elearning.wiacekp.pl.jarvis.books;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import elearning.wiacekp.pl.jarvis.R;
import elearning.wiacekp.pl.jarvis.helpclasses.FilesList;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;

public class EpubReader {
    private ArrayList<File> books = new ArrayList<>();
    private String[] pages = new String[20];

    private int pageNumber = -1;

    private Book book = null;

    private Context ctx;
    private SharedPreferences sharedpreferences;

    int readedBooks = -1;

    public EpubReader(Context ctx) {
        this.ctx=ctx;
        sharedpreferences = ctx.getSharedPreferences("EpubPreference", Context.MODE_PRIVATE);
    }

    public void loadEbooks(ArrayList<FilesList> booksList) {
        books.clear();
        for (int i = 0; i < booksList.size(); i++) {
            books.add(booksList.get(i).getFile());
        }
    }

    public void startRead(int id) {
        try {
            InputStream inStr = new FileInputStream(books.get(id));
            book = (new nl.siegmann.epublib.epub.EpubReader()).readEpub(inStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        unzipFunction();
        pageNumber = sharedpreferences.getInt(books.get(id).getName(), -1);
        readedBooks=id;
    }

    public void setPageNumber(int pageNumber){
        if(pageNumber>=0&&pageNumber<pages.length) {
            this.pageNumber = pageNumber;
        }
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public String nextPage() {
        pageNumber++;
        if(pageNumber<pages.length) {
            return pages[pageNumber];
        }else {
            return "<><><><>";
        }
    }

    public void savePostion() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(books.get(readedBooks).getName(), pageNumber-2);
        editor.putString("lastFile", books.get(readedBooks).getAbsolutePath());
        editor.commit();
    }

    public void loadLastFile() {
        if(sharedpreferences.getString("lastFile", null)!=null) {
            try {
                File file = new File(sharedpreferences.getString("lastFile", null));
                books.add(file);
                readedBooks = books.size()-1;
                InputStream inStr = new FileInputStream(books.get(readedBooks));
                book = (new nl.siegmann.epublib.epub.EpubReader()).readEpub(inStr);
                unzipFunction();
                pageNumber = sharedpreferences.getInt(file.getName(), -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void unzipFunction() {
        ArrayList<String> out = new ArrayList<>();

        try {
            List<SpineReference> spineReferences = book.getSpine().getSpineReferences();
            InputStream is = null;

            for(int j=0; j<spineReferences.size(); j++){
                is = spineReferences.get(j).getResource().getInputStream();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(is));



                String line = null;
                while ((line = reader.readLine()) != null) {
                    out.add(deleteHtmlTag(line));
                }

            }
            out=removeEmpty(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pages = new String[out.size()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = out.get(i);
        }
    }

    private String deleteHtmlTag(String text){
        String out ="";
        Boolean skipThis=false;
        for(int i=0; i<text.length(); i++){
            if(text.charAt(i)=='<'){
                skipThis=true;
            }
            if(!skipThis){
                out+=text.charAt(i);
            }
            if(text.charAt(i)=='>'){
                skipThis=false;
            }
        }
        return out;
    }

    private ArrayList<String> removeEmpty(ArrayList<String>input){
        ArrayList<String> output = input;
        for(int i=0; i<output.size(); i++){
            if(output.get(i).trim().equals("")){
                output.remove(i);
                i--;
            }
        }
        return output;
    }
}
