package com.oliver_curtis.firestoreexampleproject


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.oliver_curtis.firestoreexampleproject.utils.performClick
import com.oliver_curtis.firestoreexampleproject.utils.performTypeText
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
private const val NOTE_ADDED_SUCCESS = "Note Added"
private const val NOTE_ADD_ALERT = "Please add a title and a description"

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
@LargeTest
class NoteEntityInstrumentedTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun addNoteSuccess() {

        R.id.edit_text_title.performTypeText("Title Test One")
        R.id.edit_text_description.performTypeText("Description Test Two")
        R.id.add_note_button.performClick()

        verifyNoteSuccessfullyAdded()
    }

    private fun verifyNoteSuccessfullyAdded() {
        this.verifyIntentWithToast(NOTE_ADDED_SUCCESS)
    }

    @Test
    fun notifyBothTitleAndDescriptionNeedToBeCompleted(){

        R.id.edit_text_title.performTypeText("Title Test One")
        R.id.edit_text_description.performTypeText("")
        R.id.add_note_button.performClick()

        verifyNoteAddAlert()

    }

    private fun verifyNoteAddAlert(){
        this.verifyIntentWithToast(NOTE_ADD_ALERT)
    }

    private fun verifyIntentWithToast(value: String) {

        onView(withText(value))
            .inRoot(withDecorView(not(activityTestRule.activity.window.decorView)))
            .check(matches(isDisplayed()));

    }
}
