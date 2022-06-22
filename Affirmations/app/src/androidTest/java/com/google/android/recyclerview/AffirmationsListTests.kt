//package com.google.android.recyclerview
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import org.junit.Rule
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class AffirmationsListTests {
//
//    @get:Rule
//    val activity = ActivityScenarioRule(MainActivity::class.java)
//
//    onView(withId(R.id.recycler_view)).perform(
//    RecyclerViewActions
//    .scrollTo<RecyclerView.ViewHolder>(
//    withText(R.string.affirmation10)
//    )
//    )
//
//    onView(withText(R.string.affirmation10))
//    .check(matches(isDisplayed())
//    )
//}