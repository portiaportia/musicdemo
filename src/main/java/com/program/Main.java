package com.program;

import java.io.IOException;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import com.assemblyai.api.RealtimeTranscriber;
import static java.lang.Thread.interrupted;

//https://www.youtube.com/watch?v=-NB1TdXf35Q&ab_channel=AssemblyAI
public final class Main {
    volatile boolean running = true;

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.startTranscription();
    }
    public void startTranscription() throws IOException {
        Thread thread = new Thread(() -> {
            try {
                RealtimeTranscriber realtimeTranscriber = RealtimeTranscriber.builder()
                    .apiKey("c67a51ff7f07411697eeb7628033b8fa")
                    .sampleRate(16_000)
                    .onSessionBegins(sessionBegins -> System.out.println(
                        "Session open ID: " + sessionBegins.getSessionId()))
                    .disablePartialTranscripts()
                    .endUtteranceSilenceThreshold(750)
                    .onPartialTranscript(transcript -> {
                        if (!transcript.getText().isEmpty())
                            System.out.println("Partial: " + transcript.getText());
                    })
                    .onFinalTranscript(transcript -> System.out.println("Final: " + transcript.getText()))
                    .onError(err -> System.out.println("Error: " + err.getMessage()))
                    .build();
    
                System.out.println("Connecting to AssemblyAI");
                realtimeTranscriber.connect();
    
                System.out.println("Start Recording");
                AudioFormat format = new AudioFormat(16_000, 16, 1, true, false);
                TargetDataLine line = AudioSystem.getTargetDataLine(format);
                line.open(format);
                byte[] data = new byte[line.getBufferSize()];
                line.start();
    
                while (running) {
                    line.read(data, 0, data.length);
                    realtimeTranscriber.sendAudio(data);
                }
    
                System.out.println("Stopping recording");
                line.close();
    
                System.out.println("Closing real-time transcript connection");
                realtimeTranscriber.close();
            } catch (LineUnavailableException e) {
                System.out.println("Audio line unavailable.");
            } catch (Exception e) {
                System.out.println("Error sending audio: " + e.getMessage());
            }
        });
    
        thread.start();
        System.out.println("Press Enter to stop...");
        System.in.read();
    
        // Graceful shutdown
        running = false;
        try {
            thread.join();  // Ensure the thread finishes cleanly before exiting
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }
    
        System.exit(0);
    }
}