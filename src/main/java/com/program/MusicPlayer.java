package com.program;

import com.music.*;
import java.lang.Thread;

public class MusicPlayer {

    public void playSong() {
        try {
            playLine1();
            Thread.sleep(300);
            playLine2();
            Thread.sleep(300);
            playLine3();
            Thread.sleep(300);
            playLine1();
            Thread.sleep(300);
            playLine2();
        } 
        catch(Exception e){
            System.out.println(e);
        }
    }

    private void playLine1() {
        Music.playNote("C");
        Music.playNote("C");
        Music.playNote("G");
        Music.playNote("G");
        Music.playNote("A");
        Music.playNote("A");
        Music.playNote("G");
    }

    private void playLine2() {
        Music.playNote("F");
        Music.playNote("F");
        Music.playNote("E");
        Music.playNote("E");
        Music.playNote("D");
        Music.playNote("D");
        Music.playNote("C");
    }

    private void playLine3(){
        Music.playNote("F");
        Music.playNote("F");
        Music.playNote("E");
        Music.playNote("E");
        Music.playNote("D");
        Music.playNote("D");
        Music.playNote("C");
    }

    public static void main(String[] args){
        MusicPlayer player = new MusicPlayer();
        player.playSong();       
    }
}
