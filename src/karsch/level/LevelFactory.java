package karsch.level;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import karsch.Values;
import karsch.characters.CharacterBase;
import karsch.characters.Cow;
import karsch.characters.Cow2;
import karsch.characters.Gunther;
import karsch.characters.Karsch;
import karsch.characters.MrsKarsch;
import karsch.characters.Pig;
import karsch.characters.PigHouse;
import karsch.characters.Sheep;
import karsch.characters.CreditSheep;
import karsch.effects.FogOfWar;
import karsch.items.Key;
import karsch.level.tiles.Fence;
import karsch.level.tiles.FenceGate;
import karsch.level.tiles.Floor;
import karsch.level.tiles.Hay;
import karsch.level.tiles.House;
import karsch.level.tiles.HouseInside;
import karsch.level.tiles.LevelEntrance;
import karsch.level.tiles.Lever;
import karsch.level.tiles.RollingStone;
import karsch.level.tiles.SpikeTrap;
import karsch.level.tiles.Stone;
import karsch.level.tiles.Tile;
import karsch.level.tiles.Tree;
import karsch.level.tiles.Vegetation;
import karsch.level.tiles.Wall;
import karsch.states.LevelGameState;

import com.jme.math.Vector3f;
import com.jme.util.resource.ResourceLocatorTool;

public class LevelFactory {
	private static LevelFactory instance;
	public static final int COLOR_FREE =  			new Color(255,255,255).getRGB();
	public static final int COLOR_PIG =  			new Color(0,0,255).getRGB();
	public static final int COLOR_PIG_HOUSE =		new Color(0,0,254).getRGB();
	public static final int COLOR_WALL =  			new Color(0,0,0).getRGB();
	public static final int COLOR_SHEEP =  			new Color(255,255,0).getRGB();
	public static final int COLOR_CREDITSSHEEP =  	new Color(200,200,0).getRGB();
	public static final int COLOR_COW =  			new Color(0,255,255).getRGB();
	public static final int COLOR_COW2 =  			new Color(1,255,255).getRGB();
	public static final int COLOR_GUNTHER =  		new Color(150,150,0).getRGB();
	public static final int COLOR_TREE = 			new Color(0,50,0).getRGB();
	public static final int COLOR_HAY = 			new Color(0,50,0).getRGB();
	public static final int COLOR_FENCE = 			new Color(100,70,0).getRGB();
	public static final int COLOR_FENCEGATE = 		new Color(150, 70, 0).getRGB();
	public static final int COLOR_LEVER = 			new Color(155, 255, 255).getRGB();
	public static final int COLOR_KEY = 			new Color(255, 50, 50).getRGB();
	public static final int COLOR_HOUSE = 			new Color(100,100,0).getRGB();
	public static final int COLOR_BLOCKER = 		new Color(100,100,100).getRGB();
	public static final int COLOR_NEXTLEVEL =  		new Color(0,255,0).getRGB();
	public static final int COLOR_PREVLEVEL =  		new Color(255,0,55).getRGB();
	public static final int COLOR_PREVLEVEL_SIGN =  new Color(255,0,255).getRGB();
	public static final int COLOR_SPIKETRAP = 		new Color(50,50,50).getRGB();
	public static final int COLOR_STONE = 			new Color(100,0,0).getRGB();
	public static final int COLOR_MRSKARSCH = 		new Color(255,50,155).getRGB();
	
	public static final int COLOR_LEVELSTYLE_FARM =  new Color(0,0,0).getRGB();
	public static final int COLOR_LEVELSTYLE_UNDERGROUND =  new Color(1,1,1).getRGB();
	
	private final Freefield free = new Freefield();
	
	private LevelText levelText;
	
	private LevelFactory() {
	}
	
	public static LevelFactory getInstance(){
		if (instance == null)
			instance = new LevelFactory();
		return instance;
	}
	
	public Level createLevel(LevelGameState parent, final int number){
		long time = System.currentTimeMillis();
		Level level = new Level(number);
		
		levelText = new LevelText(number);
		level.setLevelText(levelText);
		String imageFile = "level" + number + ".png";
		
		BufferedImage levelImage = null;
		try {
			levelImage = ImageIO.read(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, imageFile));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		int xSize = levelImage.getWidth();
		int ySize = levelImage.getHeight();
		
		int levelStyle = levelText.getLevelStyle();
		level.setLevelStyle(levelStyle);
		
		LevelMap levelMap = new LevelMap(xSize, ySize, levelStyle, number);
		level.setLevelMap(levelMap);
		Values.getInstance().setLevelMap(levelMap);
		
		if (levelStyle != Level.LS_UNDERGRND && levelStyle != Level.LS_HOUSE){
			level.attachChild(new PanoramaEnv(xSize/2, ySize/2, 200,12));
		}
		
		if (levelStyle != Level.LS_HOUSE){
			level.attachChild(new Floor(xSize*2,ySize*2, levelStyle));
		}
		
		for (int x = 0; x < xSize; x++){
        	for (int y = 0; y < ySize; y++){
				CharacterBase actChar = null;
    			int pixel = levelImage.getRGB(x, y);
    			
    			Tile tmp = null;
    			
    			if (pixel == COLOR_WALL){
    				if (levelStyle == Level.LS_UNDERGRND){
    					tmp = new Wall(x,y);
    				} else if (levelStyle == Level.LS_FARM){
    					tmp = new Stone(x,y);	
    				} else if (levelStyle == Level.LS_HOUSE){
    					tmp = new Wall(x,y,true);
    				}
    				level.getWallNode().attachChild(tmp);
					levelMap.getLevelMap()[x][y] = tmp;
    			} else if (pixel == COLOR_TREE){
    				if (levelStyle == Level.LS_HOUSE){
    					tmp = new Hay(x, y);
	    				level.getFieldNode().attachChild(tmp);
	    				levelMap.getLevelMap()[x][y] = tmp;
    				} else {
	    				tmp = new Tree(x, y, 1);
	    				level.getFieldNode().attachChild(tmp);
	    				levelMap.getLevelMap()[x][y] = tmp;
    				}
    			} else if (pixel == COLOR_HOUSE){
    				tmp = new House(x,y,levelImage);
    				if(((House)tmp).getNewHouse()){
    					level.getWallNode().attachChild(tmp); 
    				}
    				levelMap.getLevelMap()[x][y] = tmp;										
    			} else if (pixel == COLOR_FENCE){
    				tmp = new Fence(x,y,levelImage);
    				level.getWallNode().attachChild(tmp);
    				levelMap.getLevelMap()[x][y] = tmp;
    			} else if (pixel == COLOR_FENCEGATE){
    				tmp = new FenceGate(x,y, levelText);
    				level.getWallNode().attachChild(tmp);
    				levelMap.getLevelMap()[x][y] = tmp;
    			} else if (pixel == COLOR_LEVER){
    				tmp = new Lever(x,y, levelText);
    				level.getWallNode().attachChild(tmp);
    				levelMap.getLevelMap()[x][y] = tmp;
    			} else if (pixel == COLOR_STONE){
    				tmp = new RollingStone(x,y, levelText, levelMap);
    				level.getWallNode().attachChild(tmp);
    				levelMap.getLevelMap()[x][y] = tmp;
    			} else if (pixel == COLOR_BLOCKER){
    				levelMap.getLevelMap()[x][y] = new Blocker();
    			} else {
    				levelMap.getLevelMap()[x][y] = free;
    				if (levelStyle == Level.LS_HOUSE){
    					tmp = new HouseInside(x,y,levelImage);
        				if(((HouseInside)tmp).getNewHouse()){
        					level.getWallNode().attachChild(tmp); 
        				}
    				}
    			}
    			if (pixel == COLOR_PIG){
    				actChar = new Pig(x, y, levelMap);
    				actChar.setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_PIG_HOUSE){
    				int babies = Values.getInstance().getBabies();
    				if (Values.getInstance().getBabies() > 0){
    					
    					Values.getInstance().setBabies(babies-1);
    					
    					actChar = new PigHouse(x, y, levelMap);
        				actChar.setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
        				
        				// look at mrs karsch
        				actChar.lookAt(new Vector3f(12*5f+2.5f, 2,11*5f + 2.5f), Vector3f.UNIT_Y);
        				
        				level.getNpcNode().attachChild(actChar);
        				levelMap.getLevelMap()[x][y] = actChar;
    				} else {
    					levelMap.getLevelMap()[x][y] = free;
    				}
    				
    			} else if (pixel == COLOR_SHEEP){
    				actChar = new Sheep(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_CREDITSSHEEP){
    				actChar = new CreditSheep(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_COW){
    				actChar = new Cow(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_COW2){
    				actChar = new Cow2(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_GUNTHER){
    				actChar = new Gunther(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_MRSKARSCH){
    				actChar = new MrsKarsch(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_KEY){
    				actChar = new Key(x, y, levelMap);
    				level.getNpcNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar;
    			} else if (pixel == COLOR_SPIKETRAP){
    				actChar = new SpikeTrap(x,y);
    				level.getFieldNode().attachChild(actChar);
    				levelMap.getLevelMap()[x][y] = actChar; 
    			} else if (pixel == COLOR_NEXTLEVEL){
    				tmp = new LevelEntrance(x,y,levelImage,1, levelText, new Vector3f(x*5+2.5f, 0f, y*5+2.5f), false);
    				levelMap.getLevelMap()[x][y] = tmp;
    				level.getFieldNode().attachChild(tmp);
    				if (parent != null)
    					parent.setLevelExit(new Vector3f(x*5+2.5f, 0f, y*5+2.5f));
    			} else if (pixel == COLOR_PREVLEVEL_SIGN){
    				tmp = new LevelEntrance(x,y,levelImage,-1, levelText, new Vector3f(x*5+2.5f, 0f, y*5+2.5f), false );
    				
    				levelMap.getLevelMap()[x][y] = tmp;
    				level.getFieldNode().attachChild(tmp);
    				if (parent != null)
    					parent.setLevelEntrance(new Vector3f(x*5+2.5f, 0f, y*5+2.5f));
    				
    				Karsch karsch = new Karsch(x, y, levelMap, levelText);
    				karsch.setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
    				if (levelStyle == Level.LS_UNDERGRND)
    					karsch.attachChild(new FogOfWar());
    				level.setKarsch(karsch);
    				
    			} else if (pixel == COLOR_PREVLEVEL){
    				tmp = new LevelEntrance(x,y,levelImage,-1, levelText, new Vector3f(x*5+2.5f, 0f, y*5+2.5f), true );
    				
    				levelMap.getLevelMap()[x][y] = tmp;
    				level.getFieldNode().attachChild(tmp);
    				if (parent != null)
    					parent.setLevelEntrance(new Vector3f(x*5+2.5f, 0f, y*5+2.5f));
    				
    				Karsch karsch = new Karsch(x, y, levelMap, levelText);
    				karsch.setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
    				if (levelStyle == Level.LS_UNDERGRND)
    					karsch.attachChild(new FogOfWar());
    				level.setKarsch(karsch);
    				
    			}
			}
		}
		
		level.attachChild(new Vegetation(levelMap));
		
		System.out.println("DONE, TIME WAS " + (System.currentTimeMillis()-time + " ms"));

		return level;
    	
	}
}
