package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL45.glTextureParameterf;
import static org.lwjgl.opengl.GL45.glTextureParameteri;
import static org.lwjgl.stb.STBImage.STBI_rgb_alpha;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    // ***ATTRIBUTE***
    private final String filepath;
    private final int texID;

    // ***CONSTRUCTOR***
    public Texture(String filepath){
        this.filepath = filepath;

        // Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeat image in both directions and pixelate when stretching and shrinking
        glTextureParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_TEXTURE_WRAP_S);
        glTextureParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_TEXTURE_WRAP_T);
        glTextureParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTextureParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTextureParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_FALSE);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, STBI_rgb_alpha);

        if (image != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0) , height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }else{
            assert false: "Error (Texture): Could not load image '" + filepath + "'";
        }

        stbi_image_free(image);
    }

    // ***METHODS***
    // Binding and unbinding functions
    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
    }
    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
