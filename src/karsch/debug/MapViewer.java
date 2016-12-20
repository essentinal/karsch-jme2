package karsch.debug;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import karsch.characters.Cow;
import karsch.characters.Karsch;
import karsch.characters.Sheep;
import karsch.interfaces.KarschPassable;
import karsch.items.Key;
import karsch.level.Blocker;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.level.tiles.LevelEntrance;
import karsch.level.tiles.Tile;

@SuppressWarnings("serial")
public class MapViewer extends JFrame{
	private static MapViewer instance;
	private LevelMap levelMap;
	private PaintPanel paintPanel;
	private MapViewer() {
		super("MapViewer");
		setVisible(true);
		setSize(600, 600);
		paintPanel = new PaintPanel();
		paintPanel.setVisible(true);
		add(paintPanel);
	}
	
	public static MapViewer getInstance(){
		if (instance == null)
			instance = new MapViewer();
		return instance;
	}
	
	public synchronized void setLevelMap(LevelMap levelMap){
		this.levelMap = levelMap;
		setSize(levelMap.getLevelMap().length*10+40, levelMap.getLevelMap()[0].length*10+40);
	}
	
	public void update(){
		paintPanel.repaint();
	}
	
	@SuppressWarnings("serial")
	private class PaintPanel extends JPanel{
		@Override
		protected void paintComponent(Graphics g) {
			if (levelMap != null){
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, levelMap.getLevelMap().length*10, levelMap.getLevelMap()[0].length*10);
				for (int x=0; x<levelMap.getLevelMap().length; x++){
					for (int y=0; y<levelMap.getLevelMap()[x].length; y++){
						if (levelMap.getLevelMap()[x][y] instanceof Freefield){
							g.setColor(Color.WHITE);
						} else if ( (levelMap.getLevelMap()[x][y] instanceof Tile) &&
								!(levelMap.getLevelMap()[x][y] instanceof KarschPassable) ){
							g.setColor(Color.BLACK);
						} else if (levelMap.getLevelMap()[x][y] instanceof Karsch){
							g.setColor(Color.BLUE);
						} else if (levelMap.getLevelMap()[x][y] instanceof Cow){
							g.setColor(Color.CYAN);
						} else if (levelMap.getLevelMap()[x][y] instanceof Sheep){
							g.setColor(Color.YELLOW);
						} else if (levelMap.getLevelMap()[x][y] instanceof LevelEntrance){
							if (((LevelEntrance)levelMap.getLevelMap()[x][y]).getLevelOffset() > 0){
								g.setColor(Color.GREEN);
							} else {
								g.setColor(Color.RED);
							}
							
							
						} 
						if (levelMap.getLevelMap()[x][y] instanceof Key){
							g.setColor(Color.ORANGE);
						} else if (levelMap.getLevelMap()[x][y] instanceof Blocker){
							g.setColor(Color.PINK);
						}
						
						g.fillRect(x*10, y*10, 10, 10);
						g.setColor(Color.BLACK);
						
						g.drawRect(x*10, y*10, 10, 10);
						
					}
				}
			}
		}
	}
}
