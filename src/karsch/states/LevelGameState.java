package karsch.states;

import karsch.KarschSimpleGame;
import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.FlyCamController;
import karsch.debug.MapViewer;
import karsch.input.KarschInputHandler;
import karsch.items.SwitchRiddle;
import karsch.level.Level;
import karsch.level.LevelFactory;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.curve.BezierCurve;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.audio.AudioSystem;
import com.jmex.game.state.CameraGameStateDefaultCamera;

public class LevelGameState extends CameraGameStateDefaultCamera {
  private final DisplaySystem display;
  private final Values values;
  private Karsch karsch;
  private LightNode lightNode;
  private Level level;

  private SwitchRiddle riddle;

  private final int levelNumber;
  private SceneMonitor monitor;

  private Vector3f levelEntrance, levelExit;

  // settings
  private final Vector3f camPos = new Vector3f(0, 20, 15);

  private boolean pause = true;

  private final Camera cam, flycam;

  public LevelGameState(final int levelNumber) {
    super("level" + levelNumber);
    this.levelNumber = levelNumber;

    values = Values.getInstance();
    display = DisplaySystem.getDisplaySystem();

    display.getRenderer().getQueue().setTwoPassTransparency(false);
    final CullState cs = display.getRenderer().createCullState();
    cs.setCullFace(CullState.Face.Back);
    cs.setEnabled(true);

    rootNode.setRenderState(cs);
    rootNode.setCullHint(Spatial.CullHint.Dynamic);

    final int displayWidth = display.getWidth();
    final int displayHeight = display.getHeight();
    final float aspect = displayWidth / (float) displayHeight;

    cam = display.getRenderer().createCamera(displayWidth, displayHeight);
    cam.setFrustumPerspective(65, aspect, 0.5f, 1000);
    cam.update();

    flycam = display.getRenderer().createCamera(display.getWidth(),
        display.getHeight());
    flycam.setFrustumPerspective(65, aspect, 0.5f, 1000);
    flycam.update();

    values.setLevelGameState(levelNumber, this);

    loadLevel();
  }

  @Override
  protected void onActivate() {
    DisplaySystem.getDisplaySystem().setTitle("Karsch Level " + levelNumber);
    display.getRenderer().setCamera(cam);
    if (level != null && level.getLevelMap() != null && KarschSimpleGame.DEBUG) {
      MapViewer.getInstance().setLevelMap(level.getLevelMap());
      MapViewer.getInstance().update();
    }

    setPause(false);
  }

  @Override
  protected void onDeactivate() {
    super.onDeactivate();
  }

  @Override
  protected void stateUpdate(final float tpf) {
    super.stateUpdate(tpf);

    if (!pause) {
      cam.setLocation(karsch.getLocalTranslation().add(camPos));
      cam.lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);

      AudioSystem.getSystem().update();
    }

    if (KarschSimpleGame.DEBUG) {
      if (monitor != null)
        monitor.updateViewer(tpf);

      MapViewer.getInstance().update();
    }
  }

  private void loadLevel() {
    level = LevelFactory.getInstance().createLevel(this, levelNumber);
    setLevel(level);
  }

  public void setLevel(final Level level) {
    this.level = level;

    rootNode.attachChild(level);

    karsch = level.getKarsch();
    rootNode.attachChild(karsch);

    KarschInputHandler.getInstance().setKarsch(karsch);

    cam.setLocation(karsch.getLocalTranslation().add(camPos));
    cam.lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);

    rootNode.updateGeometricState(0, true);
    rootNode.updateRenderState();

    if (KarschSimpleGame.DEBUG) {
      monitor = values.getMonitor();
      monitor.registerNode(rootNode);
      monitor.showViewer(true);
    }

    setPause(false);
    update(0.01f);
    setPause(true);

    if ((level.getLevelStyle() == Level.LS_UNDERGRND)
        || (levelEntrance == null || levelExit == null)) {
      display.getRenderer().setCamera(cam);
      final TimeController tc = new TimeController(3);
      rootNode.addController(tc);
      setupLight(0.3f);
    } else {
      final Vector3f tmp = levelEntrance.add(levelExit).divide(1.5f);
      tmp.y = 60;

      final Vector3f[] cameraPoints = new Vector3f[] { levelExit, tmp,
          karsch.getLocalTranslation().add(camPos) };

      // Create a path for the camera.
      final BezierCurve bc = new BezierCurve("camera path", cameraPoints);

      // Create a curve controller to move the CameraNode along the path
      final FlyCamController cc = new FlyCamController(bc, flycam, cam,
          rootNode);
      // Slow down the curve controller a bit
      cc.setSpeed(.5f);
      cc.setLookAt(karsch.getLocalTranslation());
      cc.setMaxTime(1);
      setupLight(1);
    }
    Timer.getTimer().reset();
  }

  public void startLevel() {
    setPause(false);
    Values
        .getInstance()
        .getHudState()
        .displayTextTime(
            Values.getInstance().getLevelGameState().getLevel().getLevelText()
                .getText("start_txt"), 3000);
  }

  private void setupLight(final float factor) {
    final PointLight l = new PointLight();
    l.setDiffuse(ColorRGBA.white);
    l.setShadowCaster(true);
    l.setEnabled(true);
    l.setConstant(1 / factor);
    l.setLocation(new Vector3f(0, 5, 0));

    lightNode = new LightNode("lightnode");
    lightNode.setLight(l);
    lightNode.setLocalTranslation(new Vector3f(0, 1, 5));
    lightNode.setLocalTranslation(karsch.getLocalTranslation());

    // karsch.attachChild(lightNode);

    final PointLight l2 = new PointLight();
    l2.setEnabled(true);
    l2.setAttenuate(true);
    l2.setConstant(1.5f / factor);
    l2.setLocation(new Vector3f(0, 80, 100));
    // LightNode ln2 = new LightNode("lightnode2");
    // ln2.setLight(l2);
    // rootNode.attachChild(ln2);
    // ln2.setLocalTranslation(new Vector3f(100,8,100));

    // lightNode.attachChild(ln2);

    final LightState lightState = DisplaySystem.getDisplaySystem()
        .getRenderer().createLightState();
    lightState.attach(l);
    lightState.attach(l2);
    rootNode.clearRenderState(RenderState.RS_LIGHT);
    rootNode.setRenderState(lightState);
    rootNode.updateRenderState();
  }

  public void setPause(final boolean pause) {
    if (this.pause == pause)
      return;
    this.pause = pause;

    level.setPause(pause);
    karsch.setPause(pause);
    values.setPause(pause);
  }

  public void setPause(final boolean pause, final boolean showGui) {
    if (pause) {
      values.getHudState().displayText("Paused");
    } else {
      values.getHudState().disableProgressbar();
    }
    setPause(pause);
  }

  // public Vector3f getLevelEntrance() {
  // return levelEntrance;
  // }

  public void setLevelEntrance(final Vector3f levelEntrance) {
    this.levelEntrance = levelEntrance;
  }

  // public Vector3f getLevelExit() {
  // return levelExit;
  // }

  public void setLevelExit(final Vector3f levelExit) {
    this.levelExit = levelExit;
  }

  public Karsch getKarsch() {
    return karsch;
  }

  public Level getLevel() {
    return level;
  }

  public Camera getCam() {
    return cam;
  }

  @SuppressWarnings("serial")
  private class TimeController extends Controller {
    float time = 0, targetTime;
    Controller controller;

    public TimeController(final float maxTime) {
      this.controller = this;
      setMaxTime(maxTime);
      values.getHudPass().setEnabled(false);
    }

    @Override
    public void update(final float tpf) {
      if ((time += tpf) > getMaxTime()) {
        setActive(false);
        values.getHudPass().setEnabled(true);
        startLevel();
        rootNode.removeController(this);

        try {
          finalize();
        } catch (final Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

  public SwitchRiddle getRiddle() {
    if (riddle == null)
      riddle = new SwitchRiddle();
    return riddle;
  }

}
