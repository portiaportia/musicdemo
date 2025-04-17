package com.program;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.Transcript;
import com.assemblyai.api.resources.transcripts.types.TranscriptStatus;

public class SpeechToTextEngine {
    private static final String API_KEY = "c67a51ff7f07411697eeb7628033b8fa"; // Replace with your AssemblyAI API key
    private static final String AUDIO_FILE = "recorded_audio.wav";

    public String record(int numSeconds) throws Exception {
        System.out.println("Please speak for " + numSeconds + " seconds after the beep...");

        try {

            System.out.println("Recording started.");
            recordAudio(numSeconds); 
            System.out.println("Recording finished.");


            AssemblyAI client = AssemblyAI.builder()
                .apiKey(API_KEY)
                .build();
        
            Transcript transcript = client.transcripts().submit(new File(AUDIO_FILE));

            while (transcript.getStatus() != TranscriptStatus.COMPLETED &&
                transcript.getStatus() != TranscriptStatus.ERROR) {
                Thread.sleep(2000);
                transcript = client.transcripts().get(transcript.getId());
            }

            if (transcript.getStatus() == TranscriptStatus.COMPLETED) {
                return transcript.getText().orElse("No text available");
            } else {
                throw new Exception("Transcription failed: " + transcript.getError());
            }
        }
        catch(Exception e){
            throw new Exception("Speech to Text Failed");
        }

    }

    private static void recordAudio(int seconds) throws LineUnavailableException, IOException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.err.println("Line not supported");
            System.exit(1);
        }

        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        File wavFile = new File(AUDIO_FILE);
        AudioInputStream ais = new AudioInputStream(line);

        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            line.stop();
            line.close();
        });

        stopper.start();
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
    }
    
}
