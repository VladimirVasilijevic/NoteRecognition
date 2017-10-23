package com.example.vlatko.musicalnotes;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView mImageView;

    private final int c_pitch_A = 220;
    private final int c_base = 2;
    private final int c_12_tone_pitch = 12;
    private final int c_sampleRate = 22050;
    private final int c_bufferRate = 1024;
    private final int c_number_of_notes = 8;
    private final int c_half_spets = 45;

    private int m_note_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(c_sampleRate,c_bufferRate,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, c_sampleRate, c_bufferRate, pdh);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    private double log(float x, float base) {
        return (Math.log(x) / Math.log(base));
    }

    private int number_of_half_steps(float pitchInHz) {
        return (int) Math.round(c_12_tone_pitch * log(pitchInHz / c_pitch_A, c_base));
    }

    private String note(float pitchInHz) {
        int number_of_half_steps = number_of_half_steps(pitchInHz);

        switch (number_of_half_steps % c_12_tone_pitch) {
            case -11://Bb
                return "Bb";
            case -10://B
                return "B";
            case -9://C
                return "C";
            case -8://C#
                return "Ch";
            case -7://D
                return "D";
            case -6://Eb
                return "Eb";
            case -5://E
                return "E";
            case -4://F
                return "F";
            case -3://F#
                return "Fh";
            case -2://G
                return "G";
            case -1://G#
                return "Gh";
            case 0://A
                return "A";
            case 1://Bb
                return "Bb";
            case 2://B
                return "B";
            case 3://C
                return "C";
            case 4://C#
                return "Ch";
            case 5://D
                return "D";
            case 6://Eb
                return "Eb";
            case 7://E
                return "E";
            case 8://F
                return "F";
            case 9://F#
                return "Fh";
            case 10://G
                return "G";
            case 11://G#
                return "Gh";
            default:
                return ("ERROR!!!");
        }
    }

    public void processPitch(float pitchInHz) {

        if (pitchInHz != -1) {
            //View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            pitchText = (TextView) findViewById(R.id.note_pith);
            noteText = (TextView) findViewById(R.id.note_text);

            pitchText.setText("" + pitchInHz);

            noteText.setText(note(pitchInHz));

            setNoteImage(pitchInHz);
        }
    }

    private void prepareNoteImage() {
        switch (m_note_number % c_number_of_notes) {
            case 0:
                mImageView = (ImageView)findViewById(R.id.note_0);
                m_note_number++;
                break;
            case 1:
                mImageView = (ImageView)findViewById(R.id.note_1);
                m_note_number++;
                break;
            case 2:
                mImageView = (ImageView)findViewById(R.id.note_2);
                m_note_number++;
                break;
            case 3:
                mImageView = (ImageView)findViewById(R.id.note_3);
                m_note_number++;
                break;
            case 4:
                mImageView = (ImageView)findViewById(R.id.note_4);
                m_note_number++;
                break;
            case 5:
                mImageView = (ImageView)findViewById(R.id.note_5);
                m_note_number++;
                break;
            case 6:
                mImageView = (ImageView)findViewById(R.id.note_6);
                m_note_number++;
                break;
            case 7:
                mImageView = (ImageView)findViewById(R.id.note_7);
                m_note_number++;
                break;
        }
    }

    private int getOctave(float pitchInHz) {
        return (number_of_half_steps(pitchInHz) + c_half_spets) / c_12_tone_pitch;
    }

    private void setNote(float pitchInHz){

        int id = getResources().getIdentifier(note(pitchInHz).toLowerCase()+getOctave(pitchInHz), "drawable", getPackageName());
        mImageView.setImageResource(id);
    }

    private void setNoteImage(float pitchInHz) {

        prepareNoteImage();

        setNote(pitchInHz);

    }
}
