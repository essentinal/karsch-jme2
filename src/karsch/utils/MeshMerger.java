package karsch.utils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

public class MeshMerger {
	public static TriMesh mergeMeshes(TriMesh one, TriMesh two) {
	      TriMesh result = new TriMesh();

	      FloatBuffer bVertices = one.getVertexBuffer();
	      bVertices.rewind();
	      FloatBuffer bNormal = one.getNormalBuffer();
	      bNormal.rewind();
	      FloatBuffer bColor = one.getColorBuffer();
	      if (bColor != null)
	         bColor.rewind();
	      TexCoords bCoords = one.getTextureCoords(0);
	      FloatBuffer bTexture = bCoords.coords;
	      bTexture.rewind();
	      IntBuffer bIndices = one.getIndexBuffer();
	      bIndices.rewind();

	      FloatBuffer tVertices = two.getVertexBuffer();
	      tVertices.rewind();
	      FloatBuffer tNormal = two.getNormalBuffer();
	      tNormal.rewind();
	      FloatBuffer tColor = two.getColorBuffer();
	      if (tColor != null)
	         tColor.rewind();
	      TexCoords tCoords = two.getTextureCoords(0);
	      FloatBuffer tTexture = tCoords.coords;
	      tTexture.rewind();
	      IntBuffer tIndices = two.getIndexBuffer();
	      tIndices.rewind();

	      FloatBuffer resultVertices = BufferUtils.createFloatBuffer(bVertices
	            .capacity()
	            + tVertices.capacity());
	      FloatBuffer resultNormal = BufferUtils.createFloatBuffer(bNormal
	            .capacity()
	            + tNormal.capacity());
	      FloatBuffer resultColor = BufferUtils.createFloatBuffer(0);
	      FloatBuffer resultTexture = BufferUtils.createFloatBuffer(bTexture
	            .capacity()
	            + tTexture.capacity());
	      IntBuffer resultIndices = BufferUtils.createIntBuffer(bIndices
	            .capacity()
	            + tIndices.capacity());

	      while (tVertices.hasRemaining()) {
	         resultVertices.put(tVertices.get());
	      }
	      while (bVertices.hasRemaining()) {
	         resultVertices.put(bVertices.get());
	      }
	      while (tNormal.hasRemaining()) {
	         resultNormal.put(tNormal.get());
	      }
	      while (bNormal.hasRemaining()) {
	         resultNormal.put(bNormal.get());
	      }

	      if (bColor != null) {
	         while (bColor.hasRemaining()) {
	            resultColor.put(bColor.get());
	         }
	      }

	      if (tColor != null) {
	         while (tColor.hasRemaining()) {
	            resultColor.put(tColor.get());
	         }
	      }

	      while (tTexture.hasRemaining()) {
	         resultTexture.put(tTexture.get());
	      }
	      while (bTexture.hasRemaining()) {
	         resultTexture.put(bTexture.get());
	      }
	      while (tIndices.hasRemaining()) {
	         resultIndices.put(tIndices.get());
	      }
	      while (bIndices.hasRemaining()) {
	         resultIndices.put(bIndices.get() + two.getVertexCount());
	      }

	      if (bColor != null && tColor != null) {
	         result.reconstruct(resultVertices, resultNormal, resultColor,
	               new TexCoords(resultTexture), resultIndices);
	      } else {
	         result.reconstruct(resultVertices, resultNormal, null,
	               new TexCoords(resultTexture), resultIndices);
	      }
	      return result;
	   }
}
