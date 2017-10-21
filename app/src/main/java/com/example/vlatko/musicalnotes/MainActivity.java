package com.example.vlatko.musicalnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity {

    private TextView pitchText;
    private TextView noteText;
    private int m_number_of_half_steps;
    private final int c_pitch_A = 220;
    private final int c_base = 2;
    private final int c_12_tone_pitch = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TextView text = (TextView) findViewById(R.id.textView1);
                        // text.setText("" + pitchInHz);
                        processPitch(pitchInHz);
                    }
                });
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    private double log(float x, float base) {
        return (Math.log(x) / Math.log(base));
    }

    private int number_of_half_steps(float pitchInHz) {
        return (int) Math.round(c_12_tone_pitch * log(pitchInHz / c_pitch_A, c_base));
    }

    public void processPitch(float pitchInHz) {

        //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        pitchText = (TextView) findViewById(R.id.note_pith);
        noteText = (TextView) findViewById(R.id.note_text);

        pitchText.setText("" + pitchInHz);

        int m_number_of_half_steps = number_of_half_steps(pitchInHz);
        //noteText.setText("" + m_number_of_half_steps);

        switch (m_number_of_half_steps % c_12_tone_pitch) {
            case -11://Bb
                noteText.setText("Bb");
                break;
            case -10://B
                noteText.setText("B");
                break;
            case -9://C
                noteText.setText("C");
                break;
            case -8://C#
                noteText.setText("C#");
                break;
            case -7://D
                noteText.setText("D");
                break;
            case -6://Eb
                noteText.setText("Eb");
                break;
            case -5://E
                noteText.setText("E");
                break;
            case -4://F
                noteText.setText("F");
                break;
            case -3://F#
                noteText.setText("F#");
                break;
            case -2://G
                noteText.setText("G");
                break;
            case -1://G#
                noteText.setText("G#");
                break;
            case 0://A
                noteText.setText("A");
                break;
            case 1://Bb
                noteText.setText("Bb");
                break;
            case 2://B
                noteText.setText("B");
                break;
            case 3://C
                noteText.setText("C");
                break;
            case 4://C#
                noteText.setText("C#");
                break;
            case 5://D
                noteText.setText("D");
                break;
            case 6://Eb
                noteText.setText("Eb");
                break;
            case 7://E
                noteText.setText("E");
                break;
            case 8://F
                noteText.setText("F");
                break;
            case 9://F#
                noteText.setText("F#");
                break;
            case 10://G
                noteText.setText("G");
                break;
            case 11://G#
                noteText.setText("G#");
                break;
            default:
                noteText.setText("ERROR!!!" + m_number_of_half_steps % c_12_tone_pitch);
        }
        /*if(pitchInHz >= 110 && pitchInHz < 123.47) {
            //A
            noteText.setText("A");
        }
        else if(pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
            noteText.setText("B");
        }
        else if(pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
            noteText.setText("C");
        }
        else if(pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
            noteText.setText("D");
        }
        else if(pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
            noteText.setText("E");
        }
        else if(pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
            noteText.setText("F");
        }
        else if(pitchInHz >= 185 && pitchInHz < 196) {
            //G
            noteText.setText("G");
        }*/
    }
}
