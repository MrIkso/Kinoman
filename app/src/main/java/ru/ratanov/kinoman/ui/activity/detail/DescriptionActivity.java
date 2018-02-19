package ru.ratanov.kinoman.ui.activity.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.ui.activity.base.BaseActivity;

public class DescriptionActivity extends BaseActivity {

    public static final String EXTRA_DESCRIPTION = "extra_description";

    @BindView(R.id.description_activity_textview)
    TextView mTextView;

    public static Intent newIntent(Context context, String description) {
        Intent intent = new Intent(context, DescriptionActivity.class);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_description);
        ButterKnife.bind(this);

        mTextView.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));

        setupToolBar();
        setupSearchView();
    }

}
