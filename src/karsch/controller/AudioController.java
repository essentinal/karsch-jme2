package karsch.controller;

import java.util.ArrayList;

import karsch.characters.CharacterBase;
import karsch.sound.SoundManager;

import com.jme.math.FastMath;
import com.jme.scene.Controller;
import com.jmex.audio.AudioTrack;

@SuppressWarnings("serial")
public class AudioController extends Controller{
	private CharacterBase character;
	private float counter = 0;
	private ArrayList<AudioTrack> tracks;
	private int randomSteps;
	public AudioController(CharacterBase character, int randomSteps) {
		this.character = character;
		this.randomSteps = randomSteps;
		
		tracks = new ArrayList<AudioTrack>(3);
	}

	@Override
	public void update(float time) {
		if (counter < 1.5f){
			counter+=time;
		} else {

			if (SoundManager.soundOn && SoundManager.getInstance().isSfxOn() && character.isVisible()){
				int rand = FastMath.rand.nextInt(tracks.size()+randomSteps);
				if (rand < tracks.size()){
					AudioTrack track = tracks.get(rand);
					track.setMaxVolume(0.8f);
					track.setMaxVolume(0.8f);
					SoundManager.getInstance().playSoundOnce(track);
				}// else play nothing
			}
			
			counter = 0;
		}
	}
	
	public void addTrack(String filename){
		AudioTrack newTrack = SoundManager.prepareTrack(filename);
		tracks.add(newTrack);
	}
}
