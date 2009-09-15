package glredbook10;

import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import com.sun.opengl.util.*;
import javax.media.opengl.awt.GLJPanel;

/**
 * Draws some text in a bitmapped font. Uses glBitmap() and other pixel
 * routines. Also demonstrates use of display lists. <br>
 * <br>
 * 
 * @author Kiet Le (Java port) Ported to JOGL 2.x by Claudio Eduardo Goes
 */
public class font //
        extends GLSkeleton<GLJPanel>
        implements GLEventListener, KeyListener {
    private int fontOffset;

    @Override
    protected GLJPanel createDrawable() {
        GLCapabilities caps = new GLCapabilities(null);
        //
        GLJPanel panel = new GLJPanel(caps);
        panel.addGLEventListener(this);
        panel.addKeyListener(this);
        return panel;
    }

    public static void main(String[] args) {
        font demo = new font();

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("font");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(demo.drawable);
        frame.setVisible(true);
        demo.drawable.requestFocusInWindow();
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2(); 
        //
        gl.glShadeModel(GL2.GL_FLAT);
        makeRasterFont(gl);
    }

    /*
     * Everything above this line could be in a library that defines a font. To
     * make it work, you've got to call makeRasterFont() before you start making
     * calls to printString().
     */
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        //
        float white[] = { 1.0f, 1.0f, 1.0f };

        // char teststring[] = new char[33];
        String teststring = "";

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3fv(white, 0);
        for (int i = 32; i < 127; i += 32) {
            gl.glRasterPos2i(20, 200 - 18 * i / 32);
            for (int j = 0; j < 32; j++)
                teststring += (char) (i + j);
            teststring += '\0';
            printString(gl, teststring);
        }
        gl.glRasterPos2i(20, 100);
        printString(gl, "The quick brown fox jumps");
        gl.glRasterPos2i(20, 82);
        printString(gl, "over a lazy dog.");
        gl.glFlush();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        //
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, w, 0.0, h, -1.0, 1.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
            boolean deviceChanged) {
    }

    private void makeRasterFont(GL2 gl) {
        int i;
        gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

        fontOffset = gl.glGenLists(128);
        for (i = 32; i < 127; i++) {
            gl.glNewList(i + fontOffset, GL2.GL_COMPILE);
            gl.glBitmap(8, 13, 0.0f, 2.0f, 10.0f, 0.0f, rasters[i - 32], 0);
            gl.glEndList();
        }
    }

    private void printString(GL2 gl, String s) {
        ByteBuffer str = BufferUtil.newByteBuffer(s.length());
        str.put(s.getBytes());
        str.rewind();
        //
        gl.glPushAttrib(GL2.GL_LIST_BIT);
        gl.glListBase(fontOffset);
        gl.glCallLists(s.length(), GL.GL_UNSIGNED_BYTE, str);
        gl.glPopAttrib();
    }

    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
        switch (key.getKeyChar()) {
        case KeyEvent.VK_ESCAPE:
            System.exit(0);
            break;

        default:
            break;
        }
    }

    public void keyReleased(KeyEvent key) {
    }

    private byte rasters[][] = {
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00 },
            { 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18,
                    0x18, 0x18 },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x36, 0x36,
                    0x36, 0x36 },
            { 0x00, 0x00, 0x00, 0x66, 0x66, (byte) 0xff, 0x66, 0x66,
                    (byte) 0xff, 0x66, 0x66, 0x00, 0x00 },
            { 0x00, 0x00, 0x18, 0x7e, (byte) 0xff, 0x1b, 0x1f, 0x7e,
                    (byte) 0xf8, (byte) 0xd8, (byte) 0xff, 0x7e, 0x18 },
            { 0x00, 0x00, 0x0e, 0x1b, (byte) 0xdb, 0x6e, 0x30, 0x18, 0x0c,
                    0x76, (byte) 0xdb, (byte) 0xd8, 0x70 },
            { 0x00, 0x00, 0x7f, (byte) 0xc6, (byte) 0xcf, (byte) 0xd8, 0x70,
                    0x70, (byte) 0xd8, (byte) 0xcc, (byte) 0xcc, 0x6c, 0x38 },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x1c,
                    0x0c, 0x0e },
            { 0x00, 0x00, 0x0c, 0x18, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                    0x18, 0x0c },
            { 0x00, 0x00, 0x30, 0x18, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c,
                    0x18, 0x30 },
            { 0x00, 0x00, 0x00, 0x00, (byte) 0x99, 0x5a, 0x3c, (byte) 0xff,
                    0x3c, 0x5a, (byte) 0x99, 0x00, 0x00 },
            { 0x00, 0x00, 0x00, 0x18, 0x18, 0x18, (byte) 0xff, (byte) 0xff,
                    0x18, 0x18, 0x18, 0x00, 0x00 },
            { 0x00, 0x00, 0x30, 0x18, 0x1c, 0x1c, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00 },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xff, (byte) 0xff,
                    0x00, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x00, 0x38, 0x38, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00 },
            { 0x00, 0x60, 0x60, 0x30, 0x30, 0x18, 0x18, 0x0c, 0x0c, 0x06, 0x06,
                    0x03, 0x03 },
            { 0x00, 0x00, 0x3c, 0x66, (byte) 0xc3, (byte) 0xe3, (byte) 0xf3,
                    (byte) 0xdb, (byte) 0xcf, (byte) 0xc7, (byte) 0xc3, 0x66,
                    0x3c },
            { 0x00, 0x00, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x78,
                    0x38, 0x18 },
            { 0x00, 0x00, (byte) 0xff, (byte) 0xc0, (byte) 0xc0, 0x60, 0x30,
                    0x18, 0x0c, 0x06, 0x03, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, 0x03, 0x03, 0x07, 0x7e, 0x07,
                    0x03, 0x03, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, (byte) 0xff,
                    (byte) 0xcc, 0x6c, 0x3c, 0x1c, 0x0c },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, 0x03, 0x03, 0x07, (byte) 0xfe,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xff },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc7, (byte) 0xfe, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, 0x30, 0x30, 0x30, 0x30, 0x18, 0x0c, 0x06, 0x03, 0x03,
                    0x03, (byte) 0xff },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xe7, 0x7e, (byte) 0xe7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xe7, 0x7e },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, 0x03, 0x03, 0x03, 0x7f,
                    (byte) 0xe7, (byte) 0xc3, (byte) 0xc3, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, 0x00, 0x38, 0x38, 0x00, 0x00, 0x38, 0x38, 0x00, 0x00,
                    0x00, 0x00 },
            { 0x00, 0x00, 0x30, 0x18, 0x1c, 0x1c, 0x00, 0x00, 0x1c, 0x1c, 0x00,
                    0x00, 0x00 },
            { 0x00, 0x00, 0x06, 0x0c, 0x18, 0x30, 0x60, (byte) 0xc0, 0x60,
                    0x30, 0x18, 0x0c, 0x06 },
            { 0x00, 0x00, 0x00, 0x00, (byte) 0xff, (byte) 0xff, 0x00,
                    (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x60, 0x30, 0x18, 0x0c, 0x06, 0x03, 0x06, 0x0c, 0x18,
                    0x30, 0x60 },
            { 0x00, 0x00, 0x18, 0x00, 0x00, 0x18, 0x18, 0x0c, 0x06, 0x03,
                    (byte) 0xc3, (byte) 0xc3, 0x7e },
            { 0x00, 0x00, 0x3f, 0x60, (byte) 0xcf, (byte) 0xdb, (byte) 0xd3,
                    (byte) 0xdd, (byte) 0xc3, 0x7e, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xff, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, 0x66,
                    0x3c, 0x18 },
            { 0x00, 0x00, (byte) 0xfe, (byte) 0xc7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc7, (byte) 0xfe, (byte) 0xc7, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc7, (byte) 0xfe },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, (byte) 0xfc, (byte) 0xce, (byte) 0xc7, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc7, (byte) 0xce, (byte) 0xfc },
            { 0x00, 0x00, (byte) 0xff, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xfc, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xff },
            { 0x00, 0x00, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xfc, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xff },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xcf, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xff, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3 },
            { 0x00, 0x00, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18,
                    0x18, 0x7e },
            { 0x00, 0x00, 0x7c, (byte) 0xee, (byte) 0xc6, 0x06, 0x06, 0x06,
                    0x06, 0x06, 0x06, 0x06, 0x06 },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xc6, (byte) 0xcc, (byte) 0xd8,
                    (byte) 0xf0, (byte) 0xe0, (byte) 0xf0, (byte) 0xd8,
                    (byte) 0xcc, (byte) 0xc6, (byte) 0xc3 },
            { 0x00, 0x00, (byte) 0xff, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0 },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xdb, (byte) 0xff,
                    (byte) 0xff, (byte) 0xe7, (byte) 0xc3 },
            { 0x00, 0x00, (byte) 0xc7, (byte) 0xc7, (byte) 0xcf, (byte) 0xcf,
                    (byte) 0xdf, (byte) 0xdb, (byte) 0xfb, (byte) 0xf3,
                    (byte) 0xf3, (byte) 0xe3, (byte) 0xe3 },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xfe, (byte) 0xc7, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc7, (byte) 0xfe },
            { 0x00, 0x00, 0x3f, 0x6e, (byte) 0xdf, (byte) 0xdb, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, 0x66,
                    0x3c },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xc6, (byte) 0xcc, (byte) 0xd8,
                    (byte) 0xf0, (byte) 0xfe, (byte) 0xc7, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc7, (byte) 0xfe },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, 0x03, 0x03, 0x07, 0x7e,
                    (byte) 0xe0, (byte) 0xc0, (byte) 0xc0, (byte) 0xe7, 0x7e },
            { 0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18,
                    0x18, (byte) 0xff },
            { 0x00, 0x00, 0x7e, (byte) 0xe7, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3 },
            { 0x00, 0x00, 0x18, 0x3c, 0x3c, 0x66, 0x66, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3 },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xe7, (byte) 0xff, (byte) 0xff,
                    (byte) 0xdb, (byte) 0xdb, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3 },
            { 0x00, 0x00, (byte) 0xc3, 0x66, 0x66, 0x3c, 0x3c, 0x18, 0x3c,
                    0x3c, 0x66, 0x66, (byte) 0xc3 },
            { 0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x3c, 0x3c, 0x66,
                    0x66, (byte) 0xc3 },
            { 0x00, 0x00, (byte) 0xff, (byte) 0xc0, (byte) 0xc0, 0x60, 0x30,
                    0x7e, 0x0c, 0x06, 0x03, 0x03, (byte) 0xff },
            { 0x00, 0x00, 0x3c, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                    0x30, 0x3c },
            { 0x00, 0x03, 0x03, 0x06, 0x06, 0x0c, 0x0c, 0x18, 0x18, 0x30, 0x30,
                    0x60, 0x60 },
            { 0x00, 0x00, 0x3c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c,
                    0x0c, 0x3c },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    (byte) 0xc3, 0x66, 0x3c, 0x18 },
            { (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x38,
                    0x30, 0x70 },
            { 0x00, 0x00, 0x7f, (byte) 0xc3, (byte) 0xc3, 0x7f, 0x03,
                    (byte) 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, (byte) 0xfe, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xfe, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0 },
            { 0x00, 0x00, 0x7e, (byte) 0xc3, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x7f, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, 0x7f, 0x03, 0x03, 0x03, 0x03, 0x03 },
            { 0x00, 0x00, 0x7f, (byte) 0xc0, (byte) 0xc0, (byte) 0xfe,
                    (byte) 0xc3, (byte) 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x30, 0x30, 0x30, 0x30, 0x30, (byte) 0xfc, 0x30,
                    0x30, 0x30, 0x33, 0x1e },
            { 0x7e, (byte) 0xc3, 0x03, 0x03, 0x7f, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, 0x7e, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xfe, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0 },
            { 0x00, 0x00, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x00, 0x00,
                    0x18, 0x00 },
            { 0x38, 0x6c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x00, 0x00,
                    0x0c, 0x00 },
            { 0x00, 0x00, (byte) 0xc6, (byte) 0xcc, (byte) 0xf8, (byte) 0xf0,
                    (byte) 0xd8, (byte) 0xcc, (byte) 0xc6, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xc0, (byte) 0xc0 },
            { 0x00, 0x00, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18,
                    0x18, 0x78 },
            { 0x00, 0x00, (byte) 0xdb, (byte) 0xdb, (byte) 0xdb, (byte) 0xdb,
                    (byte) 0xdb, (byte) 0xdb, (byte) 0xfe, 0x00, 0x00, 0x00,
                    0x00 },
            { 0x00, 0x00, (byte) 0xc6, (byte) 0xc6, (byte) 0xc6, (byte) 0xc6,
                    (byte) 0xc6, (byte) 0xc6, (byte) 0xfc, 0x00, 0x00, 0x00,
                    0x00 },
            { 0x00, 0x00, 0x7c, (byte) 0xc6, (byte) 0xc6, (byte) 0xc6,
                    (byte) 0xc6, (byte) 0xc6, 0x7c, 0x00, 0x00, 0x00, 0x00 },
            { (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xfe, (byte) 0xc3,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, (byte) 0xfe, 0x00,
                    0x00, 0x00, 0x00 },
            { 0x03, 0x03, 0x03, 0x7f, (byte) 0xc3, (byte) 0xc3, (byte) 0xc3,
                    (byte) 0xc3, 0x7f, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0, (byte) 0xc0,
                    (byte) 0xc0, (byte) 0xe0, (byte) 0xfe, 0x00, 0x00, 0x00,
                    0x00 },
            { 0x00, 0x00, (byte) 0xfe, 0x03, 0x03, 0x7e, (byte) 0xc0,
                    (byte) 0xc0, 0x7f, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x1c, 0x36, 0x30, 0x30, 0x30, 0x30, (byte) 0xfc,
                    0x30, 0x30, 0x30, 0x00 },
            { 0x00, 0x00, 0x7e, (byte) 0xc6, (byte) 0xc6, (byte) 0xc6,
                    (byte) 0xc6, (byte) 0xc6, (byte) 0xc6, 0x00, 0x00, 0x00,
                    0x00 },
            { 0x00, 0x00, 0x18, 0x3c, 0x3c, 0x66, 0x66, (byte) 0xc3,
                    (byte) 0xc3, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, (byte) 0xc3, (byte) 0xe7, (byte) 0xff, (byte) 0xdb,
                    (byte) 0xc3, (byte) 0xc3, (byte) 0xc3, 0x00, 0x00, 0x00,
                    0x00 },
            { 0x00, 0x00, (byte) 0xc3, 0x66, 0x3c, 0x18, 0x3c, 0x66,
                    (byte) 0xc3, 0x00, 0x00, 0x00, 0x00 },
            { (byte) 0xc0, 0x60, 0x60, 0x30, 0x18, 0x3c, 0x66, 0x66,
                    (byte) 0xc3, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, (byte) 0xff, 0x60, 0x30, 0x18, 0x0c, 0x06,
                    (byte) 0xff, 0x00, 0x00, 0x00, 0x00 },
            { 0x00, 0x00, 0x0f, 0x18, 0x18, 0x18, 0x38, (byte) 0xf0, 0x38,
                    0x18, 0x18, 0x18, 0x0f },
            { 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18,
                    0x18, 0x18 },
            { 0x00, 0x00, (byte) 0xf0, 0x18, 0x18, 0x18, 0x1c, 0x0f, 0x1c,
                    0x18, 0x18, 0x18, (byte) 0xf0 },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, (byte) 0x8f,
                    (byte) 0xf1, 0x60, 0x00, 0x00, 0x00 } };
    private ByteBuffer rastersBuf = //
    BufferUtil.newByteBuffer(rasters.length * rasters[0].length);
    {
        for (int i = 0; i < rasters.length; i++)
            rastersBuf.put((byte[]) rasters[i]);// put each array into byte
        // buffer
        rastersBuf.rewind();
    }

    public void dispose(GLAutoDrawable arg0) {
         
    }
}
