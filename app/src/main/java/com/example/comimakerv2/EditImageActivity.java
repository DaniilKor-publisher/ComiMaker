package com.example.comimakerv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comimakerv2.adapters.EditingToolsAdapter;
import com.example.comimakerv2.base.BaseActivity;
import com.example.comimakerv2.filters.FilterListener;
import com.example.comimakerv2.filters.FilterViewAdapter;
import com.example.comimakerv2.fragments.StickerBSFragment;
import com.example.comimakerv2.fragments.TextEditorDialogFragment;
import com.example.comimakerv2.helpers.TemplateDatabaseHelper;
import com.example.comimakerv2.myClasses.MyAdjusts;
import com.example.comimakerv2.tools.ToolType;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener, EditingToolsAdapter.OnItemSelected, FilterListener
        , StickerBSFragment.StickerListener {

    public static final String PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE";
    private static final String TAG = EditImageActivity.class.getSimpleName();

    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private RecyclerView mRvTools, mRvFilters;
    PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    private ConstraintLayout mRootView;
    private StickerBSFragment mStickerBSFragment;

    TextView tNumber;
    ImageView imgNextFrame, imgPreviousFrame, imgUndo, imgRedo, imgClose, imgSave;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private TemplateDatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    String[] frameOptions = {"Новый кадр", "Удалить кадр", "Перейти на первый кадр", "Перейти на последний кадр"};
    private ListView mLvFrameOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        mRootView = findViewById(R.id.editImageRootView);
        mDBHelper = new TemplateDatabaseHelper(this);

        initViews();

        mRvTools = findViewById(R.id.rvConstraintTools);
        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        mRvFilters = findViewById(R.id.rvFilterView);
        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);

        mStickerBSFragment = new StickerBSFragment(getApplicationContext(), getContentResolver());
        mStickerBSFragment.setStickerListener(this);

        boolean pinchTextScalable = getIntent().getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true);

        mPhotoEditorView = findViewById(R.id.imageEditorView);
        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(pinchTextScalable)
                .build();

        mPhotoEditor.setOnPhotoEditorListener(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    private void initViews() {
        tNumber = findViewById(R.id.txtNumber);

        imgNextFrame = findViewById(R.id.imgNext);
        imgNextFrame.setOnClickListener(this);

        imgPreviousFrame = findViewById(R.id.imgPrevious);
        imgPreviousFrame.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                Toast.makeText(EditImageActivity.this, "В разработке", Toast.LENGTH_SHORT);
                break;

            case R.id.imgClose:
                Intent i = new Intent(EditImageActivity.this, CreatureActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onEditTextChangeListener(View view, String s, int i) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode, int fontCode) {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);

                        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), fontCode);
                        styleBuilder.withTextFont(typeface);

                        mPhotoEditor.addText(inputText, styleBuilder);
                    }
                });
                break;
            case FILTER:
                showFilter(true);
                break;

            case BUBBLE_SPEECH:
                showChooseBubbleSpeechDialogWindow();
                break;

            case STICKER:
                showBottomSheetDialogFragment(mStickerBSFragment);
                break;
            case FRAMES:
                showFramesChooseDialog();
                break;
        }
    }

    private void showFramesChooseDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View chooseFrameOption = getLayoutInflater().inflate(R.layout.choose_frame_window, null);

        dialogBuilder.setView(chooseFrameOption);
        dialog = dialogBuilder.create();
        dialog.show();

        mLvFrameOptions = chooseFrameOption.findViewById(R.id.lvFrames);

        mLvFrameOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                }
            }
        });
    }

    private void showChooseBubbleSpeechDialogWindow() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View chooseBubble = getLayoutInflater().inflate(R.layout.add_bubble_speech, null);

        dialogBuilder.setView(chooseBubble);
        dialog = dialogBuilder.create();
        dialog.show();

        TextView tInComingBubble = chooseBubble.findViewById(R.id.txtIncomingBubble);
        TextView tOutGoingBubble = chooseBubble.findViewById(R.id.txtOutGoingBubble);

        tInComingBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(EditImageActivity.this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode, int fontCode) {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);

                        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), fontCode);
                        styleBuilder.withTextFont(typeface);

                        Drawable drawable = getResources().getDrawable(R.drawable.incoming_bubble);
                        styleBuilder.withBackgroundDrawable(drawable);

                        mPhotoEditor.addText(inputText, styleBuilder);
                    }
                });
            }
        });

        tOutGoingBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(EditImageActivity.this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode, int fontCode) {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);

                        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), fontCode);
                        styleBuilder.withTextFont(typeface);

                        Drawable drawable = getResources().getDrawable(R.drawable.outgoing_bubble);
                        styleBuilder.withBackgroundDrawable(drawable);

                        mPhotoEditor.addText(inputText, styleBuilder);
                    }
                });
            }
        });
    }

    @Override
    public void onFilterSelected(PhotoFilter filter) {
        mPhotoEditor.setFilterEffect(filter);
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }


    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
        }
    }

    @Override
    public void onStickerClick(Bitmap bitmap, String url) {
        if (getChosenTemplateCategory(url).equals("backgrounds")) {
            mPhotoEditor.clearAllViews();
            mPhotoEditorView.getSource().setImageBitmap(bitmap);
        } else {
            mPhotoEditor.addImage(bitmap);
        }
    }

    private void showBottomSheetDialogFragment(BottomSheetDialogFragment fragment) {
        if (fragment == null || fragment.isAdded()) {
            return;
        }
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    public String getChosenTemplateCategory(String url) {
        Cursor c = null;
        try {
            String query = "select category from favourites where image_link = ?";
            c = mDb.rawQuery(query, new String[]{url});
            if (c.moveToFirst()) {
                return c.getString(0);
            }
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
}