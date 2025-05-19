import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    public Clip clip;
    public URL soundURL[] = new URL[30];

    public Sound() {
        try {
            soundURL[0] = new File("project/sound/mainTheme.wav").toURI().toURL();
            soundURL[1] = new File("project/sound/coin.wav").toURI().toURL();
            soundURL[2] = new File("project/sound/hurt.wav").toURI().toURL();
            soundURL[3] = new File("project/sound/death.wav").toURI().toURL();
            soundURL[4] = new File("project/sound/transform.wav").toURI().toURL();
            soundURL[5] = new File("project/sound/won.wav").toURI().toURL();
            soundURL[6] = new File("project/sound/climb.wav").toURI().toURL();
            soundURL[7] = new File("project/sound/click.wav").toURI().toURL();
            soundURL[8] = new File("project/sound/btnpress.wav").toURI().toURL();
            soundURL[9] = new File("project/sound/lobby.wav").toURI().toURL();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFile(int i) {
        try {
            if (soundURL[i] == null) {
                System.out.println("Sound URL untuk index " + i + " adalah null!");
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            // System.out.println("Sukses load clip!");
        } catch (Exception e) {
            System.out.println("Gagal load clip:");
            e.printStackTrace(); // Menampilkan error
        }
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);  // rewind to the start
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void playOnce() {
        clip.setFramePosition(0);
        clip.start();
    }

}
