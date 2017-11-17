package com.example.vlatko.musicalnotes;

/**
 * Created by vlatko on 28-Oct-17.
 */

public class Note {
    private final int c_pitch_A = 220;
    private final int c_base = 2;
    private final int c_12_tone_pitch = 12;
    private final int c_half_spets = 45;

    private final int c_pause = -1;

    private final float c_torial_fault = (float) 10 / 100;

    private final int c_bpm = 60;

    private float m_note_start;
    private float m_note_end;

    private int m_tempo;

    private float m_pitchInHz;

    public Note() {
        this.m_tempo = c_bpm;
        this.m_note_start = 0;
        this.m_note_end = 0;
        this.m_pitchInHz = c_pause;
    }

    public void setPitchInHz(float pitchInHz) {
        this.m_pitchInHz = pitchInHz;
    }

    public void setNoteStart(float start) {
        m_note_start = start;
    }

    public void setNoteEnd(float end) {
        this.m_note_end = end;
    }

    private float noteDuration() {
        return m_note_end - m_note_start;
    }

    private String noteLength() {
        final float whole_note = (float) c_bpm/m_tempo * 4;
        final float half_note = (float) c_bpm/m_tempo * 2;
        final float quarter_note = (float) c_bpm/m_tempo;
        final float eighth_note = (float) c_bpm/m_tempo / 2;
        final float sixteenth_note = (float) c_bpm/m_tempo / 4;

        final float whole_note_border = whole_note - (whole_note - half_note) * c_torial_fault;
        final float half_note_border = half_note - (half_note - quarter_note) * c_torial_fault;
        final float quarter_note_border = quarter_note - (quarter_note - eighth_note) * c_torial_fault;
        final float eighth_note_border = eighth_note - (eighth_note - sixteenth_note) * c_torial_fault;

        final float note_duration = noteDuration();

        if (note_duration >= whole_note_border)
            return "whole_note";
        if (note_duration >= half_note_border)
            return "half_note";
        if (note_duration >= quarter_note_border)
            return "quarter_note";
        if (note_duration >= eighth_note_border)
            return "eighth_note";
        if (!isRest())
            return "eighth_note";
        return "pause";
    }

    private double log(float x, float base) {
        return (Math.log(x) / Math.log(base));
    }

    private int number_of_half_steps(float pitchInHz) {
        return (int) Math.round(c_12_tone_pitch * log(pitchInHz / c_pitch_A, c_base));
    }

    private String note() {
        if (m_pitchInHz != c_pause) {
            int number_of_half_steps = number_of_half_steps(m_pitchInHz);

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
        else {
            return "rest";
        }
    }

    private int getOctave(float pitchInHz) {
        return (number_of_half_steps(pitchInHz) + c_half_spets) / c_12_tone_pitch - 1;
    }

    public boolean isRest() {
        if (note().compareTo("rest") == 0)
            return true;
        return false;
    }

    public String getNoteName() {
        if (note().compareTo("rest") != 0) {
            return note().toLowerCase() + getOctave(m_pitchInHz);
        }
        return "rest";
    }

    public String getImageName() {
        if (noteLength().compareTo("pause") != 0) {
            if (!isRest()) {
                return note().toLowerCase() + getOctave(m_pitchInHz) + "_" + noteLength();
            }
            return noteLength() + "_" + note().toLowerCase();
        }
        return "pause";
    }
}
