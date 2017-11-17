package com.example.vlatko.musicalnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private TextView secondsProcessed;

    private ImageView mImageView;

    private final int c_sampleRate = 22050;
    private final int c_bufferRate = 1024;
    private final int c_number_of_notes = 8;

    private Thread recordThread;

    private int m_note_number = 0;

    AudioDispatcher m_dispatcher;

    private Note m_note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(c_sampleRate,c_bufferRate,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();

                pitchText = (TextView)findViewById(R.id.note_pith);
                noteText = (TextView)findViewById(R.id.note_text);
                secondsProcessed = (TextView)findViewById(R.id.seconds_processed);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Note new_note = new Note();
                        new_note.setPitchInHz(pitchInHz);
                        pitchText.setText("" + pitchInHz);
                        noteText.setText(new_note.getNoteName());
                        secondsProcessed.setText("" + m_dispatcher.secondsProcessed());

                        noteProcessing(m_dispatcher, pitchInHz);

                    }
                });
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, c_sampleRate, c_bufferRate, pdh);
        m_dispatcher.addAudioProcessor(p);
        recordThread = new Thread(m_dispatcher,"Audio Dispatcher");
        recordThread.start();
    }

    private void noteProcessing(AudioDispatcher dispatcher, float pitchInHz) {
        Note new_note = new Note();
        new_note.setPitchInHz(pitchInHz);
        if (new_note.isRest() && m_note.isRest()) {
            //do noting
        }
        if (new_note.isRest() && !m_note.isRest()) {
            switchToNewNote(dispatcher, pitchInHz);

        }
        if (!new_note.isRest() && m_note.isRest()) {
            switchToNewNote(dispatcher, pitchInHz);
        }
        if (!new_note.isRest() && !m_note.isRest()) {
            if (new_note.getNoteName().compareTo(m_note.getNoteName()) != 0) {
                switchToNewNote(dispatcher, pitchInHz);
            }
        }
    }

    private void switchToNewNote(AudioDispatcher dispatcher, float pitchInHz) {
        m_note.setNoteEnd(dispatcher.secondsProcessed());
        setNoteImage();
        m_note.setPitchInHz(pitchInHz);
        m_note.setNoteStart(dispatcher.secondsProcessed());
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

    private void setNote(){
        int id = getResources().getIdentifier(m_note.getImageName(), "drawable", getPackageName());
        if (id != 0) {
            mImageView.setImageResource(id);
        }
    }

    private void setNoteImage() {
        if (m_note.getImageName().compareTo("pause") != 0) {
            prepareNoteImage();
            setNote();
        }

    }

    public void onStart() {
        super.onStart();
        
        //myLocationListener.start();
    }

    public void onStop() {
        super.onStop();
        if (this.m_dispatcher != null)
        {
            this.m_dispatcher.stop();
        }

        if (this.recordThread != null)
        {
            this.recordThread.stop();
        }
    }



}
