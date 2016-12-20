package karsch.level;

public class LevelMap{
	private Object[][] levelMap;
	private int style, xSize, ySize, levelNumber;
	public LevelMap(int x, int y, int style, int levelNumber){
		this.xSize = x;
		this.ySize = y;
		this.style = style;
		this.levelNumber = levelNumber;
		levelMap = new Object[x][y];
	}
	
	public Object[][] getLevelMap() {
		return levelMap;
	}

	public int getStyle() {
		return style;
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public int getLevelNumber() {
		return levelNumber;
	}
}
