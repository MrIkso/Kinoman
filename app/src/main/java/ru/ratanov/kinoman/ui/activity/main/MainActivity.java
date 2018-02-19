package ru.ratanov.kinoman.ui.activity.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import ru.ratanov.kinoman.R;
import ru.ratanov.kinoman.model.query.FilterParams;
import ru.ratanov.kinoman.model.utils.QueryPreferences;
import ru.ratanov.kinoman.model.views.LabelledSpinner;
import ru.ratanov.kinoman.ui.activity.base.BaseActivity;
import ru.ratanov.kinoman.ui.fragment.main.TopFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    private static TabLayout mTabs;
    private static Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        mTabs = (TabLayout) findViewById(R.id.tabs);
        mTabs.setupWithViewPager(viewPager);
        // Setup ToolBar
        setupToolBar();
        setupSearchView();
        // Init Realm
        Realm.init(this);
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new Adapter(getSupportFragmentManager());

        String cat1 = QueryPreferences.getStoredQuery(this, "title_tab_1", "1");
        String cat2 = QueryPreferences.getStoredQuery(this, "title_tab_2", "2");
        String cat3 = QueryPreferences.getStoredQuery(this, "title_tab_3", "3");

        String title1 = FilterParams.getTabTitle(cat1);
        String title2 = FilterParams.getTabTitle(cat2);
        String title3 = FilterParams.getTabTitle(cat3);

        mAdapter.addFragment(TopFragment.newInstance("1"), title1);
        mAdapter.addFragment(TopFragment.newInstance("2"), title2);
        mAdapter.addFragment(TopFragment.newInstance("3"), title3);

        viewPager.setAdapter(mAdapter);
    }


    // Adapter for ViewPager
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            new FilterDialogFragment().show(getSupportFragmentManager(), "filter");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class FilterDialogFragment extends BottomSheetDialogFragment {

        @BindView(R.id.category_spinner) LabelledSpinner mCategorySpinner;
        @BindView(R.id.year_spinner) LabelledSpinner mYearSpinner;
        @BindView(R.id.country_spinner) LabelledSpinner mCountrySpinner;
        @BindView(R.id.format_spinner) LabelledSpinner mFormatSpinner;
        @BindView(R.id.added_spinner) LabelledSpinner mAddedSpinner;
        @BindView(R.id.sort_spinner) LabelledSpinner mSortSpinner;

        private String mCategory;
        private String mYear;
        private String mCountry;
        private String mFormat;
        private String mAdded;
        private String mSort;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.main_bottom_sheet_filter, container, false);

            ButterKnife.bind(this, view);

            initSpinner(mCategorySpinner, "Раздел", FilterParams.CATEGORY_PARAMS);
            initSpinner(mYearSpinner, "Год выпуска", FilterParams.YEAR_PARAMS);
            initSpinner(mCountrySpinner, "Страна", FilterParams.COUNTRY_PARAMS);
            initSpinner(mFormatSpinner, "Формат", FilterParams.FORMAT_PARAMS);
            initSpinner(mAddedSpinner, "Добавлен", FilterParams.ADDED_PARAMS);
            initSpinner(mSortSpinner, "Сортировать", FilterParams.SORT_PARAMS);

            final int page = mTabs.getSelectedTabPosition() + 1;
            System.out.println("Tab = " + page);

            final String category_pref_key = "title_tab_" + page;
            final String year_pref_key = "year_tab_" + page;
            final String country_pref_key = "country_tab_" + page;
            final String format_pref_key = "format_tab_" + page;
            final String added_pref_key = "added_tab_" + page;
            final String sort_pref_key = "sort_tab_" + page;

            mCategory = QueryPreferences.getStoredQuery(getActivity(), category_pref_key, String.valueOf(page));
            mYear = QueryPreferences.getStoredQuery(getActivity(), year_pref_key, "0");
            mCountry = QueryPreferences.getStoredQuery(getActivity(), country_pref_key, "0");
            mFormat = QueryPreferences.getStoredQuery(getActivity(), format_pref_key, "0");
            mAdded = QueryPreferences.getStoredQuery(getActivity(), added_pref_key, "0");
            mSort = QueryPreferences.getStoredQuery(getActivity(), sort_pref_key, "0");

            mCategorySpinner.setSelection(FilterParams.getCurrentPosition(FilterParams.CATEGORY_PARAMS, mCategory));
            mYearSpinner.setSelection(FilterParams.getCurrentPosition(FilterParams.YEAR_PARAMS, mYear));
            mCountrySpinner.setSelection(FilterParams.getCurrentPosition(FilterParams.COUNTRY_PARAMS, mCountry));
            mFormatSpinner.setSelection(FilterParams.getCurrentPosition(FilterParams.FORMAT_PARAMS, mFormat));
            mAddedSpinner.setSelection(FilterParams.getCurrentPosition(FilterParams.ADDED_PARAMS, mAdded));
            mSortSpinner.setSelection(FilterParams.getCurrentPosition(FilterParams.SORT_PARAMS, mSort));

            Button updateButton = (Button) view.findViewById(R.id.bottom_sheet_update_button);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    QueryPreferences.setStoredQuery(getActivity(), category_pref_key, mCategory);
                    QueryPreferences.setStoredQuery(getActivity(), year_pref_key, mYear);
                    QueryPreferences.setStoredQuery(getActivity(), country_pref_key, mCountry);
                    QueryPreferences.setStoredQuery(getActivity(), format_pref_key, mFormat);
                    QueryPreferences.setStoredQuery(getActivity(), added_pref_key, mAdded);
                    QueryPreferences.setStoredQuery(getActivity(), sort_pref_key, mSort);

                    System.out.println("Save: " + category_pref_key + " = " + mCategory);

                    mTabs.getTabAt(page - 1).setText(FilterParams.getTabTitle(mCategory));
                    TopFragment fragment = (TopFragment) mAdapter.getItem(page - 1);
                    fragment.update();

                    dismiss();
                }
            });

            return view;
        }

        private void initSpinner(LabelledSpinner spinner, String label, final ArrayList<FilterParams> paramsArrayList) {

            spinner.setItemsArray(paramsArrayList);
            spinner.setLabelText(label);
            spinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                @Override
                public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                    FilterParams filterParams = (FilterParams) adapterView.getSelectedItem();
                    switch (filterParams.getParamKey()) {
                        case "t":
                            mCategory = filterParams.getId();
                            break;
                        case "d":
                            mYear = filterParams.getId();
                            break;
                        case "k":
                            mCountry = filterParams.getId();
                            break;
                        case "f":
                            mFormat = filterParams.getId();
                            break;
                        case "w":
                            mAdded = filterParams.getId();
                            break;
                        case "s":
                            mSort = filterParams.getId();
                            break;
                    }
                }

                @Override
                public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

                }
            });
        }

    }


}
