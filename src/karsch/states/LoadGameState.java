package karsch.states;

import java.awt.Font;
import java.net.URL;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.load.Loader;
import com.jmex.scene.TimedLifeController;

/**
 * LoadGameState
 *
 * The transition game state provides additional functionality to
 * LoadingGameState. A background image is now shown during the loading phase of
 * LoadingGameState. In addition, if a lead in game state is provided, the
 * transition state will fade frame the previous game state into the loading
 * state and then fade away. The lead in game state will be deactivated once the
 * transition is complete, but not removed from the game state manager.
 *
 * @author Andrew Carter
 */
public class LoadGameState extends GameState implements Loader {
	/** Background image will be on this */
	protected Quad background;
	
	protected Node rootNode;
	private Text2D statusText;
	protected ColorRGBA color;
	protected BlendState alphaState;

	private int steps;
	private int current;

	/**
	 * Constructs a new Transition state without fading from the previous game
	 * state. Essentially the LoadingGameState but with a background image.
	 *
	 * @param imagePath
	 *          URL for a background image, null if none
	 */
	public LoadGameState(URL imagePath) {
			this.steps = 10;
			current = 0;
			init();
			initBackground(imagePath);
		}

	/**
	 * Places a a textured quad as the background. If the url is null, no quad is
	 * created.
	 *
	 * @param imagePath
	 *          URL to the background image
	 */
	private void initBackground(URL imagePath) {
		if (imagePath != null) {
			background = new Quad("Background", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem
							.getDisplaySystem().getHeight());
			background.setRenderQueueMode(Renderer.QUEUE_ORTHO);
			background.setColorBuffer(null);
			background.setDefaultColor(color);
			background.setRenderState(alphaState);
			background.setLocalTranslation(new Vector3f(DisplaySystem.getDisplaySystem().getWidth() / 2, DisplaySystem
							.getDisplaySystem().getHeight() / 2, 0.0f));

			TextureState texState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			Texture tex = TextureManager.loadTexture(imagePath, Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);
			texState.setTexture(tex);
			background.setRenderState(texState);

			background.updateRenderState();

			background.updateRenderState();
			rootNode.attachChildAt(background, 0);
			

		}
	}
	
	protected void init() {
		color = new ColorRGBA(1.0f, 0.8f, 0.2f, 1.0f);

		alphaState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alphaState.setBlendEnabled(true);
		alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaState.setTestEnabled(true);
		alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
		alphaState.setEnabled(true);

		rootNode = new Node();
		rootNode.setCullHint(Spatial.CullHint.Never);
		rootNode.setLightCombineMode(Spatial.LightCombineMode.Off);

		Font2D font = new Font2D();
		ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zbs.setFunction(ZBufferState.TestFunction.Always);

		statusText = font.createText(" ", 20.0f, Font.BOLD);
		statusText.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		statusText.setRenderState(zbs);
		statusText.setTextColor(color);
		statusText.updateRenderState();

		rootNode.attachChild(statusText);

	}

	public void update(float tpf) {
		rootNode.updateGeometricState(tpf, true);
	}

	public void render(float tpf) {
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
	}

	public void cleanup() {
	}

	public void setProgress(float progress) {
		int percentage = (int) (progress * 100);
		
		statusText.updateRenderState();
		statusText.updateModelBound();
		statusText.setLocalTranslation(new Vector3f((DisplaySystem.getDisplaySystem().getWidth() / 2)
						- (statusText.getWidth() / 2), (DisplaySystem.getDisplaySystem().getHeight() / 10)
						- (statusText.getHeight() / 10) + 50.0f, 0.0f));


		if (percentage == 100) {
			LoaderFadeOut fader = new LoaderFadeOut(1.5f, this);
			rootNode.addController(fader);
			fader.setActive(true);
		}
	}

	public void setProgress(float progress, String activity) {
		if (statusText != null) {
			statusText.setText(activity);
			setProgress(progress);
		}
	}

	protected void setAlpha(float alpha) {
		color.a = alpha;
	}

	public float increment() {
		return increment(1);
	}
	

	public float increment(int steps) {
		current += steps;
		float percent = (float)current / (float)this.steps;
		setProgress(percent);
		return percent;
	}
	

	public float increment(String activity) {
		float percent = increment();
		setProgress(percent, activity);
		return percent;
	}
	

	public float increment(int steps, String activity) {
		float percent = increment(steps);
		setProgress(percent, activity);
		return percent;
	}
}

class LoaderFadeOut extends TimedLifeController {
	private static final long serialVersionUID = 1L;

	private LoadGameState loading;

	public LoaderFadeOut(float lifeInSeconds, LoadGameState loading) {
		super(lifeInSeconds);
		this.loading = loading;
	}

	public void updatePercentage(float percentComplete) {
		loading.setAlpha(1.0f - percentComplete);
		if (percentComplete == 1.0f) {
			loading.setActive(false);
			GameStateManager.getInstance().detachChild(loading);
		}
	}
}
