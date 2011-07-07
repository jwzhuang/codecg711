/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

/**
 *
 * @author Catheryne
 */
import codecg711.Ld8k;
import java.io.*;
import javax.sound.sampled.*;
import org.mobicents.media.server.impl.dsp.audio.g711.ulaw.Decoder;
import org.mobicents.media.server.impl.dsp.audio.g711.ulaw.Encoder;
import org.mobicents.media.server.spi.memory.*;

/**
 *
 * @author Catheryne
 */
public class Capture extends Ld8k {

    private boolean running;
    ByteArrayOutputStream out;
    ByteArrayOutputStream codificado = new ByteArrayOutputStream();
    ByteArrayOutputStream decodificado = new ByteArrayOutputStream();

    Frame frmCodificado;
    Frame frmDecodificado;

    public void captureAudio() {
        try {
            final AudioFormat format = getFormat();
            DataLine.Info info = new DataLine.Info(
                    TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {

                int bufferSize = (int) format.getSampleRate()
                        * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    out = new ByteArrayOutputStream();
                    running = true;
                    try {
                        while (running) {
                            int count =
                                    line.read(buffer, 0, buffer.length);
                            if (count > 0) {

                                out.write(buffer, 0, count);
                            }
                        }
                        out.close();
                        Frame f = new Frame(null, out.toByteArray());
                        f.setLength(out.toByteArray().length);
                        frmCodificado = new Encoder().process(f);
                        codificado = new ByteArrayOutputStream(frmCodificado.getData().length);
                        codificado.write(frmCodificado.getData(), 0, frmCodificado.getData().length);
                        codificado.close();
                    } catch (IOException e) {
                        System.err.println("I/O problems: " + e);
                        System.exit(-1);
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
            System.exit(-2);
        }
    }

    public void playAudio() {
        try {
            frmDecodificado = new Decoder().process(frmCodificado);
            byte audio3[] = frmDecodificado.getData();

            InputStream input =
                    new ByteArrayInputStream(audio3);
            final AudioFormat format = getFormat();
            final AudioInputStream ais =
                    new AudioInputStream(input, format,
                    audio3.length / format.getFrameSize());
            DataLine.Info info = new DataLine.Info(
                    SourceDataLine.class, format);
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            Runnable runner = new Runnable() {

                int bufferSize = (int) format.getSampleRate()
                        * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    try {
                        int count;
                        while ((count = ais.read(
                                buffer, 0, buffer.length)) != -1) {
                            if (count > 0) {
                                line.write(buffer, 0, count);
                            }
                        }
                        line.drain();
                        line.close();
                    } catch (IOException e) {
                        System.err.println("I/O problems: " + e);
                        System.exit(-3);
                    }
                }
            };
            Thread playThread = new Thread(runner);
            playThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
            System.exit(-4);
        }
    }

    public AudioFormat getFormat() {
        float sampleRate = 8000;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate,
                sampleSizeInBits, channels, signed, bigEndian);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
