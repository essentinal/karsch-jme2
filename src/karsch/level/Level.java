package karsch.level;

import java.util.Iterator;

import karsch.KarschSimpleGame;
import karsch.characters.CharacterBase;
import karsch.characters.Karsch;
import karsch.debug.MapViewer;

import com.jme.scene.Node;

@SuppressWarnings("serial")
public class Level extends Node {
	private Node fieldNode = new Node("fieldnode");
	private Node wallNode = new Node("wallnode");
	private Node npcNode = new Node("npcnode");
	private LevelMap levelMap;
	private Karsch karsch;
	private MapViewer mapViewer;
	private int levelStyle;
	
	private LevelText levelText;
	
	public static final int LS_FARM = 0;
	public static final int LS_UNDERGRND = 1;
	public static final int LS_FORREST = 2;
	public static final int LS_HOUSE = 3;
	
	public Level(int number) {
		super("level" + number);
		
		attachChild(fieldNode);
		attachChild(wallNode);
		attachChild(npcNode);
	}
	
	public void setPause(boolean pause){
		if (npcNode != null && npcNode.getChildren() != null){
			Iterator<?> it = npcNode.getChildren().iterator();
			while (it.hasNext()){
				Object o = it.next();
				if (o instanceof CharacterBase){
					CharacterBase actChar = (CharacterBase)o;
					actChar.setPause(pause);
				}
			}
		}
	}

	public Node getFieldNode() {
		return fieldNode;
	}

	public Node getWallNode() {
		return wallNode;
	}

	public Node getNpcNode() {
		return npcNode;
	}
	
	public LevelMap getLevelMap() {
		return levelMap;
	}

	public Karsch getKarsch() {
		return karsch;
	}

	public LevelText getLevelText() {
		return levelText;
	}

	public int getLevelStyle() {
		return levelStyle;
	}

	public void setLevelMap(LevelMap levelMap) {
		this.levelMap = levelMap;
		if (KarschSimpleGame.DEBUG){
			mapViewer = MapViewer.getInstance();
			mapViewer.setLevelMap(levelMap);
		}
	}

	public void setLevelStyle(int levelStyle) {
		this.levelStyle = levelStyle;
	}

	public void setLevelText(LevelText levelText) {
		this.levelText = levelText;
	}

	public void setKarsch(Karsch karsch) {
		this.karsch = karsch;
	}
}
