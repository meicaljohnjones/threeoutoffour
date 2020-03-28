package com.clackjones.threeoutoffour.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;

public class ResetGameDialogFragment extends DialogFragment {
    private ThreeOutOfFourGame game;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_reset_game)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        game.restartGame();
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
}
