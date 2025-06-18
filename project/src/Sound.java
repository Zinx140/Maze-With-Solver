import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    public Clip clip;
    public URL soundURL[] = new URL[30];

    public Sound() {
        try {
            soundURL[0] = getClass().getResource("/sound/mainTheme.wav").toURI().toURL();
            soundURL[1] = getClass().getResource("/sound/coin.wav").toURI().toURL();
            soundURL[2] = getClass().getResource("/sound/hurt.wav").toURI().toURL();
            soundURL[3] = getClass().getResource("/sound/death.wav").toURI().toURL();
            soundURL[4] = getClass().getResource("/sound/transform.wav").toURI().toURL();
            soundURL[5] = getClass().getResource("/sound/won.wav").toURI().toURL();
            soundURL[6] = getClass().getResource("/sound/climb.wav").toURI().toURL();
            soundURL[7] = getClass().getResource("/sound/click.wav").toURI().toURL();
            soundURL[8] = getClass().getResource("/sound/btnpress.wav").toURI().toURL();
            soundURL[10] = getClass().getResource("/sound/healSound.wav").toURI().toURL();
        } catch (Exception e) {
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
            clip.setFramePosition(0); // rewind to the start
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
