package karsch.level;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import karsch.controller.NPCController;

import com.jme.util.resource.ResourceLocatorTool;

public class LevelText {
	private Properties prop;
	public LevelText(int levelNumber) {
		prop = new Properties();
		load("level" + levelNumber + ".txt");

	}
	
	private boolean load(String filename) {
        InputStream fin = null;
        try {
            fin = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, filename).openStream();
        } catch (Exception e) {
        	e.printStackTrace();
        	try {
				fin.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            return false;
        }

        try {
            if (fin != null) {
                prop.load(fin);
                fin.close();
            }
        } catch (IOException e) {
        	e.printStackTrace();
            return false;
        }
        return true;
    }
	
	public String getText(String key){
		String text = prop.getProperty(key);
		if (text == null)
			text = "no key";
		return text;
	}
	
	public int getRotationField(int x, int y){
		String text = prop.getProperty(x + "," + y);
		if (text == null)
			return NPCController.DIRECTION_NODIR;
		try {
			text = text.split("rotation")[1];
			text = text.substring(text.indexOf('(')+1, text.indexOf(')'));
			
			if (text.equalsIgnoreCase("down")){
				return NPCController.DIRECTION_DOWN;
			} else if (text.equalsIgnoreCase("up")){
				return NPCController.DIRECTION_UP;
			} else if (text.equalsIgnoreCase("left")){
				return NPCController.DIRECTION_LEFT;
			} else if (text.equalsIgnoreCase("right")){
				return NPCController.DIRECTION_RIGHT;
			}
		} catch (Exception e){
			return NPCController.DIRECTION_NODIR;
		}
		
		return NPCController.DIRECTION_NODIR;
	}
	
	public int getRotationEntrance(){
		String text = prop.getProperty("entrance");
		if (text == null){
			System.err.println("entrance not found in leveltext");
			return NPCController.DIRECTION_NODIR;
		}
		try {
			if (text.equalsIgnoreCase("down")){
				return NPCController.DIRECTION_DOWN;
			} else if (text.equalsIgnoreCase("up")){
				return NPCController.DIRECTION_UP;
			} else if (text.equalsIgnoreCase("left")){
				return NPCController.DIRECTION_LEFT;
			} else if (text.equalsIgnoreCase("right")){
				return NPCController.DIRECTION_RIGHT;
			}
		} catch (Exception e){
			System.err.println("entrance not found in leveltext");
			return NPCController.DIRECTION_NODIR;
		}
		System.err.println("entrance not found in leveltext");
		return NPCController.DIRECTION_NODIR;
	}
	
	public int getRotationExit(){
		String text = prop.getProperty("exit");
		if (text == null){
			System.err.println("exit not found in leveltext");
			return NPCController.DIRECTION_NODIR;
		}
		try {
			if (text.equalsIgnoreCase("down")){
				return NPCController.DIRECTION_DOWN;
			} else if (text.equalsIgnoreCase("up")){
				return NPCController.DIRECTION_UP;
			} else if (text.equalsIgnoreCase("left")){
				return NPCController.DIRECTION_LEFT;
			} else if (text.equalsIgnoreCase("right")){
				return NPCController.DIRECTION_RIGHT;
			}
		} catch (Exception e){
			System.err.println("exit not found in leveltext");
			return NPCController.DIRECTION_NODIR;
		}
		System.err.println("exit not found in leveltext");
		return NPCController.DIRECTION_NODIR;
	}
	
	public Point getSecondRollingStoneField(int x, int y){
		String text = prop.getProperty(x + "," + y);
		
		try {
			int oX, oY;
				
			oX = Integer.parseInt(text.split(",")[0]);
			oY = Integer.parseInt(text.split(",")[1]);
			return new Point(oX,oY);
		} catch (Exception e){
			return null;
		}
	}
	
	public int getLevelStyle(){
		String text = prop.getProperty("levelstyle");
		if (text.trim().equalsIgnoreCase("forest")){
			return Level.LS_FORREST;
		} else if (text.trim().equalsIgnoreCase("underground")){
			return Level.LS_UNDERGRND;
		} else if (text.trim().equalsIgnoreCase("house")){
			return Level.LS_HOUSE;
		} else {
			return Level.LS_FARM;
		}
	}
}
