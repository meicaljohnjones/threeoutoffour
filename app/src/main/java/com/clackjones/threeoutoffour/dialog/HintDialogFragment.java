package com.clackjones.threeoutoffour.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.model.InsufficientCoinScoreException;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;

public class HintDialogFragment extends DialogFragment {
    private ThreeOutOfFourGame game;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.hints)
                .setItems(R.array.hint_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                // remove a letter
                                try {
                                    game.performRemoveALetterHint();
                                } catch (InsufficientCoinScoreException e) {
                                    showToast();
                                }
                            default:
                                return;
                        }
                    }
                });
        return builder.create();
    }

    public void setGame(ThreeOutOfFourGame game) {
        this.game = game;
    }

    public ThreeOutOfFourGame getGame() {
        return game;
    }

    private void showToast() {
        Toast toast = Toast.makeText(this.getContext(),
                R.string.insufficient_credit,
                Toast.LENGTH_SHORT);

        toast.show();
    }

}
