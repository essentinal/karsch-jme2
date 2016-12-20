package karsch.passes;

import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;

/**
 * Wrap a BasicGameState into a RenderPass.
 * @author Christoph Luder
 */
@SuppressWarnings("serial")
public class LevelPass extends RenderPass {
    private GameState state = null;
    public LevelPass() {
    }
    public void setState(BasicGameState gs) {
        state = gs;
        add(gs.getRootNode());
    }
    @Override
    protected void doUpdate(float tpf) {
    	state.update(tpf);
    }
 
    @Override
    public void doRender(Renderer r) {
        super.doRender(r);
    }
}