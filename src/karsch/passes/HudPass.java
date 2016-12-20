package karsch.passes;

import com.jme.renderer.Renderer;
import com.jme.renderer.pass.Pass;
import com.jmex.game.state.GameState;

/**
 * Wrap a GameState into a RenderPass.
 * @author Christoph Luder
 */
@SuppressWarnings("serial")
public class HudPass extends Pass {
    private GameState state = null;
    public HudPass() {
 
    }
    public void setState(GameState gs) {
        state = gs;
    }
    @Override
    public void doUpdate(float tpf) {
        state.update(tpf);
    }
 
    /**
     * Render the FengGUI GameState,
     * the time per Frame is not needed in the FengGUI Gamestate,
     * so we can pass 0 to it.
     */
    @Override
    protected void doRender(Renderer r) {
        state.render(0);
    }
}
