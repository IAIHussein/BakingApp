package com.iaihussein.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.iaihussein.bakingapp", appContext.getPackageName());
//    }
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void buttonShouldUpdateText(){
        onView(withId(R.id.main_grid)).perform(click());
        onData((IsInstanceOf.instanceOf(Recipe.class))).atPosition(0).perform(click());
    }
    @Rule
    public ActivityTestRule<ItemListActivity> mActivity2Rule =
            new ActivityTestRule<>(ItemListActivity.class);

    @Test
    public void testSample(){
        if (getRVcount() > 0){
            onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }
    }

    private int getRVcount(){
        RecyclerView recyclerView = (RecyclerView) mActivity2Rule.getActivity().findViewById(R.id.item_list);
        return recyclerView.getAdapter().getItemCount();
    }
}
