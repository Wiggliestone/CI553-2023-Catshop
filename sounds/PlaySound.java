package sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySound {

    public static void playSound(Sound sound) {
        try {
            AudioInputStream audioInputStream = AudioSystem
            		.getAudioInputStream(PlaySound.class.getResource(sound + ".wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


