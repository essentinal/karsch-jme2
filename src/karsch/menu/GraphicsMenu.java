package karsch.menu;

import java.io.IOException;
import java.util.ArrayList;

import karsch.Values;
import karsch.states.MenuGameState;

import com.jme.system.GameSettings;

public class GraphicsMenu extends SubMenu{
	private ArrayList<Resolution> resolutions = new ArrayList<Resolution>(5);
	private int resIndex;
	private boolean fullscreen;
	private GameSettings settings = Values.getInstance().getSettings();
	public GraphicsMenu(MenuGameState menuGameState) {
	super(menuGameState, "karsch2.jpg");
		resolutions.add(new Resolution(640, 480));
		resolutions.add(new Resolution(800, 600));
		resolutions.add(new Resolution(1024, 768));
		resolutions.add(new Resolution(1152, 864));
		resolutions.add(new Resolution(1280, 1024));
		
		Resolution actRes = getResolution(settings.getWidth());
		resIndex = resolutions.indexOf(actRes);
		
		addEntry("Resolution: " + actRes.getText());
		
		fullscreen = settings.isFullscreen();
		
		addEntry("Fullscreen: " + fullscreen);
		addEntry("Back");
		
		init();
	}

	@Override
	public void select() {
		if (counter == 0){
			right();
		} else if (counter == 1){
			fullscreen = !fullscreen;
			settings.setFullscreen(fullscreen);
			labels.get(counter).setText("Fullscreen: " + fullscreen);
			if (fullscreen){
				settings.setFrequency(60);
				settings.setDepth(16);
			} else {
				settings.setFrequency(-1);
				settings.setDepth(24);
			}
		} else if (counter == 2){
			try {
				settings.save();
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
	

	@Override
	public void left() {
		if (counter == 0){
			resIndex--;
			if (resIndex < 0){			
				resIndex = resolutions.size()-1;
			} else if (resIndex >= resolutions.size()){
				resIndex = 0;
			}
			Resolution actRes = resolutions.get(resIndex);
			labels.get(counter).setText("Resolution: " + actRes.getText());
			settings.setWidth(actRes.getWidth());
			settings.setHeight(actRes.getHeight());
		}
	}
	
	@Override
	public void right() {
		if (counter == 0){
			resIndex++;
			if (resIndex < 0){			
				resIndex = resolutions.size()-1;
			} else if (resIndex >= resolutions.size()){
				resIndex = 0;
			}
			Resolution actRes = resolutions.get(resIndex);
			labels.get(counter).setText("Resolution: " + actRes.getText());
			settings.setWidth(actRes.getWidth());
			settings.setHeight(actRes.getHeight());
		}
	}
	
	private Resolution getResolution(int width){
		for (Resolution actRes : resolutions){
			if (actRes.getWidth() == width ){
				return actRes;
			}
		}
		
		// if not found
		return new Resolution(800,600);
	}
	
	private class Resolution{
		private int width, height;
		public Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
		
		public String getText(){
			return new String(width + " x " + height);
		}
		
	}
}
