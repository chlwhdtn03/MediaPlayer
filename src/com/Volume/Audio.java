package com.Volume;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class Audio {
	
	public static void setVolume(float volume) {
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for(Mixer.Info mixerInfo : mixers) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineInfos = mixer.getTargetLineInfo();
			
			for (Line.Info lineInfo : lineInfos) {
				Line line = null;
                boolean opened = true;
                try {
                    line = mixer.getLine(lineInfo);
                    opened = line.isOpen() || line instanceof Clip;
                    if (!opened) {
                        line.open();
                    }
                    FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                    //System.out.println(volCtrl.getMinimum());
                    volCtrl.setValue(volume);
                    //System.out.println("    volCtrl.getValue() = " + volCtrl.getValue());
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException iaEx) {
                    //System.out.println("  -!-  " + iaEx);
                } finally {
                    if (line != null && !opened) {
                        line.close();
                    }
                }
			}
			
		}
	}

	

}