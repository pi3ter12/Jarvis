package elearning.wiacekp.pl.jarvis.media;

/**
 * Created by Piotrek on 2016-04-04.
 */
public class JokeList {
    private int id;

    public JokeList(int id, String joke) {
        this.id = id;
        this.joke = joke;
    }

    private String joke;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }
}
