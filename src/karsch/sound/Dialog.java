package karsch.sound;

import java.util.ArrayList;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.effects.SpeechBalloon;

import com.jmex.audio.AudioTrack;

public class Dialog{
	private int textNumber;
	
	private ArrayList<String> texts;
	private ArrayList<AudioTrack> speeches;
	private SpeechBalloon balloon;
	private Karsch karsch;
	public Dialog(SpeechBalloon balloon) {
		this.balloon = balloon;
		texts = new ArrayList<String>();
		speeches = new ArrayList<AudioTrack>();
	}
	
	public void addText(String text){
		texts.add(text);
	}
	
	public void addSpeech(String filename){
		AudioTrack track = SoundManager.prepareTrack(filename);
		if (track != null){
			speeches.add(track);
		}
	}
	
	public boolean start(){
		karsch = Values.getInstance().getLevelGameState().getKarsch();
		if (textNumber < texts.size()){
			
			if (textNumber > 0){
				AudioTrack lastTrack = getTrack(textNumber-1);
				if (lastTrack != null && lastTrack.isPlaying())
					lastTrack.stop();
			}
			
			String text = texts.get(textNumber);
			AudioTrack track = getTrack(textNumber);
			
			
			textNumber++;
			
			if (text.equals("")){
				return start();
			}

			if (track != null){
				SoundManager.getInstance().playSoundOnce(track);
			}

			if ((textNumber % 2) == 1){
				karsch.speak(null);
				balloon.showText(text);
			} else {
				karsch.speak(text);
				balloon.showText(null);
			}
			return true;
		} else {
			stop();
			return false;
		}
	}
	
	private AudioTrack getTrack(int number){
		if (number >= 0 && speeches.size() > number){
			return speeches.get(number);
		} else {
			return null;
		}
	}
	
	private void stop(){
		karsch.speak(null);
		balloon.showText(null);
		textNumber = 0;
	}
}
