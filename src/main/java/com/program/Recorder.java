package com.program;

public class Recorder {

    public void run(){
        SpeechToTextEngine engine = new SpeechToTextEngine();
        
        try {
            String result = engine.record(10);
            System.out.println("You said: " + result);
        } catch(Exception e){
            System.out.println(e.toString());
        }
        
    }

    public static void main(String[] args){
        (new Recorder()).run();
    }
}
