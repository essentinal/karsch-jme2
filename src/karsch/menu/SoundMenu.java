package karsch.menu;

import java.io.IOException;

import karsch.Values;
import karsch.sound.SoundManager;
import karsch.states.MenuGameState;

import com.jme.system.GameSettings;

public class SoundMenu extends SubMenu{
	private int sfxVolume;
	private int musicVolume;
	
	public SoundMenu(MenuGameState menuGameState) {
		super(menuGameState, "karsch2.jpg");
		
		if (SoundManager.getInstance().isSfxOn())
			addEntry("Sound Effects On");
		else
			addEntry("Sound Effects Off");
		
		
		sfxVolume = SoundManager.getInstance().getSfxVolume();
		addEntry("Sound Volume " + sfxVolume);
		
		if (SoundManager.getInstance().isMusicOn())
			addEntry("Music On");
		else
			addEntry("Music Off");
		
		musicVolume = SoundManager.getInstance().getMusicVolume();
		addEntry("Music Volume " + musicVolume);
		
		addEntry("Back");
		
		init();
	}

	@Override
	public void select() {
		if (counter == 0){
			toggleSounds();
		} else if (counter == 2){
			toggleMusic();
		} else if (counter == 4){
			try {
				Values.getInstance().getSettings().save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			menuGameState.activateMenu("options");
		}
	}
	
	@Override
	public void escape() {
		menuGameState.activateMenu("options");
	}
	
	private void toggleMusic(){
		boolean musicOn = SoundManager.getInstance().isMusicOn();
		if (musicOn){
			SoundManager.getInstance().setMusicOn(false);
			labels.get(2).setText("Music Off");
		} else {
			SoundManager.getInstance().setMusicOn(true);
			labels.get(2).setText("Music On");
		}
	}
	
	private void toggleSounds(){
		GameSettings settings = Values.getInstance().getSettings();
		if (settings.isSFX()){
			settings.setSFX(false);
			labels.get(0).setText("Sound Effects Off");
		} else {
			settings.setSFX(true);
			labels.get(0).setText("Sound Effects On");
		}
	}
	
	@Override
	public void left() {
		if (counter == 1){
			if (sfxVolume >= 5)
				sfxVolume -= 5;
			SoundManager.getInstance().setSfxVolume(sfxVolume);
			labels.get(counter).setText("Sound Volume " + sfxVolume);
		} else if (counter == 3){
			if (musicVolume >= 5)
				musicVolume -= 5;
			SoundManager.getInstance().setMusicVolume(musicVolume);
			labels.get(counter).setText("Music Volume " + musicVolume);
		}
	}
	
	@Override
	public void right() {
		if (counter == 1){
			if (sfxVolume <= 95)
				sfxVolume += 5;
			SoundManager.getInstance().setSfxVolume(sfxVolume);
			labels.get(counter).setText("Sound Volume " + sfxVolume);
		} else if (counter == 3){
			if (musicVolume <= 95)
				musicVolume += 5;
			SoundManager.getInstance().setMusicVolume(musicVolume);
			labels.get(counter).setText("Music Volume " + musicVolume);
		}
	}

}
