/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

/**
 *
 * @author Catheryne
 */
import codec.Coder;
import codec.Decoder;
import codec.Ld8k;
import codec.Util;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author Catheryne
 */
public class Capture extends Ld8k {

    private boolean running;
    ByteArrayOutputStream out;
    ByteArrayOutputStream codificado = new ByteArrayOutputStream();
    ByteArrayOutputStream decodificado = new ByteArrayOutputStream();

    /* public void captureAudio() {
    try {

    final AudioFormat format = getFormat();
    DataLine.Info info = new DataLine.Info(
    TargetDataLine.class, format);
    final TargetDataLine line = (TargetDataLine)
    AudioSystem.getLine(info);
    line.open(format);
    line.start();
    Runnable runner = new Runnable() {
    int bufferSize = (int)format.getSampleRate()
     * format.getFrameSize();
    byte buffer[] = new byte[bufferSize];

    public void run() {
    out = new ByteArrayOutputStream();
    running = true;

    InputStream f_speech= new ByteArrayInputStream(buffer);                     /* Speech data
    ByteArrayOutputStream f_serial= new ByteArrayOutputStream();                     /* Serial bit stream

    short[] sp16 = new short[L_FRAME];         /* Buffer to read 16 bits speech
    short[] serial = new short[SERIAL_SIZE];   /* Output bit stream buffer

    int   frame;
    /*-----------------------------------------------------------------------*
     * Open speech file and result file (output serial bit stream)           *
     *-----------------------------------------------------------------------


    /*-------------------------------------------------*
     * Initialization of the coder.                    *
     *-------------------------------------------------

    Coder coder = new Coder();

    try {
    while (running) {
    int count =line.read(buffer, 0, buffer.length);
    f_speech= new ByteArrayInputStream(buffer);
    if (count > 0) {
    //*******************************************************************************



    /*-------------------------------------------------------------------------*
     * Loop for every analysis/transmission frame.                             *
     * -New L_FRAME data are read. (L_FRAME = number of speech data per frame) *
     * -Conversion of the speech data from 16 bit integer to real              *
     * -Call cod_ld8k to encode the speech.                                    *
     * -The compressed serial output stream is written to a file.              *
     * -The synthesis speech is written to a file                              *
     *-------------------------------------------------------------------------*
    

    frame=0;
    while(Util.fread(sp16, L_FRAME, f_speech) == L_FRAME){
    frame++;
    System.out.printf(" Frame: %d\r", frame);

    coder.process(sp16, serial);

    Util.fwrite(serial, SERIAL_SIZE,f_serial);
    out.write(f_serial.toByteArray());
    System.out.println("Codec");
    for(int i=0;i<15;i++)
    System.out.print(" "+out.toByteArray()[i]);
    out.close();
    }

    //f_serial.close();
    //f_speech.close();





    //*******************************************************************************


    }

    }
    System.out.println("Buffer");
    for(int i=0;i<15;i++)
    System.out.print(" "+buffer[i]);

    out.close();
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
    }*/
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
                        codificado = new Coder().mainCodec(out.toByteArray());
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

    /* public void playAudio() {
    try {
    byte audio[] = codificado.toByteArray();



    ByteArrayOutputStream f_syn;
    InputStream f_serial;

    short[]  serial = new short[SERIAL_SIZE];             /* Serial stream
    short[] sp16 = new short[L_FRAME];         /* Buffer to write 16 bits speech

    int   frame;



    /* Open file for synthesis and packed serial stream


    f_serial = new ByteArrayInputStream(audio);



    f_syn = new ByteArrayOutputStream();



    /*-----------------------------------------------------------------*
     *           Initialization of decoder                             *
     *-----------------------------------------------------------------

    Decoder decoder = new Decoder();

    /*-----------------------------------------------------------------*
     *            Loop for each "L_FRAME" speech data                  *
     *-----------------------------------------------------------------

    frame =0;
    while(Util.fread(serial, SERIAL_SIZE, f_serial) == SERIAL_SIZE)
    {
    frame++;
    System.out.printf(" Frame: %d\r", frame);

    decoder.process(serial, sp16);

    Util.fwrite(sp16, L_FRAME, f_syn);
    }

    f_syn.close();
    f_serial.close();





    //*********************************************************************************************
    InputStream input =
    new ByteArrayInputStream(f_syn.toByteArray());
    final AudioFormat format = getFormat();
    final AudioInputStream ais =
    new AudioInputStream(input, format,
    f_syn.toByteArray().length / format.getFrameSize());
    DataLine.Info info = new DataLine.Info(
    SourceDataLine.class, format);
    final SourceDataLine line = (SourceDataLine)
    AudioSystem.getLine(info);
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
    }   catch (IOException ex) {
    Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
    } catch (LineUnavailableException e) {
    System.err.println("Line unavailable: " + e);
    System.exit(-4);
    }
    }*/
    public void playAudio() {
        try {
            try {
                decodificado = new Decoder().mainCodec(codificado.toByteArray());
            } catch (IOException ex) {
                Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
            }

            byte audio[] = decodificado.toByteArray();
            byte audio2[] = out.toByteArray();

            InputStream input =
                    new ByteArrayInputStream(audio2);
            final AudioFormat format = getFormat();
            final AudioInputStream ais =
                    new AudioInputStream(input, format,
                    audio2.length / format.getFrameSize());
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
